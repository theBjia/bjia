package com.llkj.newbjia.utils;





import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

public class SaveBitmapToSdUtil {
	// 将图片保存到SD卡和相册
	public static String savePhotoToSDCard(Bitmap photoBitmap, String path,
			String photoName) {
		String filePath = "";
		if (checkSDCardAvailable()) {
			File dir = new File(path);
			if (!dir.exists()) {
				dir.mkdirs();
			}

			File photoFile = new File(path, photoName + ".png");
			FileOutputStream fileOutputStream = null;
			try {
				fileOutputStream = new FileOutputStream(photoFile);
				if (photoBitmap != null) {
					if (photoBitmap.compress(Bitmap.CompressFormat.PNG, 100,
							fileOutputStream)) {
						fileOutputStream.flush();
						filePath = photoFile.toString();
						System.out.println(filePath);
					}
				}
			} catch (FileNotFoundException e) {
				photoFile.delete();
				e.printStackTrace();
			} catch (IOException e) {
				photoFile.delete();
				e.printStackTrace();
			} finally {
				try {
					fileOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return filePath;
	}

	public static void scanPhotos(Bitmap photoBitmap, String path,
			String photoName, Context context) {
		//----第一步  保存到SD卡
		String filePath = savePhotoToSDCard(photoBitmap, path, photoName);
		//----第二步  保存到系统相册
		Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		Uri uri = Uri.fromFile(new File(filePath));
		intent.setData(uri);
		context.sendBroadcast(intent);
	}


	public static boolean checkSDCardAvailable (){
		if (android.os.Environment.getExternalStorageState().equals( 
				android.os.Environment.MEDIA_MOUNTED))
			return true;
		else
			return false;
	}

}
