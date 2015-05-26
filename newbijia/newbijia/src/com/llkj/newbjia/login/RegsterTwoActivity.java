package com.llkj.newbjia.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.llkj.newbjia.BaseActivity;
import com.llkj.newbjia.MainActivity;
import com.llkj.newbjia.MyApplication;
import com.llkj.newbjia.R;
import com.llkj.newbjia.bean.KeyBean;
import com.llkj.newbjia.bean.ResponseBean;
import com.llkj.newbjia.bean.UserInfoBean;
import com.llkj.newbjia.chat.Constants;
import com.llkj.newbjia.http.PoCService;
import com.llkj.newbjia.utils.LogUtil;
import com.llkj.newbjia.utils.StringUtil;
import com.llkj.newbjia.utils.ToastUtil;

public class RegsterTwoActivity extends BaseActivity implements OnClickListener {
	private EditText et_code, et_pwd, et_sure_pwd;
	private TextView tv_getcode;
	private TimeCount timeCount;
	private String phone, code, pwd, username;
	private Button btn_next;
	private int mRegister, mGetCodeId;
	private Intent bigIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_rigestertwo);
		setTitle(R.string.register, true, R.string.kong, false,
				R.string.register);

		initView();
		initListener();
		initData();

		if (StringUtil.isEmpty(phone) || !StringUtil.isPhoneNumber(phone)) {
			ToastUtil.makeLongText(this, R.string.phoneerr);
			return;
		}
		if (StringUtil.isNetworkConnected(this)) {
			mGetCodeId = mRequestManager.getCode(phone, "1", true);

		} else {
			ToastUtil.makeLongText(this, R.string.no_wangluo);
		}
	}

	private void initData() {
		// TODO Auto-generated method stub
		bigIntent = getIntent();
		if (bigIntent.hasExtra(KeyBean.KEY_USERNAME)) {
			username = bigIntent.getStringExtra(KeyBean.KEY_USERNAME);
		}
		if (bigIntent.hasExtra(KeyBean.KEY_PHONE)) {
			phone = bigIntent.getStringExtra(KeyBean.KEY_PHONE);
		}
		timeCount = new TimeCount(30000, 1000);

	}

	private void initView() {
		// TODO Auto-generated method stub
		et_pwd = (EditText) findViewById(R.id.et_pwd);
		et_code = (EditText) findViewById(R.id.et_code);
		et_sure_pwd = (EditText) findViewById(R.id.et_sure_pwd);

		tv_getcode = (TextView) findViewById(R.id.tv_getcode);

		btn_next = (Button) findViewById(R.id.btn_next);
	}

	private void initListener() {
		// TODO Auto-generated method stub
		tv_getcode.setOnClickListener(this);
		btn_next.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.tv_getcode:
			if (StringUtil.isNetworkConnected(this)) {
				mGetCodeId = mRequestManager.getCode(phone, "1", true);
			}
			break;
		case R.id.btn_next:
			code = et_code.getText().toString();
			pwd = et_pwd.getText().toString();
			String surePwd = et_sure_pwd.getText().toString();

			if (StringUtil.isEmpty(code)) {
				ToastUtil.makeLongText(this, "请输入验证码");
				return;
			}

			if (StringUtil.isEmpty(pwd)) {
				ToastUtil.makeLongText(this, "请输入密码");
				return;
			}
			if (pwd.length() < 6) {
				ToastUtil.makeLongText(this, "密码不能小于6位");
				return;
			}
			if (StringUtil.isEmpty(surePwd)) {
				ToastUtil.makeLongText(this, "请再次输入密码");
				return;
			}
			if (!pwd.equals(surePwd)) {
				ToastUtil.makeLongText(this, "两次输入密码不一致，请重新输入");
				return;
			}
			if (pwd.length() > 18) {
				ToastUtil.makeLongText(this, "密码不能过长，请重新输入");
				return;
			}
			if (StringUtil.isNetworkConnected(this)) {
				mRegister = mRequestManager.getRegister(phone, code, pwd,
						username, true);
			}
			break;
		default:
			break;
		}
	}

	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {
			tv_getcode.setText("重新发送");
			tv_getcode.setClickable(true);

		}

		@Override
		public void onTick(long millisUntilFinished) {
			tv_getcode.setClickable(false);
			tv_getcode.setText(millisUntilFinished / 1000 + "秒后");

		}

	}

	@Override
	public void onRequestFinished(int requestId, int resultCode, Bundle payload) {
		super.onRequestFinished(requestId, resultCode, payload);
		if (resultCode == PoCService.SUCCESS_CODE) {
			if (mRegister == requestId) {
				int result = payload.getInt(ResponseBean.RESPONSE_STATE);
				if (result == 1) {
					String uid = payload.getString(ResponseBean.RESPONSE_UID);
					String phone = payload
							.getString(ResponseBean.RESPONSE_PHONE);
					String user_name = payload
							.getString(ResponseBean.RESPONSE_USERNAME);
					String logo = payload.getString(ResponseBean.RESPONSE_LOGO);
					UserInfoBean.getUserInfo(this).setUid(uid);
					UserInfoBean.getUserInfo(this).setPhone(phone);
					UserInfoBean.getUserInfo(this).setLogo(logo);
					UserInfoBean.getUserInfo(this).setUserName(user_name);
					UserInfoBean.saveUserinfo(this);

					// 如果服务器已经连上就 把addme = true；然后向服务器发送addme 否则就重练服务器
					if (MyApplication.isConnect) {
						MyApplication.isAddMe = true;
						UserInfoBean.addMe(this);
					} else {
						MyApplication.reConnect(this);
					}
					Intent intent = new Intent(this, MainActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
							| Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
					this.finish();
				} else {
					String msg = payload
							.getString(ResponseBean.RESPONSE_MESSAGE);
					ToastUtil.makeShortText(this, msg);
				}

			}
			if (mGetCodeId == requestId) {
				int result = payload.getInt(ResponseBean.RESPONSE_STATE);
				if (result == 1) {
					String code = payload.getString(ResponseBean.RESPONSE_CODE);
					timeCount.start();
					ToastUtil.makeShortText(this, "已发送请等待");
				} else {
					String msg = payload
							.getString(ResponseBean.RESPONSE_MESSAGE);
					ToastUtil.makeShortText(this, msg);
				}

			}
		}
	}
}
