package com.llkj.newbjia.setting;

import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract.Instances;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.llkj.newbjia.BaseFragment;
import com.llkj.newbjia.MainActivity;
import com.llkj.newbjia.MyApplication;
import com.llkj.newbjia.R;
import com.llkj.newbjia.bean.UserInfoBean;
import com.llkj.newbjia.chat.Constants;
import com.llkj.newbjia.mybijia.AboutActivity;
import com.llkj.newbjia.mybijia.FeedbackActivity;
import com.llkj.newbjia.mybijia.UpdatePassword;
import com.llkj.newbjia.utils.PreferenceHelper;
import com.llkj.newbjia.utils.SlidButton;
import com.llkj.newbjia.utils.ToastUtil;
import com.llkj.newbjia.utils.SlidButton.OnChangedListener;

/**
 * 设置页
 * 
 * @author John
 * 
 */
public class MySettingFragment extends BaseFragment implements OnClickListener {
	// 震动按钮 声音按钮
	SlidButton su_Shake, su_Voice;
	// 关于我们 意见反馈 修改密码
	RelativeLayout rl_About, rl_Feedback, rl_PasswordUpdate;
	private LinearLayout ll_title_back;
	private Button btnLogout;
	private boolean isZhendong, isLingsheng;
	private String uid;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.my_setting, null);
			initView();
			initListener();
			initData();
		} else {
			ViewGroup parent = (ViewGroup) rootView.getParent();
			if (parent != null) {
				parent.removeView(rootView);
			}
		}
		return rootView;
	}

	private void initView() {
		uid = UserInfoBean.getUserInfo(getActivity()).getUid();
		btnLogout = (Button) rootView.findViewById(R.id.btn_logout);
		rl_About = (RelativeLayout) rootView.findViewById(R.id.rl_About);
		rl_Feedback = (RelativeLayout) rootView.findViewById(R.id.rl_Feedback);
		rl_PasswordUpdate = (RelativeLayout) rootView
				.findViewById(R.id.rl_PasswordUpdate);
		su_Shake = (SlidButton) rootView.findViewById(R.id.su_Shake);
		su_Voice = (SlidButton) rootView.findViewById(R.id.su_Voice);
		su_Shake.SetOnChangedListener(new OnChangedListener() {
			@Override
			public void OnChanged(boolean checkState) {
				if (checkState) {

					ToastUtil.makeShortText(getActivity(), "震动开");
				} else {
					ToastUtil.makeShortText(getActivity(), "震动关");
				}
				PreferenceHelper.write(getActivity(), uid
						+ Constants.NOTIFYSTYLE, "iszhendong", checkState);
			}
		});
		su_Voice.SetOnChangedListener(new OnChangedListener() {

			@Override
			public void OnChanged(boolean checkState) {
				if (checkState) {

					ToastUtil.makeShortText(getActivity(), "声音开");
				} else {
					ToastUtil.makeShortText(getActivity(), "声音关");
				}
				PreferenceHelper.write(getActivity(), uid
						+ Constants.NOTIFYSTYLE, "isLingsheng", checkState);
			}
		});
		ll_title_back = (LinearLayout) rootView
				.findViewById(R.id.ll_title_back);
	}

	private void initData() {
		isZhendong = PreferenceHelper.readBoolean(getActivity(), uid
				+ Constants.NOTIFYSTYLE, "iszhendong", true);
		isLingsheng = PreferenceHelper.readBoolean(getActivity(), uid
				+ Constants.NOTIFYSTYLE, "isLingsheng", true);
		su_Shake.setCheck(isZhendong);
		su_Voice.setCheck(isLingsheng);
	}

	private void initListener() {
		rl_About.setOnClickListener(this);
		rl_Feedback.setOnClickListener(this);
		rl_PasswordUpdate.setOnClickListener(this);
		ll_title_back.setOnClickListener(this);
		btnLogout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.rl_About:
			intent = new Intent(getActivity(), AboutActivity.class);
			startActivity(intent);
			break;

		case R.id.rl_Feedback:
			intent = new Intent(getActivity(), FeedbackActivity.class);
			startActivity(intent);

			break;
		case R.id.rl_PasswordUpdate:
			intent = new Intent(getActivity(), UpdatePassword.class);
			startActivity(intent);
			break;
		case R.id.ll_title_back:
			if ((MainActivity) getActivity() != null) {
				((MainActivity) getActivity()).switchContent(0);
			}
			break;
		case R.id.btn_logout:
			MyApplication.showAlertDialog(getActivity());
			break;
		}
	}

}
