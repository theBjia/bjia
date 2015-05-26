package com.llkj.newbjia.adpater;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.llkj.newbjia.R;
import com.llkj.newbjia.bean.ResponseBean;
import com.llkj.newbjia.utils.FinalBitmapUtil;

public class CouponAdapter extends BaseAdapter{
	private LayoutInflater inflater;
	private ArrayList arrayList;
	private Map<Integer, View>map = new HashMap<Integer, View>();
	private Context context;
	private FinalBitmapUtil bitmapUtil;
	
	public CouponAdapter(Context context,ArrayList list){
		this.inflater = LayoutInflater.from(context);
		this.context = context;
		setData(list);
		this.bitmapUtil = FinalBitmapUtil.create(context);
	}
	private void setData(ArrayList list) {
		if(null != list){
			this.arrayList = list;
		}else{
			this.arrayList = new ArrayList();
		}
	}
	
	public void notifyDataSetChanged(ArrayList list) {
		setData(list);
		map.clear();
		this.notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		return arrayList.size();
	}

	@Override
	public Object getItem(int position) {
		return arrayList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		HoldView holdView;
		View pv;
		if(null == map.get(position)){
			pv = inflater.inflate(R.layout.coupon_item, null);
			holdView = new HoldView();
			holdView.tvPrivilege = (TextView) pv.findViewById(R.id.tvPrivilege);
			holdView.tvMoney = (TextView) pv.findViewById(R.id.tvMoney);
			holdView.tvStartTime = (TextView) pv.findViewById(R.id.tvStartTime);
			holdView.tvEndTime = (TextView) pv.findViewById(R.id.tvEndTime);
			HashMap item = (HashMap) arrayList.get(position);
			if(item.containsKey(ResponseBean.RESPONSE_TYPE_NAME)){
				String name = (String) item.get(ResponseBean.RESPONSE_TYPE_NAME);
				holdView.tvPrivilege.setText(name);
			}
			if(item.containsKey(ResponseBean.RESPONSE_TYPE_MONEY)){
				String name = (String) item.get(ResponseBean.RESPONSE_TYPE_MONEY);
				holdView.tvMoney.setText(name);
			}
			if(item.containsKey(ResponseBean.RESPONSE_USE_STARTDATE)){
				String name = (String) item.get(ResponseBean.RESPONSE_USE_STARTDATE);
				holdView.tvStartTime.setText(name);
			}
			if(item.containsKey(ResponseBean.RESPONSE_USE_ENDDATE)){
				String name = (String) item.get(ResponseBean.RESPONSE_USE_ENDDATE);
				holdView.tvEndTime.setText(name);
			}
			pv.setTag(holdView);
			map.put(position, pv);
		}else{
			pv= map.get(position);
			holdView = (HoldView) pv.getTag();
		}
		return pv;
	}
	
	class HoldView{
		TextView tvPrivilege,tvMoney,tvStartTime,tvEndTime;
	}
}
