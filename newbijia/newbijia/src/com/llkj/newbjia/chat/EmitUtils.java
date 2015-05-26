package com.llkj.newbjia.chat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.llkj.newbjia.MyApplication;
import com.llkj.newbjia.bean.UserInfoBean;

public class EmitUtils {
	// 4、推送将某人加入某群或创建某群时将某人加入某群事件信息
	public static JSONArray toJSONObject(String user_id, String group_id,
			String group_name, String creator_name, String creator,
			String to_users, String target_id, String type)
			throws JSONException {

		JSONArray ja = new JSONArray();
		JSONObject json = new JSONObject();

		json.put("user_id", user_id);
		json.put("group_id", group_id);
		json.put("group_name", group_name);
		json.put("creator_name", creator_name);
		json.put("creator", creator);
		json.put("to_users", to_users);
		json.put("target_id", target_id);
		json.put("type", type);
		ja.put(json);
		return ja;

	}

	public static void deleteMsgNotifyServer(Context context, String msgId,
			int type) {
		JSONArray ja = new JSONArray();
		JSONObject json = new JSONObject();
		try {
			UserInfoBean.getUserInfo(context);
			json.put("user_id", UserInfoBean.getUserInfo(context).getUid());
			json.put("msg_id", msgId);
			ja.put(json);
			switch (type) {
			case 0:
				MyApplication.client.emit("onChatRemove", ja);
				break;
			case 1:
				MyApplication.client.emit("onEsqRemove", ja);
				break;
			default:
				break;
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
