package com.llkj.newbjia.main;

import java.net.URLEncoder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alipay.Keys;
import com.alipay.Result;
import com.alipay.Rsa;
import com.alipay.android.app.sdk.AliPay;
import com.llkj.newbjia.BaseActivity;
import com.llkj.newbjia.MyApplication;
import com.llkj.newbjia.R;
import com.llkj.newbjia.bean.ResponseBean;
import com.llkj.newbjia.bean.UserInfoBean;
import com.llkj.newbjia.chat.Constants;
import com.llkj.newbjia.http.PoCService;
import com.llkj.newbjia.utils.StringUtil;
import com.llkj.newbjia.utils.ToastUtil;
import com.unionpay.UPPayAssistEx;
import com.unionpay.uppay.PayActivity;

public class YinLianPayActivity extends BaseActivity implements OnClickListener {

	private RelativeLayout rl_one, rl_three;

	public static final int PLUGIN_VALID = 0;
	public static final int PLUGIN_NOT_INSTALLED = -1;
	public static final int PLUGIN_NEED_UPGRADE = 2;
	public static final String LOG_TAG = "YinLianPayActivity";
	/*****************************************************************
	 * mMode参数解释： "00" - 启动银联正式环境 "01" - 连接银联测试环境
	 *****************************************************************/
	private String mMode = "00", uid;
	private ImageView iv_zhifubao, iv_yinhangka;
	private boolean isYinlian = true;
	private int mRequestId, mAlipayAffirmId;
	private String money, order_sn, title, intro;
	private Intent bigIntent, dataa;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		MyApplication.getInstance().addActivity(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_select_pay);
		setTitle(R.string.select_pay, true, R.string.kong, false, R.string.kong);
		initView();
		initData();
		initListener();

	}

	private void initView() {
		// TODO Auto-generated method stub
		rl_one = (RelativeLayout) findViewById(R.id.rl_one);
		rl_three = (RelativeLayout) findViewById(R.id.rl_three);

		iv_zhifubao = (ImageView) findViewById(R.id.iv_zhifubao);
		iv_yinhangka = (ImageView) findViewById(R.id.iv_yinhangka);

	}

	private void initListener() {
		// TODO Auto-generated method stub
		rl_one.setOnClickListener(this);
		rl_three.setOnClickListener(this);
	}

	private void initData() {
		dataa = new Intent();
		setResult(RESULT_OK, dataa);
		uid = UserInfoBean.getUserInfo(this).getUid();
		bigIntent = getIntent();
		// money, order_sn, title, intro
		if (bigIntent.hasExtra("money")) {
			money = bigIntent.getStringExtra("money");
		}
		//TODO
		//money = "0.01";
		if (bigIntent.hasExtra("order_sn")) {
			order_sn = bigIntent.getStringExtra("order_sn");
		}
		if (bigIntent.hasExtra("title")) {
			title = bigIntent.getStringExtra("title");
		} else {
			title = "商品";
		}
		if (bigIntent.hasExtra("intro")) {
			intro = bigIntent.getStringExtra("intro");
		} else {
			intro = "商品购买";
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_one:
			isYinlian = true;
			setPayStyle(isYinlian);
			if (StringUtil.isNetworkConnected(this)) {

				if (!StringUtil.isEmpty(uid)) {
					mRequestId = mRequestManager.getTn(money, order_sn, true);
				} else {
					ToastUtil.makeShortText(this, R.string.xiandenglu);
				}

			} else {
				ToastUtil.makeShortText(this, R.string.no_wangluo);
			}

			break;
		case R.id.rl_three:
			isYinlian = false;
			setPayStyle(isYinlian);
			zhifubaoPay();
			break;
		default:
			break;
		}

	}

	public void setPayStyle(boolean isYinlian) {
		if (isYinlian) {
			iv_yinhangka.setImageResource(R.drawable.icon_selected);
			iv_zhifubao.setImageResource(R.drawable.icon_no_select);
		} else {
			iv_yinhangka.setImageResource(R.drawable.icon_no_select);
			iv_zhifubao.setImageResource(R.drawable.icon_selected);
		}

	}

	public void doStartUnionPayPlugin(Activity activity, String tn, String mode) {
		// mMode参数解释：
		// 0 - 启动银联正式环境
		// 1 - 连接银联测试环境
		int ret = UPPayAssistEx.startPay(this, null, null, tn, mode);
		if (ret == PLUGIN_NEED_UPGRADE || ret == PLUGIN_NOT_INSTALLED) {
			// 需要重新安装控件
			Log.e(LOG_TAG, " plugin not found or need upgrade!!!");

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("提示");
			builder.setMessage("完成购买需要安装银联支付控件，是否安装？");

			builder.setNegativeButton("确定",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});

			builder.setPositiveButton("取消",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			builder.create().show();

		}
		Log.e(LOG_TAG, "" + ret);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		/*************************************************
		 * 
		 * 步骤3：处理银联手机支付控件返回的支付结果
		 * 
		 ************************************************/
		if (data == null) {
			return;
		}

		String msg = "";
		/*
		 * 支付控件返回字符串:success、fail、cancel 分别代表支付成功，支付失败，支付取消
		 */
		String str = data.getExtras().getString("pay_result");
		if (str.equalsIgnoreCase("success")) {
			msg = "支付成功！";
			Intent dataa = new Intent();
			dataa.putExtra(Constants.REFRESH, true);
			setResult(RESULT_OK, dataa);

		} else if (str.equalsIgnoreCase("fail")) {
			msg = "支付失败！";
		} else if (str.equalsIgnoreCase("cancel")) {
			msg = "用户取消了支付";
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("支付结果通知");
		builder.setMessage(msg);
		builder.setInverseBackgroundForced(true);
		// builder.setCustomTitle();
		builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();

	}

	public void zhifubaoPay() {
		new Thread() {
			public void run() {
				AliPay alipay = new AliPay(YinLianPayActivity.this, mHandler);

				// 设置为沙箱模式，不设置默认为线上环境
				// alipay.setSandBox(true);

				String info = getInfo(money, order_sn, title, intro);
				String sign = Rsa.sign(info, Keys.PRIVATE);
				sign = URLEncoder.encode(sign);
				info += "&sign=\"" + sign + "\"&" + getSignType();
				final String orderInfo = info;
				String result = alipay.pay(orderInfo);

				Log.i("GetGoldActivity", "result = " + result);
				Message msg = new Message();
				msg.what = RQF_PAY;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		}.start();
	}

	// ========================
	// 支付宝支付
	private static final int RQF_PAY = 1;

	/*
	 * partner="2088101568358171"&seller_id="alipay-test09@alipay.com"&out_trade_no
	 * ="0819145412-6177"&subject="《暗黑破坏神3:凯恩之书》"&body=
	 * "暴雪唯一官方授权中文版!玩家必藏!附赠暗黑精致手绘地图!绝不仅仅是一本暗黑的故事或画册，而是一个栩栩如生的游戏再现。是游戏玩家珍藏的首选。"
	 * &total_fee
	 * ="0.01"&notify_url="http%3A%2F%2Fnotify.msp.hk%2Fnotify.htm"&service
	 * ="mobile.securitypay.pay"
	 * &payment_type="1"&_input_charset="utf-8"&it_b_pay
	 * ="30m"&show_url="m.alipay.com"&sign=
	 * "lBBK%2F0w5LOajrMrji7DUgEqNjIhQbidR13GovA5r3TgIbNqv231yC1NksLdw%2Ba3JnfHXoXuet6XNNHtn7VE%2BeCoRO1O%2BR1KugLrQEZMtG5jmJIe2pbjm%2F3kb%2FuGkpG%2BwYQYI51%2BhA3YBbvZHVQBYveBqK%2Bh8mUyb7GM1HxWs9k4%3D"
	 * &sign_type="RSA"
	 */
	public String getInfo(String coins, String id, String title, String intro) {
		StringBuilder sb = new StringBuilder();
		// partner 合作者身份ID 签约的支付宝账号对应的支付宝唯一用户号。 以2088开头的16位纯数字组成。
		sb.append("partner=\"");
		sb.append(Keys.DEFAULT_PARTNER);
		// out_trade_no 商户网站唯一订单号
		sb.append("\"&out_trade_no=\"");
		sb.append(id);
		// subject 商品名称
		sb.append("\"&subject=\"");
		sb.append(title);
		// body 商品详情
		sb.append("\"&body=\"");
		sb.append(intro);
		// total_fee 总金额
		sb.append("\"&total_fee=\"");
		sb.append(coins);
		// notify_url 支付宝服务器主动通知商户网站里指定的页面http路径。需要URL encode
		// sb.append("\"&notify_url=\"");

		// 网址需要做URL编码
		// sb.append(URLEncoder.encode("http://notify.java.jpxx.org/index.jsp"));
		// service 接口名称
		sb.append("\"&service=\"mobile.securitypay.pay");
		// _input_charset 参数编码字符集
		sb.append("\"&_input_charset=\"UTF-8");
		//
		sb.append("\"&return_url=\"");
		sb.append(URLEncoder.encode("http://m.alipay.com"));
		// payment_type 支付类型
		sb.append("\"&payment_type=\"1");
		// seller_id 卖家支付宝账号
		sb.append("\"&seller_id=\"");
		sb.append(Keys.DEFAULT_SELLER);
		// show_url 商品展示地址
		// 如果show_url值为空，可不传
		// sb.append("\"&show_url=\"");

		// it_b_pay 未付款交易的超时时间 1m～15d。 m-分钟，h-小时，d-天
		sb.append("\"&it_b_pay=\"1m");
		sb.append("\"");

		return new String(sb);
	}

	private String getSignType() {
		return "sign_type=\"RSA\"";
	}

	/*
	 * resultStatus={9000};memo={};result={partner="2088101568358171"&seller_id=
	 * "alipay-test09@alipay.com"
	 * &out_trade_no="0819145412-6177"&subject="《暗黑破坏神3:凯恩之书》"
	 * &body="暴雪唯一官方授权中文版!玩家必藏!附赠暗黑精致手绘地图!绝不仅仅是一本暗黑的故事或画册，而是一个栩栩如生的游戏再现。是游戏玩家珍藏的首选。"
	 * &
	 * total_fee="0.01"&notify_url="http%3A%2F%2Fnotify.msp.hk%2Fnotify.htm"&service
	 * =
	 * "mobile.securitypay.pay"&payment_type="1"&_input_charset="utf-8"&it_b_pay
	 * ="30m"&show_url="m.alipay.com"&success="true"&sign_type="RSA"&sign=
	 * "hkFZr+zE9499nuqDNLZEF7W75RFFPsly876QuRSeN8WMaUgcdR00IKy5ZyBJ4eldhoJ/2zghqrD4E2G2mNjs3aE+HCLiBXrPDNdLKCZgSOIqmv46TfPTEqopYfhs+o5fZzXxt34fwdrzN4mX6S13cr3UwmEV4L3Ffir/02RBVtU="
	 * }
	 */
	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Result result = new Result((String) msg.obj);
			switch (msg.what) {
			case RQF_PAY:

				result.parseResult();

				if (result.resultStatus.equals("9000")) {
					Toast.makeText(YinLianPayActivity.this, "操作成功",
							Toast.LENGTH_SHORT).show();
					//TODO change it here.
//					mAlipayAffirmId = mRequestManager.alipayAffirm(uid,
//							order_sn,true);
					
					mAlipayAffirmId = mRequestManager.alipayAffirm(uid,
							order_sn,result.getTotalFee() ,true);

				}
				if (result.resultStatus.equals("4000")) {
					Toast.makeText(YinLianPayActivity.this, "系统异常",
							Toast.LENGTH_SHORT).show();

				}

				if (result.resultStatus.equals("4001")) {
					Toast.makeText(YinLianPayActivity.this, "数据格式不正确",
							Toast.LENGTH_SHORT).show();

				}
				if (result.resultStatus.equals("4003")) {
					Toast.makeText(YinLianPayActivity.this,
							"该用户绑定的支付宝账户被冻结或不允许支付", Toast.LENGTH_SHORT).show();

				}
				if (result.resultStatus.equals("4003")) {
					Toast.makeText(YinLianPayActivity.this, "该用户已解除绑定",
							Toast.LENGTH_SHORT).show();

				}
				if (result.resultStatus.equals("4005")) {
					Toast.makeText(YinLianPayActivity.this, "绑定失败或没有绑定",
							Toast.LENGTH_SHORT).show();

				}
				if (result.resultStatus.equals("4006")) {
					Toast.makeText(YinLianPayActivity.this, "订单支付失败",
							Toast.LENGTH_SHORT).show();

				}
				if (result.resultStatus.equals("4010")) {
					Toast.makeText(YinLianPayActivity.this, "重新绑定账户",
							Toast.LENGTH_SHORT).show();

				}
				if (result.resultStatus.equals("6000")) {
					Toast.makeText(YinLianPayActivity.this, "支付服务正在进行升级操作",
							Toast.LENGTH_SHORT).show();

				}
				if (result.resultStatus.equals("6001")) {
					Toast.makeText(YinLianPayActivity.this, "用户中途取消支付操作",
							Toast.LENGTH_SHORT).show();

				}
				if (result.resultStatus.equals("7001")) {
					Toast.makeText(YinLianPayActivity.this, "网页支付失败",
							Toast.LENGTH_SHORT).show();

				}

				break;
			default:
				break;
			}
		};
	};

	@Override
	public void onRequestFinished(int requestId, int resultCode, Bundle payload) {
		super.onRequestFinished(requestId, resultCode, payload);
		if (resultCode == PoCService.SUCCESS_CODE) {

			if (mRequestId == requestId) {
				int result = payload.getInt(ResponseBean.RESPONSE_STATE);
				if (result == 1) {
					String tn = payload.getString(ResponseBean.RESPONSE_TN);
					if(!StringUtil.isEmpty(tn)){
						UPPayAssistEx.startPayByJAR(this, PayActivity.class, null,
								null, tn, mMode);
					}else{  
						ToastUtil.makeLongText(getApplicationContext(), "获取不到tn");
					}
				
				} else {
					String msg = payload
							.getString(ResponseBean.RESPONSE_MESSAGE);
					ToastUtil.makeShortText(this, msg);
				}

			}
			if (mAlipayAffirmId == requestId) {
				int result = payload.getInt(ResponseBean.RESPONSE_STATE);
				if (result == 1) {
					//TODO added by wang .
					String msg = payload
							.getString(ResponseBean.RESPONSE_MESSAGE);
					ToastUtil.makeShortText(this, msg);
				} else {
					String msg = payload
							.getString(ResponseBean.RESPONSE_MESSAGE);
					ToastUtil.makeShortText(this, msg);
				}

			}
		}
	}

	
}
