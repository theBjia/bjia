package com.llkj.newbjia.mybijia;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.llkj.newbjia.BaseActivity;
import com.llkj.newbjia.BaseFragment;
import com.llkj.newbjia.MainActivity;
import com.llkj.newbjia.R;
import com.llkj.newbjia.bean.KeyBean;
import com.llkj.newbjia.bean.ResponseBean;
import com.llkj.newbjia.bean.UserInfoBean;
import com.llkj.newbjia.http.PoCService;
import com.llkj.newbjia.login.LoginActivity;
import com.llkj.newbjia.utils.FinalBitmapUtil;
import com.llkj.newbjia.utils.LogUtil;
import com.llkj.newbjia.utils.StringUtil;
import com.llkj.newbjia.utils.ToastUtil;

/**
 * 我的彼佳页
 * 
 * @author John
 * 
 */
public class MyBijiaFragment extends BaseFragment implements OnClickListener {
	// 0.用户信息 ，订单查询， 我的彼佳币， 我的积分， 我的优惠券， 收藏夹， 浏览历史， 设置
	RelativeLayout rl_UserInfo, rl_OrderCheck, rl_MyMoney, rl_MyScore,
			rl_MyCoupon, rl_Favorite, rl_History, rl_Setting;
	private LinearLayout ll_title_back;
	private TextView tvUserName;
	private int mPersonDesc;
	private Intent bigIntent;
	private String uid;
	private String name;
	private ImageView iv_logo;
	private FinalBitmapUtil fb;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.my_bijia, null);
			initView();
			initListener();
			initData();
		} else {
			ViewGroup parent = (ViewGroup) rootView.getParent();
			if (parent != null) {
				parent.removeView(rootView);
			}
		}

		return rootView;
	}

	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(!isLoaded){
			if (StringUtil.isNetworkConnected(getActivity())) {
				mPersonDesc = mRequestManager.getPersonDesc(uid, true);
			} else {
				ToastUtil.makeShortText(getActivity(), R.string.no_wangluo);
			}
		}
		
	}


	private void initView() {
		rl_UserInfo = (RelativeLayout) rootView.findViewById(R.id.rl_UserInfo);
		rl_OrderCheck = (RelativeLayout) rootView
				.findViewById(R.id.rl_OrderCheck);
		rl_MyMoney = (RelativeLayout) rootView.findViewById(R.id.rl_MyMoney);
		rl_MyScore = (RelativeLayout) rootView.findViewById(R.id.rl_MyScore);
		rl_MyCoupon = (RelativeLayout) rootView.findViewById(R.id.rl_MyCoupon);
		rl_Favorite = (RelativeLayout) rootView.findViewById(R.id.rl_Favorite);
		rl_History = (RelativeLayout) rootView.findViewById(R.id.rl_History);
		rl_Setting = (RelativeLayout) rootView.findViewById(R.id.rl_Setting);
		ll_title_back = (LinearLayout) rootView
				.findViewById(R.id.ll_title_back);
		tvUserName = (TextView) rootView.findViewById(R.id.tvUserName);
		iv_logo = (ImageView) rootView.findViewById(R.id.iv_logo);

	}

	private void initData() {
		// bigIntent = getActivity().getIntent();
		// if(bigIntent.hasExtra(KeyBean.KEY_UID)){
		fb = FinalBitmapUtil.create(getActivity());
		uid = UserInfoBean.getUserInfo(getActivity()).getUid();
		// }
		if (StringUtil.isNetworkConnected(getActivity())) {
			mPersonDesc = mRequestManager.getPersonDesc(uid, true);
			isLoaded = true;
		} else {
			ToastUtil.makeShortText(getActivity(), R.string.no_wangluo);
		}

	}

	private void initListener() {
		rl_UserInfo.setOnClickListener(this);
		rl_OrderCheck.setOnClickListener(this);
		rl_MyMoney.setOnClickListener(this);
		rl_MyScore.setOnClickListener(this);
		rl_MyCoupon.setOnClickListener(this);
		rl_Favorite.setOnClickListener(this);
		rl_History.setOnClickListener(this);
		rl_Setting.setOnClickListener(this);
		ll_title_back.setOnClickListener(this);
		name = UserInfoBean.getUserInfo(getActivity()).getName();
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.rl_UserInfo:
			intent = new Intent(getActivity(), MyCenterActivity.class);
			startActivityForResult(intent, 100);

			break;
		case R.id.rl_OrderCheck:
			if ((MainActivity) getActivity() != null) {
				((MainActivity) getActivity()).isBijiaInto = true;
				((MainActivity) getActivity()).switchContent(11);
			}

			break;
		case R.id.rl_MyMoney:
			intent = new Intent(getActivity(), MyMoneyActivity.class);
			startActivity(intent);
			break;
		case R.id.rl_MyScore:
			intent = new Intent(getActivity(), MyScoreActivity.class);
			startActivity(intent);
			break;
		case R.id.rl_MyCoupon:
			intent = new Intent(getActivity(), CouponActivity.class);
			startActivity(intent);
			break;
		case R.id.rl_Favorite:
			if ((MainActivity) getActivity() != null) {
				((MainActivity) getActivity()).isBijiaInto = true;
				((MainActivity) getActivity()).switchContent(6);
			}
			break;
		case R.id.rl_History:
			intent = new Intent(getActivity(), BrowsingActivity.class);
			startActivity(intent);
			break;
		case R.id.rl_Setting:
			if ((MainActivity) getActivity() != null) {
				((MainActivity) getActivity()).switchContent(13);
			}
			break;
		case R.id.ll_title_back:
			if ((MainActivity) getActivity() != null) {
				((MainActivity) getActivity()).switchContent(0);
			}
			break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (data != null) {
			switch (requestCode) {
			case 100:
				fb.displayForHeader(iv_logo,
						UserInfoBean.getUserInfo(getActivity()).getLogo());
				tvUserName.setText(UserInfoBean.getUserInfo(getActivity())
						.getUserName());
				break;

			default:
				break;
			}
		}
	}

	@Override
	public void onRequestFinished(int requestId, int resultCode, Bundle payload) {
		super.onRequestFinished(requestId, resultCode, payload);
		if (resultCode == PoCService.SUCCESS_CODE) {
			if (mPersonDesc == requestId) {
				int result = payload.getInt(ResponseBean.RESPONSE_STATE);
				if (result == 1) {
					String user_name = payload
							.getString(ResponseBean.RESPONSE_USERNAME);
					String logo = payload.getString(ResponseBean.RESPONSE_LOGO);
					tvUserName.setText(user_name);
					fb.displayForHeader(iv_logo, logo);
					isLoaded = true;
				} else {
					isLoaded = false;
					String msg = payload
							.getString(ResponseBean.RESPONSE_MESSAGE);
					ToastUtil.makeShortText(getActivity(), msg);
				}

			}
		}
	}

}
