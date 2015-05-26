package com.llkj.newbjia.http;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.Random;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.SparseArray;
import com.llkj.cm.restfull.network.NetworkConnection;
import com.llkj.cm.restfull.requestmanager.RequestManager;
import com.llkj.newbjia.bean.KeyBean;

public class PoCRequestManager extends RequestManager {
	private static final int MAX_RANDOM_REQUEST_ID = 1000000;

	private static PoCRequestManager sInstance;

	public static PoCRequestManager from(final Context context) {
		if (sInstance == null) {
			sInstance = new PoCRequestManager(context);
			// 要调用此方法,生成UA
			NetworkConnection.generateDefaultUserAgent(context);
		}
		return sInstance;
	}

	private static Random sRandom = new Random();

	private SparseArray<Intent> mRequestSparseArray;
	private Context mContext;
	private ArrayList<WeakReference<OnRequestFinishedListener>> mListenerList;
	private Handler mHandler = new Handler();
	private EvalReceiver mEvalReceiver = new EvalReceiver(mHandler);

	private PoCRequestManager(final Context context) {
		mContext = context.getApplicationContext();
		mRequestSparseArray = new SparseArray<Intent>();
		mListenerList = new ArrayList<WeakReference<OnRequestFinishedListener>>();
	}

	/**
	 * The ResultReceiver that will receive the result from the Service
	 */
	private class EvalReceiver extends ResultReceiver {
		EvalReceiver(final Handler h) {
			super(h);
		}

		@Override
		public void onReceiveResult(final int resultCode,
				final Bundle resultData) {
			handleResult(resultCode, resultData);
		}
	}

	/**
	 * Clients may implements this interface to be notified when a request is
	 * finished
	 * 
	 * @author Foxykeep
	 */
	public static interface OnRequestFinishedListener extends EventListener {

		/**
		 * Event fired when a request is finished.
		 * 
		 * @param requestId
		 *            The request Id (to see if this is the right request)
		 * @param resultCode
		 *            The result code (0 if there was no error)
		 * @param payload
		 *            The result of the service execution.
		 */
		public void onRequestFinished(int requestId, int resultCode,
				Bundle payload);

		/**
		 * Prepare before a request will be called;
		 * 
		 * @author zhang.zk
		 * 
		 */
		public void onRequestPrepareListener();
	}

	/**
	 * Add a {@link OnRequestFinishedListener} to this {@link PoCRequestManager}
	 * . Clients may use it in order to listen to events fired when a request is
	 * finished.
	 * <p>
	 * <b>Warning !! </b> If it's an {@link Activity} that is used as a
	 * Listener, it must be detached when {@link Activity#onPause} is called in
	 * an {@link Activity}.
	 * </p>
	 * 
	 * @param listener
	 *            The listener to add to this {@link PoCRequestManager} .
	 */
	public void addOnRequestFinishedListener(
			final OnRequestFinishedListener listener) {
		synchronized (mListenerList) {
			// Check if the listener is not already in the list
			if (!mListenerList.isEmpty()) {
				for (WeakReference<OnRequestFinishedListener> weakRef : mListenerList) {
					if (weakRef.get() != null) {
						if (weakRef.get().equals(listener)) {
							return;
						}
					}
				}
			}

			mListenerList.add(new WeakReference<OnRequestFinishedListener>(
					listener));
		}
	}

	/**
	 * Remove a {@link OnRequestFinishedListener} to this
	 * {@link PoCRequestManager}.
	 * 
	 * @param listenerThe
	 *            listener to remove to this {@link PoCRequestManager}.
	 */
	public void removeOnRequestFinishedListener(
			final OnRequestFinishedListener listener) {
		synchronized (mListenerList) {
			final int listenerListSize = mListenerList.size();
			for (int i = 0; i < listenerListSize; i++) {
				if (mListenerList.get(i).get() != null) {
					if (mListenerList.get(i).get().equals(listener)) {
						mListenerList.remove(i);
						return;
					}
				}
			}
		}
	}

	/**
	 * Return whether a request (specified by its id) is still in progress or
	 * not
	 * 
	 * @param requestId
	 *            The request id
	 * @return whether the request is still in progress or not.
	 */
	public boolean isRequestInProgress(final int requestId) {
		return (mRequestSparseArray.indexOfKey(requestId) >= 0);
	}

	/**
	 * This method is call whenever a request is finished. Call all the
	 * available listeners to let them know about the finished request
	 * 
	 * @param resultCode
	 *            The result code of the request
	 * @param resultData
	 *            The bundle sent back by the service
	 *            
	 */
	protected void handleResult(final int resultCode, final Bundle resultData) {

		// Get the request Id
		final int requestId = resultData.getInt(RECEIVER_EXTRA_REQUEST_ID);

		// Remove the request Id from the "in progress" request list
		mRequestSparseArray.remove(requestId);

		// Call the available listeners
		if (mListenerList == null || mListenerList.size() == 0)
			return;
		synchronized (mListenerList) {
			for (int i = 0; i < mListenerList.size(); i++) {
				final WeakReference<OnRequestFinishedListener> weakRef = mListenerList
						.get(i);
				final OnRequestFinishedListener listener = weakRef.get();
				if (listener != null) {
					listener.onRequestFinished(requestId, resultCode,
							resultData);
				} else {
					mListenerList.remove(i);
					i--;
				}
			}
		}
	}

	/**
	 * 发起请求.默认为显示等待框
	 * 
	 * @param type
	 * @param bundle
	 * @return
	 */
	public int request(int type, Bundle bundle) {
		return request(type, bundle, true);
	}

	/**
	 * request
	 * 
	 * @param type
	 * @param bundle
	 * @param isShow
	 *            是否显示等待框
	 * 
	 * @return the request Id
	 */
	public int request(int type, Bundle bundle, boolean isShow) {
		// 获取最后一个监听器,调用它的准备方法
		// if (mListenerList != null && mListenerList.size() > 0) {
		// mListenerList.get(mListenerList.size() - 1).get()
		// .onRequestPrepareListener();
		// }
		// fixed by ning.dai. WeakReference may be contain null.
		final int listLength = mListenerList.size();
		if (mListenerList != null && listLength > 0) {
			final OnRequestFinishedListener lst = mListenerList.get(
					listLength - 1).get();
			if (lst != null && isShow) {
				lst.onRequestPrepareListener();
			}
		}
		// Check if a match to this request is already launched
		final int requestSparseArrayLength = mRequestSparseArray.size();
		for (int i = 0; i < requestSparseArrayLength; i++) {
			final Intent savedIntent = mRequestSparseArray.valueAt(i);
			if (savedIntent
					.getIntExtra(PoCService.INTENT_EXTRA_WORKER_TYPE, -1) != type) {
				continue;
			}
			return mRequestSparseArray.keyAt(i);
		}
		final int requestId = sRandom.nextInt(MAX_RANDOM_REQUEST_ID);
		final Intent intent = new Intent(mContext, PoCService.class);
		intent.putExtra(PoCService.INTENT_EXTRA_WORKER_TYPE, type);
		intent.putExtra(PoCService.INTENT_EXTRA_RECEIVER, mEvalReceiver);
		intent.putExtra(PoCService.INTENT_EXTRA_REQUEST_ID, requestId);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		mContext.startService(intent);
		mRequestSparseArray.append(requestId, intent);
		return requestId;
	}

	/**
	 * 1.1获取验证码
	 * 
	 * @param phone
	 * @param type
	 * @param isShow
	 * @return
	 */
	public int getCode(String phone, String type, boolean isShow) {
		Bundle bundle = new Bundle();
		bundle.putString(KeyBean.KEY_PHONE, phone);
		bundle.putString(KeyBean.KEY_TYPE, type);
		return request(PoCService.TYPE_INDEX_GETCODE, bundle, isShow);
	}

	/**
	 * 1.2注册
	 * 
	 * @param phone
	 * @param code
	 * @param password
	 * @param username
	 * @param isShow
	 * @return
	 */
	public int getRegister(String phone, String code, String password,
			String username, boolean isShow) {
		Bundle bundle = new Bundle();
		bundle.putString(KeyBean.KEY_PHONE, phone);
		bundle.putString(KeyBean.KEY_CODE, code);
		bundle.putString(KeyBean.KEY_PASSWORD, password);
		bundle.putString(KeyBean.KEY_NAME, username);
		return request(PoCService.TYPE_INDEX_REGISTER, bundle, isShow);
	}

	/**
	 * 1.3登录
	 * 
	 * @param phone
	 * @param password
	 * @param isShow
	 * @return
	 */
	public int getLogin(String phone, String password, boolean isShow) {
		Bundle bundle = new Bundle();
		bundle.putString(KeyBean.KEY_PHONE, phone);
		bundle.putString(KeyBean.KEY_PASSWORD, password);
		return request(PoCService.TYPE_INDEX_LOGIN, bundle, isShow);
	}

	/**
	 * 1.4找回密码
	 * 
	 * @param phone
	 * @param password
	 * @param isShow
	 * @return
	 */
	public int getForgetPassword(String phone, String code, String password,
			boolean isShow) {
		Bundle bundle = new Bundle();
		bundle.putString(KeyBean.KEY_PHONE, phone);
		bundle.putString(KeyBean.KEY_CODE, code);
		bundle.putString(KeyBean.KEY_PASSWORD, password);
		return request(PoCService.TYPE_INDEX_FORGETPOSSWORD, bundle, isShow);
	}

	/**
	 * 1.4个人信息
	 * 
	 * @param phone
	 * @param password
	 * @param isShow
	 * @return
	 */
	public int getPersonDesc(String uid, boolean isShow) {
		Bundle bundle = new Bundle();
		bundle.putString(KeyBean.KEY_UID, uid);
		return request(PoCService.TYPE_INDEX_PERSONDESC, bundle, isShow);
	}

	/**
	 * 1.5修改个性签名
	 * 
	 * @param phone
	 * @param password
	 * @param isShow
	 * @return
	 */
	public int getSignEdit(String uid, String signature, boolean isShow) {
		Bundle bundle = new Bundle();
		bundle.putString(KeyBean.KEY_UID, uid);
		bundle.putString(KeyBean.KEY_SIGNATURE, signature);
		return request(PoCService.TYPE_INDEX_SIGNEDIT, bundle, isShow);
	}

	/**
	 * 1.5修改用户名
	 * 
	 * @param phone
	 * @param password
	 * @param isShow
	 * @return
	 */
	public int getNameEdit(String uid, String name, boolean isShow) {
		Bundle bundle = new Bundle();
		bundle.putString(KeyBean.KEY_UID, uid);
		bundle.putString(KeyBean.KEY_NAME, name);
		return request(PoCService.TYPE_INDEX_NAMEEDIT, bundle, isShow);
	}

	/**
	 * 1.6我的积分
	 * 
	 * @param uid
	 * @param isShow
	 * @return
	 */
	public int getMyIntegral(String uid, boolean isShow) {
		Bundle bundle = new Bundle();
		bundle.putString(KeyBean.KEY_UID, uid);
		return request(PoCService.TYPE_INDEX_MYINTEGRAL, bundle, isShow);
	}

	/**
	 * 1.6我的彼佳币
	 * 
	 * @param uid
	 * @param isShow
	 * @return
	 */
	public int getMyCoin(String uid, boolean isShow) {
		Bundle bundle = new Bundle();
		bundle.putString(KeyBean.KEY_UID, uid);
		return request(PoCService.TYPE_INDEX_MYCOIN, bundle, isShow);
	}

	/**
	 * 1.6收藏夹
	 * 
	 * @param uid
	 * @param isShow
	 * @return
	 */
	public int getCollectList(String uid, boolean isShow) {
		Bundle bundle = new Bundle();
		bundle.putString(KeyBean.KEY_UID, uid);
		return request(PoCService.TYPE_INDEX_COLLECTLIST, bundle, isShow);
	}

	/**
	 * 1.6我的优惠券
	 * 
	 * @param uid
	 * @param isShow
	 * @return
	 */
	public int getMyPrivilege(String uid, boolean isShow) {
		Bundle bundle = new Bundle();
		bundle.putString(KeyBean.KEY_UID, uid);
		return request(PoCService.TYPE_INDEX_MYPRIVILEGE, bundle, isShow);
	}

	/**
	 * 2.1修改密码
	 * 
	 * @param uid
	 * @param isShow
	 * @return
	 */

	public int getPasswordEdit(String uid, String old_password,
			String new_password, String type, boolean isShow) {
		Bundle bundle = new Bundle();
		bundle.putString(KeyBean.KEY_UID, uid);
		bundle.putString(KeyBean.KEY_OLD_PASSWORD, old_password);
		bundle.putString(KeyBean.KEY_NEW_PASSWORD, new_password);
		bundle.putString("type", type);
		return request(PoCService.TYPE_INDEX_PASSWORDEDIT, bundle, isShow);
	}

	/**
	 * 2.2购物车列表
	 * 
	 * @param uid
	 * @param isShow
	 * @return
	 */
	public int getShopCartList(String uid, boolean isShow) {
		Bundle bundle = new Bundle();
		bundle.putString(KeyBean.KEY_UID, uid);
		return request(PoCService.TYPE_INDEX_SHOPCARTLIST, bundle, isShow);
	}

	/**
	 * 2.3收货地址列表
	 * 
	 * @param uid
	 * @param isShow
	 * @return
	 */
	public int getShopAddRessList(String uid, boolean isShow) {
		Bundle bundle = new Bundle();
		bundle.putString(KeyBean.KEY_USER_ID, uid);
		return request(PoCService.TYPE_INDEX_SHOPADDRESSLIST, bundle, isShow);
	}

	/**
	 * 2.4编辑收货地址
	 * 
	 * @param uid
	 * @param isShow
	 * @return
	 */
	public int getAddRessEdit(String consignee, String phone, String id,
			String ziti, String uid, boolean isShow) {
		Bundle bundle = new Bundle();
		bundle.putString(KeyBean.KEY_CONSIGNEE, consignee);
		bundle.putString(KeyBean.KEY_PHONE, phone);
		bundle.putString(KeyBean.KEY_ID, id);
		bundle.putString(KeyBean.KEY_UID, uid);
		bundle.putString(KeyBean.KEY_ZITI, ziti);
		return request(PoCService.TYPE_INDEX_ADDRESSEDIT, bundle, isShow);
	}

	/**
	 * 1.6判断用户是否注册
	 * 
	 * @param uid
	 * @param isShow
	 * @return
	 */
	public int isUser(String phone, String name, boolean isShow) {
		Bundle bundle = new Bundle();
		bundle.putString(KeyBean.KEY_PHONE, phone);
		bundle.putString(KeyBean.KEY_NAME, name);
		return request(PoCService.TYPE_INDEX_ISUSER, bundle, isShow);
	}

	/**
	 * 1.7根据用户手机号获取用户头像
	 * 
	 * @param uid
	 * @param isShow
	 * @return
	 */
	public int getLogo(String phone, boolean isShow) {
		Bundle bundle = new Bundle();
		bundle.putString(KeyBean.KEY_PHONE, phone);
		return request(PoCService.TYPE_INDEX_GETLOGO, bundle, isShow);
	}

	/**
	 * 2.1首页
	 * 
	 * @param uid
	 * @param isShow
	 * @return
	 */
	public int index(boolean isShow) {
		Bundle bundle = new Bundle();
		return request(PoCService.TYPE_INDEX_INDEX, bundle, isShow);
	}

	/**
	 * 2.3商品详情
	 * 
	 * @param uid
	 * @param isShow
	 * @return
	 */
	public int goodDesc(String id, String uid, boolean isShow) {
		Bundle bundle = new Bundle();
		bundle.putString(KeyBean.KEY_ID, id);
		bundle.putString(KeyBean.KEY_UID, uid);
		return request(PoCService.TYPE_INDEX_GOODDESC, bundle, isShow);
	}
	
	

	/**
	 * 2.9团购详情
	 * 
	 * @param uid
	 * @param isShow
	 * @return
	 */
	public int purchaseDesc(String id, boolean isShow) {
		Bundle bundle = new Bundle();
		bundle.putString(KeyBean.KEY_ID, id);
		return request(PoCService.TYPE_INDEX_PURCHASEDESC, bundle, isShow);
	}

	/**
	 * 2.11关注商品goodAttention
	 * 
	 * @param uid
	 * @param isShow
	 * @return
	 */
	public int goodAttention(String id, String uid, String type, boolean isShow) {
		Bundle bundle = new Bundle();
		bundle.putString(KeyBean.KEY_ID, id);
		bundle.putString(KeyBean.KEY_UID, uid);
		bundle.putString(KeyBean.KEY_TYPE, type);
		return request(PoCService.TYPE_INDEX_GOODATTENTION, bundle, isShow);
	}

	/**
	 * 2.12加入购物车接口
	 * 
	 * @param uid
	 * @param isShow
	 * @return
	 */
	public int shopCartAdd(String number, String uid, String goods_id,
			String spec, boolean isShow) {
		Bundle bundle = new Bundle();
		bundle.putString(KeyBean.KEY_NUMBER, number);
		bundle.putString(KeyBean.KEY_GOODS_ID, goods_id);
		bundle.putString(KeyBean.KEY_SPEC, spec);
		bundle.putString(KeyBean.KEY_UID, uid);
		return request(PoCService.TYPE_INDEX_SHOPCARTADD, bundle, isShow);
	}

	/**
	 * 2.7分类列表
	 * 
	 * @param uid
	 * @param isShow
	 * @return
	 */
	public int classList(String id, boolean isShow) {
		Bundle bundle = new Bundle();
		bundle.putString(KeyBean.KEY_ID, id);
		return request(PoCService.TYPE_INDEX_CLASSLIST, bundle, isShow);
	}

	/**
	 * 2.7分类列表
	 * 
	 * @param uid
	 * @param isShow
	 * @return
	 */
	public int goodList(String id, String page, boolean isShow) {
		Bundle bundle = new Bundle();
		bundle.putString(KeyBean.KEY_ID, id);
		bundle.putString(KeyBean.KEY_PAGE, page);
		return request(PoCService.TYPE_INDEX_GOODLIST, bundle, isShow);
	}

	/**
	 * 2.8意见反馈
	 * 
	 * @param uid
	 * @param isShow
	 * @return
	 */
	public int Advise(String id, String content, boolean isShow) {
		Bundle bundle = new Bundle();
		bundle.putString(KeyBean.KEY_UID, id);
		bundle.putString(KeyBean.KEY_CONTENT, content);
		return request(PoCService.TYPE_INDEX_ADVISE, bundle, isShow);
	}

	/**
	 * 2.4自提点列表
	 * 
	 * @param uid
	 * @param isShow
	 * @return
	 */
	public int zitiList(boolean isShow) {
		Bundle bundle = new Bundle();
		return request(PoCService.TYPE_INDEX_ZITILIST, bundle, isShow);
	}

	/**
	 * 2.5附近自提点
	 * 
	 * @param uid
	 * @param isShow
	 * @return
	 */
	public int ZitiDistribution(String lng, String lat, boolean isShow) {
		Bundle bundle = new Bundle();
		bundle.putString(KeyBean.KEY_LNG, lng);
		bundle.putString(KeyBean.KEY_LAT, lat);
		return request(PoCService.TYPE_INDEX_ZITIDISTRIBUTION, bundle, isShow);
	}

	/**
	 * 配送自提点是否有货
	 * 
	 * @param uid
	 * @param isShow
	 * @return
	 */
	public int judgeAvailable(String ziti_id, String goods_id, boolean isShow) {
		Bundle bundle = new Bundle();
		bundle.putString(KeyBean.KEY_ZITI_ID, ziti_id);
		bundle.putString(KeyBean.KEY_GOODS_ID, goods_id);
		return request(PoCService.TYPE_INDEX_JUDGEAVAILABLE, bundle, isShow);
	}

	/**
	 * 5.3 获取默认收货地址
	 * 
	 * @param uid
	 * @param isShow
	 * @return
	 */
	public int defaultAddress(String uid, boolean isShow) {
		Bundle bundle = new Bundle();
		bundle.putString(KeyBean.KEY_UID, uid);
		return request(PoCService.TYPE_INDEX_DEFAULTADDRESS, bundle, isShow);
	}

	/**
	 * 5.4添加收货地址
	 * 
	 * @param uid
	 * @param isShow
	 * @return
	 */
	public int getAddressAdd(String uid, String consignee, String phone,
			String ziti, boolean isShow) {
		Bundle bundle = new Bundle();
		bundle.putString(KeyBean.KEY_UID, uid);
		bundle.putString(KeyBean.KEY_CONSIGNEE, consignee);
		bundle.putString(KeyBean.KEY_PHONE, phone);
		bundle.putString(KeyBean.KEY_ZITI, ziti);
		return request(PoCService.TYPE_INDEX_ADDRESSADD, bundle, isShow);
	}

	/**
	 * 2.15搜索商品 searchGoods
	 * 
	 * @param uid
	 * @param isShow
	 * @return
	 */
	public int searchGoods(String name, String page, boolean isShow) {
		Bundle bundle = new Bundle();
		bundle.putString(KeyBean.KEY_NAME, name);
		bundle.putString(KeyBean.KEY_PAGE, page);
		return request(PoCService.TYPE_INDEX_SEARCHGOODS, bundle, isShow);
	}

	/**
	 * 3.1好友列表
	 * 
	 * @param uid
	 * @param isShow
	 * @return
	 */
	public int friendsList(String uid, boolean isShow) {
		Bundle bundle = new Bundle();
		bundle.putString(KeyBean.KEY_UID, uid);
		return request(PoCService.TYPE_INDEX_FRIENDSLIST, bundle, isShow);
	}

	/**
	 * 3.2新的好友
	 * 
	 * @param uid
	 * @param isShow
	 * @return
	 */
	public int newFriend(String uid, boolean isShow) {
		Bundle bundle = new Bundle();
		bundle.putString(KeyBean.KEY_UID, uid);
		return request(PoCService.TYPE_INDEX_NEWFRIEND, bundle, isShow);
	}

	/**
	 * 3.3搜索好友
	 * 
	 * @param uid
	 * @param isShow
	 * @return
	 */
	public int friendSearch(String uid, String name, boolean isShow) {
		Bundle bundle = new Bundle();
		bundle.putString(KeyBean.KEY_UID, uid);
		bundle.putString(KeyBean.KEY_NAME, name);
		return request(PoCService.TYPE_INDEX_FRIENDSEARCH, bundle, isShow);
	}

	/**
	 * 3.4通讯录好友
	 * 
	 * @param uid
	 * @param isShow
	 * @return
	 */
	public int addressList(String uid, String users, boolean isShow) {
		Bundle bundle = new Bundle();
		bundle.putString(KeyBean.KEY_UID, uid);
		bundle.putString(KeyBean.KEY_USERS, users);
		return request(PoCService.TYPE_INDEX_ADDRESSLIST, bundle, isShow);
	}

	/**
	 * 3.5社区圈好友
	 * 
	 * @param uid
	 * @param isShow
	 * @return
	 */
	public int communityFriend(String uid, String id, boolean isShow) {
		Bundle bundle = new Bundle();
		bundle.putString(KeyBean.KEY_UID, uid);
		bundle.putString(KeyBean.KEY_ID, id);
		return request(PoCService.TYPE_INDEX_COMMUNITYFRIEND, bundle, isShow);
	}

	/**
	 * 3.6好友资料
	 * 
	 * @param uid
	 * @param isShow
	 * @return
	 */
	public int friendDesc(String uid, String id, boolean isShow) {
		Bundle bundle = new Bundle();
		bundle.putString(KeyBean.KEY_UID, uid);
		bundle.putString(KeyBean.KEY_ID, id);
		return request(PoCService.TYPE_INDEX_FRIENDDESC, bundle, isShow);
	}

	/**
	 * 3.7加为好友
	 * 
	 * @param uid
	 * @param isShow
	 * @return
	 */
	public int friendAdd(String uid, String id, boolean isShow) {
		Bundle bundle = new Bundle();
		bundle.putString(KeyBean.KEY_UID, uid);
		bundle.putString(KeyBean.KEY_ID, id);
		return request(PoCService.TYPE_INDEX_FRIENDADD, bundle, isShow);
	}

	/**
	 * 3.9通过验证
	 * 
	 * @param uid
	 * @param isShow
	 * @return
	 */
	public int passVerification(String uid, String id, boolean isShow) {
		Bundle bundle = new Bundle();
		bundle.putString(KeyBean.KEY_UID, uid);
		bundle.putString(KeyBean.KEY_ID, id);
		return request(PoCService.TYPE_INDEX_PASSVERIFICATION, bundle, isShow);
	}

	/**
	 * 4.2加入的圈子列表
	 * 
	 * @param uid
	 * @param isShow
	 * @return
	 */
	public int communityList(String uid, boolean isShow) {
		Bundle bundle = new Bundle();
		bundle.putString(KeyBean.KEY_UID, uid);
		return request(PoCService.TYPE_INDEX_COMMUNITYLIST, bundle, isShow);
	}

	/**
	 * 4.1加入的圈子列表
	 * 
	 * @param uid
	 * @param isShow
	 * @return
	 */
	public int friendPen(String uid, String page, String type, String pen_id,
			boolean isShow) {
		Bundle bundle = new Bundle();
		bundle.putString(KeyBean.KEY_UID, uid);
		bundle.putString(KeyBean.KEY_PAGE, page);
		bundle.putString(KeyBean.KEY_TYPE, type);
		bundle.putString(KeyBean.KEY_PEN_ID, pen_id);
		return request(PoCService.TYPE_INDEX_FRIENDPEN, bundle, isShow);
	}

	/**
	 * 4.4发布圈子
	 * 
	 * @param uid
	 * @param isShow
	 * @return
	 */
	public int penAdd(String pictures, String uid, String content, String type,
			String pen_id, boolean isShow) {
		Bundle bundle = new Bundle();
		bundle.putString(KeyBean.KEY_UID, uid);
		bundle.putString(KeyBean.KEY_PICTURES, pictures);
		bundle.putString(KeyBean.KEY_CONTENT, content);
		bundle.putString(KeyBean.KEY_TYPE, type);
		bundle.putString(KeyBean.KEY_PEN_ID, pen_id);
		return request(PoCService.TYPE_INDEX_PENADD, bundle, isShow);
	}

	/**
	 * 4.5评论
	 * 
	 * @param uid
	 * @param isShow
	 * @return
	 */
	public int penComment(String uid, String id, String fid, String content,
			boolean isShow) {
		Bundle bundle = new Bundle();
		bundle.putString(KeyBean.KEY_UID, uid);
		bundle.putString(KeyBean.KEY_ID, id);
		bundle.putString(KeyBean.KEY_CONTENT, content);
		bundle.putString(KeyBean.KEY_FID, fid);
		return request(PoCService.TYPE_INDEX_PENCOMMENT, bundle, isShow);
	}

	/**
	 * 4.6点赞或取消赞
	 * 
	 * @param uid
	 * @param isShow
	 * @return
	 */
	public int penPraise(String uid, String id, String type, boolean isShow) {
		Bundle bundle = new Bundle();
		bundle.putString(KeyBean.KEY_UID, uid);
		bundle.putString(KeyBean.KEY_ID, id);
		bundle.putString(KeyBean.KEY_TYPE, type);
		return request(PoCService.TYPE_INDEX_PENPRAISE, bundle, isShow);
	}

	/**
	 * //3.8注册成功后调用
	 * 
	 * @param uid
	 * @param isShow
	 * @return
	 */
	public int registerSucceed(String uid, String id, String type,
			boolean isShow) {
		Bundle bundle = new Bundle();
		bundle.putString(KeyBean.KEY_UID, uid);
		bundle.putString(KeyBean.KEY_ID, id);
		bundle.putString(KeyBean.KEY_TYPE, type);
		return request(PoCService.TYPE_INDEX_PENPRAISE, bundle, isShow);
	}

	/**
	 * 6.20 编辑用户头像接口
	 * 
	 * @param uid
	 * @param isShow
	 * @return
	 */
	public int editLogo(String uid, String user_name, String user_path,
			boolean isShow) {
		Bundle bundle = new Bundle();
		bundle.putString(KeyBean.KEY_UID, uid);
		bundle.putString(KeyBean.KEY_USER_NAME, user_name);
		bundle.putString(KeyBean.KEY_USER_PATH, user_path);
		return request(PoCService.TYPE_INDEX_EDITLOGO, bundle, isShow);
	}

	/**
	 * 5.10 修改购物车商品数量接口
	 * 
	 * @param uid
	 * @param isShow
	 * @return
	 */
	public int editNumber(String id, String user_id, String number,
			boolean isShow) {
		Bundle bundle = new Bundle();
		bundle.putString(KeyBean.KEY_ID, id);
		bundle.putString(KeyBean.KEY_USER_ID, user_id);
		bundle.putString(KeyBean.KEY_NUMBER, number);
		return request(PoCService.TYPE_INDEX_EDITNUMBER, bundle, isShow);
	}

	/**
	 * 2.13 删除购物车
	 * 
	 * @param uid
	 * @param isShow
	 * @return
	 */
	public int shopCartDel(String id, String uid, boolean isShow) {
		Bundle bundle = new Bundle();
		bundle.putString(KeyBean.KEY_ID, id);
		bundle.putString(KeyBean.KEY_UID, uid);
		return request(PoCService.TYPE_INDEX_SHOPCARTDEL, bundle, isShow);
	}

	/**
	 * 2.14团购购买
	 * 
	 * @param uid
	 * @param isShow
	 * @return
	 */
	public int purchaseBuy(String uid, String goods_id, String group_buy_id,
			String goods_number, String consignee, String ziti_name,
			String ziti_id, String phone, String send_time, boolean isShow) {
		Bundle bundle = new Bundle();
		bundle.putString(KeyBean.KEY_UID, uid);
		bundle.putString(KeyBean.KEY_GOODS_ID, goods_id);
		bundle.putString(KeyBean.KEY_GROUP_BUY_ID, group_buy_id);
		bundle.putString(KeyBean.KEY_GOODS_NUMBER, goods_number);
		bundle.putString(KeyBean.KEY_CONSIGNEE, consignee);
		bundle.putString(KeyBean.KEY_ZITI_NAME, ziti_name);
		bundle.putString(KeyBean.KEY_ZITI_ID, ziti_id);
		bundle.putString(KeyBean.KEY_PHONE, phone);
		bundle.putString(KeyBean.KEY_SEND_TIME, send_time);
		return request(PoCService.TYPE_INDEX_PURCHASEBUY, bundle, isShow);
	}

	/**
	 * 5.11 银联支付获取Tn接口
	 * 
	 * @param uid
	 * @param isShow
	 * @return
	 */
	public int getTn(String money, String order_sn, boolean isShow) {
		Bundle bundle = new Bundle();
		bundle.putString(KeyBean.KEY_MONEY, money);
		bundle.putString(KeyBean.KEY_ORDER_SN, order_sn);

		return request(PoCService.TYPE_INDEX_GETTN, bundle, isShow);
	}

	/**
	 * 支付成功后调用
	 * 
	 * @param uid
	 * @param isShow
	 * @return
	 */
	public int alipayAffirm(String uid, String order_sn, boolean isShow) {
		Bundle bundle = new Bundle();
		bundle.putString(KeyBean.KEY_UID, uid);
		bundle.putString(KeyBean.KEY_ORDER_SN, order_sn);

		return request(PoCService.TYPE_INDEX_ALIPAYAFFIRM, bundle, isShow);
	}
	
	//TODO upload the money.
	public int alipayAffirm(String uid, String order_sn,String order_money, boolean isShow) {
		Bundle bundle = new Bundle();
		bundle.putString(KeyBean.KEY_UID, uid);
		bundle.putString(KeyBean.KEY_ORDER_SN, order_sn);
		bundle.putString(KeyBean.KEY_PAY_MONEY, order_money);
		

		return request(PoCService.TYPE_INDEX_ALIPAYAFFIRM, bundle, isShow);
	}

	/**
	 * 5.8提交订单
	 * 
	 * @param uid
	 * @param isShow
	 * @return
	 */
	public int submitOrder(String uid, String consignee, String ziti_name,
			String ziti_id, String phone, String send_time, boolean isShow) {
		Bundle bundle = new Bundle();
		bundle.putString(KeyBean.KEY_UID, uid);
		bundle.putString(KeyBean.KEY_CONSIGNEE, consignee);
		bundle.putString(KeyBean.KEY_ZITI_NAME, ziti_name);
		bundle.putString(KeyBean.KEY_ZITI_ID, ziti_id);
		bundle.putString(KeyBean.KEY_PHONE, phone);
		bundle.putString(KeyBean.KEY_SEND_TIME, send_time);
		bundle.putString(KeyBean.KEY_TYPE, "1");
		return request(PoCService.TYPE_INDEX_SUBMITORDER, bundle, isShow);
	}

	/**
	 * 6.16 确认收货
	 * 
	 * @param uid
	 * @param isShow
	 * @return
	 */
	public int verifyGood(String uid, String order_id, String distribution_id,
			boolean isShow) {

		Bundle bundle = new Bundle();

		bundle.putString(KeyBean.KEY_UID, uid);
		bundle.putString(KeyBean.KEY_ORDER_ID, order_id);
		bundle.putString(KeyBean.KEY_DISTRIBUTION_ID, distribution_id);
		return request(PoCService.TYPE_INDEX_VERIFYGOOD, bundle, isShow);
	}

	/**
	 * 2.10搜索自提点
	 * 
	 * @param uid
	 * @param isShow
	 * @return
	 */
	public int zitiSearch(String name, boolean isShow) {
		Bundle bundle = new Bundle();
		bundle.putString(KeyBean.KEY_NAME, name);

		return request(PoCService.TYPE_INDEX_ZITISEARCH, bundle, isShow);
	}

	/**
	 * 6.4 我的订单列表
	 * 
	 * @param uid
	 * @param isShow
	 * @return
	 */
	public int myOrder(String uid, boolean isShow) {
		Bundle bundle = new Bundle();
		bundle.putString(KeyBean.KEY_UID, uid);

		return request(PoCService.TYPE_INDEX_MYORDER, bundle, isShow);
	}

	/**
	 * 6.5 我的订单详情
	 * 
	 * @param uid
	 * @param isShow
	 * @return
	 */
	public int orderDesc(String uid, String order_id, boolean isShow) {
		Bundle bundle = new Bundle();
		bundle.putString(KeyBean.KEY_UID, uid);
		bundle.putString(KeyBean.KEY_ORDER_ID, order_id);
		return request(PoCService.TYPE_INDEX_ORDERDESC, bundle, isShow);
	}

	/**
	 * 6.10 浏览记录列表
	 * 
	 * @param uid
	 * @param isShow
	 * @return
	 */
	public int browseList(String goods_ids, boolean isShow) {
		Bundle bundle = new Bundle();
		bundle.putString(KeyBean.KEY_GOODS_IDS, goods_ids);
		return request(PoCService.TYPE_INDEX_BROWSELIST, bundle, isShow);
	}

	/**
	 * 帮助列表
	 * 
	 * @param uid
	 * @param isShow
	 * @return
	 */
	public int helpList(boolean isShow) {
		Bundle bundle = new Bundle();
		return request(PoCService.TYPE_INDEX_HELPLIST, bundle, isShow);
	}

	/**
	 * 6.20版本更新
	 * 
	 * @param uid
	 * @param isShow
	 * @return
	 */
	public int version(String versions, boolean isShow) {
		Bundle bundle = new Bundle();
		bundle.putString(KeyBean.KEY_VERSIONS, versions);
		return request(PoCService.TYPE_INDEX_VERSION, bundle, isShow);
	}

	/**
	 * 5.13 取消订单接口
	 * 
	 * @param uid
	 * @param isShow
	 * @return
	 */
	public int cancelOrder(String uid, String order_sn, boolean isShow) {
		Bundle bundle = new Bundle();
		bundle.putString(KeyBean.KEY_UID, uid);
		bundle.putString(KeyBean.KEY_ORDER_SN, order_sn);
		return request(PoCService.TYPE_INDEX_CANCELORDER, bundle, isShow);
	}

	/**
	 * 2.16 获取团购商品价格
	 * 
	 * @param uid
	 * @param isShow
	 * @return
	 */

	public int getGroupPrice(String uid, String goods_id, String group_buy_id,
			String goods_number, String consignee, String ziti_name,
			String ziti_id, String phone, String send_time, boolean isShow) {
		Bundle bundle = new Bundle();
		bundle.putString(KeyBean.KEY_UID, uid);
		bundle.putString(KeyBean.KEY_GOODS_ID, goods_id);
		bundle.putString(KeyBean.KEY_GROUP_BUY_ID, group_buy_id);
		bundle.putString(KeyBean.KEY_GOODS_NUMBER, goods_number);
		bundle.putString(KeyBean.KEY_CONSIGNEE, consignee);
		bundle.putString(KeyBean.KEY_ZITI_NAME, ziti_name);
		bundle.putString(KeyBean.KEY_ZITI_ID, ziti_id);
		bundle.putString(KeyBean.KEY_PHONE, phone);
		bundle.putString(KeyBean.KEY_SEND_TIME, send_time);
		return request(PoCService.TYPE_INDEX_GETGROUPPRICE, bundle, isShow);
	}

	/**
	 * 6.2社区圈
	 * 
	 * @param uid
	 * @param isShow
	 * @return
	 */
	public int appPersonCommunityList(String uid, boolean isShow) {
		Bundle bundle = new Bundle();
		bundle.putString(KeyBean.KEY_UID, uid);
		return request(PoCService.TYPE_INDEX_APPPERSON_COMMUNITYLIST, bundle,
				isShow);
	}

	/**
	 * 6.2社区圈
	 * 
	 * @param uid
	 * @param isShow
	 * @return
	 */
	public int appPersonCityList(String uid, boolean isShow) {
		Bundle bundle = new Bundle();
		bundle.putString(KeyBean.KEY_UID, uid);
		return request(PoCService.TYPE_INDEX_APPPERSON_CITYLIST, bundle, isShow);
	}

	/**
	 * 4.7 删除圈子内发布的内容接口
	 * 
	 * @param uid
	 * @param isShow
	 * @return
	 */
	public int delCircle(String uid, String id, boolean isShow) {
		Bundle bundle = new Bundle();
		bundle.putString(KeyBean.KEY_UID, uid);
		bundle.putString(KeyBean.KEY_ID, id);
		return request(PoCService.TYPE_INDEX_DELCIRCLE, bundle, isShow);
	}

	/**
	 * 3.10 删除好友
	 * 
	 * @param uid
	 * @param isShow
	 * @return
	 */
	public int friendDel(String uid, String fid, boolean isShow) {
		Bundle bundle = new Bundle();
		bundle.putString(KeyBean.KEY_UID, uid);
		bundle.putString(KeyBean.KEY_FID, fid);
		return request(PoCService.TYPE_INDEX_FRIENDDEL, bundle, isShow);
	}

}
