About Wishlist Java
===================
This is an example implementation of the YaaS "Wishlist" RAML based on Java. It uses the RAML definition generated by the SDK without modifications. It shows how to implement a basic service and how to integrate with other services on YaaS.

API Console
-----------

You can open the API Console in a separate window by using the following link: 
- [API Console](https://api.yaas.io/sap/java-wishlist/example)


Tenant Information
------------------

You can use `playground` as value for `hybris-tenant` (a.k.a. project id) as a sandbox when experimenting with the service.

Alternatively, in the Builder you need to [create a project](https://devportal.yaas.io/gettingstarted/setupaproject/index.html) containing a subscription to the package "Java Jersey Wishlist Example" with ID `logvqzfhlrk5`. 


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
  
See also [WishlistService.java](src/main/java/com/sap/wishlist/service/WishlistService.java).

### Email Service
A mail is sent to the wishlist owner when a wishlist is created. For more details, have a look at method `sendMail` in [WishlistService.java](src/main/java/com/sap/wishlist/service/WishlistService.java).

### Media Repository
The endpoint `/wishlists/{wishlistId}/media` enables you to:
  - Get a list of all media for the wishlist
  - Create a new medium
  - Delete a specific medium based on an id

See also [WishlistMediaService.java](src/main/java/com/sap/wishlist/service/WishlistMediaService.java)

### Customer Service
When a wishlist is being created, then the implementation checks if its owner exists as customer. 
You can find the details at the beginning of the `post` method in [WishlistService.java](src/main/java/com/sap/wishlist/service/WishlistService.java).


Purpose & Benefits
------------------

Showcase how a service can be written using Java. Demonstrate the integration with other services on YaaS, including authentication. Topics covered:
- Usage of Spring framework
- Property handling
- Authentication with YaaS platform
- Consumption of YaaS services
- Deployment to CloudFoundry
- Testing


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

In order to build the service locally, you need to [create an *Application*](https://devportal.yaas.io/gettingstarted/createanapplication/index.html), within your *Project/Site*, which subscribes to the following packages and scopes:
- Email Package (Scopes: `hybris.email_send`, `hybris.email_manage`)
- Core Package (Scopes: `hybris.media_manage`, `hybris.document_manage`, `hybris.document_view`)
- Commerce as a Service (Scope: `hybris.customer_read`)

You then have to set the following environment variables:
- `YAAS_CLIENT`: your *Application*'s *Identifier*
- `YAAS_CLIENT_ID`: your *Application*'s *Client ID*
- `YAAS_CLIENT_SECRET`: Your *Application*'s *Client Secret*
- `YAAS_CLIENT_IS_APPLICATION` parameter in [default.properties](src/main/resources/default.properties) to “true” (As you are using the  Application's credentials, you're running in a single tenant mode. Therefore you have to set this to true flag)"

You have to store the ID of your *Project/Site* as `TENANT` in [TestConstants.java](src/test/java/com/sap/wishlist/api/TestConstants.java).

You also need to [create a customer](https://devportal.yaas.io/services/customer/latest/index.html#CreateNewAccount) in your tenant and store it as `CUSTOMER` in [TestConstants.java](src/test/java/com/sap/wishlist/api/TestConstants.java).

Finally, use `mvn clean install` to build the service and run the tests.

To run it locally you can call `mvn jetty:run` and navigate to the local [API-Console](http://localhost:8080)

FAQ / Troubleshooting
---------------------

If you get failed tests while building with `mvn clean install`, e.g. *"response code expected:201 but was:500"*, 
then it probably means that the test cannot connect to the core services due to your proxy settings.

Hint: In that case, you might want to try it out with:

    mvn clean install -Dhttp.proxyPort=8080 -Dhttp.proxyHost=proxy.wdf.sap.corp -Dhttps.proxyPort=8080 -Dhttps.proxyHost=proxy.wdf.sap.corp
