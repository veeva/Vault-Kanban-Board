package com.veeva.vault.model;

import com.fasterxml.jackson.annotation.*;
import com.veeva.vault.vapil.api.model.VaultModel;

@JsonIgnoreProperties
public class IntegrationResponse extends VaultModel {

	@JsonProperty("responseMessage")
	public String getResponseMessage() {
		return this.getString("responseMessage");
	}
	public void setResponseMessage(String responseMessage) {
		this.set("responseMessage",responseMessage );
	}

	public enum ResponseStatus {
		FAILURE("FAILURE"),
		SUCCESS("SUCCESS"),
		UNDEFINED("UNDEFINED");

		String value;
		ResponseStatus(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

	@JsonProperty("responseStatus")
	public ResponseStatus getResponseStatus() {
		return (ResponseStatus) this.get("responseStatus");
	}

	@JsonSetter
	public void setResponseStatus(ResponseStatus responseStatus) {
		this.set("responseStatus", responseStatus);
	}

	@JsonProperty("data")
	public Object getData() {
		return this.get("data");
	}
	public void setData(Object data) {
		this.set("data", data);
	}
}
