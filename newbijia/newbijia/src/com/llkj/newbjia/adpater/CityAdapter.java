package com.llkj.newbjia.adpater;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.llkj.newbjia.R;
import com.llkj.newbjia.utils.FinalBitmapUtil;

import net.tsz.afinal.FinalBitmap;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CityAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private ArrayList arrayList;
	private FinalBitmapUtil bitmapUtil;
	private Context context;

	public CityAdapter(Context context, ArrayList list) {
		this.inflater = LayoutInflater.from(context);
		this.context = context;
		setData(list);
		bitmapUtil.create(context);
	}

	private void setData(ArrayList list) {
		if (null != list) {
			this.arrayList = list;
		} else {
			this.arrayList = new ArrayList();
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return arrayList.size();
	}

	@Override
	public Object getItem(int position) {
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
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.city_ring_item, null);
			holdView = new HoldView();
			holdView.tv_Ring = (TextView) convertView
					.findViewById(R.id.tv_Ring);
			
			convertView.setTag(holdView);

		} else {

			holdView = (HoldView) convertView.getTag();
		}
		HashMap item = (HashMap) arrayList.get(position);
		if (item.containsKey("name")) {
			String name = item.get("name") + "";
			holdView.tv_Ring.setText(name);
		}
		return convertView;
	}

	class HoldView {
		TextView tv_Ring;
	}
}
