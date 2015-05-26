package com.llkj.newbjia.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;

public class MyWebView extends WebView {

	private View mTitleBar;

	private LinearLayout.LayoutParams mTitleBarLayoutParams;

	public MyWebView(Context context) {
		super(context);

	}

	public MyWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyWebView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void setEmbeddedTitleBar(View v) {
		if (mTitleBar == v)
			return;
		if (mTitleBar != null) {
			removeView(mTitleBar);
		}
		if (null != v) {
			mTitleBarLayoutParams = new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);

			v.setLayoutParams(mTitleBarLayoutParams);
			addView(v);
			setInitialScale(100);
		}
		mTitleBar = v;
	}

}