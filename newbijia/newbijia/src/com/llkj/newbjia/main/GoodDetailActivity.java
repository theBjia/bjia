package com.llkj.newbjia.main;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.llkj.newbjia.BaseActivity;
import com.llkj.newbjia.MyApplication;
import com.llkj.newbjia.R;
import com.llkj.newbjia.bean.KeyBean;
import com.llkj.newbjia.bean.ResponseBean;
import com.llkj.newbjia.bean.UserInfoBean;
import com.llkj.newbjia.http.PoCService;
import com.llkj.newbjia.shoppingcart.WriteOrderActivity;
import com.llkj.newbjia.utils.FinalBitmapUtil;
import com.llkj.newbjia.utils.ObjectUtils;
import com.llkj.newbjia.utils.StringUtil;
import com.llkj.newbjia.utils.ToastUtil;

/** 团购 */
public class GoodDetailActivity extends BaseActivity implements OnClickListener {
	private ViewPager guidePages;
	private ArrayList<View> viewList = new ArrayList<View>();
	private ImageView[] imageViews;
	private ArrayList<String> urls;
	private FinalBitmapUtil fb;
	private int currentItem;
	private TextView tv_ivnmus, tv_Offered, tv_goods_name, tv_time,
			tv_xiangounum, tv_zengsongjifen, tv_dangqianjiage, tv_jietijiage,
			tv_nums, tv_price, tv_shangpinshuxing, tv_tuangoushuoming,
			textView10;
	private EditText et_nums;
	private int nums, mRequestId, mPurchaseBuyId;
	// 限购数量 restrict_amount 赠送积分gift_integral 价格阶梯 price_ladder 团购说明 act_desc
	// 当前价 formated_cur_price
	private String strnums, group_buy_id, goods_img, goods_name, start_time,
			end_time, restrict_amount, gift_integral, act_desc, goods_id,
			formated_cur_price, uid, end_timecuo;
	private RelativeLayout rl_goods_info;
	private Intent bigIntent;

	private ArrayList price_ladder, properties;
	private StringBuilder sbb;
	private ScheduledExecutorService scheduledExecutorService;
	private boolean isBuyed = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_goods_detail);
		setTitle(R.string.goodsdetail, true, R.string.kongzifuchuan, false,
				R.string.kongzifuchuan);
		initView();
		initListener();
		initData();
		uid = UserInfoBean.getUserInfo(this).getUid();
		if (!StringUtil.isEmpty(uid)) {
			String ids = ObjectUtils.readObject("goodids.out") + "";
			sbb.append(ids);
		}
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

	}

	class MyTask implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			final String timecontent = com.llkj.newbjia.utils.TimeUtils
					.getTimeSpacing(System.currentTimeMillis() + "",
							end_timecuo);
			if ((System.currentTimeMillis() + "").equals(end_timecuo)) {
				isBuyed = false;
				MyApplication.handler.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						textView10.setText("已结束");
						tv_Offered.setVisibility(View.INVISIBLE);
					}
				});
			} else {
				isBuyed = true;
				MyApplication.handler.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						textView10.setText(timecontent);
						tv_Offered.setVisibility(View.VISIBLE);
					}
				});
			}

			// LogUtil.e(timecontent);
		}

	}

	private void initView() {
		guidePages = (ViewPager) findViewById(R.id.guidePages);
		tv_ivnmus = (TextView) findViewById(R.id.tv_ivnmus);
		et_nums = (EditText) findViewById(R.id.et_nums);
		rl_goods_info = (RelativeLayout) findViewById(R.id.rl_goods_info);
		tv_Offered = (TextView) findViewById(R.id.tv_Offered);
		tv_goods_name = (TextView) findViewById(R.id.tv_goods_name);
		tv_time = (TextView) findViewById(R.id.tv_time);
		tv_xiangounum = (TextView) findViewById(R.id.tv_xiangounum);
		tv_zengsongjifen = (TextView) findViewById(R.id.tv_zengsongjifen);
		tv_dangqianjiage = (TextView) findViewById(R.id.tv_dangqianjiage);
		tv_jietijiage = (TextView) findViewById(R.id.tv_jietijiage);
		tv_nums = (TextView) findViewById(R.id.tv_nums);
		tv_price = (TextView) findViewById(R.id.tv_price);
		tv_shangpinshuxing = (TextView) findViewById(R.id.tv_shangpinshuxing);
		tv_tuangoushuoming = (TextView) findViewById(R.id.tv_tuangoushuoming);

		textView10 = (TextView) findViewById(R.id.textView10);

	}

	private void initListener() {
		guidePages.setOnPageChangeListener(new NavigationPageChangeListener());

		rl_goods_info.setOnClickListener(this);
		tv_Offered.setOnClickListener(this);
	}

	private void initData() {
		fb = FinalBitmapUtil.create(this);
		sbb = new StringBuilder("");
		bigIntent = getIntent();
		if (bigIntent.hasExtra("group_buy_id")) {
			group_buy_id = bigIntent.getStringExtra("group_buy_id");

		}
		if (bigIntent.hasExtra("goods_img")) {
			goods_img = bigIntent.getStringExtra("goods_img");

		}
		urls = new ArrayList<String>();
		urls.add(goods_img);
		fillGuanggao(urls);
		if (StringUtil.isNetworkConnected(this)) {
			if (!StringUtil.isEmpty(group_buy_id)) {
				mRequestId = mRequestManager.purchaseDesc(group_buy_id, true);
			}
		} else {
			ToastUtil.makeShortText(this, R.string.no_wangluo);
		}

	}

	public void fillGuanggao(ArrayList arrayList) {
		for (int i = 0; i < arrayList.size(); i++) {
			ImageView iv = new ImageView(this);
			iv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT));

			fb.displayForPicture(iv, arrayList.get(i) + "");
			iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
			iv.setTag(i);
			iv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub

				}
			});
			viewList.add(iv);
		}
		tv_ivnmus.setText("1/" + viewList.size());
		guidePages.setAdapter(new MyViewPagerAdapter(viewList));
		imageViews = new ImageView[arrayList.size()];
		
	}

	class NavigationPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int arg0) {
			ToastUtil.makeLongText(getApplicationContext(), "nihao a ");
	
		}
	}

	public class MyViewPagerAdapter extends PagerAdapter {
		private List<View> mListViews;

		public MyViewPagerAdapter(List<View> mListViews) {
			this.mListViews = mListViews;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(mListViews.get(position));
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(mListViews.get(position), 0);
			return mListViews.get(position);
		}

		@Override
		public int getCount() {
			return mListViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
	}

	@Override
	public void onClick(View arg0) {
		Intent intent;
		switch (arg0.getId()) {
		case R.id.rl_goods_info:
			intent = new Intent(this, GoodsInfoTwoActivity.class);
			intent.putExtra(KeyBean.KEY_ID, goods_id);
			startActivity(intent);

			break;

		case R.id.tv_Offered:
			String number = et_nums.getText() + "";
			if (!StringUtil.isNumber(number)) {
				number = "1";
			}
			ArrayList arrayList = new ArrayList();
			HashMap map = new HashMap();
			map.put("goods_id", goods_id);
			map.put("goods_price", formated_cur_price);
			map.put("goods_name", goods_name);
			map.put("goods_number", number);
			map.put("commodity_packaging", "");
			map.put("goods_weight", "");
			map.put("goods_img", goods_img);
			map.put("group_buy_id", group_buy_id);
			arrayList.add(map);
			intent = new Intent(this, WriteOrderActivity.class);
			intent.putParcelableArrayListExtra("arrayList", arrayList);
			intent.putExtra("isTuanGou", true);
			startActivity(intent);
			break;
		}
	}

	@Override
	public void onRequestFinished(int requestId, int resultCode, Bundle payload) {
		super.onRequestFinished(requestId, resultCode, payload);
		if (resultCode == PoCService.SUCCESS_CODE) {
			if (mRequestId == requestId) {
				int result = payload.getInt(ResponseBean.RESPONSE_STATE);
				// if (result == 1) {
				// 限购数量 restrict_amount 赠送积分gift_integral 价格阶梯 price_ladder
				// 团购说明 act_desc 当前价 formated_cur_price
				restrict_amount = payload
						.getString(ResponseBean.RESPONSE_RESTRICT_AMOUNT);
				gift_integral = payload
						.getString(ResponseBean.RESPONSE_GIFT_INTEGRAL);

				price_ladder = payload
						.getParcelableArrayList(ResponseBean.RESPONSE_PRICE_LADDER);

				properties = payload.getParcelableArrayList("properties");
				act_desc = payload.getString(ResponseBean.RESPONSE_ACT_DESC);
				formated_cur_price = payload
						.getString(ResponseBean.RESPONSE_FORMATED_CUR_PRICE);
				goods_name = payload.getString("goods_name");
				start_time = payload
						.getString(ResponseBean.RESPONSE_START_TIME);
				end_time = payload.getString(ResponseBean.RESPONSE_END_TIME);
				goods_id = payload.getString(ResponseBean.RESPONSE_GOODS_ID);
				tv_goods_name.setText(goods_name);
				tv_time.setText(start_time + "--" + end_time);
				tv_xiangounum.setText(restrict_amount);
				tv_zengsongjifen.setText(gift_integral);
				tv_dangqianjiage.setText(formated_cur_price);
				try {
					end_timecuo = com.llkj.newbjia.utils.TimeUtils
							.getTimestamp(end_time) + "";
					scheduledExecutorService.scheduleAtFixedRate(new MyTask(),
							1000, 1000, TimeUnit.MILLISECONDS);

				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (!sbb.toString().contains(goods_id)) {
					sbb.append(goods_id + ",");
					ObjectUtils.fileSave(this, sbb.toString(), "goodids.out");
				}

				HashMap jietijiage = (HashMap) price_ladder.get(0);
				if (jietijiage.containsKey("formated_price")) {

					tv_jietijiage
							.setText(jietijiage.get("formated_price") + "");
				}
				if (jietijiage.containsKey("amount")) {
					tv_nums.setText(jietijiage.get("amount") + "");
				}
				if (jietijiage.containsKey("price")) {
					tv_price.setText("￥" + jietijiage.get("price") + "");
				}
				StringBuilder sb = new StringBuilder("");
				for (int i = 0; i < properties.size(); i++) {
					HashMap peizhi = (HashMap) properties.get(i);
					if (peizhi.containsKey("name")) {
						String name = peizhi.get("name") + "";
						sb.append(name + ":");
					}
					if (peizhi.containsKey("value")) {
						String value = peizhi.get("value") + "";
						sb.append(value + "  ");
					}
				}
				tv_shangpinshuxing.setText(sb.toString());
				tv_tuangoushuoming.setText(act_desc);
			
			}

		}
	}

	@Override
	protected void onDestroy() {
		
		scheduledExecutorService.shutdown();
		super.onDestroy();
	}
}
