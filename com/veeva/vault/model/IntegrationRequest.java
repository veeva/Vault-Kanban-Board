package com.veeva.vault.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.veeva.vault.model.IntegrationRequestBodyParams;
import com.veeva.vault.util.Environment;
import com.veeva.vault.vapil.api.model.VaultModel;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

@JsonIgnoreProperties
public class IntegrationRequest extends VaultModel {

   private static Logger logger = Logger.getLogger(IntegrationRequest.class);
   @JsonIgnore
   private IntegrationRequestBodyParams bodyParams;


   public IntegrationRequest() {
      logger.setLevel(Level.toLevel(Environment.getLogLevel()));
   }

   @JsonIgnore
   public IntegrationRequestBodyParams getBodyParams() {
      return this.bodyParams;
   }

   @JsonIgnore
   public void setBodyParams(IntegrationRequestBodyParams bodyParams) {
      this.bodyParams = bodyParams;
   }

   @JsonProperty("body")
   public String getBody() {
      return this.getString("body");
   }

   public void setBody(String body) {
      try {
         logger.debug("body : " + body);
         this.set("body", body);
         IntegrationRequestBodyParams e = (IntegrationRequestBodyParams)(new ObjectMapper()).readValue(body, IntegrationRequestBodyParams.class);
         this.bodyParams = e;
      } catch (Exception var3) {
         logger.error(var3.getMessage());
         this.bodyParams = new IntegrationRequestBodyParams();
      }

   }

}
