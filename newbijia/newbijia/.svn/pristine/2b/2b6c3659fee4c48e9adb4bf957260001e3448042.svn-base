package com.llkj.newbjia.utils;

import java.io.File;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.CharArrayBuffer;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.llkj.newbjia.bean.ResponseBean;
import com.llkj.newbjia.http.UrlConfig;

public class UploadFile {

	private static int CONNECTION_TIME_OUT = 30 * 1000;
	private static int SOCKET_TIME_OUT = 30 * 1000;
	private static final String UPLOAD_FILE_TAG = "upload_file_info";
	public static String TYPE_ONE = "1";// 上传图片
	public static String TYPE_TWO = "2";// 用户头像
	public static String TUPIAN_URL;
	private OnUploadFileForResultListener listener;

	public void setTimeOut(int CTO, int STO) {
		CONNECTION_TIME_OUT = CTO;
		SOCKET_TIME_OUT = STO;
	}

	public void uploadImg(final Context context, final Bitmap bitmap,
			final String type) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				File file = FileOperation.saveImg(context,
						ImageOperate.getBitmapByte(bitmap, "png"));
				upload(file, type);
			}
		}).start();
	}

	public void uploadBg(final File file, final String type) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				upload(file, type);
			}
		}).start();
	}

	private void upload(File file, String type) {
		final DefaultHttpClient client = new DefaultHttpClient();
		HttpConnectionParams.setConnectionTimeout(client.getParams(),
				CONNECTION_TIME_OUT);
		HttpConnectionParams.setSoTimeout(client.getParams(), SOCKET_TIME_OUT);
		try {
			if (TYPE_ONE.equals(type)) {
				TUPIAN_URL = UrlConfig.BJ_UPLOADPIC_URL;
			} else if (TYPE_TWO.equals(type)) {
				TUPIAN_URL = UrlConfig.BJ_UPLOADLOGO_URL;
			}
			final URI uri = new URI(TUPIAN_URL);
			HttpPost request = new HttpPost(uri);
			MultipartEntity entity = new MultipartEntity();
			long size = file.length();
			FileBody fileBody = new FileBody(file);
			entity.addPart("pic", fileBody);
			request.setEntity(entity);
			final HttpResponse response = client.execute(request);
			final StatusLine status = response.getStatusLine();
			final int statusCode = status.getStatusCode();
			HttpEntity contentEntity = response.getEntity();
			Log.i(UPLOAD_FILE_TAG, String.valueOf(statusCode));
			if (statusCode == HttpStatus.SC_OK) {
				int i = (int) contentEntity.getContentLength();
				if (i < 0) {
					i = 4096;
				}

				final Reader reader = new InputStreamReader(
						contentEntity.getContent());
				final CharArrayBuffer buffer = new CharArrayBuffer(i);
				final char[] tmp = new char[1024];
				int l;
				while ((l = reader.read(tmp)) != -1) {
					buffer.append(tmp, 0, l);
				}
				Log.e(UPLOAD_FILE_TAG, "content:" + buffer.toString());
				doWorkResult(buffer.toString(), true, type);
			} else {
				doWorkResult(null, false, type);
			}
		} catch (Exception e) {
			Log.e(UPLOAD_FILE_TAG, e.toString());
			doWorkResult(null, false, type);
		} finally {
			client.getConnectionManager().shutdown();
		}
	}

	private void doWorkResult(String result, boolean isUploadSuccess,
			String type) {
		try {
			int state = 0;
			String pic_id = "";
			String message = "";
			String url = "";
			String path = "";
			String user_name = "";
			String user_path = "";
			if (isUploadSuccess) {
				JSONObject resultObject = new JSONObject(result);
				if (resultObject.has(ResponseBean.RESPONSE_STATE)) {
					state = resultObject.getInt(ResponseBean.RESPONSE_STATE);
				}

				if (resultObject.has(ResponseBean.RESPONSE_ID)) {
					pic_id = resultObject.getString(ResponseBean.RESPONSE_ID);
				}
				if (resultObject.has(ResponseBean.RESPONSE_URL)) {
					url = resultObject.getString(ResponseBean.RESPONSE_URL);
				}
				if (resultObject.has(ResponseBean.RESPONSE_MESSAGE)) {
					message = resultObject
							.getString(ResponseBean.RESPONSE_MESSAGE);
				}
				if (resultObject.has(ResponseBean.RESPONSE_PATH)) {
					path = resultObject.getString(ResponseBean.RESPONSE_PATH);
				}
				if (resultObject.has(ResponseBean.RESPONSE_USER_NAME)) {
					user_name = resultObject
							.getString(ResponseBean.RESPONSE_USER_NAME);
				}
				if (resultObject.has(ResponseBean.RESPONSE_USER_PATH)) {
					user_path = resultObject
							.getString(ResponseBean.RESPONSE_USER_PATH);
				}

			}
			if (listener != null) {
				if (TYPE_ONE.equals(type)) {
					listener.onResultListener(isUploadSuccess, state, pic_id,
							message, url, path);
				} else if (TYPE_TWO.equals(type)) {
					listener.onResultListener(isUploadSuccess, state,
							user_path, user_name, url, null);
				}

			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public interface OnUploadFileForResultListener {
		public abstract void onResultListener(boolean isUploadSuccess,
				int state, String pic_id, String message, String url,
				String path);
	}

	public void setListener(OnUploadFileForResultListener resultListener) {
		listener = resultListener;
	}

	public void removeListener() {
		listener = null;
	}

}
