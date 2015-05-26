package com.llkj.newbjia.chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.InfoWindowAdapter;
import com.amap.api.maps2d.AMap.OnCameraChangeListener;
import com.amap.api.maps2d.AMap.OnInfoWindowClickListener;
import com.amap.api.maps2d.AMap.OnMapLongClickListener;
import com.amap.api.maps2d.AMap.OnMarkerClickListener;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.llkj.newbjia.BaseActivity;
import com.llkj.newbjia.MyApplication;
import com.llkj.newbjia.R;
import com.llkj.newbjia.utils.LogUtil;
import com.llkj.newbjia.utils.StringUtil;

public class SendPositionActivity extends BaseActivity implements
		OnClickListener, OnMarkerClickListener, InfoWindowAdapter,
		OnInfoWindowClickListener, OnMapLongClickListener,
		OnGeocodeSearchListener {
	private MapView mapView;
	private AMap aMap;

	private GeocodeSearch geocoderSearch;
	private LatLng latlng;
	private LinearLayout ll_title_back;
	private Intent bigIntent;
	private boolean isSend;
	private double slat, slng;
	private String[] positions;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		MyApplication.getInstance().addActivity(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_send_position);
		mapView = (MapView) findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);// 此方法必须重写
		initView();
		addLisenter();

		LatLonPoint lp = new LatLonPoint(StringUtil.strToDouble(positions[1]),
				StringUtil.strToDouble(positions[2]));
		this.latlng = new LatLng(StringUtil.strToDouble(positions[1]),
				StringUtil.strToDouble(positions[2]));
		getAddress(lp);

	}

	private void initView() {
		// TODO Auto-generated method stub
		bigIntent = getIntent();
		if (bigIntent.hasExtra("isSend")) {
			isSend = bigIntent.getBooleanExtra("isSend", false);
		}
		if (bigIntent.hasExtra("content")) {
			String content = bigIntent.getStringExtra("content");
			positions = content.split(";");
		}
		if (StringUtil.isEmpty(positions[0])) {
			positions[0] = "朝阳区财满街";
			positions[1] = "39.11";
			positions[2] = "116.22";
		}

		if (aMap == null) {
			aMap = mapView.getMap();
			aMap.setOnCameraChangeListener(new OnCameraChangeListener() {

				@Override
				public void onCameraChangeFinish(CameraPosition cameraPosition) {

					System.out.println("zoom level is:" + cameraPosition.tilt);

				}

				@Override
				public void onCameraChange(CameraPosition arg0) {

				}
			});

		}
		geocoderSearch = new GeocodeSearch(this);
		geocoderSearch.setOnGeocodeSearchListener(this);

		aMap.setOnMarkerClickListener(this);// 添加点击marker监听事件
		aMap.setInfoWindowAdapter(this);// 添加显示infowindow监听事件
		aMap.setOnInfoWindowClickListener(this);// 设置点击infoWindow事件监听器
		if (isSend) {
			aMap.setOnMapLongClickListener(this);
		}

		ll_title_back = (LinearLayout) findViewById(R.id.ll_title_back);

	}

	private void addLisenter() {
		// TODO Auto-generated method stub
		ll_title_back.setOnClickListener(this);

	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();

	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ll_title_back:
			this.finish();
			break;

		default:
			break;
		}
	}

	/**
	 * 响应逆地理编码
	 */
	public void getAddress(final LatLonPoint latLonPoint) {

		RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200,
				GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
		geocoderSearch.getFromLocationAsyn(query);// 设置同步逆地理编码请求
	}

	@Override
	public void onMapLongClick(LatLng arg0) {
		// TODO Auto-generated method stub
		LogUtil.e("SendPositionActivity", "onMapLongClick");
		this.latlng = arg0;
		double lat = arg0.latitude;
		double lng = arg0.longitude;

		LatLonPoint lp = new LatLonPoint(lat, lng);
		getAddress(lp);
	}

	@Override
	public void onInfoWindowClick(Marker arg0) {
		// TODO Auto-generated method stub
		if (isSend) {
			String address = arg0.getTitle();
			slat = arg0.getPosition().latitude;
			slng = arg0.getPosition().longitude;
			Intent data = new Intent();
			data.putExtra("address", address);
			data.putExtra("lat", slat + "");
			data.putExtra("lng", slng + "");
			setResult(100, data);
			this.finish();
		} else {
			arg0.hideInfoWindow();
		}

	}

	@Override
	public View getInfoContents(Marker arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public View getInfoWindow(Marker arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean onMarkerClick(Marker arg0) {
		// TODO Auto-generated method stub
		LogUtil.e("SendPositionActivity", "onMarkerClick");
		return false;
	}

	@Override
	public void onGeocodeSearched(GeocodeResult arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRegeocodeSearched(RegeocodeResult arg0, int arg1) {
		// TODO Auto-generated method stub
		if (arg1 == 0) {
			if (arg0 != null && arg0.getRegeocodeAddress() != null
					&& arg0.getRegeocodeAddress().getFormatAddress() != null) {
				String addressName = arg0.getRegeocodeAddress()
						.getFormatAddress();

				MarkerOptions markerOption = new MarkerOptions();
				markerOption.position(latlng);

				markerOption.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.ditubiaozhi));
				markerOption.title(addressName);

				aMap.addMarker(markerOption);
				// ==
				CameraUpdate update = CameraUpdateFactory
						.newCameraPosition(new CameraPosition(this.latlng, 17,
								30, 0));
				aMap.animateCamera(update);

			} else {

			}
		} else if (arg1 == 27) {

		} else if (arg1 == 32) {

		} else {

		}
	}

	public void addMarkderToMap(LatLng latlng, String addressName) {

	}
}
