package com.llkj.newbjia.factory;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;

import com.llkj.newbjia.bean.ResponseBean;

public class PurchaseDescFactory {
	@SuppressWarnings({ "rawtypes", "unchecked" })
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
		if (jsonObject.has(ResponseBean.RESPONSE_ACT_ID)) {
			bundle.putString(ResponseBean.RESPONSE_ACT_ID,
					jsonObject.getString(ResponseBean.RESPONSE_ACT_ID));
		}
		if (jsonObject.has(ResponseBean.RESPONSE_ACT_DESC)) {
			bundle.putString(ResponseBean.RESPONSE_ACT_DESC,
					jsonObject.getString(ResponseBean.RESPONSE_ACT_DESC));
		}
		if (jsonObject.has(ResponseBean.RESPONSE_GOODS_ID)) {
			bundle.putString(ResponseBean.RESPONSE_GOODS_ID,
					jsonObject.getString(ResponseBean.RESPONSE_GOODS_ID));
		}

		if (jsonObject.has(ResponseBean.RESPONSE_GOODS_NAME)) {
			bundle.putString(ResponseBean.RESPONSE_GOODS_NAME,
					jsonObject.getString(ResponseBean.RESPONSE_GOODS_NAME));
		}
		if (jsonObject.has(ResponseBean.RESPONSE_START_TIME)) {
			bundle.putString(ResponseBean.RESPONSE_START_TIME,
					jsonObject.getString(ResponseBean.RESPONSE_START_TIME));
		}
		if (jsonObject.has(ResponseBean.RESPONSE_END_TIME)) {
			bundle.putString(ResponseBean.RESPONSE_END_TIME,
					jsonObject.getString(ResponseBean.RESPONSE_END_TIME));
		}

		if (jsonObject.has(ResponseBean.RESPONSE_RESTRICT_AMOUNT)) {
			bundle.putString(ResponseBean.RESPONSE_RESTRICT_AMOUNT,
					jsonObject.getString(ResponseBean.RESPONSE_RESTRICT_AMOUNT));
		}
		if (jsonObject.has(ResponseBean.RESPONSE_GIFT_INTEGRAL)) {
			bundle.putString(ResponseBean.RESPONSE_GIFT_INTEGRAL,
					jsonObject.getString(ResponseBean.RESPONSE_GIFT_INTEGRAL));
		}
		if (jsonObject.has(ResponseBean.RESPONSE_FORMATED_CUR_PRICE)) {
			bundle.putString(
					ResponseBean.RESPONSE_FORMATED_CUR_PRICE,
					jsonObject
							.getString(ResponseBean.RESPONSE_FORMATED_CUR_PRICE));
		}
		if (jsonObject.has("price_ladder")) {
			JSONArray jsonarray = jsonObject.getJSONArray("price_ladder");
			ArrayList arrayone = new ArrayList();
			for (int i = 0; i < jsonarray.length(); i++) {
				JSONObject oneObject = jsonarray.getJSONObject(i);
				HashMap map = new HashMap();
				if (oneObject.has("amount")) {
					map.put("amount", oneObject.getString("amount"));
				}
				if (oneObject.has("price")) {
					map.put("price", oneObject.getString("price"));
				}
				if (oneObject.has("formated_price")) {
					map.put("formated_price",
							oneObject.getString("formated_price"));
				}
				arrayone.add(map);
			}
			bundle.putParcelableArrayList("price_ladder", arrayone);
		}
		if (jsonObject.has("properties")) {
			JSONArray jsonarray = jsonObject.getJSONArray("properties");
			ArrayList arraytwo = new ArrayList();
			for (int i = 0; i < jsonarray.length(); i++) {
				JSONObject twoObject = jsonarray.getJSONObject(i);
				HashMap map = new HashMap();
				if (twoObject.has("name")) {
					map.put("name", twoObject.getString("name"));
				}
				if (twoObject.has("value")) {
					map.put("value", twoObject.getString("value"));
				}

				arraytwo.add(map);
			}
			bundle.putParcelableArrayList("properties", arraytwo);
		}
		if (jsonObject.has("pic")) {
			JSONArray jsonarray = jsonObject.getJSONArray("pic");
			ArrayList pic = new ArrayList();
			for (int i = 0; i < jsonarray.length(); i++) {
				pic.add(jsonarray.get(i));
			}
			bundle.putParcelableArrayList("pic", pic);
		}

		return bundle;
	}
}
