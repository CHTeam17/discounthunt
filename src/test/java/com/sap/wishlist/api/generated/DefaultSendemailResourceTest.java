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

import com.sap.wishlist.api.TestConstants;


public final class DefaultSendemailResourceTest extends AbstractResourceTest
{
	/**
	 * Server side root resource /sendemail,
	 * evaluated with some default value(s).
	 */
	private static final String ROOT_RESOURCE_PATH = "/sendemail";

	/* post(null) /sendemail */
	@Test
	public void testPost()
	{
		final WebTarget target = getRootTarget(ROOT_RESOURCE_PATH).path("");

		final Response response = target.request()
			.header("hybris-tenant", TestConstants.TENANT)
			.header("hybris-user", "test@hybristest.com") // mails to domain hybristest.com are ignored by mail service
			.post(null);

		Assert.assertNotNull("Response must not be null", response);
		Assert.assertEquals("Response does not have expected response code", Status.CREATED.getStatusCode(),
			response.getStatus());}

	@Override
	protected ResourceConfig configureApplication()
	{
		final ResourceConfig application = new ResourceConfig();
		application.register(DefaultSendemailResource.class);
		return application;
	}
}
