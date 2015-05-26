package com.llkj.newbjia.chat;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.llkj.newbjia.MyApplication;
import com.llkj.newbjia.R;
import com.llkj.newbjia.utils.ImageDispose;
import com.llkj.newbjia.utils.ImageOperate;

public class OneImageScanTwoActivity extends Activity {
	private ImageView imageView1;
	private Bitmap bm;
	private Intent bitIntent;
	private String path;
	private RelativeLayout myrl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		MyApplication.getInstance().addActivity(this);
		setContentView(R.layout.one_image_scan);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		myrl = (RelativeLayout) findViewById(R.id.myrl);	
		bitIntent = getIntent();
		if (bitIntent.hasExtra("path")) {
			path = bitIntent.getStringExtra("path");
			
			int degree = ImageDispose.readPictureDegree(path);
			imageView1 = ImageOperate.setImageSrcBackground(this, path, degree);
			myrl.addView(imageView1);
		} else {
			Toast.makeText(this, "读取失败", Toast.LENGTH_SHORT).show();
		}

		imageView1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		imageView1.setScaleType(ScaleType.FIT_XY);
	}

	private String getPicPath() {
		File file = new File(Environment.getExternalStorageDirectory(),
				"my/pic.png");
		return file.getAbsolutePath();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		ImageOperate.destoryBimap(bm);
		super.onDestroy();
	}
}
