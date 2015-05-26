package com.llkj.newbjia.adpater;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.llkj.newbjia.R;
import com.llkj.newbjia.bean.ResponseBean;
import com.llkj.newbjia.utils.FinalBitmapUtil;
import com.llkj.newbjia.utils.LogUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyScoreAdapter extends BaseAdapter{
	private LayoutInflater inflater;
	private Map<Integer, View>map = new HashMap<Integer, View>();
	private ArrayList arrayList;
	private Context context;
	private FinalBitmapUtil bitmapUtil;
	
	public MyScoreAdapter(Context context,ArrayList list){
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
			pv = inflater.inflate(R.layout.my_score_item, null);
			holdView = new HoldView();
			holdView.tv_ScoreTime = (TextView) pv.findViewById(R.id.tv_ScoreTime);
			holdView.tv_ScoreMoeny = (TextView) pv.findViewById(R.id.tv_ScoreMoeny);
			holdView.tv_ScoreDesc = (TextView) pv.findViewById(R.id.tv_ScoreDesc);
			HashMap item = (HashMap) arrayList.get(position);
			if(item.containsKey(ResponseBean.RESPONSE_ADD_TIME)){
				String Time = item.get(ResponseBean.RESPONSE_ADD_TIME).toString();
				holdView.tv_ScoreTime.setText(Time);
			}
			if(item.containsKey(ResponseBean.RESPONSE_POINTS)){
				String Money = item.get(ResponseBean.RESPONSE_POINTS).toString();
				holdView.tv_ScoreMoeny.setText(Money);
			}
			if(item.containsKey(ResponseBean.RESPONSE_DESC)){
				String desc = item.get(ResponseBean.RESPONSE_DESC).toString();
				holdView.tv_ScoreDesc.setText(desc);
			}
			if(item.containsKey(ResponseBean.RESPONSE_GOODS_ID)){
				String desc = item.get(ResponseBean.RESPONSE_GOODS_ID).toString();
				holdView.tv_ScoreDesc.setText(desc);
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
		TextView tv_ScoreTime,tv_ScoreMoeny,tv_ScoreDesc;
	}
}
