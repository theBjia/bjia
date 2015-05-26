package com.llkj.newbjia.mybijia;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.llkj.newbjia.BaseActivity;
import com.llkj.newbjia.R;
import com.llkj.newbjia.bean.ResponseBean;
import com.llkj.newbjia.bean.UserInfoBean;
import com.llkj.newbjia.http.PoCService;
import com.llkj.newbjia.utils.StringUtil;
import com.llkj.newbjia.utils.ToastUtil;

/**
 * 意见反馈页
 * 
 * @author John
 * 
 */
public class FeedbackActivity extends BaseActivity implements OnClickListener {
	EditText et_sign;
	TextView tvRight;
	private int mAdvise;
	private String uid;
	private String con;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feedback);
		setTitle(R.string.feedback, true, R.string.kong, true, R.string.send);
		initView();
		initData();
		initListener();
	}

	private void initView() {
		et_sign = (EditText) findViewById(R.id.et_sign);
		tvRight = (TextView) findViewById(R.id.tv_title_right);
	}

	private void initData() {
		uid = UserInfoBean.getUserInfo(this).getUid().toString();

	}

	private void initListener() {
		tvRight.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_title_right:
			if (StringUtil.isNetworkConnected(this)) {
				if (null != uid) {
					con = et_sign.getText().toString();
					if (!TextUtils.isEmpty(con)) {
						mAdvise = mRequestManager.Advise(uid, con, true);
					} else {
						ToastUtil.makeShortText(this, R.string.No);
					}
				}
			} else {
				ToastUtil.makeShortText(this, R.string.no_wangluo);
			}
			break;

		default:
			break;
		}

	}

	@Override
	public void onRequestFinished(int requestId, int resultCode, Bundle payload) {
		super.onRequestFinished(requestId, resultCode, payload);
		if (resultCode == PoCService.SUCCESS_CODE) {
			if (mAdvise == requestId) {
				int result = payload.getInt(ResponseBean.RESPONSE_STATE);
				if (result == 1) {
					ToastUtil.makeShortText(this, R.string.ok);
					this.finish();
				} else {
					String msg = payload
							.getString(ResponseBean.RESPONSE_MESSAGE);
					ToastUtil.makeShortText(this, msg);
				}

			}
		}
	}
}
