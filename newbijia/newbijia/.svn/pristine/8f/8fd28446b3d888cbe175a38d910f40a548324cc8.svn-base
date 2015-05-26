package com.llkj.newbjia.mybijia;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.llkj.newbjia.BaseActivity;
import com.llkj.newbjia.R;
import com.llkj.newbjia.R.color;
import com.llkj.newbjia.adpater.CouponAdapter;
import com.llkj.newbjia.bean.ResponseBean;
import com.llkj.newbjia.bean.UserInfoBean;
import com.llkj.newbjia.http.PoCService;
import com.llkj.newbjia.utils.StringUtil;
import com.llkj.newbjia.utils.ToastUtil;
/**
 * 优惠券页
 * @author John
 *
 */
public class CouponActivity extends BaseActivity implements OnClickListener{
	private LinearLayout ll_Show;
	private TextView tv_Yes,tv_No;
	private CouponAdapter adapter;
	private ListView lv_Coupon;
	private ArrayList arrayList;
	private int mMyPrivilege;
	private String uid;
	private String RESPONSE_LIST = "use_list";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.coupon);
		setTitle(R.string.coupon,true,R.string.kong,true,R.string.kong);
		initView();
		initData();
		initListener();
	}

	private void initView() {
		ll_Show = (LinearLayout) findViewById(R.id.ll_Show);
		tv_Yes = (TextView) findViewById(R.id.tv_Yes);
		tv_No = (TextView) findViewById(R.id.tv_No);
		lv_Coupon = (ListView) findViewById(R.id.lv_Coupon);
	}

	private void initData() {
		uid = UserInfoBean.getUserInfo(this).getUid();
		if(StringUtil.isNetworkConnected(this)){
			if(null != uid){
			 mMyPrivilege = mRequestManager.getMyPrivilege(uid, true);	
			}
		}else{
			ToastUtil.makeShortText(this, R.string.no_wangluo);
		}
		adapter = new CouponAdapter(this, arrayList);
		lv_Coupon.setAdapter(adapter);
	}

	private void initListener() {
		tv_Yes.setOnClickListener(this);
		tv_No.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_Yes:
			ll_Show.setBackgroundResource(R.drawable.t1);
			 tv_Yes.setTextColor(0xffffffff);
			 tv_No.setTextColor(0xff3ab483);
			 RESPONSE_LIST = "use_list";
				if(StringUtil.isNetworkConnected(this)){
					if(null != uid){
					 mMyPrivilege = mRequestManager.getMyPrivilege(uid, true);	
					}
				}else{
					ToastUtil.makeShortText(this, R.string.no_wangluo);
				}
			break;
			
		case R.id.tv_No:
			ll_Show.setBackgroundResource(R.drawable.t2);
			tv_Yes.setTextColor(0xff3ab483);
			tv_No.setTextColor(0xffffffff);
			RESPONSE_LIST = "not_list";
			if(StringUtil.isNetworkConnected(this)){
				if(null != uid){
				 mMyPrivilege = mRequestManager.getMyPrivilege(uid, true);	
				}
			}else{
				ToastUtil.makeShortText(this, R.string.no_wangluo);
			}
			break;
		}
	}
	
	@Override
	public void onRequestFinished(int requestId, int resultCode, Bundle payload) {
		super.onRequestFinished(requestId, resultCode, payload);
		if (resultCode == PoCService.SUCCESS_CODE) {
			if(mMyPrivilege == requestId){
				int result = payload.getInt(ResponseBean.RESPONSE_STATE);
				if (result == 1) {
					arrayList = payload.getParcelableArrayList(RESPONSE_LIST);
					if(null != arrayList && arrayList.size()>0){
						adapter.notifyDataSetChanged(arrayList);
					}else{
						arrayList.clear();
						adapter.notifyDataSetChanged(arrayList);
						ToastUtil.makeShortText(this, R.string.no_data);
					}
				} else {
					String msg = payload.getString(ResponseBean.RESPONSE_MESSAGE);
					ToastUtil.makeShortText(this, msg);
				}
			}
		}
	}
}
