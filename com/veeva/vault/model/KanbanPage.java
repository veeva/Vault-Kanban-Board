package com.veeva.vault.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.model.KanbanComponent;

@JsonIgnoreProperties
public class KanbanPage extends KanbanComponent {

   @JsonProperty("index")
   public Integer getIndex() {
      return this.getInteger("index");
   }

   public void setIndex(Integer index) {
      this.set("index", index);
   }
}
