package com.sap.wishlist.client;

import java.net.URI;
import java.net.URISyntaxException;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.sap.wishlist.api.generated.Wishlist;
import com.sap.wishlist.client.document.DocumentWishlist;
import com.sap.wishlist.client.document.ResponseCreated;
import com.sap.wishlist.exception.WishlistException;

@ManagedBean
public class DocumentClient {

    @Inject
    private RestClient restClient;

    public static final String WISHLIST_PATH = "wishlist";
    public static final String DATA_NAMESPACE = "data";
    private static final Logger LOG = LoggerFactory.getLogger(DocumentClient.class);

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

    public DocumentWishlist getWishlist(final String tenant,
	    final String wishlistId) throws ClientErrorException {

	String[] path = { tenant, appPath, DATA_NAMESPACE, WISHLIST_PATH, wishlistId };

	final Response wishlistResponse = restClient.get(tenant, uri, path, null, null);
	if (wishlistResponse.getStatus() != Response.Status.OK.getStatusCode()) {
	    final String errorMsg = "Wishlist not found {} {}";
	    LOG.error(errorMsg, wishlistId, wishlistResponse.getStatusInfo().getReasonPhrase());
	    throw new WishlistException(wishlistResponse.getStatus(), "Not available");
	}

	return wishlistResponse.readEntity(DocumentWishlist.class);

    }

    public ResponseCreated createWishlist(final String tenant,
	    final Wishlist documetWishlist,
	    final String wishlistId) throws ClientErrorException {

	String[] path = { tenant, appPath, DATA_NAMESPACE, WISHLIST_PATH, wishlistId };

	final Response readWishlist = restClient.post(tenant, uri, path, null, null, Entity.json(documetWishlist));
	if (readWishlist.getStatus() != Response.Status.CREATED.getStatusCode()) {
	    final String errorMsg = "Wishlist cloud not be created {} {}";
	    LOG.error(errorMsg, wishlistId, readWishlist.getStatusInfo().getReasonPhrase());
	    throw new WishlistException(readWishlist.getStatus(), "Creating Wishlist failed");
	} else {
	    return readWishlist.readEntity(ResponseCreated.class);
	}
    }

    public void updateWhishlist(final String tenant,
	    final Wishlist wishlist,
	    final String wishlistId) throws ClientErrorException {

	String[] path = { tenant, appPath, DATA_NAMESPACE, WISHLIST_PATH, wishlistId };
	MultivaluedMap<String, Object> header = new MultivaluedHashMap<String, Object>();

	final Response readWishlist = restClient.put(tenant, uri, path, null, header, Entity.json(wishlistId));
	if (readWishlist.getStatus() != Response.Status.OK.getStatusCode()) {
	    final String errorMsg = "Wishlist not found {} {} ";
	    LOG.error(errorMsg, wishlistId, readWishlist.getStatusInfo().getReasonPhrase());
	    throw new WishlistException(readWishlist.getStatus(), "Updating Wishlist failed");
	}
    }

}
