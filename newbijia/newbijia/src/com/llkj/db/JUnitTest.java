package com.llkj.db;



import java.io.IOException;
import java.util.ArrayList;

import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.bitmap.core.BitmapDisplayConfig;
import net.tsz.afinal.bitmap.display.Displayer;
import android.content.ContentValues;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.test.AndroidTestCase;
import android.view.View;
import android.widget.ImageView;

import com.llkj.newbjia.bean.UserInfoBean;
import com.llkj.newbjia.utils.LogUtil;

public class JUnitTest extends AndroidTestCase {

	public void dbtest() {
		UserInfoBean user = UserInfoBean.getUserInfo(getContext());
		user.setUid("66");
		DBHelper instance = DBHelper.getInstance(getContext());
		GroupChatMessages entity = new GroupChatMessages(user.getUid(),
				"test11", "test11", "test11", "test11", "test11", "test11",
				"test11", "test11", "test11", "test11", "test11", "test11",
				"test11", "aaa", "test11", "test11", "test11", "test11",
				"test11");
		instance.saveChatGroup(entity);

		PrivateChatMessagesEntity privateChat = new PrivateChatMessagesEntity(
				user.getUid(), "aaaa", "aaaa", "aaaa", "aaaa", "aaaa", "aaaa",
				"aaaa", "aaaa", "aaaa", "aaaa", "aaaa", "aaaa", "aaaa", "bbb",
				"aaaa", "aaaa");
		instance.saveChatPerson(privateChat);

		NoticeContacts notice = new NoticeContacts("222", "bbb", "bbb",
				user.getUid(), "bbb");
		instance.saveNoticeContacts(notice);

		RecentlyContacts recently = new RecentlyContacts(user.getUid(), "ccc",
				"333", "ccc", "ccc", "ccc", "ccc", "ccc", "ccc");
		instance.saveRecentlyContacts(recently);

	}

	// 是否存在
	public void dbtest1() {
		UserInfoBean user = UserInfoBean.getUserInfo(getContext());
		user.setUid("66");
		DBHelper instance = DBHelper.getInstance(getContext());
		boolean b = instance.isGroupMsgExist("test11", "test11", "test11");
		LogUtil.e(">>>>>>>>>>" + b);
	}

	// 查询群记录
	public void dbtest2() {
		UserInfoBean user = UserInfoBean.getUserInfo(getContext());
		user.setUid("66");
		DBHelper instance = DBHelper.getInstance(getContext());
		ArrayList<GroupChatMessages> queryGroupChatRecord = instance
				.queryGroupChatRecord("test11", "0", "10");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		LogUtil.e(queryGroupChatRecord.toString());
	}

	// 编辑群名称
	public void dbtest3() {
		UserInfoBean user = UserInfoBean.getUserInfo(getContext());
		user.setUid("66");
		DBHelper instance = DBHelper.getInstance(getContext());
		boolean editGroupName = instance.editGroupName("test11", "***||||**");
		LogUtil.e(">>>>" + editGroupName);
		dbtest2();
	}

	// 设置群消息未读/已读
	public void dbtest4() {
		UserInfoBean user = UserInfoBean.getUserInfo(getContext());
		user.setUid("66");
		DBHelper instance = DBHelper.getInstance(getContext());
		boolean editGroupName = instance.setGroupChatReaded("test11", "test11",
				"test11", "1");
		LogUtil.e(">>>>" + editGroupName);
		dbtest2();
	}

	// 删除群消息
	public void dbtest5() {
		UserInfoBean user = UserInfoBean.getUserInfo(getContext());
		user.setUid("66");
		DBHelper instance = DBHelper.getInstance(getContext());
		boolean b = instance.deleteGroupChat("test11");
		LogUtil.e(">>>>" + b);
		dbtest2();
	}

	// 更新群组提醒方式
	public void dbtest6() {
		UserInfoBean user = UserInfoBean.getUserInfo(getContext());
		user.setUid("66");
		DBHelper instance = DBHelper.getInstance(getContext());
		ContentValues values = new ContentValues();
		values.put("N_newMsgRemind", "背背佳");
		values.put("showNikeName", "背背佳");
		values.put("isMember", "背背佳");
		boolean b = instance.updateNoticeContacts(values, "222");
		LogUtil.e(">>>>" + b);
		dbtest7();
	}

	// 获取消息提醒参数
	public void dbtest7() {
		UserInfoBean user = UserInfoBean.getUserInfo(getContext());
		user.setUid("66");
		DBHelper instance = DBHelper.getInstance(getContext());
		NoticeContacts noticeContacts = instance.queryNoticeContacts("222");
		LogUtil.e(">>>>" + noticeContacts);

	}

	// 查找聊天记录
	public void dbtest8() {
		UserInfoBean user = UserInfoBean.getUserInfo(getContext());
		user.setUid("66");
		DBHelper instance = DBHelper.getInstance(getContext());
		ArrayList<PrivateChatMessagesEntity> queryPrivateChatRecord = instance
				.queryPrivateChatRecord("aaaa", "aaaa", "0", "10");
		LogUtil.e(queryPrivateChatRecord.toString());
	}

	// 删除聊天记录
	public void dbtest9() {
		UserInfoBean user = UserInfoBean.getUserInfo(getContext());
		user.setUid("66");
		DBHelper instance = DBHelper.getInstance(getContext());
		boolean b = instance.deletePrivateChat("aaaa", "aaaa");
		LogUtil.e(">>>>" + b);
		dbtest8();
	}

	// 最近聊天记录 查找该表中有没有该元素
	public void dbtest10() {
		UserInfoBean user = UserInfoBean.getUserInfo(getContext());
		user.setUid("66");
		DBHelper instance = DBHelper.getInstance(getContext());
		boolean b = instance.queryRecently("333");
		LogUtil.e(">>>>" + b);
		b = instance.queryRecently("3333");
		LogUtil.e(">>>>" + b);
	}

	// 插入信息元素
	public void dbtest11() {
		UserInfoBean user = UserInfoBean.getUserInfo(getContext());
		user.setUid("66");
		DBHelper instance = DBHelper.getInstance(getContext());
		RecentlyContacts recently = new RecentlyContacts(user.getUid(), "55",
				"55", "55", "55", "55", "55", "55", "55");
		boolean b = instance.saveRecentlyContacts(recently);
		LogUtil.e(">>>>" + b);
	}

	// 更改信息元素
	public void dbtest12() {
		UserInfoBean user = UserInfoBean.getUserInfo(getContext());
		user.setUid("66");
		DBHelper instance = DBHelper.getInstance(getContext());
		ContentValues values = new ContentValues();
		values.put("messageChatType", "77");
		values.put("receiveName", "77");
		values.put("receiveLogo", "77");
		values.put("unReadNumber", "77");
		values.put("messageContent", "77");
		values.put("messageDate", "77");
		values.put("messageType", "77");
		boolean b = instance.updateRecentlyContacts(values, "333");
		LogUtil.e(">>>>" + b);
	}

	// 查看此联系人的未读
	public void dbtest13() {
		UserInfoBean user = UserInfoBean.getUserInfo(getContext());
		user.setUid("66");
		DBHelper instance = DBHelper.getInstance(getContext());
		int i = instance.queryRecentlyUnRead("55");
		LogUtil.e(">>>>" + i);
	}

	// 取出所有的最近联系人
	public void dbtest14() {
		UserInfoBean user = UserInfoBean.getUserInfo(getContext());
		user.setUid("66");
		DBHelper instance = DBHelper.getInstance(getContext());
		ArrayList<RecentlyContacts> list = instance.queryRecentlyRecord();
		LogUtil.e(list.toString());
	}

	// 删除最近消息记录\本地用户id
	public void dbtest15() {
		UserInfoBean user = UserInfoBean.getUserInfo(getContext());
		user.setUid("66");
		DBHelper instance = DBHelper.getInstance(getContext());
		boolean b = instance.deleteRecentlyRecord("55");
		LogUtil.e(">>>>" + b);
		dbtest14();
	}

	// 清空聊天记录
	public void dbtest16() {
		UserInfoBean user = UserInfoBean.getUserInfo(getContext());
		user.setUid("66");
		DBHelper instance = DBHelper.getInstance(getContext());
		boolean b = instance.clearRecentlyRecord("333");
		LogUtil.e(">>>>" + b);
		dbtest14();
	}

	// 清除角标
	public void dbtest17() {
		UserInfoBean user = UserInfoBean.getUserInfo(getContext());
		user.setUid("66");
		DBHelper instance = DBHelper.getInstance(getContext());
		boolean b = instance.clearUnReadNumber("333");
		LogUtil.e(">>>>" + b);
		dbtest14();
	}

	// 清空所有聊天记录
	public void dbtest18() {
		UserInfoBean user = UserInfoBean.getUserInfo(getContext());
		user.setUid("66");
		DBHelper instance = DBHelper.getInstance(getContext());
		boolean b = instance.clearAllRecently();
		LogUtil.e(">>>>" + b);
		dbtest14();
	}

	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	public void picDegree() {
		FinalBitmap fb = FinalBitmap.create(getContext());
		fb.configDisplayer(new Displayer() {

			@Override
			public void loadFailDisplay(View imageView, Bitmap bitmap) {

			}

			@Override
			public void loadCompletedisplay(View imageView, Bitmap bitmap,
					BitmapDisplayConfig config) {

			}
		});
		fb.display(new ImageView(getContext()),
				"http://shuohua.bloveambition.com//upload//gallery//1414752191.png");
	}
}
