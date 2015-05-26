package com.llkj.newbjia;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.llkj.cm.restfull.requestmanager.RequestManager;
import com.llkj.newbjia.R;
import com.llkj.newbjia.http.NetworkErrorLog;
import com.llkj.newbjia.http.PoCRequestManager;
import com.llkj.newbjia.http.PoCService;
import com.llkj.newbjia.http.PoCRequestManager.OnRequestFinishedListener;
import com.llkj.newbjia.utils.StringUtil;
import com.llkj.newbjia.utils.ToastUtil;

/**
 * Activity 基类. Created by zhang.zk on 2014/10/8 0008.
 */
public class BaseActivity extends Activity implements OnRequestFinishedListener {
	protected PoCRequestManager mRequestManager;
	protected ProgressDialog waitDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		MyApplication.getInstance().addActivity(this);

		mRequestManager = PoCRequestManager.from(this);
		mRequestManager.addOnRequestFinishedListener(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		dismissDialog();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mRequestManager.removeOnRequestFinishedListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mRequestManager.addOnRequestFinishedListener(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		mRequestManager.removeOnRequestFinishedListener(this);
	}

	/**
	 * 需在setContentView方法之后调用。标题栏的显示和设置。
	 * 
	 * @param title
	 *            标题
	 * @param left
	 *            是否显示左侧的部分
	 * @param l
	 *            左侧部分文字
	 * @param right
	 *            是否显示右侧的部分
	 * @param r
	 *            右侧部分的文字
	 */
	protected void setTitle(String title, boolean left, String l,
			boolean right, String r) {
		
		TextView tvTitle = (TextView) findViewById(R.id.tv_titile);
		TextView tvLeft = (TextView) findViewById(R.id.tv_title_left);
		TextView tvRight = (TextView) findViewById(R.id.tv_title_right);

		LinearLayout llBack = (LinearLayout) findViewById(R.id.ll_title_back);

		if (!TextUtils.isEmpty(title)) {
			tvTitle.setVisibility(View.VISIBLE);
			tvTitle.setText(title);
		} else {
			tvTitle.setVisibility(View.GONE);
		}
		if (left) {
			tvLeft.setVisibility(View.VISIBLE);
			tvLeft.setText(l);
			llBack.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					BaseActivity.this.finish();
					StringUtil.hideSoft(BaseActivity.this);
				}
			});
		} else {
			tvLeft.setVisibility(View.INVISIBLE);
		}
		if (right) {
			tvRight.setVisibility(View.VISIBLE);
			tvRight.setText(r);
		} else {
			tvRight.setVisibility(View.INVISIBLE);
		}

	}

	/**
	 * 需在setContentView方法之后调用.
	 * 
	 * @param resTitle
	 *            标题
	 * @param left
	 *            是否显示左侧的部分
	 * @param resL
	 *            左侧部分文字
	 * @param right
	 *            是否显示右侧的部分
	 * @param resR
	 *            右侧部分的文字
	 */
	protected void setTitle(int resTitle, boolean left, int resL,
			boolean right, int resR) {
		setTitle(getString(resTitle), left, resL == -1 ? "" : getString(resL),
				right, resR == -1 ? "" : getString(resR));
	}

	/**
	 * 子类需要重写
	 */
	@Override
	public void onRequestFinished(int requestId, int resultCode, Bundle payload) {
		dismissDialog();
		if (resultCode == PoCService.ERROR_CODE) {
			if (payload != null) {
				final int errorType = payload.getInt(
						RequestManager.RECEIVER_EXTRA_ERROR_TYPE, -1);
				NetworkErrorLog.networkErrorOperate(
						this.getApplicationContext(), errorType);
			} else {
				ToastUtil.makeShortText(this, "服务器出错！");
			}
		}
	}

	@Override
	public void onRequestPrepareListener() {
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				showWaitDialog();
			}
		});
	}

	/**
	 * 全局等待对话框的显示
	 */
	public void showWaitDialog() {
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				waitDialog = new ProgressDialog(BaseActivity.this);
				waitDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				waitDialog.setCanceledOnTouchOutside(false);
				ImageView view = new ImageView(BaseActivity.this);
				view.setLayoutParams(new LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				Animation loadAnimation = AnimationUtils.loadAnimation(
						BaseActivity.this, R.anim.rotate);
				view.startAnimation(loadAnimation);
				loadAnimation.start();
				view.setImageResource(R.drawable.loading);
				waitDialog.show();
				waitDialog.setContentView(view);
			}
		});

	}

	/**
	 * 等待提示的清除
	 */
	public void dismissDialog() {
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (waitDialog != null && waitDialog.isShowing()) {
					waitDialog.dismiss();
					waitDialog = null;
				}
			}
		});

	}

}
