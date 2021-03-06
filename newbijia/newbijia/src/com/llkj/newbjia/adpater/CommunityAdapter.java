package com.llkj.newbjia.adpater;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.llkj.newbjia.R;
import com.llkj.newbjia.utils.FinalBitmapUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CommunityAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private ArrayList arrayList;
	private FinalBitmapUtil fbutil;
	private Context context;

	public CommunityAdapter(Context context, ArrayList list) {
		this.inflater = LayoutInflater.from(context);
		this.context = context;
		setData(list);
		fbutil = FinalBitmapUtil.create(context);
	}

	private void setData(ArrayList list) {
		if (null != list) {
			this.arrayList = list;
		} else {
			arrayList = new ArrayList();
		}
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

		if (null == convertView) {
			convertView = inflater.inflate(R.layout.community_ring_item, null);
			holdView = new HoldView();
			holdView.tv_CommunitRing = (TextView) convertView
					.findViewById(R.id.tv_CommunitRing);

			convertView.setTag(holdView);

		} else {

			holdView = (HoldView) convertView.getTag();
		}
		 HashMap item = (HashMap) arrayList.get(position);
		 if(item.containsKey("name")){
			 String name = item.get("name").toString();
			 holdView.tv_CommunitRing.setText(name);
		 }
		return convertView;
	}

	class HoldView {
		TextView tv_CommunitRing;
	}
}
