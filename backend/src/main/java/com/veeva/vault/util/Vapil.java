package com.veeva.vault.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.veeva.vault.vapil.api.model.VaultModel;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Vapil {

	public static <T> T convert(VaultModel source, Class<T> valueType) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			return mapper.readValue(source.toJsonString(), valueType);
		}
		catch (Exception e) {

			return null;
		}
	}

	public static JSONArray toJsonArray(List<? extends VaultModel> source) {
		try {
			JSONArray jsonArray = new JSONArray();
			for (VaultModel model : source) {
				jsonArray.put(model.toMap());
			}
			return jsonArray;
		}
		catch (Exception e) {
			return null;
		}
	}

	public static Object toMapList(List<? extends VaultModel> source) {
		try {
			List<Map<String, Object>> results = new ArrayList<>();
			for (VaultModel model : source) {
				results.add(model.toMap());
			}
			return results;
		}
		catch (Exception e) {
			return null;
		}
	}
}
