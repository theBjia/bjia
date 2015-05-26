package com.llkj.newbjia.mybijia;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.llkj.newbjia.BaseActivity;
import com.llkj.newbjia.MyApplication;
import com.llkj.newbjia.R;
import com.llkj.newbjia.bean.ResponseBean;
import com.llkj.newbjia.bean.UserInfoBean;
import com.llkj.newbjia.chat.Constants;
import com.llkj.newbjia.http.PoCService;
import com.llkj.newbjia.http.UrlConfig;
import com.llkj.newbjia.quanzi.QuanzhiActivity;
import com.llkj.newbjia.setting.HelpListActivity;
import com.llkj.newbjia.utils.FinalBitmapUtil;
import com.llkj.newbjia.utils.ImageOperate;
import com.llkj.newbjia.utils.StringUtil;
import com.llkj.newbjia.utils.ToastUtil;
import com.llkj.newbjia.utils.UploadFile;
import com.llkj.newbjia.utils.Utils;
import com.llkj.newbjia.utils.UploadFile.OnUploadFileForResultListener;

/**
 * 个人中心页
 * 
 * @author John
 * 
 */
public class MyCenterActivity extends BaseActivity implements OnClickListener,
		OnUploadFileForResultListener {
	// 用户头像
	// 用户名 用户等级 用户电话 城市圈 社区圈 好友圈 个性签名
	RelativeLayout rl_UserName, rl_Level, rl_Phone, rl_Citi, rl_UserCommunity,
			rl_UserFriendRing, rl_Sign, ll_Portrait;

	private TextView tv_UName, tvUserLevel, tvPhoneNumber, tvUserCityRing,
			tvUserCommunityRing, tvUserFriendRing, tvUserIndividuality;
	private int mPersonDescTwo, mRequestId;
	private Intent bigIntent;
	private String uid;
	private String name;
	private static final int REQUEST_CODE_CAMERA = 0;
	private static final int REQUEST_CODE_GALLERY = 1;
	public static final int PHOTORESOULT = 3;// 结果
	private UploadFile uploadfile;
	private Bitmap bum;
	private ImageView imPortrait;
	private FinalBitmapUtil fb;
	private String user_path, user_name, url, username, mobile_phone,
			signature, city, community, user_rank;

	private Button btnLogout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_center);
		setTitle(R.string.mycenter, true, R.string.kong, false, R.string.kong);
		initView();
		initData();
		initListener();
		mPersonDescTwo = mRequestManager.getPersonDesc(uid, true);
	}

	private void initView() {
		// TODO Auto-generated method stub
		btnLogout = (Button) findViewById(R.id.btn_logout1);
		btnLogout.setOnClickListener(this);
		ll_Portrait = (RelativeLayout) findViewById(R.id.ll_Portrait);
		rl_UserName = (RelativeLayout) findViewById(R.id.rl_UserName);
		rl_Level = (RelativeLayout) findViewById(R.id.rl_Level);
		rl_Phone = (RelativeLayout) findViewById(R.id.rl_Phone);
		rl_Citi = (RelativeLayout) findViewById(R.id.rl_Citi);
		rl_UserCommunity = (RelativeLayout) findViewById(R.id.rl_UserCommunity);
		rl_UserFriendRing = (RelativeLayout) findViewById(R.id.rl_UserFriendRing);
		rl_Sign = (RelativeLayout) findViewById(R.id.rl_Sign);
		tv_UName = (TextView) findViewById(R.id.tv_UName);
		tvUserLevel = (TextView) findViewById(R.id.tvUserLevel);
		tvPhoneNumber = (TextView) findViewById(R.id.tvPhoneNumber);
		tvUserCityRing = (TextView) findViewById(R.id.tvUserCityRing);
		tvUserCommunityRing = (TextView) findViewById(R.id.tvUserCommunityRing);
		tvUserFriendRing = (TextView) findViewById(R.id.tvUserFriendRing);
		tvUserIndividuality = (TextView) findViewById(R.id.tvUserIndividuality);

		imPortrait = (ImageView) findViewById(R.id.imPortrait);
	}

	private void initData() {
		fb = FinalBitmapUtil.create(this);
		uid = UserInfoBean.getUserInfo(this).getUid();
		uploadfile = new UploadFile();
		uploadfile.setListener(this);
	}

	private void initListener() {
		ll_Portrait.setOnClickListener(this);
		rl_UserName.setOnClickListener(this);
		rl_Level.setOnClickListener(this);
		rl_Phone.setOnClickListener(this);
		rl_Citi.setOnClickListener(this);
		rl_UserCommunity.setOnClickListener(this);
		rl_UserFriendRing.setOnClickListener(this);
		rl_Sign.setOnClickListener(this);
		ll_Portrait
				.setOnCreateContextMenuListener(new Button.OnCreateContextMenuListener() {

					public void onCreateContextMenu(ContextMenu menu, View v,
							ContextMenuInfo menuInfo) {
						menu.setHeaderTitle("选择图片");
						menu.add(0, 0, 0, "拍照");
						menu.add(0, 1, 1, "从相机获取");
					}
				});
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if ("0".equals(item.getItemId() + "")) {
			getAvatarFromCamera();
		} else if ("1".equals(item.getItemId() + "")) {
			getAvatarFromGallery();
		}

		return super.onContextItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {

			switch (requestCode) {
			case REQUEST_CODE_CAMERA:
				String path1;
				try {
					path1 = ImageOperate.revitionImageSize(Utils.path, this);
					File picture = new File(path1);
					Utils.startPhotoZoom(this, Uri.fromFile(picture));
				} catch (IOException e) {
					e.printStackTrace();
				}

				break;
			case REQUEST_CODE_GALLERY:
				if (data != null) {
					Uri selectedImage = data.getData();
					String[] filePathColumns = { MediaStore.Images.Media.DATA };
					Cursor c = this.getContentResolver().query(selectedImage,
							filePathColumns, null, null, null);
					c.moveToFirst();
					int columnIndex = c.getColumnIndex(filePathColumns[0]);
					String picturePath = c.getString(columnIndex);
					try {
						picturePath = ImageOperate.revitionImageSize(
								picturePath, this);
						File file = new File(picturePath);
						Utils.startPhotoZoom(this, Uri.fromFile(file));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else {

					ToastUtil.makeShortText(this, "文件错误");
				}
				break;
			case PHOTORESOULT:
				Bundle extras = data.getExtras();
				if (extras != null) {
					Bitmap photo = extras.getParcelable("data");
					showWaitDialog();
					uploadfile.uploadImg(this, photo, UploadFile.TYPE_TWO);
				}
			case 10:
				if (data != null) {
					tv_UName.setText(UserInfoBean.getUserInfo(this)
							.getUserName());
					Intent dataa = new Intent();
					data.putExtra(Constants.REFRESH, true);
					setResult(RESULT_OK, dataa);
				}
				break;
			case 20:
				if (data != null) {
					tvUserIndividuality.setText(UserInfoBean.getUserInfo(this)
							.getSignature());
				}
				break;
			default:
				break;
			}
		}
	}

	/**
	 * 通过相册获取头像
	 */
	private void getAvatarFromGallery() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(intent, REQUEST_CODE_GALLERY);
	}

	/**
	 * 通过拍照获取头像
	 */
	private void getAvatarFromCamera() {
		Intent intent = Utils.photo(this);
		startActivityForResult(intent, REQUEST_CODE_CAMERA);
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.btn_logout1:
			MyApplication.showAlertDialog(MyCenterActivity.this);
			break;

		case R.id.ll_Portrait:
			v.showContextMenu();
			break;
		case R.id.rl_UserName:
			intent = new Intent(this, UpdateUserNameActivity.class);
			startActivityForResult(intent, 10);
			break;
		case R.id.rl_Level:
			break;
		case R.id.rl_Phone:
			break;
		case R.id.rl_Citi:
			startActivity(new Intent(getApplicationContext(),
					CityActivity.class));
			break;
		case R.id.rl_UserCommunity:
			startActivity(new Intent(getApplicationContext(),
					CommunityRingActivity.class));
			break;
		case R.id.rl_UserFriendRing:
			Intent intentt = new Intent(this, QuanzhiActivity.class);
			intentt.putExtra("id", "0");
			intentt.putExtra("type", "1");
			intentt.putExtra("name", "好友圈");
			startActivity(intentt);
			break;
		case R.id.rl_Sign:
			intent = new Intent(this, UpdateSignActivity.class);
			startActivityForResult(intent, 20);
			break;
		}
	}

	@Override
	public void onRequestFinished(int requestId, int resultCode, Bundle payload) {
		super.onRequestFinished(requestId, resultCode, payload);
		if (resultCode == PoCService.SUCCESS_CODE) {
			if (mPersonDescTwo == requestId) {
				int result = payload.getInt(ResponseBean.RESPONSE_STATE);
				if (result == 1) {
					username = payload
							.getString(ResponseBean.RESPONSE_USERNAME);
					mobile_phone = payload
							.getString(ResponseBean.RESPONSE_MOBILE_PHONE);
					signature = payload
							.getString(ResponseBean.RESPONSE_SIGNATURE);
					String logo = payload.getString(ResponseBean.RESPONSE_LOGO);
					city = payload.getString(ResponseBean.RESPONSE_CITY);
					community = payload
							.getString(ResponseBean.RESPONSE_COMMUNITY);
					user_rank = payload
							.getString(ResponseBean.RESPONSE_USER_RANK);

					fb.displayForHeader(imPortrait, logo);
					tv_UName.setText(username);
					tvPhoneNumber.setText(mobile_phone);
					tvUserIndividuality.setText(signature);
					tvUserCommunityRing.setText(community);
					tvUserLevel.setText(user_rank);
					tvUserCityRing.setText(city + "圈");
					UserInfoBean.getUserInfo(this).setSignature(signature);
					UserInfoBean.saveUserinfo(this);

				} else {
					String msg = payload
							.getString(ResponseBean.RESPONSE_MESSAGE);
					ToastUtil.makeShortText(MyCenterActivity.this, msg);
				}
			}
			if (mRequestId == requestId) {
				int result = payload.getInt(ResponseBean.RESPONSE_STATE);
				if (result == 1) {
					url = UrlConfig.ROOT_URL_TWO + url;
					fb.displayForHeader(imPortrait, url);
					UserInfoBean.getUserInfo(this).setLogo(url);
					UserInfoBean.saveUserinfo(this);
					Intent br = new Intent(Constants.REFRESH_TOUXIANG);
					sendBroadcast(br);
					Intent data = new Intent();
					data.putExtra(Constants.REFRESH, true);
					setResult(RESULT_OK, data);
				} else {
					String msg = payload
							.getString(ResponseBean.RESPONSE_MESSAGE);
					ToastUtil.makeShortText(MyCenterActivity.this, msg);
				}
			}
		}
	}

	@Override
	public void onResultListener(boolean isUploadSuccess, int state,
			String path, final String user_name, final String url, String other) {
		// TODO Auto-generated method stub
		dismissDialog();
		if (isUploadSuccess) {
			if (state == 1) {
				this.user_path = path;
				this.user_name = user_name;
				this.url = url;
				mRequestId = mRequestManager.editLogo(uid, this.user_name,
						this.user_path, true);
				MyApplication.handler.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						ToastUtil.makeLongText(MyCenterActivity.this, "上传成功");
					}
				});

			}

		} else {

		}
	}
}
