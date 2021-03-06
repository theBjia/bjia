package com.llkj.newbjia.mybijia;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.llkj.newbjia.BaseActivity;
import com.llkj.newbjia.R;
import com.llkj.newbjia.adpater.MyMoneyAdapter;
import com.llkj.newbjia.bean.KeyBean;
import com.llkj.newbjia.bean.ResponseBean;
import com.llkj.newbjia.bean.UserInfoBean;
import com.llkj.newbjia.http.PoCService;
import com.llkj.newbjia.login.RegsterTwoActivity;
import com.llkj.newbjia.utils.LogUtil;
import com.llkj.newbjia.utils.StringUtil;
import com.llkj.newbjia.utils.ToastUtil;
/**
 * 我的彼佳币页
 * @author John
 *
 */
public class MyMoneyActivity extends BaseActivity{
	private ListView lv_Money;
	private ArrayList arrayList;
	private MyMoneyAdapter adapter;
	private TextView tv_Money_Balance;
	private int mMycoin;
	private String uid;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_bijia_money);
		setTitle(R.string.mymoeny, true, R.string.kong, true, R.string.kong);
		initView();
		initData();
		initListener();
	}

	private void initView() {
		lv_Money = (ListView) findViewById(R.id.lv_Money);
		tv_Money_Balance = (TextView) findViewById(R.id.tv_Money_Balance);
	}

	private void initData() {
		uid = UserInfoBean.getUserInfo(this).getUid();
		if(StringUtil.isNetworkConnected(this)){
			if(null != uid){
				mMycoin = mRequestManager.getMyCoin(uid, true);
			}
		}else{
			ToastUtil.makeShortText(this, R.string.no_wangluo);
		}
		
		adapter = new MyMoneyAdapter(MyMoneyActivity.this, arrayList);
		lv_Money.setAdapter(adapter);
	}

	private void initListener() {
		
	}
	
	@Override
	public void onRequestFinished(int requestId, int resultCode, Bundle payload) {
		super.onRequestFinished(requestId, resultCode, payload);
		if (resultCode == PoCService.SUCCESS_CODE) {
			if(mMycoin == requestId){
				int result = payload.getInt(ResponseBean.RESPONSE_STATE);
				if (result == 1) {
					String money = payload.getString(ResponseBean.RESPONSE_USER_MONEY);
					tv_Money_Balance.setText(money);
					arrayList = payload.getParcelableArrayList(ResponseBean.RESPONSE_LIST);
					if(null!=arrayList&&arrayList.size()>0){
						adapter.notifyDataSetChanged(arrayList);
						
					}else{
						//ToastUtil.makeShortText(this, R.string.no_data);
					}
					String user_money = payload.getString(ResponseBean.RESPONSE_USER_MONEY);
					tv_Money_Balance.setText(user_money);
				} else {
					String msg = payload.getString(ResponseBean.RESPONSE_MESSAGE);
					ToastUtil.makeShortText(this, msg);
				}
			}
		}
	}
}
