package com.llkj.newbjia.utils;

import java.io.File;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.UnknownHostException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.CharArrayBuffer;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.llkj.newbjia.bean.ResponseBean;
import com.llkj.newbjia.http.UrlConfig;

public class UploadImageUtil {

	private static int CONNECTION_TIME_OUT = 30 * 1000;
	private static int SOCKET_TIME_OUT = 30 * 1000;
	private static final String UPLOAD_FILE_TAG = "upload_file_info";
	/**
	 * 类型:1 头像
	 */
	public static final String TYPE_HEADER = "1";
	/**
	 * 类型:发布图片上传
	 */
	public static final String TYPE_PUBLISH = "2";

	private OnUploadFileForResultListener listener;

	public void setTimeOut(int CTO, int STO) {
		CONNECTION_TIME_OUT = CTO;
		SOCKET_TIME_OUT = STO;
	}

	public void uploadBg(final Context context, final File file,
			final String type, final String ImagePath) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				uploadImage(context, file, type, ImagePath);
			}
		}).start();
	}

	private void uploadImage(Context context, File file, String type,
			String ImagePath) {
		final DefaultHttpClient client = new DefaultHttpClient();
		HttpConnectionParams.setConnectionTimeout(client.getParams(),
				CONNECTION_TIME_OUT);
		HttpConnectionParams.setSoTimeout(client.getParams(), SOCKET_TIME_OUT);
		try {
			final URI uri = new URI(UrlConfig.ROOT_URL);
			HttpPost request = new HttpPost(uri);
			MultipartEntity entity = new MultipartEntity();
			// 封装body
			StringBody typeBody = new StringBody(type);
			FileBody fileBody = new FileBody(file);
			// 添加body
			if (!TextUtils.isEmpty(type)) {
				entity.addPart("type", typeBody);
			}
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
				doImageWorkResult(buffer.toString(), ImagePath, true);
			} else {
				doImageWorkResult(null, ImagePath, false);
			}
		} catch (UnknownHostException e) {
			doImageWorkResult("网络连接异常", ImagePath, false);
		} catch (Exception e) {
			Log.e(UPLOAD_FILE_TAG, e.toString());
			doImageWorkResult(null, ImagePath, false);
		} finally {
			client.getConnectionManager().shutdown();
		}
	}

	private void doImageWorkResult(String result, String imagePath,
			boolean isUploadSuccess) {
		try {
			int result_code = 0;
			String message = "";
			String url = "";
			String path = "";
			if (isUploadSuccess) {
				LogUtil.e("upload pic result =" + result);
				JSONObject resultObject = new JSONObject(result);
				if (resultObject.has(ResponseBean.RESPONSE_STATE)) {
					result_code = resultObject
							.getInt(ResponseBean.RESPONSE_STATE);
				}

				if (resultObject.has(ResponseBean.RERSPONSE_URL)) {
					url = resultObject.getString(ResponseBean.RERSPONSE_URL);
				}
				if (resultObject.has(ResponseBean.RESPONSE_PATH)) {
					path = resultObject.getString(ResponseBean.RESPONSE_PATH);
				}
				if (resultObject.has(ResponseBean.RESPONSE_MESSAGE)) {
					message = resultObject
							.getString(ResponseBean.RESPONSE_MESSAGE);
				}

			} else {
				message = result;
				Log.e(message, message);
			}
			if (listener != null)
				listener.onResultListener(isUploadSuccess, result_code, path,
						url, message, imagePath);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public interface OnUploadFileForResultListener {

		public abstract void onResultListener(boolean isUploadSuccess,
				int result_code, String path, String url, String message,
				String imagePath);
	}

	public void setListener(OnUploadFileForResultListener resultListener) {
		listener = resultListener;
	}

	public void removeListener() {
		listener = null;
	}

}
