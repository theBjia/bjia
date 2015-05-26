package com.llkj.newbjia.utils;





import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.util.Base64;

@SuppressLint({ "InlinedApi", "NewApi" })
public class ComplexOperation {

	public static String encodeBase64Img(byte[] data) {
		return Base64.encodeToString(data, Base64.DEFAULT);
	}

	public static Bitmap decodeBase64Img(String data) {
		byte[] result = Base64.decode(data, Base64.DEFAULT);
		return ImageOperate.getBitmapFromByte(result);
	}

}
