package com.sap.wishlist.client;

import java.net.URI;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Iterator;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ManagedBean
@Singleton
/** REST Client. Use it for sending requests to the mashup service. */
public class RestClient {
    private static final Client CLIENT = createClient();

    private static final Logger LOG = LoggerFactory.getLogger(RestClient.class);

    private HashMap<String, String> tenantAuthorizationHeaders = new HashMap<String, String>();

    @Inject
    private OAuth2ServiceClient oAuth2Client;

    /** Creates REST client with necessary HTTPS settings */
    private static Client createClient() {

	TrustManager[] certs = new TrustManager[] { new X509TrustManager() {
	    @Override
	    public X509Certificate[] getAcceptedIssuers() {
		return null;
	    }

	    @Override
	    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
	    }

	    @Override
	    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
	    }
	} };
	SSLContext ctx = null;
	try {
	    ctx = SSLContext.getInstance("TLS");
	    ctx.init(null, certs, new SecureRandom());
	} catch (java.security.GeneralSecurityException ex) {
	    LOG.error("Security Exception was thrown:", ex);
	    return null;
	}
	HttpsURLConnection.setDefaultSSLSocketFactory(ctx.getSocketFactory());

	HostnameVerifier hostnameVerifier = new HostnameVerifier() {
	    @Override
	    public boolean verify(String hostname, SSLSession session) {
		return true;
	    }
	};

	return ClientBuilder.newBuilder().sslContext(ctx).hostnameVerifier(hostnameVerifier).build();
    }

    /**
     * Invokes GET operation and adds authorization header for Cloud 4 Builder
     * request.
     */
    public <T> Response get(String tenant, URI target, String[] path,
	    MultivaluedMap<String, String> queryParams,
	    MultivaluedMap<String, Object> header) {
	Response resp = get(target, path, queryParams, addAuthorizationHeader(tenant, header));

	// check response, in case of authentication error retry with new
	// authorization token
	if (resp.getStatus() != Response.Status.UNAUTHORIZED.getStatusCode()) {
	    return resp;
	} else {
	    return get(target, path, queryParams, reNewAuthorizationHeader(tenant, header));
	}
    }

    /**
     * Invokes GET operation.
     */
    public <T> Response get(URI target, String[] path,
	    MultivaluedMap<String, String> queryParams,
	    MultivaluedMap<String, Object> header) {
	final Response response = this.createInvocationBuilder(target, path, queryParams, header).get();
	return response;
    }

    /**
     * Invokes POST operation and adds authorization header for Cloud 4 Builder
     * request.
     */
    public <T> Response post(String tenant, URI target, String[] path,
	    MultivaluedMap<String, String> queryParams,
	    MultivaluedMap<String, Object> header, Entity<T> body) {
	Response resp = post(target, path, queryParams, addAuthorizationHeader(tenant, header), body);

	// check response, in case of authentication error retry with new
	// authorization token
	if (resp.getStatus() != Response.Status.UNAUTHORIZED.getStatusCode()) {
	    return resp;
	} else {
	    return post(target, path, queryParams, reNewAuthorizationHeader(tenant, header), body);
	}
    }

    /**
     * Invokes POST operation.
     */
    public <T> Response post(URI target, String[] path,
	    MultivaluedMap<String, String> queryParams,
	    MultivaluedMap<String, Object> header, Entity<T> body) {
	final Response response = this.createInvocationBuilder(target, path, queryParams, header).post(body);
	return response;
    }

    /**
     * Invokes PUT operation and adds authorization header for Cloud 4 Builder
     * request.
     */
    public <T> Response put(String tenant, URI target, String[] path,
	    MultivaluedMap<String, String> queryParams,
	    MultivaluedMap<String, Object> header, Entity<T> body) {
	Response resp = put(target, path, queryParams, addAuthorizationHeader(tenant, header), body);

	// check response, in case of authentication error retry with new
	// authorization token
	if (resp.getStatus() != Response.Status.UNAUTHORIZED.getStatusCode()) {
	    return resp;
	} else {
	    return put(target, path, queryParams, reNewAuthorizationHeader(tenant, header), body);
	}
    }

    /**
     * Invokes PUT operation.
     */
    public <T> Response put(URI target, String[] path,
	    MultivaluedMap<String, String> queryParams,
	    MultivaluedMap<String, Object> header, Entity<T> body) {
	final Response response = this.createInvocationBuilder(target, path, queryParams, header).put(body);
	return response;
    }

    /**
     * Copy headers and add authorization header for the request
     */
    private MultivaluedMap<String, Object> addAuthorizationHeader(String tenant,
	    MultivaluedMap<String, Object> header) {
	MultivaluedMap<String, Object> headerWithAuth = new MultivaluedHashMap<String, Object>();

	if (header != null) {
	    headerWithAuth.putAll(header);
	}

	String authorizationHeader = this.tenantAuthorizationHeaders.get(tenant);

	if (authorizationHeader == null) {
	    authorizationHeader = oAuth2Client.requestAccessToken(tenant);
	    this.tenantAuthorizationHeaders.put(tenant, authorizationHeader);
	}

	headerWithAuth.add("Authorization", authorizationHeader);
	return headerWithAuth;
    }

    private MultivaluedMap<String, Object> reNewAuthorizationHeader(String tenant,
	    MultivaluedMap<String, Object> header) {
	this.tenantAuthorizationHeaders.remove(tenant);
	return addAuthorizationHeader(tenant, header);
    }

    /**
     * Creates invocation builder for the given URI, path, query parameters and
     * headers
     */
    private Builder createInvocationBuilder(URI uri, String[] path, MultivaluedMap<String, String> queryParams,
	    MultivaluedMap<String, Object> header) {

	CacheControl cacheControl = new CacheControl();
	cacheControl.setNoCache(true);

	WebTarget webTarget = CLIENT.target(uri);
	int nPaths = path.length;
	for (int i = 0; i < nPaths; i++) {
	    webTarget = webTarget.path(path[i]);
	}

	int nQueryParams = path.length;
	for (int i = 0; i < nQueryParams; i++) {
	    webTarget = webTarget.queryParam(path[i]);
	}

	if (queryParams != null) {
	    Iterator<String> iterator = queryParams.keySet().iterator();
	    while (iterator.hasNext()) {
		String key = iterator.next();
		String value = queryParams.getFirst(key);
		webTarget = webTarget.queryParam(key, value);
	    }
	}

	final Builder invocationBuilder = webTarget.request().cacheControl(cacheControl).headers(header);
	return invocationBuilder;

    }
}