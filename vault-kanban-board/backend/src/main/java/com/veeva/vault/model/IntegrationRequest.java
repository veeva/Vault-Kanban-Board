package com.veeva.vault.model;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.veeva.vault.util.Environment;
import com.veeva.vault.vapil.api.model.VaultModel;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

@JsonIgnoreProperties
public class IntegrationRequest extends VaultModel {
	private static Logger logger = Logger.getLogger(IntegrationRequest.class);

	public IntegrationRequest() {
		logger.setLevel(Level.toLevel(Environment.getLogLevel()));
	}

	@JsonIgnore
	private IntegrationRequestBodyParams bodyParams;

	@JsonIgnore
	public IntegrationRequestBodyParams getBodyParams() {
		return bodyParams;
	}

	@JsonIgnore
	public void setBodyParams(IntegrationRequestBodyParams bodyParams) {
		this.bodyParams = bodyParams;
	}

	@JsonProperty("body")
	public String getBody() { return this.getString("body"); }
	public void setBody(String body) {
		try {
			logger.debug("body : " + body);
			this.set("body", body);
			IntegrationRequestBodyParams integrationRequestBody = new ObjectMapper().readValue(body, IntegrationRequestBodyParams.class);
			this.bodyParams = integrationRequestBody;
		} catch (Exception e) {
			logger.error(e.getMessage());
			this.bodyParams = new IntegrationRequestBodyParams();
		}
	}
}
