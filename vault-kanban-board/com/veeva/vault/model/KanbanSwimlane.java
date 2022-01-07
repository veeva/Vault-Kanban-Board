package com.veeva.vault.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.model.KanbanCard;
import com.veeva.vault.model.KanbanComponent;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties
public class KanbanSwimlane extends KanbanComponent {

   public KanbanSwimlane() {
      this.setCards(new ArrayList());
   }

   @JsonProperty("accent_color_name__c")
   @JsonAlias({"accent_color__cr.name__v"})
   public String getAccentColorName() {
      return this.getString("accent_color_name__c");
   }

   public void setAccentColorName(String accentColorName) {
      this.set("accent_color_name__c", accentColorName);
   }

   @JsonProperty("cards")
   public List<KanbanCard> getCards() {
      return (List)this.get("cards");
   }

   public void setCards(List<KanbanCard> cards) {
      this.set("cards", cards);
   }

   @JsonProperty("control_value__c")
   public String getControlValue() {
      return this.getString("control_value__c");
   }

   public void setControlValue(String controlValue) {
      this.set("control_value__c", controlValue);
   }

   @JsonProperty("visible_when_empty__c")
   public Boolean getVisibleWhenEmpty() {
      return this.getBoolean("visible_when_empty__c");
   }

   public void setVisibleWhenEmpty(Boolean visibleWhenEmpty) {
      this.set("visible_when_empty__c", visibleWhenEmpty);
   }

   @JsonProperty("visible__c")
   public Boolean isVisible() {
      return this.getVisibleWhenEmpty() == Boolean.FALSE && (this.getCards() == null || this.getCards().isEmpty())?Boolean.valueOf(false):Boolean.valueOf(true);
   }
}
