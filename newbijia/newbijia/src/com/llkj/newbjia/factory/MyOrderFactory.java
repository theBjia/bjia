package com.llkj.newbjia.factory;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;

import com.llkj.newbjia.bean.ResponseBean;

public class MyOrderFactory {
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
				if (item.has("order_id")) {
					map.put("order_id", item.getString("order_id"));
				}
				if (item.has("order_sn")) {
					map.put("order_sn", item.getString("order_sn"));
				}
				if (item.has("goods_amount")) {
					map.put("goods_amount", item.getString("goods_amount"));
				}
				if (item.has("add_time")) {
					map.put("add_time", item.getString("add_time"));
				}

				if (item.has("goods_list")) {
					ArrayList alstwo = new ArrayList();
					JSONArray jastwo = item.getJSONArray("goods_list");
					for (int j = 0; j < jastwo.length(); j++) {
						HashMap maptwo = new HashMap();
						JSONObject jotwo = jastwo.getJSONObject(j);
						if (jotwo.has("goods_id")) {
							maptwo.put("goods_id", jotwo.getString("goods_id"));
						}
						if (jotwo.has("goods_name")) {
							maptwo.put("goods_name",
									jotwo.getString("goods_name"));
						}
						if (jotwo.has("goods_thumb")) {
							maptwo.put("goods_thumb",
									jotwo.getString("goods_thumb"));
						}
						if (jotwo.has("goods_weight")) {
							maptwo.put("goods_weight",
									jotwo.getString("goods_weight"));
						}
						if (jotwo.has("commodity_packaging")) {
							maptwo.put("commodity_packaging",
									jotwo.getString("commodity_packaging"));
						}
						alstwo.add(maptwo);
					}
					map.put("goods_list", alstwo);
				}

				if (item.has("status")) {
					map.put("status", item.getString("status"));
				}

				list.add(map);
			}
			bundle.putParcelableArrayList(ResponseBean.RESPONSE_LIST, list);
		}

		return bundle;
	}
}
