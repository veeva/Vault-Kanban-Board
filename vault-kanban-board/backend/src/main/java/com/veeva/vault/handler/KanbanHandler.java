package com.veeva.vault.handler;

import com.veeva.vault.model.*;
import com.veeva.vault.util.Environment;
import com.veeva.vault.util.Vapil;
import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.model.VaultModel;
import com.veeva.vault.vapil.api.model.metadata.VaultObjectField;
import com.veeva.vault.vapil.api.model.response.MetaDataObjectFieldResponse;
import com.veeva.vault.vapil.api.model.response.ObjectRecordBulkResponse;
import com.veeva.vault.vapil.api.model.response.PicklistValueResponse;
import com.veeva.vault.vapil.api.model.response.QueryResponse;
import com.veeva.vault.vapil.api.request.*;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.util.*;

public class KanbanHandler {
	private static Logger logger = Logger.getLogger(KanbanHandler.class);

	KanbanBoard kanbanBoard = null;
	int currentPage = 1;
	private VaultClient vaultClient = null;

	public KanbanHandler(VaultClient vaultClient, String boardApiName, int page) {
		this.vaultClient = vaultClient;
		this.currentPage = page;

		String logLevel = Environment.getLogLevel();
		logger.setLevel(Level.toLevel(logLevel.toUpperCase()));
		logger.debug("logLevel : " + logLevel);

		loadSettings(boardApiName);
	}

	public void loadSettings(String boardApiName) {
		List<QueryResponse.QueryResult> settingRecords = getSettingRecords(boardApiName);
		if (settingRecords != null) {
			for (QueryResponse.QueryResult queryResult : settingRecords) {
				List<QueryResponse.QueryResult> cardFields = queryResult.getSubQuery("kanban_card_fields__cr").getData();
				List<QueryResponse.QueryResult> swimlanes = queryResult.getSubQuery("kanban_swimlanes__cr").getData();
				queryResult.getVaultModelData().remove("kanban_card_fields__cr");
				queryResult.getVaultModelData().remove("kanban_swimlanes__cr");

				kanbanBoard = Vapil.convert(queryResult, KanbanBoard.class);

				for (QueryResponse.QueryResult cardFieldQueryResult : cardFields) {
					kanbanBoard.getCardFields().add(Vapil.convert(cardFieldQueryResult, KanbanCardField.class));
				}

				for (QueryResponse.QueryResult swimlaneQueryResult : swimlanes) {
					kanbanBoard.getSwimlanes().add(Vapil.convert(swimlaneQueryResult, KanbanSwimlane.class));
				}
			}
		}
	}

	public Object getData() {
		try {
			String controlFieldApiName = kanbanBoard.getControlFieldName();
			QueryResponse recordResponse = getObjectRecordResponse();

			if (recordResponse != null && recordResponse.isSuccessful()) {

				String swimlaneControlType = null;
				List<KanbanSwimlaneOption> swimlaneOptions = null;
				List<VaultObjectField> fields = recordResponse.getQueryDescribe().getFields();
				if (fields != null) {
					for (VaultObjectField field : fields) {
						if (field.getName().equals(controlFieldApiName)) {
							swimlaneControlType = field.getType();
							switch (swimlaneControlType) {
								case "Object":
									swimlaneOptions = getControlObjectValues(field.getName());
									break;
								case "Picklist":
									swimlaneOptions = getControlPicklistValues(field.getPicklist());
									break;
							}
						}
					}
				}

				if (swimlaneOptions != null) {
					int swimlaneIndex = 0;
					for (KanbanSwimlane swimlane : kanbanBoard.getSwimlanes()) {
						int offset = (currentPage - 1) * kanbanBoard.getPageSize();
						int swimlaneOptionIndex = swimlaneIndex + offset;

						if (swimlaneOptions.size() > swimlaneOptionIndex) {
							KanbanSwimlaneOption swimlaneOption = swimlaneOptions.get(swimlaneOptionIndex);
							swimlane.setControlValue(swimlaneOption.getApiName());
							swimlane.setLabel(swimlaneOption.getLabel());
						}
						else {
							swimlane.setVisibleWhenEmpty(false);
						}

						swimlaneIndex += 1;
					}

					int pageCount = (int)Math.ceil((double)swimlaneOptions.size() / kanbanBoard.getPageSize());
					for (int pageIndex = 1; pageIndex <= pageCount; pageIndex++) {
						KanbanPage page = new KanbanPage();
						page.setIndex(pageIndex);
						page.setLabel("Page " + String.format("%02d", pageIndex));
						kanbanBoard.getPages().add(page);
					}
				}

				List<QueryResponse.QueryResult> objectRecords = recordResponse.getData();
				if (objectRecords != null) {
					for (QueryResponse.QueryResult queryResult : objectRecords) {
						KanbanCard card = new KanbanCard();
						card.setId(queryResult.getString("id"));
						card.setName(queryResult.getString("name__v"));
						card.setOldValues(queryResult);

						Object swimlaneObjectValue = queryResult.get(controlFieldApiName);
						String swimlaneControlValue = null;
						switch (swimlaneControlType) {
							case "Object":
								swimlaneControlValue = swimlaneObjectValue.toString();
								break;
							case "Picklist":
								swimlaneControlValue = swimlaneObjectValue.toString()
										.replace("[", "")
										.replace("]", "");
								break;
						}

						for (KanbanSwimlane swimlane : kanbanBoard.getSwimlanes()) {
							if (swimlaneControlValue != null && swimlane.getControlValue() != null) {
								if (swimlane.getControlValue().equals(swimlaneControlValue)) {
									swimlane.getCards().add(card);
								}
							}
						}
					}
				}
			}

			//return the result
			return kanbanBoard;
		}
		catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	private List<KanbanSwimlaneOption> getControlObjectValues(String fieldName) {
		MetaDataObjectFieldResponse response = vaultClient.newRequest(MetaDataRequest.class)
				.retrieveObjectFieldMetaData(kanbanBoard.getObjectName(), fieldName);

		logger.debug("response : " + (response.isSuccessful() ? response.getResponse() : null));
		if (response != null && response.isSuccessful()) {
			String controlObjectName = response.getField().getObjectReference().getName();

			StringBuilder query = new StringBuilder();
			query.append("SELECT id, name__v");
			query.append(" FROM " + controlObjectName);
			query.append(" WHERE status__v = 'active__v'");
			query.append(" ORDER BY name__v");

			QueryResponse queryResponse = vaultClient.newRequest(QueryRequest.class)
					.query(query.toString());

			logger.debug("response : " + (queryResponse.isSuccessful() ? queryResponse.getResponse() : null));
			if (queryResponse != null && queryResponse.isSuccessful()) {
				List<KanbanSwimlaneOption> result = new ArrayList<>();
				for (QueryResponse.QueryResult queryResult : queryResponse.getData()) {
					result.add(Vapil.convert(queryResult, KanbanSwimlaneOption.class));
				}
				return result;
			}
		}
		return null;
	}

	private List<KanbanSwimlaneOption> getControlPicklistValues(String picklistName) {
		PicklistValueResponse response = vaultClient.newRequest(PicklistRequest.class)
				.retrievePicklistValues(picklistName);

		logger.debug("response : " + (response.isSuccessful() ? response.getResponse() : null));
		if (response != null && response.isSuccessful()) {
			List<KanbanSwimlaneOption> result = new ArrayList<>();
			for (PicklistValueResponse.PicklistValue picklistValue : response.getPicklistValues()) {
				result.add(Vapil.convert(picklistValue, KanbanSwimlaneOption.class));
			}
			return result;
		}
		return null;
	}

	private QueryResponse getObjectRecordResponse() {
		Set<String> selectFields = new HashSet<>();
		selectFields.add("id");
		List<String> orderByFields = new ArrayList<>();
		for (KanbanCardField cardFieldSetting : kanbanBoard.getCardFields()) {
			selectFields.add(cardFieldSetting.getFieldApiName());
			if (cardFieldSetting.isOrderBy()) {
				if (!orderByFields.contains(cardFieldSetting.getFieldApiName())) {
					orderByFields.add(cardFieldSetting.getFieldApiName());
				}
			}
		}

		StringBuilder query = new StringBuilder();
		query.append("SELECT ");
		query.append(String.join(", ", selectFields));
		query.append(" FROM " + kanbanBoard.getObjectName());

		query.append(" WHERE id != null");

		if (kanbanBoard.getVqlCriteria() != null) {
			query.append(" AND (" + kanbanBoard.getVqlCriteria() + ")");
		}

		if (orderByFields != null && !orderByFields.isEmpty()) {
			query.append(" ORDER BY ");
			query.append(String.join(", ", orderByFields));
		}

		QueryResponse response = vaultClient.newRequest(QueryRequest.class)
				.setDescribeQuery(true)
				.query(query.toString());

		logger.debug("response : " + (response.isSuccessful() ? response.getResponse() : null));
		if (response != null && response.isSuccessful()) {
			return response;
		}
		return null;
	}

	public Object saveData(KanbanBoard kanbanBoard) {

		List<VaultModel> updatedRecords = new ArrayList<>();
		for (KanbanSwimlane swimlane : kanbanBoard.getSwimlanes()) {
			for (KanbanCard card : swimlane.getCards()) {
				if (card.getNewValues() != null) {
					updatedRecords.add(card.getNewValues());
				}
			}
		}

		logger.debug(Vapil.toJsonArray(updatedRecords).toString());
		ObjectRecordBulkResponse response  = vaultClient.newRequest(ObjectRecordRequest.class)
				.setContentTypeJson()
				.setRequestString(Vapil.toJsonArray(updatedRecords).toString())
				.updateObjectRecords(kanbanBoard.getObjectName());
		logger.debug(response.getResponse());

		return getData();
	}

	private List<QueryResponse.QueryResult> getSettingRecords(String boardApiName) {
		StringBuilder query = new StringBuilder();
		query.append("SELECT");
		query.append(" id,");
		query.append(" name__v,");
		query.append(" label__c,");
		query.append(" sort_order__c,");
		query.append(" api_name__c,");
		query.append(" object_name__c,");
		query.append(" vql_criteria__c,");
		query.append(" (SELECT id,");
		query.append("      name__v,");
		query.append("      label__c,");
		query.append("      sort_order__c,");
		query.append("      api_name__c,");
		query.append("      accent_color__cr.name__v,");
		query.append("      visible_when_empty__c");
		query.append("  FROM kanban_swimlanes__cr");
		query.append("  ORDER BY sort_order__c");
		query.append(" ),");
		query.append(" (SELECT id,");
		query.append("      name__v,");
		query.append("      label__c,");
		query.append("      sort_order__c,");
		query.append("      api_name__c,");
		query.append("      label__c,field_api_name__c,");
		query.append("      visible__c,");
		query.append("      editable__c,");
		query.append("      order_by__c");
		query.append("  FROM kanban_card_fields__cr");
		query.append("  ORDER BY sort_order__c");
		query.append(" )");
		query.append(" FROM kanban_board__c");
		query.append(" WHERE api_name__c = '" + boardApiName + "'");

		QueryResponse response = vaultClient.newRequest(QueryRequest.class)
				.query(query.toString());

		logger.debug("response : " + (response.isSuccessful() ? response.getResponse() : null));
		if (response != null && response.isSuccessful()) {
			return response.getData();
		}
		return null;
	}
}
