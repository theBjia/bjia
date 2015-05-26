package com.sunny.cache;

import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import java.lang.ref.SoftReference;

/**
 * base listener.
 */
public class BaseLoadListener implements OnSetImageListener {
    SoftReference<ImageView> view;

    public BaseLoadListener(ImageView view) {
        this.view = new SoftReference<ImageView>(view);
    }

    @Override
    public void onProgress(int i) {

    }

    @Override
    public void onFinish(ImageView imageView, final BitmapDrawable drawable,
                         CacheWorker.Builder cacheParams, boolean isCached) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {

                final ImageView v = view.get();
                if (v != null) {
                    v.setImageDrawable(drawable);
                }
            }
        });
    }

    @Override
    public void onError() {

    }

    @Override
    public void onLoadGIF(String filePath) {

    }

    @Override
    public void onStartDownloading() {

    }

    @Override
    public void onStart(ImageView imageView, String url) {

    }

}