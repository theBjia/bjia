package com.llkj.newbjia.chat;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Binder;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.llkj.db.DBHelper;
import com.llkj.db.NoticeContacts;
import com.llkj.db.PrivateChatMessagesEntity;
import com.llkj.newbjia.MyApplication;
import com.llkj.newbjia.R;
import com.llkj.newbjia.bean.UserInfoBean;
import com.llkj.newbjia.quanzi.QuanzhiActivity;
import com.llkj.newbjia.utils.PreferenceHelper;
import com.llkj.newbjia.utils.StringUtil;

/**
 * 收取消息服务
 * 
 * @author way
 * 
 */
public class GetMsgServiceTwo extends Service {
	private static final int MSG = 0x001;

	public static SocketIOClient client;
	private NotificationManager mNotificationManager;

	private Notification mNotification;
	private Context mContext = this;

	private static final String TAG = "GetMsgService";
	private ScheduledExecutorService scheduledExecutorService;
	private ArrayList<ReceverListener> listenerList;
	private DBHelper instance;

	private Bitmap bmp;
	// 收到用户按返回键发出的广播，就显示通知栏
	private BroadcastReceiver backKeyReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Toast.makeText(context, "IM进入后台运行", 0).show();
			// setMsgNotification();
		}
	};

	// 消息响应sockethandler

	private String tag = "SocketIOClient.Handler ";

	private SocketIOClient.Handler sockethandler = new SocketIOClient.Handler() {

		// 连接成功后回调该方法
		@Override
		public void onConnect() {

			MyApplication.isConnect = true;
			if (MyApplication.isAddMe) { // 如果还没有登录成功，连接成功以后不能发addme
				UserInfoBean.addMe(getApplicationContext());
			}

			Log.e(TAG, "onConnect");
		}

		@Override
		public void onConnectToEndpoint(String endpoint) {
			Log.e(TAG, "onConnectToEndpoint");
		}

		/**
		 * @param event
		 *            事件名 根据事件名去判断处理 私聊，群聊，登录，退出，离线消息等
		 * @param arguments
		 *            服务器发过来的内容
		 */
		@Override
		public void on(String event, JSONArray arguments) {

			JSONObject jsonObject = null;
			TranObject tObject = new TranObject();
			try {
				jsonObject = arguments.getJSONObject(0);
			} catch (JSONException e1) {
				e1.printStackTrace();
				Log.e(tag, "Args[0]消息格式解析出错:" + arguments);
			}

			/**
			 * 根据event处理
			 */
			if (event.equals(Constants.CHECKUSER)) {

				Log.e(TAG, "checkuser");

				// 通知服务器可以接受离线消息

				try {
					if (jsonObject.has("flag")) {
						String flag = jsonObject.getString("flag");

						if ("1".equals(flag)) {
							instance = DBHelper
									.getInstance(getApplicationContext());
							UserInfoBean.getUserInfo(mContext);
							JSONArray ja = new JSONArray();
							JSONObject json = new JSONObject();
							json.put(
									"user_id",
									UserInfoBean.getUserInfo(
											getApplicationContext()).getUid());
							ja.put(json);
							client.emit("getOffline", ja);
						}
					}
					if (jsonObject.has("esq_id")) {
						MyApplication.esq_id = jsonObject.getString("esq_id");
					}
					if (jsonObject.has("esq_name")) {
						MyApplication.esq_name = jsonObject
								.getString("esq_name");
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// 获取离线数据
			} else if (event.equals(Constants.OFFLINEMESSAGE)) {

				try {

					JSONArray data = jsonObject.getJSONArray("data");
					for (int i = 0; i < data.length(); i++) {
						TranObject to = new TranObject();
						JSONObject dataone = data.getJSONObject(i);

						if (dataone.has("type")) {
							String type = dataone.getString("type");
							if ("0".equals(type)) {

								PrivateChatMessagesEntity entity = ChatEntityUtils
										.getPrivateChatEntity(dataone,
												getApplicationContext());
								if (jsonObject.has("msg_id")) {
									String dataId;
									try {
										dataId = jsonObject.getString("msg_id");
										entity.setCmid(dataId);
										EmitUtils.deleteMsgNotifyServer(
												getApplicationContext(),
												dataId, 0);
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

								}
								entity.setBubbleType("1");
								entity.setActionType(Constants.ONTRANSMIT);
								entity.setOtherId("0");
								to.setActionType(Constants.ONTRANSMIT);
								to.setObject(entity);
							} else if ("1".equals(type)) {
								PrivateChatMessagesEntity entity = ChatEntityUtils
										.getPrivateChatEntity(dataone,
												getApplicationContext());
								if (jsonObject.has("msg_id")) {
									String dataId;
									try {
										dataId = jsonObject.getString("msg_id");
										entity.setCmid(dataId);
										EmitUtils.deleteMsgNotifyServer(
												getApplicationContext(),
												dataId, 1);
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

								}
								entity.setOtherId("1");
								entity.setBubbleType("1");
								entity.setActionType(Constants.ONREPLY);
								to.setActionType(Constants.ONREPLY);
								to.setObject(entity);
							}
						}

						kuoshanMessage(to);
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// 2、获取私聊数据
			} else if (Constants.ONTRANSMIT.equals(event)) {

				PrivateChatMessagesEntity entity = ChatEntityUtils
						.getPrivateChatEntity(jsonObject,
								getApplicationContext());
				if (jsonObject.has("msg_id")) {
					String dataId;
					try {
						dataId = jsonObject.getString("msg_id");
						entity.setCmid(dataId);
						EmitUtils.deleteMsgNotifyServer(
								getApplicationContext(), dataId, 0);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				entity.setBubbleType("1");
				entity.setActionType(Constants.ONTRANSMIT);
				entity.setOtherId("0");
				tObject.setActionType(Constants.ONTRANSMIT);
				tObject.setObject(entity);
				kuoshanMessage(tObject);
			} else if (Constants.ONREPLY.equals(event)) {

				PrivateChatMessagesEntity entity = ChatEntityUtils
						.getPrivateChatEntity(jsonObject,
								getApplicationContext());
				if (jsonObject.has("msg_id")) {
					String dataId;
					try {
						dataId = jsonObject.getString("msg_id");
						entity.setCmid(dataId);
						EmitUtils.deleteMsgNotifyServer(
								getApplicationContext(), dataId, 1);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				entity.setBubbleType("1");
				entity.setActionType(Constants.ONREPLY);
				entity.setOtherId("1");
				tObject.setActionType(Constants.ONREPLY);
				tObject.setObject(entity);
				kuoshanMessage(tObject);
			}
			Log.e(TAG, jsonObject + "");

		}

		public void savePrivateMessage(PrivateChatMessagesEntity entity) {
			if (Constants.ONREPLY.equals(entity.getActionType())) {
				instance.saveChatOther(entity);
				if (instance.queryRecently(entity.getReceiveId())) {
					ContentValues cv = new ContentValues();
					cv.put("messageContent", entity.getMessageContent());
					cv.put("messageDate", entity.getMessageDate());
					cv.put("messageType", entity.getMessageType());
					int number = instance.queryRecentlyUnRead(entity
							.getReceiveId());
					number++;
					cv.put("unReadNumber", number + "");
					instance.updateRecentlyContacts(cv, entity.getReceiveId());
				} else {
					instance.saveRecentlyContacts(ChatEntityUtils.c2r(entity,
							getApplicationContext()));
				}
			} else if (Constants.ONTRANSMIT.equals(entity.getActionType())) {
				instance.saveChatPerson(entity);
				if (instance.queryRecently(entity.getSendedId())) {
					ContentValues cv = new ContentValues();
					cv.put("messageContent", entity.getMessageContent());
					cv.put("messageDate", entity.getMessageDate());
					cv.put("messageType", entity.getMessageType());
					int number = instance.queryRecentlyUnRead(entity
							.getSendedId());
					number++;
					cv.put("unReadNumber", number + "");
					instance.updateRecentlyContacts(cv, entity.getSendedId());
				} else {
					instance.saveRecentlyContacts(ChatEntityUtils.c2r(entity,
							getApplicationContext()));
				}
			}

		}

		public void kuoshanMessage(TranObject tObject) {
			// String tabName = tObject.getEntity().getMessageType()
			// + UserInfoBean.getUserInfo(getApplicationContext()).getUid() +
			// tObject.getEntity().getTargetId();
			// if (messageDB.isHasMsg(tabName, tObject.getEntity().getDate(),
			// tObject.getEntity().getFromId())) {
			// return;
			// }

			// 私聊和群聊 自己发的消息，不接受

			PrivateChatMessagesEntity entity = (PrivateChatMessagesEntity) tObject
					.getObject();
			if (entity.getSendedId().equals(
					UserInfoBean.getUserInfo(getApplicationContext()).getUid())) {
				return;
			}
			// 不是发给我的不接受 私聊

			if (!entity.getReceiveId().equals(
					UserInfoBean.getUserInfo(getApplicationContext()).getUid())) {
				return;
			}

			if (instance.isPrivateMsgExist(entity.getSendedId(),
					entity.getReceiveId(), entity.getMessageDate())) {
				return;
			}
			savePrivateMessage(entity);

			if (listenerList != null) {
				for (int i = 0; i < listenerList.size(); i++) {
					listenerList.get(i).doResult(tObject);
				}
			}
			Message message = handler.obtainMessage();
			message.what = MSG;
			message.getData().putSerializable("msg", tObject);
			handler.sendMessage(message);

			// Intent broadCast = new Intent();
			// broadCast.setAction(Constants.ACTION);
			// broadCast.putExtra(Constants.MSGKEY, tObject);
			// sendBroadcast(broadCast);// 把收到的消息已广播的形式发送出去

		}

		private String getPicPath() {
			File file = new File(Environment.getExternalStorageDirectory(),
					System.currentTimeMillis() + ".jpg");

			return file.getAbsolutePath();
		}

		@Override
		public void onDisconnect(int code, String reason) {

			Log.e(TAG, "onDisconnect");
			MyApplication.isConnect = false;

		}

		@Override
		public void onJSON(JSONObject json) {
			Log.e(TAG, "onJSON");
		}

		@Override
		public void onMessage(String message) {
			Log.e(TAG, "onMessage");
		}

		@Override
		public void onError(Exception error) {
			MyApplication.isConnect = false;

			error.printStackTrace();

		}

	};

	// 用来更新通知栏消息的handler
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG:
				int newMsgNum = MyApplication.newMsgNum;// 从全局变量中获取
				newMsgNum++;// 每收到一次消息，自增一次
				MyApplication.newMsgNum = newMsgNum;// 再设置为全局变量

				TranObject tObject = (TranObject) msg.getData()
						.getSerializable("msg");
				notifyChat(tObject, newMsgNum);

				break;
			default:
				break;
			}
		}
	};

	public void notifyChat(TranObject tro, int newMsgNum) {

		PrivateChatMessagesEntity pentity = (PrivateChatMessagesEntity) tro
				.getObject();
		String content = "";
		if (pentity.getMessageType().equals(Constants.PICTURE)) {
			content = "[图片]";
		} else if (pentity.getMessageType().equals(Constants.VOICE)) {
			content = "[语音]";
		} else if (pentity.getMessageType().equals(Constants.TEXT)) {
			content = pentity.getMessageContent();
		}
		int icon = R.drawable.icon_bijia1;
		CharSequence tickerText = pentity.getSendedName() + ":" + content;
		long when = System.currentTimeMillis();
		mNotification = new Notification(icon, tickerText, when);
		mNotification.flags = Notification.FLAG_AUTO_CANCEL;
		// 设置默认声音
		NoticeContacts nc;
		if (Constants.ONREPLY.equals(pentity.getActionType())) {
			nc = instance.queryNoticeContacts(pentity.getReceiveId());
		} else {
			nc = instance.queryNoticeContacts(pentity.getSendedId());
		}

		boolean isTiXing = true;
		boolean isZhendong = PreferenceHelper.readBoolean(
				getApplicationContext(),
				UserInfoBean.getUserInfo(getApplicationContext()).getUid()
						+ Constants.NOTIFYSTYLE, "iszhendong", true);
		boolean isLingsheng = PreferenceHelper.readBoolean(
				getApplicationContext(),
				UserInfoBean.getUserInfo(getApplicationContext()).getUid()
						+ Constants.NOTIFYSTYLE, "isLingsheng", true);

		if (nc != null) {
			if (!StringUtil.isEmpty(nc.getN_newMsgRemind())) {
				if ("1".equals(nc.getN_newMsgRemind())) {
					isTiXing = true;

				} else {
					isTiXing = false;
				}
			}

		} else {
			isTiXing = true;
		}
		// 震动开启的话，如果铃声开，提醒开就设置都开，如果铃声开，提醒未开就设置
		if (isZhendong) {
			if (isLingsheng) {
				if (isTiXing) {
					ChatUtils.setYZ(mNotification, isLingsheng, isZhendong);
				} else {
					ChatUtils.setYZ(mNotification, false, false);
				}
			} else {
				if (isTiXing) {
					ChatUtils.setYZ(mNotification, isLingsheng, isZhendong);
				} else {
					ChatUtils.setYZ(mNotification, false, false);
				}
			}
		}

		if (isLingsheng) {
			if (isZhendong) {
				if (isTiXing) {
					ChatUtils.setYZ(mNotification, isLingsheng, isZhendong);
				} else {
					ChatUtils.setYZ(mNotification, false, false);
				}
			} else {
				if (isTiXing) {
					ChatUtils.setYZ(mNotification, isLingsheng, isZhendong);
				} else {
					ChatUtils.setYZ(mNotification, false, false);
				}
			}
		}

		// 更新通知栏

		// 设定震动(需加VIBRATE权限)

		mNotification.contentView = null;
		Intent intent = new Intent(getApplicationContext(),
				ChatPersonActivity.class);
		String targetid = pentity.getSendedId();
		String targetname = pentity.getSendedName();
		String targetlogo = pentity.getSendedLogo();
		intent.putExtra(Constants.TARGETID, targetid);
		intent.putExtra(Constants.TAGETNAME, targetname);
		intent.putExtra(Constants.TAGETPHOTO, targetlogo);
		PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0,
				intent, 0);

		mNotification.setLatestEventInfo(mContext, pentity.getSendedName()
				+ " (" + newMsgNum + "条新消息)", content, contentIntent);

		try {
			Class<?> clazz = Class.forName("com.android.internal.R$id");

			Field field = clazz.getField("icon");
			field.setAccessible(true);
			int id_icon = field.getInt(null);

			mNotification.setLatestEventInfo(mContext, pentity.getSendedName()
					+ " (" + newMsgNum + "条新消息)", content, contentIntent);
			mNotification.flags |= Notification.FLAG_AUTO_CANCEL;

			if (mNotification.contentView != null) {
				mNotification.contentView.setImageViewResource(id_icon, R.drawable.lili);
			}
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mNotificationManager.notify(Constants.NOTIFY_ID, mNotification);// 通知一下才会生效哦
	}

	@Override
	public void onCreate() {// 在onCreate方法里面注册广播接收者

		super.onCreate();
		MyApplication.serviceisRunning = true;

		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.BACKKEY_ACTION);
		registerReceiver(backKeyReceiver, filter);
		mNotificationManager = (NotificationManager) getSystemService(android.content.Context.NOTIFICATION_SERVICE);
		client = MyApplication.client;

		// 4.15
		if (client != null) {
			client.addHandler(sockethandler);
			client.connect();
			Log.d(TAG, "创建结束");
			MyApplication.mNotificationManager = mNotificationManager;

		}
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		scheduledExecutorService.scheduleAtFixedRate(new MyTask(), 0, 10,
				TimeUnit.SECONDS);

	}

	class MyTask implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			MyApplication.reConnect(getApplicationContext());
			Log.e("重练", "MyTask");
		}

	}

	@Override
	// 在服务被摧毁时，做一些事情
	public void onDestroy() {
		super.onDestroy();
		// stopForeground(true);
		unregisterReceiver(backKeyReceiver);
		mNotificationManager.cancel(Constants.NOTIFY_ID);
		// 给服务器发送下线消息
		MyApplication.serviceisRunning = false;
		Log.e(TAG, "杀死service");

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// Notification notification = new Notification(R.drawable.ic_launcher,
		// getText(R.string.app_name), System.currentTimeMillis());
		// Intent notificationIntent = new Intent(this,
		// SplashScreenActivity.class);
		// notificationIntent.setFlags(flags);
		// PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
		// notificationIntent, 0);
		// notification.setLatestEventInfo(this, "金融部落", "请保持程序在后台运行",
		// pendingIntent);
		// startForeground(0x11, notification);
		flags = START_STICKY;
		return super.onStartCommand(intent, flags, startId);
	}

	public interface ReceverListener {
		void doResult(TranObject tro);
	}

	public void addReceverListener(ReceverListener receverListener) {
		if (listenerList == null) {
			listenerList = new ArrayList<GetMsgServiceTwo.ReceverListener>();
		}
		this.listenerList.add(receverListener);
	}

	public void removeReceverListener(ReceverListener receverListener) {
		if (listenerList != null && listenerList.contains(receverListener)) {
			listenerList.remove(receverListener);
		}
	}

	public class ServiceBinder extends Binder {
		public GetMsgServiceTwo getService() {
			return GetMsgServiceTwo.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		IBinder result = null;
		if (null == result)
			result = new ServiceBinder();
		return result;
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
