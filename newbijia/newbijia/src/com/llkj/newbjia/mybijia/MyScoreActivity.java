package com.llkj.newbjia.mybijia;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.llkj.newbjia.BaseActivity;
import com.llkj.newbjia.MainActivity;
import com.llkj.newbjia.R;
import com.llkj.newbjia.adpater.MyScoreAdapter;
import com.llkj.newbjia.bean.KeyBean;
import com.llkj.newbjia.bean.ResponseBean;
import com.llkj.newbjia.bean.UserInfoBean;
import com.llkj.newbjia.http.PoCService;
import com.llkj.newbjia.utils.LogUtil;
import com.llkj.newbjia.utils.ToastUtil;
/**
 * 我的积分页
 * @author John
 *
 */
public class MyScoreActivity extends BaseActivity{
	
	private ListView lv_Form;
	private ArrayList arrayList;
	private MyScoreAdapter adapter;
	private int mMyIntegral;
	private Intent bigIntent;
	private String uid;
	private TextView tv_Score_Balance;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_score);
		setTitle(R.string.myscore, true, R.string.kong, true, R.string.kong);
		initView();
		initData();
		initListener();
	}

	private void initView() {
		lv_Form = (ListView) findViewById(R.id.lv_Form);
		tv_Score_Balance = (TextView) findViewById(R.id.tv_Score_Balance);
	}

	private void initData() {
		uid = UserInfoBean.getUserInfo(this).getUid();
		if(null != uid){
			mMyIntegral = mRequestManager.getMyIntegral(uid, true);
		}
		adapter = new MyScoreAdapter(MyScoreActivity.this, arrayList);
		lv_Form.setAdapter(adapter);
	}

	private void initListener() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onRequestFinished(int requestId, int resultCode, Bundle payload) {
		super.onRequestFinished(requestId, resultCode, payload);
		if (resultCode == PoCService.SUCCESS_CODE) {
			if(mMyIntegral == requestId){
				int result = payload.getInt(ResponseBean.RESPONSE_STATE);
				if (result == 1) {
					arrayList = payload.getParcelableArrayList(ResponseBean.RESPONSE_LIST);
					if(null != arrayList && arrayList.size()>0){
						adapter.notifyDataSetChanged(arrayList);
					}else{
						ToastUtil.makeShortText(this, R.string.no_data);
					}
					String Score_Balance = payload.getString(ResponseBean.RESPONSE_POINTS);
					tv_Score_Balance.setText(Score_Balance);
				} else {
					String msg = payload.getString(ResponseBean.RESPONSE_MESSAGE);
					ToastUtil.makeShortText(this, msg);
				}
			}
		}
	}
}
