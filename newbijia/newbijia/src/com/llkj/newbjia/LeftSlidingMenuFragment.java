package com.llkj.newbjia;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.llkj.newbjia.R;
import com.llkj.newbjia.MainActivity.OnMessegeChangedListener;
import com.llkj.newbjia.bean.UserInfoBean;
import com.llkj.newbjia.login.LoginReginActivity;
import com.llkj.newbjia.mybijia.MyCenterActivity;
import com.llkj.newbjia.utils.FinalBitmapUtil;
import com.llkj.newbjia.utils.StringUtil;

/**
 * 左侧菜单类
 * 
 * @author John
 * 用户名 3，主页 4， 分类 5，购物车 6.收藏夹 7好友 8.聊天 9，圈子 10我的彼佳 11 我的订单
 */
public class LeftSlidingMenuFragment extends Fragment implements
		OnClickListener,OnMessegeChangedListener,MyOnNewMessageListener {

	private View view;
	private RelativeLayout r_laout_no,r_laout;

	public TextView tv_UserName, tv_wether_time, tv_wether_adres,
			tv_temperater, tv_wetherson, tv_wether_date;
	// 用户名 3，主页 4， 分类 5，购物车 6.收藏夹 7好友 8.聊天 9，圈子 10我的彼佳 11 我的订单
	RelativeLayout rl_user, rl_main, rl_sort, rl_shop_cart, rl_collection, rl_friends,
			rl_chat, rl_quanzi, rl_my_bjia, rl_order, rl_onetwo, rl_setting;
	
	//TODO 
	ImageView iv_new_message_chatting;
	ImageView iv_new_message_quanzi;

	private ImageView imUserHead;// 侧边栏用户头像

	private ImageView iv_wether_iMg;

	private FinalBitmapUtil fb;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.left_drawer_fragment, null);

		initView();
		addListener();
		initData();

		return view;
	}

	private void initData() {
		fb = FinalBitmapUtil.create(getActivity());
		setTouxiang();
	}

	private void initView() {

		tv_UserName = (TextView) view.findViewById(R.id.tv_UserName);
		tv_wether_time = (TextView) view.findViewById(R.id.tv_wether_time);
		tv_wether_adres = (TextView) view.findViewById(R.id.tv_wether_adres);
		tv_temperater = (TextView) view.findViewById(R.id.tv_temperater);
		tv_wetherson = (TextView) view.findViewById(R.id.tv_wetherson);
		tv_wether_date = (TextView) view.findViewById(R.id.tv_wether_date);
		imUserHead = (ImageView) view.findViewById(R.id.imUserHead);
		iv_wether_iMg = (ImageView) view.findViewById(R.id.iv_wether_iMg);

		rl_user = (RelativeLayout) view.findViewById(R.id.rl_user);
		rl_main = (RelativeLayout) view.findViewById(R.id.rl_three);
		rl_sort = (RelativeLayout) view.findViewById(R.id.rl_four);
		rl_shop_cart = (RelativeLayout) view.findViewById(R.id.rl_five);
		rl_collection = (RelativeLayout) view.findViewById(R.id.rl_six);
		rl_friends = (RelativeLayout) view.findViewById(R.id.rl_seven);
		rl_chat = (RelativeLayout) view.findViewById(R.id.rl_eight);
		rl_quanzi = (RelativeLayout) view.findViewById(R.id.rl_night);
		rl_my_bjia = (RelativeLayout) view.findViewById(R.id.rl_ten);
		rl_order = (RelativeLayout) view.findViewById(R.id.rl_oneone);
		rl_onetwo = (RelativeLayout) view.findViewById(R.id.rl_onetwo);
		rl_setting = (RelativeLayout) view.findViewById(R.id.rl_setting);
		
		r_laout_no=(RelativeLayout) view.findViewById(R.id.wether_id_no);
		r_laout=(RelativeLayout) view.findViewById(R.id.wether_id);
		//TODO
		iv_new_message_chatting = (ImageView)view.findViewById(R.id.iv_new_message_chat);
		iv_new_message_quanzi = (ImageView)view.findViewById(R.id.iv_new_message_quanzi);
		updateChatNewIcon(false,iv_new_message_chatting);
		updateChatNewIcon(false, iv_new_message_quanzi);
	}

	private void addListener() {

		rl_user.setOnClickListener(this);
		rl_main.setOnClickListener(this);
		rl_sort.setOnClickListener(this);
		rl_shop_cart.setOnClickListener(this);
		rl_collection.setOnClickListener(this);
		rl_friends.setOnClickListener(this);
		rl_chat.setOnClickListener(this);
		rl_quanzi.setOnClickListener(this);
		rl_my_bjia.setOnClickListener(this);
		rl_order.setOnClickListener(this);
		rl_onetwo.setOnClickListener(this);
		rl_setting.setOnClickListener(this);

	}

	@Override
	public void onClick(View arg0) {

		switch (arg0.getId()) {
		case R.id.rl_three:
			if ((MainActivity) getActivity() != null) {
				((MainActivity) getActivity()).switchContent(0);
			}
			break;
		case R.id.rl_four:
			if ((MainActivity) getActivity() != null) {
				((MainActivity) getActivity()).switchContent(1);
			}
			break;
		case R.id.rl_five:
			if (StringUtil.isEmpty(UserInfoBean.getUserInfo(getActivity())
					.getUid())) {
				Intent intentt = new Intent(getActivity(),
						LoginReginActivity.class);
				getActivity().startActivity(intentt);
				return;
			}
			if ((MainActivity) getActivity() != null) {
				((MainActivity) getActivity()).switchContent(5);
			}

			break;
		case R.id.rl_six:
			if (StringUtil.isEmpty(UserInfoBean.getUserInfo(getActivity())
					.getUid())) {
				Intent intentt = new Intent(getActivity(),
						LoginReginActivity.class);
				getActivity().startActivity(intentt);
				return;
			}
			if ((MainActivity) getActivity() != null) {
				((MainActivity) getActivity()).switchContent(6);
			}
			break;
		case R.id.rl_seven:
			if (StringUtil.isEmpty(UserInfoBean.getUserInfo(getActivity())
					.getUid())) {
				Intent intentt = new Intent(getActivity(),
						LoginReginActivity.class);
				getActivity().startActivity(intentt);
				return;
			}
			if ((MainActivity) getActivity() != null) {
				((MainActivity) getActivity()).switchContent(7);
			}
			break;
		case R.id.rl_eight:
			if (StringUtil.isEmpty(UserInfoBean.getUserInfo(getActivity())
					.getUid())) {
				Intent intentt = new Intent(getActivity(),
						LoginReginActivity.class);
				getActivity().startActivity(intentt);
				return;
			}
			if ((MainActivity) getActivity() != null) {
				updateChatNewIcon(false,iv_new_message_chatting);
				((MainActivity) getActivity()).switchContent(8);
			}
			break;
		case R.id.rl_night:
			if (StringUtil.isEmpty(UserInfoBean.getUserInfo(getActivity())
					.getUid())) {
				Intent intentt = new Intent(getActivity(),
						LoginReginActivity.class);
				getActivity().startActivity(intentt);
				return;
			}
			if ((MainActivity) getActivity() != null) {
				//TODO
				updateChatNewIcon(false, iv_new_message_quanzi);
				((MainActivity) getActivity()).switchContent(9);
			}
			break;
		case R.id.rl_ten:
			if (StringUtil.isEmpty(UserInfoBean.getUserInfo(getActivity())
					.getUid())) {
				Intent intentt = new Intent(getActivity(),
						LoginReginActivity.class);
				getActivity().startActivity(intentt);
				return;
			}
			if ((MainActivity) getActivity() != null) {
				((MainActivity) getActivity()).switchContent(10);
			}

			break;
		case R.id.rl_oneone:
			if (StringUtil.isEmpty(UserInfoBean.getUserInfo(getActivity())
					.getUid())) {
				Intent intentt = new Intent(getActivity(),
						LoginReginActivity.class);
				getActivity().startActivity(intentt);
				return;
			}
			if ((MainActivity) getActivity() != null) {
				((MainActivity) getActivity()).switchContent(11);
			}
			break;
		case R.id.rl_onetwo:
			if ((MainActivity) getActivity() != null) {
				((MainActivity) getActivity()).switchContent(12);
			}
			break;
		case R.id.rl_setting:
			if (StringUtil.isEmpty(UserInfoBean.getUserInfo(getActivity())
					.getUid())) {
				Intent intentt = new Intent(getActivity(),
						LoginReginActivity.class);
				getActivity().startActivity(intentt);
				return;
			}

			if ((MainActivity) getActivity() != null) {
				((MainActivity) getActivity()).switchContent(13);
			}

			break;
		case R.id.rl_user:
			if (StringUtil.isEmpty(UserInfoBean.getUserInfo(getActivity())
					.getUid())) {
				Intent intentt = new Intent(getActivity(),
						LoginReginActivity.class);
				getActivity().startActivity(intentt);
				return;
			}

			if ((MainActivity) getActivity() != null) {
				Intent intentt = new Intent(getActivity(),
						MyCenterActivity.class);
				getActivity().startActivity(intentt);
			}
			break;

		default:
			break;
		}
	}

	public void setWeather(boolean bool) {

		if(!bool){
			r_laout.setVisibility(View.GONE);
			r_laout_no.setVisibility(View.VISIBLE);
		}else{
			r_laout_no.setVisibility(View.GONE);
			r_laout.setVisibility(View.VISIBLE);
			tv_wether_adres.setText(MyApplication.localcity);
			tv_temperater.setText(MyApplication.localTemperature);
			tv_wetherson.setText("/"+MyApplication.localWeather);
			tv_wether_date.setText(MyApplication.localReportTime);
		}
	

		if ("霾".equals(MyApplication.localWeather)) {
			iv_wether_iMg.setBackgroundResource(R.drawable.mai);
		} else if ("晴".equals(MyApplication.localWeather)) {
			iv_wether_iMg.setBackgroundResource(R.drawable.qing);
		} else if ("多云".equals(MyApplication.localWeather)) {
			iv_wether_iMg.setBackgroundResource(R.drawable.duoyun);
		} else if ("阴".equals(MyApplication.localWeather)) {
			iv_wether_iMg.setBackgroundResource(R.drawable.yin);
		} else if ("阵雨".equals(MyApplication.localWeather)) {
			iv_wether_iMg.setBackgroundResource(R.drawable.zhenyu);
		} else if ("雷阵雨".equals(MyApplication.localWeather)) {
			iv_wether_iMg.setBackgroundResource(R.drawable.leizhenyu);
		} else if ("雷阵雨并伴有冰雹".equals(MyApplication.localWeather)) {
			iv_wether_iMg.setBackgroundResource(R.drawable.bingbao);
		} else if ("雨夹雪".equals(MyApplication.localWeather)) {
			iv_wether_iMg.setBackgroundResource(R.drawable.yujiaxue);
		} else if ("小雨".equals(MyApplication.localWeather)) {
			iv_wether_iMg.setBackgroundResource(R.drawable.dayu);
		} else if ("雾".equals(MyApplication.localWeather)) {
			iv_wether_iMg.setBackgroundResource(R.drawable.wu);
		}
	}

	public void setTouxiang() {

		String url = UserInfoBean.getUserInfo(getActivity()).getLogo();
		fb.displayForHeader(imUserHead, url);
		if(!UserInfoBean.getUserInfo(getActivity())
				.getUserName().isEmpty()){
			tv_UserName.setText(UserInfoBean.getUserInfo(getActivity())
					.getUserName());
		}else{
			tv_UserName.setText("登录");
		}
		
	}

	@Override
	public void onMessegeChanged(int i) {
		// TODO Auto-generated method stub
		switch (i) {
		case OnMessegeChangedListener.NEW_CHAT_MESSAGE:
			updateChatNewIcon(true,iv_new_message_chatting);
			break;

		default:
			break;
		}
		
	}
	/**
	 * it is used to change the visibility of the view.
	 * 
	 * @param isNew  if true,then visible.  if false ,then invisible.
	 * 
	 * @param iv_new The view whose visibility should be changed.
	 */
	public void updateChatNewIcon(boolean isNew,ImageView iv_new){
		if(isNew){
			iv_new.setVisibility(View.VISIBLE);
		}else{
			iv_new.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public void onNewMessegeComing(int type) {		
		switch(type){
		case MyOnNewMessageListener.NEW_QUANZI_MESSAGE:
			// TODO 
			updateChatNewIcon(true, iv_new_message_quanzi);
			break;
		
		}
		
	}

}
