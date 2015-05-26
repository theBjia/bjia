package com.llkj.newbjia.chat;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.llkj.db.DBHelper;
import com.llkj.db.NoticeContacts;
import com.llkj.newbjia.BaseActivity;
import com.llkj.newbjia.R;
import com.llkj.newbjia.bean.UserInfoBean;
import com.llkj.newbjia.utils.FinalBitmapUtil;
import com.llkj.newbjia.utils.SlidButton;
import com.llkj.newbjia.utils.ToastUtil;
import com.llkj.newbjia.utils.SlidButton.OnChangedListener;

/**
 * 关于我们页
 * 
 * @author John
 * 
 */
public class ChatSettingActivity extends BaseActivity implements
		OnClickListener {
	private TextView tv_setchatbg, tv_clear_chatcontent, tv_username;
	private TextView tv_nickname;
	private DBHelper dbinstance;
	private Intent bigIntent;
	private String targetId, targetName, targetPhoto, bgPath, gid;
	private boolean isKefu;
	private SlidButton su_Shake;
	private boolean isTiXing;
	private ImageView iv_photo,iv_user_modify;
	private LinearLayout ll_title_back;
	private FinalBitmapUtil fbu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_chatsetting);
		initView();
		initData();
		initListener();
	}

	private void initView() {
		tv_setchatbg = (TextView) findViewById(R.id.tv_setchatbg);
		tv_clear_chatcontent = (TextView) findViewById(R.id.tv_clear_chatcontent);
		su_Shake = (SlidButton) findViewById(R.id.su_Shake);
		ll_title_back = (LinearLayout) findViewById(R.id.ll_title_back);
		tv_username = (TextView) findViewById(R.id.tv_username);
		tv_nickname = (TextView) findViewById(R.id.tv_nickname);

		iv_photo = (ImageView) findViewById(R.id.iv_photo);
		iv_user_modify = (ImageView) findViewById(R.id.tv_chat_username_modify);
	}

	private void initData() {
		fbu = FinalBitmapUtil.create(this);
		dbinstance = DBHelper.getInstance(this);
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
		tv_username.setText(targetName);
		//TODO init the data.
		if (isKefu) {
			iv_photo.setImageResource(R.drawable.chat_xitongxiaoxi);
		} else {
			fbu.displayForHeader(iv_photo, targetPhoto);
		}

		if (isKefu) {
			gid = UserInfoBean.getUserInfo(this).getUid();
		} else {
			gid = targetId;
		}

		NoticeContacts nc = dbinstance.queryNoticeContacts(gid);
		if ("0".equals(nc.getN_newMsgRemind())) {
			isTiXing = false;
		} else {
			isTiXing = true;
		}
		su_Shake.setCheck(isTiXing);
		su_Shake.SetOnChangedListener(new OnChangedListener() {

			@Override
			public void OnChanged(boolean CheckState) {
				// TODO Auto-generated method stub
				isTiXing = CheckState;
				ContentValues cv = new ContentValues();
				if (isTiXing) {
					cv.put("N_newMsgRemind", "1");
				} else {
					cv.put("N_newMsgRemind", "0");
				}

				dbinstance.updateNoticeContacts(cv, gid);
			}
		});
	}

	private void initListener() {
		tv_setchatbg.setOnClickListener(this);
		tv_clear_chatcontent.setOnClickListener(this);
		ll_title_back.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.tv_setchatbg:
			Intent intent = new Intent(this, SetChatBgActivity.class);
			startActivityForResult(intent, 200);
			break;
		case R.id.tv_clear_chatcontent:
			new AlertDialog.Builder(this)
					.setTitle("警告")
					.setMessage("删除聊天记录将无法恢复，请谨慎操作")
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();

								}
							})

					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									boolean isOkDelete;
									if (isKefu) {
										isOkDelete = dbinstance
												.deleteOtherChat();
									} else {
										isOkDelete = dbinstance
												.deletePrivateChat(
														UserInfoBean
																.getUserInfo(
																		ChatSettingActivity.this)
																.getUid(),
														targetId);
										finish();
									}
									if (isOkDelete) {
										ToastUtil.makeLongText(
												ChatSettingActivity.this,
												R.string.caozuochenggong);
										Intent data = new Intent();
										data.putExtra("delete", true);
										setResult(RESULT_OK, data);
										finish();

									} else {
										ToastUtil.makeLongText(
												ChatSettingActivity.this,
												R.string.caozuoshibai);
									}
								}
							}).show();
			break;
		case R.id.ll_title_back:
			this.finish();
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (data != null) {
			switch (requestCode) {
			case 200:
				
				if (data.hasExtra("path")) {
					
					bgPath = data.getStringExtra("path");
					
					ContentValues cv = new ContentValues();
					
					cv.put("backgroundPic", bgPath);
					
					boolean setchatbg = dbinstance
							.updateNoticeContacts(cv, gid);
					
					if (setchatbg) {
						ToastUtil.makeLongText(this, R.string.caozuochenggong);
					} else {
						ToastUtil.makeLongText(this, R.string.caozuoshibai);
					}

				}
				break;
				
			default:
				break;
			}
		}
	}

}
