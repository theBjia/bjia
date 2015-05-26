package com.llkj.newbjia.factory;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;

import com.llkj.newbjia.bean.ResponseBean;

public class MyPrivilegeFactory {
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

		if (jsonObject.has(ResponseBean.RESPONSE_USE_LIST)) {
			JSONArray array = jsonObject.getJSONArray(ResponseBean.RESPONSE_USE_LIST);
			ArrayList list = new ArrayList();
			HashMap map;
			for (int i = 0; i < array.length(); i++) {
				JSONObject item = array.getJSONObject(i);
				map = new HashMap();
				if (item.has(ResponseBean.RESPONSE_USE_STARTDATE)) {
					map.put(ResponseBean.RESPONSE_USE_STARTDATE, item.getString(ResponseBean.RESPONSE_USE_STARTDATE));
				}
				if (item.has(ResponseBean.RESPONSE_USE_ENDDATE)) {
					map.put(ResponseBean.RESPONSE_USE_ENDDATE, item.getString(ResponseBean.RESPONSE_USE_ENDDATE));
				}
				if (item.has(ResponseBean.RESPONSE_TYPE_MONEY)) {
					map.put(ResponseBean.RESPONSE_TYPE_MONEY, item.getString(ResponseBean.RESPONSE_TYPE_MONEY));
				}
				if (item.has(ResponseBean.RESPONSE_TYPE_NAME)) {
					map.put(ResponseBean.RESPONSE_TYPE_NAME, item.getString(ResponseBean.RESPONSE_TYPE_NAME));
				}
				list.add(map);
			}
			bundle.putParcelableArrayList(ResponseBean.RESPONSE_USE_LIST, list);
		}
		
		if (jsonObject.has(ResponseBean.RESPONSE_NOT_LIST)) {
			JSONArray array = jsonObject.getJSONArray(ResponseBean.RESPONSE_NOT_LIST);
			ArrayList list = new ArrayList();
			HashMap map;
			for (int i = 0; i < array.length(); i++) {
				JSONObject item = array.getJSONObject(i);
				map = new HashMap();
				if (item.has(ResponseBean.RESPONSE_USE_STARTDATE)) {
					map.put(ResponseBean.RESPONSE_USE_STARTDATE, item.getString(ResponseBean.RESPONSE_USE_STARTDATE));
				}
				if (item.has(ResponseBean.RESPONSE_USE_ENDDATE)) {
					map.put(ResponseBean.RESPONSE_USE_ENDDATE, item.getString(ResponseBean.RESPONSE_USE_ENDDATE));
				}
				if (item.has(ResponseBean.RESPONSE_TYPE_MONEY)) {
					map.put(ResponseBean.RESPONSE_TYPE_MONEY, item.getString(ResponseBean.RESPONSE_TYPE_MONEY));
				}
				if (item.has(ResponseBean.RESPONSE_TYPE_NAME)) {
					map.put(ResponseBean.RESPONSE_TYPE_NAME, item.getString(ResponseBean.RESPONSE_TYPE_NAME));
				}
				list.add(map);
			}
			bundle.putParcelableArrayList(ResponseBean.RESPONSE_NOT_LIST, list);
	    }
		return bundle;
   }
}
