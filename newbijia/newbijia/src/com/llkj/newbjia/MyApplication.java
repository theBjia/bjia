package com.llkj.newbjia;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;

import com.llkj.newbjia.bean.UserInfoBean;
import com.llkj.newbjia.chat.Constants;
import com.llkj.newbjia.chat.GetMsgServiceTwo;
import com.llkj.newbjia.chat.SocketIOClient;
import com.llkj.newbjia.login.SplashScreenActivity;
import com.llkj.newbjia.quanzi.QuanzhiActivity;
import com.llkj.newbjia.utils.LogUtil;
import com.llkj.newbjia.utils.StringUtil;

import android.app.AlertDialog.Builder;

/**
 * 
 * @author Administrator 全局存储容器 整个应用程序唯一实例 描述： 当android程序启动时系统会创建一个
 *         application对象，用来存储系统的一些 信息。
 *         android系统会为每个程序运行时创建一个Application类的对象且仅创建一个(单例)。
 *         且application对象的生命周期是整个程序中最长的，它的生命周期就等于这个程序的生命周期。
 *         因为它是全局的单例的，所以在不同的Activity,Service中获得的对象都是同一个对象。
 *         所以通过Application来进行一些，数据传递，数据共享,数据缓存等操作。
 * 
 */
public class MyApplication extends Application {

	private List<Activity> activitys = new LinkedList<Activity>();
	private List<Activity> twoactivitys = new LinkedList<Activity>();

	public static Handler handler = new Handler();

	public static String localcity;
	public static String localWeather;
	public static String localTemperature;
	public static String localReportTime;
	
	public static String geoLng = "";
	public static String geoLat = "";
	public static String esq_id = "";
	public static String esq_name = "";
	public static String locationPosition = "";

	public static boolean isConnect;// socket是否连上
	public static boolean isAddMe;// 判断连上服务器以后是否调用addme方法。
	//TODO
	public static boolean isFromLogin;

	private String tag = "MyApplication";

	public static SocketIOClient client;
	public static int newMsgNum = 0;// 后台运行的消息

	public static boolean serviceisRunning; // 服务器是否在运行
	public static NotificationManager mNotificationManager;
	public static boolean isRefrsh; // 服务器是否在运行

	public MyApplication() {

	}

	// 添加Activity到容器中
	public void addActivity(Activity activity) {
		if (activitys != null && activitys.size() > 0) {
			if (!activitys.contains(activity)) {
				activitys.add(activity);
			}
		} else {
			activitys.add(activity);
		}

	}

	public void exit() {
		if (activitys != null && activitys.size() > 0) {
			for (Activity activity : activitys) {
				activity.finish();
			}
		}
	}

	public void addActivityTwo(Activity activity) {
		if (twoactivitys != null && twoactivitys.size() > 0) {
			if (!twoactivitys.contains(activity)) {
				twoactivitys.add(activity);
			}
		} else {
			twoactivitys.add(activity);
		}
	}

	public void exitTwo() {
		if (twoactivitys != null && twoactivitys.size() > 0) {
			for (Activity activity : twoactivitys) {
				activity.finish();
			}
		}

	}

	/**
	 * 全局变量
	 */
	private HashMap<String, Object> allData = new HashMap<String, Object>();

	// 存数据
	public void saveData(String key, Object value) {
		allData.put(key, value);
	}

	// 取数据
	public Object getData(String key) {
		Object o = null;
		if (allData.containsKey(key)) {
			o = allData.get(key);
		} else {
			o = new Object();
		}
		return o;
	}

	// 删除一条
	public boolean del(String key) {
		boolean isdel = false;
		if (allData.containsKey(key)) {
			allData.remove(key);
			isdel = true;
		}
		return isdel;
	}

	// 删除所有
	public void delAll() {
		if (allData != null) {
			allData.clear();
		}

	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		client = new SocketIOClient(URI.create("http://" + Constants.SERVER_IP
				+ ":" + Constants.SERVER_PORT));
		if (!MyApplication.serviceisRunning) {
			Intent service = new Intent(this, GetMsgServiceTwo.class);
			startService(service);
		}
		LogUtil.i("Application====onCreate()");
	}

	/** 单例模式 */
	private static MyApplication application;

	public static MyApplication getInstance() {
		if (null == application) {
			application = new MyApplication();
		}
		return application;
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		LogUtil.i("Application====onTerminate()");
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		LogUtil.i("Application====onConfigurationChanged()");
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		LogUtil.i("Application====onLowMemory()");
	}

	/**
	 * 
	 * @param activity
	 */
	public void returnBack(final Activity activity) {
		if (activitys.size() > 1) {
			for (Activity elem : activitys) {
				if (elem.getClass().getName()
						.equals(activity.getClass().getName())) {
					activity.finish();
					activitys.remove(elem);
					break;
				}
			}

		} else {
			new AlertDialog.Builder(activity)
					.setTitle("退出")
					.setMessage("是否退出？")
					.setNegativeButton("否",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									arg0.dismiss();
								}
							})
					.setPositiveButton("是",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									arg0.dismiss();
									activity.finish();
									activitys.removeAll(activitys);
								}
							}).show();

		}

	}

	/**
	 * 重新连接
	 * 
	 * @param context
	 */
	public static void reConnect(Context context) {
		if (!StringUtil.isNetworkConnected(context)) {
			MyApplication.isConnect = false;

		}
		if (StringUtil.isNetworkConnected(context)) {
			if (!MyApplication.isConnect) {
				try {

					MyApplication.client.disconnect();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				MyApplication.client.connect();
				if (MyApplication.isAddMe) {
					UserInfoBean.addMe(context);
				}
			}
		}

	}

	/**
	 * 断开连接
	 * 
	 * @param context
	 */
	public static void disConnect(Context context) {

		if (MyApplication.isConnect) {
			try {
				MyApplication.client.disconnect();
				MyApplication.isAddMe = false;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	/**
	 * 提示是否要退出
	 */
	public static void showAlertDialog(final Context context) {
		Builder builder = new AlertDialog.Builder(context);
		builder.setMessage("确认退出登录吗？")
				.setPositiveButton("退出", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						UserInfoBean.clearUserInfo(context);
						MyApplication.disConnect(context);
						UserInfoBean.userLogout();
						Intent intent = new Intent(context,
								SplashScreenActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
								| Intent.FLAG_ACTIVITY_NEW_TASK);
						context.startActivity(intent);

						((Activity) context).finish();
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).create().show();
	}

	/**
	 * 提示是否要保存到相册
	 */
	public static void showSaveDialog(final Context context, final String uri) {
		Builder builder = new AlertDialog.Builder(context);
		builder.setMessage("保存到相册")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						new Thread() {
							@Override
							public void run() {
								// TODO Auto-generated method stub

								String url = MediaStore.Images.Media
										.insertImage(
												context.getContentResolver(),
												returnBitMap(uri), "hehe",
												"hehe");
								if (!url.isEmpty()) {
									Message msg = new Message();
									msg.what = 1;
									QuanzhiActivity.handler.sendEmptyMessage(1);
								}
							}
						}.start();

					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).create().show();
	}

	public static Bitmap returnBitMap(String url) {
		URL myFileUrl = null;
		Bitmap bitmap = null;
		try {
			myFileUrl = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		try {
			HttpURLConnection conn = (HttpURLConnection) myFileUrl
					.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}

}
