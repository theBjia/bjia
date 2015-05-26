package com.llkj.newbjia.factory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;

import com.llkj.newbjia.bean.ResponseBean;

public class FriendPenFactory {
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
				if (item.has("id")) {
					map.put("id", item.getString("id"));
				}
				if (item.has("user_id")) {
					
					
					map.put("user_id", item.getString("user_id"));
				}
				if (item.has("add_time")) {
					String time=item.getString("add_time");
					SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					try {
						Date date = sdf.parse(time);
						
						long Time = (date.getTime() / 1000) - 480* 60;  
						   date.setTime(Time * 1000);  
						   map.put("add_time", date);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
				}
				if (item.has("content")) {
					map.put("content", item.getString("content"));
				}
				if (item.has("love")) {
					map.put("love", item.getString("love"));
				}
				if (item.has("comments")) {
					ArrayList alstwo = new ArrayList();
					JSONArray jastwo = item.getJSONArray("comments");
					for (int j = 0; j < jastwo.length(); j++) {
						HashMap maptwo = new HashMap();
						JSONObject jotwo = jastwo.getJSONObject(j);
						if (jotwo.has("content")) {
							maptwo.put("content", jotwo.getString("content"));
						}
						if (jotwo.has("add_time")) {
							maptwo.put("add_time", jotwo.getString("add_time"));
						}
						if (jotwo.has("from_id")) {
							maptwo.put("from_id", jotwo.getString("from_id"));
						}
						if (jotwo.has("from_name")) {
							maptwo.put("from_name",
									jotwo.getString("from_name"));
						}
						if (jotwo.has("to_id")) {
							maptwo.put("to_id", jotwo.getString("to_id"));
						}
						if (jotwo.has("to_name")) {
							maptwo.put("to_name", jotwo.getString("to_name"));
						}
						alstwo.add(maptwo);
					}
					map.put("comments", alstwo);
				}
				if (item.has("pictures")) {
					ArrayList alsone = new ArrayList();
					JSONArray jasone = item.getJSONArray("pictures");
					for (int j = 0; j < jasone.length(); j++) {
						alsone.add(jasone.get(j));
					}
					map.put("pictures", alsone);

				}
				if (item.has("name")) {
					map.put("name", item.getString("name"));
				}
				if (item.has("pic")) {
					map.put("pic", item.getString("pic"));
				}
				if (item.has("number")) {
					map.put("number", item.getString("number"));
				}

				list.add(map);
			}
			bundle.putParcelableArrayList(ResponseBean.RESPONSE_LIST, list);
		}

		return bundle;
	}
}
