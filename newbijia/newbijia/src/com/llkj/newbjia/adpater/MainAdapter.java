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
import com.llkj.newbjia.utils.StringUtil;

@SuppressLint("UseSparseArrays")
public class MainAdapter extends BaseAdapter {

	private LayoutInflater inflater;

	@SuppressWarnings("rawtypes")
	private ArrayList list;
	private Context context;
	private FinalBitmapUtil fb;

	@SuppressWarnings("rawtypes")
	public MainAdapter(Context context, ArrayList list) {
		this.inflater = LayoutInflater.from(context);
		this.list = list;
		this.context = context;
		fb = FinalBitmapUtil.create(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_main, null);
			holder = new ViewHolder();
			holder.tv_conent_two = (TextView) convertView
					.findViewById(R.id.tv_conent_two);
			holder.tv_conent_one = (TextView) convertView
					.findViewById(R.id.tv_conent_one);
			holder.iv_content = (ImageView) convertView
					.findViewById(R.id.iv_content);
			holder.iv_cart = (ImageView) convertView.findViewById(R.id.iv_cart);
			convertView.setTag(holder);

		} else {

			holder = (ViewHolder) convertView.getTag();
		}

		HashMap map = (HashMap) list.get(position);
		String name = "", price = "", goods_img = "";
		if (map.containsKey("goods_name")) {
			name = map.get("goods_name") + "";
			holder.tv_conent_one.setText("团购秒杀");
			holder.iv_cart.setImageResource(R.drawable.icon_tuang_cart);
		} else {
			holder.iv_cart.setImageResource(R.drawable.chuxiao);
			holder.tv_conent_one.setText("促销活动");
		}
		if (map.containsKey("goods_img")) {
			goods_img = map.get("goods_img") + "";
		}
		if (map.containsKey("last_price")) {
			price = map.get("last_price") + "";
			holder.tv_conent_two.setText(name + "一口价" + price);
		}

		if (map.containsKey("name")) {
			name = map.get("name") + "";
		}
		if (map.containsKey("promote_price")) {
			price = map.get("promote_price") + "";
			holder.tv_conent_two.setText(name + "促销价：" + price);
		}
		if (!StringUtil.isEmpty(goods_img)) {
			fb.displayForPicture(holder.iv_content, goods_img);
		} else {
			holder.iv_content.setImageResource(R.drawable.ic_launcher);
		}

		return convertView;
	}

	class ViewHolder {
		TextView tv_conent_one, tv_conent_two;
		ImageView iv_content, iv_cart;

	}

}
