package com.sap.wishlist.client.document;

import com.sap.wishlist.api.generated.Wishlist;

public class DocumentWishlist extends Wishlist {

    @javax.validation.constraints.Pattern(regexp = "^.+")
    @javax.validation.constraints.NotNull
    private com.sap.wishlist.client.document.DocumentMetaData _metaData;

    public com.sap.wishlist.client.document.DocumentMetaData getMetadata()
    {
	return _metaData;
    }

    public void setMetadata(final com.sap.wishlist.client.document.DocumentMetaData _metaData)
    {
	this._metaData = _metaData;
    }

}
