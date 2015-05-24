package com.sap.wishlist.utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.ManagedBean;

import org.springframework.beans.factory.annotation.Value;

import com.hybris.authorization.AccessToken;

@ManagedBean
public class AuthorizationHelper {
    @Value("${OAUTH2_SCOPES}")
    private String scopes;

    public List<String> getScopes() {
	return new ArrayList<String>(Arrays.asList(this.scopes.split(" ")));
    }

    public String buildToken(AccessToken token) {
	return token.getType() + " " + token.getValue();
    }
}