package com.llkj.newbjia.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.llkj.db.DBHelper;
import com.llkj.newbjia.BaseActivity;
import com.llkj.newbjia.MainActivity;
import com.llkj.newbjia.MyApplication;
import com.llkj.newbjia.R;
import com.llkj.newbjia.bean.KeyBean;
import com.llkj.newbjia.bean.ResponseBean;
import com.llkj.newbjia.bean.UserInfoBean;
import com.llkj.newbjia.http.PoCService;
import com.llkj.newbjia.http.UrlConfig;
import com.llkj.newbjia.utils.FinalBitmapUtil;
import com.llkj.newbjia.utils.LogUtil;
import com.llkj.newbjia.utils.SharedPreferenceUtil;
import com.llkj.newbjia.utils.StringUtil;
import com.llkj.newbjia.utils.ToastUtil;

/**
 * 登录页
 * 
 * @author John
 * 
 */
public class LoginActivity extends BaseActivity implements OnClickListener {

	private SharedPreferenceUtil spUtil;
	// TODO
	private SharedPreferenceUtil spLoginSetting;
	private EditText et_login_username, et_login_password;
	private Button btn_login_finish, btn_regist;
	private TextView tv_login_forget_pwd;
	// TODO
	private CheckBox cb_remember_pwd, cb_auto_login;
	private int mLogin, mGetLogoId;
	private Intent bigIntent;
	private String username, phone, uid;
	private FinalBitmapUtil fb;
	private ImageView iv_header;
	// TODO
	private boolean isRemeberPwd = false, isAutoLogin = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_login);
		spUtil = SharedPreferenceUtil.init(this,
				SharedPreferenceUtil.ACCOUNT_INFO, Activity.MODE_APPEND);
		// TODO
		spLoginSetting = SharedPreferenceUtil.init(this,
				SharedPreferenceUtil.LOGIN_SETTING, Activity.MODE_PRIVATE);

		fb = FinalBitmapUtil.create(this);
		initView();
		initData();
		initListener();
	}

	protected void initView() {
		iv_header = (ImageView) findViewById(R.id.iv_header);
		// TODO
		cb_remember_pwd = (CheckBox) findViewById(R.id.cb_remember_pwd);
		cb_auto_login = (CheckBox) findViewById(R.id.cb_auto_login);

		btn_regist = (Button) findViewById(R.id.btn_regist);
		et_login_username = (EditText) findViewById(R.id.et_login_username);
		et_login_password = (EditText) findViewById(R.id.et_login_password);
		tv_login_forget_pwd = (TextView) findViewById(R.id.tv_login_forget_pwd);
		btn_login_finish = (Button) findViewById(R.id.btn_login_finish);
		et_login_username.setText(UserInfoBean.getUserInfo(this).getPhone());
		// et_login_username.setSelection(UserInfoBean.getUserInfo(this)
		// .getPhone().length()-1);
		if (StringUtil.isNetworkConnected(LoginActivity.this)) {
			if (!StringUtil.isEmpty(et_login_username.getText() + "")
					&& StringUtil.isPhoneNumber(et_login_username.getText()
							+ "")) {
				mGetLogoId = mRequestManager.getLogo(
						et_login_username.getText() + "", true);
			}
		} else {
			ToastUtil.makeShortText(LoginActivity.this, R.string.no_wangluo);
		}

		et_login_username.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				String phone = et_login_username.getText() + "";
				if (StringUtil.isNetworkConnected(LoginActivity.this)) {
					if (!StringUtil.isEmpty(phone)
							&& StringUtil.isPhoneNumber(phone)) {
						mGetLogoId = mRequestManager.getLogo(phone, true);
					}
				} else {
					ToastUtil.makeShortText(LoginActivity.this,
							R.string.no_wangluo);
				}

			}
		});
		String account = spUtil.getString("account");
		if (!TextUtils.isEmpty(account)) {

		}
		// 如想保存登录状态,取消注释即可
		// UserInfoBean.getUserInfo(this);
		// if (UserInfoBean.getIsLogin()) {
		// Intent in = new Intent(this, MainActivity.class);
		// startActivity(in);
		// finish();
		// }
	}

	private void initData() {

		bigIntent = getIntent();
		if (bigIntent.hasExtra(KeyBean.KEY_USERNAME)) {
			username = bigIntent.getStringExtra(KeyBean.KEY_USERNAME);
		}
		if (bigIntent.hasExtra(KeyBean.KEY_PHONE)) {
			phone = bigIntent.getStringExtra(KeyBean.KEY_PHONE);
		}
		if (bigIntent.hasExtra(KeyBean.KEY_UID)) {
			uid = bigIntent.getStringExtra(KeyBean.KEY_UID);
			LogUtil.e("彼佳" + uid);
		}
		//TODO
		initLoginSetting();
		MyApplication.isFromLogin = true;

	}

	protected void initListener() {
		btn_regist.setOnClickListener(this);
		btn_login_finish.setOnClickListener(this);
		tv_login_forget_pwd.setOnClickListener(this);
		// TODO
		cb_remember_pwd
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub
						
							isRemeberPwd = isChecked;
					}
				});
		cb_auto_login.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
					isAutoLogin = isChecked;
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_regist:
			// 跳转到注册界面RegisterOneActivity
			startActivity(new Intent(getApplicationContext(),
					RegisterOneActivity.class));
			break;
		case R.id.btn_login_finish:
			// 登录
			String account = et_login_username.getText().toString().trim();
			String pwd = et_login_password.getText().toString().trim();
			if (TextUtils.isEmpty(account)) {
				ToastUtil.makeShortText(this, "请输入登录帐号");
				return;
			}
			if (TextUtils.isEmpty(pwd)) {
				ToastUtil.makeShortText(this, "请输入密码");
				return;
			}
			if (pwd.length() < 6) {
				ToastUtil.makeShortText(this, "密码不能小于6位");
				return;
			}
			// TODO 判断是否需要进行保存 数据信息到sharepreferences中。
			spLoginSetting.put(SharedPreferenceUtil.LOGIN_IS_REMEBER_PWD,
					isRemeberPwd);
			spLoginSetting.put(SharedPreferenceUtil.LOGIN_IS_AUTO, isAutoLogin);
			spLoginSetting.put(SharedPreferenceUtil.LOGIN_USER_NAME, et_login_username.getText().toString());
			spLoginSetting.put(SharedPreferenceUtil.LOGIN_USER_PWD, et_login_password.getText().toString());

			// TODO 如果软键盘弹出,则隐藏软键盘.
			InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			if (imm.isActive()) {
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
			}
			if (StringUtil.isNetworkConnected(this)) {
				mLogin = mRequestManager.getLogin(account, pwd, true);
				// TODO

			} else {
				ToastUtil.makeShortText(this, R.string.no_wangluo);
			}

			break;
		case R.id.tv_login_forget_pwd:
			startActivity(new Intent(getApplicationContext(),
					FindPswActivity.class));
			break;
		default:
			break;
		}
	}

	@Override
	public void onRequestFinished(int requestId, int resultCode, Bundle payload) {
		super.onRequestFinished(requestId, resultCode, payload);
		if (resultCode == PoCService.SUCCESS_CODE) {
			if (mLogin == requestId) {
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
					DBHelper.getInstance(getApplicationContext());
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
			if (mGetLogoId == requestId) {
				int result = payload.getInt(ResponseBean.RESPONSE_STATE);
				if (result == 1) {
					String url = payload.getString(ResponseBean.RESPONSE_URL);
					if (!StringUtil.isEmpty(url)) {
						fb.displayForHeader(iv_header, UrlConfig.ROOT_URL_TWO
								+ url);
					}

				} else {
					String msg = payload
							.getString(ResponseBean.RESPONSE_MESSAGE);
					ToastUtil.makeShortText(this, msg);
				}

			}
		}
	}
/**
 * according to the setting ,set the user login info.
 */
	public void initLoginSetting() {
		isRemeberPwd = spLoginSetting
				.getBoolean(SharedPreferenceUtil.LOGIN_IS_REMEBER_PWD);
		isAutoLogin = spLoginSetting
				.getBoolean(SharedPreferenceUtil.LOGIN_IS_AUTO);
		cb_remember_pwd.setChecked(isRemeberPwd);
		// TODO fill the user info.
		if (isRemeberPwd) {
			//UserInfoBean user = UserInfoBean.getUserInfo(this);
			et_login_username.setText(spLoginSetting
					.getString(SharedPreferenceUtil.LOGIN_USER_NAME));
			et_login_password.setText(spLoginSetting
					.getString(SharedPreferenceUtil.LOGIN_USER_PWD));
		}
		
		cb_auto_login.setChecked(isAutoLogin);

	}
}
