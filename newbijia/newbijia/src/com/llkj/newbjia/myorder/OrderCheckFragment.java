package com.llkj.newbjia.myorder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.llkj.newbjia.BaseFragment;
import com.llkj.newbjia.MainActivity;
import com.llkj.newbjia.MyApplication;
import com.llkj.newbjia.R;
import com.llkj.newbjia.adpater.MyOrderAdapter;
import com.llkj.newbjia.bean.ResponseBean;
import com.llkj.newbjia.bean.UserInfoBean;
import com.llkj.newbjia.chat.ChatPersonActivity;
import com.llkj.newbjia.chat.Constants;
import com.llkj.newbjia.customview.MyDialog;
import com.llkj.newbjia.http.PoCService;
import com.llkj.newbjia.main.YinLianPayActivity;
import com.llkj.newbjia.utils.LogUtil;
import com.llkj.newbjia.utils.StringUtil;
import com.llkj.newbjia.utils.ToastUtil;
import com.llkj.newbjia.utils.Tools;

/**
 * 订单查询页
 * 
 * @author John
 * 
 */
public class OrderCheckFragment extends BaseFragment implements
		com.llkj.newbjia.MyClicker, OnClickListener {
	private ListView lv_Order;
	private ArrayList arrayList;
	private MyOrderAdapter adapter;
	private LinearLayout ll_title_back;
	private int mmyOrderId, mVerifyGoodId, mCancelOrderId;
	private String uid;
	private HashMap map, mapthree;// 确认收货的map;
	private Button bt_constantkefu, bt_sure_shouhuo;
	private TextView tv_close;
	private MyDialog dialog;
	private ImageView imageView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.order_check, null);
			initView();
			initListener();
			//initData();
		} else {
			ViewGroup parent = (ViewGroup) rootView.getParent();
			if (parent != null) {
				parent.removeView(rootView);
			}
		}
		return rootView;
	}

	private void initView() {
		lv_Order = (ListView) rootView.findViewById(R.id.lv_Order);
		ll_title_back = (LinearLayout) rootView
				.findViewById(R.id.ll_title_back);
		imageView = (ImageView) rootView.findViewById(R.id.imageView);
		uid = UserInfoBean.getUserInfo(getActivity()).getUid();
		arrayList = new ArrayList();
		adapter = new MyOrderAdapter(getActivity(), arrayList, this);
		lv_Order.setAdapter(adapter);
	}

	private void initData() {
		
		if (StringUtil.isNetworkConnected(getActivity())) {

			if (!StringUtil.isEmpty(uid)) {
				mmyOrderId = mRequestManager.myOrder(uid, true);
				isLoaded = true;
			} else {
				ToastUtil.makeShortText(getActivity(), R.string.xiandenglu);
			}

		} else {
			ToastUtil.makeShortText(getActivity(), R.string.no_wangluo);
		}
	
	}

	private void initListener() {
		ll_title_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if ((MainActivity) getActivity() != null) {
					if (((MainActivity) getActivity()).isBijiaInto) {
						((MainActivity) getActivity()).switchContent(10);
					} else {
						((MainActivity) getActivity()).switchContent(0);
					}
				}
				((MainActivity) getActivity()).isBijiaInto = false;
			}
		});
	}

	public MyDialog getDialog(HashMap map) {
		MyDialog mydialog = new MyDialog(getActivity(), 240, 150,
				R.layout.layout_sure_shouhuo, R.style.Theme_dialog);
		bt_constantkefu = (Button) mydialog.findViewById(R.id.bt_constantkefu);
		bt_sure_shouhuo = (Button) mydialog.findViewById(R.id.bt_sure_shouhuo);
		tv_close = (TextView) mydialog.findViewById(R.id.tv_close);
		bt_constantkefu.setOnClickListener(this);
		bt_sure_shouhuo.setOnClickListener(this);
		tv_close.setOnClickListener(this);
		bt_sure_shouhuo.setTag(map);
		return mydialog;
	}

	@Override
	public void myClick(View v, int type) {
		switch (type) {
		case 1:
			String order_sn = "",
			money = "";
			HashMap mapone = (HashMap) v.getTag();
			if (mapone.containsKey("order_sn")) {
				order_sn = mapone.get("order_sn") + "";

			}
			if (mapone.containsKey("goods_amount")) {
				money = mapone.get("goods_amount") + "";

			}
			if (!StringUtil.isEmpty(order_sn) && !StringUtil.isEmpty(money)) {
				Intent intent = new Intent(getActivity(),
						YinLianPayActivity.class);
				intent.putExtra("order_sn", order_sn);
				intent.putExtra("money", money);
				startActivityForResult(intent, 100);
			}
			break;
			
			//TODO confirm getting the goods.
		case 2:
			HashMap maptwo = (HashMap) v.getTag();
			dialog = getDialog(maptwo);
			dialog.show();
			break;
		case 3:
			Intent intent = new Intent(getActivity(), ChatPersonActivity.class);
			intent.putExtra(Constants.TARGETID, MyApplication.esq_id);
			intent.putExtra(Constants.TAGETNAME, MyApplication.esq_name);
			intent.putExtra("isKefu", true);
			startActivity(intent);
			break;
		case 4:
			String status = "";
			HashMap mapfour = (HashMap) v.getTag();
			if (mapfour.containsKey("status")) {
				status = mapfour.get("status") + "";
			}
			if (mapfour.containsKey("order_id")) {
				String order_id = mapfour.get("order_id") + "";
				Intent intentt = new Intent(getActivity(),
						OrderDetailsActivity.class);
				intentt.putExtra("order_id", order_id);
				intentt.putExtra("status", status);
				startActivity(intentt);
			}

			break;
		case 5:
			mapthree = (HashMap) v.getTag();
			if (mapthree.containsKey("order_sn")) {
				String order_snn = mapthree.get("order_sn") + "";
				if (!StringUtil.isEmpty(order_snn)) {
					mCancelOrderId = mRequestManager.cancelOrder(uid,
							order_snn, true);
				}
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void myLongClick(View v, int type) {

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data != null) {

		}
	}

	@Override
	public void onRequestFinished(int requestId, int resultCode, Bundle payload) {
		super.onRequestFinished(requestId, resultCode, payload);
		if (resultCode == PoCService.SUCCESS_CODE) {

			if (mmyOrderId == requestId) {
				int result = payload.getInt(ResponseBean.RESPONSE_STATE);
				if (result == 1) {
					arrayList.clear();
					ArrayList newList = payload
							.getParcelableArrayList(ResponseBean.RESPONSE_LIST);
					isLoaded = true;
					if (newList != null && newList.size() > 0) {
						arrayList.addAll(newList);
						adapter.notifyDataSetChanged();
					} else {
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
			if (mCancelOrderId == requestId) {
				int result = payload.getInt(ResponseBean.RESPONSE_STATE);
				if (result == 1) {
					mmyOrderId = mRequestManager.myOrder(uid, true);
					ToastUtil.makeShortText(getActivity(),
							R.string.caozuochenggong);
				} else {
					String msg = payload
							.getString(ResponseBean.RESPONSE_MESSAGE);
					ToastUtil.makeShortText(getActivity(), msg);
				}

			}
			//TODO
			if(mVerifyGoodId == requestId){
				int result = payload.getInt(ResponseBean.RESPONSE_STATE);
				if (result == 1) {
					ToastUtil.makeShortText(getActivity(),
							R.string.caozuochenggong);
				} else {
					String msg = payload
							.getString(ResponseBean.RESPONSE_MESSAGE);
					ToastUtil.makeShortText(getActivity(), msg);
				}
			}
		}
	}

	@Override
	public void onResume() {
		super.onResume();
//		if (!isLoaded) {
//			if (StringUtil.isNetworkConnected(getActivity())) {
//
//				if (!StringUtil.isEmpty(uid)) {
//					mmyOrderId = mRequestManager.myOrder(uid, true);
//					isLoaded = true;
//				} else {
//					ToastUtil.makeShortText(getActivity(), R.string.xiandenglu);
//				}
//
//			} else {
//				ToastUtil.makeShortText(getActivity(), R.string.no_wangluo);
//			}
//		}
		//TODO
		initData();
		
		
		if (PeiSongJiHuaActivity.isPreshOrderCart) {
			mmyOrderId = mRequestManager.myOrder(uid, true);
			PeiSongJiHuaActivity.isPreshOrderCart = false;
		}
		if (((MainActivity) getActivity()).isBijiaInto) {
			imageView.setImageResource(R.drawable.back_img);
		} else {
			imageView.setImageResource(R.drawable.back_img);
		}

		LogUtil.e("onResume---true");
		
	}
	@Override
	public void onPause() {
		super.onPause();
		System.out.println("onPause");
	}
	
	@Override
	public void onStart() {
		super.onStart();
		System.out.println("onStart");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_constantkefu:
			Intent intent = new Intent(getActivity(), ChatPersonActivity.class);
			intent.putExtra(Constants.TARGETID, MyApplication.esq_id);
			intent.putExtra(Constants.TAGETNAME, MyApplication.esq_name);
			intent.putExtra("isKefu", true);
			startActivity(intent);
			break;
		case R.id.bt_sure_shouhuo:
			map = (HashMap) v.getTag();
			//TODO confirm getting the goods.
			
			verifyGood(map);
			Tools.dissDialog(dialog);
			break;
		case R.id.tv_close:
			Tools.dissDialog(dialog);
			break;

		default:
			break;
		}
	}
	
	/**
	 *  To confirm that the user have already taken the goods.
	 */
	public void verifyGood(Map map){
		
		String order_id = "",
				distribution_id = "";
				if (map.containsKey("not_click")) {
					String not_click = map.get("not_click") + "";
					if ("1".equals(not_click)) {
						return;
					}
				} 
				if (map.containsKey("order_id")) {
					order_id = map.get("order_id") + "";
				}
//				if (map.containsKey("id")) {
//					distribution_id = map.get("id") + "";
//				}
				if (map.containsKey("order_sn")) {
					distribution_id = map.get("order_sn") + "";
				}
				if (StringUtil.isNetworkConnected(getActivity())) {
					if (!StringUtil.isEmpty(uid)) {
						if (!StringUtil.isEmpty(order_id)
								&& !StringUtil.isEmpty(distribution_id)) {
							//TODO have a problem.
//							mVerifyGoodId = mRequestManager.verifyGood(uid, order_id,
//									distribution_id, true);
							mVerifyGoodId = mRequestManager.verifyGood(uid, order_id,
									distribution_id, true);
						}
					} else {
						ToastUtil.makeLongText(getActivity(), R.string.xiandenglu);
					}

				}
	}

}
