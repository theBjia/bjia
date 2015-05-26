package com.llkj.newbjia.adpater;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.llkj.newbjia.R;
import com.llkj.newbjia.utils.FinalBitmapUtil;

public class PublishPicAdpter extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater;
	private ArrayList arrayList;
	private FinalBitmapUtil fb;

	public PublishPicAdpter(Context context, ArrayList arrayList) {
		super();
		this.context = context;
		setData(arrayList);
		inflater = LayoutInflater.from(context);
		fb = FinalBitmapUtil.create(context);
	}

	public void setData(ArrayList arrayList) {
		if (arrayList == null) {
			this.arrayList = new ArrayList();
		} else {
			this.arrayList = arrayList;
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return arrayList.size() + 1;
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
	public View getView(int position, View view, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (view == null) {
			holder = new ViewHolder();
			view = inflater.inflate(R.layout.item_publish, null);
			holder.iv_one = (ImageView) view.findViewById(R.id.iv_one);
			holder.iv_red = (ImageView) view.findViewById(R.id.iv_red);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		if (position == arrayList.size()) {
			holder.iv_one.setImageResource(R.drawable.add_hao);
			holder.iv_red.setVisibility(View.GONE);
			if (arrayList.size() == 9) {
				holder.iv_one.setVisibility(View.GONE);
			} else {
				holder.iv_one.setVisibility(View.VISIBLE);
			}
		} else {
			holder.iv_red.setVisibility(View.VISIBLE);
			holder.iv_one.setVisibility(View.VISIBLE);
			HashMap map = (HashMap) arrayList.get(position);
			if (map.containsKey("url")) {
				String path = map.get("url").toString();
				fb.displayForPicture(holder.iv_one, path);
			}
		}
		return view;
	}

	class ViewHolder {
		ImageView iv_one, iv_red;
	}

}
