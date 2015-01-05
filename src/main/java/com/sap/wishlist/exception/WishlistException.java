package com.sap.wishlist.exception;

import javax.ws.rs.ClientErrorException;

public class WishlistException extends ClientErrorException {

    /**
     *
     */
    private static final long serialVersionUID = 2109023805798831836L;

    public WishlistException(int statusCode, String message) {
	super(message, statusCode);
    }

}
