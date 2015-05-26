package com.llkj.newbjia.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.llkj.newbjia.BaseActivity;
import com.llkj.newbjia.MainActivity;
import com.llkj.newbjia.R;
import com.llkj.newbjia.adpater.GoodsDetailAdapter;
import com.llkj.newbjia.bean.KeyBean;
import com.llkj.newbjia.bean.ResponseBean;
import com.llkj.newbjia.bean.UserInfoBean;
import com.llkj.newbjia.chat.Constants;
import com.llkj.newbjia.customview.MyActivity;
import com.llkj.newbjia.customview.MyGridView;
import com.llkj.newbjia.customview.MyWebView;
import com.llkj.newbjia.customview.PagerScrollView;
import com.llkj.newbjia.http.PoCService;
import com.llkj.newbjia.shoppingcart.MyCartActivity;
import com.llkj.newbjia.shoppingcart.SelfDesionActivity;
import com.llkj.newbjia.utils.FinalBitmapUtil;
import com.llkj.newbjia.utils.ObjectUtils;
import com.llkj.newbjia.utils.StringUtil;
import com.llkj.newbjia.utils.ToastUtil;

public class GoodDetailTwoActivity extends BaseActivity implements
		OnClickListener {
	public final static int PAGE_MAIN = 1;
	public final static int PAGE_CHAT = 2;
	public final static int PAGE_SORT = 3;
	public final static int PAGE_MY_BJIA = 4;
	public final static int PAGE_SHOPPING_CART = 5;

	private ViewPager guidePages;
	private ViewGroup anim_mask_layout;
	private ImageView img1, tv_shouchang, iv_fanhui, iv_liebiao;
	private ImageView iv_main_page, iv_sort_page, iv_chat_page,
			iv_mybijia_page;

	private ImageView iv_goods_add, iv_goods_minus;
	private WebView wv_goods_detail_info;
	private TextView tv_goods_add_number;
	private TextView tv_ivnmus, tv_peisongzhi, tv_shifouxianhuo, tv_number;
	private PagerScrollView scroll;
	private RelativeLayout rl_good_detail_info, rl_gouwuche, rl_add_cart,
			rl_place;
	private LinearLayout rl_my;
	private TextView tv_goods_name, tv_shichangjia, tv_bijiajia, tv_zhongliang,
			tv_baozhuang;

	private ArrayList<View> viewList = new ArrayList<View>();
	private ArrayList urls;
	private FinalBitmapUtil fb;
	private int currentItem, mRequestId, mSetAttentionId, mCancleAttentionId,
			mAddCartId, mDefaultAddressId, mJudgeAvailableId, mShopCartList;
	private GoodsDetailAdapter adapter;
	private ArrayList arrayList;
	private Intent bigIntent;
	private String id, is_attention, uid, zitidianId, zitidianName;
	private boolean isShouchang;// 收藏
	private StringBuilder sb;
	private String numbers;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_goods_detail_two);

		initView();
		initListener();
		initData();

		scroll.smoothScrollTo(0, 0);

		uid = UserInfoBean.getUserInfo(this).getUid();
		if (!StringUtil.isEmpty(uid)) {
			String ids = ObjectUtils.readObject("goodids.out") + "";
			sb.append(ids);
		}

		if (StringUtil.isNetworkConnected(this)) {
			if (!StringUtil.isEmpty(id)) {
				mRequestId = mRequestManager.goodDesc(id, UserInfoBean
						.getUserInfo(this).getUid(), true);
			}
		} else {
			ToastUtil.makeShortText(this, R.string.no_wangluo);

		}

	}

	private void initView() {
		guidePages = (ViewPager) findViewById(R.id.guidePages);
		tv_ivnmus = (TextView) findViewById(R.id.tv_ivnmus);

		scroll = (PagerScrollView) findViewById(R.id.pslv_content);
		rl_gouwuche = (RelativeLayout) findViewById(R.id.rl_gouwuche);
		rl_add_cart = (RelativeLayout) findViewById(R.id.rl_add_cart);

		rl_good_detail_info = (RelativeLayout) findViewById(R.id.rl_goods_detail_info);

		tv_goods_name = (TextView) findViewById(R.id.tv_goods_name);
		tv_shichangjia = (TextView) findViewById(R.id.tv_shichangjia);
		tv_bijiajia = (TextView) findViewById(R.id.tv_bijiajia);
		tv_zhongliang = (TextView) findViewById(R.id.tv_zhongliang);
		tv_baozhuang = (TextView) findViewById(R.id.tv_baozhuang);
		tv_goods_add_number = (TextView) findViewById(R.id.tv_goods_number);
		tv_shouchang = (ImageView) findViewById(R.id.tv_shouchang);

		iv_fanhui = (ImageView) findViewById(R.id.iv_fanhui);
		iv_liebiao = (ImageView) findViewById(R.id.iv_liebiao);
		iv_main_page = (ImageView) findViewById(R.id.iv_main_page);
		iv_sort_page = (ImageView) findViewById(R.id.iv_sort_page);
		iv_chat_page = (ImageView) findViewById(R.id.iv_chat_page);
		iv_mybijia_page = (ImageView) findViewById(R.id.iv_mybijia_page);
		iv_goods_add = (ImageView) findViewById(R.id.iv_goods_add);
		iv_goods_minus = (ImageView) findViewById(R.id.iv_goods_minus);
		wv_goods_detail_info = (WebView) findViewById(R.id.wv_goods_detail_info);

		rl_place = (RelativeLayout) findViewById(R.id.rl_place);
		rl_my = (LinearLayout) findViewById(R.id.myrl);
		tv_peisongzhi = (TextView) findViewById(R.id.tv_peisongzhi);
		tv_shifouxianhuo = (TextView) findViewById(R.id.tv_shifouxianhuo);
		//
		tv_number = (TextView) findViewById(R.id.tv_number);
		anim_mask_layout = createAnimLayout();
		img1 = (ImageView) GoodDetailTwoActivity.this.findViewById(R.id.img);
	}

	private void initListener() {
		guidePages.setOnPageChangeListener(new NavigationPageChangeListener());
		rl_gouwuche.setOnClickListener(this);
		rl_good_detail_info.setOnClickListener(this);
		tv_shouchang.setOnClickListener(this);
		iv_fanhui.setOnClickListener(this);
		iv_liebiao.setOnClickListener(this);
		iv_main_page.setOnClickListener(this);
		iv_sort_page.setOnClickListener(this);
		iv_chat_page.setOnClickListener(this);
		iv_mybijia_page.setOnClickListener(this);
		iv_goods_add.setOnClickListener(this);
		iv_goods_minus.setOnClickListener(this);

		rl_add_cart.setOnClickListener(this);
		rl_place.setOnClickListener(this);
		rl_my.setOnClickListener(this);

	}

	private void initData() {
		sb = new StringBuilder("");
		bigIntent = getIntent();
		fb = FinalBitmapUtil.create(this);
		urls = new ArrayList<String>();
		if (bigIntent.hasExtra("id")) {
			id = bigIntent.getStringExtra("id");
		}

		// 表示不支持js，如果想让java和js交互或者本身希望js完成一定的功能请把false改为true。
		wv_goods_detail_info.getSettings().setJavaScriptEnabled(false);
		// 设置是否支持缩放，我这里为false，默认为true。
		wv_goods_detail_info.getSettings().setSupportZoom(true);
		// 设置是否显示缩放工具，默认为false。
		wv_goods_detail_info.getSettings().setBuiltInZoomControls(true);
		// 一般很少会用到这个，用WebView组件显示普通网页时一般会出现横向滚动条，这样会导致页面查看起来非常不方便。
		/**
		 * LayoutAlgorithm是一个枚举，用来控制html的布局，总共有三种类型： NORMAL：正常显示，没有渲染变化。
		 * SINGLE_COLUMN：把所有内容放到WebView组件等宽的一列中。
		 * NARROW_COLUMNS：可能的话，使所有列的宽度不超过屏幕宽度。
		 */
		wv_goods_detail_info.getSettings().setLayoutAlgorithm(
				LayoutAlgorithm.SINGLE_COLUMN);
		// 设置默认的字体大小，默认为16，有效值区间在1-72之间。
		wv_goods_detail_info.getSettings().setDefaultFontSize(18);
		wv_goods_detail_info.setWebViewClient(new MyWebViewClient());
		if (StringUtil.isNetworkConnected(this)) {
			if (!StringUtil.isEmpty(id)) {
				String url;
				url = "http://www.bjia.co/api/appIndex.php?action=goodInfo&id="
						+ id;

				
				wv_goods_detail_info.loadUrl(url);

			} else {
				ToastUtil.makeShortText(this, R.string.no_wangluo);
			}

		}

	}

	/**
	 * 加载商品滑动图片
	 * 
	 * @param arrayList
	 */
	public void fillGuanggao(ArrayList arrayList) {
		for (int i = 0; i < arrayList.size(); i++) {
			ImageView iv = new ImageView(this);
			iv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT));
			fb.displayForPicture(iv, arrayList.get(i) + "");
			iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
			iv.setTag(i);
			viewList.add(iv);
		}
		tv_ivnmus.setText("1/" + viewList.size());
		guidePages.setAdapter(new MyViewPagerAdapter(viewList));

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
			currentItem = arg0;
			tv_ivnmus.setText((currentItem + 1) + "/" + viewList.size());

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

			mListViews.get(position).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					Intent intent = new Intent(getApplicationContext(),
							MyActivity.class);
					intent.putStringArrayListExtra("url", urls);
					startActivity(intent);

				}
			});

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
		int goods_number;
		String num = (tv_goods_add_number.getText()).toString();
		goods_number = Integer.parseInt(num);
		if (StringUtil.isNumber(num)) {
			goods_number = StringUtil.toInt(num, 1);
		} else {
			goods_number = 1;
		}

		switch (arg0.getId()) {
		case R.id.iv_main_page:
			Intent intent_main = new Intent();
			intent_main.setClass(this, MainActivity.class);
			intent_main.putExtra("page_type", PAGE_MAIN);
			startActivity(intent_main);
			break;
		case R.id.iv_chat_page:
			Intent intent_chat = new Intent();
			intent_chat.setClass(this, MainActivity.class);
			intent_chat.putExtra("page_type", PAGE_CHAT);
			startActivity(intent_chat);
			break;
		case R.id.iv_sort_page:
			Intent intent_sort = new Intent();
			intent_sort.setClass(this, MainActivity.class);
			intent_sort.putExtra("page_type", PAGE_SORT);
			startActivity(intent_sort);
			break;
		case R.id.iv_mybijia_page:
			Intent intent_mybijia = new Intent();
			intent_mybijia.setClass(this, MainActivity.class);
			intent_mybijia.putExtra("page_type", PAGE_MY_BJIA);
			startActivity(intent_mybijia);
			break;

		case R.id.iv_goods_add:
			++goods_number;
			tv_goods_add_number.setText(goods_number + "");
			break;

		case R.id.iv_goods_minus:
			if (goods_number <= 1) {
				goods_number = 1;
			} else {
				--goods_number;
			}
			tv_goods_add_number.setText(goods_number + "");
			break;

		case R.id.rl_goods_detail_info:

			intent = new Intent(this, GoodsInfoTwoActivity.class);

			intent.putExtra(KeyBean.KEY_ID, id);

			startActivity(intent);

			break;

		case R.id.rl_add_cart:

			if (StringUtil.isEmpty(uid)) {

				ToastUtil.makeLongText(getApplicationContext(), "请先登录");

				return;
			}
			String shifouyouhuo = tv_shifouxianhuo.getText() + "";

//			if ("有货".equals(shifouyouhuo)) {
//
//				mAddCartId = mRequestManager
//
//				.shopCartAdd(tv_goods_add_number.getText().toString(), uid, id,
//						"", true);
//				// TODO
//				if (urls != null && urls.size() != 0) {
//					fb.displayForHeader(img1, urls.get(currentItem) + "");
//				}
//
//				setAnim(img1);
//				img1.setVisibility(View.GONE);
//			} else {
//
//				ToastUtil.makeLongText(this, "此自提点无货");
//
//			}
			
			if (shifouyouhuo != null && shifouyouhuo != "" ) {

				mAddCartId = mRequestManager

				.shopCartAdd(tv_goods_add_number.getText().toString(), uid, id,
						"", true);
				// TODO
				if (urls != null && urls.size() != 0) {
					fb.displayForHeader(img1, urls.get(currentItem) + "");
				}

				setAnim(img1);
				img1.setVisibility(View.GONE);
			} else {

				ToastUtil.makeLongText(this, "此自提点无货");

			}

			break;
		case R.id.rl_gouwuche:

			if (StringUtil.isEmpty(uid)) {

				ToastUtil.makeLongText(this, "请先登录");

				return;
			}
			intent = new Intent(Constants.SWITCHCART);

			sendBroadcast(intent);

			// 错误原因，自己定义了一个新的购物车Activity，而之前那个是在MainActivity的Fragment中。
			// MyCartActivity 未进行使用。
			// Intent intent2 = new Intent(this, MyCartActivity.class);

			Intent intent2 = new Intent(this, MainActivity.class);
			intent2.putExtra("page_type", PAGE_SHOPPING_CART);

			startActivity(intent2);

			break;

		case R.id.rl_place:

			if (StringUtil.isEmpty(uid)) {

				ToastUtil.makeLongText(this, "请先登录");

				return;
			}

			Intent intentt = new Intent(this, SelfDesionActivity.class);

			startActivityForResult(intentt, 100);

			break;

		case R.id.iv_fanhui:
			finish();
			break;
		case R.id.iv_liebiao:
			if (rl_my.getVisibility() == View.INVISIBLE) {
				rl_my.setVisibility(View.VISIBLE);
			} else {
				rl_my.setVisibility(View.INVISIBLE);
			}

			break;

		case R.id.tv_shouchang:

			if (StringUtil.isEmpty(uid)) {

				ToastUtil.makeLongText(this, "请先登录");

				return;
			}
			if (isShouchang) {

				mCancleAttentionId = mRequestManager.goodAttention(id,
						UserInfoBean.getUserInfo(this).getUid(), "2", true);

			} else {

				mSetAttentionId = mRequestManager.goodAttention(id,
						UserInfoBean.getUserInfo(this).getUid(), "1", true);

			}

			break;

		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data != null) {

			switch (requestCode) {

			case 100:
				if (data.hasExtra(ResponseBean.RESPONSE_NAME)) {
					zitidianName = data
							.getStringExtra(ResponseBean.RESPONSE_NAME);
					tv_peisongzhi.setText("配  送  至：" + zitidianName);
				}
				if (data.hasExtra(ResponseBean.RESPONSE_ID)) {

					zitidianId = data.getStringExtra(ResponseBean.RESPONSE_ID);
				}
				if (!StringUtil.isEmpty(zitidianId)) {

					mJudgeAvailableId = mRequestManager.judgeAvailable(
							zitidianId, id, true);
				}
				break;
			case 200:

				Intent dataa = new Intent();

				dataa.putExtra(Constants.REFRESH, true);

				setResult(RESULT_OK, dataa);

				this.finish();
				break;

			default:
				break;
			}
		}
	}

	@Override
	public void onRequestFinished(int requestId, int resultCode, Bundle payload) {
		super.onRequestFinished(requestId, resultCode, payload);
		if (resultCode == PoCService.SUCCESS_CODE) {
			if (mRequestId == requestId) {
				int result = payload.getInt(ResponseBean.RESPONSE_STATE);
				if (result == 1) {
					// 限购数量 restrict_amount 赠送积分gift_integral 价格阶梯 price_ladder
					// 团购说明 act_desc 当前价 formated_cur_price
					// tv_goods_name, tv_shichangjia, tv_bijiajia,
					// tv_zhongliang,
					// tv_baozhuang
					String goods_name = payload
							.getString(ResponseBean.RESPONSE_GOODS_NAME);
					String shop_price_formated = payload
							.getString(ResponseBean.RESPONSE_SPF);
					String market_price = payload
							.getString(ResponseBean.RESPONSE_MARKET_PRICE);
					String goods_number = payload
							.getString(ResponseBean.RESPONSE_GOODS_NUMBER);
					String goods_weight = payload
							.getString(ResponseBean.RESPONSE_GOODS_WEIGHT);

					String commodity_packaging = payload
							.getString(ResponseBean.RESPONSE_COMMODITY_PACKAGING);
					if (commodity_packaging.endsWith("0")) {
						commodity_packaging = "无";
					}
					String goods_id = payload
							.getString(ResponseBean.RESPONSE_GOODS_ID);
					if (!sb.toString().contains(goods_id)) {
						sb.append(goods_id + ",");
						ObjectUtils
								.fileSave(this, sb.toString(), "goodids.out");
					}

					tv_goods_name.setText(goods_name);
					tv_baozhuang.setText(commodity_packaging);
					tv_bijiajia.setText(shop_price_formated);

					tv_shichangjia.setText(market_price);
					tv_zhongliang.setText(goods_weight + "g");
					urls = payload.getParcelableArrayList("pic");
					fillGuanggao(urls);

					is_attention = payload
							.getString(ResponseBean.RESPONSE_IS_ATTENTION);
					if ("1".equals(is_attention)) {
						setShouchang(true);
					} else {
						setShouchang(false);
					}

					if (!StringUtil.isEmpty(uid)) {
						mDefaultAddressId = mRequestManager.defaultAddress(uid,
								true);

					}

				} else {
					String msg = payload
							.getString(ResponseBean.RESPONSE_MESSAGE);
					ToastUtil.makeShortText(this, msg);
				}

			} else if (mCancleAttentionId == requestId) {
				int result = payload.getInt(ResponseBean.RESPONSE_STATE);
				if (result == 1) {
					ToastUtil.makeShortText(this, "取消成功");
					setShouchang(false);
					Intent br = new Intent(Constants.REFRESH_COLLECTION);
					sendBroadcast(br);
				} else {
					String msg = payload
							.getString(ResponseBean.RESPONSE_MESSAGE);
					ToastUtil.makeShortText(this, msg);
				}
			} else if (mSetAttentionId == requestId) {
				int result = payload.getInt(ResponseBean.RESPONSE_STATE);
				if (result == 1) {
					ToastUtil.makeShortText(this, "关注成功");
					setShouchang(true);
					Intent br = new Intent(Constants.REFRESH_COLLECTION);
					sendBroadcast(br);
				} else {
					String msg = payload
							.getString(ResponseBean.RESPONSE_MESSAGE);
					ToastUtil.makeShortText(this, msg);
				}
			} else if (mAddCartId == requestId) {
				int result = payload.getInt(ResponseBean.RESPONSE_STATE);
				if (result == 1) {
					int add_number = StringUtil.toInt(tv_goods_add_number
							.getText().toString());
					int tmp_number = Integer.parseInt(numbers);
					tmp_number += add_number;
					numbers = tmp_number + "";

					tv_number.setText(numbers);
					tv_number.setVisibility(View.VISIBLE);
					tv_goods_add_number.setText("1");
					Intent br = new Intent(Constants.REFRESH_SHOPCART);
					sendBroadcast(br);
					ToastUtil.makeShortText(this, "已加入购物车，请到购物车查看");
				} else {
					String msg = payload
							.getString(ResponseBean.RESPONSE_MESSAGE);
					ToastUtil.makeShortText(this, msg);
				}
			} else if (mDefaultAddressId == requestId) {
				int result = payload.getInt(ResponseBean.RESPONSE_STATE);
				if (result == 1) {
					zitidianId = payload
							.getString(ResponseBean.RESPONSE_ZITI_ID);
					zitidianName = payload
							.getString(ResponseBean.RESPONSE_ZITI_NAME);
					tv_peisongzhi.setText(zitidianName);
					if (!StringUtil.isEmpty(zitidianId)) {
						mJudgeAvailableId = mRequestManager.judgeAvailable(
								zitidianId, id, true);
					}
				} else {
					String msg = payload
							.getString(ResponseBean.RESPONSE_MESSAGE);
					ToastUtil.makeShortText(this, msg);
				}
			} else if (mJudgeAvailableId == requestId) {
				int result = payload.getInt(ResponseBean.RESPONSE_STATE);
				if (result == 2) {
					tv_shifouxianhuo.setText("无货");
				} else if (result == 1) {
					tv_shifouxianhuo.setText("有货");
				} else if (result == 0) {
					tv_shifouxianhuo.setText("失败");
				}
				if (StringUtil.isEmpty(numbers)) {
					mShopCartList = mRequestManager.getShopCartList(uid, true);
				}

			} else if (mShopCartList == requestId) {
				int result = payload.getInt(ResponseBean.RESPONSE_STATE);

				if (result == 1) {
					numbers = payload.getString(ResponseBean.RESPONSE_NUMBER);
					if ("0".equals(numbers)) {
						tv_number.setVisibility(View.INVISIBLE);
					} else {
						tv_number.setVisibility(View.VISIBLE);
					}
					tv_number.setText(numbers);
				}
			}

		}
	}

	@SuppressLint("NewApi")
	public void setShouchang(boolean isShouchang) {
		if (isShouchang) {

			// tv_shouchang.setVisibility(View.INVISIBLE);
			tv_shouchang.setBackground(getResources().getDrawable(
					R.drawable.shoucang));
			this.isShouchang = true;
		} else {

			// tv_shouchang.setVisibility(View.VISIBLE);
			tv_shouchang.setBackground(getResources().getDrawable(
					R.drawable.shoucang_cancled));
			this.isShouchang = false;
		}

	}

	private ViewGroup createAnimLayout() {
		ViewGroup rootView = (ViewGroup) this.getWindow().getDecorView();
		LinearLayout animLayout = new LinearLayout(this);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT);
		animLayout.setLayoutParams(lp);

		animLayout.setBackgroundResource(android.R.color.transparent);
		rootView.addView(animLayout);
		return animLayout;
	}

	private View addViewToAnimLayout(final ViewGroup vg, final View view,
			int[] location) {
		int x = location[0];
		int y = location[1];
		vg.addView(view);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.leftMargin = x;
		lp.topMargin = y;
		view.setLayoutParams(lp);
		return view;
	}

	private void setAnim(View v) {
		Animation mScaleAnimation = new ScaleAnimation(1.5f, 0.1f, 1.5f, 0.1f,
				Animation.RELATIVE_TO_SELF, 0.1f, Animation.RELATIVE_TO_SELF,
				0.1f);
		mScaleAnimation.setDuration(AnimationDuration);
		mScaleAnimation.setFillAfter(true);
		int[] start_location = new int[2];

		img1.setVisibility(View.VISIBLE);
		guidePages.getLocationOnScreen(start_location);

		ViewGroup vg = (ViewGroup) img1.getParent();
		vg.removeView(img1);
		// 将组件添加到我们的动画层上
		View view = addViewToAnimLayout(anim_mask_layout, img1, start_location);
		int[] end_location = new int[2];

		tv_number.getLocationInWindow(end_location);
		// 计算位移
		int endX = end_location[0];
		int endY = end_location[1] - start_location[1];

		Animation mTranslateAnimation = new TranslateAnimation(400, endX, 600,
				endY + 200);// 移动
		mTranslateAnimation.setDuration(AnimationDuration);

		AnimationSet mAnimationSet = new AnimationSet(false);
		// 这块要注意，必须设为false,不然组件动画结束后，不会归位。
		mAnimationSet.setFillAfter(false);
		mAnimationSet.addAnimation(mScaleAnimation);
		mAnimationSet.addAnimation(mTranslateAnimation);
		view.startAnimation(mAnimationSet);

		mTranslateAnimation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {

			}
		});

	}

	/**
	 * 动画播放时间
	 */
	private int AnimationDuration = 2000;

	private class MyWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	}

}
