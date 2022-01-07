package com.veeva.vault.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.veeva.vault.vapil.api.model.VaultModel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;

public class Vapil {

   public static <T extends Object> T convert(VaultModel source, Class<T> valueType) {
      try {
         ObjectMapper e = new ObjectMapper();
         e.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
         return e.readValue(source.toJsonString(), valueType);
      } catch (Exception var3) {
         return null;
      }
   }

   public static JSONArray toJsonArray(List<? extends VaultModel> source) {
      try {
         JSONArray e = new JSONArray();
         Iterator var2 = source.iterator();

         while(var2.hasNext()) {
            VaultModel model = (VaultModel)var2.next();
            e.put(model.toMap());
         }

         return e;
      } catch (Exception var4) {
         return null;
      }
   }

   public static Object toMapList(List<? extends VaultModel> source) {
      try {
         ArrayList e = new ArrayList();
         Iterator var2 = source.iterator();

         while(var2.hasNext()) {
            VaultModel model = (VaultModel)var2.next();
            e.add(model.toMap());
         }

         return e;
      } catch (Exception var4) {
         return null;
      }
   }
}
