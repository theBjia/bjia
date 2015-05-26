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
public class AddFriendAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	protected Map<Integer, View> viewMap = new HashMap<Integer, View>();
	@SuppressWarnings("rawtypes")
	private ArrayList list;
	private Context context;

	@SuppressWarnings("rawtypes")
	public AddFriendAdapter(Context context, ArrayList list) {
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
			pv = inflater.inflate(R.layout.item_addfriend, null);
			holder = new ViewHolder();
			holder.tv_content = (TextView) pv.findViewById(R.id.tv_content);
			holder.iv_content = (ImageView) pv.findViewById(R.id.iv_content);
			pv.setTag(holder);
			viewMap.put(position, pv);

		} else {
			pv = viewMap.get(position);
			holder = (ViewHolder) pv.getTag();
		}
		String type = "";
		HashMap map = (HashMap) list.get(position);
		if (map.containsKey("type")) {
			type = map.get("type") + "";
		}

		if (map.containsKey("name")) {
			String name = map.get("name") + "";
			if ("-1".equals(type)) {
				holder.tv_content.setText("添加通讯录好友");
				holder.iv_content.setImageResource(R.drawable.tongxunluicon);
				//changed by wang 2015.4.21
				holder.iv_content.setVisibility(View.INVISIBLE);
			} else if ("1".equals(type)) {
				holder.tv_content.setText("添加" + name + "城市圈好友");
				holder.iv_content.setImageResource(R.drawable.cityquan);
				holder.iv_content.setVisibility(View.INVISIBLE);
			} else if ("2".equals(type)) {
				holder.tv_content.setText("添加" + name + "社区圈好友");
				holder.iv_content.setImageResource(R.drawable.sheququanicon);
				holder.iv_content.setVisibility(View.INVISIBLE);
			}
		}

		return pv;
	}

	class ViewHolder {
		TextView tv_content;
		ImageView iv_content;

	}

}
