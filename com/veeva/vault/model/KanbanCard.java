package com.veeva.vault.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.model.KanbanComponent;
import com.veeva.vault.vapil.api.model.VaultModel;

@JsonIgnoreProperties
public class KanbanCard extends KanbanComponent {

   @JsonProperty("new_values")
   public VaultModel getNewValues() {
      return (VaultModel)this.get("new_values");
   }

   public void setNewValues(VaultModel newValues) {
      this.set("new_values", newValues);
   }

   @JsonProperty("old_values")
   public VaultModel getOldValues() {
      return (VaultModel)this.get("old_values");
   }

   public void setOldValues(VaultModel oldValues) {
      this.set("old_values", oldValues);
   }
}
