package com.llkj.newbjia.friend;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.llkj.newbjia.BaseActivity;
import com.llkj.newbjia.R;
import com.llkj.newbjia.bean.KeyBean;
import com.llkj.newbjia.bean.ResponseBean;
import com.llkj.newbjia.bean.UserInfoBean;
import com.llkj.newbjia.chat.ChatPersonActivity;
import com.llkj.newbjia.chat.Constants;
import com.llkj.newbjia.http.PoCService;
import com.llkj.newbjia.quanzi.OtherQuanziActivity;
import com.llkj.newbjia.utils.FinalBitmapUtil;
import com.llkj.newbjia.utils.StringUtil;
import com.llkj.newbjia.utils.ToastUtil;

/**
 * 关于我们页
 * 
 * @author John
 * 
 */
public class DetailInfoActivity extends BaseActivity implements OnClickListener {
	private TextView tv_cityquan, tv_sheququan, tv_signature, tv_username_two,
			tv_title_right, tv_nickname;
	private ImageView iv_logo;
	private int mRequestId, mAddFriendId;
	private Intent bigIntent;
	private String uid, id, type, user_name, signature, logo, key, fid;
	private Button bt_do_what;
	private FinalBitmapUtil fb;
	private RelativeLayout rl_bg;

	private ImageView img1, img2, img3;
	private String[] url = new String[5];
	private LinearLayout xiangce;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.layout_detail_info);
		setTitle(R.string.detailinfo, true, R.string.kong, true,
				R.string.setting);
		initView();
		initData();
		initListener();
		uid = UserInfoBean.getUserInfo(this).getUid();
		if (StringUtil.isNetworkConnected(this)) {
			if (!StringUtil.isEmpty(uid)) {
				if (!StringUtil.isEmpty(fid)) {
					mRequestId = mRequestManager.friendDesc(uid, fid, true);
				} else if (!StringUtil.isEmpty(key)) {
					mRequestId = mRequestManager.friendSearch(uid, key, true);
				}
			} else {
				ToastUtil.makeShortText(this, R.string.xiandenglu);
			}

		} else {
			ToastUtil.makeShortText(this, R.string.no_wangluo);
		}
	}

	private void initView() {
		tv_cityquan = (TextView) findViewById(R.id.tv_cityquan);
		tv_sheququan = (TextView) findViewById(R.id.tv_sheququan);

		tv_username_two = (TextView) findViewById(R.id.tv_nickname);
		tv_title_right = (TextView) findViewById(R.id.tv_title_right);
		tv_nickname = (TextView) findViewById(R.id.tv_nickname);
		tv_signature = (TextView) findViewById(R.id.tv_signature);

		iv_logo = (ImageView) findViewById(R.id.iv_logo);

		bt_do_what = (Button) findViewById(R.id.bt_do_what);

		rl_bg = (RelativeLayout) findViewById(R.id.rl_bg);
		xiangce = (LinearLayout) findViewById(R.id.my_xiangce);
		img1 = (ImageView) findViewById(R.id.img1);
		img2 = (ImageView) findViewById(R.id.img2);
		img3 = (ImageView) findViewById(R.id.img3);
	}

	private void initData() {
		bigIntent = getIntent();
		fb = FinalBitmapUtil.create(this);
		if (bigIntent.hasExtra(KeyBean.KEY_FID)) {
			fid = bigIntent.getStringExtra(KeyBean.KEY_FID);
		}
		if (bigIntent.hasExtra("key")) {
			key = bigIntent.getStringExtra("key");
		}
	}

	private void initListener() {
		tv_sheququan.setOnClickListener(this);
		tv_cityquan.setOnClickListener(this);
		bt_do_what.setOnClickListener(this);
		xiangce.setOnClickListener(this);
		tv_title_right.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.my_xiangce:
			Intent intent1 = new Intent(this, OtherQuanziActivity.class);

			intent1.putExtra("id", 0 + "");
			intent1.putExtra("type", 1 + "");
			intent1.putExtra("name", getIntent().getStringExtra("name"));
			startActivity(intent1);
			break;
		case R.id.tv_title_right:
			startActivity(new Intent(this, DialogActivity.class));
			break;
		// TODO have not get the function.
		case R.id.tv_cityquan:

			break;
		case R.id.tv_sheququan:

			break;
		case R.id.bt_do_what:
			if ("0".equals(type)) {
				mAddFriendId = mRequestManager.friendAdd(uid, fid, true);
			} else {
				Intent intent = new Intent(this, ChatPersonActivity.class);
				intent.putExtra(Constants.TARGETID, fid);
				intent.putExtra(Constants.TAGETNAME, user_name);
				intent.putExtra(Constants.TAGETPHOTO, logo);
				startActivity(intent);
			}
			break;
		}
	}

	@Override
	public void onRequestFinished(int requestId, int resultCode, Bundle payload) {
		super.onRequestFinished(requestId, resultCode, payload);
		if (resultCode == PoCService.SUCCESS_CODE) {
			if (mRequestId == requestId) {
				int result = payload.getInt(ResponseBean.RESPONSE_STATE);
				if (result == 1) {
					rl_bg.setVisibility(View.VISIBLE);
					type = payload.getString(ResponseBean.RESPONSE_TYPE);
					fid = payload.getString(ResponseBean.RESPONSE_UID);
					if (fid.equals(uid)) {
						bt_do_what.setVisibility(View.INVISIBLE);
					} else {
						bt_do_what.setVisibility(View.VISIBLE);
						if ("0".equals(type)) {
							bt_do_what.setText("加好友");
						} else {
							bt_do_what.setText("发消息");
						}
					}
					user_name = payload
							.getString(ResponseBean.RESPONSE_USER_NAME);
					signature = payload
							.getString(ResponseBean.RESPONSE_SIGNATURE);
					tv_username_two.setText("昵称：" + user_name);
					tv_signature.setText("个性签名：" + signature);
					logo = payload.getString(ResponseBean.RESPONSE_LOGO);
					String myurl = payload.getString("near_by_time");
					// TODO add examination.
					if (myurl != null) {
						myurl = myurl.replace("[", "");
						myurl = myurl.replace("]", "");
						myurl = myurl.replace("\"", "");
						myurl = myurl.replace("\\", "");

						url = myurl.split(",");

						fb.displayForHeader(iv_logo, logo);
						if (url.length > 0) {
							Log.i("hehe", url[0] + logo);
							fb.displayForHeader(img1, url[0]);
						}

						if (url.length > 1) {
							fb.displayForHeader(img2, url[1]);
						}
						if (url.length > 2) {
							fb.displayForHeader(img3, url[2]);
						}

					}

				} else {
					String msg = payload
							.getString(ResponseBean.RESPONSE_MESSAGE);
					ToastUtil.makeShortText(this, msg);
					rl_bg.setVisibility(View.INVISIBLE);
				}

			}
			if (mAddFriendId == requestId) {
				int result = payload.getInt(ResponseBean.RESPONSE_STATE);
				if (result == 1) {
					ToastUtil.makeShortText(this, "已发送请等待");
				} else {
					String msg = payload
							.getString(ResponseBean.RESPONSE_MESSAGE);
					ToastUtil.makeShortText(this, msg);
				}

			}

		}
	}

}
