package com.llkj.newbjia;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.R.bool;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import com.amap.api.location.AMapLocalWeatherForecast;
import com.amap.api.location.AMapLocalWeatherListener;
import com.amap.api.location.AMapLocalWeatherLive;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.llkj.db.DBHelper;
import com.llkj.db.RecentlyContacts;
import com.llkj.newbjia.R;
import com.llkj.newbjia.BaseFragment.BackHandledInterface;
import com.llkj.newbjia.bean.UserInfoBean;
import com.llkj.newbjia.chat.Constants;
import com.llkj.newbjia.chat.GetMsgServiceTwo;
import com.llkj.newbjia.chat.RecentlyChatFragment;
import com.llkj.newbjia.chat.TranObject;
import com.llkj.newbjia.chat.GetMsgServiceTwo.ReceverListener;
import com.llkj.newbjia.chat.GetMsgServiceTwo.ServiceBinder;
import com.llkj.newbjia.chat.RecentlyChatFragment.OnChatMessageListener;
import com.llkj.newbjia.collection.FavoriteFragment;
import com.llkj.newbjia.fenlei.FenleiFragment;
import com.llkj.newbjia.friend.FriendFragment;
import com.llkj.newbjia.main.GoodDetailTwoActivity;
import com.llkj.newbjia.main.MainFragment;
import com.llkj.newbjia.mybijia.MyBijiaFragment;
import com.llkj.newbjia.myorder.OrderCheckFragment;
import com.llkj.newbjia.quanzi.QuanZhiFragment;
import com.llkj.newbjia.setting.MySettingFragment;
import com.llkj.newbjia.shoppingcart.MyCartFragment;
import com.llkj.newbjia.utils.LogUtil;
import com.llkj.newbjia.utils.SharedPreferenceUtil;
import com.llkj.newbjia.utils.StringUtil;
import com.llkj.newbjia.utils.ToastUtil;
import com.llkj.newbjia.zitidianfenbu.ZitidianfenbuFragment;

public class MainActivity extends SlidingFragmentActivity implements
		AMapLocationListener, AMapLocalWeatherListener, ReceverListener,
		BackHandledInterface, OnChatMessageListener {

	private SharedPreferenceUtil spLoginSetting;

	private SlidingMenu localSlidingMenu;
	public LeftSlidingMenuFragment mFrag;

	private FragmentManager fm;

	private long mExitTime;// 判断是否退出

	private ScheduledExecutorService scheduledExecutorService;

	private Fragment mContent;
	private Fragment mainFragment, fenLeiFragment, quanzhiFragement,
			mySettingFragment, orderCheckFragment, favoriteFragment,
			ziTidianfenbuFragment, myBijiaFragment, myCartFragment,
			friendFragment, recentlyChatFragment;

	private MyReceiver receiver;

	private LocationManagerProxy aMapLocManager;
	private AMapLocation aMapLocation;// 用于判断定位超时

	public static MainActivity newstance;

	private GetMsgServiceTwo countService;
	private String uid;
	public boolean isBijiaInto;
	private DBHelper dbinstance;
	// TODO
	private OnMessegeChangedListener messageChanged;
	private int good_back_data = 0;

	public OnMessegeChangedListener getMessageChanged() {
		return messageChanged;
	}

	public void setMessageChanged(OnMessegeChangedListener messageChanged) {
		this.messageChanged = messageChanged;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		

		fm = getSupportFragmentManager();
		MyApplication.getInstance().addActivity(this);

		initSlidingMenu();
		initData();

		receiver = new MyReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.SWITCHCART);
		filter.addAction(Constants.REFRESH_TOUXIANG);
		filter.addAction(Constants.REFRESH_SHOPCART);
		filter.addAction(Constants.UPDATECHATLIST);
		filter.addAction(Constants.REFRESH_ORDER);
		filter.addAction(Constants.REFRESH_COLLECTION);
		registerReceiver(receiver, filter);
		newstance = this;

		Intent intent = new Intent(this, GetMsgServiceTwo.class);
		/** 进入Activity开始服务 */
		bindService(intent, conn, Context.BIND_AUTO_CREATE);

		// TODO
		spLoginSetting = SharedPreferenceUtil.init(this,
				SharedPreferenceUtil.LOGIN_SETTING, MODE_PRIVATE);
		if (!spLoginSetting.getBoolean(SharedPreferenceUtil.LOGIN_IS_AUTO)
				&& !MyApplication.isFromLogin) {
			UserInfoBean.clearUserInfo(this);
			MyApplication.disConnect(this);
			UserInfoBean.userLogout();
		}

		uid = UserInfoBean.getUserInfo(this).getUid();
		if (!StringUtil.isEmpty(uid)) {
			MyApplication.isAddMe = true;
			UserInfoBean.addMe(this);
		} else {
			MyApplication.isAddMe = false;
		}

		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		scheduledExecutorService.scheduleAtFixedRate(new MyTask(), 0, 10,
				TimeUnit.SECONDS);

	}

	@Override
	protected void onResume() {
		super.onResume();
		switch (good_back_data) {
		// to switch the fragment.
		case GoodDetailTwoActivity.PAGE_MAIN:
			switchContent(0);
			break;
		case GoodDetailTwoActivity.PAGE_CHAT:
			switchContent(8);
			break;
		case GoodDetailTwoActivity.PAGE_SORT:
			switchContent(1);
			break;
		case GoodDetailTwoActivity.PAGE_MY_BJIA:
			switchContent(10);
			break;
		case GoodDetailTwoActivity.PAGE_SHOPPING_CART:
			switchContent(5);
			break;
		default:
			break;
		}

	}

	@Override
	protected void onNewIntent(Intent intent_good) {
		// this it used when return from the goods detail.
		super.onNewIntent(intent_good);
		setIntent(intent_good);
		if (intent_good != null) {
			good_back_data = intent_good.getIntExtra("page_type", -100);

			
		}
	}

	/**
	 * 聊天消息数量获取
	 * 
	 * @author
	 * 
	 */
	private class MyTask implements Runnable {

		@Override
		public void run() {
			if (!StringUtil.isEmpty(uid)
					&& spLoginSetting
							.getBoolean(SharedPreferenceUtil.LOGIN_IS_AUTO)) {
				final boolean isShow;
				if (getUnReadMessageNum() > 0) {
					isShow = true;
				} else {
					isShow = false;
				}
				MyApplication.handler.post(new Runnable() {

					@Override
					public void run() {
						LogUtil.e(isShow + "");
						((MainFragment) mainFragment).setRedPoint(isShow);
					}
				});
			}
		}
	}

	private ServiceConnection conn = new ServiceConnection() {
		/** 获取服务对象时的操作 */
		public void onServiceConnected(ComponentName name, IBinder service) {

			countService = ((ServiceBinder) service).getService();
			countService.addReceverListener(MainActivity.this);

		}

		/** 无法获取到服务对象时的操作 */
		public void onServiceDisconnected(ComponentName name) {

			countService = null;
		}

	};

	private void initData() {

		aMapLocManager = LocationManagerProxy
				.getInstance(getApplicationContext());
		aMapLocManager.requestLocationData(LocationProviderProxy.AMapNetwork,
				60 * 1000, 100, this);
		aMapLocManager.requestWeatherUpdates(
				LocationManagerProxy.WEATHER_TYPE_LIVE, this);
		MyApplication.handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				if (aMapLocation == null) {

					ToastUtil.makeLongText(MainActivity.this, "定位未成功");
					stopLocation();// 销毁掉定位

				}
			}
		}, 12000);
	}

	/**
	 * 销毁定位
	 */
	private void stopLocation() {
		if (aMapLocManager != null) {
			aMapLocManager.removeUpdates(this);
			aMapLocManager.destory();
		}
		aMapLocManager = null;
	}

	@Override
	protected void onPause() {
		super.onPause();
		stopLocation();
	}

	public void initSlidingMenu() {
		mainFragment = new MainFragment();
		mContent = mainFragment;
		fm.beginTransaction().replace(R.id.center_frame, mContent).commit();

		setBehindContentView(R.layout.main_left_layout);// 设置左边的菜单布局

		FragmentTransaction mFragementTransaction = fm.beginTransaction();
		mFrag = new LeftSlidingMenuFragment();

		mFragementTransaction.replace(R.id.main_left_fragment, mFrag);
		mFragementTransaction.commit();
		// 改变了部分程序的初始化顺序。
		// mainFragment = new MainFragment();
		fenLeiFragment = new FenleiFragment();
		quanzhiFragement = new QuanZhiFragment();
		ziTidianfenbuFragment = new ZitidianfenbuFragment();
		mySettingFragment = new MySettingFragment();
		orderCheckFragment = new OrderCheckFragment();
		favoriteFragment = new FavoriteFragment();
		myBijiaFragment = new MyBijiaFragment();
		myCartFragment = new MyCartFragment();
		friendFragment = new FriendFragment();
		recentlyChatFragment = new RecentlyChatFragment();
		// TODO add the listener
		((RecentlyChatFragment) recentlyChatFragment).setMessageListener(this);
		((QuanZhiFragment) quanzhiFragement).setOnNewMessageListener(mFrag);

		// mContent = mainFragment;

		// fm.beginTransaction().replace(R.id.center_frame, mContent).commit();
		//
		// setBehindContentView(R.layout.main_left_layout);// 设置左边的菜单布局
		//
		// FragmentTransaction mFragementTransaction = fm.beginTransaction();
		// mFrag = new LeftSlidingMenuFragment();
		//
		// mFragementTransaction.replace(R.id.main_left_fragment, mFrag);
		// mFragementTransaction.commit();

		localSlidingMenu = getSlidingMenu();
		localSlidingMenu.setMode(SlidingMenu.LEFT);// 设置是左滑还是右滑，还是左右都可以滑，我这里只做了左滑
		localSlidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);// 设置菜单宽度
		localSlidingMenu.setFadeDegree(0.35f);// 设置淡入淡出的比例
		localSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);// 设置手势模式
		localSlidingMenu.setShadowDrawable(R.drawable.shadow);// 设置左菜单阴影图片
		localSlidingMenu.setShadowWidthRes(R.dimen.shadow_width);
		localSlidingMenu.setFadeEnabled(true);// 设置滑动时菜单的是否淡入淡出
		localSlidingMenu.setBehindScrollScale(0.333f);// 设置滑动时拖拽效果
	}

	public void showOrHideMenu() {
		if (localSlidingMenu.isMenuShowing()) {
			localSlidingMenu.showContent();
		} else {
			// TODO
			InputMethodManager imm = (InputMethodManager) MainActivity.this
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(localSlidingMenu.getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
			localSlidingMenu.showMenu();
		}
	}

	public void showOrHideSecondaryMenu() {
		if (localSlidingMenu.isSecondaryMenuShowing()) {
			localSlidingMenu.showContent();
		} else {
			localSlidingMenu.showSecondaryMenu();
		}
	}


	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		InputMethodManager imm = (InputMethodManager) MainActivity.this
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(localSlidingMenu.getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
		return false;
		
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			
			if (mContent != null && !localSlidingMenu.isMenuShowing()
					&& !localSlidingMenu.isSecondaryMenuShowing()) {
				switchContent(0);
				mContent = null;
				return true;
			}

			if (localSlidingMenu.isMenuShowing()
					|| localSlidingMenu.isSecondaryMenuShowing()) {
				localSlidingMenu.showContent();
			} else {
				if ((System.currentTimeMillis() - mExitTime) > 2000) {
					Toast.makeText(this, "再按一次退出彼佳", Toast.LENGTH_SHORT).show();
					mExitTime = System.currentTimeMillis();
				} else {
					finish();
				}
			}
			return true;
		}
		// 拦截MENU按钮点击事件，让他无任何操作
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 左侧菜单点击切换首页的内容
	 * 
	 */
	public void switchContent(int type) {

		switch (type) {
		case 0:
			mContent = mainFragment;
			break;
		case 1:
			mContent = fenLeiFragment;
			break;
		case 5:
			mContent = myCartFragment;
			break;
		case 6:
			mContent = favoriteFragment;
			break;
		case 7:
			mContent = friendFragment;
			break;
		case 8:
			mContent = recentlyChatFragment;
			break;
		case 9:
			mContent = quanzhiFragement;
			break;
		case 10:
			mContent = myBijiaFragment;
			break;
		case 11:
			mContent = orderCheckFragment;
			break;
		case 12:
			mContent = ziTidianfenbuFragment;
			break;
		case 13:
			mContent = mySettingFragment;
			break;
		default:
			break;
		}
		replaceFragment(mContent);
		getSlidingMenu().showContent();
	}

	class MyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			String action = intent.getAction();
			if (com.llkj.newbjia.chat.Constants.SWITCHCART.equals(action)) {
				fm.beginTransaction()
						.replace(R.id.center_frame, myCartFragment)
						.commitAllowingStateLoss();
			} else

			if (com.llkj.newbjia.chat.Constants.REFRESH_SHOPCART
					.equals(action)) {
				myCartFragment = new MyCartFragment();
			} else if (com.llkj.newbjia.chat.Constants.REFRESH_TOUXIANG
					.equals(action)) {
				mFrag.setTouxiang();
			} else if (com.llkj.newbjia.chat.Constants.UPDATECHATLIST
					.equals(action)) {
				if (((RecentlyChatFragment) recentlyChatFragment).isOpened)
					((RecentlyChatFragment) recentlyChatFragment).updateList();
			} else if (com.llkj.newbjia.chat.Constants.REFRESH_ORDER
					.equals(action)) {
				orderCheckFragment = new OrderCheckFragment();
			} else if (com.llkj.newbjia.chat.Constants.REFRESH_COLLECTION
					.equals(action)) {
				favoriteFragment = new FavoriteFragment();
			}
		}
	}

	private void replaceFragment(Fragment newFragment) {
		FragmentTransaction transaction = fm.beginTransaction();
		if (!newFragment.isAdded()) {
			try {
				transaction.replace(R.id.center_frame, newFragment);
				transaction.addToBackStack(null);
				transaction.commit();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			transaction.show(newFragment);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		scheduledExecutorService.shutdown();
		unregisterReceiver(receiver);
		countService.removeReceverListener(this);
		this.unbindService(conn);
	}

	@Override
	public void onLocationChanged(Location arg0) {

	}

	@Override
	public void onProviderDisabled(String arg0) {

	}

	@Override
	public void onProviderEnabled(String arg0) {

	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {

	}

	/*
	 * 混合定位回调函数
	 */
	@Override
	public void onLocationChanged(AMapLocation location) {
		if (location != null) {
			this.aMapLocation = location;// 判断超时机制
			Double geoLat = location.getLatitude();
			Double geoLng = location.getLongitude();
			// String cityCode = "";
			String desc = "";
			Bundle locBundle = location.getExtras();
			if (locBundle != null) {
				// cityCode = locBundle.getString("citycode");
				desc = locBundle.getString("desc");
			}
			// String str = ("定位成功:(" + geoLng + "," + geoLat + ")"
			// + "\n精    度    :" + location.getAccuracy() + "米"
			// + "\n定位方式:" + location.getProvider() + "\n定位时间:"
			// + AMapUtil.convertToTime(location.getTime()) + "\n城市编码:"
			// + cityCode + "\n位置描述:" + desc + "\n省:"
			// + location.getProvince() + "\n市:" + location.getCity()
			// + "\n区(县):" + location.getDistrict() + "\n区域编码:" + location
			// .getAdCode());

			MyApplication.geoLng = geoLng + "";
			MyApplication.geoLat = geoLat + "";
			MyApplication.localcity = location.getCity();
			MyApplication.locationPosition = desc;

			// LogUtil.e(str + MyApplication.geoLat + MyApplication.geoLng);
		}
	}

	@Override
	public void onWeatherForecaseSearched(AMapLocalWeatherForecast arg0) {

	}

	@Override
	public void onWeatherLiveSearched(AMapLocalWeatherLive aMapLocalWeatherLive) {
		if (aMapLocalWeatherLive != null
				&& aMapLocalWeatherLive.getAMapException().getErrorCode() == 0) {
			// 天气预报成功回调 设置天气信息
			String city = aMapLocalWeatherLive.getCity();
			MyApplication.localcity = city.substring(0, city.length() - 1);

			MyApplication.localWeather = aMapLocalWeatherLive.getWeather();
			MyApplication.localTemperature = aMapLocalWeatherLive
					.getTemperature() + "℃";
			String time = aMapLocalWeatherLive.getReportTime();
			MyApplication.localReportTime = time.substring(0, 10);

			mFrag.setWeather(true);

		} else {

			// 获取天气预报失败
			Toast.makeText(
					this,
					"获取天气信息失败:"
							+ aMapLocalWeatherLive.getAMapException()
									.getErrorMessage(), Toast.LENGTH_SHORT)
					.show();
			mFrag.setWeather(false);

		}
	}

	@Override
	public void doResult(TranObject tro) {
		MyApplication.handler.post(new Runnable() {

			@Override
			public void run() {
				if (((RecentlyChatFragment) recentlyChatFragment).isOpened)
					((RecentlyChatFragment) recentlyChatFragment).updateList();

			}
		});

	}

	private int getUnReadMessageNum() {
		int count = 0;
		dbinstance = DBHelper.getInstance(this);
		ArrayList<RecentlyContacts> rcs = dbinstance.queryRecentlyRecord();
		if (rcs != null && rcs.size() > 0) {
			for (int i = 0; i < rcs.size(); i++) {
				int num = StringUtil.strToInt(rcs.get(i).getUnReadNumber());
				count += num;
			}
		}
		return count;

	}

	@Override
	public void setSelectedFragment(BaseFragment selectedFragment) {
		this.mContent = selectedFragment;
	}

	@Override
	public void onNewMessageComing() {
		messageChanged
				.onMessegeChanged(OnMessegeChangedListener.NEW_CHAT_MESSAGE);

	}

	public interface OnMessegeChangedListener {
		public static final int NEW_CHAT_MESSAGE = 1;

		public void onMessegeChanged(int i);
	}

}
