package com.llkj.newbjia.adpater;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.llkj.newbjia.MyClicker;
import com.llkj.newbjia.R;
import com.llkj.newbjia.bean.ResponseBean;
import com.llkj.newbjia.http.UrlConfig;
import com.llkj.newbjia.utils.FinalBitmapUtil;
import com.llkj.newbjia.utils.StringUtil;

public class WriteOrderAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private ArrayList arrayList;
	private Context context;
	private FinalBitmapUtil bitmapUtil;
	private MyClicker myClicker;

	public WriteOrderAdapter(Context context, ArrayList list,
			MyClicker myClicker) {
		this.inflater = LayoutInflater.from(context);
		this.context = context;
		setData(list);
		this.bitmapUtil = FinalBitmapUtil.create(context);
		this.myClicker = myClicker;

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

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.write_dan_itme, null);
			holdView = new HoldView();

			holdView.tv_number = (TextView) convertView
					.findViewById(R.id.tv_num);
			holdView.tv_money = (TextView) convertView
					.findViewById(R.id.tv_money);
			holdView.img_good = (ImageView) convertView
					.findViewById(R.id.good_img);
			holdView.tv_weight = (TextView) convertView
					.findViewById(R.id.tv_weight);
			holdView.tv_pakge = (TextView) convertView
					.findViewById(R.id.tv_pakge);
			holdView.tv_goods_name = (TextView) convertView
					.findViewById(R.id.tv_goods_name);
			holdView.rl_bg = (RelativeLayout) convertView
					.findViewById(R.id.rl_bg);
			holdView.rl_bg.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					myClicker.myClick(v, 1);// 跳转
				}
			});
			convertView.setTag(holdView);

		} else {

			holdView = (HoldView) convertView.getTag();
		}

		HashMap item = (HashMap) arrayList.get(position);
		holdView.rl_bg.setTag(item);
		String Money = "", number = "", weight = "", packageName = "", image = "";
		if (item.containsKey(ResponseBean.RESPONSE_GOODS_PRICE)) {
			Money = item.get(ResponseBean.RESPONSE_GOODS_PRICE).toString();
			holdView.tv_money.setText(Money);

		}
		String img = item.get(ResponseBean.RESPONSE_GOODS_IMG).toString();
		if (!StringUtil.isEmpty(img)) {
			bitmapUtil.displayForPicture(holdView.img_good,
					UrlConfig.ROOT_URL_TWO + img);
		} else {
			bitmapUtil.displayForPicture(holdView.img_good,
					UrlConfig.ROOT_URL_TWO + img);
		}
	

		if (item.containsKey(ResponseBean.RESPONSE_GOODS_NAME)) {
			String name = item.get(ResponseBean.RESPONSE_GOODS_NAME).toString();
			holdView.tv_goods_name.setText(name.substring(0, 3)+"...");
		}
		if (item.containsKey(ResponseBean.RESPONSE_GOODS_WEIGHT)) {
			weight = item.get(ResponseBean.RESPONSE_GOODS_WEIGHT).toString();

		}
		if (item.containsKey(ResponseBean.RESPONSE_COMMODITY_PACKAGING)) {
			packageName = item.get(ResponseBean.RESPONSE_COMMODITY_PACKAGING)
					.toString();
			holdView.tv_weight.setText(weight + "g");
			holdView.tv_pakge.setText(packageName);

		}
		if (item.containsKey(ResponseBean.RESPONSE_GOODS_NUMBER)) {
			number = item.get(ResponseBean.RESPONSE_GOODS_NUMBER).toString();
			holdView.tv_number.setText("X" + number);
		}
		return convertView;
	}

	class HoldView {
		TextView tv_money, tv_number, tv_weight, tv_pakge, tv_goods_name;
		RelativeLayout rl_bg;
		ImageView img_good;

	}
}
