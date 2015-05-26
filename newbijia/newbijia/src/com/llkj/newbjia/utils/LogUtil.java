package com.llkj.newbjia.utils;

import android.util.Log;

/**
 * 日志工具类
 * 
 * @author Administrator
 * 
 */
public class LogUtil {

	// 打印开关
	public static boolean isOpen = true;
	// 打印的标签
	public static final String TAG = "彼佳";

	// 封装方法
	public static void i(String tag, String msg) {
		if (isOpen) {
			Log.i(tag, msg);
		}

	}

	// 封装方法
	public static void i(String msg) {
		if (isOpen) {
			Log.i(TAG, msg);

		}
	}

	// 封装方法
	public static void e(String msg) {
		if (isOpen) {
			Log.e(TAG, msg);

		}
	}

	// 封装方法
	public static void e(String tag, String msg) {
		if (isOpen) {
			Log.e(tag, msg);
		}

	}

}
