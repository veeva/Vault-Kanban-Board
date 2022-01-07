package com.veeva.vault.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.veeva.vault.handler.IntegrationHandler;
import com.veeva.vault.model.IntegrationRequest;

public class LambdaHandler implements RequestHandler<IntegrationRequest, Object> {

   public Object handleRequest(IntegrationRequest integrationRequest, Context context) {
      IntegrationHandler integrationHandler = new IntegrationHandler(integrationRequest);
      return integrationHandler.process().toJsonString();
   }
}
