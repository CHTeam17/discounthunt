package com.sap.wishlist.service;

import java.net.URI;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.sap.wishlist.api.generated.AppAwareParameters;
import com.sap.wishlist.api.generated.Wishlist;
import com.sap.wishlist.client.DocumentClient;
import com.sap.wishlist.client.document.DocumentWishlist;
import com.sap.wishlist.client.document.ResponseCreated;

@ManagedBean
public class WishlistService {

    @Inject
    private DocumentClient documentClient;

    /* GET / */
    public Response get(final AppAwareParameters appAware)
    {
	// place some logic here
	return Response.ok()
		.entity(new java.util.ArrayList<>()).build();
    }

    /* POST / */
    public Response post(final AppAwareParameters appAware, final UriInfo uriInfo, final Wishlist wishlist)
    {
	ResponseCreated wishlistDataType = documentClient.createWishlist(appAware.getHybrisTenant(), wishlist,
		wishlist.getId());

	URI resourceURI = uriInfo.getRequestUriBuilder().path("/" + wishlistDataType.getId()).build();
	return Response.created(resourceURI).build();
    }

    /* GET //{wishlistId} */
    public Response getByWishlistId(final AppAwareParameters appAware, final java.lang.String wishlistId)
    {
	DocumentWishlist documentWishlist = documentClient.getWishlist(appAware.getHybrisTenant(), wishlistId);
	Wishlist wishlist = documentWishlist;

	return Response.ok().entity(wishlist).build();
    }

    /* PUT //{wishlistId} */
    public Response putByWishlistId(final AppAwareParameters appAware, final java.lang.String wishlistId,
	    final Wishlist wishlist)
    {
	// place some logic here
	return Response.ok()
		.build();
    }

    /* DELETE //{wishlistId} */
    public Response deleteByWishlistId(final AppAwareParameters appAware, final java.lang.String wishlistId)
    {
	// place some logic here
	return Response.noContent()
		.build();
    }

}
