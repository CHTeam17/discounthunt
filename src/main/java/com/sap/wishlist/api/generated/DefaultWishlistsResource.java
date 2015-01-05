/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2014 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 */
package com.sap.wishlist.api.generated;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Component;

import com.sap.wishlist.service.WishlistService;

/**
 * Resource class containing the custom logic. Place put your logic here!
 */
@Component("apiWishlistsResource")
@Singleton
public class DefaultWishlistsResource implements com.sap.wishlist.api.generated.WishlistsResource
{
    @javax.ws.rs.core.Context
    private javax.ws.rs.core.UriInfo uriInfo;

    @Inject
    private WishlistService wishlistService;

    /* GET / */
    @Override
    public Response get(final AppAwareParameters appAware)
    {
	return wishlistService.get(appAware);
    }

    /* POST / */
    @Override
    public Response post(final AppAwareParameters appAware, final Wishlist wishlist)
    {
	return wishlistService.post(appAware, uriInfo, wishlist);
    }

    /* GET //{wishlistId} */
    @Override
    public Response getByWishlistId(final AppAwareParameters appAware, final java.lang.String wishlistId)
    {
	return wishlistService.getByWishlistId(appAware, wishlistId);
    }

    /* PUT //{wishlistId} */
    @Override
    public Response putByWishlistId(final AppAwareParameters appAware, final java.lang.String wishlistId,
	    final Wishlist wishlist)
    {
	return wishlistService.putByWishlistId(appAware, wishlistId, wishlist);
    }

    /* DELETE //{wishlistId} */
    @Override
    public Response deleteByWishlistId(final AppAwareParameters appAware, final java.lang.String wishlistId)
    {
	return wishlistService.deleteByWishlistId(appAware, wishlistId);
    }

}
