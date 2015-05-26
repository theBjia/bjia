package com.llkj.newbjia.factory;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;

import com.llkj.newbjia.MyApplication;
import com.llkj.newbjia.bean.ResponseBean;

public class ZitiDistributionFactory {
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
				if (item.has(ResponseBean.RESPONSE_NAME)) {
					map.put(ResponseBean.RESPONSE_NAME,
							item.getString(ResponseBean.RESPONSE_NAME));
				}
				if (item.has(ResponseBean.RESPONSE_LNG)) {
					map.put(ResponseBean.RESPONSE_LNG,
							item.getString(ResponseBean.RESPONSE_LNG));
				}
				if (item.has(ResponseBean.RESPONSE_LAT)) {
					map.put(ResponseBean.RESPONSE_LAT,
							item.getString(ResponseBean.RESPONSE_LAT));
				}

				double s = getDistance(Double.parseDouble(item
						.getString(ResponseBean.RESPONSE_LNG)),
						Double.parseDouble(item
								.getString(ResponseBean.RESPONSE_LAT)));

				if (s < 1000) {
					map.put("s", s);
					list.add(map);
				}
			}

			
			bundle.putParcelableArrayList(ResponseBean.RESPONSE_LIST, list);
		}

		return bundle;
	}

	private static final double EARTH_RADIUS = 6378137.0;

	// 返回单位是米
	public static double getDistance(double longitude2, double latitude2) {
		double longitude1 = Double.parseDouble(MyApplication.geoLng);
		double latitude1 = Double.parseDouble(MyApplication.geoLat);

		double Lat1 = rad(latitude1);
		double Lat2 = rad(latitude2);
		double a = Lat1 - Lat2;
		double b = rad(longitude1) - rad(longitude2);
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
				+ Math.cos(Lat1) * Math.cos(Lat2)
				* Math.pow(Math.sin(b / 2), 2)));
		s = s * EARTH_RADIUS;
		s = Math.round(s * 10000) / 10000;
		return s;
	}

	private static double rad(double d) {
		return d * Math.PI / 180.0;
	}

}
