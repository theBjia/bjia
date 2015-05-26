package com.llkj.newbjia.quanzi;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.llkj.newbjia.BaseFragment;
import com.llkj.newbjia.MainActivity;
import com.llkj.newbjia.MyOnNewMessageListener;
import com.llkj.newbjia.R;
import com.llkj.newbjia.adpater.AddFriendAdapter;
import com.llkj.newbjia.adpater.FenLeiAdapter;
import com.llkj.newbjia.adpater.QuanzhitwoAdapter;
import com.llkj.newbjia.bean.ResponseBean;
import com.llkj.newbjia.bean.UserInfoBean;
import com.llkj.newbjia.friend.AddFriendActivity;
import com.llkj.newbjia.friend.AddFriendShequActivity;
import com.llkj.newbjia.http.PoCService;
import com.llkj.newbjia.main.GoodsActivity;
import com.llkj.newbjia.utils.StringUtil;
import com.llkj.newbjia.utils.ToastUtil;

/**
 * 
 * 
 * @author
 * 
 */
public class QuanZhiFragment extends BaseFragment implements OnClickListener {

	private ListView lv_content;
	private ArrayList arrayList;
	private QuanzhitwoAdapter adapter;
	private LinearLayout ll_title_back;
	private ImageView iv_quanzi_add;
	private int mCommunityListId;
	private String uid, id, type, name;
	// TODO init it and add the listener .
	private MyOnNewMessageListener onNewMessageListener;

	public MyOnNewMessageListener getOnNewMessageListener() {
		return onNewMessageListener;
	}

	public void setOnNewMessageListener(
			MyOnNewMessageListener onNewMessageListener) {
		this.onNewMessageListener = onNewMessageListener;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.layout_quanzhitwo, null);
			initView();
			initListener();
			intData();

		} else {
			ViewGroup parent = (ViewGroup) rootView.getParent();
			if (parent != null) {
				parent.removeView(rootView);
			}
		}
		return rootView;
	}

	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(!isLoaded){
			if (StringUtil.isNetworkConnected(getActivity())) {
				if (!StringUtil.isEmpty(uid)) {
					mCommunityListId = mRequestManager.communityList(uid, true);
				} else {
					ToastUtil.makeShortText(getActivity(), R.string.xiandenglu);
				}

			} else {
				ToastUtil.makeShortText(getActivity(), R.string.no_wangluo);
			}
			
		}
	}

	private void intData() {
		uid = UserInfoBean.getUserInfo(getActivity()).getUid();
		arrayList = new ArrayList();
		HashMap map = new HashMap();
		map.put("id", "0");
		map.put("name", "好友圈");
		map.put("type", "0");
		arrayList.add(map);

		if (StringUtil.isNetworkConnected(getActivity())) {
			if (!StringUtil.isEmpty(uid)) {
				mCommunityListId = mRequestManager.communityList(uid, true);
				isLoaded = true;
			} else {
				ToastUtil.makeShortText(getActivity(), R.string.xiandenglu);
			}

		} else {
			ToastUtil.makeShortText(getActivity(), R.string.no_wangluo);
		}

	}

	private void initListener() {
		ll_title_back.setOnClickListener(this);
		lv_content.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				HashMap map = (HashMap) arrayList.get(arg2);
				if (map.containsKey("type")) {
					type = map.get("type") + "";
					type = Integer.parseInt(type) + 1 + "";
				}
				if (map.containsKey("id")) {
					id = map.get("id") + "";
				}
				if (map.containsKey("name")) {
					name = map.get("name") + "";
				}
				if (!StringUtil.isEmpty(id)) {
					Intent intent = new Intent(getActivity(),
							QuanzhiActivity.class);
					intent.putExtra("id", id);
					intent.putExtra("type", type);
					intent.putExtra("name", name);
					startActivity(intent);
				}

			}
		});
	}

	private void initView() {
		lv_content = (ListView) rootView.findViewById(R.id.lv_content);
		ll_title_back = (LinearLayout) rootView
				.findViewById(R.id.ll_title_back);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_title_back:
			if ((MainActivity) getActivity() != null) {
				((MainActivity) getActivity()).switchContent(0);
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
			if (mCommunityListId == requestId) {
				int result = payload.getInt(ResponseBean.RESPONSE_STATE);
				if (result == 1) {
					ArrayList newList = payload
							.getParcelableArrayList(ResponseBean.RESPONSE_LIST);
					arrayList.addAll(newList);
					// TODO send some info outside. (message reminder .)
					if (newList != null || newList.size() != 0) {
						onNewMessageListener
								.onNewMessegeComing(MyOnNewMessageListener.NEW_QUANZI_MESSAGE);
					}
					adapter = new QuanzhitwoAdapter(getActivity(), arrayList);
					isLoaded = true;
					lv_content.setAdapter(adapter);
				} else {
					isLoaded = false;
					String msg = payload
							.getString(ResponseBean.RESPONSE_MESSAGE);
					ToastUtil.makeShortText(getActivity(), msg);
				}

			}

		}
	}

}
