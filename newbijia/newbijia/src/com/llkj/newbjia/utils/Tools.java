package com.llkj.newbjia.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.llkj.newbjia.R;

public class Tools {

	public static DisplayMetrics getDisplayMetrics(Context context) {
		return context.getResources().getDisplayMetrics();
	}

	/**
	 * 根据手机分辨率从 px(像素) 单位 转成 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 根据手机分辨率从 dp 单位 转成 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 判断当前是否有网络
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	public static String genMuLu(Activity activity) {
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())
				&& Environment.getExternalStorageDirectory().exists()) {
			return Environment.getExternalStorageDirectory().getAbsolutePath();
		} else {
			return activity.getApplication().getFilesDir().getAbsolutePath();
		}
	}

	public static PopupWindow getPopupWindow(Context context, View view,
			int color) {
		PopupWindow popupWindow = new PopupWindow(view);
		popupWindow.setBackgroundDrawable(new ColorDrawable(color));// 没有此句点击外部不会消失
		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(true);
		popupWindow.setAnimationStyle(R.style.PopupAnimation);
		return popupWindow;
	}

	public static void dissPopw(PopupWindow p) {
		if (p != null && p.isShowing()) {
			p.dismiss();
		}
	}

	public static void dissDialog(Dialog d) {
		if (d != null && d.isShowing()) {
			d.dismiss();
		}
	}

}
