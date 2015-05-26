package com.llkj.newbjia.factory;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;

import com.llkj.newbjia.bean.ResponseBean;

public class OrderDescFactory {
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

		if (jsonObject.has("goods_list")) {
			JSONArray array = jsonObject.getJSONArray("goods_list");
			ArrayList list = new ArrayList();
			HashMap map;
			for (int i = 0; i < array.length(); i++) {
				JSONObject item = array.getJSONObject(i);
				map = new HashMap();
				if (item.has("goods_id")) {
					map.put("goods_id", item.getString("goods_id"));
				}
				if (item.has("goods_name")) {
					map.put("goods_name", item.getString("goods_name"));
				}
				if (item.has("goods_thumb")) {
					map.put("goods_thumb", item.getString("goods_thumb"));
				}
				if (item.has("goods_weight")) {
					map.put("goods_weight", item.getString("goods_weight"));
				}

				if (item.has("commodity_packaging")) {
					map.put("commodity_packaging",
							item.getString("commodity_packaging"));
				}
				if (item.has("goods_number")) {
					map.put("goods_number", item.getString("goods_number"));
				}

				list.add(map);
			}
			bundle.putParcelableArrayList("goods_list", list);
		}
		if (jsonObject.has("distribution_list")) {
			JSONArray array = jsonObject.getJSONArray("distribution_list");
			ArrayList list = new ArrayList();
			HashMap map;
			for (int i = 0; i < array.length(); i++) {
				JSONObject item = array.getJSONObject(i);
				map = new HashMap();
				if (item.has("time")) {
					map.put("time", item.getString("time"));
				}
				if (item.has("id")) {
					map.put("id", item.getString("id"));
				}

				if (item.has("order_id")) {
					map.put("order_id", item.getString("order_id"));
				}
				if (item.has("note")) {
					map.put("note", item.getString("note"));
				}
				if (item.has("status")) {
					map.put("status", item.getString("status"));
				}
				if (item.has("not_click")) {
					map.put("not_click", item.getString("not_click"));
				}

				if (item.has("goods_list")) {
					JSONArray arrayy = item.getJSONArray("goods_list");
					ArrayList listt = new ArrayList();
					HashMap mapp;
					for (int j = 0; j < array.length(); j++) {
						JSONObject itemm = arrayy.getJSONObject(j);
						mapp = new HashMap();

						if (itemm.has("goods_id")) {
							mapp.put("goods_id", itemm.getString("goods_id"));
						}
						if (itemm.has("goods_num")) {
							mapp.put("goods_num", itemm.getString("goods_num"));
						}

						if (itemm.has("goods_name")) {
							mapp.put("goods_name",
									itemm.getString("goods_name"));
						}

						listt.add(mapp);
					}
					map.put("goods_list", listt);
				}

				list.add(map);
			}
			bundle.putParcelableArrayList("distribution_list", list);
		}
		if (jsonObject.has("order_id")) {
			bundle.putString("order_id", jsonObject.getString("order_id"));
		}
		if (jsonObject.has("order_sn")) {
			bundle.putString("order_sn", jsonObject.getString("order_sn"));
		}
		if (jsonObject.has("goods_amount")) {
			bundle.putString("goods_amount",
					jsonObject.getString("goods_amount"));
		}
		if (jsonObject.has("bonus")) {
			bundle.putString("bonus", jsonObject.getString("bonus"));
		}
		if (jsonObject.has("surplus")) {
			bundle.putString("surplus", jsonObject.getString("surplus"));
		}
		if (jsonObject.has("integral_money")) {
			bundle.putString("integral_money",
					jsonObject.getString("integral_money"));
		}
		if (jsonObject.has("shipping_fee")) {
			bundle.putString("shipping_fee",
					jsonObject.getString("shipping_fee"));
		}
		if (jsonObject.has("ziti_id")) {
			bundle.putString("ziti_id", jsonObject.getString("ziti_id"));
		}
		if (jsonObject.has("ziti_name")) {
			bundle.putString("ziti_name", jsonObject.getString("ziti_name"));
		}
		if (jsonObject.has("consignee")) {
			bundle.putString("consignee", jsonObject.getString("consignee"));
		}
		if (jsonObject.has("mobile")) {
			bundle.putString("mobile", jsonObject.getString("mobile"));
		}

		return bundle;
	}
}
