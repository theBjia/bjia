package com.llkj.newbjia.bean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import com.llkj.newbjia.MyApplication;

public class UserInfoBean {
	/**
	 * 用户id
	 */
	private static String uid;
	/**
	 * 手机号
	 */
	private static String phone;
	/**
	 * 用户token
	 */
	private static String token;
	/**
	 * 画号（注册时无）
	 */
	private static String name;
	/**
	 * 昵称
	 */
	private static String nickname;
	/**
	 * qq（注册时无）
	 */
	private static String QQ;
	/**
	 * 邮箱（注册时无）
	 */
	private static String email;
	/**
	 * 个性签名（注册时无）
	 */
	private static String signature;
	/**
	 * 头像（注册时无）
	 */
	private static String logo;
	private Context context;
	private static UserInfoBean bean;
	private static String user_name;

	private UserInfoBean(Context context) {
		this.context = context.getApplicationContext();
	}

	public String getUid() {
		if (TextUtils.isEmpty(uid)) {
			getUserInfo(context);
		}
		return uid;
	}

	public void setUid(String uid) {
		UserInfoBean.uid = uid;
	}

	public String getPhone() {
		if (TextUtils.isEmpty(uid)) {
			getUserInfo(context);
		}
		return phone;
	}

	public void setPhone(String phone) {
		UserInfoBean.phone = phone;
	}

	public String getToken() {
		if (TextUtils.isEmpty(uid)) {
			getUserInfo(context);
		}
		return token;
	}

	public void setToken(String token) {
		UserInfoBean.token = token;
	}

	public String getName() {
		if (TextUtils.isEmpty(uid)) {
			getUserInfo(context);
		}
		return name;
	}

	public void setName(String name) {
		UserInfoBean.name = name;
	}

	public void setUserName(String user_name) {
		UserInfoBean.user_name = user_name;
	}

	public String getUserName() {
		if (TextUtils.isEmpty(uid)) {
			getUserInfo(context);
		}
		return user_name;
	}

	public String getNickname() {
		if (TextUtils.isEmpty(uid)) {
			getUserInfo(context);
		}
		return nickname;
	}

	public void setNickname(String nickname) {
		UserInfoBean.nickname = nickname;
	}

	public String getQQ() {
		if (TextUtils.isEmpty(uid)) {
			getUserInfo(context);
		}
		return QQ;
	}

	public void setQQ(String qQ) {
		QQ = qQ;
	}

	public String getEmail() {
		if (TextUtils.isEmpty(uid)) {
			getUserInfo(context);
		}
		return email;
	}

	public void setEmail(String email) {
		UserInfoBean.email = email;
	}

	public String getSignature() {
		if (TextUtils.isEmpty(uid)) {
			getUserInfo(context);
		}
		return signature;
	}

	public void setSignature(String signature) {
		UserInfoBean.signature = signature;
	}

	public String getLogo() {
		if (TextUtils.isEmpty(uid)) {
			getUserInfo(context);
		}
		return logo;
	}

	public void setLogo(String logo) {
		UserInfoBean.logo = logo;
	}

	/**
	 * 退出登录
	 */
	public static void userLogout() {
		UserInfoBean.uid = "";
		UserInfoBean.phone ="";
		UserInfoBean.token = "";
		UserInfoBean.name = "";
		UserInfoBean.nickname = "";
		UserInfoBean.QQ = "";
		UserInfoBean.email = "";
		UserInfoBean.signature = "";
		UserInfoBean.logo = "";
		UserInfoBean.user_name = "";
	}

	/**
	 * 判断用户是否登陆
	 * 
	 * @return
	 */
	public static boolean getIsLogin() {
		if (TextUtils.isEmpty(uid)) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 获取用户信息
	 * 
	 * @param context
	 * @return
	 */
	public static UserInfoBean getUserInfo(Context context) {
		SharedPreferences login_sp = context.getSharedPreferences("login_sp",
				Context.MODE_PRIVATE);
		if (TextUtils.isEmpty(UserInfoBean.uid) || bean == null) {
			UserInfoBean.uid = login_sp.getString("uid", "");
			UserInfoBean.phone = login_sp.getString("phone", "");
			UserInfoBean.token = login_sp.getString("token", "");
			UserInfoBean.name = login_sp.getString("name", "");
			UserInfoBean.nickname = login_sp.getString("nickname", "");
			UserInfoBean.QQ = login_sp.getString("QQ", "");
			UserInfoBean.email = login_sp.getString("email", "");
			UserInfoBean.signature = login_sp.getString("signature", "");
			UserInfoBean.logo = login_sp.getString("logo", "");
			UserInfoBean.user_name = login_sp.getString("user_name", "");
			bean = new UserInfoBean(context);
			return bean;
		} else {
			return bean;
		}
	}

	/**
	 * 保存帐号信息
	 * 
	 * @param context
	 * @param userid
	 * @param ucode
	 * @param usertype
	 * @param picurl
	 * @param name
	 * @param tel
	 */
	public static void saveUserinfo(Context context) {
		SharedPreferences sp = context.getSharedPreferences("login_sp",
				Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putString("uid", uid);
		editor.putString("phone", phone);
		editor.putString("token", token);
		editor.putString("name", name);
		editor.putString("nickaname", nickname);
		editor.putString("QQ", QQ);
		editor.putString("email", email);
		editor.putString("signature", signature);
		editor.putString("logo", logo);
		editor.putString("user_name", user_name);
		editor.commit();
	}

	/**
	 * 删除帐号信息
	 * 
	 * @param context
	 */
	public static void clearUserInfo(Context context) {
		userLogout();
		SharedPreferences sp = context.getSharedPreferences("login_sp",
				Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.clear();
		editor.commit();
	}

	public static void addMe(Context context) {
		JSONArray ja = new JSONArray();
		JSONObject json = new JSONObject();
		try {
			json.put("user_id", UserInfoBean.getUserInfo(context).getUid());
			ja.put(json);
			MyApplication.client.emit("addMe", ja);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
