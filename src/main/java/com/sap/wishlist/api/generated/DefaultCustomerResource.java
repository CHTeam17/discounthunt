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

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.hybris.authorization.AccessToken;
import com.hybris.authorization.AuthorizationScope;
import com.hybris.authorization.DiagnosticContext;
import com.hybris.authorization.integration.AuthorizedExecutionCallback;
import com.hybris.authorization.integration.AuthorizedExecutionTemplate;
import com.sap.wishlist.customer.CustomerIgnoreUnknownProperties;
import com.sap.wishlist.utility.AuthorizationHelper;
import com.sap.wishlist.utility.ErrorHandler;
import com.sap.yaastemplates.client.customer.RestEndpointForSignupLoginAndCustomerProfileManagementClient;

/**
 * Resource class containing the custom logic. Place put your logic here!
 */
@Component("apiCustomerResource")
@Singleton
public class DefaultCustomerResource implements CustomerResource
{
    @javax.ws.rs.core.Context
    private javax.ws.rs.core.UriInfo uriInfo;

    private RestEndpointForSignupLoginAndCustomerProfileManagementClient customerClient = new RestEndpointForSignupLoginAndCustomerProfileManagementClient(
	    RestEndpointForSignupLoginAndCustomerProfileManagementClient.DEFAULT_BASE_URI);;

    @Inject
    private AuthorizedExecutionTemplate authorizedExecutionTemplate;

    @Inject
    private AuthorizationHelper authorizationHelper;

    @Value("${YAAS_CLIENT}")
    private String client;

    /* POST / */
    @Override
    public Response post(final YaasAwareParameters yaasAware, final Customer customer)
    {
	Response response = authorizedExecutionTemplate.executeAuthorized(
		new AuthorizationScope(authorizationHelper.getScopes()),
		new DiagnosticContext(yaasAware.getHybrisRequestId(), yaasAware.getHybrisHop()),
		new AuthorizedExecutionCallback<Response>()
		{
		    @Override
		    public Response execute(final AccessToken token)
		    {
			return customerClient.tenant(yaasAware.getHybrisTenant())
				.customers()
				.preparePost()
				.withAuthorization(authorizationHelper.buildToken(token))
				.withPayload(Entity.json(customer))
				.execute();
		    }
		});

	if (response.getStatus() != Response.Status.CREATED.getStatusCode()) {
	    ErrorHandler.handleResponse(response);
	    return response;
	} else {
	    final PostCustomer201Response customerCreated = response.readEntity(PostCustomer201Response.class);
	    for (final Address address : customer.getAddresses()) {
		final Response responseAddress = authorizedExecutionTemplate.executeAuthorized(
			new AuthorizationScope(authorizationHelper.getScopes()),
			new DiagnosticContext(yaasAware.getHybrisRequestId(), yaasAware.getHybrisHop()),
			new AuthorizedExecutionCallback<Response>()
			{
			    @Override
			    public Response execute(final AccessToken token)
			    {
				return customerClient.tenant(yaasAware.getHybrisTenant())
					.customers()
					.customerNumber(customerCreated.getId())
					.addresses()
					.preparePost()
					.withAuthorization(authorizationHelper.buildToken(token))
					.withPayload(Entity.json(address))
					.execute();
			    }
			});

		if (responseAddress.getStatus() != Response.Status.CREATED.getStatusCode()) {
		    ErrorHandler.handleResponse(responseAddress);
		}
	    }

	    PostCustomer201Response responseCustomerNumber = new PostCustomer201Response();
	    responseCustomerNumber.setId(customerCreated.getId());

	    return Response.created(uriInfo.getAbsolutePath())
		    .entity(responseCustomerNumber).build();
	}
    }

    /* GET //{customerNumber} */
    @Override
    public Response getByCustomerNumber(final YaasAwareParameters yaasAware, final String customerNumber)
    {
	Response response = authorizedExecutionTemplate.executeAuthorized(
		new AuthorizationScope(authorizationHelper.getScopes()),
		new DiagnosticContext(yaasAware.getHybrisRequestId(), yaasAware.getHybrisHop()),
		new AuthorizedExecutionCallback<Response>()
		{
		    @Override
		    public Response execute(final AccessToken token)
		    {
			return customerClient.tenant(yaasAware.getHybrisTenant())
				.customers()
				.customerNumber(customerNumber)
				.prepareGet()
				.withAuthorization(authorizationHelper.buildToken(token))
				.withQuery("expand", "addresses,defaultAddress")
				.execute();
		    }
		});

	if (response.getStatus() == Response.Status.OK.getStatusCode()) {
	    return Response.ok(response.readEntity(CustomerIgnoreUnknownProperties.class)).build();
	} else {
	    ErrorHandler.handleResponse(response);
	    return response;
	}
    }
}