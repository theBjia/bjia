package com.llkj.newbjia.factory;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;

import com.llkj.newbjia.bean.ResponseBean;

public class PurchaseBuyFactory {
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
		if (jsonObject.has(ResponseBean.RESPONSE_ORDER_SN)) {
			bundle.putString(ResponseBean.RESPONSE_ORDER_SN,
					jsonObject.getString(ResponseBean.RESPONSE_ORDER_SN));
		}

		return bundle;
	}
}
