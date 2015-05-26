package com.llkj.newbjia.shoppingcart;

import java.util.ArrayList;
import java.util.HashMap;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.llkj.newbjia.BaseFragment;
import com.llkj.newbjia.MainActivity;
import com.llkj.newbjia.MyApplication;
import com.llkj.newbjia.R;
import com.llkj.newbjia.adpater.MyCartAdapter;
import com.llkj.newbjia.adpater.MyCartAdapter.Myclick;
import com.llkj.newbjia.bean.ResponseBean;
import com.llkj.newbjia.bean.UserInfoBean;
import com.llkj.newbjia.http.PoCService;
import com.llkj.newbjia.main.GoodDetailTwoActivity;
import com.llkj.newbjia.utils.StringUtil;
import com.llkj.newbjia.utils.ToastUtil;

/**
 * 我的购物车页
 * 
 * @author John
 * 
 */
public class MyCartFragment extends BaseFragment implements Myclick,
		OnClickListener {
	private ArrayList arrayList;
	private MyCartAdapter adapter;
	private ListView My_carnTId;
	private LinearLayout ll_title_back;
	private Button btn_jiesuanID;
	private TextView tv_rental;

	private HashMap map, maptwo;
	private HashMap<String, String> temp_map;
	private int mShopCartList, mEditNumId, mShopCartDelId;
	private String uid, number;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.my_cart, null);
			initView();
			initData();
			initListener();
		} else {
			ViewGroup parent = (ViewGroup) rootView.getParent();
			if (parent != null) {
				parent.removeView(rootView);
			}
		}
		return rootView;
	}

	private void initView() {

		My_carnTId = (ListView) rootView.findViewById(R.id.My_carnTId);
		ll_title_back = (LinearLayout) rootView
				.findViewById(R.id.ll_title_back);
		btn_jiesuanID = (Button) rootView.findViewById(R.id.btn_jiesuanID);
		tv_rental = (TextView) rootView.findViewById(R.id.tv_rental);

	}

	private void initData() {
		uid = UserInfoBean.getUserInfo(getActivity()).getUid();
		if (StringUtil.isNetworkConnected(getActivity())) {
			if (null != uid) {
				mShopCartList = mRequestManager.getShopCartList(uid, true);
				isLoaded = true;
			}
		} else {
			ToastUtil.makeShortText(getActivity(), R.string.no_wangluo);
		}
		arrayList = new ArrayList();
		adapter = new MyCartAdapter(getActivity(), arrayList, this);
		My_carnTId.setAdapter(adapter);
	}

	public void reFreshData() {
		if (StringUtil.isNetworkConnected(getActivity())) {
			if (null != uid) {
				mShopCartList = mRequestManager.getShopCartList(uid, true);
			}
		} else {
			ToastUtil.makeShortText(getActivity(), R.string.no_wangluo);
		}
	}

	private void initListener() {
		ll_title_back.setOnClickListener(this);
		btn_jiesuanID.setOnClickListener(this);
	}

	@Override
	public void myClick(View v, int type) {

		switch (type) {
		case 1:
			temp_map = (HashMap) v.getTag();
			if (temp_map.containsKey(ResponseBean.RESPONSE_ID)) {
				String id = temp_map.get(ResponseBean.RESPONSE_ID) + "";
				if (!StringUtil.isEmpty(id)) {
					// TODO 这里写死了,不正确。
					if (temp_map
							.containsKey(ResponseBean.RESPONSE_GOODS_NUMBER)) {
						number = temp_map
								.get(ResponseBean.RESPONSE_GOODS_NUMBER);
						mEditNumId = mRequestManager.editNumber(id, uid,
								number, false);
					} else {
						mEditNumId = mRequestManager.editNumber(id, uid,
								1 + "", false);
					}

				}
			}
			break;
		case 2:
			maptwo = (HashMap) v.getTag();
			if (maptwo.containsKey(ResponseBean.RESPONSE_ID)) {
				String id = maptwo.get(ResponseBean.RESPONSE_ID) + "";
				if (!StringUtil.isEmpty(id)) {
					mShopCartDelId = mRequestManager
							.shopCartDel(id, uid, false);
				}
			}
			break;
		case 3:
			tv_rental.setText("￥" + adapter.getTotal() + "元");
			break;
		case 4:
			HashMap map = (HashMap) v.getTag();
			if (map.containsKey(ResponseBean.RESPONSE_GOODS_ID)) {
				String good_id = map.get(ResponseBean.RESPONSE_GOODS_ID) + "";
				Intent intent = new Intent(getActivity(),
						GoodDetailTwoActivity.class);
				intent.putExtra("id", good_id);
				startActivity(intent);
			}
			break;

		}
	}

	@Override
	public void myLongClick(View v, int type) {

	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.ll_title_back:
			if ((MainActivity) getActivity() != null) {
				((MainActivity) getActivity()).switchContent(0);
			}
			break;
		case R.id.btn_jiesuanID:
			if (adapter.getTotal() > 0) {
				intent = new Intent(getActivity(), WriteOrderActivity.class);
				intent.putParcelableArrayListExtra("arrayList",
						adapter.getArrayList());
				intent.putExtra("total", tv_rental.getText() + "");
				startActivityForResult(intent, 100);
			} else {
				ToastUtil.makeLongText(getActivity(), R.string.caozuoshibai);
			}

			break;

		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (MyApplication.isRefrsh)
			mShopCartList = mRequestManager.getShopCartList(uid, true);
		if (!isLoaded) {
			if (StringUtil.isNetworkConnected(getActivity())) {
				if (null != uid) {
					mShopCartList = mRequestManager.getShopCartList(uid, true);
					isLoaded = true;
				}
			} else {
				ToastUtil.makeShortText(getActivity(), R.string.no_wangluo);
			}

		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data != null) {
			switch (requestCode) {
			case 100:
				break;
			default:
				break;
			}
		}
	}

	@Override
	public void onRequestFinished(int requestId, int resultCode, Bundle payload) {
		super.onRequestFinished(requestId, resultCode, payload);
		
		if (resultCode == PoCService.SUCCESS_CODE) {
			if (mShopCartList == requestId) {
				int result = payload.getInt(ResponseBean.RESPONSE_STATE);
				if (result == 1) {
					arrayList.clear();
					ArrayList newList = payload
							.getParcelableArrayList(ResponseBean.RESPONSE_GOODS_LIST);
					String total_price = payload
							.getString(ResponseBean.RESPONSE_TOTAL_PRICE);
					tv_rental.setText(total_price);
					arrayList.addAll(newList);
					// adapter.setBooleans(newList);
					adapter.notifyDataSetChanged();
					MyApplication.isRefrsh = false;
					isLoaded = true;
				} else {
					isLoaded = false;
					String msg = payload
							.getString(ResponseBean.RESPONSE_MESSAGE);
					ToastUtil.makeShortText(getActivity(), msg);
				}
			}
			if (mEditNumId == requestId) {
				int result = payload.getInt(ResponseBean.RESPONSE_STATE);
				if (result == 1) {
					temp_map.put(ResponseBean.RESPONSE_GOODS_NUMBER, number);
					adapter.notifyDataSetChanged();
					tv_rental.setText("￥" + adapter.getTotal() + "元");
				} else {
					String msg = payload
							.getString(ResponseBean.RESPONSE_MESSAGE);
					ToastUtil.makeShortText(getActivity(), msg);
				}
			}
			if (mShopCartDelId == requestId) {
				int result = payload.getInt(ResponseBean.RESPONSE_STATE);
				if (result == 1) {
					int pos = arrayList.indexOf(maptwo);
					// adapter.removeBooleans(pos);
					arrayList.remove(maptwo);

					adapter.notifyDataSetChanged();
					tv_rental.setText("￥" + adapter.getTotal() + "元");
				} else {
					String msg = payload
							.getString(ResponseBean.RESPONSE_MESSAGE);
					ToastUtil.makeShortText(getActivity(), msg);
				}
			}
		}
	}
}
