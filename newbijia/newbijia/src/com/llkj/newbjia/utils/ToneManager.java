package com.llkj.newbjia.utils;



import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import android.app.Service;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

public class ToneManager {
	public static MediaPlayer player = new MediaPlayer();
	private Context mContext;
	private AudioManager audioManager;
	private onCompletion oc;

	public ToneManager(Context context, onCompletion oc) {
		mContext = context;
		audioManager = (AudioManager) mContext
				.getSystemService(Service.AUDIO_SERVICE);
		this.oc = oc;
	}

	OnCompletionListener mOnCompletionListener = new OnCompletionListener() {

		@Override
		public void onCompletion(MediaPlayer mp) {
			// stop();
			if (oc != null)
				oc.stop();
			else
				stop();

		}
	};

	public void play(String filePath) {
		try {
			int maxVolume = audioManager
					.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
			audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume,
					0);
			player.reset();
			player.setOnCompletionListener(mOnCompletionListener);
			player.setDataSource(filePath);
			player.prepare();
			player.start();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void play(File file) {
		try {
			final FileInputStream fis = new FileInputStream(file);
			int maxVolume = audioManager
					.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
			audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume,
					0);
			player.reset();
			player.setOnCompletionListener(mOnCompletionListener);
			player.setDataSource(fis.getFD());
			player.prepare();
			player.start();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void stop() {
		player.stop();

	}

	interface onCompletion {
		void stop();
	}

}
