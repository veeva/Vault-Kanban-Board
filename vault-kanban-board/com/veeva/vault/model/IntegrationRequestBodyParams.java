package com.veeva.vault.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.model.KanbanBoard;
import com.veeva.vault.vapil.api.model.VaultModel;
import org.apache.commons.lang3.EnumUtils;

@JsonIgnoreProperties
public class IntegrationRequestBodyParams extends VaultModel {

   @JsonProperty("action")
   public String getAction() {
      return this.getString("action");
   }

   public void setAction(String action) {
      this.set("action", action);
   }

   @JsonIgnore
   public IntegrationRequestBodyParams.ActionType getActionType() {
      return this.getActionType(IntegrationRequestBodyParams.ActionType.UNDEFINED);
   }

   @JsonIgnore
   public IntegrationRequestBodyParams.ActionType getActionType(IntegrationRequestBodyParams.ActionType defaultValue) {
      return this.getAction() != null && EnumUtils.isValidEnum(IntegrationRequestBodyParams.ActionType.class, this.getAction().toUpperCase())?IntegrationRequestBodyParams.ActionType.valueOf(this.getAction().toUpperCase()):defaultValue;
   }

   @JsonIgnore
   public void setActionType(IntegrationRequestBodyParams.ActionType actionType) {
      this.set("action", actionType.getValue());
   }

   @JsonProperty("board")
   public String getBoard() {
      return this.getString("board");
   }

   public void setBoard(String board) {
      this.set("board", board);
   }

   @JsonProperty("sessionId")
   public String getSessionId() {
      return this.getString("sessionId");
   }

   public void setSessionId(String sessionId) {
      this.set("sessionId", sessionId);
   }

   @JsonProperty("data")
   public KanbanBoard getData() {
      return (KanbanBoard)this.get("data");
   }

   public void setData(KanbanBoard data) {
      this.set("data", data);
   }

   @JsonProperty("page")
   public int getPage() {
      Integer page = this.getInteger("page");
      return page != null?page.intValue():1;
   }

   public void setPage(Integer page) {
      this.set("page", page);
   }

   @JsonProperty("vaultDNS")
   public String getVaultDNS() {
      return this.getString("vaultDNS");
   }

   public void setVaultDNS(String vaultDNS) {
      this.set("vaultDNS", vaultDNS);
   }

   public static enum ActionType {

      GET_KANBAN_DATA("GET_KANBAN_DATA", 0, "GET_KANBAN_DATA"),
      SET_KANBAN_DATA("SET_KANBAN_DATA", 1, "SET_KANBAN_DATA"),
      UNDEFINED("UNDEFINED", 2, "UNDEFINED");
      String value;
      // $FF: synthetic field
      private static final IntegrationRequestBodyParams.ActionType[] $VALUES = new IntegrationRequestBodyParams.ActionType[]{GET_KANBAN_DATA, SET_KANBAN_DATA, UNDEFINED};


      private ActionType(String var1, int var2, String value) {
         this.value = value;
      }

      public String getValue() {
         return this.value;
      }

   }
}
