package com.llkj.newbjia.factory;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;

import com.llkj.newbjia.bean.ResponseBean;

public class BrowseListFactory {
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

		if (jsonObject.has(ResponseBean.RESPONSE_LIST)) {
			JSONArray array = jsonObject.getJSONArray(ResponseBean.RESPONSE_LIST);
			ArrayList list = new ArrayList();
			HashMap map;
			for (int i = 0; i < array.length(); i++) {
				JSONObject item = array.getJSONObject(i);
				map = new HashMap();
				if (item.has(ResponseBean.RESPONSE_ID)) {
					map.put(ResponseBean.RESPONSE_ID, item.getString(ResponseBean.RESPONSE_ID));
				}
				if (item.has(ResponseBean.RESPONSE_GOODS_PRICE)) {
					map.put(ResponseBean.RESPONSE_GOODS_PRICE, item.getString(ResponseBean.RESPONSE_GOODS_PRICE));
				}
				if (item.has(ResponseBean.RESPONSE_GOODS_NAME)) {
					map.put(ResponseBean.RESPONSE_GOODS_NAME, item.getString(ResponseBean.RESPONSE_GOODS_NAME));
				}
				if (item.has(ResponseBean.RESPONSE_COMMODITY_PACKAGING)) {
					map.put(ResponseBean.RESPONSE_COMMODITY_PACKAGING, item.getString(ResponseBean.RESPONSE_COMMODITY_PACKAGING));
				}
				if (item.has(ResponseBean.RESPONSE_GOODS_WEIGHT)) {
					map.put(ResponseBean.RESPONSE_GOODS_WEIGHT, item.getString(ResponseBean.RESPONSE_GOODS_WEIGHT));
				}
				if (item.has(ResponseBean.RESPONSE_GOODS_IMG)) {
					map.put(ResponseBean.RESPONSE_GOODS_IMG, item.getString(ResponseBean.RESPONSE_GOODS_IMG));
				}
				list.add(map);
			}
			bundle.putParcelableArrayList(ResponseBean.RESPONSE_LIST, list);
		}
		return bundle;
	}
}
