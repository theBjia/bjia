package com.llkj.newbjia.adpater;

/**
 * 分类适配器
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
public class FenLeiAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	protected Map<Integer, View> viewMap = new HashMap<Integer, View>();

	@SuppressWarnings("rawtypes")
	private ArrayList list;
	private Context context;
	private FinalBitmapUtil fb;

	@SuppressWarnings("rawtypes")
	public FenLeiAdapter(Context context, ArrayList list) {
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
			pv = inflater.inflate(R.layout.item_fenlei, null);
			holder = new ViewHolder();
			holder.tv_content = (TextView) pv.findViewById(R.id.tv_content);
			holder.iv_content = (ImageView) pv.findViewById(R.id.iv_content);
			holder.iv_menu_folded = (ImageView) pv.findViewById(R.id.iv_menu_folded);
			pv.setTag(holder);
			viewMap.put(position, pv);

		} else {
			pv = viewMap.get(position);
			holder = (ViewHolder) pv.getTag();
		}
		HashMap map = (HashMap) list.get(position);
		if (map.containsKey("cat_name")) {
			holder.tv_content.setText(map.get("cat_name") + "");
		}
		if (map.containsKey("cat_logo")) {
			fb.displayForPicture(holder.iv_content, map.get("cat_logo") + "");
		}

		return pv;
	}

	class ViewHolder {
		TextView tv_content;
		ImageView iv_content;
		ImageView iv_menu_folded;

	}

}
