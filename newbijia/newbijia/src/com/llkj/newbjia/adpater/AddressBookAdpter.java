package com.llkj.newbjia.adpater;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.llkj.newbjia.MyClicker;
import com.llkj.newbjia.R;
import com.llkj.newbjia.sortlistview.SortModel;

public class AddressBookAdpter extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater;
	private List<SortModel> arrayList;
	private int type;
	private MyClicker myClicker;

	public AddressBookAdpter(Context context, List<SortModel> arrayList,
			int type, MyClicker myClicker) {
		super();
		this.context = context;
		setData(arrayList);
		inflater = LayoutInflater.from(context);
		this.type = type;
		this.myClicker = myClicker;

	}

	public void setData(List<SortModel> arrayList) {
		if (arrayList == null) {
			this.arrayList = new ArrayList();
		} else {
			this.arrayList = arrayList;
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return arrayList.size();
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
	public View getView(int arg0, View view, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (view == null) {
			holder = new ViewHolder();
			view = inflater.inflate(R.layout.item_address_book, null);
			holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
			holder.tv_do = (TextView) view.findViewById(R.id.tv_do);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		SortModel sm = arrayList.get(arg0);
		holder.tv_do.setTag(sm);
		holder.tv_name.setText(sm.getName()+sm.getPhone());
		switch (type) {
		case 0:
			holder.tv_do.setText("添加");
			holder.tv_do.setTextColor(context.getResources().getColor(
					R.color.green));
			holder.tv_do.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					myClicker.myClick(v, 0);//添加好友
				}
			});

			break;
		case 1:
			holder.tv_do.setText("邀请");
			holder.tv_do.setTextColor(context.getResources().getColor(
					R.color.blue));

			holder.tv_do.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					myClicker.myClick(v, 1);//邀请好友
				}
			});
			break;

		case 2:
			holder.tv_do.setText("已添加");
			holder.tv_do.setTextColor(context.getResources().getColor(
					R.color.text_color_gray));

			break;

		default:
			break;
		}

		return view;
	}

	class ViewHolder {
		TextView tv_name, tv_do;
	}

}
