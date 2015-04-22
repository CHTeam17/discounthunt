package com.sap.wishlist.service;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Value;

import com.hybris.api.DocumentRepositoryClient;
import com.sap.wishlist.api.generated.AppAwareParameters;
import com.sap.wishlist.api.generated.Wishlist;
import com.sap.wishlist.client.OAuth2ServiceClient;

@ManagedBean
public class WishlistService {

    public static final String WISHLIST_PATH = "wishlist";

    @Inject
    private OAuth2ServiceClient oAuth2Client;

    private String clientId;

    @Value("${YAAS_CLIENT_ID}")
    public void setClientId(String clientId) {
	this.clientId = clientId;
    }

    /* GET / */
    public Response get(final AppAwareParameters appAware) {
	String authorization = oAuth2Client.requestAccessToken(appAware.getHybrisTenant());

	DocumentRepositoryClient client = new DocumentRepositoryClient(DocumentRepositoryClient.DEFAULT_BASE_URI);
	final Response response = client.tenant(appAware.getHybrisTenant())
		.clientData(this.clientId)
		.type(WISHLIST_PATH)
		.prepareGet()
		.withHeader("Authorization", authorization)
		.execute();

	return Response.ok().entity(response.readEntity(String.class)).build();
    }

    /* POST / */
    public Response post(final AppAwareParameters appAware, final UriInfo uriInfo, final Wishlist wishlist) {
	String wishlistId = wishlist.getId();

	String authorization = oAuth2Client.requestAccessToken(appAware.getHybrisTenant());

	DocumentRepositoryClient client = new DocumentRepositoryClient(DocumentRepositoryClient.DEFAULT_BASE_URI);
	final Response response = client.tenant(appAware.getHybrisTenant())
		.clientData(this.clientId)
		.type(WISHLIST_PATH)
		.dataId(wishlistId)
		.preparePost()
		.withHeader("Authorization", authorization)
		.withPayload(Entity.json(wishlist))
		.execute();

	return response;
    }

    /* GET //{wishlistId} */
    public Response getByWishlistId(final AppAwareParameters appAware, final java.lang.String wishlistId) {
	String authorization = oAuth2Client.requestAccessToken(appAware.getHybrisTenant());

	DocumentRepositoryClient client = new DocumentRepositoryClient(DocumentRepositoryClient.DEFAULT_BASE_URI);
	final Response response = client.tenant(appAware.getHybrisTenant())
		.clientData(this.clientId)
		.type(WISHLIST_PATH)
		.dataId(wishlistId)
		.prepareGet()
		.withHeader("Authorization", authorization)
		.execute();

	return response;

    }

    /* PUT //{wishlistId} */
    public Response putByWishlistId(final AppAwareParameters appAware, final java.lang.String wishlistId,
	    final Wishlist wishlist) {
	String authorization = oAuth2Client.requestAccessToken(appAware.getHybrisTenant());

	DocumentRepositoryClient client = new DocumentRepositoryClient(DocumentRepositoryClient.DEFAULT_BASE_URI);
	final Response response = client.tenant(appAware.getHybrisTenant())
		.clientData(this.clientId)
		.type(WISHLIST_PATH)
		.dataId(wishlistId)
		.preparePut()
		.withHeader("Authorization", authorization)
		.withPayload(Entity.json(wishlist))
		.execute();

	return response;
    }

    /* DELETE //{wishlistId} */
    public Response deleteByWishlistId(final AppAwareParameters appAware, final java.lang.String wishlistId) {
	String authorization = oAuth2Client.requestAccessToken(appAware.getHybrisTenant());

	DocumentRepositoryClient client = new DocumentRepositoryClient(DocumentRepositoryClient.DEFAULT_BASE_URI);
	final Response response = client.tenant(appAware.getHybrisTenant())
		.clientData(this.clientId)
		.type(WISHLIST_PATH)
		.dataId(wishlistId)
		.prepareDelete()
		.withHeader("Authorization", authorization)
		.execute();

	return response;
    }

}
