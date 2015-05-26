package com.llkj.newbjia.adpater;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.llkj.newbjia.MyClicker;
import com.llkj.newbjia.R;
import com.llkj.newbjia.adpater.MyOrderAdapter.HolderView;
import com.llkj.newbjia.bean.ResponseBean;
import com.llkj.newbjia.http.UrlConfig;
import com.llkj.newbjia.utils.FinalBitmapUtil;

import android.R.array;
import android.R.integer;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BrowsingAdapter extends BaseAdapter {
	private LayoutInflater inflater;

	private ArrayList arrayList;
	private Context context;
	private FinalBitmapUtil bitmapUtil;
	private MyClicker clicker;

	public BrowsingAdapter(Context context, ArrayList list, MyClicker clicker) {
		this.inflater = LayoutInflater.from(context);
		this.context = context;
		this.clicker = clicker;
		setData(list);
		bitmapUtil = FinalBitmapUtil.create(context);
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
			convertView = inflater
					.inflate(R.layout.browsing_history_item, null);
			holdView = new HoldView();
			holdView.im_Photo = (ImageView) convertView
					.findViewById(R.id.im_Photo);
			holdView.tv_Name = (TextView) convertView
					.findViewById(R.id.tv_Name);
			holdView.tv_Weight = (TextView) convertView
					.findViewById(R.id.tv_Weight);
			holdView.tv_Pack = (TextView) convertView
					.findViewById(R.id.tv_Pack);
			holdView.tv_money = (TextView) convertView
					.findViewById(R.id.tv_money);
			holdView.rl_Show = (RelativeLayout) convertView
					.findViewById(R.id.rl_Show);
			holdView.rl_Show.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					clicker.myClick(v, 1);
				}
			});

			convertView.setTag(holdView);

		} else {

			holdView = (HoldView) convertView.getTag();
		}
		HashMap item = (HashMap) arrayList.get(position);
		holdView.rl_Show.setTag(item);
		if (item.containsKey(ResponseBean.RESPONSE_GOODS_NAME)) {
			String name = item.get(ResponseBean.RESPONSE_GOODS_NAME)+"";
			holdView.tv_Name.setText(name);
		}
		if (item.containsKey(ResponseBean.RESPONSE_GOODS_PRICE)) {
			String goods_price = item.get(ResponseBean.RESPONSE_GOODS_PRICE)+"";
			holdView.tv_money.setText("￥"+goods_price+"元");
		}
		if (item.containsKey(ResponseBean.RESPONSE_COMMODITY_PACKAGING)) {
			String commodity_packaging = item.get(ResponseBean.RESPONSE_COMMODITY_PACKAGING)+"";
			holdView.tv_Pack.setText("包装："+commodity_packaging);
		}
		if (item.containsKey(ResponseBean.RESPONSE_GOODS_WEIGHT)) {
			String goods_weight = item.get(ResponseBean.RESPONSE_GOODS_WEIGHT)+"";
			holdView.tv_Weight.setText("重量："+goods_weight+"kg");
		}
		if (item.containsKey(ResponseBean.RESPONSE_GOODS_IMG)) {
			String image = item.get(ResponseBean.RESPONSE_GOODS_IMG)+"";
			bitmapUtil.displayForPicture(holdView.im_Photo, UrlConfig.ROOT_URL_TWO+image);
		}
		
		return convertView;
	}

	class HoldView {
		RelativeLayout rl_Show;
		ImageView im_Photo;
		TextView tv_Name, tv_Weight, tv_Pack, tv_money;
	}
}
