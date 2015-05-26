package com.llkj.newbjia.setting;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.llkj.newbjia.BaseActivity;
import com.llkj.newbjia.R;
import com.llkj.newbjia.bean.ResponseBean;
import com.llkj.newbjia.http.PoCService;
import com.llkj.newbjia.utils.StringUtil;
import com.llkj.newbjia.utils.ToastUtil;

public class HelpListActivity extends BaseActivity {
	private ListView lv_content;
	private ArrayList arrayList;
	private int mRequestId;
	private ArrayAdapter<String> adapter;
	private ArrayList<String> contents;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_helplist);
		setTitle(R.string.help_list, true, R.string.kong, false, R.string.kong);
		initView();
		initData();
		initListener();
		if (StringUtil.isNetworkConnected(this)) {
			mRequestId = mRequestManager.helpList(true);
		} else {
			ToastUtil.makeShortText(this, R.string.no_wangluo);
		}

	}

	private void initListener() {
		lv_content.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				HashMap map = (HashMap) arrayList.get(position);
				if (map.containsKey(ResponseBean.RESPONSE_ID)) {
					String idd = map.get(ResponseBean.RESPONSE_ID) + "";
					if (!StringUtil.isEmpty(idd)) {
						Intent intent = new Intent(HelpListActivity.this,
								HelpDetailActivity.class);
						intent.putExtra("id", idd);
						startActivity(intent);
					}
				}
			}
		});
	}

	private void initData() {
		contents = new ArrayList<String>();
	}

	private void initView() {
		lv_content = (ListView) findViewById(R.id.lv_content);
	}

	@Override
	public void onRequestFinished(int requestId, int resultCode, Bundle payload) {
		super.onRequestFinished(requestId, resultCode, payload);
		if (resultCode == PoCService.SUCCESS_CODE) {
			if (mRequestId == requestId) {
				int result = payload.getInt(ResponseBean.RESPONSE_STATE);
				if (result == 1) {
					arrayList = payload
							.getParcelableArrayList(ResponseBean.RESPONSE_LIST);
					if (arrayList != null && arrayList.size() > 0) {
						for (int i = 0; i < arrayList.size(); i++) {
							HashMap map = (HashMap) arrayList.get(i);
							if (map.containsKey(ResponseBean.RESPONSE_TITLE)) {
								contents.add(map
										.get(ResponseBean.RESPONSE_TITLE) + "");
							}
						}
					}
					adapter = new ArrayAdapter<String>(this,
							android.R.layout.simple_list_item_1, contents);
					lv_content.setAdapter(adapter);
				} else {
					String msg = payload
							.getString(ResponseBean.RESPONSE_MESSAGE);
					ToastUtil.makeShortText(this, msg);
				}

			}

		}
	}
}
