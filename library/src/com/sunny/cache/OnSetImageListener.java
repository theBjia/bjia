package com.sunny.cache;

import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;


/**
 * The base listener for UI. You can handle Callback.
 *
 * @author ning.dai
 */
public interface OnSetImageListener {

    /**
     * Call this method prepare download.
     *
     * @param url
     * @param imageView
     */
    void onStart(final ImageView imageView, final String url);

    /**
     * Call this method begin download.
     */
    void onStartDownloading();

    /**
     * Call this method while downloading.
     *
     * @param i percentage. (0-100)
     */
    void onProgress(int i);

    /**
     * Set image at here.
     *
     * @param imageView
     * @param drawable
     * @param cacheParams
     * @param isCached
     */
    void onFinish(final ImageView imageView, final BitmapDrawable drawable,
                  final CacheWorker.Builder cacheParams, final boolean isCached);

    /**
     * Encounter problems
     */
    void onError();

    /**
     * If {@link CacheWorker.Builder#supportGIF(boolean)} set true. This method
     * called when GIF is prepare to show.
     *
     * @param filePath GIF's file path.
     */
    void onLoadGIF(String filePath);
}
