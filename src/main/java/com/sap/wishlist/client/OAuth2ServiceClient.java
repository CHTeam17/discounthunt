package com.sap.wishlist.client;

import javax.annotation.ManagedBean;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Value;

import com.hybris.api.OAuth2AuthorizationServerClient;
import com.sap.wishlist.client.oauth2.Oauth2Token;

@ManagedBean
public class OAuth2ServiceClient {

    private String serviceClientID;
    private String serviceClientSecret;
    private String grantType;
    private String scope;

    private static String QUERY_PARAM_CLIENT_ID = "client_id";
    private static String QUERY_PARAM_CLIENT_SECRET = "client_secret";
    private static String QUERY_PARAM_GRANT_TYPE = "grant_type";
    private static String QUERY_PARAM_HYBRIS_TENANT = "hybris-tenant";
    private static String QUERY_PARAM_SCOPE = "scope";

    @Value("${YAAS_CLIENT_ID}")
    public void setServiceClientID(String serviceClientID) {
	this.serviceClientID = serviceClientID;
    }

    @Value("${YAAS_CLIENT_SECRET}")
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

	Form form = new Form();
	form.param(QUERY_PARAM_CLIENT_ID, serviceClientID);
	form.param(QUERY_PARAM_CLIENT_SECRET, serviceClientSecret);
	form.param(QUERY_PARAM_GRANT_TYPE, grantType);
	form.param(QUERY_PARAM_HYBRIS_TENANT, tenant);
	form.param(QUERY_PARAM_SCOPE, scope);

	Entity<Form> tokenRequest = Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE);

	OAuth2AuthorizationServerClient client = new OAuth2AuthorizationServerClient(
		OAuth2AuthorizationServerClient.DEFAULT_BASE_URI);
	final Response response = client.token().preparePost().withPayload(tokenRequest).execute();

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
