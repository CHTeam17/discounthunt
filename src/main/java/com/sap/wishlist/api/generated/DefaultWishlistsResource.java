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
    public Response get(YaasAwareParameters yaasAware)
    {
	return wishlistService.get(yaasAware);
    }

    /* POST / */
    @Override
    public Response post(YaasAwareParameters yaasAware, Wishlist wishlist)
    {
	return wishlistService.post(yaasAware, uriInfo, wishlist);
    }

    /* GET //{wishlistId} */
    @Override
    public Response getByWishlistId(YaasAwareParameters yaasAware, String wishlistId)
    {
	return wishlistService.getByWishlistId(yaasAware, wishlistId);
    }

    /* PUT //{wishlistId} */
    @Override
    public Response putByWishlistId(YaasAwareParameters yaasAware, String wishlistId, Wishlist wishlist)
    {
	return wishlistService.putByWishlistId(yaasAware, wishlistId, wishlist);
    }

    /* DELETE //{wishlistId} */
    @Override
    public Response deleteByWishlistId(YaasAwareParameters yaasAware, String wishlistId)
    {
	return wishlistService.deleteByWishlistId(yaasAware, wishlistId);
    }
}
