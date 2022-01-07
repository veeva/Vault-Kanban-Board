package com.veeva.vault.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.veeva.vault.model.IntegrationRequest;

public class LambdaHandler implements RequestHandler<IntegrationRequest, Object> {
	// Request method called by AWS Lambda service
	public Object handleRequest(IntegrationRequest integrationRequest, Context context) {
		IntegrationHandler integrationHandler = new IntegrationHandler(integrationRequest);
		return integrationHandler.process().toJsonString();
	}
}