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
public class GoodsDetailAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	protected Map<Integer, View> viewMap = new HashMap<Integer, View>();
	@SuppressWarnings("rawtypes")
	private ArrayList list;
	private Context context;
	private FinalBitmapUtil fb;

	@SuppressWarnings("rawtypes")
	public GoodsDetailAdapter(Context context, ArrayList list) {
		this.inflater = LayoutInflater.from(context);
		this.list = list;
		this.context = context;
		fb = FinalBitmapUtil.create(context);
	}

	@SuppressWarnings("rawtypes")
	public void notifyDataSetChanged(ArrayList arrayList) {
		viewMap.clear();
		this.notifyDataSetChanged();

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
		View pv;
		if (viewMap.get(position) == null) {
			pv = inflater.inflate(R.layout.item_goods_detail, null);
			holder = new ViewHolder();
			holder.tv_content = (TextView) pv.findViewById(R.id.tv_content);
			holder.tv_price = (TextView) pv.findViewById(R.id.tv_price);
			holder.iv_content = (ImageView) pv.findViewById(R.id.iv_content);

			pv.setTag(holder);
			viewMap.put(position, pv);

		} else {
			pv = viewMap.get(position);
			holder = (ViewHolder) pv.getTag();
		}
		HashMap map = (HashMap) list.get(position);
		if (map.containsKey("goods_name")) {
			String goods_name = map.get("goods_name") + "";
			holder.tv_content.setText(goods_name);
		}
		if (map.containsKey("goods_img")) {
			String goods_img = map.get("goods_img") + "";
			fb.displayForPicture(holder.iv_content, goods_img);
		}
		if (map.containsKey("shop_price")) {
			String shop_price = map.get("shop_price") + "";
			holder.tv_price.setText("￥"+shop_price);
		}
		return pv;
	}

	class ViewHolder {
		TextView tv_content, tv_price;
		ImageView iv_content;

	}

}
