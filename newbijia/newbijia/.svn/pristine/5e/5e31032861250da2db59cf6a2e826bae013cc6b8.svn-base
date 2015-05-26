package com.llkj.newbjia.utils;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;

import org.jsoup.Jsoup;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.TextView;

public class MetherUtil {

	/**
	 * 温度
	 */
	private TextView mTemprater;
	/**
	 * 天气图标
	 */
	private ImageView wether_pic; // wether_iMg_id
	/**
	 * 天气
	 */
	private TextView mStatus;
	public Activity activity;

	/**
	 * 白天天气
	 */
	public String status1;
	/**
	 * 夜晚天气
	 */
	public String status2;
	/**
	 * 白天温度
	 */
	public String temperature1;
	/**
	 * 夜间温度
	 */
	public String temperature2;
	public String url = "http://php.weather.sina.com.cn/xml.php?city=";
	public String city;
	public String HtmlUil;
	AccessNetUtil ac = new AccessNetUtil();

	public String rsPonseResultXml;

	private Handler handler;

	// String city = java.net.URLEncoder.encode(“温州”,”gb2312”);
	// java.net.URLEncoder.encode(city, "gb2312");
	public void showWether(Activity activity, final String city, Handler handler)
			throws UnsupportedEncodingException, URISyntaxException {
		this.handler = handler;
		this.activity = activity;
		this.city = city;
		HtmlUil = url + city + "&password=DJOYnieT8234jlsK&day=0";
		rsPonseResultXml = ac.showResPonseStr(city);

		org.jsoup.nodes.Document doc = Jsoup.parse(rsPonseResultXml);

		if (doc.body().getElementsByTag("Profiles").size() == 0) {

			return;
		} else if (doc.body().getElementsByTag("Profiles").get(0)
				.getElementsByTag("Weather").size() == 0) {

			return;
		}
		org.jsoup.nodes.Element element = doc.body()
				.getElementsByTag("Profiles").get(0)
				.getElementsByTag("Weather").get(0);

		this.city = element.getElementsByTag("city").text();
		this.status1 = element.getElementsByTag("status1").text();
		this.status2 = element.getElementsByTag("status2").text();
		this.temperature1 = element.getElementsByTag("temperature1").text();
		this.temperature2 = element.getElementsByTag("temperature2").text();
		System.out.println("温度是" + temperature2);
		FindItemAndAetValue();
	}

	private void FindItemAndAetValue() {
		// TODO Auto-generated method stub
		Message message = handler.obtainMessage();
		Bundle bundle = new Bundle();
		bundle.putString("temperature1", temperature1);
		bundle.putString("status1", status1);
		message.setData(bundle);
		handler.sendMessage(message);

		// wether_pic.setImageResource(R.drawable.many_cloud);
		// if(status1.contains("晴")){wether_pic.setImageResource(R.drawable.many_cloud);}
		// if(status1.contains("晴")){wether_pic.setImageResource(R.drawable.many_cloud);}

		/*
		 * else if (status1.contains("雨")) {
		 * wether_pic.setImageResource(R.drawable.cloud); }else {
		 * wether_pic.setImageResource(R.drawable.sunshine); }
		 */

	}

}
