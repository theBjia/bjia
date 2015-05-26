package com.llkj.newbjia.friend;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.llkj.newbjia.BaseActivity;
import com.llkj.newbjia.MyClicker;
import com.llkj.newbjia.R;
import com.llkj.newbjia.adpater.AddFriendAdapter;
import com.llkj.newbjia.adpater.AddFriendShequAdpter;
import com.llkj.newbjia.bean.KeyBean;
import com.llkj.newbjia.bean.ResponseBean;
import com.llkj.newbjia.bean.UserInfoBean;
import com.llkj.newbjia.customview.MyListView;
import com.llkj.newbjia.http.PoCService;
import com.llkj.newbjia.utils.StringUtil;
import com.llkj.newbjia.utils.ToastUtil;

/**
 * 关于我们页
 * 
 * @author John
 * 
 */
public class AddFriendShequActivity extends BaseActivity implements
		OnClickListener, MyClicker {

	private AddFriendShequAdpter adapterone, adaptertwo;
	private ArrayList isFrendList, noFriendList;
	private MyListView mlv_no_add, mlv_add;
	private LinearLayout ll_title_back;
	private TextView tv_titile;
	private ImageView iv_title_search;
	private Intent bigIntent;
	private String xid, type, name, uid;
	private int mRequestId, mAddFriendId;

	private HashMap map;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_sheququan);

		initView();
		initData();
		initListener();

		uid = UserInfoBean.getUserInfo(this).getUid();

		if (StringUtil.isNetworkConnected(this)) {
			if (!StringUtil.isEmpty(uid) && !StringUtil.isEmpty(xid)) {
				mRequestId = mRequestManager.communityFriend(uid, xid, true);
			} else {
				ToastUtil.makeShortText(this, R.string.xiandenglu);
			}

		} else {
			ToastUtil.makeShortText(this, R.string.no_wangluo);
		}

	}

	private void initView() {
		mlv_no_add = (MyListView) findViewById(R.id.mlv_no_add);

		mlv_add = (MyListView) findViewById(R.id.mlv_add);
		ll_title_back = (LinearLayout) findViewById(R.id.ll_title_back);
		//TODO different condition ,different show.
		tv_titile = (TextView) findViewById(R.id.tv_titile);
		iv_title_search = (ImageView) findViewById(R.id.iv_title_search);

	}

	private void initData() {
		bigIntent = getIntent();
		if (bigIntent.hasExtra("xid")) {
			xid = bigIntent.getStringExtra("xid");

		}
		if (bigIntent.hasExtra("type")) {
			type = bigIntent.getStringExtra("type");
		}
		if (bigIntent.hasExtra("name")) {
			name = bigIntent.getStringExtra("name");
		}
		if ("1".equals(type)) {
			tv_titile.setText(name + "城市圈好友");
		} else if ("2".equals(type)) {
			tv_titile.setText(name + "社区圈好友");

		}

		isFrendList = new ArrayList();
		noFriendList = new ArrayList();

	}

	private void initListener() {
		ll_title_back.setOnClickListener(this);
		iv_title_search.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_title_back:
			finish();
			break;
		case R.id.iv_title_search:
			//TODO add the listner. 
			break;

		}
	}

	@Override
	public void myClick(View view, int type) {
		// TODO Auto-generated method stub
		map = (HashMap) view.getTag();
		switch (type) {
		case 0:
			if (map.containsKey("user_id")) {
				String user_id = map.get("user_id") + "";
				if (!StringUtil.isEmpty(user_id)) {
					mAddFriendId = mRequestManager
							.friendAdd(uid, user_id, true);
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
					isFrendList = payload.getParcelableArrayList("data_yes");
					noFriendList = payload.getParcelableArrayList("data_no");
					adapterone = new AddFriendShequAdpter(this, noFriendList,
							0, this);
					adaptertwo = new AddFriendShequAdpter(this, isFrendList, 1,
							this);
					mlv_no_add.setAdapter(adapterone);
					mlv_add.setAdapter(adaptertwo);
				} else {
					String msg = payload
							.getString(ResponseBean.RESPONSE_MESSAGE);
					ToastUtil.makeShortText(this, msg);
				}

			}
			if (mAddFriendId == requestId) {
				int result = payload.getInt(ResponseBean.RESPONSE_STATE);
				if (result == 1) {
					ToastUtil.makeShortText(this, "已发送请等待");
				} else {
					String msg = payload
							.getString(ResponseBean.RESPONSE_MESSAGE);
					ToastUtil.makeShortText(this, msg);
				}

			}

		}
	}

}
