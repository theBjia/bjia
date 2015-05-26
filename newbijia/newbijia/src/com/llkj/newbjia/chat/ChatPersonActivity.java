package com.llkj.newbjia.chat;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.llkj.db.DBHelper;
import com.llkj.db.NoticeContacts;
import com.llkj.db.PrivateChatMessagesEntity;
import com.llkj.db.RecentlyContacts;
import com.llkj.newbjia.MyApplication;
import com.llkj.newbjia.R;
import com.llkj.newbjia.bean.UserInfoBean;
import com.llkj.newbjia.chat.GetMsgServiceTwo.ReceverListener;
import com.llkj.newbjia.utils.AudioRecorder;
import com.llkj.newbjia.utils.ComplexOperation;
import com.llkj.newbjia.utils.File2Code;
import com.llkj.newbjia.utils.ImageDispose;
import com.llkj.newbjia.utils.ImageOperate;
import com.llkj.newbjia.utils.LogUtil;
import com.llkj.newbjia.utils.SaveBitmapToSdUtil;
import com.llkj.newbjia.utils.StringUtil;
import com.llkj.newbjia.utils.ToastUtil;

public class ChatPersonActivity extends Activity implements OnClickListener,
		ReceverListener {

	private Thread recordThread;
	private InputMethodManager inputMethodManager;

	private static int MAX_TIME = 60; // 最长录制时间，单位秒，0为无时间限制
	private static int MIX_TIME = 1; // 最短录制时间，单位秒，0为无时间限制，建议设为1

	private static int RECORD_NO = 0; // 不在录音
	private static int RECORD_ING = 1; // 正在录音
	private static int RECODE_ED = 2; // 完成录音

	private static int RECODE_STATE = 0; // 录音的状态

	private static float recodeTime = 0.0f; // 录音的时间
	private static double voiceValue = 0.0; // 麦克风获取的音量值

	// 录音按钮
	private ImageView chatting_mode_btn, dialog_img;

	private boolean isShowMore, isShowBiaoqing, isLuyin;

	private Button mBtnRcd;
	private Dialog dialog;
	private AudioRecorder mr;
	// =================================文字=======================
	private TextView mEditTextContent;
	// ===============表情============..
	private ImageView chatting_biaoqing_focuseBtn;
	private ArrayList<GridView> grids;
	private ViewPager viewPager;
	private LinearLayout page_select;
	private int[] expressionImages;
	private String[] expressionImageNames;
	private int[] expressionImages1;
	private String[] expressionImageNames1;
	private int[] expressionImages2;
	private String[] expressionImageNames2;
	private GridView gView1, gView2, gView3;
	private ImageView page0, page1, page2;

	// ==============更多===================
	private ImageView moreBtn;
	// ===========其他===
	private TextView photoBtn, take_photoBtn, tv_send_position;

	private LinearLayout moreInfoBtn;
	// 判断字段

	// 拍照
	private String selectedImagePath = "";
	final private int PICK_IMAGE = 1;
	final private int CAPTURE_IMAGE = 2;
	private String imgPath;

	public static final int NONE = 0;
	public static final int PHOTOHRAPH = 1;// 拍照
	public static final int PHOTOZOOM = 2; // 缩放
	public static final int PHOTORESOULT = 3;// 结果
	public static final String IMAGE_UNSPECIFIED = "image/*";

	public static final String VOICE = "Voice";
	public static final String PICTURE = "Picture";
	public static final String TEXT = "Text";

	private List<PrivateChatMessagesEntity> mDataArrays = new ArrayList<PrivateChatMessagesEntity>();
	private PrivateChatMessagesAdapter mAdapter;
	private ListView mListView;
	private PullToRefreshListView ptrListView;
	private int page = 0;
	private String targetId, targetName, targetPhoto, bgPath, gid; // target
																	// 是接受方，gid是最近聊天记录和消息提醒的条件。
	private Intent bigIntent;
	public static ChatPersonActivity instance;
	private List<PrivateChatMessagesEntity> list;
	// 其他

	private TextView tv_title_name;

	private RelativeLayout rl_back;
	private ImageView iv_chatsetting;

	private DBHelper dbinstance;
	private GetMsgServiceTwo countService;
	private int a = 10, b = 10;
	private View activityRootView;
	private boolean isKefu;
	private TextView tv_send;
	private boolean isTiXing;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		MyApplication.getInstance().addActivity(this);
		setContentView(R.layout.chat_person);
		// 启动activity时不自动弹出软键盘
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		initView();
		initLisiData();
		addListener();
		MyApplication.newMsgNum = 0;
		Intent intent = new Intent(this, GetMsgServiceTwo.class);
		/** 进入Activity开始服务 */
		bindService(intent, conn, Context.BIND_AUTO_CREATE);
	}

	@SuppressLint("NewApi")
	public void setNC() {
		if (isKefu) {
			gid = UserInfoBean.getUserInfo(this).getUid();
		} else {
			gid = targetId;
		}
		NoticeContacts nc = dbinstance.queryNoticeContacts(gid);
		if (nc != null) {
			if ("0".equals(nc.getN_newMsgRemind())) {
				isTiXing = false;
			} else {
				isTiXing = true;
			}
			if (!StringUtil.isEmpty(nc.getBackgroundPic())) {
				bgPath = nc.getBackgroundPic();
				BitmapFactory.Options option = new BitmapFactory.Options();
				option.inSampleSize = 2;
				Bitmap bm = BitmapFactory.decodeFile(bgPath, option);
				activityRootView.setBackgroundDrawable(new BitmapDrawable(bm));
			}
		} else {
			nc = new NoticeContacts();
			nc.setGid(gid);
			nc.setIsMember("1");
			nc.setN_newMsgRemind("1");
			nc.setShowNikeName("0");
			nc.setUserId(UserInfoBean.getUserInfo(this).getUid());
			nc.setBackgroundPic("");
			dbinstance.saveNoticeContacts(nc);
			isTiXing = true;
		}
	}

	private ServiceConnection conn = new ServiceConnection() {
		/** 获取服务对象时的操作 */
		public void onServiceConnected(ComponentName name, IBinder service) {

			countService = ((GetMsgServiceTwo.ServiceBinder) service)
					.getService();
			countService.addReceverListener(ChatPersonActivity.this);
		}

		/** 无法获取到服务对象时的操作 */
		public void onServiceDisconnected(ComponentName name) {

			countService = null;
		}

	};

	private void initView() {

		activityRootView = findViewById(R.id.activityRoot);
		activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						int heightDiff = activityRootView.getRootView()
								.getHeight() - activityRootView.getHeight();
						if (heightDiff > 100) { // 如果高度差超过100像素，就很有可能是有软键盘...
							moreInfoBtn.setVisibility(View.GONE);
							isShowBiaoqing = false;
							isShowMore = false;
							biaoqingControl(isShowBiaoqing);
							moreControl(isShowMore);
						}
					}
				});

		instance = this;
		dbinstance = DBHelper.getInstance(this);
		inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		bigIntent = getIntent();
		if (bigIntent.hasExtra(Constants.TARGETID)) {
			targetId = bigIntent.getStringExtra(Constants.TARGETID);
		}
		if (bigIntent.hasExtra(Constants.TAGETNAME)) {
			targetName = bigIntent.getStringExtra(Constants.TAGETNAME);
		}
		if (bigIntent.hasExtra(Constants.TAGETPHOTO)) {
			targetPhoto = bigIntent.getStringExtra(Constants.TAGETPHOTO);
		}
		if (bigIntent.hasExtra("isKefu")) {
			isKefu = bigIntent.getBooleanExtra("isKefu", false);
		}

		clearunReadNum();

		ptrListView = (PullToRefreshListView) findViewById(R.id.listview);

		mListView = ptrListView.getRefreshableView();
		mListView.setCacheColorHint(getResources().getColor(R.color.trans));
		ptrListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				UserInfoBean.getUserInfo(ChatPersonActivity.this);
				page++;
				// 读取消息历史记录
				UserInfoBean.getUserInfo(ChatPersonActivity.this);
				ArrayList<PrivateChatMessagesEntity> newlist;
				if (isKefu) {
					newlist = dbinstance.queryOtherChatRecord(page * a + "", b
							+ "");
				} else {
					newlist = dbinstance.queryPrivateChatRecord(UserInfoBean
							.getUserInfo(ChatPersonActivity.this).getUid(),
							targetId, page * a + "", b + "");
				}

				if (newlist != null && newlist.size() > 0) {
					for (PrivateChatMessagesEntity privateChatMessagesEntity : newlist) {
						mDataArrays.add(0, privateChatMessagesEntity);
					}
				}

				mAdapter.notifyDataSetChanged(mDataArrays);

				MyApplication.handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						ptrListView.onRefreshComplete();
					}
				}, 500);
			}
		});
		// Ashish Rauniyar
		// ===========================
		rl_back = (RelativeLayout) findViewById(R.id.rl_title_back);
		iv_chatsetting = (ImageView) findViewById(R.id.iv_chatsetting);
		// 录音
		chatting_mode_btn = (ImageView) findViewById(R.id.ivPopUp);
		mBtnRcd = (Button) findViewById(R.id.btn_rcd);
		// 文字
		mEditTextContent = (TextView) findViewById(R.id.et_sendmessage);

		findViewById(R.id.activityRoot).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						mEditTextContent.setCursorVisible(false);// 失去光标
						imm.hideSoftInputFromWindow(
								mEditTextContent.getWindowToken(), 0);

					}
				});
		// 表情
		chatting_biaoqing_focuseBtn = (ImageView) findViewById(R.id.chatting_biaoqing_focuse_btn);

		page_select = (LinearLayout) findViewById(R.id.page_select);
		page0 = (ImageView) findViewById(R.id.page0_select);
		page1 = (ImageView) findViewById(R.id.page1_select);
		page2 = (ImageView) findViewById(R.id.page2_select);
		// 其他
		tv_title_name = (TextView) findViewById(R.id.tv_title_name);
		tv_title_name.setText(targetName);
		expressionImages = Expressions.expressionImgs;
		expressionImageNames = Expressions.expressionImgNames;
		expressionImages1 = Expressions.expressionImgs1;
		expressionImageNames1 = Expressions.expressionImgNames1;
		expressionImages2 = Expressions.expressionImgs2;
		expressionImageNames2 = Expressions.expressionImgNames2;
		// ViewPager
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		// 更多
		moreBtn = (ImageView) findViewById(R.id.chatting_more_btn);
		photoBtn = (TextView) findViewById(R.id.photo);
		take_photoBtn = (TextView) findViewById(R.id.take_photo);
		moreInfoBtn = (LinearLayout) findViewById(R.id.chat_more_info);
		tv_send_position = (TextView) findViewById(R.id.tv_send_position);
		initViewPager();

		tv_send = (TextView) findViewById(R.id.tv_send);

	}

	private void initLisiData() {

		// DBHelper instance = DBHelper.getInstance(getApplicationContext());
		if (isKefu) {
			list = dbinstance.queryOtherChatRecord(page * a + "", b + "");
		} else {
			list = dbinstance.queryPrivateChatRecord(
					UserInfoBean.getUserInfo(ChatPersonActivity.this).getUid(),
					targetId, page * a + "", b + "");
		}

		if (list.size() > 0) {
			for (PrivateChatMessagesEntity entity : list) {

				mDataArrays.add(0, entity);
			}
			// Collections.reverse(mDataArrays);
		}
		mAdapter = new PrivateChatMessagesAdapter(this, mDataArrays);
		mListView.setAdapter(mAdapter);
		mListView.setSelection(mAdapter.getCount() - 1);
	}

	/** 发送消息 */

	private void send(PrivateChatMessagesEntity entity, String content) {
		entity.setUserId(UserInfoBean.getUserInfo(ChatPersonActivity.this)
				.getUid());
		entity.setReceiveName(targetName);
		entity.setReceiveLogo(targetPhoto);

		if (isKefu) {
			entity.setOtherId("1");
			entity.setActionType(Constants.ONREPLY);
		} else {
			entity.setOtherId("0");
			entity.setActionType(Constants.ONTRANSMIT);
		}
		// DBHelper instance = DBHelper.getInstance(this);
		RecentlyContacts rc = new RecentlyContacts();
		if (isKefu) {
			rc.setMessageChatType(Constants.ONREPLY);
			dbinstance.saveChatOther(entity);

		} else {
			rc.setMessageChatType(Constants.ONTRANSMIT);
			dbinstance.saveChatPerson(entity);
		}
		if (dbinstance.queryRecently(gid)) {
			ContentValues cv = new ContentValues();
			cv.put("messageContent", entity.getMessageContent());
			cv.put("messageDate", entity.getMessageDate());
			cv.put("messageType", entity.getMessageType());
			dbinstance.updateRecentlyContacts(cv, gid);
		} else {
			dbinstance.saveRecentlyContacts(ChatEntityUtils.c2r(entity, this));
		}

		mDataArrays.add(entity);
		mAdapter.notifyDataSetChanged(mDataArrays);

		mEditTextContent.setText("");// 清空编辑框数据

		mListView.setSelection(mListView.getCount() - 1);// 发送一条消息时，ListView显示选择最后一项
		PrivateChatMessagesEntity entity_current = new PrivateChatMessagesEntity();

		entity_current.setSendedName(entity.getSendedName());
		entity_current.setSendedId(entity.getSendedId());
		entity_current.setSendedLogo(entity.getSendedLogo());
		entity_current.setReceiveId(entity.getReceiveId());
		entity_current.setReceiveLogo(entity.getReceiveLogo());
		entity_current.setReceiveName(entity.getReceiveName());
		entity_current.setActionType(entity.getActionType());
		entity_current.setVoicelength(entity.getVoicelength());
		entity_current.setBubbleType(entity.getBubbleType());
		entity_current.setMessageDate(entity.getMessageDate());
		entity_current.setOtherId(entity.getOtherId());
		entity_current.setMessageType(entity.getMessageType());
		if (entity.getMessageType().equals(Constants.PICTURE)) {
			entity_current.setMessageContent(content);
		} else {
			entity_current.setMessageContent(entity.getMessageContent());
		}

		// 发送消息
		SocketIOClient client = MyApplication.client;
		if (client != null) {
			JSONArray ja = new JSONArray();
			try {
				ja.put(entity_current.toJSONObject());
				if (MyApplication.isConnect) {
					client.emit(Constants.SENDCHAT, ja);
					Intent br = new Intent(Constants.UPDATECHATLIST);
					sendBroadcast(br);
				} else {
					ToastUtil.makeLongText(this, "无法连接服务器，请稍后再发");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}

	}

	private void addListener() {
		tv_send.setOnClickListener(this);
		mBtnRcd.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					if (RECODE_STATE != RECORD_ING) {
						scanOldFile();
						mr = new AudioRecorder("voice");
						RECODE_STATE = RECORD_ING;
						showVoiceDialog();
						try {
							mr.start();
							mBtnRcd.setText("正在录音");
						} catch (IOException e) {
							e.printStackTrace();
						}
						mythread();
					}

					break;
				case MotionEvent.ACTION_UP:
					if (RECODE_STATE == RECORD_ING) {
						RECODE_STATE = RECODE_ED;
						if (dialog.isShowing()) {
							mBtnRcd.setText("按住开始录音");
							dialog.dismiss();
						}
						try {
							mr.stop();
							voiceValue = 0.0;
						} catch (IOException e) {
							e.printStackTrace();
						}

						if (recodeTime < MIX_TIME) {
							showWarnToast();

							RECODE_STATE = RECORD_NO;
						} else {
							// =====================发送语音
							String contString;
							try {
								UserInfoBean
										.getUserInfo(ChatPersonActivity.this);
								contString = File2Code
										.encodeBase64File(getAmrPath());
								PrivateChatMessagesEntity entity = new PrivateChatMessagesEntity();
								entity.setBubbleType("0");
								entity.setMessageType(VOICE);
								entity.setMessageDate(System
										.currentTimeMillis() + "");
								entity.setSendedName(UserInfoBean.getUserInfo(
										ChatPersonActivity.this).getUserName());
								entity.setSendedLogo(UserInfoBean.getUserInfo(
										ChatPersonActivity.this).getLogo());
								entity.setMessageContent(contString);
								entity.setVoicelength(recodeTime + "");
								entity.setSendedId(UserInfoBean.getUserInfo(
										ChatPersonActivity.this).getUid());
								entity.setReceiveId(targetId);

								send(entity, null);
							} catch (Exception e) {
								e.printStackTrace();
							}

						}
					}

					break;
				}
				return false;
			}
		});

		// 语音文字切换按钮
		chatting_mode_btn.setOnClickListener(this);
		chatting_biaoqing_focuseBtn.setOnClickListener(this);
		moreBtn.setOnClickListener(this);
		photoBtn.setOnClickListener(this);
		take_photoBtn.setOnClickListener(this);
		tv_send_position.setOnClickListener(this);
		rl_back.setOnClickListener(this);
		iv_chatsetting.setOnClickListener(this);
		mEditTextContent.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				String content = mEditTextContent.getText().toString();
				if (StringUtil.isEmpty(content)) {
					tv_send.setVisibility(View.GONE);
					moreBtn.setVisibility(View.VISIBLE);
				} else {
					tv_send.setVisibility(View.VISIBLE);
					moreBtn.setVisibility(View.GONE);
				}
			}
		});
		mEditTextContent.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// 强制显示软键盘
				boolean bool = inputMethodManager.showSoftInput(v,
						InputMethodManager.SHOW_IMPLICIT);// SHOW_FORCED
				if (bool) {

					moreInfoBtn.setVisibility(View.GONE);
					mBtnRcd.setVisibility(View.GONE);
					viewPager.setVisibility(View.GONE);
					page_select.setVisibility(View.GONE);
					mEditTextContent.setVisibility(View.VISIBLE);
				}
				return false;
			}
		});
	}

	// =============================语音============================
	// 录音时显示Dialog
	void showVoiceDialog() {
		dialog = new Dialog(this, R.style.DialogStyle);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		dialog.setContentView(R.layout.my_dialog);
		dialog_img = (ImageView) dialog.findViewById(R.id.dialog_img);
		dialog.show();
	}

	// 录音时间太短时Toast显示
	void showWarnToast() {
		Toast toast = new Toast(this);
		LinearLayout linearLayout = new LinearLayout(this);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		linearLayout.setPadding(20, 20, 20, 20);

		// 定义一个ImageView
		ImageView imageView = new ImageView(this);
		imageView.setImageResource(R.drawable.voice_to_short); // 图标

		TextView mTv = new TextView(this);
		mTv.setText("时间太短   录音失败");
		mTv.setTextSize(14);
		mTv.setTextColor(Color.WHITE);// 字体颜色
		// mTv.setPadding(0, 10, 0, 0);

		// 将ImageView和ToastView合并到Layout中
		linearLayout.addView(imageView);
		linearLayout.addView(mTv);
		linearLayout.setGravity(Gravity.CENTER);// 内容居中
		linearLayout.setBackgroundResource(R.drawable.record_bg);// 设置自定义toast的背景

		toast.setView(linearLayout);
		toast.setGravity(Gravity.CENTER, 0, 0);// 起点位置为中间 100为向下移100dp
		toast.show();
	}

	// 获取文件手机路径
	private String getAmrPath() {
		File file = new File(Environment.getExternalStorageDirectory(),
				"my/voice.amr");
		return file.getAbsolutePath();
	}

	// 录音计时线程
	void mythread() {
		recordThread = new Thread(ImgThread);
		recordThread.start();
	}

	// 录音Dialog图片随声音大小切换
	void setDialogImage() {
		if (voiceValue < 200.0) {
			dialog_img.setImageResource(R.drawable.record_animate_01);
		} else if (voiceValue > 200.0 && voiceValue < 400) {
			dialog_img.setImageResource(R.drawable.record_animate_02);
		} else if (voiceValue > 400.0 && voiceValue < 800) {
			dialog_img.setImageResource(R.drawable.record_animate_03);
		} else if (voiceValue > 800.0 && voiceValue < 1600) {
			dialog_img.setImageResource(R.drawable.record_animate_04);
		} else if (voiceValue > 1600.0 && voiceValue < 3200) {
			dialog_img.setImageResource(R.drawable.record_animate_05);
		} else if (voiceValue > 3200.0 && voiceValue < 5000) {
			dialog_img.setImageResource(R.drawable.record_animate_06);
		} else if (voiceValue > 5000.0 && voiceValue < 7000) {
			dialog_img.setImageResource(R.drawable.record_animate_07);
		} else if (voiceValue > 7000.0 && voiceValue < 10000.0) {
			dialog_img.setImageResource(R.drawable.record_animate_08);
		} else if (voiceValue > 10000.0 && voiceValue < 14000.0) {
			dialog_img.setImageResource(R.drawable.record_animate_09);
		} else if (voiceValue > 14000.0 && voiceValue < 17000.0) {
			dialog_img.setImageResource(R.drawable.record_animate_10);
		} else if (voiceValue > 17000.0 && voiceValue < 20000.0) {
			dialog_img.setImageResource(R.drawable.record_animate_11);
		} else if (voiceValue > 20000.0 && voiceValue < 24000.0) {
			dialog_img.setImageResource(R.drawable.record_animate_12);
		} else if (voiceValue > 24000.0 && voiceValue < 28000.0) {
			dialog_img.setImageResource(R.drawable.record_animate_13);
		} else if (voiceValue > 28000.0) {
			dialog_img.setImageResource(R.drawable.record_animate_14);
		}
	}

	// 录音线程
	private Runnable ImgThread = new Runnable() {

		@Override
		public void run() {
			recodeTime = 0.0f;
			while (RECODE_STATE == RECORD_ING) {
				if (recodeTime >= MAX_TIME && MAX_TIME != 0) {
					imgHandle.sendEmptyMessage(0);
				} else {
					try {
						Thread.sleep(200);
						recodeTime += 0.2;
						if (RECODE_STATE == RECORD_ING) {
							voiceValue = mr.getAmplitude();
							imgHandle.sendEmptyMessage(1);
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}

		Handler imgHandle = new Handler() {
			@Override
			public void handleMessage(Message msg) {

				switch (msg.what) {
				case 0:
					// 录音超过15秒自动停止
					if (RECODE_STATE == RECORD_ING) {
						RECODE_STATE = RECODE_ED;
						if (dialog.isShowing()) {
							dialog.dismiss();
						}
						try {
							mr.stop();
							UserInfoBean.getUserInfo(ChatPersonActivity.this);
							String contString;
							try {
								contString = File2Code
										.encodeBase64File(getAmrPath());
								PrivateChatMessagesEntity entity = new PrivateChatMessagesEntity();
								entity.setBubbleType("0");
								entity.setMessageType(VOICE);
								entity.setMessageDate(System
										.currentTimeMillis() + "");
								entity.setSendedName(UserInfoBean.getUserInfo(
										ChatPersonActivity.this).getUserName());
								entity.setSendedLogo(UserInfoBean.getUserInfo(
										ChatPersonActivity.this).getLogo());
								entity.setMessageContent(contString);
								entity.setVoicelength(recodeTime + "");
								entity.setSendedId(UserInfoBean.getUserInfo(
										ChatPersonActivity.this).getUid());
								entity.setReceiveId(targetId);

								send(entity, null);
							} catch (Exception e) {
								e.printStackTrace();
							}
							voiceValue = 0.0;
						} catch (IOException e) {
							e.printStackTrace();
						}

						if (recodeTime < 1.0) {
							showWarnToast();
						}
						mBtnRcd.setText("按住开始录音");
						RECODE_STATE = RECORD_NO;
					}
					break;
				case 1:
					setDialogImage();
					break;
				default:
					break;
				}

			}
		};
	};

	// 删除老文件
	void scanOldFile() {
		File file = new File(Environment.getExternalStorageDirectory(),
				"my/voice.amr");
		if (file.exists()) {
			file.delete();
		}
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.rl_title_back:
			clearunReadNum();
			this.finish();
			break;
		// 更多
		case R.id.chatting_more_btn:
			inputMethodManager.hideSoftInputFromWindow(
					mEditTextContent.getWindowToken(), 0);
			if (isShowMore) {
				isShowMore = false;
				isLuyin = false;
				isShowBiaoqing = false;
			} else {
				isShowMore = true;
				isLuyin = false;
				isShowBiaoqing = false;

			}

			biaoqingControl(isShowBiaoqing);
			luyinControl(isLuyin);
			moreControl(isShowMore);
			// title右边按钮点击
			break;

		// 相册取照片
		case R.id.photo:

			String state1 = Environment.getExternalStorageState();

			if (state1.equals(Environment.MEDIA_MOUNTED)) {

				Intent i = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(i, PICK_IMAGE);
			} else {
				Toast.makeText(this, "sdcard", Toast.LENGTH_SHORT).show();
			}

			break;
		// 拍照
		case R.id.take_photo:

			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_REMOVED)) {
				Toast.makeText(this, "没有sdCard", 1000).show();
				return;
			}
			Intent intentt = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intentt.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri());
			startActivityForResult(intentt, CAPTURE_IMAGE);
			break;
		// 表情
		case R.id.chatting_biaoqing_focuse_btn:

			inputMethodManager.hideSoftInputFromWindow(
					mEditTextContent.getWindowToken(), 0);
			if (isShowBiaoqing) {
				isShowMore = false;
				isLuyin = false;
				isShowBiaoqing = false;
			} else {
				isShowMore = false;
				isLuyin = false;
				isShowBiaoqing = true;
			}
			biaoqingControl(isShowBiaoqing);
			luyinControl(isLuyin);
			moreControl(isShowMore);
			break;
		// 按住语音
		case R.id.ivPopUp:
			inputMethodManager.hideSoftInputFromWindow(
					mEditTextContent.getWindowToken(), 0);
			if (isLuyin) {
				isShowMore = false;
				isLuyin = false;
				isShowBiaoqing = false;
			} else {
				isShowMore = false;
				isLuyin = true;
				isShowBiaoqing = false;
			}
			biaoqingControl(isShowBiaoqing);
			luyinControl(isLuyin);
			moreControl(isShowMore);
			break;
		case R.id.iv_chatsetting:
			Intent intent = new Intent(this, ChatSettingActivity.class);
			intent.putExtra(Constants.TARGETID, targetId);
			intent.putExtra(Constants.TAGETNAME, targetName);
			intent.putExtra(Constants.TAGETPHOTO, targetPhoto);
			intent.putExtra("isKefu", isKefu);
			startActivityForResult(intent, 200);
			break;
		case R.id.tv_send:
			String content = mEditTextContent.getText().toString();
			if (StringUtil.isEmpty(content)) {
				Toast.makeText(this, "内容不能为空", Toast.LENGTH_SHORT).show();

			} else {
				UserInfoBean.getUserInfo(this);
				PrivateChatMessagesEntity entity = new PrivateChatMessagesEntity();
				entity.setBubbleType("0");
				entity.setMessageType(TEXT);
				entity.setMessageDate(System.currentTimeMillis() + "");
				entity.setSendedName(UserInfoBean.getUserInfo(
						ChatPersonActivity.this).getUserName());
				entity.setSendedLogo(UserInfoBean.getUserInfo(
						ChatPersonActivity.this).getLogo());
				entity.setMessageContent(content);
				entity.setVoicelength("0");
				entity.setSendedId(UserInfoBean.getUserInfo(
						ChatPersonActivity.this).getUid());
				entity.setReceiveId(targetId);
				send(entity, null);

			}

			break;
		case R.id.tv_send_position:
			Intent intenttt = new Intent(this, SendPositionActivity.class);
			intenttt.putExtra("isSend", true);
			intenttt.putExtra("content", MyApplication.locationPosition + ";"
					+ MyApplication.geoLat + ";" + MyApplication.geoLng);
			startActivityForResult(intenttt, 300);
			break;
		default:
			break;
		}

	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		// TODO
		// 相册
		case PICK_IMAGE:
			if (data != null) {
				Bitmap photo = null;
				String[] projection;
				Uri uri = null;
				File f = null;
				String uploadPhotPath = null;
				Bundle extras = data.getExtras();
				if (null != extras) {
					// 为了适配不同的手机需要加多个判断
					if (extras.containsKey("selectedItems")) {
						ArrayList arrayList = extras
								.getParcelableArrayList("selectedItems");
						if (arrayList != null && arrayList.size() > 0)
							uri = Uri.parse(arrayList.get(0) + "");
					}

				} else {
					uri = data.getData();
				}
				if (uri != null) {
					projection = new String[] { MediaColumns.DATA };
					Cursor cursor = managedQuery(uri, projection, null, null,
							null);
					int column_index = cursor
							.getColumnIndexOrThrow(MediaColumns.DATA);
					System.out.println("706-------------camera------"
							+ column_index);
					cursor.moveToFirst();
					uploadPhotPath = cursor.getString(column_index);
					System.out
							.println("camera--------------------561----------- 相册图片的地址--------"
									+ uploadPhotPath);
					BitmapFactory.Options option = new BitmapFactory.Options();
					option.inSampleSize = 2;
					photo = BitmapFactory.decodeFile(uploadPhotPath, option);
				}

				if (photo == null)
					return;

				String bitmapBase64 = ComplexOperation
						.encodeBase64Img(ImageOperate.getBitmapByte(
								ImageOperate.CompressionBigBitmap(photo), "png"));

				PrivateChatMessagesEntity entity = new PrivateChatMessagesEntity();
				UserInfoBean.getUserInfo(this);
				entity.setBubbleType("0");
				entity.setMessageType(PICTURE);
				entity.setMessageDate(System.currentTimeMillis() + "");
				entity.setSendedName(UserInfoBean.getUserInfo(
						ChatPersonActivity.this).getUserName());
				entity.setSendedLogo(UserInfoBean.getUserInfo(this).getLogo());
				entity.setMessageContent(uploadPhotPath);
				entity.setVoicelength("0");
				entity.setSendedId(UserInfoBean.getUserInfo(
						ChatPersonActivity.this).getUid());
				entity.setReceiveId(targetId);
				send(entity, bitmapBase64);
				if (true) {
					saveBitmap(photo);
				} else {
					ImageOperate.destoryBimap(photo);
				}

				// ================调用发送图片的
				isLuyin = false;
				isShowBiaoqing = false;
				isShowMore = false;
				moreControl(isShowMore);
				biaoqingControl(isShowBiaoqing);
				luyinControl(isLuyin);

			}
			break;
		case CAPTURE_IMAGE:
			// 拍照
			selectedImagePath = getImagePath();
			BitmapFactory.Options option = new BitmapFactory.Options();
			option.inSampleSize = 2;
			Bitmap photo = BitmapFactory.decodeFile(selectedImagePath, option);
			if (photo == null)
				return;
			int degree = ImageDispose.readPictureDegree(selectedImagePath);
			photo = ImageDispose.rotaingImageView(degree, photo);
			String bitmapBase64 = ComplexOperation.encodeBase64Img(ImageOperate
					.getBitmapByte(ImageOperate.CompressionBigBitmap(photo),
							"png"));

			PrivateChatMessagesEntity entity = new PrivateChatMessagesEntity();
			UserInfoBean.getUserInfo(this);
			entity.setBubbleType("0");
			entity.setMessageType(PICTURE);
			entity.setMessageDate(System.currentTimeMillis() + "");
			entity.setSendedName(UserInfoBean.getUserInfo(
					ChatPersonActivity.this).getUserName());
			entity.setSendedLogo(UserInfoBean.getUserInfo(this).getLogo());
			entity.setMessageContent(selectedImagePath);
			entity.setVoicelength("0");
			entity.setSendedId(UserInfoBean.getUserInfo(this).getUid());
			entity.setReceiveId(targetId);
			send(entity, bitmapBase64);
			if (true) {
				saveBitmap(photo);
			} else
				ImageOperate.destoryBimap(photo);

			isLuyin = false;
			isShowBiaoqing = false;
			isShowMore = false;
			moreControl(isShowMore);
			biaoqingControl(isShowBiaoqing);
			luyinControl(isLuyin);

			break;
		case 200:
			if (data != null) {
				if (data.hasExtra("delete")) {
					mDataArrays.clear();
					mAdapter.notifyDataSetChanged(mDataArrays);
				}
			}
			break;
		case 300:
			String lat = "",
			lng = "",
			address = "";
			if (data != null) {
				if (data.hasExtra("lat")) {
					lat = data.getStringExtra("lat");
				}
				if (data.hasExtra("lng")) {
					lng = data.getStringExtra("lng");
				}
				if (data.hasExtra("address")) {
					address = data.getStringExtra("address");
				}
				String content = address + ";" + lat + ";" + lng;
				if (StringUtil.isEmpty(address)) {
					Toast.makeText(this, "内容不能为空", Toast.LENGTH_SHORT).show();
				} else {
					UserInfoBean.getUserInfo(this);
					PrivateChatMessagesEntity entityy = new PrivateChatMessagesEntity();
					entityy.setBubbleType("0");
					entityy.setMessageType(Constants.LOCATIONPOSITION);
					entityy.setMessageDate(System.currentTimeMillis() + "");
					entityy.setSendedName(UserInfoBean.getUserInfo(
							ChatPersonActivity.this).getUserName());
					entityy.setSendedLogo(UserInfoBean.getUserInfo(
							ChatPersonActivity.this).getLogo());
					entityy.setMessageContent(content);
					entityy.setVoicelength("0");
					entityy.setSendedId(UserInfoBean.getUserInfo(
							ChatPersonActivity.this).getUid());
					entityy.setReceiveId(targetId);
					send(entityy, null);

				}

			}
			break;

		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	// ================================
	private void initViewPager() {
		LayoutInflater inflater = LayoutInflater.from(this);
		grids = new ArrayList<GridView>();
		gView1 = (GridView) inflater.inflate(R.layout.grid1, null);
		List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();

		for (int i = 0; i < 24; i++) {
			Map<String, Object> listItem = new HashMap<String, Object>();
			listItem.put("image", expressionImages[i]);
			listItems.add(listItem);
		}

		SimpleAdapter simpleAdapter = new SimpleAdapter(
				ChatPersonActivity.this, listItems, R.layout.singleexpression,
				new String[] { "image" }, new int[] { R.id.image });
		gView1.setAdapter(simpleAdapter);
		gView1.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Bitmap bitmap = null;
				bitmap = BitmapFactory.decodeResource(getResources(),
						expressionImages[arg2 % expressionImages.length]);
				ImageSpan imageSpan = new ImageSpan(ChatPersonActivity.this,
						bitmap);
				SpannableString spannableString = new SpannableString(
						expressionImageNames[arg2]);
				spannableString.setSpan(imageSpan, 0,
						expressionImageNames[arg2].length(),
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

				mEditTextContent.append(spannableString);
				System.out.println("edit= " + spannableString);
			}
		});
		grids.add(gView1);

		gView2 = (GridView) inflater.inflate(R.layout.grid2, null);
		grids.add(gView2);

		gView3 = (GridView) inflater.inflate(R.layout.grid3, null);
		grids.add(gView3);
		System.out.println("GridView = " + grids.size());

		PagerAdapter mPagerAdapter = new PagerAdapter() {
			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}

			@Override
			public int getCount() {
				return grids.size();
			}

			@Override
			public void destroyItem(View container, int position, Object object) {
				((ViewPager) container).removeView(grids.get(position));
			}

			@Override
			public Object instantiateItem(View container, int position) {
				((ViewPager) container).addView(grids.get(position));
				return grids.get(position);
			}
		};

		viewPager.setAdapter(mPagerAdapter);
		// viewPager.setAdapter();

		viewPager.setOnPageChangeListener(new GuidePageChangeListener());
	}

	class GuidePageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
			System.out.println("" + arg0);

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			System.out.println("" + arg0);
		}

		@Override
		public void onPageSelected(int arg0) {
			switch (arg0) {
			case 0:
				page0.setImageDrawable(getResources().getDrawable(
						R.drawable.page_focused));
				page1.setImageDrawable(getResources().getDrawable(
						R.drawable.page_unfocused));

				break;
			case 1:
				page1.setImageDrawable(getResources().getDrawable(
						R.drawable.page_focused));
				page0.setImageDrawable(getResources().getDrawable(
						R.drawable.page_unfocused));
				page2.setImageDrawable(getResources().getDrawable(
						R.drawable.page_unfocused));
				List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();

				for (int i = 0; i < 24; i++) {
					Map<String, Object> listItem = new HashMap<String, Object>();
					listItem.put("image", expressionImages1[i]);
					listItems.add(listItem);
				}

				SimpleAdapter simpleAdapter = new SimpleAdapter(
						ChatPersonActivity.this, listItems,
						R.layout.singleexpression, new String[] { "image" },
						new int[] { R.id.image });
				gView2.setAdapter(simpleAdapter);
				gView2.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						Bitmap bitmap = null;
						bitmap = BitmapFactory.decodeResource(getResources(),
								expressionImages1[arg2
										% expressionImages1.length]);
						ImageSpan imageSpan = new ImageSpan(
								ChatPersonActivity.this, bitmap);
						SpannableString spannableString = new SpannableString(
								expressionImageNames1[arg2]);
						spannableString.setSpan(imageSpan, 0,
								expressionImageNames1[arg2].length(),
								Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

						mEditTextContent.append(spannableString);
						System.out.println("edit = " + spannableString);
					}
				});
				break;
			case 2:
				page2.setImageDrawable(getResources().getDrawable(
						R.drawable.page_focused));
				page1.setImageDrawable(getResources().getDrawable(
						R.drawable.page_unfocused));
				page0.setImageDrawable(getResources().getDrawable(
						R.drawable.page_unfocused));
				List<Map<String, Object>> listItems1 = new ArrayList<Map<String, Object>>();

				for (int i = 0; i < 24; i++) {
					Map<String, Object> listItem = new HashMap<String, Object>();
					listItem.put("image", expressionImages2[i]);
					listItems1.add(listItem);
				}

				SimpleAdapter simpleAdapter1 = new SimpleAdapter(
						ChatPersonActivity.this, listItems1,
						R.layout.singleexpression, new String[] { "image" },
						new int[] { R.id.image });
				gView3.setAdapter(simpleAdapter1);
				gView3.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						Bitmap bitmap = null;
						bitmap = BitmapFactory.decodeResource(getResources(),
								expressionImages2[arg2
										% expressionImages2.length]);
						ImageSpan imageSpan = new ImageSpan(
								ChatPersonActivity.this, bitmap);
						SpannableString spannableString = new SpannableString(
								expressionImageNames2[arg2]);
						spannableString.setSpan(imageSpan, 0,
								expressionImageNames2[arg2].length(),
								Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

						mEditTextContent.append(spannableString);
						System.out.println("edit= " + spannableString);
					}
				});
				break;

			}
		}
	}

	// 拍照相关方法
	public Uri setImageUri() {
		// Store image in dcim
		File file = new File(Environment.getExternalStorageDirectory()
				+ "/DCIM/", "image" + new Date().getTime() + ".png");
		Uri imgUri = Uri.fromFile(file);
		this.imgPath = file.getAbsolutePath();
		return imgUri;
	}

	public String getImagePath() {
		return imgPath;
	}

	public String getAbsolutePath(Uri uri) {
		String[] projection = { MediaColumns.DATA };
		@SuppressWarnings("deprecation")
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		if (cursor != null) {
			int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} else
			return null;
	}

	private void saveBitmap(final Bitmap photo) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String path1 = Environment.getExternalStorageDirectory()
						+ "/DCIM/Camera";
				SaveBitmapToSdUtil.scanPhotos(photo, path1,
						System.currentTimeMillis() + "",
						ChatPersonActivity.this);
				ImageOperate.destoryBimap(photo);
			}
		}).start();
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			if (event.getAction() == KeyEvent.ACTION_DOWN
					&& event.getRepeatCount() == 0) {
				clearunReadNum();
				this.finish();
			}
			return true;
		}
		return super.dispatchKeyEvent(event);

	}

	@Override
	protected void onStart() {
		super.onStart();
		LogUtil.e("onStart");
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		LogUtil.e("onRestart");
	}

	@Override
	protected void onResume() {
		super.onResume();
		setNC();
		LogUtil.e("onResume");
	}

	@Override
	protected void onPause() {
		super.onPause();
		LogUtil.e("onPause");
	}

	@Override
	protected void onStop() {
		super.onStop();
		mAdapter.getTM().stop();
		LogUtil.e("onStop");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		countService.removeReceverListener(this);
		this.unbindService(conn);
		inputMethodManager.hideSoftInputFromWindow(
				mEditTextContent.getWindowToken(), 0);
		LogUtil.e("onDestroy");
	}

	@Override
	public void finish() {

		LogUtil.e("finish");
		super.finish();
	}

	public void clearunReadNum() {
		if (dbinstance.queryRecently(gid)) {
			ContentValues cv = new ContentValues();
			cv.put("unReadNumber", "0");
			dbinstance.updateRecentlyContacts(cv, gid);
			Intent intent = new Intent(Constants.UPDATECHATLIST);
			sendBroadcast(intent);
		}
	}

	public void biaoqingControl(boolean isShowBiaoqing) {
		if (isShowBiaoqing) {

			viewPager.setVisibility(View.VISIBLE);
			page_select.setVisibility(View.VISIBLE);
		} else {
			viewPager.setVisibility(View.GONE);
			page_select.setVisibility(View.GONE);
		}
	}

	public void moreControl(boolean isShowMore) {
		if (isShowMore) {
			moreInfoBtn.setVisibility(View.VISIBLE);

		} else {
			moreInfoBtn.setVisibility(View.GONE);

		}
	}

	public void luyinControl(boolean isLuyin) {
		if (isLuyin) {
			mBtnRcd.setVisibility(View.VISIBLE);
			mEditTextContent.setVisibility(View.GONE);
			chatting_mode_btn.setImageResource(R.drawable.chat_pen_style);

		} else {
			mBtnRcd.setVisibility(View.GONE);
			mEditTextContent.setVisibility(View.VISIBLE);
			chatting_mode_btn.setImageResource(R.drawable.chat_voice_style);
		}
	}

	@Override
	public void doResult(final TranObject tro) {
		PrivateChatMessagesEntity entity = (PrivateChatMessagesEntity) tro
				.getObject();
		if (UserInfoBean.getUserInfo(this).getUid()
				.equals(entity.getReceiveId())) {
			if ("0".equals(entity.getOtherId())) {
				if (!isKefu) {
					mDataArrays.add(entity);
				}

			} else {
				if (isKefu) {
					mDataArrays.add(entity);
				}
			}

			MyApplication.handler.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					mAdapter.notifyDataSetChanged(mDataArrays);
					mListView.setSelection(mListView.getCount() - 1);
				}
			});

		}

	}

}
