package com.llkj.newbjia.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;

public class FileOperation {
	public static String sdFile = Environment.getExternalStorageDirectory()
			.getPath();
	public static String FOLDER = sdFile + "/fb/";
	public static String FILE_NAME = System.currentTimeMillis() + "_img.jpg";

	private ArrayList<File> m_dirs;

	public FileOperation() {
	}

	public byte[] getImg() {
		ByteArrayOutputStream byteArray = null;
		FileInputStream fileInputStream = null;
		if (isSDCardExist()) {
			try {
				fileInputStream = new FileInputStream(new File(FOLDER
						+ FILE_NAME));
				byteArray = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = fileInputStream.read(buffer)) != -1) {
					byteArray.write(buffer, 0, len);
				}
			} catch (FileNotFoundException e) {
			} catch (IOException e) {
			} finally {
				try {
					byteArray.close();
					fileInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		} else {

		}
		return byteArray.toByteArray();
	}

	public static File saveBgFile(Context context, Bitmap bmp) {
		String FOLD_PATH = isSDCardExist() ? Environment
				.getExternalStorageDirectory() + "/cm/" : context.getFilesDir()
				+ "/cm/";
		String fileName = System.currentTimeMillis() + "_bg.jpg";
		File fImage = new File(FOLD_PATH);
		if (!fImage.exists()) {
			fImage.mkdir();
		}
		fImage = new File(fImage, fileName);
		FileOutputStream outStream;
		try {
			outStream = new FileOutputStream(fImage);
			bmp.compress(CompressFormat.PNG, 100, outStream);
			outStream.close();
			outStream = null;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fImage;
	}

	public static String saveChatImgFile(Context context, Bitmap bmp) {
		String FOLD_PATH = isSDCardExist() ? Environment
				.getExternalStorageDirectory() + "/lp/chat/img/" : context
				.getFilesDir() + "/lp/chat/img/";
		String fileName = System.currentTimeMillis() + "_chat_img.jpg";
		File fImage = new File(FOLD_PATH);
		if (!fImage.exists()) {
			fImage.mkdirs();
		}
		fImage = new File(fImage, fileName);
		if (!fImage.exists()) {
			try {
				fImage.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		FileOutputStream outStream;
		try {
			outStream = new FileOutputStream(fImage);
			bmp.compress(CompressFormat.PNG, 100, outStream);
			outStream.close();
			outStream = null;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fImage.getAbsolutePath();
	}

	public static File saveImg(Context context, byte[] photo) {
		FileOutputStream fileOutputStream = null;
		File file = null;
		String fileName = isSDCardExist() ? FOLDER + FILE_NAME : context
				.getFilesDir() + "/bj/" + FILE_NAME;
		if (isSDCardExist()) {
			try {
				File folder = new File(FOLDER);
				if (!folder.exists()) {
					folder.mkdirs();
				}
				file = new File(fileName);
				fileOutputStream = new FileOutputStream(file);
				fileOutputStream.write(photo);
				fileOutputStream.flush();
			} catch (FileNotFoundException e) {
			} catch (IOException e) {
			} finally {
				try {
					if (fileOutputStream != null) {
						fileOutputStream.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return file;
	}

	private void visitAll(File tempRoot) {
		File[] dirs = tempRoot.listFiles();
		if (dirs != null) {
			List<File> dirList = Arrays.asList(dirs);
			m_dirs.addAll(dirList);
			for (int i = 0; i < dirList.size(); i++) {
				this.visitAll(dirList.get(i));
			}
		}
	}

	public void rootDelete() {
		if (m_dirs != null) {
			for (int i = m_dirs.size() - 1; i >= 0; i--) {
				File f = m_dirs.remove(i);
				f.delete();
			}
		}
	}

	public void deleteDirs() {
		m_dirs = new ArrayList<File>();
		File file = new File(FOLDER + "/log/");
		m_dirs.add(file);
		this.visitAll(file);
		this.rootDelete();
	}

	public boolean isExist() {
		File file = null;
		if (isSDCardExist()) {
			file = new File(FOLDER + FILE_NAME);
			if (file.exists()) {
				return true;
			} else
				return false;

		} else {

		}
		return false;
	}

	public String fileUri() {
		File file = null;
		if (isSDCardExist()) {
			file = new File(FOLDER + FILE_NAME);
			return file.getAbsolutePath();
		} else {
		}
		return null;
	}

	public static boolean isSDCardExist() {

		return Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);

	}

}
