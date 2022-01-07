package com.veeva.vault.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.VaultModel;

@JsonIgnoreProperties
public class KanbanComponent extends VaultModel {

   @JsonProperty("api_name__c")
   public String getApiName() {
      return this.getString("api_name__c");
   }

   public void setApiName(String apiName) {
      this.set("api_name__c", apiName);
   }

   @JsonProperty("id")
   public String getId() {
      return this.getString("id");
   }

   public void setId(String id) {
      this.set("id", id);
   }

   @JsonProperty("label__c")
   public String getLabel() {
      return this.getString("label__c");
   }

   public void setLabel(String label) {
      this.set("label__c", label);
   }

   @JsonProperty("name__v")
   public String getName() {
      return this.getString("name__v");
   }

   public void setName(String name) {
      this.set("name__v", name);
   }

   @JsonProperty("sort_order__c")
   public Integer getSortOrder() {
      return this.getInteger("sort_order__c");
   }

   public void setSortOrder(Integer sortOrder) {
      this.set("sort_order__c", sortOrder);
   }
}
