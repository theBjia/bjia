package com.llkj.newbjia.collection;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.llkj.newbjia.BaseFragment;
import com.llkj.newbjia.MainActivity;
import com.llkj.newbjia.MyClicker;
import com.llkj.newbjia.R;
import com.llkj.newbjia.adpater.FavoriteAdapter;
import com.llkj.newbjia.bean.ResponseBean;
import com.llkj.newbjia.bean.UserInfoBean;
import com.llkj.newbjia.http.PoCService;
import com.llkj.newbjia.main.GoodDetailActivity;
import com.llkj.newbjia.main.GoodDetailTwoActivity;
import com.llkj.newbjia.utils.StringUtil;
import com.llkj.newbjia.utils.ToastUtil;

/**
 * 收藏夹页
 * 
 * @author John
 * 
 */
public class FavoriteFragment extends BaseFragment implements OnClickListener,
		MyClicker {

	private ListView lv_History;
	private ArrayList arrayList;
	private FavoriteAdapter adapter;
	private LinearLayout ll_title_back;
	private int mCollectList;
	private String uid;
	private ImageView imageView;
	private RelativeLayout rl_no_data;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.favorite, null);
			setTitle(rootView, R.string.collectList, true, R.string.kong, true,
					R.string.kong);
			initView();
			initListener();
			initData();
		} else {
			ViewGroup parent = (ViewGroup) rootView.getParent();
			if (parent != null) {
				parent.removeView(rootView);
			}
		}
		return rootView;
	}

	
	private void initView() {
		lv_History = (ListView) rootView.findViewById(R.id.lv_History);
		ll_title_back = (LinearLayout) rootView
				.findViewById(R.id.ll_title_back);
		imageView = (ImageView) rootView.findViewById(R.id.imageView);
		rl_no_data = (RelativeLayout) rootView.findViewById(R.id.rl_no_data);
	}

	private void initData() {
		
		uid = UserInfoBean.getUserInfo(getActivity()).getUid();
		if (StringUtil.isNetworkConnected(getActivity())) {
			if (null != uid) {
				mCollectList = mRequestManager.getCollectList(uid, true);
				isLoaded = true;
			}
		}
		adapter = new FavoriteAdapter(getActivity(), arrayList, this);
		lv_History.setAdapter(adapter);
	}

	private void initListener() {
		ll_title_back.setOnClickListener(this);

	}

	@Override
	public void onResume() {
		super.onResume();
		// if (((MainActivity) getActivity()).isBijiaInto) {
		// imageView.setImageResource(R.drawable.back_img);
		// } else {
		// imageView.setImageResource(R.drawable.main_left);
		// }
		imageView.setImageResource(R.drawable.back_img);
		
		if(!isLoaded){
			//TODO
			if (StringUtil.isNetworkConnected(getActivity())) {
				if (null != uid) {
					mCollectList = mRequestManager.getCollectList(uid, true);
				}
			}
			
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_title_back:
			if ((MainActivity) getActivity() != null) {
				if (((MainActivity) getActivity()).isBijiaInto) {
					((MainActivity) getActivity()).switchContent(10);
				} else {
					((MainActivity) getActivity()).switchContent(0);
				}
			}
			// ((MainActivity) getActivity()).isBijiaInto = false;
			break;
		default:
			break;
		}
	}

	@Override
	public void myClick(View v, int type) {
		HashMap map;
		Intent intent;
		switch (type) {
		case 0:
			// delete the item of favorite.
			HashMap item = (HashMap) v.getTag(R.id.tag_favor_list_info);
			if (item.containsKey(ResponseBean.RESPONSE_GOODS_ID)) {
				mRequestManager.goodAttention(
						(String) item.get(ResponseBean.RESPONSE_GOODS_ID),
						UserInfoBean.getUserInfo(getActivity()).getUid(), "2",
						true);

			}
			// without the consideration of no net.
			int position = (Integer) v.getTag(R.id.tag_favor_position);
			arrayList.remove(position);
			// TODO
			if (arrayList == null || arrayList.size() == 0) {
				rl_no_data.setVisibility(View.VISIBLE);
			} else {
				rl_no_data.setVisibility(View.GONE);
			}
			adapter.notifyDataSetChanged(arrayList);
			break;
		case 1:
			map = (HashMap) v.getTag();
			if (map.containsKey("goods_id")) {
				String id = map.get("goods_id") + "";
				intent = new Intent(getActivity(), GoodDetailTwoActivity.class);
				intent.putExtra("id", id);
				startActivity(intent);
			}

			break;

		}
	}

	@Override
	public void myLongClick(View view, int type) {
	}

	@Override
	public void onRequestFinished(int requestId, int resultCode, Bundle payload) {
		super.onRequestFinished(requestId, resultCode, payload);
		if (resultCode == PoCService.SUCCESS_CODE) {
			if (mCollectList == requestId) {
				int result = payload.getInt(ResponseBean.RESPONSE_STATE);
				if (result == 1) {
					arrayList = payload
							.getParcelableArrayList(ResponseBean.RESPONSE_LIST);
					isLoaded = true ;
					if (null != arrayList && arrayList.size() > 0) {
						adapter.notifyDataSetChanged(arrayList);
						rl_no_data.setVisibility(View.GONE);
					} else {
						rl_no_data.setVisibility(View.VISIBLE);
						ToastUtil
								.makeShortText(getActivity(), R.string.no_data);
					}
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
