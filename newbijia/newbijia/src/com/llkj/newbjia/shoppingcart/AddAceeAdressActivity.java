package com.llkj.newbjia.shoppingcart;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
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

public class AddAceeAdressActivity extends BaseActivity implements
		OnClickListener {
	private int mAddressAdd;
	private EditText et_AddRess_Name, et_AddRess_Phone;
	private Button addAfreBtonnoid;
	private String uid;
	private String zitidian, zitidianId;
	private TextView tv_AddRess_Ziti;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_address);
		setTitle(R.string.addaceegood, true, R.string.kong, true, R.string.kong);
		initView();
		initData();
		initListener();
	}

	private void initView() {
		et_AddRess_Name = (EditText) findViewById(R.id.et_AddRess_Name);
		et_AddRess_Phone = (EditText) findViewById(R.id.et_AddRess_Phone);
		tv_AddRess_Ziti = (TextView) findViewById(R.id.tv_AddRess_Ziti);
		addAfreBtonnoid = (Button) findViewById(R.id.addAfreBtonnoid);
	}

	private void initData() {
		uid = UserInfoBean.getUserInfo(this).getUid();
	}

	private void initListener() {
		addAfreBtonnoid.setOnClickListener(this);
		tv_AddRess_Ziti.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.addAfreBtonnoid:
			String name = et_AddRess_Name.getText().toString();
			if (StringUtil.isEmpty(name)) {
				ToastUtil.makeShortText(this, R.string.contentnotisnull);
				return;
			}
			String phone = et_AddRess_Phone.getText().toString();
			if (StringUtil.isEmpty(phone) || !StringUtil.isPhoneNumber(phone)) {
				ToastUtil.makeShortText(this, R.string.phoneerr);
				return;
			}

			if (StringUtil.isEmpty(zitidianId)) {
				ToastUtil.makeShortText(this, "请选择自提点");
				return;
			}
			if (StringUtil.isNetworkConnected(this)) {
				if (null != uid) {
					mAddressAdd = mRequestManager.getAddressAdd(uid, name,
							phone, zitidianId, true);
				}
			} else {
				ToastUtil.makeShortText(this, R.string.no_wangluo);
			}
			break;
		case R.id.tv_AddRess_Ziti:
			Intent intent = new Intent(this, SelfDesionActivity.class);
			startActivityForResult(intent, 200);
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (data != null) {
			switch (requestCode) {
			case 200:
				if (data.hasExtra(ResponseBean.RESPONSE_NAME)) {
					zitidian = data.getStringExtra(ResponseBean.RESPONSE_NAME);
					tv_AddRess_Ziti.setText(zitidian);
				}
				if (data.hasExtra(ResponseBean.RESPONSE_ID)) {
					zitidianId = data.getStringExtra(ResponseBean.RESPONSE_ID);

				}
				break;

			default:
				break;
			}
		}
	}

	@Override
	public void onRequestFinished(int requestId, int resultCode, Bundle payload) {
		super.onRequestFinished(requestId, resultCode, payload);
		if (resultCode == PoCService.SUCCESS_CODE) {
			if (mAddressAdd == requestId) {
				int result = payload.getInt(ResponseBean.RESPONSE_STATE);
				if (result == 1) {
					ToastUtil.makeShortText(this, R.string.PWDok);
					Intent data = new Intent();
					data.putExtra(Constants.REFRESH, true);
					setResult(RESULT_OK, data);
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
