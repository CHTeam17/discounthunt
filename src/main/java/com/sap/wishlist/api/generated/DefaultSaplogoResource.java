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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.StreamDataBodyPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.hybris.authorization.AccessToken;
import com.hybris.authorization.AuthorizationScope;
import com.hybris.authorization.DiagnosticContext;
import com.hybris.authorization.integration.AuthorizedExecutionCallback;
import com.hybris.authorization.integration.AuthorizedExecutionTemplate;
import com.hybris.patterns.schemas.ResourceLocation;
import com.sap.wishlist.client.mediaRepository.MediaRepositoryClient;
import com.sap.wishlist.utility.AuthorizationHelper;
import com.sap.wishlist.utility.ErrorHandler;

/**
 * Resource class containing the custom logic.
 */
@Component("apiSaplogoResource")
@Singleton
public class DefaultSaplogoResource implements SaplogoResource
{
    @javax.ws.rs.core.Context
    private javax.ws.rs.core.UriInfo uriInfo;

    private MediaRepositoryClient mediaRepositoryClient;

    @Inject
    private AuthorizationHelper authorizationHelper;

    @Inject
    private AuthorizedExecutionTemplate authorizedExecutionTemplate;
    @Value("${YAAS_CLIENT}")
    private String client;
    @Value("${OAUTH2_SCOPES}")
    private String scopes;

    @SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(DefaultSaplogoResource.class);

    public static final String SAP_LOGO_FILE_PATH = "src/main/resources/saplogo.png";

    public DefaultSaplogoResource() {
	mediaRepositoryClient = new MediaRepositoryClient(
		com.sap.wishlist.client.mediaRepository.MediaRepositoryClient.DEFAULT_BASE_URI, ClientBuilder
			.newClient().register(MultiPartFeature.class));
    }

    @Override
    public Response post(final YaasAwareParameters yaasAware) {
	InputStream is = null;
	try {
	    is = new FileInputStream(SAP_LOGO_FILE_PATH);
	} catch (FileNotFoundException e) {
	    return Response.serverError().entity("Cannot read SAP Logo at path " + SAP_LOGO_FILE_PATH).build();
	}
	// Prepare MultiPart
	final FormDataMultiPart multiPart = new FormDataMultiPart();
	multiPart.bodyPart(new StreamDataBodyPart("file", is));

	Response response = authorizedExecutionTemplate.executeAuthorized(
		new AuthorizationScope(yaasAware.getHybrisTenant(), authorizationHelper.getScopes()),
		new DiagnosticContext(yaasAware.getHybrisRequestId(), yaasAware.getHybrisHop()),
		new AuthorizedExecutionCallback<Response>()
		{
		    @Override
		    public Response execute(final AccessToken token)
		    {
			return mediaRepositoryClient.tenant(yaasAware.getHybrisTenant())
				.client(client)
				.media()
				.preparePost()
				.withAuthorization(token.getType() + " " + token.getValue())
				.withPayload(multiPart, MediaType.MULTIPART_FORM_DATA)
				.execute();
		    }
		});

	if (response.getStatus() != Response.Status.CREATED.getStatusCode()) {
	    ErrorHandler.handleResponse(response);
	}

	ResourceLocation location = response.readEntity(ResourceLocation.class);

	return Response.created(uriInfo.getRequestUriBuilder().path("/" + location.getId()).build()).entity(location)
		.build();
    }

    @Override
    public Response deleteByMediaId(final YaasAwareParameters yaasAware, final String mediaId) {
	Response response = authorizedExecutionTemplate.executeAuthorized(
		new AuthorizationScope(yaasAware.getHybrisTenant(), authorizationHelper.getScopes()),
		new DiagnosticContext(yaasAware.getHybrisRequestId(), yaasAware.getHybrisHop()),
		new AuthorizedExecutionCallback<Response>()
		{
		    @Override
		    public Response execute(final AccessToken token)
		    {
			return mediaRepositoryClient.tenant(yaasAware.getHybrisTenant())
				.client(client)
				.media()
				.mediaId(mediaId)
				.prepareDelete()
				.withAuthorization(token.getType() + " " + token.getValue())
				.execute();
		    }
		});
	if (response.getStatus() != Response.Status.NO_CONTENT.getStatusCode()) {
	    ErrorHandler.handleResponse(response);
	}

	return Response.noContent().build();
    }

}