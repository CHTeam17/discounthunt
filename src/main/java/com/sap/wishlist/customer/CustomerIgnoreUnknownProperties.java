package com.sap.wishlist.customer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sap.wishlist.api.generated.Customer;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerIgnoreUnknownProperties extends Customer {
    
}
