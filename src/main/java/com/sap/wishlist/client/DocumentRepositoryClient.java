package com.sap.wishlist.client;

import javax.annotation.ManagedBean;

@ManagedBean
public class DocumentRepositoryClient extends com.sap.wishlist.client.documentrepository.DocumentRepositoryClient {
    public DocumentRepositoryClient() {
	super(DEFAULT_BASE_URI);
    }
}