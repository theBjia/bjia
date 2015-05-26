package com.llkj.newbjia.factory;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;

import com.llkj.newbjia.bean.ResponseBean;

public class DefaultAddressFactory {
	public static Bundle parseResult(String wsResponse) throws JSONException {

		JSONObject jsonObject = new JSONObject(wsResponse);
		Bundle bundle = new Bundle();
		if (jsonObject.has(ResponseBean.RESPONSE_STATE)) {
			bundle.putInt(ResponseBean.RESPONSE_STATE,
					jsonObject.getInt(ResponseBean.RESPONSE_STATE));
		}

		if (jsonObject.has(ResponseBean.RESPONSE_ID)) {
			bundle.putString(ResponseBean.RESPONSE_ID,
					jsonObject.getString(ResponseBean.RESPONSE_ID));
		}
		if (jsonObject.has(ResponseBean.RESPONSE_MESSAGE)) {
			bundle.putString(ResponseBean.RESPONSE_MESSAGE,
					jsonObject.getString(ResponseBean.RESPONSE_MESSAGE));
		}
		if (jsonObject.has(ResponseBean.RESPONSE_ADDRESS)) {
			bundle.putString(ResponseBean.RESPONSE_ADDRESS,
					jsonObject.getString(ResponseBean.RESPONSE_ADDRESS));
		}
		if (jsonObject.has(ResponseBean.RESPONSE_CONSIGNEE)) {
			bundle.putString(ResponseBean.RESPONSE_CONSIGNEE,
					jsonObject.getString(ResponseBean.RESPONSE_CONSIGNEE));
		}
		if (jsonObject.has(ResponseBean.RESPONSE_ZITI_ID)) {
			bundle.putString(ResponseBean.RESPONSE_ZITI_ID,
					jsonObject.getString(ResponseBean.RESPONSE_ZITI_ID));
		}
		if (jsonObject.has(ResponseBean.RESPONSE_ZITI_NAME)) {
			bundle.putString(ResponseBean.RESPONSE_ZITI_NAME,
					jsonObject.getString(ResponseBean.RESPONSE_ZITI_NAME));
		}
		if (jsonObject.has(ResponseBean.RESPONSE_PHONE)) {
			bundle.putString(ResponseBean.RESPONSE_PHONE,
					jsonObject.getString(ResponseBean.RESPONSE_PHONE));
		}

		return bundle;
	}
}
