package com.llkj.newbjia.factory;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;

import com.llkj.newbjia.bean.ResponseBean;

public class FriendSearchFactory {
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
		if (jsonObject.has(ResponseBean.RESPONSE_UID)) {
			bundle.putString(ResponseBean.RESPONSE_UID,
					jsonObject.getString(ResponseBean.RESPONSE_UID));
		}

		if (jsonObject.has(ResponseBean.RESPONSE_USER_NAME)) {
			bundle.putString(ResponseBean.RESPONSE_USER_NAME,
					jsonObject.getString(ResponseBean.RESPONSE_USER_NAME));
		}

		if (jsonObject.has(ResponseBean.RESPONSE_SIGNATURE)) {
			bundle.putString(ResponseBean.RESPONSE_SIGNATURE,
					jsonObject.getString(ResponseBean.RESPONSE_SIGNATURE));
		}

		if (jsonObject.has(ResponseBean.RESPONSE_LOGO)) {
			bundle.putString(ResponseBean.RESPONSE_LOGO,
					jsonObject.getString(ResponseBean.RESPONSE_LOGO));
		}

		if (jsonObject.has(ResponseBean.RESPONSE_TYPE)) {
			bundle.putString(ResponseBean.RESPONSE_TYPE,
					jsonObject.getString(ResponseBean.RESPONSE_TYPE));
		}

		return bundle;
	}
}
