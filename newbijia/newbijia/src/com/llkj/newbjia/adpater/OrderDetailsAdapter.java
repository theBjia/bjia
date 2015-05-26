package com.llkj.newbjia.adpater;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.Inflater;

import com.llkj.newbjia.R;
import com.llkj.newbjia.http.UrlConfig;
import com.llkj.newbjia.utils.FinalBitmapUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class OrderDetailsAdapter extends BaseAdapter {
	private LayoutInflater inflater;

	private ArrayList arrayList;
	private FinalBitmapUtil bitmapUtil;
	private Context context;

	public OrderDetailsAdapter(Context context, ArrayList list) {
		this.inflater = LayoutInflater.from(context);
		this.context = context;
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

		if (null == convertView) {
			convertView = inflater.inflate(R.layout.order_details_item, null);
			holdView = new HoldView();
			holdView.im_Photo = (ImageView) convertView
					.findViewById(R.id.im_Photo);
			holdView.tv_CommodityName = (TextView) convertView
					.findViewById(R.id.tv_CommodityName);
			holdView.tv_Weight = (TextView) convertView
					.findViewById(R.id.tv_Weight);
			holdView.tv_Number = (TextView) convertView
					.findViewById(R.id.tv_Number);
			holdView.tv_Pack = (TextView) convertView
					.findViewById(R.id.tv_Pack);

			convertView.setTag(holdView);

		} else {

			holdView = (HoldView) convertView.getTag();
		}
		HashMap item = (HashMap) arrayList.get(position);
		if (item.containsKey("goods_name")) {
			String name = item.get("goods_name").toString();
			holdView.tv_CommodityName.setText(name);
		}
		if (item.containsKey("goods_weight")) {
			String goods_weight = item.get("goods_weight").toString();
			holdView.tv_Weight.setText("重量：" + goods_weight + "kg");
		}
		if (item.containsKey("commodity_packaging")) {
			String commodity_packaging = item.get("commodity_packaging")
					.toString();
			holdView.tv_Pack.setText("包装：" + commodity_packaging);
		}
		if (item.containsKey("goods_number")) {
			String goods_number = item.get("goods_number").toString();
			holdView.tv_Number.setText("数量：" + goods_number + "个");
		}
		if (item.containsKey("goods_thumb")) {
			String goods_thumb = item.get("goods_thumb").toString();
			bitmapUtil.displayForPicture(holdView.im_Photo,
					UrlConfig.ROOT_URL_TWO + goods_thumb);
		}
		return convertView;
	}

	class HoldView {
		ImageView im_Photo;
		TextView tv_CommodityName, tv_Weight, tv_Pack, tv_Number;
	}
}
