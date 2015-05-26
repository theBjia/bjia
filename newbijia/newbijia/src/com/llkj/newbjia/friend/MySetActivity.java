package com.llkj.newbjia.friend;

import android.os.Bundle;

import com.llkj.newbjia.BaseActivity;
import com.llkj.newbjia.R;


public class MySetActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myset_layout);
		setTitle(R.string.xiugaibeizhu, true, R.string.kong, true,
				R.string.over);
	}

}
