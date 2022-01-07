package com.veeva.vault.handler;

import com.veeva.vault.handler.KanbanHandler;
import com.veeva.vault.model.IntegrationRequest;
import com.veeva.vault.model.IntegrationResponse;
import com.veeva.vault.util.Environment;
import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.client.VaultClientBuilder;
import com.veeva.vault.vapil.api.client.VaultClientId;
import com.veeva.vault.vapil.api.client.VaultClient.AuthenticationType;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class IntegrationHandler {

   private static Logger logger = Logger.getLogger(IntegrationHandler.class);
   private IntegrationRequest integrationRequest;
   private static IntegrationResponse integrationResponse;
   private VaultClient vaultClient;


   public IntegrationHandler(IntegrationRequest integrationRequest) {
      this.integrationRequest = integrationRequest;
      integrationResponse = new IntegrationResponse();
   }

   public IntegrationResponse process() {
      try {
         logger.debug("integration : start");
         String e = Environment.getLogLevel();
         logger.setLevel(Level.toLevel(e.toUpperCase()));
         logger.debug("logLevel : " + e);
         logger.debug("vaultDNS : " + this.integrationRequest.getBodyParams().getVaultDNS());
         logger.debug("sessionId : " + this.integrationRequest.getBodyParams().getSessionId());
         logger.debug("action : " + this.integrationRequest.getBodyParams().getAction());
         logger.debug("board : " + this.integrationRequest.getBodyParams().getBoard());
         if(this.integrationRequest.getBodyParams().getVaultDNS() != null && this.integrationRequest.getBodyParams().getSessionId() != null) {
            VaultClientId vaultClientId = new VaultClientId("veeva", "vault", "devsupport", Boolean.valueOf(true), "vault-kanban-board");
            this.vaultClient = VaultClientBuilder.newClientBuilder(AuthenticationType.SESSION_ID).withVaultClientId(vaultClientId).withVaultDNS(this.integrationRequest.getBodyParams().getVaultDNS()).withVaultSessionId(this.integrationRequest.getBodyParams().getSessionId()).build();
            logger.debug("vaultClient : " + this.vaultClient.hasSessionId());
            if(this.vaultClient != null && this.vaultClient.hasSessionId()) {
               integrationResponse.setResponseStatus(IntegrationResponse.ResponseStatus.SUCCESS);
            } else {
               integrationResponse.setResponseStatus(IntegrationResponse.ResponseStatus.FAILURE);
               integrationResponse.setResponseMessage("Invalid Vault Credentials");
            }

            if(this.integrationRequest.getBodyParams().getAction() != null) {
               logger.debug("actionType : " + this.integrationRequest.getBodyParams().getActionType().getValue());
               KanbanHandler kanbanHandler = new KanbanHandler(this.vaultClient, this.integrationRequest.getBodyParams().getBoard(), this.integrationRequest.getBodyParams().getPage());
               Object responseData = null;
               switch(null.$SwitchMap$com$veeva$vault$model$IntegrationRequestBodyParams$ActionType[this.integrationRequest.getBodyParams().getActionType().ordinal()]) {
               case 1:
                  responseData = kanbanHandler.getData();
                  break;
               case 2:
                  responseData = kanbanHandler.saveData(this.integrationRequest.getBodyParams().getData());
               }

               if(responseData != null) {
                  integrationResponse.setResponseStatus(IntegrationResponse.ResponseStatus.SUCCESS);
                  integrationResponse.setData(responseData);
                  integrationResponse.setResponseMessage("Success");
               } else {
                  integrationResponse.setResponseStatus(IntegrationResponse.ResponseStatus.FAILURE);
                  integrationResponse.setResponseMessage("Error");
               }
            }
         } else {
            logger.error("integration : missing required parameters");
            integrationResponse.setResponseStatus(IntegrationResponse.ResponseStatus.FAILURE);
            integrationResponse.setResponseMessage("Missing required parameters");
         }

         logger.debug(integrationResponse.toJsonString());
         logger.debug("integration: end");
      } catch (Exception var5) {
         logger.error(var5.getMessage());
         integrationResponse.setResponseStatus(IntegrationResponse.ResponseStatus.FAILURE);
         integrationResponse.setResponseMessage(var5.getMessage());
      }

      return integrationResponse;
   }

}
