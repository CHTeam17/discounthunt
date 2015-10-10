/*
 * [y] hybris Platform
 * 
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 */
package com.sap.wishlist.api.generated;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.server.ResourceConfig;
import org.junit.Assert;
import org.junit.Test;


public final class DefaultRegisterResourceTest extends AbstractResourceTest
{
	/**
	 * Server side root resource /register,
	 * evaluated with some default value(s).
	 */
	private static final String ROOT_RESOURCE_PATH = "/register";

	/* post(entity) /register */
	@Test
	public void testPostWithString()
	{
		final WebTarget target = getRootTarget(ROOT_RESOURCE_PATH).path("");
		final String entityBody =
		new String();
		final javax.ws.rs.client.Entity<String> entity =
		javax.ws.rs.client.Entity.entity(entityBody,"application/json");

		final Response response = target.request().post(entity);

		Assert.assertNotNull("Response must not be null", response);
		Assert.assertEquals("Response does not have expected response code", Status.CREATED.getStatusCode(), response.getStatus());
	}

	/* get() /register */
	@Test
	public void testGet()
	{
		final WebTarget target = getRootTarget(ROOT_RESOURCE_PATH).path("");

		final Response response = target.request().get();

		Assert.assertNotNull("Response must not be null", response);
		Assert.assertEquals("Response does not have expected response code", Status.OK.getStatusCode(), response.getStatus());
	}

	/* put(entity) /register */
	@Test
	public void testPutWithRegister()
	{
		final WebTarget target = getRootTarget(ROOT_RESOURCE_PATH).path("");
		final Register entityBody =
		new Register();
		final javax.ws.rs.client.Entity<Register> entity =
		javax.ws.rs.client.Entity.entity(entityBody,"application/json");

		final Response response = target.request().put(entity);

		Assert.assertNotNull("Response must not be null", response);
		Assert.assertEquals("Response does not have expected response code", Status.OK.getStatusCode(), response.getStatus());
	}

	/* delete() /register */
	@Test
	public void testDelete()
	{
		final WebTarget target = getRootTarget(ROOT_RESOURCE_PATH).path("");

		final Response response = target.request().delete();

		Assert.assertNotNull("Response must not be null", response);
		Assert.assertEquals("Response does not have expected response code", Status.NO_CONTENT.getStatusCode(), response.getStatus());
	}

	@Override
	protected ResourceConfig configureApplication()
	{
		final ResourceConfig application = new ResourceConfig();
		application.register(DefaultRegisterResource.class);
		return application;
	}
}
