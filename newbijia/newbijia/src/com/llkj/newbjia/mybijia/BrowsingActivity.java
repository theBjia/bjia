package com.llkj.newbjia.mybijia;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.llkj.newbjia.BaseActivity;
import com.llkj.newbjia.MyApplication;
import com.llkj.newbjia.MyClicker;
import com.llkj.newbjia.R;
import com.llkj.newbjia.adpater.BrowsingAdapter;
import com.llkj.newbjia.adpater.MainAdapter;
import com.llkj.newbjia.bean.ResponseBean;
import com.llkj.newbjia.bean.UserInfoBean;
import com.llkj.newbjia.http.PoCService;
import com.llkj.newbjia.main.GoodDetailActivity;
import com.llkj.newbjia.main.GoodDetailTwoActivity;
import com.llkj.newbjia.utils.ObjectUtils;
import com.llkj.newbjia.utils.StringUtil;
import com.llkj.newbjia.utils.ToastUtil;

/**
 * 浏览历史页
 * 
 * @author John
 * 
 */
public class BrowsingActivity extends BaseActivity implements MyClicker,
		OnClickListener {
	private BrowsingAdapter adapter;
	private ArrayList arrayList;
	private ListView listView;
	private String uid;
	private StringBuilder sb;
	private RelativeLayout rl_title_back;
	private TextView tv_right;
	private int mRequestId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.browsing_history);
		//setTitle(R.string.browsing, true, R.string.kong, true, R.string.clear);  // null pointer 错误 .
		initView();
		initData();
		initListener();
		uid = UserInfoBean.getUserInfo(this).getUid();

		String ids = ObjectUtils.readObject("goodids.out") + "";

		sb.append(ids);

		if (StringUtil.isNetworkConnected(this)) {

			if (sb.length() > 0) {
				mRequestId = mRequestManager.browseList(
						sb.substring(0, sb.length() - 1), true);
			}

		}

	}

	private void initView() {
		listView = (ListView) findViewById(R.id.lv_History);
		rl_title_back = (RelativeLayout) findViewById(R.id.rl_title_back);
		tv_right = (TextView) findViewById(R.id.tv_right);
	}

	private void initData() {
		sb = new StringBuilder("");
		arrayList = new ArrayList();
		adapter = new BrowsingAdapter(BrowsingActivity.this, arrayList, this);
		listView.setAdapter(adapter);
	}

	private void initListener() {
		rl_title_back.setOnClickListener(this);
		tv_right.setOnClickListener(this);
	}

	@Override
	public void myClick(View v, int type) {

		switch (type) {
		case 1:
			HashMap mapone = (HashMap) v.getTag();
			if (mapone.containsKey("id")) {
				String id = mapone.get("id") + "";
				Intent intent = new Intent(this, GoodDetailTwoActivity.class);
				intent.putExtra("id", id);
				startActivity(intent);
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
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rl_title_back:
			this.finish();
			break;
		case R.id.tv_right:
			if (!StringUtil.isEmpty(uid)) {
				ObjectUtils.fileSave(this, "", "goodids.out");
				arrayList.clear();
				adapter.notifyDataSetChanged();
			} else {
				ToastUtil.makeShortText(this, R.string.xiandenglu);
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
					ArrayList newlist = payload
							.getParcelableArrayList(ResponseBean.RESPONSE_LIST);
					if (newlist != null && newlist.size() > 0) {
						arrayList.addAll(newlist);
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
}
