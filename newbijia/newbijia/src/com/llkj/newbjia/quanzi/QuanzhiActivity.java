package com.llkj.newbjia.quanzi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.logic.ImgFileListActivity;
import com.llkj.newbjia.BaseActivity;
import com.llkj.newbjia.MyApplication;
import com.llkj.newbjia.R;
import com.llkj.newbjia.adpater.QuanzhiAdapter;
import com.llkj.newbjia.adpater.QuanzhiAdapter.MyClicker;
import com.llkj.newbjia.bean.KeyBean;
import com.llkj.newbjia.bean.ResponseBean;
import com.llkj.newbjia.bean.UserInfoBean;
import com.llkj.newbjia.chat.SetChatBgActivity;
import com.llkj.newbjia.friend.DetailInfoActivity;
import com.llkj.newbjia.http.PoCService;
import com.llkj.newbjia.sortlistview.SelectPicPopupWindow;
import com.llkj.newbjia.utils.FinalBitmapUtil;
import com.llkj.newbjia.utils.ImageOperate;
import com.llkj.newbjia.utils.LogUtil;
import com.llkj.newbjia.utils.StringUtil;
import com.llkj.newbjia.utils.ToastUtil;
import com.llkj.newbjia.utils.UploadFile;
import com.llkj.newbjia.utils.Utils;
import com.llkj.newbjia.utils.XListView;
import com.llkj.newbjia.utils.XListView.IXListViewListener;

public class QuanzhiActivity extends BaseActivity implements OnClickListener,
		IXListViewListener, MyClicker {

	SelectPicPopupWindow menuWindow;
	private FinalBitmapUtil fb;

	private XListView listview;// 下拉刷新listView控件

	private ArrayList arrayList;

	private RelativeLayout rl_title_back;// 返回按钮

	// TODO change the desk color.
	private LinearLayout ll_community_desk_in;

	private QuanzhiAdapter adapter;

	private PopupWindow pw, reply_pw;

	private View view;// 评论和赞的视图

	private RelativeLayout rl_pinglun, rl_zan;// 评论、赞

	private ImageView civ_user_desk_myphoto;

	private HashMap map, bigMap, map3;

	private ImageView iv_xiangji;// 相机按钮

	protected static final int ACTION_HIDE_IMM = 300;

	protected static final int REQUEST_CODE_CAMERA = 1;

	protected static final int REQUEST_CODE_GALLERY = 0;

	// TODO added by wang .
	protected static final int REQUEST_CODE_CAMERA_BACKGROUND = 2;

	protected static final int REQUEST_CODE_GALLERY_BACKGROUND = 3;

	protected static final String COMMUNITY_BACKGROUND = "community_background";
	protected static final String COMMUNITY_CONFIG = "community_config";

	private Intent bigIntent;

	private String id, type, name, uid;

	private TextView tv_titile, tv_user_desk_name;// 标题栏

	public static int mRequestId;

	public int mLoadMoreId;

	public int mPenCommentId;

	public int mPenPraiseId;

	public int mDelCircleId;

	private int page = 1;

	private boolean isZan;

	private UploadFile uploadfile;

	private Bitmap bum;

	public static Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_quanzhi);

		initView();
		initListener();
		initData();
	}

	@Override
	protected void onResume() {
		super.onResume();
		setDeskBackGround();
	}

	private void initData() {
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					Toast toast = new Toast(getApplicationContext());

					toast.setGravity(Gravity.CENTER, 0, 0);
					LinearLayout toastView = (LinearLayout) toast.getView();
					ImageView imageCodeProject = new ImageView(
							QuanzhiActivity.this);
					imageCodeProject.setImageResource(R.drawable.yibaocun);
					toast.setView(imageCodeProject);
					toast.show();
					break;

				default:
					break;
				}
			}
		};
		bigIntent = getIntent();

		if (bigIntent.hasExtra("id")) {
			id = bigIntent.getStringExtra("id");
		}
		if (bigIntent.hasExtra("type")) {
			type = bigIntent.getStringExtra("type");
		}
		if (bigIntent.hasExtra("name")) {
			name = bigIntent.getStringExtra("name");
		}
		if ("1".equals(type)) {
			tv_titile.setText(name);
		} else if ("2".equals(type)) {
			tv_titile.setText(name + "圈");
		} else if ("3".equals(type)) {
			tv_titile.setText(name + "社区圈");

		}

		arrayList = new ArrayList();
		fb = FinalBitmapUtil.create(this);
		fb.displayForHeader(civ_user_desk_myphoto,
				UserInfoBean.getUserInfo(this).getLogo());
		tv_user_desk_name.setText(UserInfoBean.getUserInfo(this).getUserName());
		adapter = new QuanzhiAdapter(this, arrayList, this);
		listview.setAdapter(adapter);

		uid = UserInfoBean.getUserInfo(this).getUid();
		page = 1;
		if (StringUtil.isNetworkConnected(this)) {
			mRequestId = mRequestManager.friendPen(uid, page + "", type, id,
					true);
		} else {
			ToastUtil.makeShortText(this, R.string.no_wangluo);
		}
	}

	@SuppressLint("NewApi")
	private void setDeskBackGround() {
		// TODO set the desk background.
		SharedPreferences sp = getSharedPreferences("community_config",
				MODE_PRIVATE);
		String background_path = sp.getString(COMMUNITY_BACKGROUND, "");
		if (!StringUtil.isEmpty(background_path)) {
			BitmapFactory.Options option = new BitmapFactory.Options();
			option.inSampleSize = 2;
			Bitmap bm = BitmapFactory.decodeFile(background_path, option);
			ll_community_desk_in.setBackgroundDrawable(new BitmapDrawable(bm));
		} else {
			ll_community_desk_in.setBackground(getResources().getDrawable(
					R.drawable.front_bg));
		}

	}

	private void initListener() {

		rl_title_back.setOnClickListener(this);

		ll_community_desk_in.setOnClickListener(this);

		listview.setXListViewListener(this);

		listview.setPullLoadEnable(true);

		listview.setPullRefreshEnable(true);

		iv_xiangji.setOnClickListener(this);
		civ_user_desk_myphoto.setOnClickListener(this);

	}

	public void stopXlistview() {

		listview.stopLoadMore();

		listview.stopRefresh();

	}

	private void initView() {
		tv_titile = (TextView) findViewById(R.id.tv_titile);
		listview = (XListView) findViewById(R.id.xlv_content);
		rl_title_back = (RelativeLayout) findViewById(R.id.rl_title_back);
		ll_community_desk_in = (LinearLayout) findViewById(R.id.ll_community_desk_in);

		view = LayoutInflater.from(this).inflate(
				R.layout.item_quanzhi_popwindow, null);

		iv_xiangji = (ImageView) findViewById(R.id.iv_xiangji);

		tv_user_desk_name = (TextView) findViewById(R.id.tv_user_desk_name);
		civ_user_desk_myphoto = (ImageView) findViewById(R.id.civ_user_desk_myphoto);
	}

	private void getPupopDialog(View v) {

		HashMap mapp = (HashMap) v.getTag();

		if (pw != null && pw.isShowing()) {
			pw.dismiss();
		}
		pw = new PopupWindow(view);

		rl_pinglun = (RelativeLayout) view.findViewById(R.id.rl_pinglun);
		rl_zan = (RelativeLayout) view.findViewById(R.id.rl_zan);

		rl_pinglun.setOnClickListener(this);
		rl_zan.setOnClickListener(this);

		rl_pinglun.setTag(mapp);
		rl_zan.setTag(mapp);

		pw.setWidth(LayoutParams.WRAP_CONTENT);
		pw.setHeight(LayoutParams.WRAP_CONTENT);

		pw.setBackgroundDrawable(new BitmapDrawable());
		pw.setOutsideTouchable(true);
		pw.setFocusable(true);
		pw.showAsDropDown(v, StringUtil.dip2px(this, -200),
				StringUtil.dip2px(this, -30));
	}

	@Override
	public void onClick(View v) {

		Intent intent;

		switch (v.getId()) {

		case R.id.rl_title_back:
			this.finish();
			break;

		case R.id.ll_community_desk_in:
			// to get the result ,change the background of the desk .
			Intent intent1 = new Intent(this, SetChatBgActivity.class);
			startActivityForResult(intent1, 200);
			break;
		case R.id.civ_user_desk_myphoto:
			//TODO just consume the click action.(something needs adding)
			break;
		// change it into method .(may be i should delete it !!)
		case R.id.rl_zan:
			map = null;
			bigMap = (HashMap) v.getTag();
			String love = bigMap.get("love") + "";
			String iiid = bigMap.get("id") + "";
			if ("0".equals(love)) {
				isZan = true;
				mPenPraiseId = mRequestManager.penPraise(uid, iiid, 1 + "",
						true);
			} else {
				isZan = false;
				mPenPraiseId = mRequestManager.penPraise(uid, iiid, 2 + "",
						true);
			}
			StringUtil.dismiss(pw);
			break;
		case R.id.rl_pinglun:
			map = null;
			bigMap = (HashMap) v.getTag();
			// 该圈子id
			String iid = bigMap.get("id") + "";
			// 发表该圈子id
			String fid = bigMap.get("user_id") + "";
			to_name = "";
			sendCommentDialog(iid, uid, uid, to_name);
			StringUtil.dismiss(pw);
			break;
		case R.id.iv_xiangji:
			// intent = new Intent(this, PublishActivity.class);
			// intent.putExtra("type", type);
			// intent.putExtra("id", id);
			// startActivityForResult(intent, 100);

			menuWindow = new SelectPicPopupWindow(this, itemsOnClick);
			menuWindow.showAtLocation(this.findViewById(R.id.iv_xiangji),
					Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

			break;
		default:
			break;
		}

	}

	private OnClickListener itemsOnClick = new OnClickListener() {

		public void onClick(View v) {
			menuWindow.dismiss();
			switch (v.getId()) {
			case R.id.btn_take_photo:
				Intent intent = Utils.photo(getApplicationContext());
				startActivityForResult(intent, REQUEST_CODE_CAMERA);
				break;
			case R.id.btn_pick_photo:
				Intent intent1 = new Intent(getApplicationContext(),
						ImgFileListActivity.class);
				startActivityForResult(intent1, REQUEST_CODE_GALLERY);
				break;
			default:
				break;
			}

		}

	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case REQUEST_CODE_CAMERA:
				String path1;
				try {
					path1 = ImageOperate.revitionImageSize(Utils.path, this);
					Intent intent = new Intent(this, PublishActivity.class);
					intent.putExtra("path1", path1);
					intent.putExtra("type", type);
					intent.putExtra("id", id);
					startActivityForResult(intent, 100);
				} catch (IOException e) {
					e.printStackTrace();
				}

				break;
			case REQUEST_CODE_GALLERY:
				Intent intent1 = new Intent(this, PublishActivity.class);

				intent1.putStringArrayListExtra("files",
						data.getStringArrayListExtra("files"));
				intent1.putExtra("type", type);
				intent1.putExtra("id", id);
				startActivityForResult(intent1, 100);

				break;
			}
		}
		if (data != null) {
			switch (requestCode) {

			case 100:
				page = 1;
				if (StringUtil.isNetworkConnected(this)) {
					mRequestId = mRequestManager.friendPen(uid, page + "",
							type, id, true);
				} else {
					ToastUtil.makeShortText(this, R.string.no_wangluo);
				}
				break;
			case 200:
				// TODO
				String bgPath;
				if (data.hasExtra("path")) {
					bgPath = data.getStringExtra("path");
				}else{
					bgPath = "";
				}
				SharedPreferences sp =  getSharedPreferences(COMMUNITY_CONFIG, MODE_PRIVATE);
				Editor editor =  sp.edit();
				editor.putString(COMMUNITY_BACKGROUND, bgPath);
				editor.commit();
				break;

			default:
				break;
			}
		}
	}

	@Override
	public void onRefresh() {

		page = 1;
		if (StringUtil.isNetworkConnected(this)) {
			mRequestId = mRequestManager.friendPen(uid, page + "", type, id,
					true);
		} else {
			ToastUtil.makeShortText(this, R.string.no_wangluo);
		}

	}

	@Override
	public void onLoadMore() {

		page++;
		if (StringUtil.isNetworkConnected(this)) {
			mLoadMoreId = mRequestManager.friendPen(uid, page + "", type, id,
					true);
		} else {
			ToastUtil.makeShortText(this, R.string.no_wangluo);
		}
	}

	@Override
	public void myClick(View v, int type) {

		switch (type) {
		case 0:
			getPupopDialog(v);
			break;
		case 1:
			bigMap = null;
			map = (HashMap) v.getTag();
			String id = map.get("id") + "";
			String fid = map.get("from_id") + "";
			to_name = map.get("from_name") + "";
			sendCommentDialog(id, uid, fid, to_name);
			StringUtil.dismiss(pw);
			break;
		case 2:

			// Intent intent = new Intent(this, OtherQuanziActivity.class);
			//
			// intent.putExtra("id", 0+"");
			// intent.putExtra("type", 1+"");
			// intent.putExtra("name", name);

			String fidd = v.getTag() + "";
			Intent intent = new Intent(this, DetailInfoActivity.class);
			intent.putExtra(KeyBean.KEY_FID, fidd);
			ToastUtil.makeLongText(getApplicationContext(), fidd);
			startActivity(intent);

			break;
		case 3:
			map3 = (HashMap) v.getTag();
			String idString = map3.get("id") + "";
			if (!StringUtil.isEmpty(idString)) {
				mDelCircleId = mRequestManager.delCircle(uid, idString, true);
			}
			break;
		case 4:
			praise(v);
			break;
		case 5:
			comment(v);
			break;
		default:
			break;
		}
	}

	private String content;
	private String add_time;
	private String from_id;
	private String from_name;
	private String to_id;
	private String to_name;

	private void sendCommentDialog(final String commentId, final String uid,
			final String fid, String to_name) {
		if (reply_pw != null && reply_pw.isShowing()) {
			reply_pw.dismiss();
		} else {
			final View view = LayoutInflater.from(this).inflate(
					R.layout.reply_pop_window, null);
			reply_pw = new PopupWindow(view,
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.MATCH_PARENT);
			final EditText et_replyInput = (EditText) view
					.findViewById(R.id.et_input);
			et_replyInput.setHint("@" + to_name);
			InputMethodManager imm = (InputMethodManager) et_replyInput
					.getContext()
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
			// 下面这行代码的作用:输入法在弹出的时候整个Layout就是重新编排布局，重新分配多余空间，输入法退出的时候同理。
			reply_pw.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
			Button btn_send = (Button) view.findViewById(R.id.btn_send);
			btn_send.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String contentt = et_replyInput.getText().toString().trim();

					if (StringUtil.isEmpty(contentt)) {
						ToastUtil.makeLongText(QuanzhiActivity.this,
								R.string.contentnotisnull);
						return;
					}
					content = contentt;
					from_id = uid;
					from_name = UserInfoBean.getUserInfo(QuanzhiActivity.this)
							.getUserName();
					to_id = fid;

					mPenCommentId = mRequestManager.penComment(uid, commentId,
							fid, contentt, true);
					MyApplication.handler.post(new Runnable() {

						@Override
						public void run() {
							hideSoft();
						}
					});
					reply_pw.dismiss();
				}
			});
			reply_pw.setOutsideTouchable(true);
			reply_pw.setFocusable(true);
			view.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					int height = view.findViewById(R.id.ll_reply_input)
							.getTop();
					int y = (int) event.getY();
					if (y < height) {
						MyApplication.handler.post(new Runnable() {

							@Override
							public void run() {
								hideSoft();
							}
						});
						reply_pw.dismiss();
					}
					return true;
				}
			});
			// 此行代码可以在返回键按下的时候,使pw消失.
			reply_pw.setBackgroundDrawable(new BitmapDrawable());
			reply_pw.setAnimationStyle(R.style.PopupAnimation);
			reply_pw.showAtLocation(this.getWindow().getDecorView(),
					Gravity.BOTTOM, 0, 0);
		}
	}

	public void hideSoft() {
		LogUtil.e("hideSoft");
		InputMethodManager imm = (InputMethodManager) this
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(this.getWindow().getDecorView()
				.getWindowToken(), 0);
	}

	@Override
	public void onRequestFinished(int requestId, int resultCode, Bundle payload) {
		super.onRequestFinished(requestId, resultCode, payload);
		stopXlistview();
		if (resultCode == PoCService.SUCCESS_CODE) {
			if (mRequestId == requestId) {
				int result = payload.getInt(ResponseBean.RESPONSE_STATE);
				if (result == 1) {
					arrayList.clear();
					ArrayList newList = payload
							.getParcelableArrayList(ResponseBean.RESPONSE_LIST);
					if (newList != null && newList.size() > 0) {
						arrayList.addAll(newList);
						adapter.notifyDataSetChanged(arrayList);
					} else {
						ToastUtil.makeShortText(this, R.string.no_data);
					}
				} else {
					String msg = payload
							.getString(ResponseBean.RESPONSE_MESSAGE);
					ToastUtil.makeShortText(this, msg);
				}

			}
			if (mLoadMoreId == requestId) {
				int result = payload.getInt(ResponseBean.RESPONSE_STATE);
				if (result == 1) {
					ArrayList newList = payload
							.getParcelableArrayList(ResponseBean.RESPONSE_LIST);
					arrayList.addAll(newList);
					if (newList != null && newList.size() > 0) {
						arrayList.addAll(newList);
						adapter.notifyDataSetChanged(arrayList);
					} else {
						ToastUtil.makeShortText(this, R.string.no_data);
					}
				} else {
					String msg = payload
							.getString(ResponseBean.RESPONSE_MESSAGE);
					ToastUtil.makeShortText(this, msg);
				}

			}
			if (mPenCommentId == requestId) {
				int result = payload.getInt(ResponseBean.RESPONSE_STATE);
				if (result == 1) {
					ArrayList comments = null;
					HashMap comMap = new HashMap();
					if (map != null && map.containsKey("position")) {
						int position = Integer.parseInt(map.get("position")
								+ "");
						HashMap mapp = (HashMap) arrayList.get(position);
						comments = (ArrayList) mapp.get("comments");
						comMap.put("from_id", from_id);
						comMap.put("from_name", from_name);
						comMap.put("to_id", to_id);
						comMap.put("to_name", to_name);
						comMap.put("content", content);
					} else {
						comments = (ArrayList) bigMap.get("comments");
						comMap.put("from_id", from_id);
						comMap.put("from_name", from_name);
						comMap.put("to_id", to_id);
						to_name = bigMap.get("name") + "";
						comMap.put("to_name", to_name);
						comMap.put("content", content);
					}

					comments.add(comMap);
					adapter.notifyDataSetChanged();
					ToastUtil.makeLongText(this, "评论成功");

				} else {
					String msg = payload
							.getString(ResponseBean.RESPONSE_MESSAGE);
					ToastUtil.makeShortText(this, msg);
				}

			}
			if (mPenPraiseId == requestId) {
				int result = payload.getInt(ResponseBean.RESPONSE_STATE);
				if (result == 1) {
					if (isZan) {
						ToastUtil.makeShortText(this, "点赞成功");
						bigMap.put("love", "1");
						int number = Integer
								.parseInt(bigMap.get("number") + "");
						if (number > -1) {
							number++;
						}
						bigMap.put("number", number);
						adapter.notifyDataSetChanged();
					} else {
						ToastUtil.makeShortText(this, "取消成功");
						bigMap.put("love", "0");
						int number = Integer
								.parseInt(bigMap.get("number") + "");
						if (number > 0) {
							number--;
						} else {
							number = 0;
						}
						bigMap.put("number", number);
						adapter.notifyDataSetChanged();
					}
				} else {
					String msg = payload
							.getString(ResponseBean.RESPONSE_MESSAGE);
					ToastUtil.makeShortText(this, msg);
				}
			} else if (mDelCircleId == requestId) {
				int result = payload.getInt(ResponseBean.RESPONSE_STATE);
				if (result == 1) {
					arrayList.remove(map3);
					adapter.notifyDataSetChanged();
				}

			}

		}
	}

	// ADD by wang.
	private void praise(View v) {
		map = null;
		bigMap = (HashMap) v.getTag();
		String love = bigMap.get("love") + "";
		String iiid = bigMap.get("id") + "";
		if ("0".equals(love)) {
			isZan = true;
			mPenPraiseId = mRequestManager.penPraise(uid, iiid, 1 + "", true);
		} else {
			isZan = false;
			mPenPraiseId = mRequestManager.penPraise(uid, iiid, 2 + "", true);
		}
		StringUtil.dismiss(pw);

	}

	private void comment(View v) {
		map = null;
		bigMap = (HashMap) v.getTag();
		// 该圈子id
		String iid = bigMap.get("id") + "";
		// 发表该圈子id
		String fid = bigMap.get("user_id") + "";
		to_name = "";
		sendCommentDialog(iid, uid, uid, to_name);
		StringUtil.dismiss(pw);

	}
}
