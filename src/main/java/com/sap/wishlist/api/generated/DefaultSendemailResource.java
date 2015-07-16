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

import java.io.File;
import java.util.Collections;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.hybris.authorization.AccessToken;
import com.hybris.authorization.AuthorizationScope;
import com.hybris.authorization.DiagnosticContext;
import com.hybris.authorization.integration.AuthorizedExecutionCallback;
import com.hybris.authorization.integration.AuthorizedExecutionTemplate;
import com.sap.wishlist.client.email.EmailServiceClient;
import com.sap.wishlist.email.Email;
import com.sap.wishlist.email.EmailTemplateDefinition;
import com.sap.wishlist.email.EmailWelcomeTemplate;
import com.sap.wishlist.email.TemplateAttributeDefinition;
import com.sap.wishlist.email.TemplateAttributeValue;
import com.sap.wishlist.utility.AuthorizationHelper;

@Component("apiSendemailResource")
@Singleton
public class DefaultSendemailResource implements SendemailResource
{
    private final String TEMPLATE_WELCOME_CODE = "welcome";
    private final String TEMPLATE_FILE_TYPE_BODY = "body";
    private final String TEMPLATE_FILE_TYPE_SUBJECT = "subject";
    private final EmailServiceClient emailServiceClient;

    private static final Logger LOG = LoggerFactory.getLogger(DefaultSendemailResource.class);

    @javax.ws.rs.core.Context
    private javax.ws.rs.core.UriInfo uriInfo;

    @Inject
    private AuthorizedExecutionTemplate authorizedExecutionTemplate;
    @Inject
    private AuthorizationHelper authorizationHelper;
    @Value("${YAAS_CLIENT}")
    private String client;

    public DefaultSendemailResource() {
	emailServiceClient = new EmailServiceClient(EmailServiceClient.DEFAULT_BASE_URI);
    }

    /* POST / */
    @Override
    public Response post(YaasAwareParameters yaasAware)
    {
	if (sendMail(yaasAware))
	{
	    return Response.created(uriInfo.getAbsolutePath()).build();
	} else {
	    return Response.status(400).build();
	}
    }

    private boolean sendMail(final YaasAwareParameters yaasAware) {

	final EmailTemplateDefinition emailTemplateDefinition = createTemplateDefinition(TEMPLATE_WELCOME_CODE);

	LOG.debug("requesting template creation");

	Response response = authorizedExecutionTemplate.executeAuthorized(
		new AuthorizationScope(yaasAware.getHybrisTenant(), authorizationHelper.getScopes()),
		new DiagnosticContext(yaasAware.getHybrisRequestId(), yaasAware.getHybrisHop()),
		new AuthorizedExecutionCallback<Response>()
		{
		    @Override
		    public Response execute(final AccessToken token)
		    {
			return emailServiceClient.tenantTemplates(yaasAware.getHybrisTenant())
				.preparePost()
				.withAuthorization(authorizationHelper.buildToken(token))
				.withPayload(Entity.json(emailTemplateDefinition))
				.execute();
		    }
		});

	LOG.debug("template creation returned with status {}", response.getStatus());

	if (response.getStatus() == Response.Status.CREATED.getStatusCode()) {
	    EmailWelcomeTemplate templateSubject = createTemplateSubject(TEMPLATE_WELCOME_CODE,
		    yaasAware.getHybrisTenant());
	    uploadWelcomeTemplate(yaasAware, templateSubject);

	    EmailWelcomeTemplate templateBody = createTemplateBody(TEMPLATE_WELCOME_CODE, yaasAware.getHybrisTenant());
	    uploadWelcomeTemplate(yaasAware, templateBody);

	}
	final Email eMail = createEMail(yaasAware.getHybrisUser());

	response = authorizedExecutionTemplate.executeAuthorized(
		new AuthorizationScope(yaasAware.getHybrisTenant(), authorizationHelper.getScopes()),
		new DiagnosticContext(yaasAware.getHybrisRequestId(), yaasAware.getHybrisHop()),
		new AuthorizedExecutionCallback<Response>()
		{
		    @Override
		    public Response execute(final AccessToken token)
		    {
			return emailServiceClient.tenantSend(yaasAware.getHybrisTenant())
				.preparePost()
				.withAuthorization(authorizationHelper.buildToken(token))
				.withPayload(Entity.json(eMail))
				.execute();
		    }
		});

	if (response.getStatus() != Response.Status.CREATED.getStatusCode()) {
	    final String errorMsg = "Email cloud not be send {} {}";
	    LOG.error(errorMsg, yaasAware.getHybrisUser(), response.getStatusInfo().getReasonPhrase());
	    return false;
	}
	return true;
    }

    private EmailTemplateDefinition createTemplateDefinition(String templateCode) {
	final EmailTemplateDefinition emailTemplateDefinition = new EmailTemplateDefinition();
	emailTemplateDefinition.setCode(templateCode);
	emailTemplateDefinition.setOwner(this.client);
	emailTemplateDefinition.setName("Welcome Mail");
	emailTemplateDefinition.setDescription("Template for Welcome Mail");
	emailTemplateDefinition.setTemplateAttributeDefinitions(Collections
		.singletonList(new TemplateAttributeDefinition("eMail", false)));
	return emailTemplateDefinition;
    }

    private EmailWelcomeTemplate createTemplateBody(String templateCode, final String tenant) {
	EmailWelcomeTemplate templateBody = EmailWelcomeTemplate
		.builder()
		.setFilePath(
			"templates" + File.separator
				+ templateCode + "-body.vm")
		.setCode(templateCode)
		.setOwner(tenant)
		.setFileType(TEMPLATE_FILE_TYPE_BODY).setLocale("en")
		.build();
	return templateBody;
    }

    private EmailWelcomeTemplate createTemplateSubject(String templateCode, final String tenant) {
	EmailWelcomeTemplate templateSubject = EmailWelcomeTemplate
		.builder()
		.setFilePath(
			"templates" + File.separator
				+ templateCode + "-subject.vm")
		.setCode(templateCode)
		.setOwner(tenant)
		.setFileType(TEMPLATE_FILE_TYPE_SUBJECT).setLocale("en")
		.build();
	return templateSubject;
    }

    private Email createEMail(String eMailReceiver) {
	final Email eMail = new Email();
	eMail.setToAddress(eMailReceiver);
	eMail.setToName("Test Receiver");
	eMail.setFromAddress("noreply@cf.hybris.com");
	eMail.setTemplateOwner(this.client);
	eMail.setTemplateCode(TEMPLATE_WELCOME_CODE);
	eMail.setLocale("en");
	eMail.setAttributes(Collections.singletonList(new TemplateAttributeValue("eMail", eMailReceiver)));
	return eMail;
    }

    private Response uploadWelcomeTemplate(final YaasAwareParameters yaasAware,
	    final EmailWelcomeTemplate template) {
	final String client = this.client;

	return authorizedExecutionTemplate.executeAuthorized(
		new AuthorizationScope(yaasAware.getHybrisTenant(), authorizationHelper.getScopes()),
		new DiagnosticContext(yaasAware.getHybrisRequestId(), yaasAware.getHybrisHop()),
		new AuthorizedExecutionCallback<Response>()
		{
		    @Override
		    public Response execute(final AccessToken token)
		    {
			return emailServiceClient
				.tenantTemplatesClient(yaasAware.getHybrisTenant(), client)
				.code(template.getCode())
				.fileType(template.getFileType())
				.preparePut()
				.withAuthorization(authorizationHelper.buildToken(token))
				.withPayload(
					Entity.entity(template.getDataStream(), MediaType.APPLICATION_OCTET_STREAM_TYPE))
				.execute();
		    }
		});
    }
}