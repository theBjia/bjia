package com.llkj.newbjia.shoppingcart;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
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
 * 编辑地址页
 * 
 * @author John
 * 
 */
public class EditAdressActivity extends BaseActivity implements OnClickListener {
	private EditText pnoheeId, aceeGoodsId;
	private TextView self_ponteId, tvRight;
	private int mAddressEdit;
	private String id;
	private String uid;
	private String consignee;
	private String Chphone;
	private String name;
	private String ziti_name, zitidian_id;
	private Intent bigIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_adress);
		setTitle(R.string.editadress, true, R.string.kong, true, R.string.over);
		initView();
		initData();
		initListener();

	}

	private void initView() {
		aceeGoodsId = (EditText) findViewById(R.id.aceeGoodsId);
		pnoheeId = (EditText) findViewById(R.id.pnoheeId);
		self_ponteId = (TextView) findViewById(R.id.self_ponteId);
		tvRight = (TextView) findViewById(R.id.tv_title_right);
	}

	private void initData() {
		bigIntent = getIntent();
		if (bigIntent.hasExtra(ResponseBean.RESPONSE_ZITI_NAME)) {
			ziti_name = bigIntent
					.getStringExtra(ResponseBean.RESPONSE_ZITI_NAME);
		}
		if (bigIntent.hasExtra(ResponseBean.RESPONSE_ID)) {
			id = bigIntent.getStringExtra(ResponseBean.RESPONSE_ID);
		}
		if (bigIntent.hasExtra(ResponseBean.RESPONSE_CONSIGNEE)) {
			consignee = bigIntent
					.getStringExtra(ResponseBean.RESPONSE_CONSIGNEE);
		}
		if (bigIntent.hasExtra(ResponseBean.RESPONSE_PHONE)) {
			Chphone = bigIntent.getStringExtra(ResponseBean.RESPONSE_PHONE);
		}

		uid = UserInfoBean.getUserInfo(this).getUid();

		aceeGoodsId.setText(consignee);
		pnoheeId.setText(Chphone);
		self_ponteId.setText(ziti_name);
	}

	private void initListener() {
		self_ponteId.setOnClickListener(this);
		tvRight.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.self_ponteId:
			Intent intent = new Intent(this, SelfDesionActivity.class);
			startActivityForResult(intent, 200);

			break;

		case R.id.tv_title_right:
			consignee = aceeGoodsId.getText() + "";
			Chphone = pnoheeId.getText() + "";
			if (TextUtils.isEmpty(consignee)) {
				ToastUtil.makeShortText(this, "收货人不能为空");
				return;
			}
			if (StringUtil.isEmpty(Chphone)
					|| !StringUtil.isPhoneNumberValid(Chphone)) {
				ToastUtil.makeShortText(this, "请输入正确手机号码");
				return;
			}
			// String consignee, String phone, String id,
			// String ziti, String uid, boolean isShow

			if (StringUtil.isEmpty(zitidian_id)) {
				mAddressEdit = mRequestManager.getAddRessEdit(consignee,
						Chphone, id, "", uid, true);
			} else {
				mAddressEdit = mRequestManager.getAddRessEdit(consignee,
						Chphone, id, zitidian_id, uid, true);
			}

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
					ziti_name = data.getStringExtra(ResponseBean.RESPONSE_NAME);
					self_ponteId.setText(ziti_name);
				}
				if (data.hasExtra(ResponseBean.RESPONSE_ID)) {
					zitidian_id = data.getStringExtra(ResponseBean.RESPONSE_ID);

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
			if (mAddressEdit == requestId) {
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
