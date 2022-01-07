package com.veeva.vault.model;

import com.fasterxml.jackson.annotation.*;
import com.veeva.vault.vapil.api.model.VaultModel;
import org.apache.commons.lang3.EnumUtils;

@JsonIgnoreProperties
public class IntegrationRequestBodyParams extends VaultModel {
	@JsonProperty("action")
	public String getAction() {
		return this.getString("action");
	}
	public void setAction(String action) {
		this.set("action",  action);
	}

	public enum ActionType {
		GET_KANBAN_DATA("GET_KANBAN_DATA"),
		SET_KANBAN_DATA("SET_KANBAN_DATA"),
		UNDEFINED("UNDEFINED");

		String value;
		ActionType(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

	@JsonIgnore
	public ActionType getActionType() {
		return getActionType(ActionType.UNDEFINED);
	}

	@JsonIgnore
	public ActionType getActionType(ActionType defaultValue) {
		if (getAction() != null && EnumUtils.isValidEnum(ActionType.class, getAction().toUpperCase()))
			return ActionType.valueOf(getAction().toUpperCase());
		else
			return defaultValue;
	}

	@JsonIgnore
	public void setActionType(ActionType actionType) {
		this.set("action",  actionType.getValue());
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
		if (page != null) {
			return page;
		}
		return 1;
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
}