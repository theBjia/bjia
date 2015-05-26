package com.llkj.newbjia.main;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

import com.amap.api.services.core.v;
import com.llkj.newbjia.BaseFragment;
import com.llkj.newbjia.MainActivity;
import com.llkj.newbjia.R;
import com.llkj.newbjia.adpater.ImgAdapter;
import com.llkj.newbjia.bean.ResponseBean;
import com.llkj.newbjia.bean.UserInfoBean;
import com.llkj.newbjia.customview.MyGallery;
import com.llkj.newbjia.customview.MyWebView;
import com.llkj.newbjia.customview.ScrollSwipeRefreshLayout;
import com.llkj.newbjia.fenlei.FenleiActivity;
import com.llkj.newbjia.http.PoCService;
import com.llkj.newbjia.login.LoginReginActivity;
import com.llkj.newbjia.utils.FinalBitmapUtil;
import com.llkj.newbjia.utils.StringUtil;
import com.llkj.newbjia.utils.ToastUtil;

@SuppressLint("SetJavaScriptEnabled")
public class MainFragment extends BaseFragment implements OnClickListener,
		OnRefreshListener {
	private ImageView iv_top_left, iv_top_center;
	private ImageView ll_search, ll_fenlei;
	private ImageView bt_search;
	private ImageView iv_back_normal;
	private EditText et_content;
	private LinearLayout ll_search_menu;
	private LinearLayout ll_search_and_sort;

	private TextView tv_number;

	private RelativeLayout rl_gouwuche;

	// private MyWebView webView;
	// private ScrollSwipeRefreshLayout refreshLayout;
	private MyGallery gallery = null;
	private LinearLayout MyLy;
	// private LinearLayout ll_focus_indicator_container;

	private Handler mHandler = new Handler();

	private ArrayList<ImageView> portImg;
	private int preSelImgIndex = 0;
	private int mShopCartList;
	private String uidString;
	private String searchString;

	private ImageView iv_hongti, iv_xilanhua, iv_jiliu, iv_yanmai, iv_jingrou,
			iv_ningmeng, iv_boluo;
	private ImageView iv_liaotian, iv_haoyou, iv_quanzi;
	private View v_dot[];
	
	private ViewPager vp_goods;
	private List<View> listViews; // Tab页面列表
	private ImageView cursor;// 动画图片
	private int offset = 0;// 动画图片偏移量
	private int currIndex = 0;// 当前页卡编号

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.mainpages_first, null);
			MyLy = (LinearLayout) inflater.inflate(R.layout.mytop, null);
			initView();
			initListener();
			intData();

		} else {
			ViewGroup parent = (ViewGroup) rootView.getParent();
			if (parent != null) {
				parent.removeView(rootView);
			}
		}
		return rootView;
	}

	final class DemoJavaScriptInterface {

		DemoJavaScriptInterface() {
		}

		@JavascriptInterface
		public void clickOnAndroid() {
			mHandler.post(new Runnable() {
				public void run() {
					// webView.loadUrl("javascript : jump_on_driver(8)");
				}

			});
		}

		/**
		 * have not used it in the program.
		 * 
		 * @param flag
		 * @param id
		 */
		@JavascriptInterface
		public void clickOnAndroid(int flag, int id) {
			switch (flag) {
			case 0:

				mHandler.post(new Runnable() {
					public void run() {
						String group_buy_id = "33";
						String goods_img = "http://www.bjia.co/images/201502/goods_img/292_G_1423965246537.jpg";
						if (!StringUtil.isEmpty(group_buy_id)) {
							Intent intent = new Intent(getActivity(),
									GoodDetailActivity.class);
							intent.putExtra("group_buy_id", group_buy_id);
							intent.putExtra("goods_img", goods_img);
							startActivity(intent);
						}
					}
				});
				break;
			case 1:
				mHandler.post(new Runnable() {
					public void run() {
						String group_buy_id = "355";
						if (!StringUtil.isEmpty(group_buy_id)) {
							Intent intent = new Intent(getActivity(),
									GoodDetailTwoActivity.class);
							intent.putExtra("id", group_buy_id);
							startActivity(intent);
						}
					}
				});
				break;
			case 2:
				mHandler.post(new Runnable() {
					public void run() {
						String group_buy_id = "32";
						String goods_img = "http://www.bjia.co/images/201410/goods_img/102_G_1413854707208.jpg";
						Intent intent = new Intent(getActivity(),
								GoodDetailActivity.class);
						intent.putExtra("id", group_buy_id);
						intent.putExtra("goods_img", goods_img);
						startActivity(intent);
					}
				});
				break;
			case 3:
				mHandler.post(new Runnable() {
					public void run() {
						String group_buy_id = "350";
						Intent intent = new Intent(getActivity(),
								GoodDetailTwoActivity.class);
						intent.putExtra("id", group_buy_id);
						startActivity(intent);
					}
				});
				break;
			case 4:
				mHandler.post(new Runnable() {
					public void run() {
						String group_buy_id = "144";
						Intent intent = new Intent(getActivity(),
								GoodDetailTwoActivity.class);
						intent.putExtra("id", group_buy_id);
						startActivity(intent);
					}
				});
				break;
			case 5:
				mHandler.post(new Runnable() {
					public void run() {
						String group_buy_id = "248";
						Intent intent = new Intent(getActivity(),
								GoodDetailTwoActivity.class);
						intent.putExtra("id", group_buy_id);
						startActivity(intent);
					}
				});
				break;
			case 6:
				mHandler.post(new Runnable() {
					public void run() {
						String group_buy_id = "338";
						Intent intent = new Intent(getActivity(),
								GoodDetailTwoActivity.class);
						intent.putExtra("id", group_buy_id);
						startActivity(intent);
					}
				});
				break;
			case 7:
				if (StringUtil.isEmpty(UserInfoBean.getUserInfo(getActivity())
						.getUid())) {
					Intent intentt = new Intent(getActivity(),
							LoginReginActivity.class);
					getActivity().startActivity(intentt);
					return;
				}
				if ((MainActivity) getActivity() != null) {
					((MainActivity) getActivity()).switchContent(8);
				}
				break;
			case 9:
				if (StringUtil.isEmpty(UserInfoBean.getUserInfo(getActivity())
						.getUid())) {
					Intent intentt = new Intent(getActivity(),
							LoginReginActivity.class);
					getActivity().startActivity(intentt);
					return;
				}
				if ((MainActivity) getActivity() != null) {
					((MainActivity) getActivity()).switchContent(9);
				}
				break;
			case 8:
				if (StringUtil.isEmpty(UserInfoBean.getUserInfo(getActivity())
						.getUid())) {
					Intent intentt = new Intent(getActivity(),
							LoginReginActivity.class);
					getActivity().startActivity(intentt);
					return;
				}
				if ((MainActivity) getActivity() != null) {
					((MainActivity) getActivity()).switchContent(7);
				}

				break;
			case 10:
				Intent intent10 = new Intent(getActivity(),
						FenleiActivity.class);
				intent10.putExtra("cat_id", 126 + "");
				getActivity().startActivity(intent10);

			case 11:
				Intent intent11 = new Intent(getActivity(),
						FenleiActivity.class);
				intent11.putExtra("cat_id", 225 + "");
				getActivity().startActivity(intent11);

				break;
			case 12:
				Intent intent12 = new Intent(getActivity(),
						FenleiActivity.class);
				intent12.putExtra("cat_id", 21 + "");
				getActivity().startActivity(intent12);

				break;
			case 13:
				Intent intent13 = new Intent(getActivity(),
						FenleiActivity.class);
				intent13.putExtra("cat_id", 101 + "");
				getActivity().startActivity(intent13);

				break;
			case 14:
				Intent intent14 = new Intent(getActivity(),
						FenleiActivity.class);
				intent14.putExtra("cat_id", 85 + "");
				getActivity().startActivity(intent14);

				break;
			case 15:
				Intent intent15 = new Intent(getActivity(),
						FenleiActivity.class);
				intent15.putExtra("cat_id", 290 + "");
				getActivity().startActivity(intent15);

				break;
			default:
				break;
			}

		}
	}

	@SuppressWarnings("deprecation")
	private void intData() {
		// 设置WebView属性，能够执行Javascript脚本
		// WebSettings webSettings = webView.getSettings();
		// webView.setHorizontalScrollBarEnabled(false);
		// webSettings.setSavePassword(false);
		// webSettings.setSaveFormData(false);
		// webSettings.setJavaScriptEnabled(true);
		// webSettings.setSupportZoom(false);
		//
		// webView.setWebViewClient(new WebViewClient() {
		// public boolean shouldOverrideUrlLoading(WebView view, String url) {
		// // 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
		// view.loadUrl(url);
		// return true;
		// }
		// });
		//
		// webView.setWebChromeClient(new WebChromeClient() {
		// @Override
		// public void onProgressChanged(WebView view, int newProgress) {
		// super.onProgressChanged(view, newProgress);
		// if (newProgress == 100) {
		// // 设置加载完成后结束动画
		// refreshLayout.setRefreshing(false);
		// }
		// }
		// });
		//
		// // 加载需要显示的网页
		// webView.loadUrl("http://www.bjia.co/app.php");
		// webView.addJavascriptInterface(new DemoJavaScriptInterface(),
		// "demo");
		//
		// refreshLayout.setViewGroup(webView);// 设置监听滚动的子view
		// refreshLayout.setOnRefreshListener(this);
		// // 设置颜色
		// refreshLayout.setColorScheme(R.color.green, R.color.gray,
		// R.color.blue,
		// R.color.light_white);

		// InitFocusIndicatorContainer();
		// gallery = (MyGallery) rootView.findViewById(R.id.gallery);
		// gallery.setAdapter(new ImgAdapter(getActivity(),
		// FinalBitmapUtil.create(getActivity())));
		// gallery.setFocusable(true);
		// gallery.setOnItemSelectedListener(new OnItemSelectedListener() {
		//
		// public void onItemSelected(AdapterView<?> arg0, View arg1,
		// int selIndex, long arg3) {
		// selIndex = selIndex % 6;
		// // 修改上一次选中项的背景
		// portImg.get(preSelImgIndex).setImageResource(
		// R.drawable.ic_focus);
		// // 修改当前选中项的背景
		// portImg.get(selIndex).setImageResource(
		// R.drawable.ic_focus_select);
		// preSelImgIndex = selIndex;
		// }
		//
		// public void onNothingSelected(AdapterView<?> arg0) {
		// }
		// });

	}

	private void initView() {
		iv_top_left = (ImageView) rootView.findViewById(R.id.iv_top_left);
		rl_gouwuche = (RelativeLayout) rootView.findViewById(R.id.rl_gouwuche);
		iv_top_center = (ImageView) rootView.findViewById(R.id.iv_top_center);
		ll_search = (ImageView) rootView.findViewById(R.id.ll_search);
		iv_back_normal = (ImageView) rootView.findViewById(R.id.iv_back_normal);
		ll_fenlei = (ImageView) rootView.findViewById(R.id.ll_fenlei);
		bt_search = (ImageView) rootView.findViewById(R.id.bt_search);
		et_content = (EditText) rootView.findViewById(R.id.et_content);
		ll_search_menu = (LinearLayout) rootView
				.findViewById(R.id.ll_search_menu);
		ll_search_and_sort = (LinearLayout) rootView
				.findViewById(R.id.ll_search_and_sort);

		tv_number = (TextView) rootView.findViewById(R.id.tv_number);

		iv_hongti = (ImageView) rootView.findViewById(R.id.iv_hongti);
		iv_xilanhua = (ImageView) rootView.findViewById(R.id.iv_xilanhua);
		iv_jiliu = (ImageView) rootView.findViewById(R.id.iv_jiliu);
		iv_yanmai = (ImageView) rootView.findViewById(R.id.iv_yanmai);
		iv_liaotian = (ImageView) rootView.findViewById(R.id.iv_liaotian);
		iv_haoyou = (ImageView) rootView.findViewById(R.id.iv_haoyou);
		iv_quanzi = (ImageView) rootView.findViewById(R.id.iv_quanzi);
		iv_jingrou = (ImageView) rootView.findViewById(R.id.iv_jingrou);
		iv_ningmeng = (ImageView) rootView.findViewById(R.id.iv_ningmeng);
		iv_boluo = (ImageView) rootView.findViewById(R.id.iv_boluo);

		vp_goods = (ViewPager) rootView.findViewById(R.id.vp_goods);

		v_dot = new View[7];
		v_dot[1] = (View) rootView.findViewById(R.id.v_dot1);
		v_dot[2] = (View) rootView.findViewById(R.id.v_dot2);
		v_dot[3] = (View) rootView.findViewById(R.id.v_dot3);
		v_dot[4] = (View) rootView.findViewById(R.id.v_dot4);
		v_dot[5] = (View) rootView.findViewById(R.id.v_dot5);
		v_dot[6] = (View) rootView.findViewById(R.id.v_dot6);
		listViews = new ArrayList<View>();

		LayoutInflater mInflater = getActivity().getLayoutInflater();
		listViews.add(mInflater.inflate(R.layout.item_b1, null));
		listViews.add(mInflater.inflate(R.layout.item_b2, null));
		listViews.add(mInflater.inflate(R.layout.item_b3, null));
		listViews.add(mInflater.inflate(R.layout.item_b4, null));
		listViews.add(mInflater.inflate(R.layout.item_b5, null));
		listViews.add(mInflater.inflate(R.layout.item_b6, null));

		vp_goods.setAdapter(new MyPagerAdapter(listViews));
		vp_goods.setCurrentItem(0);
		setPagerDot(1, 6);
		// TODO set the listener.
		vp_goods.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int pos) {
				// the pos is from 0 to 5;
				// and the v_dot[] is from 1 to 6;
				setPagerDot(pos + 1, 6);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
//				switch (arg0) {
//
//				case 1:// 手势滑动，空闲中
//
//					break;
//
//				case 2:// 界面切换中
//
//					break;
//
//				case 0:// 滑动结束，即切换完毕或者加载完毕
//
//					// 当前为最后一张，此时从右向左滑，则切换到第一张
//
//					if (vp_goods.getCurrentItem() == vp_goods.getAdapter()
//							.getCount() - 1) {
//
//						vp_goods.setCurrentItem(0);
//						setPagerDot(1, 6);
//
//					}
//					// 当前为第一张，此时从左向右滑，则切换到最后一张
//
//					else if (vp_goods.getCurrentItem() == 0 ) {
//
//						vp_goods.setCurrentItem(vp_goods.getAdapter()
//								.getCount() - 1);
//						setPagerDot(6, 6);
//
//					}
//
//					break;
//
//				}

			}
		});

		// webView = (MyWebView) rootView.findViewById(R.id.webview);
		// webView.setEmbeddedTitleBar(MyLy);
		// refreshLayout = (ScrollSwipeRefreshLayout) rootView
		// .findViewById(R.id.refresh_layout);
		// ll_focus_indicator_container = (LinearLayout) rootView
		// .findViewById(R.id.ll_focus_indicator_container);
	}

	@Override
	public void onResume() {
		super.onResume();
		// webView.resumeTimers();

		uidString = UserInfoBean.getUserInfo(getActivity()).getUid();
		if (!StringUtil.isEmpty(uidString)) {
			mShopCartList = mRequestManager.getShopCartList(uidString, false);
		}

	}

	@SuppressLint("NewApi")
	private void setPagerDot(int pos, int total) {
		for (int i = 1; i <= total; i++) {
			if (i == pos) {
				v_dot[i].setBackground(getResources().getDrawable(
						R.drawable.circle_green_bg));
			} else {
				v_dot[i].setBackground(getResources().getDrawable(
						R.drawable.circle_gray_bg));
			}
		}

	}

	private void initListener() {
		iv_top_left.setOnClickListener(this);
		rl_gouwuche.setOnClickListener(this);
		ll_search.setOnClickListener(this);
		ll_fenlei.setOnClickListener(this);
		iv_top_center.setOnClickListener(this);

		bt_search.setOnClickListener(this);
		iv_back_normal.setClickable(true);
		iv_back_normal.setOnClickListener(this);

		iv_hongti.setOnClickListener(this);
		iv_xilanhua.setOnClickListener(this);
		iv_jiliu.setOnClickListener(this);
		iv_yanmai.setOnClickListener(this);
		iv_liaotian.setOnClickListener(this);
		iv_haoyou.setOnClickListener(this);
		iv_quanzi.setOnClickListener(this);
		iv_jingrou.setOnClickListener(this);
		iv_ningmeng.setOnClickListener(this);
		iv_boluo.setOnClickListener(this);

		et_content.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// change the input state.
				if (!hasFocus) {
					InputMethodManager imm = (InputMethodManager) v
							.getContext().getSystemService(
									Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(v.getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
				}

			}
		});

	}

	@Override
	public void onClick(View arg0) {
		String group_buy_id;
		String goods_img;
		Intent intent;
		switch (arg0.getId()) {

		case R.id.iv_top_left:
			((MainActivity) (getActivity())).showOrHideMenu();
			break;
		case R.id.rl_gouwuche:
			if ((MainActivity) getActivity() != null) {
				if (!StringUtil.isEmpty(UserInfoBean.getUserInfo(getActivity())
						.getUid())) {
					((MainActivity) getActivity()).switchContent(5);
				} else {
					Intent intentt = new Intent(getActivity(),
							LoginReginActivity.class);
					getActivity().startActivity(intentt);
				}

			}
			break;
		case R.id.ll_search:
			// if ((MainActivity) getActivity() != null) {
			// ((MainActivity) getActivity()).switchContent(1);
			// }
			ll_search_and_sort.setVisibility(View.INVISIBLE);
			ll_search_menu.setVisibility(View.VISIBLE);
			break;
		case R.id.iv_back_normal:
			ll_search_and_sort.setVisibility(View.VISIBLE);
			ll_search_menu.setVisibility(View.INVISIBLE);
			break;

		case R.id.bt_search:
			String content = et_content.getText() + "";
			if (!StringUtil.isEmpty(content)) {
				intent = new Intent(getActivity(), GoodsActivity.class);
				intent.putExtra("key", content);
				startActivity(intent);
			} else {
				ToastUtil.makeShortText(getActivity(),
						R.string.contentnotisnull);
			}
			break;

		case R.id.ll_fenlei:
			if ((MainActivity) getActivity() != null) {
				((MainActivity) getActivity()).switchContent(1);
			}
			// intent = new Intent(getActivity(),GoodsActivity.class);
			// getActivity().startActivity(intent);
			break;
		case R.id.iv_top_center:
			if ((MainActivity) getActivity() != null) {
				((MainActivity) getActivity()).switchContent(8);
			}
			break;

		case R.id.iv_hongti:
			group_buy_id = "350";
			intent = new Intent(getActivity(), GoodDetailTwoActivity.class);
			intent.putExtra("id", group_buy_id);
			startActivity(intent);
			break;
		case R.id.iv_xilanhua:
			group_buy_id = "144";
			intent = new Intent(getActivity(), GoodDetailTwoActivity.class);
			intent.putExtra("id", group_buy_id);
			startActivity(intent);
			break;
		case R.id.iv_jiliu:
			// TODO deal with the imageView.
			break;
		case R.id.iv_yanmai:
			group_buy_id = "338";
			intent = new Intent(getActivity(), GoodDetailTwoActivity.class);
			intent.putExtra("id", group_buy_id);
			startActivity(intent);
			break;
		case R.id.iv_liaotian:
			if (StringUtil.isEmpty(UserInfoBean.getUserInfo(getActivity())
					.getUid())) {
				Intent intentt = new Intent(getActivity(),
						LoginReginActivity.class);
				getActivity().startActivity(intentt);
				return;
			}
			if ((MainActivity) getActivity() != null) {
				((MainActivity) getActivity()).switchContent(8);
			}

			break;
		case R.id.iv_haoyou:
			if (StringUtil.isEmpty(UserInfoBean.getUserInfo(getActivity())
					.getUid())) {
				Intent intentt = new Intent(getActivity(),
						LoginReginActivity.class);
				getActivity().startActivity(intentt);
				return;
			}
			if ((MainActivity) getActivity() != null) {
				((MainActivity) getActivity()).switchContent(7);
			}

			break;
		case R.id.iv_quanzi:
			if (StringUtil.isEmpty(UserInfoBean.getUserInfo(getActivity())
					.getUid())) {
				Intent intentt = new Intent(getActivity(),
						LoginReginActivity.class);
				getActivity().startActivity(intentt);
				return;
			}
			if ((MainActivity) getActivity() != null) {
				((MainActivity) getActivity()).switchContent(9);
			}
			break;
		case R.id.iv_jingrou:
			group_buy_id = "33";
			goods_img = "http://www.bjia.co/images/201502/goods_img/292_G_1423965246537.jpg";
			if (!StringUtil.isEmpty(group_buy_id)) {
				intent = new Intent(getActivity(), GoodDetailActivity.class);
				intent.putExtra("group_buy_id", group_buy_id);
				intent.putExtra("goods_img", goods_img);
				startActivity(intent);
			}
			break;
		case R.id.iv_ningmeng:
			group_buy_id = "355";
			goods_img = "http://www.bjia.co/images/201410/goods_img/114_P_1413768121712.jpg";
			if (!StringUtil.isEmpty(group_buy_id)) {
				intent = new Intent(getActivity(), GoodDetailActivity.class);
				intent.putExtra("id", group_buy_id);
				intent.putExtra("goods_img", goods_img);
				startActivity(intent);
			}
			break;
		case R.id.iv_boluo:
			group_buy_id = "32";
			goods_img = "http://www.bjia.co/images/201410/goods_img/102_G_1413854707208.jpg";
			intent = new Intent(getActivity(), GoodDetailActivity.class);
			intent.putExtra("id", group_buy_id);
			intent.putExtra("goods_img", goods_img);
			startActivity(intent);
			break;

		default:
			break;
		}
	}

	public void setRedPoint(boolean isShow) {
		if (isShow) {
			iv_top_center.setImageResource(R.drawable.main_message_yuandian);
		} else {
			iv_top_center.setImageResource(R.drawable.main_message);
		}
	}

	@Override
	public void onRequestFinished(int requestId, int resultCode, Bundle payload) {
		super.onRequestFinished(requestId, resultCode, payload);
		if (resultCode == PoCService.SUCCESS_CODE) {

			int result = payload.getInt(ResponseBean.RESPONSE_STATE);

			if (result == 1) {
				String numbers = payload
						.getString(ResponseBean.RESPONSE_NUMBER);
				if ("0".equals(numbers)) {
					tv_number.setVisibility(View.INVISIBLE);
				} else {
					tv_number.setVisibility(View.VISIBLE);
				}
				tv_number.setText(numbers);
			}
		}

	}

	@Override
	public void onRefresh() {
		// 下拉重新加载
		// webView.reload();

	}

	@Override
	public void onPause() {
		super.onPause();
		// webView.pauseTimers();

	}

	// private void InitFocusIndicatorContainer() {
	// portImg = new ArrayList<ImageView>();
	// for (int i = 0; i < 6; i++) {
	// ImageView localImageView = new ImageView(getActivity());
	// localImageView.setId(i);
	// ImageView.ScaleType localScaleType = ImageView.ScaleType.FIT_XY;
	// localImageView.setScaleType(localScaleType);
	// LinearLayout.LayoutParams localLayoutParams = new
	// LinearLayout.LayoutParams(
	// 24, 24);
	// localImageView.setLayoutParams(localLayoutParams);
	// localImageView.setPadding(5, 5, 5, 5);
	// localImageView.setImageResource(R.drawable.ic_focus);
	// portImg.add(localImageView);
	// ll_focus_indicator_container.addView(localImageView);
	// }
	// }

	/**
	 * ViewPager适配器
	 */
	public class MyPagerAdapter extends PagerAdapter {
		public List<View> mListViews;

		public MyPagerAdapter(List<View> mListViews) {
			this.mListViews = mListViews;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(mListViews.get(arg1));
		}

		@Override
		public void finishUpdate(View arg0) {
		}

		@Override
		public int getCount() {
			return mListViews.size();
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(mListViews.get(arg1), 0);
			mListViews.get(arg1).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (StringUtil.isEmpty(UserInfoBean.getUserInfo(
							getActivity()).getUid())) {
						Intent intentt = new Intent(getActivity(),
								LoginReginActivity.class);
						getActivity().startActivity(intentt);
						return;
					}
					if ((MainActivity) getActivity() != null) {
						((MainActivity) getActivity()).switchContent(1);
					}

				}
			});

			return mListViews.get(arg1);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == (arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}
	}
}
