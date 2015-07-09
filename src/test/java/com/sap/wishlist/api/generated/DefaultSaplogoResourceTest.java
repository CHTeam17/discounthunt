/*
 * [y] hybris Platform
 * 
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 */
package com.sap.wishlist.api.generated;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.server.ResourceConfig;
import org.junit.Assert;
import org.junit.Test;

import com.hybris.patterns.schemas.ResourceLocation;
import com.hybris.patterns.traits.YaasAwareTrait;
import com.sap.wishlist.api.Constants;

public final class DefaultSaplogoResourceTest extends AbstractResourceTest
{
    /**
     * Server side root resource /saplogo, evaluated with some default value(s).
     */
    private static final String ROOT_RESOURCE_PATH = "/saplogo";
    final static String SAP_LOGO_FILE_PATH = DefaultSaplogoResource.SAP_LOGO_FILE_PATH;

    private String mediaId;

    public void tearDown() throws Exception {
	if (mediaId != null) deleteSapLogo(mediaId);
	super.tearDown();
    }

    @Test
    public void testPostGetDelete() throws MalformedURLException, NoSuchAlgorithmException, IOException
    {
	ResourceLocation responsePostCreated = createSapLogo();

	mediaId = responsePostCreated.getId();
	Assert.assertNotNull(mediaId);

	URL mediaLink = responsePostCreated.getLink().toURL();
	Assert.assertNotNull(mediaLink);

	String expMD5 = computeMD5ChecksumForFile(SAP_LOGO_FILE_PATH);
	String actMD5 = computeMD5ChecksumForURL(mediaLink);
	Assert.assertEquals("File on media repository is different from file sent", expMD5, actMD5);
    }

    private ResourceLocation createSapLogo() {
	final WebTarget targetPost = getRootTarget(ROOT_RESOURCE_PATH).path("");
	final Response responsePost = targetPost.request()
		.header(YaasAwareTrait.Headers.TENANT, Constants.TENANT)
		.post(null);

	Assert.assertNotNull("Response must not be null", responsePost);
	Assert.assertEquals("Response does not have expected response code", Status.CREATED.getStatusCode(),
		responsePost.getStatus());

	ResourceLocation responsePostCreated = responsePost.readEntity(ResourceLocation.class);
	return responsePostCreated;
    }

    private void deleteSapLogo(String mediaId) {
	final WebTarget targetDelete = getRootTarget(ROOT_RESOURCE_PATH).path("/" + mediaId);
	final Response responseDelete = targetDelete.request()
		.header(YaasAwareTrait.Headers.TENANT, Constants.TENANT)
		.delete();

	Assert.assertNotNull("Response must not be null", responseDelete);
	Assert.assertEquals("Response does not have expected response code", Status.NO_CONTENT.getStatusCode(),
		responseDelete.getStatus());
    }

    @Override
    protected ResourceConfig configureApplication()
    {
	final ResourceConfig application = new ResourceConfig();
	application.register(DefaultSaplogoResource.class);
	return application;
    }

    public static String computeMD5ChecksumForFile(String filename) throws NoSuchAlgorithmException, IOException {
	InputStream inputStream = new FileInputStream(filename);
	return computeMD5ChecksumForInputStream(inputStream);
    }

    public static String computeMD5ChecksumForURL(URL input) throws MalformedURLException, IOException,
	    NoSuchAlgorithmException {
	InputStream inputStream = input.openStream();
	return computeMD5ChecksumForInputStream(inputStream);
    }

    private static String computeMD5ChecksumForInputStream(InputStream inputStream) throws NoSuchAlgorithmException,
	    IOException {
	MessageDigest md = MessageDigest.getInstance("MD5");

	try {
	    InputStream digestInputStream = new DigestInputStream(inputStream, md);

	    while (digestInputStream.read() > 0) {
		;
	    }
	} finally {
	    inputStream.close();
	}
	byte[] digest = md.digest();
	StringBuffer sb = new StringBuffer();

	for (int i = 0; i < digest.length; i++) {
	    sb.append(Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1));
	}
	return sb.toString();
    }

}