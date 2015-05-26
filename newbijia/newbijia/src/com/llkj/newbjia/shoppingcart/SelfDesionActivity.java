package com.llkj.newbjia.shoppingcart;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.llkj.newbjia.BaseActivity;
import com.llkj.newbjia.MyApplication;
import com.llkj.newbjia.MyClicker;
import com.llkj.newbjia.R;
import com.llkj.newbjia.adpater.SelfDesionAdapter;
import com.llkj.newbjia.bean.ResponseBean;
import com.llkj.newbjia.http.PoCService;
import com.llkj.newbjia.utils.StringUtil;
import com.llkj.newbjia.utils.ToastUtil;

/**
 * 自提点页
 * 
 * @author John
 * 
 */
public class SelfDesionActivity extends BaseActivity implements
		OnClickListener, MyClicker {

	private ImageView self_back_imgId, self_tv_head_title_finish;
	TextView tv_Yes, tv_No;
	RelativeLayout bg_id;
	LinearLayout ll_Show;
	@SuppressWarnings("rawtypes")
	private ArrayList arrayList;
	private SelfDesionAdapter adapter;
	private ListView lv_show;
	private int mZitiList;
	private int mZitiDistribution;
	private String lng, lat;
	private String id, name;

	// protected MyApplication application;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.self_selef_desion_point);
		initView();
		initData();
		initListener();
	}

	private void initView() {
		self_back_imgId = (ImageView) findViewById(R.id.self_back_imgId);
		self_tv_head_title_finish = (ImageView) findViewById(R.id.self_tv_head_title_finish);
		tv_Yes = (TextView) findViewById(R.id.tv_zitiDistribution_Yes);
		tv_No = (TextView) findViewById(R.id.tv_zitiDistribution_No);
		ll_Show = (LinearLayout) findViewById(R.id.ll_Show);
		lv_show = (ListView) findViewById(R.id.lv_show);
	}

	private void initData() {
		lng = MyApplication.geoLng;
		lat = MyApplication.geoLat;

		if (StringUtil.isNetworkConnected(this)) {
			if (StringUtil.isEmpty(lng)) {
				lng = "116.22";
			}
			if (StringUtil.isEmpty(lat)) {
				lat = "39.11";
			}
			mZitiDistribution = mRequestManager
					.ZitiDistribution(lng, lat, true);
		} else {
			ToastUtil.makeShortText(this, R.string.no_wangluo);
		}
		adapter = new SelfDesionAdapter(SelfDesionActivity.this, arrayList,
				this);
		lv_show.setAdapter(adapter);
	}

	private void initListener() {
		self_back_imgId.setOnClickListener(this);
		self_tv_head_title_finish.setOnClickListener(this);
		tv_Yes.setOnClickListener(this);
		tv_No.setOnClickListener(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (data != null) {
			switch (requestCode) {
			case 200:
				Bundle bundle = data.getExtras();
				HashMap map = (HashMap) bundle.getSerializable("map");
				if (map.containsKey(ResponseBean.RESPONSE_ID)) {
					id = (String) map.get(ResponseBean.RESPONSE_ID);
				}
				if (map.containsKey(ResponseBean.RESPONSE_NAME)) {
					name = (String) map.get(ResponseBean.RESPONSE_NAME);
				}
				Intent dataa = new Intent();
				dataa.putExtra(ResponseBean.RESPONSE_ID, id);
				dataa.putExtra(ResponseBean.RESPONSE_NAME, name);
				setResult(200, dataa);
				this.finish();
				break;

			default:
				break;
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.self_back_imgId:
			this.finish();
			break;
		case R.id.tv_zitiDistribution_Yes:
			ll_Show.setBackgroundResource(R.drawable.t1);
			tv_Yes.setTextColor(0xffffffff);
			tv_No.setTextColor(0xff3ab485);
			if (StringUtil.isNetworkConnected(this)) {
				mZitiDistribution = mRequestManager.ZitiDistribution(lng, lat,
						true);
			} else {
				ToastUtil.makeShortText(this, R.string.no_wangluo);
			}
			break;
		case R.id.tv_zitiDistribution_No:
			ll_Show.setBackgroundResource(R.drawable.t2);
			tv_Yes.setTextColor(0xff3ab485);
			tv_No.setTextColor(0xffffffff);
			if (StringUtil.isNetworkConnected(this)) {
				mZitiList = mRequestManager.zitiList(true);
			} else {
				ToastUtil.makeShortText(this, R.string.no_wangluo);
			}
			break;
		case R.id.self_tv_head_title_finish:
			Intent intent = new Intent(this, SearchSelfPointActivity.class);
			startActivityForResult(intent, 200);
			break;
		}
	}

	@Override
	public void onRequestFinished(int requestId, int resultCode, Bundle payload) {
		super.onRequestFinished(requestId, resultCode, payload);
		if (resultCode == PoCService.SUCCESS_CODE) {
			if (mZitiList == requestId) {
				int result = payload.getInt(ResponseBean.RESPONSE_STATE);
				if (result == 1) {
					arrayList = payload
							.getParcelableArrayList(ResponseBean.RESPONSE_LIST);
					if (null != arrayList && arrayList.size() > 0) {
						adapter.notifyDataSetChanged(arrayList);
					} else {
						arrayList.clear();
						adapter.notifyDataSetChanged(arrayList);
						ToastUtil.makeShortText(this, R.string.no_data);
					}
				} else {
					String msg = payload
							.getString(ResponseBean.RESPONSE_MESSAGE);
					ToastUtil.makeShortText(this, msg);
				}
			}
			if (mZitiDistribution == requestId) {
				int result = payload.getInt(ResponseBean.RESPONSE_STATE);
				if (result == 1) {
					arrayList = payload
							.getParcelableArrayList(ResponseBean.RESPONSE_LIST);

					int i, j;
					for (i = 0; i < arrayList.size() - 1; i++) {
						for (j = arrayList.size() - 1; j > i; j--) {
							double n = Double.parseDouble(((HashMap) arrayList
									.get(j)).get("s").toString());
							double m = Double.parseDouble(((HashMap) arrayList
									.get(j - 1)).get("s").toString());
							if (n <= m) {
								Object temp1 = arrayList.get(j);
								arrayList.set(j, arrayList.get(j - 1));
								arrayList.set(j - 1, temp1);

							}
						}

					}

					if (null != arrayList && arrayList.size() > 0) {
						adapter.notifyDataSetChanged(arrayList);
					} else {
						arrayList.clear();
						adapter.notifyDataSetChanged(arrayList);
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
	public void myClick(View view, int type) {
		@SuppressWarnings("rawtypes")
		HashMap hashMap = (HashMap) view.getTag();
		switch (type) {
		case 1:
			if (hashMap.containsKey(ResponseBean.RESPONSE_ID)) {
				id = (String) hashMap.get(ResponseBean.RESPONSE_ID);
			}
			if (hashMap.containsKey(ResponseBean.RESPONSE_NAME)) {
				name = (String) hashMap.get(ResponseBean.RESPONSE_NAME);
			}
			Intent data = new Intent();
			data.putExtra(ResponseBean.RESPONSE_ID, id);
			data.putExtra(ResponseBean.RESPONSE_NAME, name);
			setResult(200, data);
			this.finish();
			break;

		default:
			break;
		}
	}

	@Override
	public void myLongClick(View view, int type) {
		// TODO Auto-generated method stub

	}
}
