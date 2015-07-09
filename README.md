About Wishlist Java [![Build Status](https://travis-ci.mo.sap.corp/CEC-Cloud-Platform-Enablement/wishlist_java.svg?token=gxowxy5qyyMiZYs28ztC)](https://travis-ci.mo.sap.corp/CEC-Cloud-Platform-Enablement/wishlist_java)
====================
This is an example implementation of the YaaS "Wishlist" RAML based on Java. It uses the RAML definition generated by the SDK without modifications. It shows how to implement a basic service and how to integrate with other services on YaaS.

API Console
-----------

You can open the API Console in a separate window by using the following links: 
- [API Console - Internal](http://wishlist-java.cf3.hybris.com)
- [API Console - External](https://api.yaas.io/wishlist-java)


Tenant Information
------------------

You can use `cecwishlist` as value for `hybris-tenant` (a.k.a. project id) as a sandbox when experimenting with the service.


API Overview
------------

This service provides REST endpoints for:
- Interacting with wishlists in a CRUD fashion
  - Getting a list of all wishlists within a tenant
  - Creating a new wishlist
  - Getting a specific wishlist based on an id
  - Updating a specific wishlist based on an id
  - Deleting a specific wishlist based on an id
- Sending an e-mail


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


How to Build and Test
---------------------

Use `mvn clean install` to build the service and run the tests.


FAQ / Troubleshooting
---------------------

If you get failed tests while building with `mvn clean install`, e.g. *"response code expected:201 but was:500"*, 
then it probably means that the test cannot connect to the core services due to your proxy settings.

Hint: In that case, you might want to try it out with:

    mvn clean install -Dhttp.proxyPort=8080 -Dhttp.proxyHost=proxy.wdf.sap.corp -Dhttps.proxyPort=8080 -Dhttps.proxyHost=proxy.wdf.sap.corp
