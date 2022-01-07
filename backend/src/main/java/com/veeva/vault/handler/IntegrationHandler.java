package com.veeva.vault.handler;

import com.veeva.vault.util.Environment;
import com.veeva.vault.model.IntegrationRequest;
import com.veeva.vault.model.IntegrationResponse;
import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.client.VaultClientBuilder;
import com.veeva.vault.vapil.api.client.VaultClientId;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class IntegrationHandler {
	private static Logger logger = Logger.getLogger(IntegrationHandler.class);

	private IntegrationRequest integrationRequest;
	private static IntegrationResponse integrationResponse;

	private VaultClient vaultClient;

	public IntegrationHandler(IntegrationRequest integrationRequest) {
		this.integrationRequest = integrationRequest;
		this.integrationResponse = new IntegrationResponse();
	}

	public IntegrationResponse process() {
		try {
			logger.debug("integration : start");

			String logLevel = Environment.getLogLevel();
			logger.setLevel(Level.toLevel(logLevel.toUpperCase()));
			logger.debug("logLevel : " + logLevel);

			logger.debug("vaultDNS : " + integrationRequest.getBodyParams().getVaultDNS());
			logger.debug("sessionId : " + integrationRequest.getBodyParams().getSessionId());
			logger.debug("action : " + integrationRequest.getBodyParams().getAction());
			logger.debug("board : " + integrationRequest.getBodyParams().getBoard());

			if (integrationRequest.getBodyParams().getVaultDNS() != null && integrationRequest.getBodyParams().getSessionId() != null) {
				VaultClientId vaultClientId = new VaultClientId(
						"veeva",
						"vault",
						"devsupport",
						true,
						"vault-kanban-board");

				vaultClient = VaultClientBuilder
						.newClientBuilder(VaultClient.AuthenticationType.SESSION_ID)
						.withVaultClientId(vaultClientId) //required
						.withVaultDNS(integrationRequest.getBodyParams().getVaultDNS() ) //required
						.withVaultSessionId(integrationRequest.getBodyParams().getSessionId())
						.build();

				logger.debug("vaultClient : " + vaultClient.hasSessionId());
				if (vaultClient != null && vaultClient.hasSessionId()) {
					integrationResponse.setResponseStatus(IntegrationResponse.ResponseStatus.SUCCESS);
				}
				else {
					integrationResponse.setResponseStatus(IntegrationResponse.ResponseStatus.FAILURE);
					integrationResponse.setResponseMessage("Invalid Vault Credentials");
				}

				if(integrationRequest.getBodyParams().getAction() != null) {
					logger.debug("actionType : " + integrationRequest.getBodyParams().getActionType().getValue());

					KanbanHandler kanbanHandler = new KanbanHandler(
							vaultClient,
							integrationRequest.getBodyParams().getBoard(),
							integrationRequest.getBodyParams().getPage()
					);
					Object responseData = null;

					switch (integrationRequest.getBodyParams().getActionType()) {
						case GET_KANBAN_DATA:
							responseData = kanbanHandler.getData();
							break;
						case SET_KANBAN_DATA:
							responseData = kanbanHandler.saveData(integrationRequest.getBodyParams().getData());
							break;
						default:
							//no action
							break;
					}

					if (responseData != null) {
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
		} catch (Exception e) {
			logger.error(e.getMessage());
			integrationResponse.setResponseStatus(IntegrationResponse.ResponseStatus.FAILURE);
			integrationResponse.setResponseMessage(e.getMessage());
		}
		return integrationResponse;
	}
}
