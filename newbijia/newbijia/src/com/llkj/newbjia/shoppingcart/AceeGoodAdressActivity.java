package com.llkj.newbjia.shoppingcart;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.llkj.newbjia.BaseActivity;
import com.llkj.newbjia.MyClicker;
import com.llkj.newbjia.R;
import com.llkj.newbjia.adpater.AceeGoodAdressAdapter;
import com.llkj.newbjia.bean.ResponseBean;
import com.llkj.newbjia.bean.UserInfoBean;
import com.llkj.newbjia.http.PoCService;
import com.llkj.newbjia.utils.StringUtil;
import com.llkj.newbjia.utils.ToastUtil;

/**
 * 收货地址页页
 * 
 * @author John
 * 
 */
public class AceeGoodAdressActivity extends BaseActivity implements MyClicker,
		OnClickListener {

	private ListView lv_content;
	private ImageView tianjia_iv;
	private ArrayList arrayList;
	private AceeGoodAdressAdapter adapter;

	private int mShopCartAdd;
	private String uid;
	private String id;
	private String consignee;
	private String phone, ziti_name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acee_good_adress);
		setTitle(R.string.aaceegoodadress, true, R.string.kong, false,
				R.string.kong);
		initView();
		initData();
		initListener();
	}

	private void initView() {
		lv_content = (ListView) findViewById(R.id.lv_content);

		tianjia_iv = (ImageView) findViewById(R.id.tianjia_iv);

	}

	private void initData() {
		uid = UserInfoBean.getUserInfo(this).getUid();
		if (StringUtil.isNetworkConnected(this)) {
			if (null != uid) {
				mShopCartAdd = mRequestManager.getShopAddRessList(uid, true);
			}
		} else {
			ToastUtil.makeShortText(this, R.string.no_wangluo);
		}
		arrayList = new ArrayList();
		adapter = new AceeGoodAdressAdapter(AceeGoodAdressActivity.this,
				arrayList, this);
		lv_content.setAdapter(adapter);

	}

	private void initListener() {
		tianjia_iv.setOnClickListener(this);
	}

	@Override
	public void myClick(View v, int type) {
		switch (type) {
		case 1:
			HashMap map = (HashMap) v.getTag();
			if (map.containsKey(ResponseBean.RESPONSE_ID)) {
				id = (String) map.get(ResponseBean.RESPONSE_ID);
			}
			if (map.containsKey(ResponseBean.RESPONSE_CONSIGNEE)) {
				consignee = (String) map.get(ResponseBean.RESPONSE_CONSIGNEE);
			}
			if (map.containsKey(ResponseBean.RESPONSE_PHONE)) {
				phone = (String) map.get(ResponseBean.RESPONSE_PHONE);
			}
			if (map.containsKey(ResponseBean.RESPONSE_ZITI_NAME)) {
				ziti_name = (String) map.get(ResponseBean.RESPONSE_ZITI_NAME);
			}
			Intent intent = new Intent(this, EditAdressActivity.class);
			intent.putExtra(ResponseBean.RESPONSE_ID, id);
			intent.putExtra(ResponseBean.RESPONSE_CONSIGNEE, consignee);
			intent.putExtra(ResponseBean.RESPONSE_PHONE, phone);
			intent.putExtra(ResponseBean.RESPONSE_ZITI_NAME, ziti_name);
			startActivityForResult(intent, 200);
			break;
		case 2:
			HashMap mapp = (HashMap) v.getTag();
			Intent data = new Intent();
			Bundle bData = new Bundle();
			bData.putSerializable("map", mapp);
			data.putExtras(bData);
			setResult(RESULT_OK, data);
			this.finish();
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
				mShopCartAdd = mRequestManager.getShopAddRessList(uid, true);
				break;
			case 100:
				mShopCartAdd = mRequestManager.getShopAddRessList(uid, true);
				break;
			default:
				break;
			}

		}
	}

	@Override
	public void myLongClick(View v, int type) {

	}

	@Override
	public void onRequestFinished(int requestId, int resultCode, Bundle payload) {
		// TODO Auto-generated method stub
		super.onRequestFinished(requestId, resultCode, payload);
		if (resultCode == PoCService.SUCCESS_CODE) {
			if (mShopCartAdd == requestId) {
				int result = payload.getInt(ResponseBean.RESPONSE_STATE);
				if (result == 1) {
					arrayList.clear();
					ArrayList newList = payload
							.getParcelableArrayList(ResponseBean.RESPONSE_LIST);
					if (null != newList && newList.size() > 0) {
						arrayList.addAll(newList);
						adapter.notifyDataSetChanged();
					} else {
						ToastUtil.makeShortText(this, R.string.no_data);
					}

				} else {
					String msg = payload
							.getString(ResponseBean.RESPONSE_MESSAGE);
					ToastUtil.makeShortText(this, msg);
				}
			}
		}
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		// mShopCartAdd = mRequestManager.getShopAddRessList(uid, true);
		// adapter.notifyDataSetChanged();
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.tianjia_iv:
			Intent intent = new Intent(this, AddAceeAdressActivity.class);
			startActivityForResult(intent, 100);
			break;
		default:
			break;
		}

	}
}
