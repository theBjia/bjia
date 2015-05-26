package com.llkj.newbjia.mybijia;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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
 * 修改个性签名页
 * 
 * @author John
 * 
 */
public class UpdateSignActivity extends BaseActivity implements OnClickListener {
	private EditText et_sign_update;
	private TextView tvRight;
	private String signEdit;
	private String uid;
	private int mSignEdit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update_sign);
		setTitle(R.string.updatesign, true, R.string.kong, true, R.string.save);
		initView();
		initData();
		initListener();
	}

	private void initView() {
		et_sign_update = (EditText) findViewById(R.id.et_sign_update);
		tvRight = (TextView) findViewById(R.id.tv_title_right);
	}

	private void initData() {
		et_sign_update.setText(UserInfoBean.getUserInfo(this).getSignature());
	}

	private void initListener() {
		tvRight.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_title_right:
			uid = UserInfoBean.getUserInfo(UpdateSignActivity.this).getUid();
			signEdit = et_sign_update.getText().toString();
			if (StringUtil.isEmpty(signEdit)) {
				ToastUtil.makeLongText(this, R.string.contentnotisnull);
				return;
			}

			if (StringUtil.isNetworkConnected(this))
				mSignEdit = mRequestManager.getSignEdit(uid, signEdit, true);
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
			if (mSignEdit == requestId) {
				int result = payload.getInt(ResponseBean.RESPONSE_STATE);
				if (result == 1) {
					UserInfoBean.getUserInfo(this).setSignature(signEdit);
					UserInfoBean.saveUserinfo(this);
					Intent data = new Intent();
					data.putExtra(Constants.REFRESH, true);
					setResult(RESULT_OK, data);
					ToastUtil.makeShortText(UpdateSignActivity.this,
							R.string.caozuochenggong);
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(
							et_sign_update.getWindowToken(), 0);
					this.finish();
				} else {
					String msg = payload
							.getString(ResponseBean.RESPONSE_MESSAGE);
					ToastUtil.makeShortText(UpdateSignActivity.this, msg);
				}

			}
		}
	}
}
