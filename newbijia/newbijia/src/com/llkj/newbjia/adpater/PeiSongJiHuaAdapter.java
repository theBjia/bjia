package com.llkj.newbjia.adpater;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.amap.api.a.s;
import com.llkj.newbjia.MyClicker;
import com.llkj.newbjia.R;
import com.llkj.newbjia.adpater.AddFriendShequAdpter.ViewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PeiSongJiHuaAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater inflater;
	private List arrayList;

	private MyClicker myClicker;

	public PeiSongJiHuaAdapter(Context context, List arrayList,
			MyClicker myClicker) {
		super();
		this.context = context;
		setData(arrayList);
		inflater = LayoutInflater.from(context);

		this.myClicker = myClicker;

	}

	public void setData(List arrayList) {
		if (arrayList == null) {
			this.arrayList = new ArrayList();
		} else {
			this.arrayList = arrayList;
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return arrayList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return arrayList.get(arg0);

	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View view, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (view == null) {
			holder = new ViewHolder();
			view = inflater.inflate(R.layout.item_peisongjihua, null);
			holder.tv_sure_shouhuo = (TextView) view
					.findViewById(R.id.tv_sure_shouhuo);
			holder.tv_time = (TextView) view.findViewById(R.id.tv_time);
			holder.ll_one = (LinearLayout) view.findViewById(R.id.ll_one);
			holder.tv_yishouhuo = (TextView) view
					.findViewById(R.id.tv_yishouhuo);
			holder.tv_sure_shouhuo.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					myClicker.myClick(v, 1);
				}
			});

			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		holder.ll_one.removeAllViews();
		HashMap map = (HashMap) arrayList.get(arg0);
		holder.tv_sure_shouhuo.setTag(map);
		if (map.containsKey("time")) {
			holder.tv_time.setText("配送时间：" + map.get("time") + "");
		}

		if (map.containsKey("goods_list")) {
			ArrayList arrayList = (ArrayList) map.get("goods_list");
			setData(arrayList, holder.ll_one);
		}
		if (map.containsKey("status")) {
			String status = map.get("status") + "";
			if ("0".equals(status)) {
				holder.tv_yishouhuo.setVisibility(View.INVISIBLE);
				holder.tv_sure_shouhuo.setVisibility(View.VISIBLE);
			} else if ("1".equals(status)) {
				holder.tv_yishouhuo.setVisibility(View.VISIBLE);
				holder.tv_sure_shouhuo.setVisibility(View.INVISIBLE);
			}
		}

		return view;
	}

	public void setData(ArrayList arrayList, LinearLayout ll) {
		for (int i = 0; i < arrayList.size(); i++) {
			View view = inflater.inflate(R.layout.item_peisongjihua_two, null);
			TextView tv_goods_name = (TextView) view
					.findViewById(R.id.tv_goods_name);
			TextView tv_goods_num = (TextView) view
					.findViewById(R.id.tv_goods_num);
			HashMap map = (HashMap) arrayList.get(i);
			if (map.containsKey("goods_name")) {
				tv_goods_name.setText(map.get("goods_name") + "");
			}
			if (map.containsKey("tv_goods_num")) {
				tv_goods_num.setText(map.get("goods_num") + "");
			}

			ll.addView(view);
		}

	}

	class ViewHolder {
		TextView tv_sure_shouhuo, tv_time, tv_yishouhuo;
		LinearLayout ll_one;
	}
}
