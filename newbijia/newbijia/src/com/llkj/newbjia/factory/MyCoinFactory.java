package com.llkj.newbjia.factory;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;

import com.llkj.newbjia.bean.ResponseBean;

public class MyCoinFactory {
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Bundle parseResult(String wsResponse) throws JSONException {
		JSONObject jsonObject = new JSONObject(wsResponse);
		Bundle bundle = new Bundle();
		if (jsonObject.has(ResponseBean.RESPONSE_STATE)) {
			bundle.putInt(ResponseBean.RESPONSE_STATE, jsonObject.getInt(ResponseBean.RESPONSE_STATE));
		}

		if (jsonObject.has(ResponseBean.RESPONSE_MESSAGE)) {
			bundle.putString(ResponseBean.RESPONSE_MESSAGE, jsonObject.getString(ResponseBean.RESPONSE_MESSAGE));
		}
		
		if (jsonObject.has(ResponseBean.RESPONSE_USER_MONEY)) {
			bundle.putString(ResponseBean.RESPONSE_USER_MONEY, jsonObject.getString(ResponseBean.RESPONSE_USER_MONEY));
		}
		if (jsonObject.has(ResponseBean.RESPONSE_LIST)) {
			JSONArray array = jsonObject.getJSONArray(ResponseBean.RESPONSE_LIST);
			ArrayList list = new ArrayList();
			HashMap map;
			for (int i = 0; i < array.length(); i++) {
				JSONObject item = array.getJSONObject(i);
				map = new HashMap();
				if (item.has(ResponseBean.RESPONSE_USER_MONEY)) {
					map.put(ResponseBean.RESPONSE_USER_MONEY, item.getString(ResponseBean.RESPONSE_USER_MONEY));
				}
				if (item.has(ResponseBean.RESPONSE_ADD_TIME)) {
					map.put(ResponseBean.RESPONSE_ADD_TIME, item.getString(ResponseBean.RESPONSE_ADD_TIME));
				}
				if (item.has(ResponseBean.RESPONSE_DESC)) {
					map.put(ResponseBean.RESPONSE_DESC, item.getString(ResponseBean.RESPONSE_DESC));
				}
				list.add(map);
			}
			bundle.putParcelableArrayList("list", list);
		}

		return bundle;
	}
}
