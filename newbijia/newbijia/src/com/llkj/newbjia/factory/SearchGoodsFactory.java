package com.llkj.newbjia.factory;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;

import com.llkj.newbjia.bean.ResponseBean;

public class SearchGoodsFactory {
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
				if (item.has("goods_name")) {
					map.put("goods_name", item.getString("goods_name"));
				}
				if (item.has("shop_price")) {
					map.put("shop_price", item.getString("shop_price"));
				}
				if (item.has("market_price")) {
					map.put("market_price", item.getString("market_price"));
				}
				if (item.has("goods_id")) {
					map.put("goods_id", item.getString("goods_id"));
				}
				if (item.has("goods_img")) {
					map.put("goods_img", item.getString("goods_img"));
				}
				if (item.has("goods_weight")) {
					map.put("goods_weight", item.getString("goods_weight"));
				}
				list.add(map);
			}
			bundle.putParcelableArrayList(ResponseBean.RESPONSE_LIST, list);
		}

		return bundle;
	}
}
