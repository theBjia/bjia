package com.llkj.newbjia.factory;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;

import com.llkj.newbjia.bean.ResponseBean;

public class VersionFactory {
	public static Bundle parseResult(String wsResponse) throws JSONException {

		JSONObject jsonObject = new JSONObject(wsResponse);
		Bundle bundle = new Bundle();
		if (jsonObject.has(ResponseBean.RESPONSE_STATE)) {
			bundle.putInt(ResponseBean.RESPONSE_STATE,
					jsonObject.getInt(ResponseBean.RESPONSE_STATE));
		}

		if (jsonObject.has(ResponseBean.RESPONSE_TITLE)) {
			bundle.putString(ResponseBean.RESPONSE_TITLE,
					jsonObject.getString(ResponseBean.RESPONSE_TITLE));
		}
		if (jsonObject.has(ResponseBean.RESPONSE_MESSAGE)) {
			bundle.putString(ResponseBean.RESPONSE_MESSAGE,
					jsonObject.getString(ResponseBean.RESPONSE_MESSAGE));
		}
		if (jsonObject.has(ResponseBean.RESPONSE_URL)) {
			bundle.putString(ResponseBean.RESPONSE_URL,
					jsonObject.getString(ResponseBean.RESPONSE_URL));
		}


		return bundle;
	}
}
