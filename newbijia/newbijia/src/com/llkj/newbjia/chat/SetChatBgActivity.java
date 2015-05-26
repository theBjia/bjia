package com.llkj.newbjia.chat;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RelativeLayout;

import com.llkj.newbjia.BaseActivity;
import com.llkj.newbjia.MyApplication;
import com.llkj.newbjia.R;
import com.llkj.newbjia.utils.ImageOperate;
import com.llkj.newbjia.utils.StringUtil;
import com.llkj.newbjia.utils.ToastUtil;
import com.llkj.newbjia.utils.Utils;

public class SetChatBgActivity extends BaseActivity implements OnClickListener {

	private RelativeLayout rl_one, rl_three;
	private static final int REQUEST_CODE_CAMERA = 0;
	private static final int REQUEST_CODE_GALLERY = 1;
	private String path;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		MyApplication.getInstance().addActivity(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_setchatbg);
		setTitle(R.string.chatbg, true, R.string.kong, false, R.string.kong);
		initView();
		initData();
		initListener();

	}

	private void initView() {
		// TODO Auto-generated method stub
		rl_one = (RelativeLayout) findViewById(R.id.rl_one);
		rl_three = (RelativeLayout) findViewById(R.id.rl_three);

	}

	private void initListener() {
		// TODO Auto-generated method stub
		rl_one.setOnClickListener(this);
		rl_three.setOnClickListener(this);
	}

	private void initData() {
		// TODO Auto-generated method stub

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
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rl_one:
			getAvatarFromGallery();
			break;
		case R.id.rl_three:
			getAvatarFromCamera();
			break;
		default:
			break;
		}

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

					if (!StringUtil.isEmpty(path1)) {

						Intent dataa = new Intent();
						dataa.putExtra("path", path1);
						setResult(RESULT_OK, dataa);
						this.finish();
					} else {
						dismissDialog();
						ToastUtil.makeShortText(this, "文件错误");
					}

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
						Intent dataa = new Intent();
						dataa.putExtra("path", picturePath);
						setResult(RESULT_OK, dataa);
						this.finish();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else {
					dismissDialog();
					ToastUtil.makeShortText(this, "文件错误");
				}
				break;

			default:
				break;
			}
		}
	}

}
