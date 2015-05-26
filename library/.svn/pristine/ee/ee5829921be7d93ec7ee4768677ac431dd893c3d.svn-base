package com.sunny;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import com.sunny.net.Callback;
import com.sunny.net.DHttpClient;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.Map;

/**
 * Created by ning.dai on 14-9-22.
 */
public class HttpUtil {

	public static void cancel(String id) {
		HttpUtil.get("", null).setCookie().run();
		HttpUtil.post("", null).setCookie().setFiles(null).run();
	}

	public static HttpRequest get(String url, Callback<?> callback) {

		return null;
	}

	public static HttpPostRequest post(String url, Callback<?> callback) {

		return null;
	}

	public HttpRequest setCallback() {
		return null;
	}

	private enum HttpMethod {
		POST, GET;
	}

	private class HttpRequest {
		HttpMethod method;
		Callback<?> callback;

		HttpRequest(HttpMethod method) {
			this.method = method;
		}

		public String run() {
			return null;
		}

		public HttpRequest toFile() {
			return this;
		}

		public HttpRequest setCookie() {
			return this;
		}
	}

	private class HttpPostRequest extends HttpRequest {

		HttpPostRequest(HttpMethod method) {
			super(method);
		}

		public HttpPostRequest setParams(Map<String, String> params) {
			return this;
		}

		public HttpPostRequest setFiles(Map<String, File> files) {
			return this;
		}

		public HttpPostRequest setCookie() {
			return this;
		}
	}

	public static void downloadImage(final String url, final ImageView view,
			final Context context, final SimpleListener lst) {
		final WeakReference<ImageView> iv = new WeakReference<ImageView>(view);
		new Thread() {
			public void run() {
				if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
					System.setProperty("http.keepAlive", "false");
				}
				DHttpClient client = new DHttpClient();
				InputStream is;
				try {
					is = client.downloadInMemory(url, context);
					final Bitmap bitmap = is == null ? null : BitmapFactory
							.decodeStream(is);
					new Handler(Looper.getMainLooper()).post(new Runnable() {
						@Override
						public void run() {
							if (lst != null) {
								lst.onFinish();
							}
							ImageView view = iv.get();
							if (view != null) {
								view.setImageBitmap(bitmap);
							}
						}
					});
				} catch (IOException e) {
					new Handler(Looper.getMainLooper()).post(new Runnable() {
						@Override
						public void run() {
							if (lst != null) {
								lst.onFinish();
							}
						}
					});
				}
			};
		}.start();
	}

	public static interface SimpleListener {
		void onFinish();
	}

}
