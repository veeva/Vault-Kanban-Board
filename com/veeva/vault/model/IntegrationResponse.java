package com.veeva.vault.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.veeva.vault.vapil.api.model.VaultModel;

@JsonIgnoreProperties
public class IntegrationResponse extends VaultModel {

   @JsonProperty("responseMessage")
   public String getResponseMessage() {
      return this.getString("responseMessage");
   }

   public void setResponseMessage(String responseMessage) {
      this.set("responseMessage", responseMessage);
   }

   @JsonProperty("responseStatus")
   public IntegrationResponse.ResponseStatus getResponseStatus() {
      return (IntegrationResponse.ResponseStatus)this.get("responseStatus");
   }

   @JsonSetter
   public void setResponseStatus(IntegrationResponse.ResponseStatus responseStatus) {
      this.set("responseStatus", responseStatus);
   }

   @JsonProperty("data")
   public Object getData() {
      return this.get("data");
   }

   public void setData(Object data) {
      this.set("data", data);
   }

   public static enum ResponseStatus {

      FAILURE("FAILURE", 0, "FAILURE"),
      SUCCESS("SUCCESS", 1, "SUCCESS"),
      UNDEFINED("UNDEFINED", 2, "UNDEFINED");
      String value;
      // $FF: synthetic field
      private static final IntegrationResponse.ResponseStatus[] $VALUES = new IntegrationResponse.ResponseStatus[]{FAILURE, SUCCESS, UNDEFINED};


      private ResponseStatus(String var1, int var2, String value) {
         this.value = value;
      }

      public String getValue() {
         return this.value;
      }

   }
}
