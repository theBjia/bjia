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
import com.llkj.newbjia.utils.FinalBitmapUtil;


public class AceeGoodAdressAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private ArrayList arrayList;

	private Context context;
	private FinalBitmapUtil bitmapUtil;
	private MyClicker onclick;

	public AceeGoodAdressAdapter(Context context, ArrayList list,
			MyClicker onclick) {
		this.inflater = LayoutInflater.from(context);
		this.context = context;
		this.onclick = onclick;
		setData(list);
		this.bitmapUtil = FinalBitmapUtil.create(context);
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
			convertView = inflater
					.inflate(R.layout.acee_good_adress_item, null);
			holdView = new HoldView();
			holdView.im_penselId = (ImageView) convertView
					.findViewById(R.id.im_penselId);
			holdView.tv_uname_goodsId = (TextView) convertView
					.findViewById(R.id.tv_uname_goodsId);
			holdView.tv_phonGoodsId = (TextView) convertView
					.findViewById(R.id.tv_phonGoodsId);
			holdView.tv_adres_goodsId = (TextView) convertView
					.findViewById(R.id.tv_adres_goodsId);
			holdView.rl_bg = (RelativeLayout) convertView
					.findViewById(R.id.rl_bg);

			holdView.im_penselId.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					onclick.myClick(v, 1);
				}
			});
			holdView.rl_bg.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					onclick.myClick(v, 2);
				}
			});

			convertView.setTag(holdView);

		} else {
			holdView = (HoldView) convertView.getTag();
		}
		HashMap item = (HashMap) arrayList.get(position);
		holdView.im_penselId.setTag(item);
		holdView.rl_bg.setTag(item);
		if (item.containsKey(ResponseBean.RESPONSE_CONSIGNEE)) {
			String name = (String) item.get(ResponseBean.RESPONSE_CONSIGNEE);
			holdView.tv_uname_goodsId.setText(name);
		}
		if (item.containsKey(ResponseBean.RESPONSE_PHONE)) {
			String phone = (String) item.get(ResponseBean.RESPONSE_PHONE);
			holdView.tv_phonGoodsId.setText(phone);
		}
		if (item.containsKey(ResponseBean.RESPONSE_ZITI_NAME)) {
			String address = (String) item.get(ResponseBean.RESPONSE_ZITI_NAME);
			holdView.tv_adres_goodsId.setText(address);
		}
		return convertView;
	}

	class HoldView {
		ImageView im_penselId;
		TextView tv_uname_goodsId, tv_phonGoodsId, tv_adres_goodsId;
		RelativeLayout rl_bg;
	}

}
