package com.sap.wishlist.client;

import javax.annotation.ManagedBean;
import javax.ws.rs.client.ClientBuilder;

import org.glassfish.jersey.media.multipart.MultiPartFeature;

@ManagedBean
public class MediaRepositoryClient extends com.sap.wishlist.client.mediaRepository.MediaRepositoryClient {
    public MediaRepositoryClient() {
	super(DEFAULT_BASE_URI, ClientBuilder.newClient().register(MultiPartFeature.class));
    }
}