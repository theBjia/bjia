package com.llkj.newbjia.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.llkj.newbjia.BaseActivity;
import com.llkj.newbjia.R;

public class LoginReginActivity extends BaseActivity implements OnClickListener {

	private Button bt_login, bt_regin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_reg_login);
		initView();
		initListener();
	}

	private void initView() {
		// TODO Auto-generated method stub
		bt_login = (Button) findViewById(R.id.bt_login);
		bt_regin = (Button) findViewById(R.id.bt_regin);

	}

	private void initListener() {
		// TODO Auto-generated method stub
		bt_login.setOnClickListener(this);
		bt_regin.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.bt_login:
			startActivity(new Intent(getApplicationContext(),
					LoginActivity.class));
			break;
		case R.id.bt_regin:
			startActivity(new Intent(getApplicationContext(),
					RegisterOneActivity.class));

			break;

		default:
			break;
		}
	}
}
