package com.veeva.vault.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.model.KanbanCardField;
import com.veeva.vault.model.KanbanComponent;
import com.veeva.vault.model.KanbanPage;
import com.veeva.vault.model.KanbanSwimlane;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@JsonIgnoreProperties
public class KanbanBoard extends KanbanComponent {

   public KanbanBoard() {
      this.setCardFields(new ArrayList());
      this.setPages(new ArrayList());
      this.setSwimlanes(new ArrayList());
   }

   @JsonProperty("allow_save__c")
   public boolean getAllowSave() {
      if(this.getCardFields() != null) {
         Iterator var1 = this.getCardFields().iterator();

         while(var1.hasNext()) {
            KanbanCardField cardFieldSetting = (KanbanCardField)var1.next();
            if(cardFieldSetting.isEditable().booleanValue()) {
               return true;
            }
         }
      }

      return false;
   }

   @JsonProperty("card_fields")
   public List<KanbanCardField> getCardFields() {
      return (List)this.get("card_fields");
   }

   public void setCardFields(List<KanbanCardField> cardFields) {
      this.set("card_fields", cardFields);
   }

   @JsonProperty("object_name__c")
   public String getObjectName() {
      return this.getString("object_name__c");
   }

   public void setObjectName(String primaryObjectName) {
      this.set("object_name__c", primaryObjectName);
   }

   @JsonProperty("sort_order__c")
   public Integer getSortOrder() {
      return this.getInteger("sort_order__c");
   }

   public void setSortOrder(Integer sortOrder) {
      this.set("sort_order__c", sortOrder);
   }

   @JsonProperty("vql_criteria__c")
   public String getVqlCriteria() {
      return this.getString("vql_criteria__c");
   }

   public void setVqlCriteria(String vqlCriteria) {
      this.set("vql_criteria__c", vqlCriteria);
   }

   @JsonProperty("control_field__c")
   public String getControlFieldName() {
      return this.getCardFields() != null && !this.getCardFields().isEmpty()?((KanbanCardField)this.getCardFields().get(0)).getFieldApiName():null;
   }

   @JsonProperty("pages")
   public List<KanbanPage> getPages() {
      return (List)this.get("pages");
   }

   public void setPages(List<KanbanPage> pages) {
      this.set("pages", pages);
   }

   @JsonProperty("page_count__c")
   public Integer getPageCount() {
      return this.getPages() != null?Integer.valueOf(this.getPages().size()):Integer.valueOf(0);
   }

   @JsonProperty("page_size__c")
   public int getPageSize() {
      return this.getSwimlanes() != null?this.getSwimlanes().size():0;
   }

   @JsonProperty("swimlanes")
   public List<KanbanSwimlane> getSwimlanes() {
      return (List)this.get("swimlanes");
   }

   public void setSwimlanes(List<KanbanSwimlane> swimlanes) {
      this.set("swimlanes", swimlanes);
   }
}
