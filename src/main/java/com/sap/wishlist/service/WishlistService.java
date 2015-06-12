package com.sap.wishlist.service;

import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Value;

import com.hybris.authorization.AccessToken;
import com.hybris.authorization.AuthorizationScope;
import com.hybris.authorization.DiagnosticContext;
import com.hybris.authorization.integration.AuthorizedExecutionCallback;
import com.hybris.authorization.integration.AuthorizedExecutionTemplate;
import com.sap.wishlist.api.generated.DocumentWishlist;
import com.sap.wishlist.api.generated.DocumentWishlistRead;
import com.sap.wishlist.api.generated.PagedParameters;
import com.sap.wishlist.api.generated.ResourceLocation;
import com.sap.wishlist.api.generated.Wishlist;
import com.sap.wishlist.api.generated.YaasAwareParameters;
import com.sap.wishlist.client.documentrepository.DocumentRepositoryClient;
import com.sap.wishlist.utility.AuthorizationHelper;
import com.sap.wishlist.utility.ErrorHandler;

@ManagedBean
public class WishlistService {

    public static final String WISHLIST_PATH = "wishlist";

    @Inject
    private DocumentRepositoryClient documentClient;
    @Inject
    private AuthorizedExecutionTemplate authorizedExecutionTemplate;
    @Inject
    private AuthorizationHelper authorizationHelper;
    @Value("${YAAS_CLIENT}")
    private String client;

    /* GET / */
    public Response get(final PagedParameters paged, final YaasAwareParameters yaasAware) {
	ArrayList<Wishlist> result = null;
	Response response = authorizedExecutionTemplate.executeAuthorized(
		new AuthorizationScope(authorizationHelper.getScopes()),
		new DiagnosticContext(yaasAware.getHybrisRequestId(), yaasAware.getHybrisHop()),
		new AuthorizedExecutionCallback<Response>()
		{
		    @Override
		    public Response execute(final AccessToken token)
		    {
			return documentClient.tenant(yaasAware.getHybrisTenant())
				.clientData(client)
				.type(WISHLIST_PATH)
				.prepareGet()
				.withPageNumber(paged.getPageNumber())
				.withPageSize(paged.getPageSize())
				.withAuthorization(authorizationHelper.buildToken(token))
				.execute();
		    }
		});
	if (response.getStatus() == Response.Status.OK.getStatusCode()) {
	    result = new ArrayList<Wishlist>();
	    for (DocumentWishlistRead documentWishlist : response.readEntity(DocumentWishlistRead[].class)) {
		Wishlist wishlist = documentWishlist.getWishlist();

		String dateString = documentWishlist.getMetadata().getCreatedAt();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		Date createdAt;
		try {
		    createdAt = df.parse(dateString);
		    wishlist.setCreatedAt(createdAt);
		} catch (ParseException e) {
		    e.printStackTrace();
		    throw new InternalServerErrorException();
		}
		result.add(wishlist);
	    }

	} else {
	    ErrorHandler.handleResponseStatusCode(response.getStatus());
	}

	return Response.ok().entity(result).build();
    }

    /* POST / */
    public Response post(final YaasAwareParameters yaasAware, final UriInfo uriInfo, final Wishlist wishlist) {
	final DocumentWishlist documentWishlist = new DocumentWishlist();
	documentWishlist.setWishlist(wishlist);

	Response response = authorizedExecutionTemplate.executeAuthorized(
		new AuthorizationScope(authorizationHelper.getScopes()),
		new DiagnosticContext(yaasAware.getHybrisRequestId(), yaasAware.getHybrisHop()),
		new AuthorizedExecutionCallback<Response>()
		{
		    @Override
		    public Response execute(final AccessToken token)
		    {
			return documentClient.tenant(yaasAware.getHybrisTenant())
				.clientData(client)
				.type(WISHLIST_PATH)
				.dataId(wishlist.getId())
				.preparePost()
				.withAuthorization(authorizationHelper.buildToken(token))
				.withPayload(Entity.json(documentWishlist))
				.execute();
		    }
		});

	if (response.getStatus() != Status.CREATED.getStatusCode()) {
	    ErrorHandler.handleResponseStatusCode(response.getStatus());
	}

	ResourceLocation location = response.readEntity(ResourceLocation.class);
	URI createdLocation = uriInfo.getRequestUriBuilder().path("/" + location.getId()).build();
	return Response.created(createdLocation).build();
    }

    /* GET //{wishlistId} */
    public Response getByWishlistId(final YaasAwareParameters yaasAware, final java.lang.String wishlistId) {
	Response response = authorizedExecutionTemplate.executeAuthorized(
		new AuthorizationScope(authorizationHelper.getScopes()),
		new DiagnosticContext(yaasAware.getHybrisRequestId(), yaasAware.getHybrisHop()),
		new AuthorizedExecutionCallback<Response>()
		{
		    @Override
		    public Response execute(final AccessToken token)
		    {
			return documentClient.tenant(yaasAware.getHybrisTenant())
				.clientData(client)
				.type(WISHLIST_PATH)
				.dataId(wishlistId)
				.prepareGet()
				.withAuthorization(authorizationHelper.buildToken(token))
				.execute();
		    }
		});

	if (response.getStatus() != Status.OK.getStatusCode()) {
	    ErrorHandler.handleResponseStatusCode(response.getStatus());
	}

	DocumentWishlistRead documentWishlistRead = response.readEntity(DocumentWishlistRead.class);
	return Response.ok(documentWishlistRead.getWishlist()).build();
    }

    /* PUT //{wishlistId} */
    public Response putByWishlistId(final YaasAwareParameters yaasAware, final java.lang.String wishlistId,
	    final Wishlist wishlist) {
	final DocumentWishlist documentWishlist = new DocumentWishlist();
	documentWishlist.setWishlist(wishlist);

	Response response = authorizedExecutionTemplate.executeAuthorized(
		new AuthorizationScope(authorizationHelper.getScopes()),
		new DiagnosticContext(yaasAware.getHybrisRequestId(), yaasAware.getHybrisHop()),
		new AuthorizedExecutionCallback<Response>()
		{
		    @Override
		    public Response execute(final AccessToken token)
		    {
			return documentClient.tenant(yaasAware.getHybrisTenant())
				.clientData(client)
				.type(WISHLIST_PATH)
				.dataId(wishlist.getId())
				.preparePut()
				.withAuthorization(authorizationHelper.buildToken(token))
				.withPayload(Entity.json(documentWishlist))
				.execute();
		    }
		});

	if (response.getStatus() != Status.OK.getStatusCode()) {
	    ErrorHandler.handleResponseStatusCode(response.getStatus());
	}

	return Response.ok().build();
    }

    /* DELETE //{wishlistId} */
    public Response deleteByWishlistId(final YaasAwareParameters yaasAware, final java.lang.String wishlistId) {
	Response response = authorizedExecutionTemplate.executeAuthorized(
		new AuthorizationScope(authorizationHelper.getScopes()),
		new DiagnosticContext(yaasAware.getHybrisRequestId(), yaasAware.getHybrisHop()),
		new AuthorizedExecutionCallback<Response>()
		{
		    @Override
		    public Response execute(final AccessToken token)
		    {
			return documentClient.tenant(yaasAware.getHybrisTenant())
				.clientData(client)
				.type(WISHLIST_PATH)
				.dataId(wishlistId)
				.prepareDelete()
				.withAuthorization(authorizationHelper.buildToken(token))
				.execute();
		    }
		});

	if (response.getStatus() != Status.NO_CONTENT.getStatusCode()) {
	    ErrorHandler.handleResponseStatusCode(response.getStatus());
	}

	return Response.noContent().build();
    }
}
