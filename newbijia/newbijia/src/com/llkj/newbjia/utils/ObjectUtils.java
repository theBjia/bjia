package com.llkj.newbjia.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;

import android.content.Context;
import android.os.Environment;

public class ObjectUtils {
	public static void fileSave(Context context, Object object, String name) {
		// 保存在本地
		try {
			// 通过openFileOutput方法得到一个输出流，方法参数为创建的文件名（不能有斜杠），操作模式
			if (context == null)
				return;
			FileOutputStream fos = context.openFileOutput(name,
					Context.MODE_WORLD_READABLE);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(object);// 写入
			fos.close(); // 关闭输出流
			// Toast.makeText(WebviewTencentActivity.this,
			// "保存oAuth_1成功",Toast.LENGTH_LONG).show();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			// Toast.makeText(WebviewTencentActivity.this,
			// "出现异常1",Toast.LENGTH_LONG).show();
		} catch (IOException e) {
			e.printStackTrace();
			// Toast.makeText(WebviewTencentActivity.this,
			// "出现异常2",Toast.LENGTH_LONG).show();
		}

		// 保存在sd卡
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {

			File sdCardDir = Environment.getExternalStorageDirectory();// 获取SDCard目录
			File sdFile = new File(sdCardDir, name);

			try {
				FileOutputStream fos = new FileOutputStream(sdFile);
				ObjectOutputStream oos = new ObjectOutputStream(fos);
				oos.writeObject(object);// 写入
				fos.close(); // 关闭输出流
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// Toast.makeText(WebviewTencentActivity.this, "成功保存到sd卡",
			// Toast.LENGTH_LONG).show();

		}
	}

	public static Object readObject(String name) {
		Object object = null;
		File sdCardDir = Environment.getExternalStorageDirectory();// 获取SDCard目录
		File sdFile = new File(sdCardDir, name);

		try {
			FileInputStream fis = new FileInputStream(sdFile); // 获得输入流
			ObjectInputStream ois = new ObjectInputStream(fis);
			object = (Object) ois.readObject();
			ois.close();
		} catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OptionalDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return object;
	}
}
