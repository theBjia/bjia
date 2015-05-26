package com.llkj.newbjia.http;

import android.content.Context;

import com.llkj.newbjia.utils.ToastUtil;

public class NetworkErrorLog {

	public static void networkErrorOperate(Context context, int errorType) {

		switch (errorType) {
		case PoCRequestManager.RECEIVER_EXTRA_VALUE_ERROR_TYPE_DATA:
			// ToastUtil.makeShortText(context, "数据解析异常");
			break;
		case PoCRequestManager.RECEIVER_EXTRA_VALUE_ERROR_TYPE_PERMISSION:
			// ToastUtil.makeShortText(context, "Please register");
			break;
		case PoCRequestManager.RECEIVER_EXTRA_VALUE_ERROR_TYPE_SERVICE:
			// ToastUtil.makeShortText(context, "Server busy！");
			break;
		case PoCRequestManager.RECEIVER_EXTRA_VALUE_ERROR_TYPE_REQUEST_PARAM:
			// ToastUtil.makeShortText(context, "The request parameter error！");
			break;
		case PoCRequestManager.RECEIVER_EXTRA_VALUE_ERROR_TYPE_RESPONSE_NULL:
			// ToastUtil.makeShortText(context, "无相关数据");
			break;
		default:
			ToastUtil.makeShortText(context, "请检查网络连接");
			break;

		}

	}

}
