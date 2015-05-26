package com.llkj.newbjia.factory;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;

import com.llkj.newbjia.bean.ResponseBean;

public class GoodDescFactory {
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

		if (jsonObject.has(ResponseBean.RESPONSE_GOODS_ID)) {
			bundle.putString(ResponseBean.RESPONSE_GOODS_ID,
					jsonObject.getString(ResponseBean.RESPONSE_GOODS_ID));
		}
		if (jsonObject.has(ResponseBean.RESPONSE_GOODS_SN)) {
			bundle.putString(ResponseBean.RESPONSE_GOODS_SN,
					jsonObject.getString(ResponseBean.RESPONSE_GOODS_SN));
		}
		if (jsonObject.has(ResponseBean.RESPONSE_GOODS_NAME)) {
			bundle.putString(ResponseBean.RESPONSE_GOODS_NAME,
					jsonObject.getString(ResponseBean.RESPONSE_GOODS_NAME));
		}
		if (jsonObject.has(ResponseBean.RESPONSE_GOODS_BRIEF)) {
			bundle.putString(ResponseBean.RESPONSE_GOODS_BRIEF,
					jsonObject.getString(ResponseBean.RESPONSE_GOODS_BRIEF));
		}
		if (jsonObject.has(ResponseBean.RESPONSE_SPF)) {
			bundle.putString(ResponseBean.RESPONSE_SPF,
					jsonObject.getString(ResponseBean.RESPONSE_SPF));
		}
		if (jsonObject.has(ResponseBean.RESPONSE_MARKET_PRICE)) {
			bundle.putString(ResponseBean.RESPONSE_MARKET_PRICE,
					jsonObject.getString(ResponseBean.RESPONSE_MARKET_PRICE));
		}
		if (jsonObject.has(ResponseBean.RESPONSE_GOODS_NUMBER)) {
			bundle.putString(ResponseBean.RESPONSE_GOODS_NUMBER,
					jsonObject.getString(ResponseBean.RESPONSE_GOODS_NUMBER));
		}
		if (jsonObject.has(ResponseBean.RESPONSE_GOODS_WEIGHT)) {
			bundle.putString(ResponseBean.RESPONSE_GOODS_WEIGHT,
					jsonObject.getString(ResponseBean.RESPONSE_GOODS_WEIGHT));
		}
		if (jsonObject.has(ResponseBean.RESPONSE_IS_ATTENTION)) {
			bundle.putString(ResponseBean.RESPONSE_IS_ATTENTION,
					jsonObject.getString(ResponseBean.RESPONSE_IS_ATTENTION));
		}
		if (jsonObject.has(ResponseBean.RESPONSE_COMMODITY_PACKAGING)) {
			bundle.putString(
					ResponseBean.RESPONSE_COMMODITY_PACKAGING,
					jsonObject
							.getString(ResponseBean.RESPONSE_COMMODITY_PACKAGING));
		}

		if (jsonObject.has("properties")) {
			JSONArray jsonarray = jsonObject.getJSONArray("properties");
			ArrayList properties = new ArrayList();
			for (int i = 0; i < jsonarray.length(); i++) {
				JSONObject propertiesObject = jsonarray.getJSONObject(i);
				HashMap map = new HashMap();
				if (propertiesObject.has("name")) {
					map.put("name", propertiesObject.getString("name"));
				}
				if (propertiesObject.has("value")) {
					map.put("value", propertiesObject.getString("value"));
				}
				properties.add(map);
			}
			bundle.putParcelableArrayList("properties", properties);
		}
		if (jsonObject.has("related_goods")) {
			JSONArray jsonarray = jsonObject.getJSONArray("related_goods");
			ArrayList related_goods = new ArrayList();
			for (int i = 0; i < jsonarray.length(); i++) {
				JSONObject related_goodsObject = jsonarray.getJSONObject(i);
				HashMap map = new HashMap();
				if (related_goodsObject.has("goods_id")) {
					map.put("goods_id",
							related_goodsObject.getString("goods_id"));
				}
				if (related_goodsObject.has("goods_name")) {
					map.put("goods_name",
							related_goodsObject.getString("goods_name"));
				}
				if (related_goodsObject.has("goods_img")) {
					map.put("goods_img",
							related_goodsObject.getString("goods_img"));
				}
				if (related_goodsObject.has("shop_price")) {
					map.put("shop_price",
							related_goodsObject.getString("shop_price"));
				}
				related_goods.add(map);
			}
			bundle.putParcelableArrayList("related_goods", related_goods);
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
