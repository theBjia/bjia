package com.llkj.newbjia.setting;

import android.os.Bundle;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.llkj.newbjia.BaseActivity;
import com.llkj.newbjia.R;
import com.llkj.newbjia.utils.StringUtil;
import com.llkj.newbjia.utils.ToastUtil;

public class GongNengJieShaoActivity extends BaseActivity {
	private WebView webview;
	private String url;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_goods_info_two);
		setTitle(R.string.gongneng_intro, true, R.string.kongzifuchuan, false,
				R.string.kongzifuchuan);

		initView();
		initListener();
		initData();
	}

	private void initView() {

		webview = (WebView) findViewById(R.id.webview);
		// 表示不支持js，如果想让java和js交互或者本身希望js完成一定的功能请把false改为true。
		webview.getSettings().setJavaScriptEnabled(false);
		// 设置是否支持缩放，我这里为false，默认为true。
		webview.getSettings().setSupportZoom(false);
		// 设置是否显示缩放工具，默认为false。
		webview.getSettings().setBuiltInZoomControls(false);
		// 一般很少会用到这个，用WebView组件显示普通网页时一般会出现横向滚动条，这样会导致页面查看起来非常不方便。
		/**
		 * LayoutAlgorithm是一个枚举，用来控制html的布局，总共有三种类型： NORMAL：正常显示，没有渲染变化。
		 * SINGLE_COLUMN：把所有内容放到WebView组件等宽的一列中。
		 * NARROW_COLUMNS：可能的话，使所有列的宽度不超过屏幕宽度。
		 */
		webview.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		// 设置默认的字体大小，默认为16，有效值区间在1-72之间。
		webview.getSettings().setDefaultFontSize(18);
		webview.setWebViewClient(new MyWebViewClient());
		if (StringUtil.isNetworkConnected(this)) {

			url = "http://baifahui.bloveambition.com/api/appPerson.php?action=info";
			webview.loadUrl(url);

		} else {
			ToastUtil.makeShortText(this, R.string.no_wangluo);
		}

	}

	private void initListener() {
		// TODO Auto-generated method stub

	}

	private void initData() {
		// TODO Auto-generated method stub

	}

	private class MyWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	}

}
