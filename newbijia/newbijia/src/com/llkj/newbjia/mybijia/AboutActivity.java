package com.llkj.newbjia.mybijia;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.llkj.newbjia.BaseActivity;
import com.llkj.newbjia.R;
import com.llkj.newbjia.bean.ResponseBean;
import com.llkj.newbjia.http.PoCService;
import com.llkj.newbjia.setting.GongNengJieShaoActivity;
import com.llkj.newbjia.setting.HelpListActivity;
import com.llkj.newbjia.utils.MyAutoUpdate;
import com.llkj.newbjia.utils.StringUtil;
import com.llkj.newbjia.utils.ToastUtil;

/**
 * 关于我们页
 * 
 * @author John
 * 
 */
public class AboutActivity extends BaseActivity implements OnClickListener {
	// 去评分 功能介绍 帮助 版本查询
	RelativeLayout rl_ToScore, rl_Function, rl_Help, rl_CheckVersions;
	private MyAutoUpdate mau;
	private TextView tv_banbenhao;
	private int mRequestId;
	private String title, url;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_us);
		setTitle(R.string.about, true, R.string.kong, false, R.string.kong);
		initView();
		initData();
		initListener();
	}

	private void initView() {
		rl_ToScore = (RelativeLayout) findViewById(R.id.rl_ToScore);
		rl_Function = (RelativeLayout) findViewById(R.id.rl_Function);
		rl_Help = (RelativeLayout) findViewById(R.id.rl_Help);
		rl_CheckVersions = (RelativeLayout) findViewById(R.id.rl_CheckVersions);

		tv_banbenhao = (TextView) findViewById(R.id.tv_banbenhao);
	}

	private void initData() {
		mau = new MyAutoUpdate(this);
	}

	private void initListener() {
		rl_Function.setOnClickListener(this);
		rl_Help.setOnClickListener(this);
		rl_CheckVersions.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_ToScore:

			break;
		case R.id.rl_Function:
			startActivity(new Intent(getApplicationContext(),
					GongNengJieShaoActivity.class));

			break;
		case R.id.rl_Help:
			startActivity(new Intent(getApplicationContext(),
					HelpListActivity.class));

			break;
		case R.id.rl_CheckVersions:
			PackageInfo pinfo = mau.getCurrentVersion();
			String piname = pinfo.versionName;
			mRequestId = mRequestManager.version(piname, true);
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
					title = payload.getString(ResponseBean.RESPONSE_TITLE);
					url = payload.getString(ResponseBean.RESPONSE_URL);
					if (!StringUtil.isEmpty(url) && url.contains("apk")) {
						mau.setUrl(url);
						mau.check();
					}
				} else {
					String msg = payload
							.getString(ResponseBean.RESPONSE_MESSAGE);
					ToastUtil.makeShortText(this, msg);
				}

			}

		}
	}
}
