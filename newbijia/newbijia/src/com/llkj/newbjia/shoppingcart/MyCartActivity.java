package com.llkj.newbjia.shoppingcart;

import java.util.ArrayList;
import java.util.HashMap;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.llkj.newbjia.BaseActivity;
import com.llkj.newbjia.R;
import com.llkj.newbjia.adpater.MyCartAdapter;
import com.llkj.newbjia.adpater.MyCartAdapter.Myclick;
import com.llkj.newbjia.bean.ResponseBean;
import com.llkj.newbjia.bean.UserInfoBean;
import com.llkj.newbjia.http.PoCService;
import com.llkj.newbjia.main.GoodDetailTwoActivity;
import com.llkj.newbjia.utils.StringUtil;
import com.llkj.newbjia.utils.ToastUtil;

/**
 * 我的购物车页
 * 
 * @author John
 * 
 */
public class MyCartActivity extends BaseActivity implements Myclick,
		OnClickListener {
	private ArrayList arrayList;
	private MyCartAdapter adapter;

	private ListView My_carnTId;
	private Button btn_jiesuanID;
	private TextView tv_rental, tv_number;

	private int mShopCartList, mEditNumId, mShopCartDelId;

	private String uid, number;
	private HashMap map, maptwo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_carttwo);
		setTitle(R.string.mycart, true, R.string.kong, false, R.string.kong);

		initView();
		initData();
		initListener();

	}

	private void initView() {
		My_carnTId = (ListView) findViewById(R.id.My_carnTId);
		btn_jiesuanID = (Button) findViewById(R.id.btn_jiesuanID);
		tv_rental = (TextView) findViewById(R.id.tv_rental);
		tv_number = (TextView) findViewById(R.id.tv_number);
	}

	private void initData() {
		uid = UserInfoBean.getUserInfo(this).getUid();

		if (StringUtil.isNetworkConnected(this)) {
			if (null != uid) {
				mShopCartList = mRequestManager.getShopCartList(uid, true);
			}
		} else {
			ToastUtil.makeShortText(this, R.string.no_wangluo);
		}

		arrayList = new ArrayList();
		adapter = new MyCartAdapter(this, arrayList, this);
		My_carnTId.setAdapter(adapter);
	}

	public void reFreshData() {
		if (StringUtil.isNetworkConnected(this)) {
			if (null != uid) {
				mShopCartList = mRequestManager.getShopCartList(uid, true);
			}
		} else {
			ToastUtil.makeShortText(this, R.string.no_wangluo);
		}
	}

	private void initListener() {
		btn_jiesuanID.setOnClickListener(this);
	}

	@Override
	public void myClick(View v, int type) {

		switch (type) {
		case 1:
			map = (HashMap) v.getTag();
			number = tv_number.getText() + "";
			if (map.containsKey(ResponseBean.RESPONSE_ID)) {
				String id = map.get(ResponseBean.RESPONSE_ID) + "";

				// map.put(ResponseBean.RESPONSE_GOODS_NUMBER, number);
				// adapter.notifyDataSetChanged();
				if (!StringUtil.isEmpty(id)) {
					mEditNumId = mRequestManager.editNumber(id, uid, 6+"",
							true);
				
				}ToastUtil.makeLongText(getApplicationContext(), "HASJHDAJ");
			}

			break;

		case 2:
			maptwo = (HashMap) v.getTag();
			if (maptwo.containsKey(ResponseBean.RESPONSE_ID)) {
				String id = maptwo.get(ResponseBean.RESPONSE_ID) + "";
				if (!StringUtil.isEmpty(id)) {
					mShopCartDelId = mRequestManager.shopCartDel(id, uid, true);
				}
			}
			break;
		case 3:
			tv_rental.setText("￥" + adapter.getTotal() + "元");
			break;

		}
	}

	@Override
	public void myLongClick(View v, int type) {

	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.btn_jiesuanID:
			if (adapter.getTotal() > 0) {
				intent = new Intent(this, WriteOrderActivity.class);
				intent.putParcelableArrayListExtra("arrayList",
						adapter.getArrayList());
				intent.putExtra("total", tv_rental.getText() + "");
				startActivityForResult(intent, 100);
			} else {
				ToastUtil.makeLongText(this, R.string.caozuoshibai);
			}

			break;

		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data != null) {
			switch (requestCode) {
			case 100:
				mShopCartList = mRequestManager.getShopCartList(uid, true);
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
			if (mShopCartList == requestId) {
				int result = payload.getInt(ResponseBean.RESPONSE_STATE);
				if (result == 1) {
					arrayList.clear();
					ArrayList newList = payload
							.getParcelableArrayList(ResponseBean.RESPONSE_GOODS_LIST);
					String total_price = payload
							.getString(ResponseBean.RESPONSE_TOTAL_PRICE);
					tv_rental.setText(total_price);
					arrayList.addAll(newList);

					// HashMap map=(HashMap) newList.get(0);
					// String
					// hehe=map.get(ResponseBean.RESPONSE_GOODS_WEIGHT).toString();
					// ToastUtil.makeLongText(getApplicationContext(), hehe);

					// adapter.setBooleans(newList);
					adapter.notifyDataSetChanged();
				} else {
					String msg = payload
							.getString(ResponseBean.RESPONSE_MESSAGE);
					ToastUtil.makeShortText(this, msg);
				}
			}
			if (mEditNumId == requestId) {
				int result = payload.getInt(ResponseBean.RESPONSE_STATE);
				if (result == 1) {
					map.put(ResponseBean.RESPONSE_GOODS_NUMBER, number);
					adapter.notifyDataSetChanged();
					tv_rental.setText("￥" + adapter.getTotal() + "元");
				} else {
					String msg = payload
							.getString(ResponseBean.RESPONSE_MESSAGE);
					ToastUtil.makeShortText(this, msg);
				}
			}
			if (mShopCartDelId == requestId) {
				int result = payload.getInt(ResponseBean.RESPONSE_STATE);
				if (result == 1) {
					int pos = arrayList.indexOf(maptwo);
					// adapter.removeBooleans(pos);
					arrayList.remove(maptwo);
					adapter.notifyDataSetChanged();
					tv_rental.setText("￥" + adapter.getTotal() + "元");
				} else {

					String msg = payload
							.getString(ResponseBean.RESPONSE_MESSAGE);
					ToastUtil.makeShortText(this, msg);
				}
			}
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent intent = new Intent(getApplicationContext(),
				GoodDetailTwoActivity.class);
		startActivity(intent);
	}

}
