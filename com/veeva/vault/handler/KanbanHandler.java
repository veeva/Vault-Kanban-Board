package com.veeva.vault.handler;

import com.veeva.vault.model.KanbanBoard;
import com.veeva.vault.model.KanbanCard;
import com.veeva.vault.model.KanbanCardField;
import com.veeva.vault.model.KanbanPage;
import com.veeva.vault.model.KanbanSwimlane;
import com.veeva.vault.model.KanbanSwimlaneOption;
import com.veeva.vault.util.Environment;
import com.veeva.vault.util.Vapil;
import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.model.metadata.VaultObjectField;
import com.veeva.vault.vapil.api.model.response.MetaDataObjectFieldResponse;
import com.veeva.vault.vapil.api.model.response.ObjectRecordBulkResponse;
import com.veeva.vault.vapil.api.model.response.PicklistValueResponse;
import com.veeva.vault.vapil.api.model.response.QueryResponse;
import com.veeva.vault.vapil.api.model.response.PicklistValueResponse.PicklistValue;
import com.veeva.vault.vapil.api.model.response.QueryResponse.QueryResult;
import com.veeva.vault.vapil.api.request.MetaDataRequest;
import com.veeva.vault.vapil.api.request.ObjectRecordRequest;
import com.veeva.vault.vapil.api.request.PicklistRequest;
import com.veeva.vault.vapil.api.request.QueryRequest;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

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
      this.loadSettings(boardApiName);
   }

   public void loadSettings(String boardApiName) {
      List settingRecords = this.getSettingRecords(boardApiName);
      if(settingRecords != null) {
         Iterator var3 = settingRecords.iterator();

         while(var3.hasNext()) {
            QueryResult queryResult = (QueryResult)var3.next();
            List cardFields = queryResult.getSubQuery("kanban_card_fields__cr").getData();
            List swimlanes = queryResult.getSubQuery("kanban_swimlanes__cr").getData();
            queryResult.getVaultModelData().remove("kanban_card_fields__cr");
            queryResult.getVaultModelData().remove("kanban_swimlanes__cr");
            this.kanbanBoard = (KanbanBoard)Vapil.convert(queryResult, KanbanBoard.class);
            Iterator var7 = cardFields.iterator();

            QueryResult swimlaneQueryResult;
            while(var7.hasNext()) {
               swimlaneQueryResult = (QueryResult)var7.next();
               this.kanbanBoard.getCardFields().add(Vapil.convert(swimlaneQueryResult, KanbanCardField.class));
            }

            var7 = swimlanes.iterator();

            while(var7.hasNext()) {
               swimlaneQueryResult = (QueryResult)var7.next();
               this.kanbanBoard.getSwimlanes().add(Vapil.convert(swimlaneQueryResult, KanbanSwimlane.class));
            }
         }
      }

   }

   public Object getData() {
      try {
         String e = this.kanbanBoard.getControlFieldName();
         QueryResponse recordResponse = this.getObjectRecordResponse();
         if(recordResponse != null && recordResponse.isSuccessful()) {
            String swimlaneControlType = null;
            List swimlaneOptions = null;
            List fields = recordResponse.getQueryDescribe().getFields();
            if(fields != null) {
               Iterator objectRecords = fields.iterator();

               while(objectRecords.hasNext()) {
                  VaultObjectField pageCount = (VaultObjectField)objectRecords.next();
                  if(pageCount.getName().equals(e)) {
                     swimlaneControlType = pageCount.getType();
                     byte card = -1;
                     switch(swimlaneControlType.hashCode()) {
                     case -1939501217:
                        if(swimlaneControlType.equals("Object")) {
                           card = 0;
                        }
                        break;
                     case -674063265:
                        if(swimlaneControlType.equals("Picklist")) {
                           card = 1;
                        }
                     }

                     switch(card) {
                     case 0:
                        swimlaneOptions = this.getControlObjectValues(pageCount.getName());
                        break;
                     case 1:
                        swimlaneOptions = this.getControlPicklistValues(pageCount.getPicklist());
                     }
                  }
               }
            }

            Iterator var17;
            if(swimlaneOptions != null) {
               int var15 = 0;

               for(var17 = this.kanbanBoard.getSwimlanes().iterator(); var17.hasNext(); ++var15) {
                  KanbanSwimlane queryResult = (KanbanSwimlane)var17.next();
                  int var21 = (this.currentPage - 1) * this.kanbanBoard.getPageSize();
                  int swimlaneObjectValue = var15 + var21;
                  if(swimlaneOptions.size() > swimlaneObjectValue) {
                     KanbanSwimlaneOption swimlaneControlValue = (KanbanSwimlaneOption)swimlaneOptions.get(swimlaneObjectValue);
                     queryResult.setControlValue(swimlaneControlValue.getApiName());
                     queryResult.setLabel(swimlaneControlValue.getLabel());
                  } else {
                     queryResult.setVisibleWhenEmpty(Boolean.valueOf(false));
                  }
               }

               int var18 = (int)Math.ceil((double)swimlaneOptions.size() / (double)this.kanbanBoard.getPageSize());

               for(int var19 = 1; var19 <= var18; ++var19) {
                  KanbanPage var23 = new KanbanPage();
                  var23.setIndex(Integer.valueOf(var19));
                  var23.setLabel("Page " + String.format("%02d", new Object[]{Integer.valueOf(var19)}));
                  this.kanbanBoard.getPages().add(var23);
               }
            }

            List var16 = recordResponse.getData();
            if(var16 != null) {
               var17 = var16.iterator();

               while(var17.hasNext()) {
                  QueryResult var20 = (QueryResult)var17.next();
                  KanbanCard var24 = new KanbanCard();
                  var24.setId(var20.getString("id"));
                  var24.setName(var20.getString("name__v"));
                  var24.setOldValues(var20);
                  Object var22 = var20.get(e);
                  String var25 = null;
                  byte swimlane = -1;
                  switch(swimlaneControlType.hashCode()) {
                  case -1939501217:
                     if(swimlaneControlType.equals("Object")) {
                        swimlane = 0;
                     }
                     break;
                  case -674063265:
                     if(swimlaneControlType.equals("Picklist")) {
                        swimlane = 1;
                     }
                  }

                  switch(swimlane) {
                  case 0:
                     var25 = var22.toString();
                     break;
                  case 1:
                     var25 = var22.toString().replace("[", "").replace("]", "");
                  }

                  Iterator var12 = this.kanbanBoard.getSwimlanes().iterator();

                  while(var12.hasNext()) {
                     KanbanSwimlane var26 = (KanbanSwimlane)var12.next();
                     if(var25 != null && var26.getControlValue() != null && var26.getControlValue().equals(var25)) {
                        var26.getCards().add(var24);
                     }
                  }
               }
            }
         }

         return this.kanbanBoard;
      } catch (Exception var14) {
         logger.error(var14.getMessage());
         return null;
      }
   }

   private List<KanbanSwimlaneOption> getControlObjectValues(String fieldName) {
      MetaDataObjectFieldResponse response = ((MetaDataRequest)this.vaultClient.newRequest(MetaDataRequest.class)).retrieveObjectFieldMetaData(this.kanbanBoard.getObjectName(), fieldName);
      logger.debug("response : " + (response.isSuccessful()?response.getResponse():null));
      if(response != null && response.isSuccessful()) {
         String controlObjectName = response.getField().getObjectReference().getName();
         StringBuilder query = new StringBuilder();
         query.append("SELECT id, name__v");
         query.append(" FROM " + controlObjectName);
         query.append(" WHERE status__v = \'active__v\'");
         query.append(" ORDER BY name__v");
         QueryResponse queryResponse = ((QueryRequest)this.vaultClient.newRequest(QueryRequest.class)).query(query.toString());
         logger.debug("response : " + (queryResponse.isSuccessful()?queryResponse.getResponse():null));
         if(queryResponse != null && queryResponse.isSuccessful()) {
            ArrayList result = new ArrayList();
            Iterator var7 = queryResponse.getData().iterator();

            while(var7.hasNext()) {
               QueryResult queryResult = (QueryResult)var7.next();
               result.add(Vapil.convert(queryResult, KanbanSwimlaneOption.class));
            }

            return result;
         }
      }

      return null;
   }

   private List<KanbanSwimlaneOption> getControlPicklistValues(String picklistName) {
      PicklistValueResponse response = ((PicklistRequest)this.vaultClient.newRequest(PicklistRequest.class)).retrievePicklistValues(picklistName);
      logger.debug("response : " + (response.isSuccessful()?response.getResponse():null));
      if(response != null && response.isSuccessful()) {
         ArrayList result = new ArrayList();
         Iterator var4 = response.getPicklistValues().iterator();

         while(var4.hasNext()) {
            PicklistValue picklistValue = (PicklistValue)var4.next();
            result.add(Vapil.convert(picklistValue, KanbanSwimlaneOption.class));
         }

         return result;
      } else {
         return null;
      }
   }

   private QueryResponse getObjectRecordResponse() {
      HashSet selectFields = new HashSet();
      selectFields.add("id");
      ArrayList orderByFields = new ArrayList();
      Iterator query = this.kanbanBoard.getCardFields().iterator();

      while(query.hasNext()) {
         KanbanCardField response = (KanbanCardField)query.next();
         selectFields.add(response.getFieldApiName());
         if(response.isOrderBy().booleanValue() && !orderByFields.contains(response.getFieldApiName())) {
            orderByFields.add(response.getFieldApiName());
         }
      }

      StringBuilder query1 = new StringBuilder();
      query1.append("SELECT ");
      query1.append(String.join(", ", selectFields));
      query1.append(" FROM " + this.kanbanBoard.getObjectName());
      query1.append(" WHERE id != null");
      if(this.kanbanBoard.getVqlCriteria() != null) {
         query1.append(" AND (" + this.kanbanBoard.getVqlCriteria() + ")");
      }

      if(orderByFields != null && !orderByFields.isEmpty()) {
         query1.append(" ORDER BY ");
         query1.append(String.join(", ", orderByFields));
      }

      QueryResponse response1 = ((QueryRequest)this.vaultClient.newRequest(QueryRequest.class)).setDescribeQuery(Boolean.valueOf(true)).query(query1.toString());
      logger.debug("response : " + (response1.isSuccessful()?response1.getResponse():null));
      if(response1 != null && response1.isSuccessful()) {
         return response1;
      } else {
         return null;
      }
   }

   public Object saveData(KanbanBoard kanbanBoard) {
      ArrayList updatedRecords = new ArrayList();
      Iterator response = kanbanBoard.getSwimlanes().iterator();

      while(response.hasNext()) {
         KanbanSwimlane swimlane = (KanbanSwimlane)response.next();
         Iterator var5 = swimlane.getCards().iterator();

         while(var5.hasNext()) {
            KanbanCard card = (KanbanCard)var5.next();
            if(card.getNewValues() != null) {
               updatedRecords.add(card.getNewValues());
            }
         }
      }

      logger.debug(Vapil.toJsonArray(updatedRecords).toString());
      ObjectRecordBulkResponse response1 = ((ObjectRecordRequest)this.vaultClient.newRequest(ObjectRecordRequest.class)).setContentTypeJson().setRequestString(Vapil.toJsonArray(updatedRecords).toString()).updateObjectRecords(kanbanBoard.getObjectName());
      logger.debug(response1.getResponse());
      return this.getData();
   }

   private List<QueryResult> getSettingRecords(String boardApiName) {
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
      query.append(" WHERE api_name__c = \'" + boardApiName + "\'");
      QueryResponse response = ((QueryRequest)this.vaultClient.newRequest(QueryRequest.class)).query(query.toString());
      logger.debug("response : " + (response.isSuccessful()?response.getResponse():null));
      return response != null && response.isSuccessful()?response.getData():null;
   }

}
