package com.sap.wishlist.service;

import java.net.URI;
import java.util.ArrayList;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.HttpClientErrorException;

import com.hybris.api.DocumentRepositoryClient;
import com.sap.wishlist.api.generated.AppAwareParameters;
import com.sap.wishlist.api.generated.ResourceLocation;
import com.sap.wishlist.api.generated.Wishlist;
import com.sap.wishlist.client.OAuth2ServiceClient;
import com.sap.wishlist.client.document.DocumentWishlist;
import com.sap.wishlist.utility.ErrorHandler;

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
	Response response = null;
	try {
	    response = client.tenant(appAware.getHybrisTenant())
		    .clientData(this.clientId)
		    .type(WISHLIST_PATH)
		    .prepareGet()
		    .withHeader("Authorization", authorization)
		    .execute();
	} catch (HttpClientErrorException e) {
	    ErrorHandler.handleResponseStatusCode(e);
	}

	if (response.getStatus() != Status.OK.getStatusCode()) {
	    throw new InternalServerErrorException();
	}

	DocumentWishlist[] responseData = response.readEntity(DocumentWishlist[].class);
	ArrayList<Wishlist> wishlists = new ArrayList<Wishlist>();
	for (DocumentWishlist documentWishlist : responseData) {

	    wishlists.add(mapDocumentData(documentWishlist));
	}

	return Response.ok().entity(wishlists).build();
    }

    /* POST / */
    public Response post(final AppAwareParameters appAware, final UriInfo uriInfo, final Wishlist wishlist) {
	String wishlistId = wishlist.getId();

	String authorization = oAuth2Client.requestAccessToken(appAware.getHybrisTenant());

	DocumentRepositoryClient client = new DocumentRepositoryClient(DocumentRepositoryClient.DEFAULT_BASE_URI);
	Response response = null;
	try {
	    response = client.tenant(appAware.getHybrisTenant())
		    .clientData(this.clientId)
		    .type(WISHLIST_PATH)
		    .dataId(wishlistId)
		    .preparePost()
		    .withHeader("Authorization", authorization)
		    .withPayload(Entity.json(wishlist))
		    .execute();
	} catch (HttpClientErrorException e) {
	    ErrorHandler.handleResponseStatusCode(e);
	}

	if (response.getStatus() != Status.CREATED.getStatusCode()) {
	    throw new InternalServerErrorException();
	}

	ResourceLocation location = response.readEntity(ResourceLocation.class);
	URI createdLocation = uriInfo.getRequestUriBuilder().path("/" + location.getId()).build();
	return Response.created(createdLocation).build();
    }

    /* GET //{wishlistId} */
    public Response getByWishlistId(final AppAwareParameters appAware, final java.lang.String wishlistId) {
	String authorization = oAuth2Client.requestAccessToken(appAware.getHybrisTenant());

	DocumentRepositoryClient client = new DocumentRepositoryClient(DocumentRepositoryClient.DEFAULT_BASE_URI);
	Response response = null;
	try {
	    response = client.tenant(appAware.getHybrisTenant())
		    .clientData(this.clientId)
		    .type(WISHLIST_PATH)
		    .dataId(wishlistId)
		    .prepareGet()
		    .withHeader("Authorization", authorization)
		    .execute();
	} catch (HttpClientErrorException e) {
	    ErrorHandler.handleResponseStatusCode(e);
	}

	if (response.getStatus() != Status.OK.getStatusCode()) {
	    throw new InternalServerErrorException();
	}

	DocumentWishlist data = response.readEntity(DocumentWishlist.class);
	return Response.ok(mapDocumentData(data)).build();

    }

    /* PUT //{wishlistId} */
    public Response putByWishlistId(final AppAwareParameters appAware, final java.lang.String wishlistId,
	    final Wishlist wishlist) {
	String authorization = oAuth2Client.requestAccessToken(appAware.getHybrisTenant());

	DocumentRepositoryClient client = new DocumentRepositoryClient(DocumentRepositoryClient.DEFAULT_BASE_URI);
	Response response = null;
	try {
	    response = client.tenant(appAware.getHybrisTenant())
		    .clientData(this.clientId)
		    .type(WISHLIST_PATH)
		    .dataId(wishlistId)
		    .preparePut()
		    .withHeader("Authorization", authorization)
		    .withPayload(Entity.json(wishlist))
		    .execute();
	} catch (HttpClientErrorException e) {
	    ErrorHandler.handleResponseStatusCode(e);
	}

	if (response.getStatus() != Status.OK.getStatusCode()) {
	    throw new InternalServerErrorException();
	}

	return Response.ok().build();
    }

    /* DELETE //{wishlistId} */
    public Response deleteByWishlistId(final AppAwareParameters appAware, final java.lang.String wishlistId) {
	String authorization = oAuth2Client.requestAccessToken(appAware.getHybrisTenant());

	DocumentRepositoryClient client = new DocumentRepositoryClient(DocumentRepositoryClient.DEFAULT_BASE_URI);
	Response response = null;
	try {
	    response = client.tenant(appAware.getHybrisTenant())

		    .clientData(this.clientId)
		    .type(WISHLIST_PATH)
		    .dataId(wishlistId)
		    .prepareDelete()
		    .withHeader("Authorization", authorization)
		    .execute();
	} catch (HttpClientErrorException e) {
	    ErrorHandler.handleResponseStatusCode(e);
	}

	if (response.getStatus() != Status.NO_CONTENT.getStatusCode()) {
	    throw new InternalServerErrorException();
	}

	return Response.noContent().build();
    }

    private Wishlist mapDocumentData(DocumentWishlist data) {
	Wishlist result = new Wishlist();
	result.setId(data.getId());
	String descriptption = data.getDescription();
	result.setDescription(descriptption);
	result.setItems(data.getItems());
	result.setOwner(data.getOwner());
	result.setTitle(data.getTitle());
	result.setUrl(data.getUrl());
	result.setCreatedAt(data.getCreatedAt());

	return result;
    }
}
