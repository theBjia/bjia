package com.llkj.newbjia.adpater;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.llkj.newbjia.R;
import com.llkj.newbjia.bean.ResponseBean;
import com.llkj.newbjia.utils.FinalBitmapUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyMoneyAdapter extends BaseAdapter{
	private LayoutInflater inflater;
	private Map<Integer, View>map = new HashMap<Integer, View>();
	private ArrayList arrayList;
	private Context context;
	private FinalBitmapUtil bitmapUtil;
	
	public MyMoneyAdapter(Context context,ArrayList list){
		this.inflater = LayoutInflater.from(context);
		this.context = context;
		setData(list);
		bitmapUtil = FinalBitmapUtil.create(context);
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
		// TODO Auto-generated method stub
		return arrayList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return arrayList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		HoldView holdView;
		View pv;
		if(null == map.get(position)){
			pv = inflater.inflate(R.layout.my_bijia_money_item, null);
			holdView = new HoldView();
			holdView.tv_MoenyTime = (TextView) pv.findViewById(R.id.tv_MoenyTime);
			holdView.tv_Moeny = (TextView) pv.findViewById(R.id.tv_Moeny);
			holdView.tv_Desc = (TextView) pv.findViewById(R.id.tv_Desc);
			HashMap item = (HashMap) arrayList.get(position);
			if(item.containsKey(ResponseBean.RESPONSE_ADD_TIME)){
				String Time = item.get(ResponseBean.RESPONSE_ADD_TIME).toString();
				holdView.tv_MoenyTime.setText(Time);
			}
			if(item.containsKey(ResponseBean.RESPONSE_USER_MONEY)){
				String Money = item.get(ResponseBean.RESPONSE_USER_MONEY).toString();
				holdView.tv_Moeny.setText(Money);
			}
			if(item.containsKey(ResponseBean.RESPONSE_GOODS_ID)){
				String Desc = item.get(ResponseBean.RESPONSE_GOODS_ID).toString();
				holdView.tv_Desc.setText(Desc);
			}
			pv.setTag(holdView);
			map.put(position, pv);
		}else{
			pv = map.get(position);
			holdView = (HoldView) pv.getTag();
		}
		return pv;
	}
	
	class HoldView{
		TextView tv_MoenyTime,tv_Moeny,tv_Desc;
	}
}
