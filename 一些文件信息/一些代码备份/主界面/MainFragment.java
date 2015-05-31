package com.llkj.newbjia.main;

import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

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

public class MainFragment extends BaseFragment implements OnClickListener,
		OnRefreshListener {
	private ImageView iv_top_left, iv_top_center;
	private ImageView ll_search, ll_fenlei;

	private int mShopCartList;

	private RelativeLayout rl_gouwuche;
	private TextView tv_number;

	private String uidString;

	private MyWebView webView;
	private ScrollSwipeRefreshLayout refreshLayout;
	private MyGallery gallery = null;
	private LinearLayout MyLy;
	private LinearLayout ll_focus_indicator_container = null;

	private Handler mHandler = new Handler();

	private ArrayList<ImageView> portImg;
	private int preSelImgIndex = 0;
	

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
		public void clickOnAndroid(int flag){
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
				mHandler.post(new Runnable() {
					public void run() {
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
					}
				});
				
				break;
			case 9:
				mHandler.post(new Runnable() {
					public void run() {
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
					}
				});
				
				break;
			case 8:
				mHandler.post(new Runnable() {
					public void run() {
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
					}
				});
				

				break;
			case 10:
				mHandler.post(new Runnable() {
					public void run() {
						Intent intent10 = new Intent(getActivity(),
								FenleiActivity.class);
						intent10.putExtra("cat_id", 126 + "");
						getActivity().startActivity(intent10);
					}
				});


			case 11:
				mHandler.post(new Runnable() {
					public void run() {
						Intent intent11 = new Intent(getActivity(),
								FenleiActivity.class);
						intent11.putExtra("cat_id", 225 + "");
						getActivity().startActivity(intent11);
					}
				});


				break;
			case 12:
				mHandler.post(new Runnable() {
					public void run() {
						Intent intent12 = new Intent(getActivity(),
								FenleiActivity.class);
						intent12.putExtra("cat_id", 21 + "");
						getActivity().startActivity(intent12);
					}
				});


				break;
			case 13:
				mHandler.post(new Runnable() {
					public void run() {
						Intent intent13 = new Intent(getActivity(),
								FenleiActivity.class);
						intent13.putExtra("cat_id", 101 + "");
						getActivity().startActivity(intent13);

					}
				});
				
				break;
			case 14:
				mHandler.post(new Runnable() {
					public void run() {
						Intent intent14 = new Intent(getActivity(),
								FenleiActivity.class);
						intent14.putExtra("cat_id", 85 + "");
						getActivity().startActivity(intent14);
					}
				});


				break;
			case 15:
				mHandler.post(new Runnable() {
					public void run() {
						
						Intent intent15 = new Intent(getActivity(),
								FenleiActivity.class);
						intent15.putExtra("cat_id", 290 + "");
						getActivity().startActivity(intent15);
					}
				});

				break;
			default:
				break;
			}
			}
		
		//TODO problem. 2 parameters should be sent to the useful function.
		//TODO in fact there is only one parameter .(see the javascript page)
		@JavascriptInterface
		public void clickOnAndroid(int flag,int id) {
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
				mHandler.post(new Runnable() {
					public void run() {
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
					}
				});
				
				break;
			case 9:
				mHandler.post(new Runnable() {
					public void run() {
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
					}
				});
				
				break;
			case 8:
				mHandler.post(new Runnable() {
					public void run() {
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
					}
				});
				

				break;
			case 10:
				mHandler.post(new Runnable() {
					public void run() {
						Intent intent10 = new Intent(getActivity(),
								FenleiActivity.class);
						intent10.putExtra("cat_id", 126 + "");
						getActivity().startActivity(intent10);
					}
				});


			case 11:
				mHandler.post(new Runnable() {
					public void run() {
						Intent intent11 = new Intent(getActivity(),
								FenleiActivity.class);
						intent11.putExtra("cat_id", 225 + "");
						getActivity().startActivity(intent11);
					}
				});


				break;
			case 12:
				mHandler.post(new Runnable() {
					public void run() {
						Intent intent12 = new Intent(getActivity(),
								FenleiActivity.class);
						intent12.putExtra("cat_id", 21 + "");
						getActivity().startActivity(intent12);
					}
				});


				break;
			case 13:
				mHandler.post(new Runnable() {
					public void run() {
						Intent intent13 = new Intent(getActivity(),
								FenleiActivity.class);
						intent13.putExtra("cat_id", 101 + "");
						getActivity().startActivity(intent13);

					}
				});
				
				break;
			case 14:
				mHandler.post(new Runnable() {
					public void run() {
						Intent intent14 = new Intent(getActivity(),
								FenleiActivity.class);
						intent14.putExtra("cat_id", 85 + "");
						getActivity().startActivity(intent14);
					}
				});


				break;
			case 15:
				mHandler.post(new Runnable() {
					public void run() {
						
						Intent intent15 = new Intent(getActivity(),
								FenleiActivity.class);
						intent15.putExtra("cat_id", 290 + "");
						getActivity().startActivity(intent15);
					}
				});

				break;
			default:
				break;
			}

		}
	}

	@SuppressLint({ "SetJavaScriptEnabled", "JavascriptInterface" })
	private void intData() {
		// 设置WebView属性，能够执行Javascript脚本
		WebSettings webSettings = webView.getSettings();
		webView.setHorizontalScrollBarEnabled(false);
		webSettings.setSavePassword(false);
		webSettings.setSaveFormData(false);
		webSettings.setJavaScriptEnabled(true);
		webSettings.setSupportZoom(false);

		webView.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
				view.loadUrl(url);
				return true;
			}
		});

		webView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				super.onProgressChanged(view, newProgress);
				if (newProgress == 100) {
					// 设置加载完成后结束动画
					refreshLayout.setRefreshing(false);
				}
			}
		});
		// 加载需要显示的网页
		//webView.loadUrl("http://www.bjia.co/app.php");
		webView.loadUrl("http://www.bjia.co/APP");
		webView.addJavascriptInterface(new DemoJavaScriptInterface(), "demo");

		refreshLayout.setViewGroup(webView);// 设置监听滚动的子view
		refreshLayout.setOnRefreshListener(this);
		// 设置颜色
		refreshLayout.setColorScheme(R.color.green, R.color.gray, R.color.blue,
				R.color.light_white);

		InitFocusIndicatorContainer();
		gallery = (MyGallery) rootView.findViewById(R.id.gallery);
		gallery.setAdapter(new ImgAdapter(getActivity(), FinalBitmapUtil
				.create(getActivity())));
		gallery.setFocusable(true);
		gallery.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int selIndex, long arg3) {
				selIndex = selIndex % 6;
				// 修改上一次选中项的背景
				portImg.get(preSelImgIndex).setImageResource(
						R.drawable.ic_focus);
				// 修改当前选中项的背景
				portImg.get(selIndex).setImageResource(
						R.drawable.ic_focus_select);
				preSelImgIndex = selIndex;
			}

			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

	}

	private void initView() {
		iv_top_left = (ImageView) rootView.findViewById(R.id.iv_top_left);
		rl_gouwuche = (RelativeLayout) rootView.findViewById(R.id.rl_gouwuche);
		iv_top_center = (ImageView) rootView.findViewById(R.id.iv_top_center);
		ll_search = (ImageView) rootView.findViewById(R.id.ll_search);
		ll_fenlei = (ImageView) rootView.findViewById(R.id.ll_fenlei);

		tv_number = (TextView) rootView.findViewById(R.id.tv_number);

		webView = (MyWebView) rootView.findViewById(R.id.webview);
		webView.setEmbeddedTitleBar(MyLy);
		refreshLayout = (ScrollSwipeRefreshLayout) rootView
				.findViewById(R.id.refresh_layout);
		ll_focus_indicator_container = (LinearLayout) rootView
				.findViewById(R.id.ll_focus_indicator_container);
	}

	@Override
	public void onResume() {
		super.onResume();
		webView.resumeTimers();

		uidString = UserInfoBean.getUserInfo(getActivity()).getUid();
		if (!StringUtil.isEmpty(uidString)) {
			mShopCartList = mRequestManager.getShopCartList(uidString, false);
		}

	}

	private void initListener() {
		iv_top_left.setOnClickListener(this);
		rl_gouwuche.setOnClickListener(this);
		ll_search.setOnClickListener(this);
		ll_fenlei.setOnClickListener(this);
		iv_top_center.setOnClickListener(this);

	}

	@Override
	public void onClick(View arg0) {
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
			if ((MainActivity) getActivity() != null) {
				((MainActivity) getActivity()).switchContent(1);
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
		webView.reload();

	}

	@Override
	public void onPause() {
		super.onPause();
		webView.pauseTimers();

	}

	private void InitFocusIndicatorContainer() {
		portImg = new ArrayList<ImageView>();
		for (int i = 0; i < 6; i++) {
			ImageView localImageView = new ImageView(getActivity());
			localImageView.setId(i);
			ImageView.ScaleType localScaleType = ImageView.ScaleType.FIT_XY;
			localImageView.setScaleType(localScaleType);
			LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(
					24, 24);
			localImageView.setLayoutParams(localLayoutParams);
			localImageView.setPadding(5, 5, 5, 5);
			localImageView.setImageResource(R.drawable.ic_focus);
			portImg.add(localImageView);
			this.ll_focus_indicator_container.addView(localImageView);
		}
	}
}
