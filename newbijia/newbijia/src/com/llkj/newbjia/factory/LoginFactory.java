package com.llkj.newbjia.factory;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;

import com.llkj.newbjia.bean.ResponseBean;

public class LoginFactory {
	public static Bundle parseResult(String wsResponse) throws JSONException {

		JSONObject jsonObject = new JSONObject(wsResponse);
		Bundle bundle = new Bundle();
		if (jsonObject.has(ResponseBean.RESPONSE_STATE)) {
			bundle.putInt(ResponseBean.RESPONSE_STATE,
					jsonObject.getInt(ResponseBean.RESPONSE_STATE));
		}

		if (jsonObject.has(ResponseBean.RESPONSE_UID)) {
			bundle.putString(ResponseBean.RESPONSE_UID,
					jsonObject.getString(ResponseBean.RESPONSE_UID));
		}
		if (jsonObject.has(ResponseBean.RESPONSE_PHONE)) {
			bundle.putString(ResponseBean.RESPONSE_PHONE,
					jsonObject.getString(ResponseBean.RESPONSE_PHONE));
		}
		if (jsonObject.has(ResponseBean.RESPONSE_USERNAME)) {
			bundle.putString(ResponseBean.RESPONSE_USERNAME,
					jsonObject.getString(ResponseBean.RESPONSE_USERNAME));
		}
		if (jsonObject.has(ResponseBean.RESPONSE_LOGO)) {
			bundle.putString(ResponseBean.RESPONSE_LOGO,
					jsonObject.getString(ResponseBean.RESPONSE_LOGO));
		}
		if (jsonObject.has(ResponseBean.RESPONSE_MESSAGE)) {
			bundle.putString(ResponseBean.RESPONSE_MESSAGE,
					jsonObject.getString(ResponseBean.RESPONSE_MESSAGE));
		}
		return bundle;
	}
}
