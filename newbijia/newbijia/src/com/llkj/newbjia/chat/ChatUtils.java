package com.llkj.newbjia.chat;



import android.app.Notification;

public class ChatUtils {
	public static void setYZ(Notification mNotification, boolean isvoice,
			boolean isZhenDong) {
		if (isvoice && isZhenDong) {
			mNotification.defaults |= Notification.DEFAULT_VIBRATE;
			mNotification.defaults |= Notification.DEFAULT_SOUND;
			return;
		}
		if (isvoice && !isZhenDong) {
			mNotification.defaults |= Notification.DEFAULT_SOUND;
			mNotification.vibrate = null;
		}
		if (isZhenDong && !isvoice) {
			mNotification.sound = null;
			mNotification.defaults |= Notification.DEFAULT_VIBRATE;
		}
	}

	

}
