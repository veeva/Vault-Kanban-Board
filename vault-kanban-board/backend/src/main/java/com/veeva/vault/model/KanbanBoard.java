package com.veeva.vault.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties
public class KanbanBoard extends KanbanComponent {
	public KanbanBoard() {
		this.setCardFields(new ArrayList<>());
		this.setPages(new ArrayList<>());
		this.setSwimlanes(new ArrayList<>());
	}

	@JsonProperty("allow_save__c")
	public boolean getAllowSave() {
		if (getCardFields() != null) {
			for (KanbanCardField cardFieldSetting : getCardFields()) {
				if (cardFieldSetting.isEditable()) {
					return true;
				}
			}
		}
		return false;
	}

	@JsonProperty("card_fields")
	public List<KanbanCardField> getCardFields() { return (List<KanbanCardField>)this.get("card_fields"); }
	public void setCardFields(List<KanbanCardField> cardFields) { this.set("card_fields", cardFields); }

	@JsonProperty("object_name__c")
	public String getObjectName() { return this.getString("object_name__c"); }
	public void setObjectName(String primaryObjectName) { this.set("object_name__c", primaryObjectName); }

	@JsonProperty("sort_order__c")
	public Integer getSortOrder() { return this.getInteger("sort_order__c"); }
	public void setSortOrder(Integer sortOrder) { this.set("sort_order__c", sortOrder); }

	@JsonProperty("vql_criteria__c")
	public String getVqlCriteria() { return this.getString("vql_criteria__c"); }
	public void setVqlCriteria(String vqlCriteria) { this.set("vql_criteria__c", vqlCriteria); }

	@JsonProperty("control_field__c")
	public String getControlFieldName() {
		if (getCardFields() != null && !getCardFields().isEmpty()) {
			return getCardFields().get(0).getFieldApiName();
		}
		return null;
	}

	@JsonProperty("pages")
	public List<KanbanPage> getPages() { return (List<KanbanPage>)this.get("pages"); }
	public void setPages(List<KanbanPage> pages) { this.set("pages", pages); }

	@JsonProperty("page_count__c")
	public Integer getPageCount() {
		if (getPages() != null) {
			return getPages().size();
		}
		return 0;
	}

	@JsonProperty("page_size__c")
	public int getPageSize() {
		if (getSwimlanes() != null) {
			return getSwimlanes().size();
		}
		return 0;
	}

	@JsonProperty("swimlanes")
	public List<KanbanSwimlane> getSwimlanes() { return (List<KanbanSwimlane>)this.get("swimlanes"); }
	public void setSwimlanes(List<KanbanSwimlane> swimlanes) { this.set("swimlanes", swimlanes); }
}
