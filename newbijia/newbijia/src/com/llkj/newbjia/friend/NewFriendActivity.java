package com.llkj.newbjia.friend;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.provider.SyncStateContract.Constants;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.llkj.newbjia.BaseActivity;
import com.llkj.newbjia.MyClicker;
import com.llkj.newbjia.R;
import com.llkj.newbjia.adpater.NewFriendAdapter;
import com.llkj.newbjia.bean.ResponseBean;
import com.llkj.newbjia.bean.UserInfoBean;
import com.llkj.newbjia.http.PoCService;
import com.llkj.newbjia.utils.StringUtil;
import com.llkj.newbjia.utils.ToastUtil;

/**
 * 关于我们页
 * 
 * @author John
 * 
 */
public class NewFriendActivity extends BaseActivity implements OnClickListener,
		MyClicker {
	private ListView lv_content;
	private NewFriendAdapter adapter;
	private ArrayList arrayList;
	private int mRequestId, mPassVerificationId;
	private String uid, id;
	private HashMap map;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_newfriend);
		setTitle(R.string.newfriend, true, R.string.kong, false, R.string.kong);
		initView();
		initData();
		initListener();
		uid = UserInfoBean.getUserInfo(this).getUid();
		if (StringUtil.isNetworkConnected(this)) {
			if (!StringUtil.isEmpty(uid)) {
				mRequestId = mRequestManager.newFriend(uid, true);
			} else {
				ToastUtil.makeShortText(this, R.string.xiandenglu);
			}

		} else {
			ToastUtil.makeShortText(this, R.string.no_wangluo);
		}
	}

	private void initView() {
		lv_content = (ListView) findViewById(R.id.lv_content);

	}

	private void initData() {

	}

	private void initListener() {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_ToScore:

			break;

		}
	}

	@Override
	public void myClick(View view, int type) {
		// TODO Auto-generated method stub
		map = (HashMap) view.getTag();
		switch (type) {
		case 1:
			if (map.containsKey("uid")) {
				id = map.get("uid") + "";
			}
			if (!StringUtil.isEmpty(id)) {
				mPassVerificationId = mRequestManager.passVerification(uid, id,
						true);
			}
			break;
		case 2:
			arrayList.remove(map);
			adapter.notifyDataSetChanged(arrayList);
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
					arrayList = payload
							.getParcelableArrayList(ResponseBean.RESPONSE_LIST);
					adapter = new NewFriendAdapter(this, arrayList, this);
					lv_content.setAdapter(adapter);
				} else {
					String msg = payload
							.getString(ResponseBean.RESPONSE_MESSAGE);
					ToastUtil.makeShortText(this, msg);
				}

			}
			if (mPassVerificationId == requestId) {
				int result = payload.getInt(ResponseBean.RESPONSE_STATE);
				if (result == 1) {
					arrayList.remove(map);
					adapter.notifyDataSetChanged(arrayList);
					ToastUtil.makeShortText(this, "通过验证");
					Intent data = new Intent();
					data.putExtra("refresh", true);
					setResult(100, data);
				} else {
					String msg = payload
							.getString(ResponseBean.RESPONSE_MESSAGE);
					ToastUtil.makeShortText(this, msg);
				}

			}

		}
	}
}
