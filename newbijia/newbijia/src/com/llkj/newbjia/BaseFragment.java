package com.llkj.newbjia;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.llkj.cm.restfull.requestmanager.RequestManager;
import com.llkj.newbjia.R;
import com.llkj.newbjia.http.NetworkErrorLog;
import com.llkj.newbjia.http.PoCRequestManager;
import com.llkj.newbjia.http.PoCService;
import com.llkj.newbjia.http.PoCRequestManager.OnRequestFinishedListener;
import com.llkj.newbjia.utils.LogUtil;
import com.llkj.newbjia.utils.ToastUtil;

public class BaseFragment extends Fragment implements OnRequestFinishedListener {

	protected View rootView;
	protected PoCRequestManager mRequestManager;
	protected ProgressDialog waitDialog;
	protected Boolean isLoaded = false;
	// TODO
	protected BackHandledInterface mBackHandledInterface;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mRequestManager = PoCRequestManager.from(activity);
		mRequestManager.addOnRequestFinishedListener(this);
		//TODO
		if(activity instanceof BackHandledInterface){
			mBackHandledInterface = (BackHandledInterface)activity;
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		dismissDialog();
	}

	@Override
	public void onResume() {
		super.onResume();
		mRequestManager.addOnRequestFinishedListener(this);
		// TODO
		if (mBackHandledInterface != null) {
			mBackHandledInterface.setSelectedFragment(this);
		}

	}

	@Override
	public void onStop() {
		super.onStop();
		mRequestManager.removeOnRequestFinishedListener(this);
	}

	/**
	 * 需在setContentView方法之后调用.
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
	protected void setTitle(View view, String title, boolean left, String l,
			boolean right, String r) {
		try {
			TextView tvTitle = (TextView) view.findViewById(R.id.tv_titile);
			TextView tvLeft = (TextView) view.findViewById(R.id.tv_title_left);
			TextView tvRight = (TextView) view
					.findViewById(R.id.tv_title_right);
			LinearLayout llBack = (LinearLayout) view
					.findViewById(R.id.ll_title_back);

			if (!TextUtils.isEmpty(title)) {
				tvTitle.setVisibility(View.VISIBLE);
				tvTitle.setText(title);
			} else {
				tvTitle.setVisibility(View.GONE);
			}
			if (left) {
				llBack.setVisibility(View.VISIBLE);
				tvLeft.setText(l);
				llBack.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						getActivity().finish();
					}
				});
			} else {
				llBack.setVisibility(View.INVISIBLE);
			}
			if (right) {
				tvRight.setVisibility(View.VISIBLE);
				tvRight.setText(r);
			} else {
				tvRight.setVisibility(View.INVISIBLE);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 需在setContentView方法之后调用.
	 * 
	 * @param mView
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
	protected void setTitle(View mView, int resTitle, boolean left, int resL,
			boolean right, int resR) {
		setTitle(mView, getString(resTitle), left, resL == -1 ? ""
				: getString(resL), right, resR == -1 ? "" : getString(resR));
	}

	/**
	 * 全局等待对话框
	 */
	public void showWaitDialog() {
		waitDialog = new ProgressDialog(getActivity());
		waitDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		waitDialog.setCanceledOnTouchOutside(false);
		ImageView view = new ImageView(getActivity());
		view.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		Animation loadAnimation = AnimationUtils.loadAnimation(getActivity(),
				R.anim.rotate);
		view.startAnimation(loadAnimation);
		loadAnimation.start();
		view.setImageResource(R.drawable.loading);
		// waitDialog.setCancelable(false);
		waitDialog.show();
		waitDialog.setContentView(view);
		LogUtil.i("waitDialong.......");
	}

	public void dismissDialog() {

		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (waitDialog != null && waitDialog.isShowing()) {
					waitDialog.dismiss();
					waitDialog = null;
				}
			}
		});
	}

	@Override
	public void onRequestFinished(int requestId, int resultCode, Bundle payload) {
		dismissDialog();
		if (resultCode == PoCService.ERROR_CODE) {
			isLoaded = false;
			if (payload != null) {
				final int errorType = payload.getInt(
						RequestManager.RECEIVER_EXTRA_ERROR_TYPE, -1);
				NetworkErrorLog.networkErrorOperate(getActivity()
						.getApplicationContext(), errorType);
			} else {
				ToastUtil.makeShortText(getActivity(), "服务器出错！");
			}
		}
	}

	@Override
	public void onRequestPrepareListener() {
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				showWaitDialog();
			}
		});
	}

	// TODO
	public interface BackHandledInterface {
		public abstract void setSelectedFragment(BaseFragment selectedFragment);
	}

}
