package com.llkj.newbjia.shoppingcart;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.llkj.newbjia.BaseActivity;
import com.llkj.newbjia.MyApplication;
import com.llkj.newbjia.MyClicker;
import com.llkj.newbjia.R;
import com.llkj.newbjia.adpater.WriteOrderAdapter;
import com.llkj.newbjia.bean.ResponseBean;
import com.llkj.newbjia.bean.UserInfoBean;
import com.llkj.newbjia.chat.Constants;
import com.llkj.newbjia.http.PoCService;
import com.llkj.newbjia.main.GoodDetailTwoActivity;
import com.llkj.newbjia.main.YinLianPayActivity;
import com.llkj.newbjia.utils.StringUtil;
import com.llkj.newbjia.utils.ToastUtil;

/**
 * 填写订单页
 * 
 * @author
 * 
 */

public class WriteOrderActivity extends BaseActivity implements
		OnClickListener, MyClicker {
	private ArrayList arrayList;
	private WriteOrderAdapter adapter;

	private ListView lv_show;
	private View vHeader;
	private RelativeLayout rl_Address, rl_settime;
	private TextView tv_writeOrderName, Tv_writeOrderPhone,
			Tv_writeOrderAdress, tv_ziti_time, tv_total;
	private Button bt_sumitorder;

	private int mDefaultAddress, mSumitOrderId, mGetGroupPriceId,
			mPurchaseBuyId;

	private DatePickerDialog dialog;
	private Calendar calendar;
	private Intent bigIntent;

	private String consignee, ziti_name, ziti_id, phone, send_time, uid, total,
			year, day, month, group_buy_id, goods_id, goods_number;
	private boolean isTuanGou;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.write_dan_info);
		setTitle(R.string.write, true, R.string.kong, true, R.string.kong);

		initView();
		initData();
		initListener();
	}

	private void initView() {
		calendar = Calendar.getInstance();
		dialog = new DatePickerDialog(this, dateListener,
				calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH));

		lv_show = (ListView) findViewById(R.id.lv_show);
		vHeader = LayoutInflater.from(this).inflate(R.layout.write_dan_heard,
				null);

		rl_Address = (RelativeLayout) vHeader.findViewById(R.id.rl_Address);
		rl_settime = (RelativeLayout) vHeader.findViewById(R.id.rl_settime);
		tv_ziti_time = (TextView) vHeader.findViewById(R.id.tv_ziti_time);
		tv_writeOrderName = (TextView) vHeader
				.findViewById(R.id.tv_writeOrderName);
		Tv_writeOrderPhone = (TextView) vHeader
				.findViewById(R.id.Tv_writeOrderPhone);
		Tv_writeOrderAdress = (TextView) vHeader
				.findViewById(R.id.Tv_writeOrderAdress);

		tv_total = (TextView) findViewById(R.id.tv_total);
		bt_sumitorder = (Button) findViewById(R.id.bt_sumitorder);
		lv_show.addHeaderView(vHeader);

	}

	DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker datePicker, int year, int month,
				int dayOfMonth) {
			String selected_date = year + "-" + (month + 1) + "-" + dayOfMonth;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String formattedDate = sdf.format(calendar.getTime());
			Date strDate = null, current_date = null;
			try {
				current_date = sdf.parse(formattedDate);
				strDate = sdf.parse(selected_date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			if (current_date.after(strDate)) {
				ToastUtil.makeLongText(WriteOrderActivity.this, "时间选择错误,请重新选择");
				return;
			}
			WriteOrderActivity.this.year = year + "";
			WriteOrderActivity.this.month = month + 1 + "";
			if (dayOfMonth < 10) {
				WriteOrderActivity.this.day = "0" + dayOfMonth;
			} else {
				WriteOrderActivity.this.day = "" + dayOfMonth;
			}

			// Calendar月份是从0开始,所以month要加1
			tv_ziti_time.setText(year + "年" + (month + 1) + "月" + dayOfMonth
					+ "日" + "  17:00-20:00");
		}
	};

	private void initData() {
		bigIntent = getIntent();
		if (bigIntent.hasExtra("arrayList")) {
			arrayList = bigIntent.getParcelableArrayListExtra("arrayList");
		}
		if (bigIntent.hasExtra("total")) {
			total = bigIntent.getStringExtra("total");
			tv_total.setText(total);
		}
		if (bigIntent.hasExtra("isTuanGou")) {
			isTuanGou = bigIntent.getBooleanExtra("isTuanGou", false);
			if (isTuanGou) {
				HashMap map = (HashMap) arrayList.get(0);
				group_buy_id = map.get("group_buy_id") + "";
				goods_id = map.get("goods_id") + "";
				goods_number = map.get("goods_number") + "";
				mGetGroupPriceId = mRequestManager.getGroupPrice(uid, goods_id,
						group_buy_id, goods_number, consignee, ziti_name,
						ziti_id, phone, year + month + day + "000000", false);
			}
		}

		uid = UserInfoBean.getUserInfo(this).getUid();
		if (StringUtil.isNetworkConnected(this)) {
			if (null != uid) {
				mDefaultAddress = mRequestManager.defaultAddress(uid, true);
			}
		} else {
			ToastUtil.makeShortText(this, R.string.no_wangluo);
		}
		adapter = new WriteOrderAdapter(WriteOrderActivity.this, arrayList,
				this);
		lv_show.setAdapter(adapter);
	}

	private void initListener() {
		rl_Address.setOnClickListener(this);
		rl_settime.setOnClickListener(this);
		bt_sumitorder.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_Address:
			Intent intent = new Intent(this, AceeGoodAdressActivity.class);
			startActivityForResult(intent, 100);
			break;
		case R.id.rl_settime:
			dialog.show();
			break;
		case R.id.bt_sumitorder:
			if (StringUtil.isEmpty(year)) {
				ToastUtil.makeLongText(this, "请选择日期");
				return;
			}
			if (StringUtil.isEmpty(ziti_id)) {
				ToastUtil.makeLongText(this, "请选择收货地址");
				return;
			}
			if (isTuanGou) {
				mPurchaseBuyId = mRequestManager.purchaseBuy(uid, goods_id,
						group_buy_id, goods_number, consignee, ziti_name,
						ziti_id, phone, year + month + day + "000000", true);
			} else {
				mSumitOrderId = mRequestManager.submitOrder(uid, consignee,
						ziti_name, ziti_id, phone, year + month + day
								+ "000000", true);
			}

			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data != null) {
			switch (requestCode) {
			case 100:
				Bundle bundle = data.getExtras();
				HashMap map = (HashMap) bundle.getSerializable("map");
				ziti_id = map.get(ResponseBean.RESPONSE_ID) + "";
				consignee = map.get(ResponseBean.RESPONSE_CONSIGNEE) + "";
				phone = map.get(ResponseBean.RESPONSE_PHONE) + "";
				ziti_name = map.get(ResponseBean.RESPONSE_ZITI_NAME) + "";
				tv_writeOrderName.setText(consignee);
				Tv_writeOrderAdress.setText(ziti_name);
				Tv_writeOrderPhone.setText(phone);
				break;
			case 200:
				this.finish();
				break;
			default:
				break;
			}
		}
	}

	@Override
	public void onRequestFinished(int requestId, int resultCode, Bundle payload) {
		super.onRequestFinished(requestId, resultCode, payload);
		if (resultCode == PoCService.SUCCESS_CODE) {
			if (mDefaultAddress == requestId) {
				int result = payload.getInt(ResponseBean.RESPONSE_STATE);
				if (result == 1) {
					ziti_id = payload.getString(ResponseBean.RESPONSE_ZITI_ID);
					consignee = payload
							.getString(ResponseBean.RESPONSE_CONSIGNEE);
					tv_writeOrderName.setText(consignee);
					phone = payload.getString(ResponseBean.RESPONSE_PHONE);
					Tv_writeOrderPhone.setText(phone);
					ziti_name = payload
							.getString(ResponseBean.RESPONSE_ZITI_NAME);
					Tv_writeOrderAdress.setText(ziti_name);
				} else {
					String msg = payload
							.getString(ResponseBean.RESPONSE_MESSAGE);
					ToastUtil.makeShortText(this, msg);
				}
			}
			if (mSumitOrderId == requestId) {
				int result = payload.getInt(ResponseBean.RESPONSE_STATE);
				if (result == 1) {
					MyApplication.isRefrsh = true;
					Intent dataa = new Intent();
					dataa.putExtra(Constants.REFRESH, true);
					setResult(RESULT_OK, dataa);
					String order_sn = payload
							.getString(ResponseBean.RESPONSE_ORDER_SN);
					Intent br = new Intent(Constants.REFRESH_ORDER);
					sendBroadcast(br);
					String money = StringUtil.removeBiFuhao(total);
					if (!StringUtil.isEmpty(order_sn)
							&& !StringUtil.isEmpty(money)) {
						Intent intent = new Intent(this,
								YinLianPayActivity.class);
						intent.putExtra("order_sn", order_sn);
						intent.putExtra("money", money);
						startActivityForResult(intent, 200);
					}

				} else {
					String msg = payload
							.getString(ResponseBean.RESPONSE_MESSAGE);
					ToastUtil.makeShortText(this, msg);
				}
			}
			if (mGetGroupPriceId == requestId) {
				int result = payload.getInt(ResponseBean.RESPONSE_STATE);
				if (result == 1) {
					total = payload
							.getString(ResponseBean.RESPONSE_GOODS_AMOUNT);
					tv_total.setText("￥" + total + "元");

				} else {
					String msg = payload
							.getString(ResponseBean.RESPONSE_MESSAGE);
					ToastUtil.makeShortText(this, msg);
				}
			}
			if (mPurchaseBuyId == requestId) {
				int result = payload.getInt(ResponseBean.RESPONSE_STATE);
				if (result == 1) {
					Intent dataa = new Intent();
					dataa.putExtra(Constants.REFRESH, true);
					setResult(RESULT_OK, dataa);
					String order_sn = payload
							.getString(ResponseBean.RESPONSE_ORDER_SN);
					Intent br = new Intent(Constants.REFRESH_ORDER);
					sendBroadcast(br);

					if (!StringUtil.isEmpty(order_sn)
							&& !StringUtil.isEmpty(total)) {
						Intent intent = new Intent(this,
								YinLianPayActivity.class);
						intent.putExtra("order_sn", order_sn);
						intent.putExtra("money", total);
						startActivityForResult(intent, 200);
					}
				} else {
					String msg = payload
							.getString(ResponseBean.RESPONSE_MESSAGE);
					ToastUtil.makeShortText(this, msg);
				}
			}

		}
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	public void myClick(View view, int type) {

		switch (type) {
		case 1:
			HashMap map = (HashMap) view.getTag();
			if (map.containsKey(ResponseBean.RESPONSE_GOODS_ID)) {
				String good_id = map.get(ResponseBean.RESPONSE_GOODS_ID) + "";
				Intent intent = new Intent(this, GoodDetailTwoActivity.class);
				intent.putExtra("id", good_id);
				startActivity(intent);
			}
			break;

		default:
			break;
		}
	}

	@Override
	public void myLongClick(View view, int type) {

	}
}
