package com.llkj.newbjia.utils;

import java.io.File;

import net.tsz.afinal.FinalBitmap;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;

import com.llkj.newbjia.R;

public class FinalBitmapUtil {
	static FinalBitmap fb;
	static Context mContext;
	private static FinalBitmapUtil instance;
	private static String cachePath = Environment.getExternalStorageDirectory()
			.getAbsolutePath()
			+ File.separator
			+ "bijia"
			+ File.separator
			+ "image";

	private FinalBitmapUtil(Context context) {
		if (fb == null) {
			mContext = context;
			fb = FinalBitmap.create(context);
			fb.configLoadingImage(R.drawable.icon_defalut);
			fb.configLoadfailImage(R.drawable.icon_defalut);
			// fb.configDiskCachePath(cachePath);
		}
	}

	public static FinalBitmapUtil create(Context context) {
		if (instance == null) {
			return new FinalBitmapUtil(context.getApplicationContext());
		} else {
			return instance;
		}
	}

	/**
	 * 用于头像显示 .已经定义好loadingbitmap 和failedbitmap.
	 * 
	 * @param mContext
	 * 
	 * @return
	 */
	public void displayForHeader(View view, String uri) {
		if (StringUtil.isEmpty(uri)) {
			ImageView iv = (ImageView) view;
			iv.setImageResource(R.drawable.icon_defalut);
		} else {
			fb.display(
					view,
					uri,
					((BitmapDrawable) mContext.getResources().getDrawable(
							R.drawable.icon_defalut)).getBitmap(),
					((BitmapDrawable) mContext.getResources().getDrawable(
							R.drawable.icon_defalut)).getBitmap());
		}

		// fb.configLoadingImage(R.drawable.icon_head_default);
		// fb.configLoadfailImage(R.drawable.icon_head_default);
		// fb.display(view, uri);
	}

	/**
	 * 用于图片显示 .已经定义好loadingbitmap 和failedbitmap.
	 * 
	 * @param mContext
	 * 
	 * @return
	 */
	public void displayForPicture(View view, String uri) {
		if (StringUtil.isEmpty(uri)) {
			ImageView iv = (ImageView) view;
			iv.setImageResource(R.drawable.icon_defalut);
		} else {
			fb.display(
					view,
					uri,
					((BitmapDrawable) mContext.getResources().getDrawable(
							R.drawable.icon_defalut)).getBitmap(),
					((BitmapDrawable) mContext.getResources().getDrawable(
							R.drawable.icon_defalut)).getBitmap());
		}

		// fb.configLoadingImage(R.drawable.icon_default_logo);
		// fb.configLoadfailImage(R.drawable.icon_default_logo);
		// fb.display(view, uri);
	}

	public FinalBitmap getFinalBitmap() {
		return fb;
	}
}
