package com.llkj.newbjia.utils;



import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * sharedPreference信息保存类
 * 
 * @author zhang.zk
 * 
 */
public class SharedPreferenceUtil {
	public static final String ACCOUNT_INFO = "account_info";
	public static final String LOGIN_SETTING = "login_setting";
	
	//login_setting's data :
	public static final String LOGIN_IS_AUTO = "login_is_auto";
	public static final String LOGIN_IS_REMEBER_PWD = "login_is_remember_pwd";
	public static final String LOGIN_USER_NAME = "login_user_name";
	public static final String LOGIN_USER_PWD = "login_pwd";
	
	private SharedPreferences sp = null;
	private static SharedPreferenceUtil spUtil = null;

	public static SharedPreferenceUtil init(Context context, String sp_name,
			int mode) {
		spUtil = new SharedPreferenceUtil(context, sp_name, mode);
		return spUtil;
	}

	private SharedPreferenceUtil(Context context, String sp_name, int mode) {
		super();
		sp = context.getSharedPreferences(sp_name, mode);
	}

	public boolean put(String key, String value) {
		Editor edit = sp.edit();
		edit.putString(key, value);
		return edit.commit();
	}

	public String getString(String key) {
		return sp.getString(key, "");
	}

	public void clear() {
		sp.edit().clear().commit();
	}

	public boolean getBoolean(String key) {
		return sp.getBoolean(key, false);
	}

	public boolean put(String key, boolean value) {
		Editor edit = sp.edit();
		edit.putBoolean(key, value);
		return edit.commit();
	}

	public int getInt(String key) {
		return sp.getInt(key, 0);
	}

	public boolean put(String key, int value) {
		Editor edit = sp.edit();
		edit.putInt(key, value);
		return edit.commit();
	}
}
