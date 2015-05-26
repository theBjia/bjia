package com.llkj.newbjia.factory;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;

import com.llkj.newbjia.bean.ResponseBean;

public class ClassListFactory {
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Bundle parseResult(String wsResponse) throws JSONException {
		JSONObject jsonObject = new JSONObject(wsResponse);
		Bundle bundle = new Bundle();
		if (jsonObject.has(ResponseBean.RESPONSE_STATE)) {
			bundle.putInt(ResponseBean.RESPONSE_STATE,
					jsonObject.getInt(ResponseBean.RESPONSE_STATE));
		}

		if (jsonObject.has(ResponseBean.RESPONSE_MESSAGE)) {
			bundle.putString(ResponseBean.RESPONSE_MESSAGE,
					jsonObject.getString(ResponseBean.RESPONSE_MESSAGE));
		}

		if (jsonObject.has(ResponseBean.RESPONSE_LIST)) {
			JSONArray array = jsonObject
					.getJSONArray(ResponseBean.RESPONSE_LIST);
			ArrayList list = new ArrayList();
			HashMap map;
			for (int i = 0; i < array.length(); i++) {
				JSONObject item = array.getJSONObject(i);
				map = new HashMap();
				if (item.has("cat_id")) {
					map.put("cat_id", item.getString("cat_id"));
				}
				if (item.has("cat_name")) {
					map.put("cat_name", item.getString("cat_name"));
				}
				if (item.has("cat_logo")) {
					map.put("cat_logo", item.getString("cat_logo"));
				}
				if (item.has("cat_next")) {
					map.put("cat_next", item.getString("cat_next"));
				}

				list.add(map);
			}
			bundle.putParcelableArrayList(ResponseBean.RESPONSE_LIST, list);
		}

		return bundle;
	}
}
