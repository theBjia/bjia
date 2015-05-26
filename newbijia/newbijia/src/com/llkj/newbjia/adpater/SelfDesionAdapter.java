package com.llkj.newbjia.adpater;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.llkj.newbjia.MyClicker;
import com.llkj.newbjia.R;
import com.llkj.newbjia.adpater.BrowsingAdapter.HoldView;
import com.llkj.newbjia.bean.ResponseBean;
import com.llkj.newbjia.utils.FinalBitmapUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SelfDesionAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private Map<Integer, View> map = new HashMap<Integer, View>();
	private ArrayList arrayList;
	private Context context;
	private FinalBitmapUtil bitmapUtil;
	private MyClicker myClicker;

	public SelfDesionAdapter(Context context, ArrayList list, MyClicker clicker) {
		this.inflater = LayoutInflater.from(context);
		this.context = context;
		setData(list);
		bitmapUtil = FinalBitmapUtil.create(context);
		this.myClicker = clicker;
	}

	private void setData(ArrayList list) {
		if (null != list) {
			this.arrayList = list;
		} else {
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
		if (null == map.get(position)) {
			pv = inflater.inflate(R.layout.serch_self_pont_item, null);
			holdView = new HoldView();
			holdView.tv_self_pont = (TextView) pv
					.findViewById(R.id.tv_self_pont);
			HashMap item = (HashMap) arrayList.get(position);
			holdView.tv_self_pont.setTag(item);
			if (item.containsKey(ResponseBean.RESPONSE_NAME)) {

				String name = item.get(ResponseBean.RESPONSE_NAME).toString();
				if (item.containsKey("s")) {
					name = name + "  距离您" + item.get("s") + "m";
				}

				holdView.tv_self_pont.setText(name);

			}

			holdView.tv_self_pont.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					myClicker.myClick(v, 1);
				}
			});
			pv.setTag(holdView);
			map.put(position, pv);
		} else {
			pv = map.get(position);
			holdView = (HoldView) pv.getTag();
		}
		return pv;
	}

	class HoldView {
		TextView tv_self_pont;
	}
}
