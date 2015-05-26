package com.llkj.newbjia.quanzi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.logic.ImgFileListActivity;
import com.llkj.newbjia.BaseActivity;
import com.llkj.newbjia.MainActivity;
import com.llkj.newbjia.MyApplication;
import com.llkj.newbjia.R;
import com.llkj.newbjia.adpater.PublishPicAdpter;
import com.llkj.newbjia.bean.ResponseBean;
import com.llkj.newbjia.bean.UserInfoBean;
import com.llkj.newbjia.customview.MyDialog;
import com.llkj.newbjia.customview.MyGridView;
import com.llkj.newbjia.http.PoCService;
import com.llkj.newbjia.login.LoginReginActivity;
import com.llkj.newbjia.utils.ImageOperate;
import com.llkj.newbjia.utils.StringUtil;
import com.llkj.newbjia.utils.ToastUtil;
import com.llkj.newbjia.utils.UploadFile;
import com.llkj.newbjia.utils.Utils;
import com.llkj.newbjia.utils.UploadFile.OnUploadFileForResultListener;

public class PublishActivity extends BaseActivity implements
		OnUploadFileForResultListener, OnClickListener {

	private MyGridView mGridView;
	@SuppressWarnings("rawtypes")
	private ArrayList arrayList;
	private PublishPicAdpter adapter;
	private UploadFile uploadfile;
	private ArrayList<String> picids;
	private static final int REQUEST_CODE_CAMERA = 0;
	private static final int REQUEST_CODE_GALLERY = 1;
	private Bitmap bum;
	private ImageView iv_xiangji, iv_xiangce;
	//TODO
	private ImageView iv_back;
	private int mRequestId;
	//TODO
	private TextView tv_ok,tv_no;
	
	private Intent bigIntent;
	private String type, uid, id, content;
	private EditText et_content;
	private MyDialog myDialog;
	private RelativeLayout rl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_publish);
		//setTitle(R.string.publish, true, R.string.kongzifuchuan, false,
		//		R.string.kongzifuchuan);
//		setTitle(R.string.dynamicCondition, true, R.string.kongzifuchuan, true,
//		R.string.kongzifuchuan);
		
		setTitle(R.string.dynamicCondition, true, R.string.qu_xiao, true,
				R.string.publish_2);
		

		initView();
		initListener();
		initData();

		if (getIntent().hasExtra("path1")) {
			String path1 = getIntent().getStringExtra("path1");
			try {
				path1 = ImageOperate.revitionImageSize(Utils.path, this);

				BitmapFactory.Options option = new BitmapFactory.Options();
				option.inSampleSize = (option.outWidth) / 200;
				option.inPreferredConfig = Bitmap.Config.ARGB_4444;
				option.inPurgeable = true;
				option.inInputShareable = true;

				if (!StringUtil.isEmpty(path1)) {
					bum = BitmapFactory.decodeFile(path1, option);
					showWaitDialog();
					uploadfile.uploadImg(this, bum, UploadFile.TYPE_ONE);
				} else {
					dismissDialog();
					ToastUtil.makeShortText(this, "文件错误");
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {

			ArrayList<String> arrayList = getIntent().getStringArrayListExtra(
					"files");

			for (int i = 0; i < arrayList.size(); i++) {
				String mypath = arrayList.get(i);
				try {
					mypath = ImageOperate.revitionImageSize(mypath, this);
					BitmapFactory.Options option = new BitmapFactory.Options();
					option.inSampleSize = (option.outWidth) / 200;
					option.inPreferredConfig = Bitmap.Config.ARGB_4444;
					option.inPurgeable = true;
					option.inInputShareable = true;

					if (!StringUtil.isEmpty(mypath)) {
						bum = BitmapFactory.decodeFile(mypath, option);
						if (i == 0) {
							showWaitDialog();
						}
						uploadfile.uploadImg(this, bum, UploadFile.TYPE_ONE);
					} else {
						dismissDialog();
						ToastUtil.makeShortText(this, "文件错误");
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}

	}

	private void initView() {
		mGridView = (MyGridView) findViewById(R.id.mgv_content);
		iv_xiangji = (ImageView) findViewById(R.id.iv_xiangji);
		iv_xiangce = (ImageView) findViewById(R.id.iv_xiangce);
		iv_back = (ImageView) findViewById(R.id.imageView);
		iv_back.setVisibility(View.INVISIBLE);
		tv_ok = (TextView) findViewById(R.id.tv_title_right);
		tv_no = (TextView) findViewById(R.id.tv_title_left);
		//LinearLayout ll_title_back = (LinearLayout) findViewById(R.id.ll_title_back);

		et_content = (EditText) findViewById(R.id.et_content);
		myDialog = new MyDialog(this, R.layout.item_publish_picture,
				R.style.DialogStyle);
		iv_xiangji = (ImageView) myDialog.findViewById(R.id.iv_xiangji);
		iv_xiangce = (ImageView) myDialog.findViewById(R.id.iv_xiangce);		
		
		findViewById(R.id.rl_pic).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						 et_content.setCursorVisible(false);//失去光标
						imm.hideSoftInputFromWindow(
								et_content.getWindowToken(), 0);

					}
				});
	}

	private void initData() {
		bigIntent = getIntent();
		uid = UserInfoBean.getUserInfo(this).getUid();
		if (bigIntent.hasExtra("id")) {
			id = bigIntent.getStringExtra("id");

		}
		if (bigIntent.hasExtra("type")) {
			type = bigIntent.getStringExtra("type");
		}
		uploadfile = new UploadFile();
		uploadfile.setListener(this);
		picids = new ArrayList<String>();
		arrayList = new ArrayList();
		adapter = new PublishPicAdpter(this, arrayList);
		mGridView.setAdapter(adapter);
	}

	private void initListener() {
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == arrayList.size()) {
					myDialog.show();
				} else {
					arrayList.remove(position);
					picids.remove(position);
					adapter.notifyDataSetChanged();
				}

			}
		});
		iv_xiangji.setOnClickListener(this);
		iv_xiangce.setOnClickListener(this);
		tv_ok.setOnClickListener(this);
		tv_no.setOnClickListener(this);
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {

			switch (requestCode) {
			case REQUEST_CODE_CAMERA:
				String path1;
				try {
					path1 = ImageOperate.revitionImageSize(Utils.path, this);

					BitmapFactory.Options option = new BitmapFactory.Options();
					option.inSampleSize = (option.outWidth) / 200;
					option.inPreferredConfig = Bitmap.Config.ARGB_4444;
					option.inPurgeable = true;
					option.inInputShareable = true;

					if (!StringUtil.isEmpty(path1)) {
						bum = BitmapFactory.decodeFile(path1, option);
						showWaitDialog();
						uploadfile.uploadImg(this, bum, UploadFile.TYPE_ONE);
					} else {
						dismissDialog();
						ToastUtil.makeShortText(this, "文件错误");
					}

				} catch (IOException e) {
					e.printStackTrace();
				}

				break;
			case REQUEST_CODE_GALLERY:
				ArrayList<String> arrayList = data
						.getStringArrayListExtra("files");

				for (int i = 0; i < arrayList.size(); i++) {
					String mypath = arrayList.get(i);
					try {
						mypath = ImageOperate.revitionImageSize(mypath, this);
						BitmapFactory.Options option = new BitmapFactory.Options();
						option.inSampleSize = (option.outWidth) / 200;
						option.inPreferredConfig = Bitmap.Config.ARGB_4444;
						option.inPurgeable = true;
						option.inInputShareable = true;

						if (!StringUtil.isEmpty(mypath)) {
							bum = BitmapFactory.decodeFile(mypath, option);
							if (i == 0) {
								showWaitDialog();
							}
							uploadfile
									.uploadImg(this, bum, UploadFile.TYPE_ONE);
						} else {
							dismissDialog();
							ToastUtil.makeShortText(this, "文件错误");
						}
					} catch (IOException e) {
						e.printStackTrace();
					}

				}
				break;

			default:
				break;
			}
		}
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public void onResultListener(boolean isUploadSuccess, int state,
			final String pic_id, final String message, final String url,
			String path) {
		if (isUploadSuccess) {
			dismissDialog();
			if (state == 1) {
				MyApplication.handler.post(new Runnable() {

					@Override
					public void run() {
						picids.add(pic_id);
						arrayList.add(getMap(pic_id, url));
						adapter.notifyDataSetChanged();
						ToastUtil.makeLongText(PublishActivity.this, "上传成功");
					}
				});

			} else {
				MyApplication.handler.post(new Runnable() {

					@Override
					public void run() {
						ToastUtil.makeLongText(PublishActivity.this, message);
					}
				});
			}

		}

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public HashMap getMap(String pic_id, String url) {
		HashMap map = new HashMap();
		map.put("url", url);
		map.put("pic_id", pic_id);
		return map;
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.iv_xiangji:
			// 拍照
			if (arrayList.size() < 9) {
				getAvatarFromCamera();
			} else {
				ToastUtil.makeShortText(this, "最多可以添加9张图片");
			}
			myDialog.dismiss();
			break;
		case R.id.iv_xiangce:
			Intent intent1 = new Intent(getApplicationContext(),
					ImgFileListActivity.class);
			startActivityForResult(intent1, REQUEST_CODE_GALLERY);
			myDialog.dismiss();
			break;

		case R.id.tv_title_right:
			StringBuilder sb2 = new StringBuilder();
			for (int i = 0; i < picids.size(); i++) {
				sb2.append(picids.get(i) + ",");
			}
			content = et_content.getText() + "";
			// if (StringUtil.isEmpty(content)) {
			// ToastUtil.makeShortText(this, R.string.contentnotisnull);
			// return;
			// }
			if (sb2.length() > 0)
				sb2.substring(0, sb2.length() - 1);
			mRequestId = mRequestManager.penAdd(sb2.toString(), uid, content,
					type, id, true);
			this.finish();
			int page2 = 1;
			if (StringUtil.isNetworkConnected(this)) {
				QuanzhiActivity.mRequestId = mRequestManager.friendPen(uid,
						page2 + "", type, id, true);
			}
			break;
		case R.id.tv_title_left:
			this.finish();
			break;
		default:
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
					ToastUtil.makeShortText(this, "发送成功");
					et_content.setText("");
					arrayList.clear();
					picids.clear();
					adapter.notifyDataSetChanged();
					Intent data = new Intent();
					data.putExtra("Refresh", true);
					setResult(100, data);
				} else {
					String msg = payload
							.getString(ResponseBean.RESPONSE_MESSAGE);
					ToastUtil.makeShortText(this, msg);
				}

			}

		}
	}

}
