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
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.server.ResourceConfig;
import org.junit.Assert;
import org.junit.Test;

import com.sap.wishlist.api.Constants;

public final class DefaultCustomerResourceTest extends AbstractResourceTest
{
    /**
     * Server side root resource /customer, evaluated with some default
     * value(s).
     */
    private static final String ROOT_RESOURCE_PATH = "/customer/";
    private static final String CUSTOMER_NUMBER = "C9449023503";
    private static final String APP = "test";

    /* get() /customer//customerNumber */
    @Test
    public void testGetByCustomerNumber()
    {
	MultivaluedMap<String, Object> headers = new MultivaluedHashMap<String, Object>();
	headers.add("hybris-tenant", Constants.TENANT);

	final WebTarget target = getRootTarget(ROOT_RESOURCE_PATH).path(CUSTOMER_NUMBER);
	final Response response = target.request().headers(headers).get();

	Assert.assertNotNull("Response must not be null", response);
	Assert.assertEquals("Response does not have expected response code", Status.OK.getStatusCode(),
		response.getStatus());
    }

    @Override
    protected ResourceConfig configureApplication()
    {
	final ResourceConfig application = new ResourceConfig();
	application.register(DefaultCustomerResource.class);
	return application;
    }
}
