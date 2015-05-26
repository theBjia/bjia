package com.llkj.newbjia.factory;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;

import com.llkj.newbjia.bean.ResponseBean;

public class AddressListFactory {
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
				if (item.has("uname")) {
					map.put("uname", item.getString("uname"));
				}
				if (item.has("phone")) {
					map.put("phone", item.getString("phone"));
				}
				if (item.has("id")) {
					map.put("id", item.getString("id"));
				}
				if (item.has("name")) {
					map.put("name", item.getString("name"));
				}

				list.add(map);
			}
			bundle.putParcelableArrayList(ResponseBean.RESPONSE_LIST, list);
		}
		if (jsonObject.has(ResponseBean.RESPONSE_USERS)) {
			JSONArray array = jsonObject
					.getJSONArray(ResponseBean.RESPONSE_USERS);
			ArrayList list = new ArrayList();
			HashMap map;
			for (int i = 0; i < array.length(); i++) {
				JSONObject item = array.getJSONObject(i);
				map = new HashMap();
				if (item.has("uname")) {
					map.put("uname", item.getString("uname"));
				}
				if (item.has("phone")) {
					map.put("phone", item.getString("phone"));
				}
				if (item.has("id")) {
					map.put("id", item.getString("id"));
				}
				if (item.has("name")) {
					map.put("name", item.getString("name"));
				}

				list.add(map);
			}
			bundle.putParcelableArrayList(ResponseBean.RESPONSE_USERS, list);
		}
		if (jsonObject.has(ResponseBean.RESPONSE_ADD)) {
			JSONArray array = jsonObject
					.getJSONArray(ResponseBean.RESPONSE_ADD);
			ArrayList list = new ArrayList();
			HashMap map;
			for (int i = 0; i < array.length(); i++) {
				JSONObject item = array.getJSONObject(i);
				map = new HashMap();
				if (item.has("uname")) {
					map.put("uname", item.getString("uname"));
				}
				if (item.has("phone")) {
					map.put("phone", item.getString("phone"));
				}
				if (item.has("id")) {
					map.put("id", item.getString("id"));
				}
				if (item.has("name")) {
					map.put("name", item.getString("name"));
				}

				list.add(map);
			}
			bundle.putParcelableArrayList(ResponseBean.RESPONSE_ADD, list);
		}

		return bundle;
	}
}
