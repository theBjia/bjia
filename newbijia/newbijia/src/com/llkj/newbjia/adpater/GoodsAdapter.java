package com.llkj.newbjia.adpater;

/**
 * 文章详情页图片适配器
 * 
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.llkj.newbjia.R;
import com.llkj.newbjia.utils.FinalBitmapUtil;

@SuppressLint("UseSparseArrays")
public class GoodsAdapter extends BaseAdapter {

	private LayoutInflater inflater;

	@SuppressWarnings("rawtypes")
	private ArrayList list;
	private Context context;
	private FinalBitmapUtil fb;

	@SuppressWarnings("rawtypes")
	public GoodsAdapter(Context context, ArrayList list) {
		this.inflater = LayoutInflater.from(context);
		this.list = list;
		this.context = context;
		fb = FinalBitmapUtil.create(context);
	}

	@Override
	public int getCount() {

		return list.size();
	}

	@Override
	public Object getItem(int position) {

		return list.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_goods, null);
			holder = new ViewHolder();
			holder.tv_content = (TextView) convertView
					.findViewById(R.id.tv_content);
			holder.tv_price = (TextView) convertView
					.findViewById(R.id.tv_price);
			holder.iv_content = (ImageView) convertView
					.findViewById(R.id.iv_content);
			holder.tv_daili = (TextView) convertView
					.findViewById(R.id.tv_daili);
			convertView.setTag(holder);

		} else {

			holder = (ViewHolder) convertView.getTag();
		}
		HashMap map = (HashMap) list.get(position);
		if (map.containsKey("goods_name")) {
			holder.tv_content.setText((map.get("goods_name") + "").substring(0,3)+"...");
		}
		if (map.containsKey("shop_price")) {
			holder.tv_price.setText("￥" + map.get("shop_price") + "");
		}
		if (map.containsKey("goods_weight")) {
			holder.tv_daili.setText("/" + map.get("goods_weight") + "kg");
		}
		if (map.containsKey("goods_img")) {
			String goods_img = map.get("goods_img") + "";
			fb.displayForPicture(holder.iv_content, goods_img);
		}
		return convertView;
	}

	class ViewHolder {
		TextView tv_content, tv_price, tv_daili;
		ImageView iv_content;

	}

}
