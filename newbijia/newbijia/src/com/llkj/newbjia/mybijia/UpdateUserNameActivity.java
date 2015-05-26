package com.llkj.newbjia.mybijia;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.llkj.newbjia.BaseActivity;
import com.llkj.newbjia.R;
import com.llkj.newbjia.bean.ResponseBean;
import com.llkj.newbjia.bean.UserInfoBean;
import com.llkj.newbjia.chat.Constants;
import com.llkj.newbjia.http.PoCService;
import com.llkj.newbjia.utils.StringUtil;
import com.llkj.newbjia.utils.ToastUtil;

/**
 * 修改用户名
 * 
 * @author John
 * 
 */
public class UpdateUserNameActivity extends BaseActivity implements
		OnClickListener {
	EditText et_user_name;
	private String name;
	private String uid;
	private int mNameEdit;
	private TextView tvRight;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update_name);
		setTitle(R.string.updateusername, true, R.string.kong, true,
				R.string.save);
		initView();
		initDate();
		initListener();
	}

	private void initView() {
		et_user_name = (EditText) findViewById(R.id.et_user_name);
		tvRight = (TextView) findViewById(R.id.tv_title_right);
	}

	private void initDate() {
		et_user_name.setText(UserInfoBean.getUserInfo(this).getUserName());
	}

	private void initListener() {
		tvRight.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_title_right:
			uid = UserInfoBean.getUserInfo(UpdateUserNameActivity.this)
					.getUid();
			name = et_user_name.getText().toString();
			if (StringUtil.isEmpty(name)) {
				ToastUtil.makeLongText(this, R.string.contentnotisnull);
				return;
			}
			if (StringUtil.isNetworkConnected(this))
				mNameEdit = mRequestManager.getNameEdit(uid, name, true);
			else {
				ToastUtil.makeLongText(this, R.string.no_wangluo);
			}
			break;
		}
	}

	@Override
	public void onRequestFinished(int requestId, int resultCode, Bundle payload) {
		super.onRequestFinished(requestId, resultCode, payload);
		if (resultCode == PoCService.SUCCESS_CODE) {
			if (mNameEdit == requestId) {
				int result = payload.getInt(ResponseBean.RESPONSE_STATE);
				if (result == 1) {
					UserInfoBean.getUserInfo(this).setUserName(name);
					UserInfoBean.saveUserinfo(this);
					Intent br = new Intent(Constants.REFRESH_TOUXIANG);
					sendBroadcast(br);
					Intent data = new Intent();
					data.putExtra(Constants.REFRESH, true);
					setResult(RESULT_OK, data);
					ToastUtil.makeShortText(UpdateUserNameActivity.this,
							R.string.caozuochenggong);
					StringUtil.hideSoft(this);
					this.finish();
				} else {
					String msg = payload
							.getString(ResponseBean.RESPONSE_MESSAGE);
					ToastUtil.makeShortText(UpdateUserNameActivity.this, msg);
				}

			}
		}
	}
}
