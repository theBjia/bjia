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
import com.llkj.newbjia.R;
import com.llkj.newbjia.bean.ResponseBean;
import com.llkj.newbjia.http.UrlConfig;
import com.llkj.newbjia.utils.FinalBitmapUtil;
import com.llkj.newbjia.utils.StringUtil;

public class MyCartAdapter extends BaseAdapter {

	private LayoutInflater inflater;

	private ArrayList arrayList;
	private Context context;
	private FinalBitmapUtil bitmapUtil;
	private Myclick myclick;

	// 状态数组，用于跟踪ListView中每一个CheckedBox的选中状态
	// private ArrayList<Boolean> mCheckedStates = new ArrayList<Boolean>();

	public MyCartAdapter(Context context, ArrayList list, Myclick clicker) {
		this.inflater = LayoutInflater.from(context);
		this.context = context;

		setData(list);
		bitmapUtil = FinalBitmapUtil.create(context);
		this.myclick = clicker;

	}

	//
	// public void setBooleans(ArrayList list) {
	// if (list != null && list.size() > 0) {
	// for (int i = 0; i < list.size(); ++i) {
	// mCheckedStates.add(true);
	// }
	// }
	// }
	//
	// public void removeBooleans(int position) {
	// mCheckedStates.remove(position);
	// }

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

	public double getTotal() {
		double total = 0;
		String money = "", number = "";
		for (int i = 0; i < arrayList.size(); i++) {
			HashMap map = (HashMap) arrayList.get(i);
			if (map.containsKey(ResponseBean.RESPONSE_GOODS_PRICE)) {
				money = map.get(ResponseBean.RESPONSE_GOODS_PRICE).toString();
			}
			if (map.containsKey(ResponseBean.RESPONSE_GOODS_NUMBER)) {
				number = map.get(ResponseBean.RESPONSE_GOODS_NUMBER).toString();

			}
			// if (mCheckedStates.get(i)) {
			// total += Double.parseDouble(StringUtil.removeBiFuhao(money))
			// * Double.parseDouble(number);
			// }
			total += Double.parseDouble(StringUtil.removeBiFuhao(money))
					* Double.parseDouble(number);
		}
		return total;
	}

	public ArrayList getArrayList() {
		// ArrayList newList = new ArrayList();
		// for (int i = 0; i < mCheckedStates.size(); i++) {
		// if (mCheckedStates.get(i)) {
		// newList.add(arrayList.get(i));
		// }
		// }
		return arrayList;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final HoldView holdView;
		HashMap<String,String> item = (HashMap) arrayList.get(position);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_my_cart, null);
			holdView = new HoldView();

			holdView.tv_goods_price = (TextView) convertView
					.findViewById(R.id.tv_goods_price);
			holdView.tv_delete = (ImageView) convertView
					.findViewById(R.id.tv_delete);
			holdView.tv_goods_name = (TextView) convertView
					.findViewById(R.id.tv_goods_name);
			holdView.tv_number = (TextView) convertView
					.findViewById(R.id.tv_number);
			holdView.iv_goods_img = (ImageView) convertView
					.findViewById(R.id.iv_goods_img);
			// holdView.iv_isselected = (ImageView) convertView
			// .findViewById(R.id.iv_isselected);
			holdView.rl_bg = (RelativeLayout) convertView
					.findViewById(R.id.rl_bg);
			holdView.iv_jian = (ImageView) convertView
					.findViewById(R.id.iv_jian);
			holdView.iv_add = (ImageView) convertView.findViewById(R.id.iv_add);
			holdView.iv_add.setOnClickListener(new OnClickListener() {
				HashMap<String,String> item2 = (HashMap) arrayList.get(position);
				@Override
				public void onClick(View v) {
					int intnumber = Integer.parseInt(holdView.tv_number
							.getText().toString());
					intnumber++;
					holdView.tv_number.setText(intnumber + "");
					//v.setTag(holdView.tv_number.getTag());
					if (item2.containsKey(ResponseBean.RESPONSE_GOODS_NUMBER)) {
						item2.put(ResponseBean.RESPONSE_GOODS_NUMBER, intnumber + "");
					}
					v.setTag(item2);
					myclick.myClick(v, 1);
				}
			});
			holdView.iv_jian.setOnClickListener(new OnClickListener() {
				HashMap<String,String> item3 = (HashMap) arrayList.get(position);
				@Override
				public void onClick(View v) {
					int intnumbers = Integer.parseInt(holdView.tv_number
							.getText().toString());
					if (intnumbers > 1)
						intnumbers--;
					holdView.tv_number.setText(intnumbers + "");
					//TODO change the number.
					if (item3.containsKey(ResponseBean.RESPONSE_GOODS_NUMBER)) {
						item3.put(ResponseBean.RESPONSE_GOODS_NUMBER, intnumbers + "");
					}
					v.setTag(item3);
					myclick.myClick(v, 1);
				}
			});
			holdView.tv_delete.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// change the number.
					myclick.myClick(v, 2);
				}
			});

			holdView.iv_goods_img.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View goodsView) {
				
					myclick.myClick(goodsView, 4);
					
				}
			});

			// holdView.iv_isselected.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// // CheckBox cb = (CheckBox) v.getTag();
			// int position = (Integer) v.getTag();
			// if (mCheckedStates.get(position)) {
			// ((ImageView) v).setImageResource(R.drawable.gray_gou);
			// mCheckedStates.set(position, false);
			//
			// } else {
			// ((ImageView) v).setImageResource(R.drawable.grenn_gou);
			// mCheckedStates.set(position, true);
			// }
			// myclick.myClick(v, 3);
			//
			// }
			// });

			convertView.setTag(holdView);
		} else {
			holdView = (HoldView) convertView.getTag();
		}
		// 选上的显示状态
		// if (mCheckedStates.get(position)) {
		// holdView.iv_isselected.setImageResource(R.drawable.grenn_gou);
		//
		// } else {
		// holdView.iv_isselected.setBackgroundResource(R.drawable.gray_gou);
		//
		// }


		holdView.tv_number.setTag(item);
		holdView.tv_delete.setTag(item);
		holdView.iv_goods_img.setTag(item);
		
		String Money = "", number = "";
		if (item.containsKey(ResponseBean.RESPONSE_GOODS_PRICE)) {
			Money = item.get(ResponseBean.RESPONSE_GOODS_PRICE).toString();
			holdView.tv_goods_price.setText(Money);
		}
		if (item.containsKey(ResponseBean.RESPONSE_GOODS_NAME)) {
			String name = item.get(ResponseBean.RESPONSE_GOODS_NAME).toString();
			
			if(name.length() > 7){
				holdView.tv_goods_name.setText(name.substring(0, 6) + "...");
			}else
			{
				holdView.tv_goods_name.setText(name);
			}
			
		}
		if (item.containsKey(ResponseBean.RESPONSE_GOODS_NUMBER)) {
			number = item.get(ResponseBean.RESPONSE_GOODS_NUMBER).toString();
			holdView.tv_number.setText(number);
		}
		if (item.containsKey(ResponseBean.RESPONSE_GOODS_IMG)) {
			String img = item.get(ResponseBean.RESPONSE_GOODS_IMG).toString();
			if (!StringUtil.isEmpty(img)) {
				bitmapUtil.displayForPicture(holdView.iv_goods_img,
						UrlConfig.ROOT_URL_TWO + img);
			} else {
				holdView.iv_goods_img.setImageResource(R.drawable.ic_launcher);
			}
		}

		// holdView.iv_isselected.setTag(position);
		// // 点击改变选中状态
		//
		// holdView.tv_xiaoji.setText("小计： ￥"
		// + (Double.parseDouble(StringUtil.removeBiFuhao(Money)) * Double
		// .parseDouble(number)) + "元");

		return convertView;
	}

	class HoldView {
		ImageView iv_jian, iv_add;
		ImageView tv_delete;
		TextView tv_goods_price, tv_goods_name, tv_number;
		ImageView iv_goods_img, iv_isselected;
		RelativeLayout rl_bg;
	}

	public interface Myclick {
		void myClick(View v, int type);

		void myLongClick(View v, int type);
	}
}
