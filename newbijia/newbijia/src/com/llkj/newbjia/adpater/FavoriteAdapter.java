package com.llkj.newbjia.adpater;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

public class FavoriteAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private ArrayList arrayList;
	private Context context;
	private Map<Integer, View> map = new HashMap<Integer, View>();
	private FinalBitmapUtil bitmapUtil;
	private MyClicker myClicker;
	private HoldView holdView;

	public FavoriteAdapter(Context context, ArrayList list, MyClicker clicker) {
		inflater = LayoutInflater.from(context);
		this.context = context;
		this.arrayList = list;
		setData(list);
		bitmapUtil = FinalBitmapUtil.create(context);
		this.myClicker = clicker;
	}

	private void setData(ArrayList list) {
		if (null != list) {
			this.arrayList = list;
		} else {
			this.arrayList = new ArrayList();
		}
	}

	public void notifyDataSetChanged(ArrayList list) {
		setData(list);
		map.clear();
		this.notifyDataSetChanged();
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
		View pv;
		if (null == map.get(position)) {
			pv = inflater.inflate(R.layout.favorite_item, null);
			holdView = new HoldView();
			holdView.im_Photo = (ImageView) pv
					.findViewById(R.id.im_Favorite_Photo);
			holdView.iv_delete = (ImageView) pv
					.findViewById(R.id.iv_favor_delete);
			holdView.tv_Name = (TextView) pv
					.findViewById(R.id.tv_Favorite_Name);
			holdView.tv_Weight = (TextView) pv
					.findViewById(R.id.tv_Favorite_Weight);
			holdView.tv_Pack = (TextView) pv
					.findViewById(R.id.tv_Favorite_Pack);
			holdView.tv_money = (TextView) pv
					.findViewById(R.id.tv_Favorite_money);
			holdView.rl_bg = (RelativeLayout) pv
					.findViewById(R.id.rl_Favorite_bg);
			holdView.rl_bg.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					myClicker.myClick(v, 1);
				}
			});

			holdView.im_Photo.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					myClicker.myClick(v, 2);
				}
			});
			holdView.iv_delete.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO send a message to delete it from array.
					myClicker.myClick(v, 0);
//					arrayList.remove(position);
//					notifyDataSetChanged();
				}
			});
			HashMap item = (HashMap) arrayList.get(position);
			holdView.rl_bg.setTag(item);
			//TODO
			holdView.iv_delete.setTag(R.id.tag_favor_list_info,item);
			holdView.iv_delete.setTag(R.id.tag_favor_position, position);
			if (item.containsKey(ResponseBean.RESPONSE_GOODS_NAME)) {
				String name = item.get(ResponseBean.RESPONSE_GOODS_NAME)
						.toString();
				holdView.tv_Name.setText(name);
			}
			if (item.containsKey(ResponseBean.RESPONSE_GOODS_WEIGHT)) {
				String weight = item.get(ResponseBean.RESPONSE_GOODS_WEIGHT)
						.toString();
				holdView.tv_Weight.setText(weight + "g");
			}
			if (item.containsKey(ResponseBean.RESPONSE_SHOP_PRICE)) {
				String money = item.get(ResponseBean.RESPONSE_SHOP_PRICE)
						.toString();
				holdView.tv_money.setText("￥" + money + "元");
			}
			if (item.containsKey(ResponseBean.RESPONSE_COMMODITY_PACKAGING)) {
				String pack = item.get(
						ResponseBean.RESPONSE_COMMODITY_PACKAGING).toString();
				holdView.tv_Pack.setText(pack);
			}
			if (item.containsKey(ResponseBean.RESPONSE_GOODS_IMG)) {
				String img = item.get(ResponseBean.RESPONSE_GOODS_IMG)
						.toString();
				bitmapUtil.displayForPicture(holdView.im_Photo, img);
			}
			pv.setTag(holdView);
			map.put(position, pv);
		} else {
			pv = map.get(position);
			holdView = (HoldView) pv.getTag();
		}

		return pv;
	}

	class HoldView {
		ImageView im_Photo, iv_delete;
		TextView tv_Name, tv_Weight, tv_Pack, tv_money;
		RelativeLayout rl_bg;
	}
	
}
