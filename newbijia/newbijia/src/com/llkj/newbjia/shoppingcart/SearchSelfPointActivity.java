package com.llkj.newbjia.shoppingcart;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.llkj.newbjia.BaseActivity;
import com.llkj.newbjia.R;
import com.llkj.newbjia.adpater.SearchSelfPointAdapter;
import com.llkj.newbjia.bean.ResponseBean;
import com.llkj.newbjia.http.PoCService;
import com.llkj.newbjia.utils.StringUtil;
import com.llkj.newbjia.utils.ToastUtil;

/**
 * 搜索自提点页
 * 
 * @author John
 * 
 */
public class SearchSelfPointActivity extends BaseActivity implements
		OnClickListener {
	private ListView srcSelfPONNTid;
	private ArrayList arrayList;
	private SearchSelfPointAdapter adapter;
	private int mRequestId;
	private EditText et_content;
	private Button bt_search;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_self_point);
		setTitle(R.string.searchsefpoint, true, R.string.kong, true,
				R.string.kong);
		initView();
		initData();
		initListener();
	}

	private void initView() {
		srcSelfPONNTid = (ListView) findViewById(R.id.srcSelfPONNTid);
		et_content = (EditText) findViewById(R.id.et_content);
		bt_search = (Button) findViewById(R.id.bt_search);
	}

	private void initData() {

		arrayList = new ArrayList();
		adapter = new SearchSelfPointAdapter(SearchSelfPointActivity.this,
				arrayList);
		srcSelfPONNTid.setAdapter(adapter);
	}

	private void initListener() {
		bt_search.setOnClickListener(this);
		srcSelfPONNTid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if (arrayList.size() > 0) {
					HashMap map = (HashMap) arrayList.get(arg2);
					Intent data = new Intent();
					Bundle bdata = new Bundle();
					bdata.putSerializable("map", map);
					data.putExtras(bdata);
					setResult(RESULT_OK, data);
					finish();

				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_search:
			String content = et_content.getText() + "";
			if (StringUtil.isEmpty(content)) {
				ToastUtil.makeShortText(this, R.string.contentnotisnull);
				return;
			}
			if (StringUtil.isNetworkConnected(this)) {
				mRequestId = mRequestManager.zitiSearch(content, true);
			} else {
				ToastUtil.makeShortText(this, R.string.no_wangluo);
			}

			break;

		default:
			break;
		}
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
					arrayList.addAll(newList);
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
