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

import java.util.ArrayList;
import java.util.UUID;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.server.ResourceConfig;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.hybris.context.Headers;

public final class DefaultWishlistsResourceTest extends com.sap.wishlist.api.generated.AbstractResourceTest
{
    /**
     * Server side root resource /wishlists, evaluated with some default
     * value(s).
     */
    private static final String ROOT_RESOURCE_PATH = "/wishlists";
    private static final String TENANT = "cecwishlist";
    private static final String CLIENT = "test";
    private static Wishlist WISHLIST;

    private ArrayList<String> instanceList = new ArrayList<String>();

    @Before
    public void before() {
	WISHLIST = new Wishlist();
	WISHLIST.setId(UUID.randomUUID().toString());
	WISHLIST.setDescription("Test");

	instanceList.add(WISHLIST.getId());

	createWishlist(WISHLIST);
    }

    /* get() /wishlists/ */
    @Test
    public void testGet()
    {
	final WebTarget target = getRootTarget(ROOT_RESOURCE_PATH).path("");

	final Response response = target.request()
		.header(Headers.CLIENT, CLIENT)
		.header(Headers.TENANT, TENANT)
		.get();

	Assert.assertNotNull("Response must not be null", response);
	Assert.assertEquals("Response does not have expected response code",
		Status.OK.getStatusCode(),
		response.getStatus());
    }

    /* post(entity) /wishlists/ */
    @Test
    public void testPostWithWishlist()
    {
	Wishlist wishlist = new Wishlist();
	wishlist.setId(UUID.randomUUID().toString());
	instanceList.add(wishlist.getId());

	final Response response = createWishlist(wishlist);

	Assert.assertNotNull("Response must not be null", response);
	Assert.assertEquals("Response does not have expected response code",
		Status.CREATED.getStatusCode(),
		response.getStatus());
    }

    /* get() /wishlists//wishlistId */
    @Test
    public void testGetByWishlistId()
    {
	final WebTarget target = getRootTarget(ROOT_RESOURCE_PATH).path("/" + WISHLIST.getId());

	final Response response = target.request()
		.header(Headers.CLIENT, CLIENT)
		.header(Headers.TENANT, TENANT)
		.get();

	Assert.assertNotNull("Response must not be null", response);
	Assert.assertEquals("Response does not have expected response code",
		Status.OK.getStatusCode(),
		response.getStatus());
    }

    /* put(entity) /wishlists//wishlistId */
    @Test
    public void testPutByWishlistIdWithWishlist()
    {
	final WebTarget target = getRootTarget(ROOT_RESOURCE_PATH).path("/" + WISHLIST.getId());
	final com.sap.wishlist.api.generated.Wishlist entityBody = WISHLIST;
	final javax.ws.rs.client.Entity<com.sap.wishlist.api.generated.Wishlist> entity =
		javax.ws.rs.client.Entity.entity(entityBody, "application/json");

	final Response response = target.request()
		.header(Headers.CLIENT, CLIENT)
		.header(Headers.TENANT, TENANT)
		.put(entity);

	Assert.assertNotNull("Response must not be null", response);
	Assert.assertEquals("Response does not have expected response code",
		Status.OK.getStatusCode(),
		response.getStatus());
    }

    /* delete() /wishlists//wishlistId */
    @Test
    public void testDeleteByWishlistId()
    {
	final Response response = deleteWishlist(WISHLIST.getId());

	Assert.assertNotNull("Response must not be null", response);
	Assert.assertEquals("Response does not have expected response code",
		Status.NO_CONTENT.getStatusCode(),
		response.getStatus());
    }

    @After
    public void after() {
	for (String instance : instanceList) {
	    deleteWishlist(instance);
	}

    }

    private Response createWishlist(Wishlist wishlist) {
	final WebTarget target = getRootTarget(ROOT_RESOURCE_PATH).path("");
	final com.sap.wishlist.api.generated.Wishlist entityBody = wishlist;
	final javax.ws.rs.client.Entity<com.sap.wishlist.api.generated.Wishlist> entity =
		javax.ws.rs.client.Entity.entity(entityBody, "application/json");

	return target.request()
		.header(Headers.CLIENT, CLIENT)
		.header(Headers.TENANT, TENANT)
		.post(entity);
    }

    private Response deleteWishlist(String wishlistId) {
	final WebTarget target = getRootTarget(ROOT_RESOURCE_PATH).path("/" + wishlistId);

	return target.request()
		.header(Headers.CLIENT, CLIENT)
		.header(Headers.TENANT, TENANT)
		.delete();
    }

    @Override
    protected ResourceConfig configureApplication()
    {
	final ResourceConfig application = new ResourceConfig();
	application.register(DefaultWishlistsResource.class);
	return application;
    }
}
