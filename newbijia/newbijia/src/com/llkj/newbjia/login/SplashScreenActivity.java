package com.llkj.newbjia.login;

/**
 * @author 
 * 启动页
 * */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import com.llkj.cm.restfull.requestmanager.RequestManager;
import com.llkj.newbjia.BaseActivity;
import com.llkj.newbjia.MainActivity;
import com.llkj.newbjia.MyApplication;
import com.llkj.newbjia.R;
import com.llkj.newbjia.bean.UserInfoBean;
import com.llkj.newbjia.http.NetworkErrorLog;
import com.llkj.newbjia.http.PoCRequestManager;
import com.llkj.newbjia.http.PoCService;
import com.llkj.newbjia.utils.FinalBitmapUtil;
import com.llkj.newbjia.utils.StringUtil;
import com.llkj.newbjia.utils.Utils;

public class SplashScreenActivity extends BaseActivity {
	private AlphaAnimation aa;
	private TranslateAnimation ta;
	private View view;
	private PoCRequestManager mRequestManager;
	private int mRequestId;
	private ImageView iv;
	private ImageView iv_logo;
	private FinalBitmapUtil fb;
	private DisplayMetrics displaysMetrics;
	private int width, height;
	private boolean isPass;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		view = View.inflate(this, R.layout.layout_splashscreen, null);
		setContentView(view);
		displaysMetrics = this.getResources().getDisplayMetrics();
		width = displaysMetrics.widthPixels;
		height = displaysMetrics.heightPixels;
		mRequestManager = PoCRequestManager.from(this);
		iv = (ImageView) findViewById(R.id.imageView1);
		// TODO
		iv_logo = (ImageView) findViewById(R.id.iv_logo_splash);
		fb = FinalBitmapUtil.create(this);
		// set the translation of logo.
		ta = new TranslateAnimation(width / 2 - 80,
				 width / 2 - 80, 0,
				 height / 2 - 150);
		ta.setDuration(2000);
		ta.setFillAfter(true);
		iv_logo.startAnimation(ta);

		// 渐变展示启动屏
		 aa = new AlphaAnimation(1.0f, 1.0f);
		 aa.setDuration(2000);
		 view.startAnimation(aa);
		// TODO
		ta.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation arg0) {
				redirectTo();
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationStart(Animation animation) {
			}

		});

//		 aa.setAnimationListener(new AnimationListener() {
//		 @Override
//		 public void onAnimationEnd(Animation arg0) {
//		 redirectTo();
//		 }
//		
//		 @Override
//		 public void onAnimationRepeat(Animation animation) {
//		 }
//		
//		 @Override
//		 public void onAnimationStart(Animation animation) {
//		 }
//		
//		 });

	}

	// 跳转
	private void redirectTo() {
		Intent intent = new Intent(this, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		MyApplication.isFromLogin = false;
		startActivity(intent);
		finish();

	}

	// TODO move the logo.
	private void animationOfLogo() {

	}

	@Override
	protected void onPause() {

		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub

		super.onDestroy();
	}

	@Override
	public void onRequestFinished(int requestId, int resultCode, Bundle payload) {
		// TODO Auto-generated method stub
		if (resultCode == PoCService.ERROR_CODE) {
			if (payload != null) {
				final int errorType = payload.getInt(
						RequestManager.RECEIVER_EXTRA_ERROR_TYPE, -1);
				NetworkErrorLog.networkErrorOperate(getApplicationContext(),
						errorType);
			} else {
				Toast.makeText(this, "服务器出错！", Toast.LENGTH_SHORT).show();
			}
		} else {
			if (mRequestId == requestId) {
				int state = payload.getInt("state");
				if (state == 1) {

					String type_three = payload.getString("android_small");
					String type_four = payload.getString("android_middle");
					String type_five = payload.getString("android_big");
					if (width == 480 && height == 800) {
						if (!StringUtil.isEmpty(type_three)) {
//							fb.displayForPicture(iv, type_three);
							fb.displayForPicture(iv_logo, type_three);
						}
					} else if (width == 720 && height == 1280) {
						if (!StringUtil.isEmpty(type_four)) {
							fb.displayForPicture(iv_logo, type_four);
						}
					} else if (width == 1080 && height == 1920) {
						if (!StringUtil.isEmpty(type_five)) {
							fb.displayForPicture(iv_logo, type_five);
						}
					} else {
						if (!StringUtil.isEmpty(type_four)) {
							fb.displayForPicture(iv_logo, type_four);
						}
					}

				} else {
					String message = payload.getString("message");
					Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
				}
			}
		}
	}

}
