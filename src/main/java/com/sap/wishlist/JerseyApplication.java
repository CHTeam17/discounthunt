/*
 * Created with [y] hybris Service SDK version 3.3.4.
 */
package com.sap.wishlist;

import com.hybris.api.console.web.ApiConsoleFeature;

import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.server.validation.ValidationFeature;

import com.sap.wishlist.api.generated.ApiFeature;

/**
 * Defines the REST application.
 */
public class JerseyApplication extends ResourceConfig
{
	/**
	 * Initialized the jersey application.
	 */
	public JerseyApplication()
	{
		// json support
		register(JacksonFeature.class);

		// resources
		register(ApiFeature.class);

		// apiconsole
		register(ApiConsoleFeature.class);

		// bean validation
		register(ValidationFeature.class);
		property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);
	}
}
