package com.veeva.vault.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.model.KanbanComponent;

@JsonIgnoreProperties
public class KanbanCardField extends KanbanComponent {

   @JsonProperty("field_api_name__c")
   public String getFieldApiName() {
      return this.getString("field_api_name__c");
   }

   public void setFieldApiName(String fieldApiName) {
      this.set("field_api_name__c", fieldApiName);
   }

   @JsonProperty("editable__c")
   public Boolean isEditable() {
      return this.getBoolean("editable__c");
   }

   public void setEditable(Boolean editable) {
      this.set("editable__c", editable);
   }

   @JsonProperty("order_by__c")
   public Boolean isOrderBy() {
      return this.getBoolean("order_by__c");
   }

   public void setOrderBy(Boolean orderBy) {
      this.set("order_by__c", orderBy);
   }

   @JsonProperty("visible__c")
   public Boolean isVisible() {
      return this.getBoolean("visible__c");
   }

   public void setVisible(Boolean visible) {
      this.set("visible__c", visible);
   }
}
