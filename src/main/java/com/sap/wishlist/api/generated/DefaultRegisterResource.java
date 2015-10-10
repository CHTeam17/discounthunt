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

import javax.inject.Singleton;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.springframework.stereotype.Component;

/**
* Resource class containing the custom logic. Please put your logic here!
*/
@Component("apiRegisterResource")
@Singleton
public class DefaultRegisterResource implements com.sap.wishlist.api.generated.RegisterResource
{
	@javax.ws.rs.core.Context
	private javax.ws.rs.core.UriInfo uriInfo;

	/* POST / */
	@Override
	public Response post(@javax.ws.rs.BeanParam @javax.validation.Valid final YaasAwareParameters yaasAware, @javax.validation.Valid final Register register)
	{
		// place some logic here

		ResponseBuilder resb = Response.created(uriInfo.getAbsolutePath());
		ResponseBuilder resb2 = resb.entity("hallo");
		
		return resb2.build();
	}

	/* GET / */
	@Override
	public Response get()
	{
		// place some logic here
		return Response.ok()
			.entity(new Register()).build();
	}

	/* PUT / */
	@Override
	public Response put(final Register register)
	{
		// place some logic here
		return Response.ok()
			.build();
	}

	/* DELETE / */
	@Override
	public Response delete()
	{
		// place some logic here
		return Response.noContent()
			.build();
	}

}
