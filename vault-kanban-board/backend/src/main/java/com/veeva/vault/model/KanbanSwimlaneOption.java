package com.veeva.vault.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.VaultModel;

@JsonIgnoreProperties
public class KanbanSwimlaneOption extends VaultModel {
	@JsonProperty("api_name__c")
	@JsonAlias({"name", "id"})
	public String getApiName() { return this.getString("api_name__c"); }
	public void setApiName(String apiName) { this.set("api_name__c", apiName); }

	@JsonProperty("label")
	@JsonAlias({"name__v"})
	public String getLabel() { return this.getString("label"); }
	public void setLabel(String label) { this.set("label", label); }
}
