package com.llkj.newbjia.login;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.llkj.newbjia.BaseActivity;
import com.llkj.newbjia.R;
import com.llkj.newbjia.bean.ResponseBean;
import com.llkj.newbjia.http.PoCService;
import com.llkj.newbjia.utils.StringUtil;
import com.llkj.newbjia.utils.ToastUtil;

/**
 * 找回密码页
 * 
 * @author John
 * 
 */
public class FindPswActivity extends BaseActivity implements OnClickListener {
	private EditText et_phone, et_code, et_newpsd;
	private TextView tv_getcode;
	private TimeCount timeCount;
	private String phone, code, newPwd;
	private Button btn_finish;
	private int mRequestId;
	private int mForgetPassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_find_pwd);
		setTitle(R.string.findpwd, true, R.string.back, false,
				R.string.register);

		initView();
		initListener();
		initData();
	}

	/**
	 * 初始化数据
	 */
	private void initData() {

		timeCount = new TimeCount(30000, 1000);
	}

	/**
	 * 初始化View
	 */
	private void initView() {
		// TODO Auto-generated method stub
		et_phone = (EditText) findViewById(R.id.et_phone);
		et_code = (EditText) findViewById(R.id.et_code);
		et_newpsd = (EditText) findViewById(R.id.et_newpsd);

		tv_getcode = (TextView) findViewById(R.id.tv_getcode);

		btn_finish = (Button) findViewById(R.id.btn_finish);
	}

	/**
	 * 添加监听
	 */
	private void initListener() {
		// TODO Auto-generated method stub
		tv_getcode.setOnClickListener(this);
		btn_finish.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.tv_getcode:
			phone = et_phone.getText().toString();
			if (StringUtil.isEmpty(phone) || !StringUtil.isPhoneNumber(phone)) {
				ToastUtil.makeLongText(this, R.string.phoneerr);
				return;
			}
			if (StringUtil.isNetworkConnected(this)) {
				mRequestId = mRequestManager.getCode(phone, "2", true);
			}

			break;
		case R.id.btn_finish:
			code = et_code.getText().toString();
			phone = et_phone.getText().toString();
			newPwd = et_newpsd.getText().toString();
			if (StringUtil.isEmpty(phone) || !StringUtil.isPhoneNumber(phone)) {
				ToastUtil.makeLongText(this, R.string.phoneerr);
				return;
			}
			if (StringUtil.isEmpty(code)) {
				ToastUtil.makeLongText(this, "请输入验证码");
				return;
			}

			if (StringUtil.isEmpty(newPwd)) {
				ToastUtil.makeLongText(this, "请输入新密码");
				return;
			}
			if (newPwd.length() < 6) {
				ToastUtil.makeLongText(this, "密码不能小于6位");
				return;
			}
			if (newPwd.length() > 20) {
				ToastUtil.makeLongText(this, "密码长度不能大于20");
				return;
			}
			if (StringUtil.isNetworkConnected(this)) {
				mForgetPassword = mRequestManager.getForgetPassword(phone,
						code, newPwd, true);
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
			if (mRequestId == requestId) {
				int result = payload.getInt(ResponseBean.RESPONSE_STATE);
				if (result == 1) {
					String code = payload.getString(ResponseBean.RESPONSE_CODE);
					ToastUtil.makeShortText(this, "发送成功" + code);
					timeCount.start();
					// LogUtil.e(code);
				} else {
					String msg = payload
							.getString(ResponseBean.RESPONSE_MESSAGE);
					ToastUtil.makeShortText(this, msg);
				}

			} else if (mForgetPassword == requestId) {
				int result = payload.getInt(ResponseBean.RESPONSE_STATE);
				if (result == 1) {
					ToastUtil.makeShortText(this, "找回密码成功");
					this.finish();
				} else {
					String msg = payload
							.getString(ResponseBean.RESPONSE_MESSAGE);
					ToastUtil.makeShortText(this, msg);
				}
			}
		}
	}
}
