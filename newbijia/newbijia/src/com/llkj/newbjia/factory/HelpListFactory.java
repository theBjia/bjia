package com.llkj.newbjia.factory;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;

import com.llkj.newbjia.bean.ResponseBean;

public class HelpListFactory {
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
				if (item.has(ResponseBean.RESPONSE_ID)) {
					map.put(ResponseBean.RESPONSE_ID,
							item.getString(ResponseBean.RESPONSE_ID));
				}
				if (item.has(ResponseBean.RESPONSE_TITLE)) {
					map.put(ResponseBean.RESPONSE_TITLE,
							item.getString(ResponseBean.RESPONSE_TITLE));
				}

				list.add(map);
			}
			bundle.putParcelableArrayList(ResponseBean.RESPONSE_LIST, list);
		}
		return bundle;
	}
}
