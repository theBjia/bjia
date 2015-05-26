package com.llkj.newbjia.myorder;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.llkj.newbjia.BaseActivity;
import com.llkj.newbjia.MyClicker;
import com.llkj.newbjia.R;
import com.llkj.newbjia.adpater.PeiSongJiHuaAdapter;
import com.llkj.newbjia.bean.ResponseBean;
import com.llkj.newbjia.bean.UserInfoBean;
import com.llkj.newbjia.http.PoCService;
import com.llkj.newbjia.utils.StringUtil;
import com.llkj.newbjia.utils.ToastUtil;

public class PeiSongJiHuaActivity extends BaseActivity implements MyClicker {
	private ListView lv_content;
	private PeiSongJiHuaAdapter adapter;
	private ArrayList arraylist;
	private int mRequestId;
	private Intent bigIntent;
	private String uid;
	private HashMap map;
	public static boolean isPreshOrderCart;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_peisongjihua);
		setTitle(R.string.peisongjihua, true, R.string.kong, false,
				R.string.kong);
		initView();
		initData();
		initListener();
	}

	private void initListener() {

	}

	private void initData() {
		// TODO Auto-generated method stub
		uid = UserInfoBean.getUserInfo(this).getUid();
		arraylist = new ArrayList();
		bigIntent = getIntent();
		if (bigIntent.hasExtra("arrayList")) {
			ArrayList newList = bigIntent
					.getParcelableArrayListExtra("arrayList");
			arraylist.addAll(newList);
		}
		adapter = new PeiSongJiHuaAdapter(this, arraylist, this);
		lv_content.setAdapter(adapter);
	}

	private void initView() {
		// TODO Auto-generated method stub
		lv_content = (ListView) findViewById(R.id.lv_content);
	}

	@Override
	public void myClick(View view, int type) {
		// TODO Auto-generated method stub
		switch (type) {
		case 1:
			String order_id = "",
			distribution_id = "";
			map = (HashMap) view.getTag();
			if (map.containsKey("not_click")) {
				String not_click = map.get("not_click") + "";
				if ("1".equals(not_click)) {
					return;
				}
			} else {
				return;
			}
			if (map.containsKey("order_id")) {
				order_id = map.get("order_id") + "";
			}
			if (map.containsKey("id")) {
				distribution_id = map.get("id") + "";
			}
			if (StringUtil.isNetworkConnected(this)) {
				if (!StringUtil.isEmpty(uid)) {
					if (!StringUtil.isEmpty(order_id)
							&& !StringUtil.isEmpty(distribution_id)) {
						mRequestId = mRequestManager.verifyGood(uid, order_id,
								distribution_id, true);
					}
				} else {
					ToastUtil.makeLongText(this, R.string.xiandenglu);
				}

			}
			break;

		default:
			break;
		}
	}

	@Override
	public void myLongClick(View view, int type) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRequestFinished(int requestId, int resultCode, Bundle payload) {
		super.onRequestFinished(requestId, resultCode, payload);
		if (resultCode == PoCService.SUCCESS_CODE) {
			if (mRequestId == requestId) {
				int result = payload.getInt(ResponseBean.RESPONSE_STATE);
				if (result == 1) {
					isPreshOrderCart = true;
					map.put("status", "1");
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
