package com.llkj.newbjia.mybijia;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.llkj.newbjia.BaseActivity;
import com.llkj.newbjia.R;
import com.llkj.newbjia.adpater.CityAdapter;
import com.llkj.newbjia.bean.ResponseBean;
import com.llkj.newbjia.bean.UserInfoBean;
import com.llkj.newbjia.http.PoCService;
import com.llkj.newbjia.utils.StringUtil;
import com.llkj.newbjia.utils.ToastUtil;

/**
 * 城市圈
 * 
 * @author John
 * 
 */
public class CityActivity extends BaseActivity implements OnClickListener {
	private ListView lv_city;
	private CityAdapter adapter;
	private ArrayList arrayList;
	private int mRequestId;
	private String uid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.city_ring);
		setTitle(R.string.cityring, true, R.string.kong, false, R.string.kong);
		initView();
		initData();
		initListener();
		uid = UserInfoBean.getUserInfo(this).getUid();
		uid  = "35";
		if (StringUtil.isNetworkConnected(this)) {
			mRequestId = mRequestManager.appPersonCityList(uid, true);
		} else {
			ToastUtil.makeShortText(this, R.string.no_wangluo);
		}
	}

	private void initView() {
		lv_city = (ListView) findViewById(R.id.lv_city);
	}

	private void initData() {
		arrayList = new ArrayList();
		adapter = new CityAdapter(CityActivity.this, arrayList);
		lv_city.setAdapter(adapter);
	}

	private void initListener() {
		lv_city.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				HashMap map = (HashMap) arrayList.get(position);
				
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRequestFinished(int requestId, int resultCode, Bundle payload) {
		super.onRequestFinished(requestId, resultCode, payload);
		if (resultCode == PoCService.SUCCESS_CODE) {
			if (mRequestId == requestId) {
				int result = payload.getInt(ResponseBean.RESPONSE_STATE);
				if (result == 1) {
					arrayList.clear();
					ArrayList newList = payload
							.getParcelableArrayList(ResponseBean.RESPONSE_LIST);
					if (newList != null && newList.size() > 0) {
						arrayList.addAll(newList);
					}
					adapter.notifyDataSetChanged();

				} else {
					String msg = payload
							.getString(ResponseBean.RESPONSE_MESSAGE);
					ToastUtil.makeShortText(this, msg);
				}

			}

		}
	}

}
