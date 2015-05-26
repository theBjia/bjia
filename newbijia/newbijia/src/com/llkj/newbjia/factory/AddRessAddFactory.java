package com.llkj.newbjia.factory;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.llkj.newbjia.bean.ResponseBean;

import android.os.Bundle;


public class AddRessAddFactory {
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
		return bundle;
	}
}
