package com.llkj.newbjia.adpater;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.llkj.newbjia.MyClicker;
import com.llkj.newbjia.R;
import com.llkj.newbjia.bean.ResponseBean;
import com.llkj.newbjia.customview.HorizontalListView;
import com.llkj.newbjia.http.UrlConfig;
import com.llkj.newbjia.main.GoodDetailTwoActivity;
import com.llkj.newbjia.utils.FinalBitmapUtil;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap.Config;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MyOrderAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private ArrayList arrayList;
	private Context context;
	private FinalBitmapUtil finalBitmapUtil;
	private MyClicker myClicker;

	public MyOrderAdapter(Context context, ArrayList list, MyClicker myClicker) {
		this.inflater = LayoutInflater.from(context);
		this.context = context;
		setData(list);
		finalBitmapUtil = FinalBitmapUtil.create(context);
		this.myClicker = myClicker;
	}

	private void setData(ArrayList list) {
		if (null != list) {
			this.arrayList = list;
		} else {
			arrayList = new ArrayList();
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

	// tv_order_sn, tv_goods_amount, tv_add_time, tv_goods_name,
	// tv_weight_page, tv_status, tv_pay, tv_cancle
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		HoldView holdView;

		if (null == convertView) {
			holdView = new HoldView();
			convertView = inflater.inflate(R.layout.item_my_order, null);
			holdView.rl_one = (RelativeLayout) convertView
					.findViewById(R.id.rl_one);
			holdView.rl_two = (RelativeLayout) convertView
					.findViewById(R.id.rl_two);
			holdView.rl_bg = (RelativeLayout) convertView
					.findViewById(R.id.rl_bg);
			holdView.iv_goods_thumb = (ImageView) convertView
					.findViewById(R.id.iv_goods_thumb);
			holdView.tv_order_sn = (TextView) convertView
					.findViewById(R.id.tv_order_sn);
			holdView.tv_goods_amount = (TextView) convertView
					.findViewById(R.id.tv_goods_amount);
			holdView.tv_add_time = (TextView) convertView
					.findViewById(R.id.tv_add_time);
			holdView.tv_goods_name = (TextView) convertView
					.findViewById(R.id.tv_goods_name);
			holdView.tv_weight_page = (TextView) convertView
					.findViewById(R.id.tv_weight_page);
			holdView.tv_status = (TextView) convertView
					.findViewById(R.id.tv_status);
			holdView.tv_pay = (TextView) convertView.findViewById(R.id.tv_pay);
			holdView.tv_cancle_sure = (TextView) convertView
					.findViewById(R.id.tv_cancle_sure);
			holdView.hlv = (HorizontalListView) convertView
					.findViewById(R.id.hlv);
			holdView.tv_sure_shouhuo = (TextView) convertView
					.findViewById(R.id.tv_sure_shouhuo);
			holdView.tv_contactskefu = (TextView) convertView
					.findViewById(R.id.tv_contactskefu);
			holdView.rl_one.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					HashMap mapone = (HashMap) v.getTag();
					if (mapone.containsKey("goods_id")) {
						String id = mapone.get("goods_id") + "";
						Intent intent = new Intent(context,
								GoodDetailTwoActivity.class);
						intent.putExtra("id", id);
						context.startActivity(intent);
					}
				}
			});
			holdView.tv_pay.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					//TODO see.
					myClicker.myClick(v, 1);// 付款
				}
			});
			holdView.tv_sure_shouhuo.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					myClicker.myClick(v, 2);// 确认收货
				}
			});
			holdView.tv_contactskefu.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					myClicker.myClick(v, 3);// 联系客服
				}
			});
			holdView.rl_bg.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					myClicker.myClick(v, 4);// 跳转到详情页
				}
			});
			holdView.tv_cancle_sure.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					myClicker.myClick(v, 5);// 取消订单
				}
			});

			convertView.setTag(holdView);

		} else {

			holdView = (HoldView) convertView.getTag();
		}

		HashMap item = (HashMap) arrayList.get(position);
		holdView.tv_pay.setTag(item);
		holdView.tv_sure_shouhuo.setTag(item);
		holdView.tv_contactskefu.setTag(item);
		holdView.rl_bg.setTag(item);
		holdView.tv_cancle_sure.setTag(item);
		if (item.containsKey("goods_list")) {
			ArrayList goods_list = (ArrayList) item.get("goods_list");
			if (goods_list != null) {
				if (goods_list.size() == 1) {
					holdView.rl_one.setVisibility(View.VISIBLE);
					holdView.rl_two.setVisibility(View.GONE);
					HashMap mapone = (HashMap) goods_list.get(0);
					holdView.rl_one.setTag(mapone);
					String goods_name = "", goods_weight = "", commodity_packaging = "", goods_thumb = "";
					if (mapone.containsKey("goods_name")) {
						goods_name = mapone.get("goods_name") + "";
						holdView.tv_goods_name.setText(goods_name);
					}
					if (mapone.containsKey("goods_weight")) {
						goods_weight = mapone.get("goods_weight") + "";

					}
					if (mapone.containsKey("commodity_packaging")) {
						commodity_packaging = mapone.get("commodity_packaging")
								+ "";
						holdView.tv_weight_page.setText("重量：" + goods_weight
								+ "Kg  包装:" + commodity_packaging);
					}
					if (mapone.containsKey("goods_thumb")) {
						goods_thumb = mapone.get("goods_thumb") + "";
						finalBitmapUtil.displayForPicture(
								holdView.iv_goods_thumb, UrlConfig.ROOT_URL_TWO
										+ goods_thumb);
					}

				} else {
					holdView.rl_one.setVisibility(View.GONE);
					holdView.rl_two.setVisibility(View.VISIBLE);
					MyAdapter myadapter = new MyAdapter(goods_list);
					holdView.hlv.setAdapter(myadapter);
				}
			}

		}
		if (item.containsKey("order_sn")) {
			String order_sn = item.get("order_sn") + "";
			holdView.tv_order_sn.setText(order_sn);
		}
		if (item.containsKey("goods_amount")) {
			String goods_amount = item.get("goods_amount") + "";
			holdView.tv_goods_amount.setText("￥" + goods_amount);
		}
		if (item.containsKey("add_time")) {
			String add_time = item.get("add_time") + "";
			holdView.tv_add_time.setText(add_time);
		}
		if (item.containsKey("status")) {
			String status = item.get("status") + "";
			if ("0".equals(status)) {
				holdView.tv_status.setText("已付款");//
				holdView.tv_cancle_sure.setVisibility(View.INVISIBLE);
				holdView.tv_sure_shouhuo.setVisibility(View.VISIBLE);
				holdView.tv_contactskefu.setVisibility(View.INVISIBLE);
				holdView.tv_pay.setVisibility(View.INVISIBLE);
			} else if ("1".equals(status)) {
				holdView.tv_status.setText("未付款");// 取消 付款两个按钮
				holdView.tv_cancle_sure.setVisibility(View.VISIBLE);
				holdView.tv_sure_shouhuo.setVisibility(View.INVISIBLE);
				holdView.tv_contactskefu.setVisibility(View.INVISIBLE);
				holdView.tv_pay.setVisibility(View.VISIBLE);
			} else if ("2".equals(status)) {
				holdView.tv_status.setText("待收货");// 确认收货按钮
				holdView.tv_cancle_sure.setVisibility(View.INVISIBLE);
				holdView.tv_sure_shouhuo.setVisibility(View.INVISIBLE);
				holdView.tv_contactskefu.setVisibility(View.INVISIBLE);
				holdView.tv_pay.setVisibility(View.INVISIBLE);
			} else if ("3".equals(status)) {
				holdView.tv_status.setText("完成");// 联系客服按钮显示
				holdView.tv_cancle_sure.setVisibility(View.INVISIBLE);
				holdView.tv_sure_shouhuo.setVisibility(View.INVISIBLE);
				holdView.tv_contactskefu.setVisibility(View.VISIBLE);
				holdView.tv_pay.setVisibility(View.INVISIBLE);
			} else if ("4".equals(status)) {
				holdView.tv_status.setText("已取消");//
				holdView.tv_cancle_sure.setVisibility(View.INVISIBLE);
				holdView.tv_sure_shouhuo.setVisibility(View.INVISIBLE);
				holdView.tv_contactskefu.setVisibility(View.VISIBLE);
				holdView.tv_pay.setVisibility(View.INVISIBLE);
			}
		}

		return convertView;
	}

	class MyAdapter extends BaseAdapter {
		ArrayList myList;

		public MyAdapter(ArrayList arrayList) {
			this.myList = arrayList;
		}

		@Override
		public int getCount() {
			
			return myList.size();
		}

		@Override
		public Object getItem(int arg0) {
		
			return myList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			
			HolderView holderview;
			if (arg1 == null) {
				holderview = new HolderView();
				arg1 = inflater.inflate(R.layout.item_image, null);
				holderview.iv_content = (ImageView) arg1
						.findViewById(R.id.iv_content);
				holderview.iv_content.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						
						HashMap mapp = (HashMap) arg0.getTag();
						if (mapp.containsKey("goods_id")) {
							String id = mapp.get("goods_id") + "";
							Intent intent = new Intent(context,
									GoodDetailTwoActivity.class);
							intent.putExtra("id", id);
							context.startActivity(intent);
						}
					}
				});
				arg1.setTag(holderview);
			} else {
				holderview = (HolderView) arg1.getTag();
			}
			HashMap map = (HashMap) myList.get(arg0);
			holderview.iv_content.setTag(map);
			if (map.containsKey(ResponseBean.RESPONSE_GOODS_THUMB)) {
				String goods_thumb = map.get(ResponseBean.RESPONSE_GOODS_THUMB)
						+ "";
				finalBitmapUtil.displayForPicture(holderview.iv_content,
						UrlConfig.ROOT_URL_TWO + goods_thumb);
			}
			return arg1;
		}

	}

	class HolderView {
		ImageView iv_content;
	}

	class HoldView {
		
		ImageView iv_goods_thumb;
		
		TextView tv_order_sn, tv_goods_amount, tv_add_time, tv_goods_name,
				tv_weight_page, tv_status, tv_pay, tv_cancle_sure, tv_sure_shouhuo,
				tv_contactskefu;
		HorizontalListView hlv;
		
		RelativeLayout rl_one, rl_two, rl_bg;
		
	}

}
