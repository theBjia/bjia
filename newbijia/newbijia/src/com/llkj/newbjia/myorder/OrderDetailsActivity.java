package com.llkj.newbjia.myorder;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.llkj.newbjia.BaseActivity;
import com.llkj.newbjia.R;
import com.llkj.newbjia.adpater.AddFriendAdapter;
import com.llkj.newbjia.adpater.OrderDetailsAdapter;
import com.llkj.newbjia.bean.ResponseBean;
import com.llkj.newbjia.bean.UserInfoBean;
import com.llkj.newbjia.http.PoCService;
import com.llkj.newbjia.shoppingcart.WriteOrderActivity;
import com.llkj.newbjia.utils.StringUtil;
import com.llkj.newbjia.utils.ToastUtil;

/**
 * 订单详情页
 * 
 * @author John
 * 
 */
public class OrderDetailsActivity extends BaseActivity implements
		OnClickListener {

	private ListView lv_goods;//listview

	private OrderDetailsAdapter adapter;//订单适配器

	private ArrayList arrayList, arrayListtwo;

	private View vHeader, vFooter;//订单详情头部和尾部

	private TextView tv_peisongjihua, tv_status, tv_order_sn, tv_goods_amount,
			tv_bonus, tv_surplus, tv_integral_money, tv_goods_amount_two,
			tv_shipping_fee, tv_mobile, tv_ziti_name, tv_consignee;

	private int mOrderDescId;

	private String uid, order_id, bonus, surplus, integral_money, shipping_fee,
			goods_amount, order_sn, status, ziti_id, ziti_name, consignee,
			mobile;

	private Intent bigIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.order_details);

		setTitle(R.string.orderdetails, true, R.string.kong, false,
				R.string.kong);

		initView();

		initData();

		initListener();

	}

	private void initView() {

		lv_goods = (ListView) findViewById(R.id.lv_goods);

		vHeader = LayoutInflater.from(this).inflate(
				R.layout.order_details_herad, null);

		vFooter = LayoutInflater.from(this).inflate(
				R.layout.order_details_found, null);

		tv_peisongjihua = (TextView) vFooter.findViewById(R.id.tv_peisongjihua);
		
		lv_goods.addHeaderView(vHeader);
		
		lv_goods.addFooterView(vFooter);

		tv_status = (TextView) vHeader.findViewById(R.id.tv_status);

		tv_order_sn = (TextView) vHeader.findViewById(R.id.tv_order_sn);//订单编号

		tv_goods_amount = (TextView) vHeader.findViewById(R.id.tv_goods_amount);//订单金额

		tv_bonus = (TextView) vHeader.findViewById(R.id.tv_bonus);//优惠券

		tv_surplus = (TextView) vHeader.findViewById(R.id.tv_surplus);//彼佳币

		tv_integral_money = (TextView) vHeader
				.findViewById(R.id.tv_integral_money);//积分

		tv_goods_amount_two = (TextView) vHeader
				.findViewById(R.id.tv_goods_amount_two);//商品总额

		tv_shipping_fee = (TextView) vHeader.findViewById(R.id.tv_shipping_fee);//运费

		tv_mobile = (TextView) vFooter.findViewById(R.id.tv_mobile);//收货人电话

		tv_ziti_name = (TextView) vFooter.findViewById(R.id.tv_ziti_name);//自提点名称

		tv_consignee = (TextView) vFooter.findViewById(R.id.tv_consignee);//收货人姓名

	}

	private void initData() {
		bigIntent = getIntent();
		
		if (bigIntent.hasExtra("order_id")) {
			
			order_id = bigIntent.getStringExtra("order_id");
			
		}
		
		if (bigIntent.hasExtra("status")) {
			
			status = bigIntent.getStringExtra("status");
			
		}
		
		arrayList = new ArrayList();
		
		arrayListtwo = new ArrayList();
		
		adapter = new OrderDetailsAdapter(OrderDetailsActivity.this, arrayList);
		
		lv_goods.setAdapter(adapter);
		
		uid = UserInfoBean.getUserInfo(this).getUid();
		
		if (StringUtil.isNetworkConnected(this)) {
			
			mOrderDescId = mRequestManager.orderDesc(uid, order_id, true);
			
		} else {
			
			ToastUtil.makeShortText(this, R.string.no_wangluo);
			
		}
	}

	private void initListener() {
		
		tv_peisongjihua.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_peisongjihua:
			
			Intent intent = new Intent(this, PeiSongJiHuaActivity.class);
			if (arrayListtwo.size() > 0) {
				intent.putParcelableArrayListExtra("arrayList", arrayListtwo);
				startActivity(intent);
			}

			break;
		default:
			break;
		}
	}

	@Override
	public void onRequestFinished(int requestId, int resultCode, Bundle payload) {
		super.onRequestFinished(requestId, resultCode, payload);
		if (resultCode == PoCService.SUCCESS_CODE) {
			if (mOrderDescId == requestId) {
				int result = payload.getInt(ResponseBean.RESPONSE_STATE);
				if (result == 1) {
					ArrayList newList = payload
							.getParcelableArrayList(ResponseBean.RESPONSE_GOODS_LIST);
					arrayList.addAll(newList);
					ArrayList newtwoList = payload
							.getParcelableArrayList(ResponseBean.RESPONSE_DISTRIBUTION_LIST);
					arrayListtwo.addAll(newtwoList);
					adapter.notifyDataSetChanged();
					bonus = payload.getString("bonus");
					surplus = payload.getString("surplus");
					integral_money = payload.getString("integral_money");
					shipping_fee = payload.getString("shipping_fee");
					goods_amount = payload.getString("goods_amount");
					order_sn = payload.getString("order_sn");
					ziti_id = payload.getString("ziti_id");
					ziti_name = payload.getString("ziti_name");
					consignee = payload.getString("consignee");
					mobile = payload.getString("mobile");
					if ("0".equals(status)) {
						tv_status.setText("已付款");
					} else if ("1".equals(status)) {
						tv_status.setText("未付款");
					} else if ("2".equals(status)) {
						tv_status.setText("待收货");
					} else if ("3".equals(status)) {
						tv_status.setText("完成");
					} else if ("4".equals(status)) {
						tv_status.setText("已取消");
					}

					tv_order_sn.setText(order_sn);
					tv_goods_amount.setText("￥" + goods_amount);
					tv_bonus.setText("￥" + bonus);
					tv_surplus.setText("￥" + surplus);
					tv_integral_money.setText("￥" + integral_money);
					tv_goods_amount_two.setText("￥" + goods_amount);
					tv_shipping_fee.setText("￥" + shipping_fee);
					tv_mobile.setText(mobile);
					tv_ziti_name.setText(ziti_name);
					tv_consignee.setText(consignee);

				} else {
					String msg = payload
							.getString(ResponseBean.RESPONSE_MESSAGE);
					ToastUtil.makeShortText(this, msg);
				}

			}

		}
	}
}
