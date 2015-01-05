package com.sap.wishlist.client;

import java.net.URI;
import java.net.URISyntaxException;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Value;

import com.sap.wishlist.client.oauth2.Oauth2Token;

@ManagedBean
public class OAuth2ServiceClient {

    private URI uri;
    private String serviceClientID;
    private String serviceClientSecret;
    private String grantType;
    private String scope;

    private static String PATH_REQUEST_TOKEN = "token";

    private static String QUERY_PARAM_CLIENT_ID = "client_id";
    private static String QUERY_PARAM_CLIENT_SECRET = "client_secret";
    private static String QUERY_PARAM_GRANT_TYPE = "grant_type";
    private static String QUERY_PARAM_HYBRIS_TENANT = "hybris-tenant";
    private static String QUERY_PARAM_SCOPE = "scope";

    @Inject
    private RestClient restClient;

    @Value("${oauth2_client.uri}")
    public void setUri(String uri) throws URISyntaxException {
	this.uri = new URI(uri);
    }

    @Value("${service.client_id}")
    public void setServiceClientID(String serviceClientID) {
	this.serviceClientID = serviceClientID;
    }

    @Value("${service.client_secret}")
    public void setServiceClientSecret(String serviceClientSecret) {
	this.serviceClientSecret = serviceClientSecret;
    }

    @Value("${oauth2_client.grant_type}")
    public void setGrantType(String grantType) {
	this.grantType = grantType;
    }

    @Value("${oauth2_client.scopes}")
    public void setScope(String scope) {
	this.scope = scope;
    }

    /**
     * Requests access token.
     * 
     * @return String containing the authorization header, e.g.
     *         "Bearer xxxxxxxxxxxxxx"
     */
    public String requestAccessToken(String tenant) {

	String[] paths = { PATH_REQUEST_TOKEN };

	Form form = new Form();
	form.param(QUERY_PARAM_CLIENT_ID, serviceClientID);
	form.param(QUERY_PARAM_CLIENT_SECRET, serviceClientSecret);
	form.param(QUERY_PARAM_GRANT_TYPE, grantType);
	form.param(QUERY_PARAM_HYBRIS_TENANT, tenant);
	form.param(QUERY_PARAM_SCOPE, scope);

	final Response response = restClient.post(uri, paths, null, null,
		Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE));

	if (response.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()) {
	    throw new BadRequestException("Invalid authentication.");
	}
	if (response.getStatus() != Response.Status.OK.getStatusCode()) {
	    throw new InternalServerErrorException("Problem occured while get data.");
	}

	Oauth2Token responseTokenEntity = response.readEntity(Oauth2Token.class);
	return responseTokenEntity.getTokenType() + " "
		+ responseTokenEntity.getAccessToken();

    }

}
