package com.sap.wishlist.client;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.sap.wishlist.api.generated.Wishlist;
import com.sap.wishlist.client.document.DocumentWishlist;
import com.sap.wishlist.client.document.ResponseCreated;
import com.sap.wishlist.exception.WishlistException;

@ManagedBean
public class WishlistDocumentClient {

    @Inject
    private RestClient restClient;

    public static final String WISHLIST_PATH = "wishlist";
    public static final String DATA_NAMESPACE = "data";
    private static final Logger LOG = LoggerFactory.getLogger(WishlistDocumentClient.class);

    private URI uri;
    private String appPath;

    @Value("${document_client.uri}")
    public void setDocumentServiceUri(String uri) throws URISyntaxException {
	this.uri = new URI(uri);
    }

    @Value("${service.client_id}")
    public void setAppPath(String client_id) {
	this.appPath = client_id;
    }

    /**
     * Retrieve all wishlists of a tenant
     *
     * @param tenant
     *            hybris tenant where the request has to be processed
     * @return ArrayList<DocumentWishlist>
     * @throws WishlistException
     */
    public ArrayList<DocumentWishlist> getWishlists(final String tenant) throws WishlistException {
	String[] path = { tenant, appPath, DATA_NAMESPACE, WISHLIST_PATH };

	final Response wishlistResponse = restClient.get(tenant, uri, path, null, null);
	if (wishlistResponse.getStatus() != Response.Status.OK.getStatusCode()) {
	    final String errorMsg = "Error reading wishlists {}";
	    LOG.error(errorMsg, wishlistResponse.getStatusInfo().getReasonPhrase());
	    throw new WishlistException(wishlistResponse.getStatus(), "Not available");
	}

	return wishlistResponse.readEntity(new GenericType<ArrayList<DocumentWishlist>>() {
	});
    }

    /**
     * Retrieve a wishlist for a given Id
     *
     * @param tenant
     *            hybris tenant where the request has to be processed
     * @param wishlistId
     *            Id of the to be retrieved wishlist
     * @return DocumentWishlist
     * @throws WishlistException
     */
    public DocumentWishlist getWishlist(final String tenant,
	    final String wishlistId) throws WishlistException {

	String[] path = { tenant, appPath, DATA_NAMESPACE, WISHLIST_PATH, wishlistId };

	final Response wishlistResponse = restClient.get(tenant, uri, path, null, null);
	if (wishlistResponse.getStatus() != Response.Status.OK.getStatusCode()) {
	    final String errorMsg = "Wishlist not found {} {}";
	    LOG.error(errorMsg, wishlistId, wishlistResponse.getStatusInfo().getReasonPhrase());
	    throw new WishlistException(wishlistResponse.getStatus(), "Not available");
	}

	return wishlistResponse.readEntity(DocumentWishlist.class);

    }

    /**
     * Creates a new wishlist
     *
     * @param tenant
     *            hybris tenant where the request has to be processed
     * @param wishlist
     *            data of the new wishlist
     * @param wishlistId
     *            Id of the new wishlist
     * @return ResponseCreated
     * @throws WishlistException
     */
    public ResponseCreated createWishlist(final String tenant,
	    final Wishlist wishlist,
	    final String wishlistId) throws WishlistException {

	String[] path = { tenant, appPath, DATA_NAMESPACE, WISHLIST_PATH, wishlistId };

	final Response readWishlist = restClient.post(tenant, uri, path, null, null, Entity.json(wishlist));
	if (readWishlist.getStatus() != Response.Status.CREATED.getStatusCode()) {
	    final String errorMsg = "Wishlist cloud not be created {} {}";
	    LOG.error(errorMsg, wishlistId, readWishlist.getStatusInfo().getReasonPhrase());
	    throw new WishlistException(readWishlist.getStatus(), "Creating Wishlist failed");
	} else {
	    return readWishlist.readEntity(ResponseCreated.class);
	}
    }

    /**
     * Update an existing wishlist based on its ID
     *
     * @param tenant
     *            hybris tenant where the request has to be processed
     * @param wishlist
     *            data of the updated wishlist
     * @param wishlistId
     *            Id of the to be updated wishlist
     * @throws WishlistException
     */
    public void updateWishlist(final String tenant,
	    final Wishlist wishlist,
	    final String wishlistId) throws WishlistException {

	String[] path = { tenant, appPath, DATA_NAMESPACE, WISHLIST_PATH, wishlistId };

	final Response readWishlist = restClient.put(tenant, uri, path, null, null, Entity.json(wishlist));
	if (readWishlist.getStatus() != Response.Status.OK.getStatusCode()) {
	    final String errorMsg = "Wishlist not found {} {} ";
	    LOG.error(errorMsg, wishlistId, readWishlist.getStatusInfo().getReasonPhrase());
	    throw new WishlistException(readWishlist.getStatus(), "Updating Wishlist failed");
	}
    }

    /**
     * Delete an existing wishlist based on its Id
     *
     * @param tenant
     *            hybris tenant where the request has to be processed
     * @param wishlistId
     *            Id of an wishlist
     * @throws WishlistException
     */
    public void deleteWishlist(final String tenant, final String wishlistId) throws WishlistException {
	String[] path = { tenant, appPath, DATA_NAMESPACE, WISHLIST_PATH, wishlistId };

	final Response deleteWishlist = restClient.delete(tenant, uri, path, null, null);
	if (deleteWishlist.getStatus() != Response.Status.NO_CONTENT.getStatusCode()) {
	    String errorMsg;
	    switch (deleteWishlist.getStatus()) {
		case 404:
		    errorMsg = "Wishlist not found {} {}";
		    break;
		default:
		    errorMsg = "Wishlist error";

	    }
	    LOG.error(errorMsg, wishlistId, deleteWishlist.getStatusInfo().getReasonPhrase());
	    throw new WishlistException(deleteWishlist.getStatus(), "Deleting Wishlist failed");
	}
    }

}
