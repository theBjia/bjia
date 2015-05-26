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
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.llkj.newbjia.MyClicker;
import com.llkj.newbjia.R;
import com.llkj.newbjia.utils.FinalBitmapUtil;
import com.llkj.newbjia.utils.StringUtil;

@SuppressLint("UseSparseArrays")
public class NewFriendAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	protected Map<Integer, View> viewMap = new HashMap<Integer, View>();
	@SuppressWarnings("rawtypes")
	private ArrayList list;
	private Context context;
	private MyClicker myClicker;
	private FinalBitmapUtil fb;

	@SuppressWarnings("rawtypes")
	public NewFriendAdapter(Context context, ArrayList list, MyClicker myClicker) {
		this.inflater = LayoutInflater.from(context);
		this.list = list;
		this.context = context;
		this.myClicker = myClicker;
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
		final ViewHolder holder;
		View pv;
		if (viewMap.get(position) == null) {
			pv = inflater.inflate(R.layout.item_newfriend, null);
			holder = new ViewHolder();
			holder.tv_passyanzheng = (TextView) pv
					.findViewById(R.id.tv_passyanzheng);
			holder.tv_delete = (TextView) pv.findViewById(R.id.tv_delete);
			holder.tv_user_name = (TextView) pv.findViewById(R.id.tv_user_name);
			holder.iv_logo = (ImageView) pv.findViewById(R.id.iv_logo);
			holder.rl_bg = (RelativeLayout) pv.findViewById(R.id.rl_bg);
			holder.tv_delete.setVisibility(View.GONE);
			holder.tv_passyanzheng.setVisibility(View.VISIBLE);
			holder.tv_passyanzheng.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					myClicker.myClick(v, 1);// 1通过验证
				}
			});
			holder.tv_delete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					myClicker.myClick(v, 2);// 删除
				}
			});
			holder.rl_bg.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) {
					// TODO Auto-generated method stub
					holder.tv_delete.setVisibility(View.VISIBLE);
					holder.tv_passyanzheng.setVisibility(View.GONE);
					return true;
				}
			});
			pv.setTag(holder);
			viewMap.put(position, pv);

		} else {
			pv = viewMap.get(position);
			holder = (ViewHolder) pv.getTag();
		}

		HashMap map = (HashMap) list.get(position);
		holder.tv_passyanzheng.setTag(map);
		holder.tv_delete.setTag(map);
		if (map.containsKey("user_name")) {
			holder.tv_user_name.setText(map.get("user_name") + "");
		}
		if (map.containsKey("logo")) {
			String logo = map.get("logo") + "";
			if (!StringUtil.isEmpty(logo)) {
				fb.displayForHeader(holder.iv_logo, logo);
			} else {
				holder.iv_logo.setImageResource(R.drawable.ic_launcher);
			}

		}

		return pv;
	}

	class ViewHolder {
		TextView tv_passyanzheng, tv_user_name, tv_delete;
		ImageView iv_logo;
		RelativeLayout rl_bg;
	}

}
