package com.llkj.newbjia.factory;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.llkj.newbjia.bean.ResponseBean;

import android.os.Bundle;

public class IndexFactory {
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

		if (jsonObject.has("sales")) {
			JSONArray array = jsonObject.getJSONArray("sales");
			ArrayList list = new ArrayList();
			HashMap map;
			for (int i = 0; i < array.length(); i++) {
				JSONObject item = array.getJSONObject(i);
				map = new HashMap();
				if (item.has("promote_price")) {
					map.put("promote_price", item.getString("promote_price"));
				}
				if (item.has("id")) {
					map.put("id", item.getString("id"));
				}
				if (item.has("name")) {
					map.put("name", item.getString("name"));
				}
				if (item.has("goods_img")) {
					map.put("goods_img", item.getString("goods_img"));
				}
				if (item.has("shop_price")) {
					map.put("shop_price", item.getString("shop_price"));
				}
				list.add(map);
			}
			bundle.putParcelableArrayList("sales", list);
		}
		if (jsonObject.has("group")) {
			JSONArray array = jsonObject.getJSONArray("group");
			ArrayList list = new ArrayList();
			HashMap map;
			for (int i = 0; i < array.length(); i++) {
				JSONObject item = array.getJSONObject(i);
				map = new HashMap();
				if (item.has("group_buy_id")) {
					map.put("group_buy_id", item.getString("group_buy_id"));
				}
				if (item.has("goods_id")) {
					map.put("goods_id", item.getString("goods_id"));
				}
				if (item.has("goods_name")) {
					map.put("goods_name", item.getString("goods_name"));
				}
				if (item.has("goods_img")) {
					map.put("goods_img", item.getString("goods_img"));
				}
				if (item.has("last_price")) {
					map.put("last_price", item.getString("last_price"));
				}
				list.add(map);
			}
			bundle.putParcelableArrayList("group", list);
		}

		return bundle;
	}
}
