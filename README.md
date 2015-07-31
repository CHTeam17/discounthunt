About Wishlist Java
===================
This is an example implementation of the YaaS "Wishlist" RAML based on Java. It uses the RAML definition generated by the SDK without modifications. It shows how to implement a basic service and how to integrate with other services on YaaS.

API Console
-----------

You can open the API Console in a separate window by using the following links: 
- [API Console - Internal](http://java-wishlist-v1.cf3.hybris.com)
- [API Console - External](https://api.stage.yaas.io/serviceexamples/javawishlist/v1)


Tenant Information
------------------

You can use `wishlistexamples` as value for `hybris-tenant` (a.k.a. project id) as a sandbox when experimenting with the service.


API Overview
------------

This service provides REST endpoints for interacting with several YaaS core and commerce services.

### Document Repository
The endpoint `/wishlists` enables you to:
- Interact with wishlists in a CRUD fashion
  - Get a list of all wishlists within a tenant
  - Create a new wishlist
  - Get a specific wishlist based on an id
  - Update a specific wishlist based on an id
  - Delete a specific wishlist based on an id
  
The endpoint `/wishlists/{wishlistId}/media` enables you to:
  - Get a list of all media for the wishlist
  - Create a new media
  - Delete a specific media based on an id
  
See also [DefaultWishlistsResource.java](src/main/java/com/sap/wishlist/api/generated/DefaultWishlistsResource.java)

### Email Service
Mail delivery happens when a wishlist is created. To get more details have look after the post message to the document repository

See also [WishlistService.java](src/main/java/com/sap/wishlist/service/WishlistService.java)

### Media Repository
The endpoint /media enables you to upload files for the wishlist.

See also [WishlistMediaService.java](src/main/java/com/sap/wishlist/service/WishlistMediaService.java)

### Customer Service
When a wishlist is being created then is checking if the owner exists as customer. You can find the detail in the post message as first step.

See also [WishlistService.java](src/main/java/com/sap/wishlist/service/WishlistService.java)

Purpose & Benefits
------------------

Showcase how a service can be written using Java. Demonstrate the integration with other services on YaaS, including authentication. Topics covered:
- Usage of Spring framework
- Property handling
- Authentication with YaaS platform
- Consumption of YaaS services
- Deployment to CloudFoundry
- Testing


Prerequisite
------------

The corresponding API product has to be scoped within your tenant.


Dependencies
------------

- Core Services
  - [OAuth2 Service](https://devportal.yaas.io/services/oauth2/latest/index.html)
  - [Document Repository](https://devportal.yaas.io/services/documentrepository/latest/index.html)
  - [Email](https://devportal.yaas.io/services/email/latest/index.html)
  - [Media Repository](https://devportal.yaas.io/services/mediarepository/latest/index.html)
  - [Customer](https://devportal.yaas.io/services/customer/latest/index.html)


How to Build and Test
---------------------
First you need to create a customer at you tenant and change the [customerid](src/test/java/com/sap/wishlist/api/TestConstants.java)   

Use `mvn clean install` to build the service and run the tests.


FAQ / Troubleshooting
---------------------

If you get failed tests while building with `mvn clean install`, e.g. *"response code expected:201 but was:500"*, 
then it probably means that the test cannot connect to the core services due to your proxy settings.

Hint: In that case, you might want to try it out with:

    mvn clean install -Dhttp.proxyPort=8080 -Dhttp.proxyHost=proxy.wdf.sap.corp -Dhttps.proxyPort=8080 -Dhttps.proxyHost=proxy.wdf.sap.corp
