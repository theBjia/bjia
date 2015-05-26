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

@SuppressLint("UseSparseArrays")
public class GoodsInfoAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	protected Map<Integer, View> viewMap = new HashMap<Integer, View>();
	@SuppressWarnings("rawtypes")
	private ArrayList list;
	private Context context;

	@SuppressWarnings("rawtypes")
	public GoodsInfoAdapter(Context context, ArrayList list) {
		this.inflater = LayoutInflater.from(context);
		this.list = list;
		this.context = context;
	}

	@SuppressWarnings("rawtypes")
	public void notifyDataSetChanged(ArrayList arrayList) {
		viewMap.clear();
		this.notifyDataSetChanged();

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
		View pv;
		if (viewMap.get(position) == null) {
			pv = inflater.inflate(R.layout.item_goods_info, null);
			holder = new ViewHolder();
			holder.tv_jingzhong = (TextView) pv.findViewById(R.id.tv_jingzhong);
			holder.tv_geshu = (TextView) pv.findViewById(R.id.tv_geshu);
			holder.iv_content = (ImageView) pv.findViewById(R.id.iv_content);
			holder.tv_pinzhong = (TextView) pv.findViewById(R.id.tv_pinzhong);
			pv.setTag(holder);
			viewMap.put(position, pv);

		} else {
			pv = viewMap.get(position);
			holder = (ViewHolder) pv.getTag();
		}

		return pv;
	}

	class ViewHolder {
		TextView tv_jingzhong, tv_geshu, tv_pinzhong;
		ImageView iv_content;

	}

}
