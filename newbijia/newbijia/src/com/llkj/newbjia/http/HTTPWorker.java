package com.llkj.newbjia.http;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.LinkedHashMap;

import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONException;

import android.content.Intent;
import android.os.Bundle;

import com.llkj.cm.restfull.exception.RestClientException;
import com.llkj.cm.restfull.network.NetworkConnection;
import com.llkj.cm.restfull.network.NetworkConnection.NetworkConnectionResult;
import com.llkj.newbjia.bean.KeyBean;
import com.llkj.newbjia.factory.AddRessAddFactory;
import com.llkj.newbjia.factory.AddressListFactory;
import com.llkj.newbjia.factory.AdviseFactory;
import com.llkj.newbjia.factory.AlipayAffirmFactory;
import com.llkj.newbjia.factory.AppPersonCityListFactory;
import com.llkj.newbjia.factory.AppPersonCommunityListFactory;
import com.llkj.newbjia.factory.BrowseListFactory;
import com.llkj.newbjia.factory.CancelOrderFactory;
import com.llkj.newbjia.factory.ClassListFactory;
import com.llkj.newbjia.factory.CollectListFactory;
import com.llkj.newbjia.factory.CommunityFriendFactory;
import com.llkj.newbjia.factory.CommunityListFactory;
import com.llkj.newbjia.factory.DefaultAddressFactory;
import com.llkj.newbjia.factory.DeleteCircleFactory;
import com.llkj.newbjia.factory.EditLogoFactory;
import com.llkj.newbjia.factory.EditNumberFactory;
import com.llkj.newbjia.factory.ForgetpasswordFactory;
import com.llkj.newbjia.factory.FriendAddFactory;
import com.llkj.newbjia.factory.FriendDescFactory;
import com.llkj.newbjia.factory.FriendPenFactory;
import com.llkj.newbjia.factory.FriendSearchFactory;
import com.llkj.newbjia.factory.FriendsListFactory;
import com.llkj.newbjia.factory.GetCodeFactory;
import com.llkj.newbjia.factory.GetGroupPriceFactory;
import com.llkj.newbjia.factory.GetLogoFactory;
import com.llkj.newbjia.factory.GetTnFactory;
import com.llkj.newbjia.factory.GoodAttentionFactory;
import com.llkj.newbjia.factory.GoodDescFactory;
import com.llkj.newbjia.factory.GoodListFactory;
import com.llkj.newbjia.factory.HelpListFactory;
import com.llkj.newbjia.factory.IndexFactory;
import com.llkj.newbjia.factory.IsUserFactory;
import com.llkj.newbjia.factory.JudgeAvailableFactory;
import com.llkj.newbjia.factory.LoginFactory;
import com.llkj.newbjia.factory.MyCoinFactory;
import com.llkj.newbjia.factory.MyIntegralFactory;
import com.llkj.newbjia.factory.MyOrderFactory;
import com.llkj.newbjia.factory.MyPrivilegeFactory;
import com.llkj.newbjia.factory.NameEditFactory;
import com.llkj.newbjia.factory.NewFriendFactory;
import com.llkj.newbjia.factory.OrderDescFactory;
import com.llkj.newbjia.factory.PassVerificationFactory;
import com.llkj.newbjia.factory.PasswordEditFactory;
import com.llkj.newbjia.factory.PenAddFactory;
import com.llkj.newbjia.factory.PenCommentFactory;
import com.llkj.newbjia.factory.PenPraiseFactory;
import com.llkj.newbjia.factory.PersonDescFactory;
import com.llkj.newbjia.factory.PurchaseBuyFactory;
import com.llkj.newbjia.factory.PurchaseDescFactory;
import com.llkj.newbjia.factory.RegisterFactory;
import com.llkj.newbjia.factory.SearchGoodsFactory;
import com.llkj.newbjia.factory.ShopAddRessListFactory;
import com.llkj.newbjia.factory.ShopCartAddFactory;
import com.llkj.newbjia.factory.ShopCartDelFactory;
import com.llkj.newbjia.factory.ShopCartListFactory;
import com.llkj.newbjia.factory.SignEditFactory;
import com.llkj.newbjia.factory.SubmitOrderFactory;
import com.llkj.newbjia.factory.VerifyGoodFactory;
import com.llkj.newbjia.factory.VersionFactory;
import com.llkj.newbjia.factory.ZitiDistributionFactory;
import com.llkj.newbjia.factory.ZitiListFactory;
import com.llkj.newbjia.factory.ZitiSearchFactory;

public class HTTPWorker {
	// 获取验证码
	public static Bundle startGetCode(Intent intent)
			throws IllegalStateException, IOException, URISyntaxException,
			RestClientException, ParserConfigurationException, JSONException {
		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		params.put(KeyBean.KEY_PHONE, intent.getStringExtra(KeyBean.KEY_PHONE));
		params.put(KeyBean.KEY_TYPE, intent.getStringExtra(KeyBean.KEY_TYPE));
		NetworkConnectionResult wsResult = NetworkConnection
				.retrieveResponseFromService(UrlConfig.BJ_GETCODE_URL,
						NetworkConnection.METHOD_GET, params);
		return GetCodeFactory.parseResult(wsResult.wsResponse);
	}

	// 注册
	public static Bundle startRegister(Intent intent)
			throws IllegalStateException, IOException, URISyntaxException,
			RestClientException, ParserConfigurationException, JSONException {
		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		params.put(KeyBean.KEY_PHONE, intent.getStringExtra(KeyBean.KEY_PHONE));
		params.put(KeyBean.KEY_CODE, intent.getStringExtra(KeyBean.KEY_CODE));
		params.put(KeyBean.KEY_PASSWORD,
				intent.getStringExtra(KeyBean.KEY_PASSWORD));
		params.put(KeyBean.KEY_NAME, intent.getStringExtra(KeyBean.KEY_NAME));
		NetworkConnectionResult wsResult = NetworkConnection
				.retrieveResponseFromService(UrlConfig.BJ_REGISTER_URL,
						NetworkConnection.METHOD_POST, params);
		return RegisterFactory.parseResult(wsResult.wsResponse);
	}

	// 登录
	public static Bundle startLogin(Intent intent)
			throws IllegalStateException, IOException, URISyntaxException,
			RestClientException, ParserConfigurationException, JSONException {
		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		params.put(KeyBean.KEY_PHONE, intent.getStringExtra(KeyBean.KEY_PHONE));
		params.put(KeyBean.KEY_PASSWORD,
				intent.getStringExtra(KeyBean.KEY_PASSWORD));
		NetworkConnectionResult wsResult = NetworkConnection
				.retrieveResponseFromService(UrlConfig.BJ_LOGIN_URL,
						NetworkConnection.METHOD_POST, params);
		return LoginFactory.parseResult(wsResult.wsResponse);
	}

	// 找回密码
	public static Bundle startForgetpassword(Intent intent)
			throws IllegalStateException, IOException, URISyntaxException,
			RestClientException, ParserConfigurationException, JSONException {
		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		params.put(KeyBean.KEY_PHONE, intent.getStringExtra(KeyBean.KEY_PHONE));
		params.put(KeyBean.KEY_CODE, intent.getStringExtra(KeyBean.KEY_CODE));
		params.put(KeyBean.KEY_PASSWORD,
				intent.getStringExtra(KeyBean.KEY_PASSWORD));
		NetworkConnectionResult wsResult = NetworkConnection
				.retrieveResponseFromService(UrlConfig.BJ_FORGETPASSWORD_URL,
						NetworkConnection.METHOD_POST, params);
		return ForgetpasswordFactory.parseResult(wsResult.wsResponse);
	}

	// 个人信息
	public static Bundle startPersonDesc(Intent intent)
			throws IllegalStateException, IOException, URISyntaxException,
			RestClientException, ParserConfigurationException, JSONException {
		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		params.put(KeyBean.KEY_UID, intent.getStringExtra(KeyBean.KEY_UID));
		NetworkConnectionResult wsResult = NetworkConnection
				.retrieveResponseFromService(UrlConfig.BJ_PERSONDESC_URL,
						NetworkConnection.METHOD_GET, params);
		return PersonDescFactory.parseResult(wsResult.wsResponse);
	}

	// 我的彼佳币
	public static Bundle startMyCoin(Intent intent)
			throws IllegalStateException, IOException, URISyntaxException,
			RestClientException, ParserConfigurationException, JSONException {
		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		params.put(KeyBean.KEY_UID, intent.getStringExtra(KeyBean.KEY_UID));
		NetworkConnectionResult wsResult = NetworkConnection
				.retrieveResponseFromService(UrlConfig.BJ_MYCOIN_URL,
						NetworkConnection.METHOD_GET, params);
		return MyCoinFactory.parseResult(wsResult.wsResponse);
	}

	// 修改个性签名
	public static Bundle startSignEdit(Intent intent)
			throws IllegalStateException, IOException, URISyntaxException,
			RestClientException, ParserConfigurationException, JSONException {
		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		params.put(KeyBean.KEY_UID, intent.getStringExtra(KeyBean.KEY_UID));
		params.put(KeyBean.KEY_SIGNATURE,
				intent.getStringExtra(KeyBean.KEY_SIGNATURE));
		NetworkConnectionResult wsResult = NetworkConnection
				.retrieveResponseFromService(UrlConfig.BJ_SIGNEDIT_URL,
						NetworkConnection.METHOD_GET, params);
		return SignEditFactory.parseResult(wsResult.wsResponse);
	}

	// 修改用户名
	public static Bundle startNameEdit(Intent intent)
			throws IllegalStateException, IOException, URISyntaxException,
			RestClientException, ParserConfigurationException, JSONException {
		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		params.put(KeyBean.KEY_UID, intent.getStringExtra(KeyBean.KEY_UID));
		params.put(KeyBean.KEY_NAME, intent.getStringExtra(KeyBean.KEY_NAME));
		NetworkConnectionResult wsResult = NetworkConnection
				.retrieveResponseFromService(UrlConfig.BJ_NAMEEDIT_URL,
						NetworkConnection.METHOD_GET, params);
		return NameEditFactory.parseResult(wsResult.wsResponse);
	}

	// 我的积分
	public static Bundle startMyIntegral(Intent intent)
			throws IllegalStateException, IOException, URISyntaxException,
			RestClientException, ParserConfigurationException, JSONException {
		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		params.put(KeyBean.KEY_UID, intent.getStringExtra(KeyBean.KEY_UID));
		NetworkConnectionResult wsResult = NetworkConnection
				.retrieveResponseFromService(UrlConfig.BJ_MYINTEGRAL_URL,
						NetworkConnection.METHOD_GET, params);
		return MyIntegralFactory.parseResult(wsResult.wsResponse);
	}

	// 我的优惠券
	public static Bundle startMyPrivilege(Intent intent)
			throws IllegalStateException, IOException, URISyntaxException,
			RestClientException, ParserConfigurationException, JSONException {
		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		params.put(KeyBean.KEY_UID, intent.getStringExtra(KeyBean.KEY_UID));
		NetworkConnectionResult wsResult = NetworkConnection
				.retrieveResponseFromService(UrlConfig.BJ_MYPRIVILEGE_URL,
						NetworkConnection.METHOD_GET, params);
		return MyPrivilegeFactory.parseResult(wsResult.wsResponse);
	}

	// 收藏夹
	public static Bundle startCollectList(Intent intent)
			throws IllegalStateException, IOException, URISyntaxException,
			RestClientException, ParserConfigurationException, JSONException {
		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		params.put(KeyBean.KEY_UID, intent.getStringExtra(KeyBean.KEY_UID));
		NetworkConnectionResult wsResult = NetworkConnection
				.retrieveResponseFromService(UrlConfig.BJ_COLLECTLIST_URL,

				NetworkConnection.METHOD_GET, params);
		return CollectListFactory.parseResult(wsResult.wsResponse);
	}

	// 意见反馈
	public static Bundle startAdvise(Intent intent)
			throws IllegalStateException, IOException, URISyntaxException,
			RestClientException, ParserConfigurationException, JSONException {
		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		params.put(KeyBean.KEY_UID, intent.getStringExtra(KeyBean.KEY_UID));
		params.put(KeyBean.KEY_CONTENT,
				intent.getStringExtra(KeyBean.KEY_CONTENT));
		NetworkConnectionResult wsResult = NetworkConnection
				.retrieveResponseFromService(UrlConfig.BJ_ADVISE_URL,
						NetworkConnection.METHOD_POST, params);
		return AdviseFactory.parseResult(wsResult.wsResponse);
	}

	// 修改密码
	public static Bundle startPasswordEdit(Intent intent)
			throws IllegalStateException, IOException, URISyntaxException,
			RestClientException, ParserConfigurationException, JSONException {
		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		params.put(KeyBean.KEY_UID, intent.getStringExtra(KeyBean.KEY_UID));
		params.put(KeyBean.KEY_OLD_PASSWORD,
				intent.getStringExtra(KeyBean.KEY_OLD_PASSWORD));
		params.put(KeyBean.KEY_NEW_PASSWORD,
				intent.getStringExtra(KeyBean.KEY_NEW_PASSWORD));
		params.put("type", intent.getStringExtra("type"));
		NetworkConnectionResult wsResult = NetworkConnection
				.retrieveResponseFromService(UrlConfig.BJ_PASSWORDEDIT_URL,
						NetworkConnection.METHOD_POST, params);
		return PasswordEditFactory.parseResult(wsResult.wsResponse);
	}

	// 购物车列表
	public static Bundle startShopCartList(Intent intent)
			throws IllegalStateException, IOException, URISyntaxException,
			RestClientException, ParserConfigurationException, JSONException {
		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		params.put(KeyBean.KEY_UID, intent.getStringExtra(KeyBean.KEY_UID));
		NetworkConnectionResult wsResult = NetworkConnection
				.retrieveResponseFromService(UrlConfig.BJ_SHOPCARTLIST_URL,
						NetworkConnection.METHOD_GET, params);
		return ShopCartListFactory.parseResult(wsResult.wsResponse);
	}

	// 收货地址列表
	public static Bundle startShopAddRessList(Intent intent)
			throws IllegalStateException, IOException, URISyntaxException,
			RestClientException, ParserConfigurationException, JSONException {
		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		params.put(KeyBean.KEY_USER_ID,
				intent.getStringExtra(KeyBean.KEY_USER_ID));
		NetworkConnectionResult wsResult = NetworkConnection
				.retrieveResponseFromService(UrlConfig.BJ_SHOPADDRESSLIST_URL,
						NetworkConnection.METHOD_GET, params);
		return ShopAddRessListFactory.parseResult(wsResult.wsResponse);
	}

	// 编辑收货地址
	public static Bundle startAddRessEdit(Intent intent)
			throws IllegalStateException, IOException, URISyntaxException,
			RestClientException, ParserConfigurationException, JSONException {
		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		params.put(KeyBean.KEY_CONSIGNEE,
				intent.getStringExtra(KeyBean.KEY_CONSIGNEE));
		params.put(KeyBean.KEY_PHONE, intent.getStringExtra(KeyBean.KEY_PHONE));
		params.put(KeyBean.KEY_ID, intent.getStringExtra(KeyBean.KEY_ID));
		params.put(KeyBean.KEY_UID, intent.getStringExtra(KeyBean.KEY_UID));
		params.put(KeyBean.KEY_ZITI, intent.getStringExtra(KeyBean.KEY_ZITI));
		NetworkConnectionResult wsResult = NetworkConnection
				.retrieveResponseFromService(UrlConfig.BJ_ADDRESSEDIT_URL,
						NetworkConnection.METHOD_GET, params);
		return ShopAddRessListFactory.parseResult(wsResult.wsResponse);
	}

	// 添加收货地址
	public static Bundle startAddRessAdd(Intent intent)
			throws IllegalStateException, IOException, URISyntaxException,
			RestClientException, ParserConfigurationException, JSONException {
		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		params.put(KeyBean.KEY_UID, intent.getStringExtra(KeyBean.KEY_UID));
		params.put(KeyBean.KEY_CONSIGNEE,
				intent.getStringExtra(KeyBean.KEY_CONSIGNEE));
		params.put(KeyBean.KEY_PHONE, intent.getStringExtra(KeyBean.KEY_PHONE));
		params.put(KeyBean.KEY_ZITI, intent.getStringExtra(KeyBean.KEY_ZITI));
		NetworkConnectionResult wsResult = NetworkConnection
				.retrieveResponseFromService(UrlConfig.BJ_ADDRESSADD_URL,
						NetworkConnection.METHOD_GET, params);
		return AddRessAddFactory.parseResult(wsResult.wsResponse);
	}

	// 1.6判断用户是否注册
	public static Bundle startIsUser(Intent intent)
			throws IllegalStateException, IOException, URISyntaxException,
			RestClientException, ParserConfigurationException, JSONException {
		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		params.put(KeyBean.KEY_PHONE, intent.getStringExtra(KeyBean.KEY_PHONE));
		params.put(KeyBean.KEY_NAME, intent.getStringExtra(KeyBean.KEY_NAME));
		NetworkConnectionResult wsResult = NetworkConnection
				.retrieveResponseFromService(UrlConfig.BJ_ISUSERURL_URL,
						NetworkConnection.METHOD_GET, params);
		return IsUserFactory.parseResult(wsResult.wsResponse);
	}

	// 1.7根据用户手机号获取用户头像
	public static Bundle startGetLogo(Intent intent)
			throws IllegalStateException, IOException, URISyntaxException,
			RestClientException, ParserConfigurationException, JSONException {
		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		params.put(KeyBean.KEY_PHONE, intent.getStringExtra(KeyBean.KEY_PHONE));
		NetworkConnectionResult wsResult = NetworkConnection
				.retrieveResponseFromService(UrlConfig.BJ_GETLOGO_URL,
						NetworkConnection.METHOD_GET, params);
		return GetLogoFactory.parseResult(wsResult.wsResponse);
	}

	// 2.1首页
	public static Bundle startIndex(Intent intent)
			throws IllegalStateException, IOException, URISyntaxException,
			RestClientException, ParserConfigurationException, JSONException {
		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		NetworkConnectionResult wsResult = NetworkConnection
				.retrieveResponseFromService(UrlConfig.BJ_INDEX_URL,
						NetworkConnection.METHOD_GET, params);
		return IndexFactory.parseResult(wsResult.wsResponse);
	}

	// 商品详情
	public static Bundle startGooddesc(Intent intent)
			throws IllegalStateException, IOException, URISyntaxException,
			RestClientException, ParserConfigurationException, JSONException {
		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		params.put(KeyBean.KEY_ID, intent.getStringExtra(KeyBean.KEY_ID));
		params.put(KeyBean.KEY_UID, intent.getStringExtra(KeyBean.KEY_UID));
		NetworkConnectionResult wsResult = NetworkConnection
				.retrieveResponseFromService(UrlConfig.BJ_GOODDESC_URL,
						NetworkConnection.METHOD_GET, params);
		return GoodDescFactory.parseResult(wsResult.wsResponse);
	}

	// 商品详情 团
	public static Bundle startPurchaseDesc(Intent intent)
			throws IllegalStateException, IOException, URISyntaxException,
			RestClientException, ParserConfigurationException, JSONException {
		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		params.put(KeyBean.KEY_ID, intent.getStringExtra(KeyBean.KEY_ID));
		NetworkConnectionResult wsResult = NetworkConnection
				.retrieveResponseFromService(UrlConfig.BJ_PURCHASEDESC_URL,
						NetworkConnection.METHOD_GET, params);
		return PurchaseDescFactory.parseResult(wsResult.wsResponse);
	}

	//
	public static Bundle startGoodAttention(Intent intent)
			throws IllegalStateException, IOException, URISyntaxException,
			RestClientException, ParserConfigurationException, JSONException {
		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		params.put(KeyBean.KEY_ID, intent.getStringExtra(KeyBean.KEY_ID));
		params.put(KeyBean.KEY_UID, intent.getStringExtra(KeyBean.KEY_UID));
		params.put(KeyBean.KEY_TYPE, intent.getStringExtra(KeyBean.KEY_TYPE));
		NetworkConnectionResult wsResult = NetworkConnection
				.retrieveResponseFromService(UrlConfig.BJ_GOODATTENTION_URL,
						NetworkConnection.METHOD_GET, params);
		return GoodAttentionFactory.parseResult(wsResult.wsResponse);
	}

	//
	public static Bundle startShopCartAdd(Intent intent)
			throws IllegalStateException, IOException, URISyntaxException,
			RestClientException, ParserConfigurationException, JSONException {
		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		params.put(KeyBean.KEY_NUMBER,
				intent.getStringExtra(KeyBean.KEY_NUMBER));
		params.put(KeyBean.KEY_GOODS_ID,
				intent.getStringExtra(KeyBean.KEY_GOODS_ID));
		params.put(KeyBean.KEY_SPEC, intent.getStringExtra(KeyBean.KEY_SPEC));
		params.put(KeyBean.KEY_UID, intent.getStringExtra(KeyBean.KEY_UID));
		NetworkConnectionResult wsResult = NetworkConnection
				.retrieveResponseFromService(UrlConfig.BJ_SHOPCARTADD_URL,
						NetworkConnection.METHOD_GET, params);
		return ShopCartAddFactory.parseResult(wsResult.wsResponse);
	}

	//
	public static Bundle startClassList(Intent intent)
			throws IllegalStateException, IOException, URISyntaxException,
			RestClientException, ParserConfigurationException, JSONException {
		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();

		params.put(KeyBean.KEY_ID, intent.getStringExtra(KeyBean.KEY_ID));
		NetworkConnectionResult wsResult = NetworkConnection
				.retrieveResponseFromService(UrlConfig.BJ_CLASSLIST_URL,
						NetworkConnection.METHOD_GET, params);
		return ClassListFactory.parseResult(wsResult.wsResponse);
	}

	//
	public static Bundle startGoodList(Intent intent)
			throws IllegalStateException, IOException, URISyntaxException,
			RestClientException, ParserConfigurationException, JSONException {
		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();

		params.put(KeyBean.KEY_ID, intent.getStringExtra(KeyBean.KEY_ID));
		params.put(KeyBean.KEY_PAGE, intent.getStringExtra(KeyBean.KEY_PAGE));
		NetworkConnectionResult wsResult = NetworkConnection
				.retrieveResponseFromService(UrlConfig.BJ_GOODLIST_URL,
						NetworkConnection.METHOD_GET, params);
		return GoodListFactory.parseResult(wsResult.wsResponse);
	}

	// startZitiList
	public static Bundle startZitiList(Intent intent)
			throws IllegalStateException, IOException, URISyntaxException,
			RestClientException, ParserConfigurationException, JSONException {
		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		NetworkConnectionResult wsResult = NetworkConnection
				.retrieveResponseFromService(UrlConfig.BJ_ZITILIST_URL,
						NetworkConnection.METHOD_GET, params);
		return ZitiListFactory.parseResult(wsResult.wsResponse);
	}

	//
	public static Bundle startJudgeAvailable(Intent intent)
			throws IllegalStateException, IOException, URISyntaxException,
			RestClientException, ParserConfigurationException, JSONException {
		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();

		params.put(KeyBean.KEY_ZITI_ID,
				intent.getStringExtra(KeyBean.KEY_ZITI_ID));
		params.put(KeyBean.KEY_GOODS_ID,
				intent.getStringExtra(KeyBean.KEY_GOODS_ID));
		NetworkConnectionResult wsResult = NetworkConnection
				.retrieveResponseFromService(UrlConfig.BJ_JUDGEAVAILABLE_URL,
						NetworkConnection.METHOD_GET, params);
		return JudgeAvailableFactory.parseResult(wsResult.wsResponse);
	}

	// 获取默认收货地址
	public static Bundle startDefaultAddress(Intent intent)
			throws IllegalStateException, IOException, URISyntaxException,
			RestClientException, ParserConfigurationException, JSONException {
		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		params.put(KeyBean.KEY_UID, intent.getStringExtra(KeyBean.KEY_UID));
		NetworkConnectionResult wsResult = NetworkConnection
				.retrieveResponseFromService(UrlConfig.BJ_DEFAULTADDRESS_URL,
						NetworkConnection.METHOD_GET, params);
		return DefaultAddressFactory.parseResult(wsResult.wsResponse);
	}

	// 附近自提点
	public static Bundle startzitiDistribution(Intent intent)
			throws IllegalStateException, IOException, URISyntaxException,
			RestClientException, ParserConfigurationException, JSONException {
		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		params.put(KeyBean.KEY_LNG, intent.getStringExtra(KeyBean.KEY_LNG));
		params.put(KeyBean.KEY_LAT, intent.getStringExtra(KeyBean.KEY_LAT));
		NetworkConnectionResult wsResult = NetworkConnection
				.retrieveResponseFromService(UrlConfig.BJ_ZITIDISTRIBUTION_URL,
						NetworkConnection.METHOD_GET, params);
		return ZitiDistributionFactory.parseResult(wsResult.wsResponse);
	}

	//
	public static Bundle startSearchGoods(Intent intent)
			throws IllegalStateException, IOException, URISyntaxException,
			RestClientException, ParserConfigurationException, JSONException {
		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();

		params.put(KeyBean.KEY_NAME, intent.getStringExtra(KeyBean.KEY_NAME));
		params.put(KeyBean.KEY_PAGE, intent.getStringExtra(KeyBean.KEY_PAGE));
		NetworkConnectionResult wsResult = NetworkConnection
				.retrieveResponseFromService(UrlConfig.BJ_SEARCHGOODS_URL,
						NetworkConnection.METHOD_GET, params);
		return SearchGoodsFactory.parseResult(wsResult.wsResponse);
	}

	// startFriendsList
	public static Bundle startFriendsList(Intent intent)
			throws IllegalStateException, IOException, URISyntaxException,
			RestClientException, ParserConfigurationException, JSONException {
		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();

		params.put(KeyBean.KEY_UID, intent.getStringExtra(KeyBean.KEY_UID));

		NetworkConnectionResult wsResult = NetworkConnection
				.retrieveResponseFromService(UrlConfig.BJ_FRIENDSLIST_URL,
						NetworkConnection.METHOD_GET, params);
		return FriendsListFactory.parseResult(wsResult.wsResponse);
	}

	//
	public static Bundle startNewFriend(Intent intent)
			throws IllegalStateException, IOException, URISyntaxException,
			RestClientException, ParserConfigurationException, JSONException {
		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();

		params.put(KeyBean.KEY_UID, intent.getStringExtra(KeyBean.KEY_UID));

		NetworkConnectionResult wsResult = NetworkConnection
				.retrieveResponseFromService(UrlConfig.BJ_NEWFRIEND_URL,
						NetworkConnection.METHOD_GET, params);
		return NewFriendFactory.parseResult(wsResult.wsResponse);
	}

	//
	public static Bundle startFriendSearch(Intent intent)
			throws IllegalStateException, IOException, URISyntaxException,
			RestClientException, ParserConfigurationException, JSONException {
		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();

		params.put(KeyBean.KEY_UID, intent.getStringExtra(KeyBean.KEY_UID));
		params.put(KeyBean.KEY_NAME, intent.getStringExtra(KeyBean.KEY_NAME));
		NetworkConnectionResult wsResult = NetworkConnection
				.retrieveResponseFromService(UrlConfig.BJ_FRIENDSEARCH_URL,
						NetworkConnection.METHOD_GET, params);
		return FriendSearchFactory.parseResult(wsResult.wsResponse);
	}

	//
	public static Bundle startAddressList(Intent intent)
			throws IllegalStateException, IOException, URISyntaxException,
			RestClientException, ParserConfigurationException, JSONException {
		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();

		params.put(KeyBean.KEY_UID, intent.getStringExtra(KeyBean.KEY_UID));
		params.put(KeyBean.KEY_USERS, intent.getStringExtra(KeyBean.KEY_USERS));
		NetworkConnectionResult wsResult = NetworkConnection
				.retrieveResponseFromService(UrlConfig.BJ_ADDRESSLIST_URL,
						NetworkConnection.METHOD_POST, params);
		return AddressListFactory.parseResult(wsResult.wsResponse);
	}

	//
	public static Bundle startCommunityFriend(Intent intent)
			throws IllegalStateException, IOException, URISyntaxException,
			RestClientException, ParserConfigurationException, JSONException {
		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		params.put(KeyBean.KEY_UID, intent.getStringExtra(KeyBean.KEY_UID));
		params.put(KeyBean.KEY_ID, intent.getStringExtra(KeyBean.KEY_ID));
		NetworkConnectionResult wsResult = NetworkConnection
				.retrieveResponseFromService(UrlConfig.BJ_COMMUNITYFRIEND_URL,
						NetworkConnection.METHOD_GET, params);
		return CommunityFriendFactory.parseResult(wsResult.wsResponse);
	}

	//
	public static Bundle startFriendDesc(Intent intent)
			throws IllegalStateException, IOException, URISyntaxException,
			RestClientException, ParserConfigurationException, JSONException {
		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		params.put(KeyBean.KEY_UID, intent.getStringExtra(KeyBean.KEY_UID));
		params.put(KeyBean.KEY_ID, intent.getStringExtra(KeyBean.KEY_ID));
		NetworkConnectionResult wsResult = NetworkConnection
				.retrieveResponseFromService(UrlConfig.BJ_FRIENDDESC_URL,
						NetworkConnection.METHOD_GET, params);
		return FriendDescFactory.parseResult(wsResult.wsResponse);
	}

	//
	public static Bundle startFriendAdd(Intent intent)
			throws IllegalStateException, IOException, URISyntaxException,
			RestClientException, ParserConfigurationException, JSONException {
		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		params.put(KeyBean.KEY_UID, intent.getStringExtra(KeyBean.KEY_UID));
		params.put(KeyBean.KEY_ID, intent.getStringExtra(KeyBean.KEY_ID));
		NetworkConnectionResult wsResult = NetworkConnection
				.retrieveResponseFromService(UrlConfig.BJ_FRIENDADD_URL,
						NetworkConnection.METHOD_GET, params);
		return FriendAddFactory.parseResult(wsResult.wsResponse);
	}

	//
	public static Bundle startPassVerification(Intent intent)
			throws IllegalStateException, IOException, URISyntaxException,
			RestClientException, ParserConfigurationException, JSONException {
		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		params.put(KeyBean.KEY_UID, intent.getStringExtra(KeyBean.KEY_UID));
		params.put(KeyBean.KEY_ID, intent.getStringExtra(KeyBean.KEY_ID));
		NetworkConnectionResult wsResult = NetworkConnection
				.retrieveResponseFromService(UrlConfig.BJ_PASSVERIFICATION_URL,
						NetworkConnection.METHOD_GET, params);
		return PassVerificationFactory.parseResult(wsResult.wsResponse);
	}

	//
	public static Bundle startCommunityList(Intent intent)
			throws IllegalStateException, IOException, URISyntaxException,
			RestClientException, ParserConfigurationException, JSONException {
		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		params.put(KeyBean.KEY_UID, intent.getStringExtra(KeyBean.KEY_UID));

		NetworkConnectionResult wsResult = NetworkConnection
				.retrieveResponseFromService(UrlConfig.BJ_COMMUNITYLIST_URL,
						NetworkConnection.METHOD_GET, params);
		return CommunityListFactory.parseResult(wsResult.wsResponse);
	}

	//
	public static Bundle startFriendPen(Intent intent)
			throws IllegalStateException, IOException, URISyntaxException,
			RestClientException, ParserConfigurationException, JSONException {
		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		params.put(KeyBean.KEY_UID, intent.getStringExtra(KeyBean.KEY_UID));
		params.put(KeyBean.KEY_PAGE, intent.getStringExtra(KeyBean.KEY_PAGE));
		params.put(KeyBean.KEY_TYPE, intent.getStringExtra(KeyBean.KEY_TYPE));
		params.put(KeyBean.KEY_PEN_ID,
				intent.getStringExtra(KeyBean.KEY_PEN_ID));

		NetworkConnectionResult wsResult = NetworkConnection
				.retrieveResponseFromService(UrlConfig.BJ_FRIENDPEN_URL,
						NetworkConnection.METHOD_GET, params);
		return FriendPenFactory.parseResult(wsResult.wsResponse);
	}

	//
	public static Bundle startPenAdd(Intent intent)
			throws IllegalStateException, IOException, URISyntaxException,
			RestClientException, ParserConfigurationException, JSONException {
		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		params.put(KeyBean.KEY_UID, intent.getStringExtra(KeyBean.KEY_UID));
		params.put(KeyBean.KEY_PICTURES,
				intent.getStringExtra(KeyBean.KEY_PICTURES));
		params.put(KeyBean.KEY_CONTENT,
				intent.getStringExtra(KeyBean.KEY_CONTENT));
		params.put(KeyBean.KEY_PEN_ID,
				intent.getStringExtra(KeyBean.KEY_PEN_ID));
		params.put(KeyBean.KEY_TYPE, intent.getStringExtra(KeyBean.KEY_TYPE));

		NetworkConnectionResult wsResult = NetworkConnection
				.retrieveResponseFromService(UrlConfig.BJ_PENADD_URL,
						NetworkConnection.METHOD_POST, params);
		return PenAddFactory.parseResult(wsResult.wsResponse);
	}

	//
	public static Bundle startPenComment(Intent intent)
			throws IllegalStateException, IOException, URISyntaxException,
			RestClientException, ParserConfigurationException, JSONException {
		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		params.put(KeyBean.KEY_UID, intent.getStringExtra(KeyBean.KEY_UID));
		params.put(KeyBean.KEY_ID, intent.getStringExtra(KeyBean.KEY_ID));
		params.put(KeyBean.KEY_CONTENT,
				intent.getStringExtra(KeyBean.KEY_CONTENT));
		params.put(KeyBean.KEY_FID, intent.getStringExtra(KeyBean.KEY_FID));
		NetworkConnectionResult wsResult = NetworkConnection
				.retrieveResponseFromService(UrlConfig.BJ_PENCOMMENT_URL,
						NetworkConnection.METHOD_POST, params);
		return PenCommentFactory.parseResult(wsResult.wsResponse);
	}

	// startPenPraise
	public static Bundle startPenPraise(Intent intent)
			throws IllegalStateException, IOException, URISyntaxException,
			RestClientException, ParserConfigurationException, JSONException {
		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		params.put(KeyBean.KEY_UID, intent.getStringExtra(KeyBean.KEY_UID));
		params.put(KeyBean.KEY_ID, intent.getStringExtra(KeyBean.KEY_ID));
		params.put(KeyBean.KEY_TYPE, intent.getStringExtra(KeyBean.KEY_TYPE));

		NetworkConnectionResult wsResult = NetworkConnection
				.retrieveResponseFromService(UrlConfig.BJ_PENPRAISE_URL,
						NetworkConnection.METHOD_GET, params);
		return PenPraiseFactory.parseResult(wsResult.wsResponse);
	}

	//
	public static Bundle startEditLogo(Intent intent)
			throws IllegalStateException, IOException, URISyntaxException,
			RestClientException, ParserConfigurationException, JSONException {
		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		params.put(KeyBean.KEY_UID, intent.getStringExtra(KeyBean.KEY_UID));
		params.put(KeyBean.KEY_USER_NAME,
				intent.getStringExtra(KeyBean.KEY_USER_NAME));
		params.put(KeyBean.KEY_USER_PATH,
				intent.getStringExtra(KeyBean.KEY_USER_PATH));

		NetworkConnectionResult wsResult = NetworkConnection
				.retrieveResponseFromService(UrlConfig.BJ_EDITLOGO_URL,
						NetworkConnection.METHOD_GET, params);
		return EditLogoFactory.parseResult(wsResult.wsResponse);
	}

	//
	public static Bundle startEditNumber(Intent intent)
			throws IllegalStateException, IOException, URISyntaxException,
			RestClientException, ParserConfigurationException, JSONException {
		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		params.put(KeyBean.KEY_ID, intent.getStringExtra(KeyBean.KEY_ID));
		params.put(KeyBean.KEY_USER_ID,
				intent.getStringExtra(KeyBean.KEY_USER_ID));
		params.put(KeyBean.KEY_NUMBER,
				intent.getStringExtra(KeyBean.KEY_NUMBER));

		NetworkConnectionResult wsResult = NetworkConnection
				.retrieveResponseFromService(UrlConfig.BJ_EDITNUMBER_URL,
						NetworkConnection.METHOD_GET, params);
		return EditNumberFactory.parseResult(wsResult.wsResponse);
	}

	//
	public static Bundle startShopCartDel(Intent intent)
			throws IllegalStateException, IOException, URISyntaxException,
			RestClientException, ParserConfigurationException, JSONException {
		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		params.put(KeyBean.KEY_ID, intent.getStringExtra(KeyBean.KEY_ID));
		params.put(KeyBean.KEY_UID, intent.getStringExtra(KeyBean.KEY_UID));
		NetworkConnectionResult wsResult = NetworkConnection
				.retrieveResponseFromService(UrlConfig.BJ_SHOPCARTDEL_URL,
						NetworkConnection.METHOD_GET, params);
		return ShopCartDelFactory.parseResult(wsResult.wsResponse);
	}

	//
	public static Bundle startPurchaseBuy(Intent intent)
			throws IllegalStateException, IOException, URISyntaxException,
			RestClientException, ParserConfigurationException, JSONException {
		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		params.put(KeyBean.KEY_UID, intent.getStringExtra(KeyBean.KEY_UID));
		params.put(KeyBean.KEY_GOODS_ID,
				intent.getStringExtra(KeyBean.KEY_GOODS_ID));
		params.put(KeyBean.KEY_GROUP_BUY_ID,
				intent.getStringExtra(KeyBean.KEY_GROUP_BUY_ID));
		params.put(KeyBean.KEY_GOODS_NUMBER,
				intent.getStringExtra(KeyBean.KEY_GOODS_NUMBER));
		params.put(KeyBean.KEY_CONSIGNEE,
				intent.getStringExtra(KeyBean.KEY_CONSIGNEE));
		params.put(KeyBean.KEY_ZITI_NAME,
				intent.getStringExtra(KeyBean.KEY_ZITI_NAME));
		params.put(KeyBean.KEY_ZITI_ID,
				intent.getStringExtra(KeyBean.KEY_ZITI_ID));
		params.put(KeyBean.KEY_PHONE, intent.getStringExtra(KeyBean.KEY_PHONE));
		params.put(KeyBean.KEY_SEND_TIME,
				intent.getStringExtra(KeyBean.KEY_SEND_TIME));
		NetworkConnectionResult wsResult = NetworkConnection
				.retrieveResponseFromService(UrlConfig.BJ_PURCHASEBUY_URL,
						NetworkConnection.METHOD_POST, params);
		return PurchaseBuyFactory.parseResult(wsResult.wsResponse);
	}

	//
	public static Bundle startGetTn(Intent intent)
			throws IllegalStateException, IOException, URISyntaxException,
			RestClientException, ParserConfigurationException, JSONException {
		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		params.put(KeyBean.KEY_MONEY, intent.getStringExtra(KeyBean.KEY_MONEY));
		params.put(KeyBean.KEY_ORDER_SN,
				intent.getStringExtra(KeyBean.KEY_ORDER_SN));

		NetworkConnectionResult wsResult = NetworkConnection
				.retrieveResponseFromService(UrlConfig.BJ_GETTN_URL,
						NetworkConnection.METHOD_GET, params);
		return GetTnFactory.parseResult(wsResult.wsResponse);
	}

	//
	public static Bundle startAlipayAffirm(Intent intent)
			throws IllegalStateException, IOException, URISyntaxException,
			RestClientException, ParserConfigurationException, JSONException {
		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		// add the money that you paid.
		//params.put(KeyBean.KEY_MONEY, value);
		params.put(KeyBean.KEY_UID, intent.getStringExtra(KeyBean.KEY_UID));
		params.put(KeyBean.KEY_ORDER_SN,
				intent.getStringExtra(KeyBean.KEY_ORDER_SN));
//TODO change it.
//		NetworkConnectionResult wsResult = NetworkConnection
//				.retrieveResponseFromService(UrlConfig.BJ_GETTN_URL,
//						NetworkConnection.METHOD_GET, params); // original
		
		NetworkConnectionResult wsResult = NetworkConnection
				.retrieveResponseFromService(UrlConfig.BJ_ALIPAYAFFIRM_URL,
						NetworkConnection.METHOD_GET, params);
		return AlipayAffirmFactory.parseResult(wsResult.wsResponse);
	}

	public static Bundle startSubmitOrder(Intent intent)
			throws IllegalStateException, IOException, URISyntaxException,
			RestClientException, ParserConfigurationException, JSONException {
		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		params.put(KeyBean.KEY_UID, intent.getStringExtra(KeyBean.KEY_UID));
		params.put(KeyBean.KEY_CONSIGNEE,
				intent.getStringExtra(KeyBean.KEY_CONSIGNEE));
		params.put(KeyBean.KEY_ZITI_NAME,
				intent.getStringExtra(KeyBean.KEY_ZITI_NAME));
		params.put(KeyBean.KEY_ZITI_ID,
				intent.getStringExtra(KeyBean.KEY_ZITI_ID));
		params.put(KeyBean.KEY_PHONE, intent.getStringExtra(KeyBean.KEY_PHONE));
		params.put(KeyBean.KEY_SEND_TIME,
				intent.getStringExtra(KeyBean.KEY_SEND_TIME));
		params.put(KeyBean.KEY_TYPE, intent.getStringExtra(KeyBean.KEY_TYPE));
		NetworkConnectionResult wsResult = NetworkConnection
				.retrieveResponseFromService(UrlConfig.BJ_SUBMITORDER_URL,
						NetworkConnection.METHOD_POST, params);
		return SubmitOrderFactory.parseResult(wsResult.wsResponse);
	}

	// /
	public static Bundle startVerifyGood(Intent intent)
			throws IllegalStateException, IOException, URISyntaxException,
			RestClientException, ParserConfigurationException, JSONException {
		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		params.put(KeyBean.KEY_UID, intent.getStringExtra(KeyBean.KEY_UID));
		params.put(KeyBean.KEY_ORDER_ID,
				intent.getStringExtra(KeyBean.KEY_ORDER_ID));
		params.put(KeyBean.KEY_DISTRIBUTION_ID,
				intent.getStringExtra(KeyBean.KEY_DISTRIBUTION_ID));

		NetworkConnectionResult wsResult = NetworkConnection
				.retrieveResponseFromService(UrlConfig.BJ_VERIFYGOOD_URL,
						NetworkConnection.METHOD_GET, params);
		return VerifyGoodFactory.parseResult(wsResult.wsResponse);
	}

	//
	public static Bundle startZitiSearch(Intent intent)
			throws IllegalStateException, IOException, URISyntaxException,
			RestClientException, ParserConfigurationException, JSONException {
		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		params.put(KeyBean.KEY_NAME, intent.getStringExtra(KeyBean.KEY_NAME));
		NetworkConnectionResult wsResult = NetworkConnection
				.retrieveResponseFromService(UrlConfig.BJ_ZITISEARCH_URL,
						NetworkConnection.METHOD_GET, params);
		return ZitiSearchFactory.parseResult(wsResult.wsResponse);
	}

	//
	public static Bundle startMyOrder(Intent intent)
			throws IllegalStateException, IOException, URISyntaxException,
			RestClientException, ParserConfigurationException, JSONException {
		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		params.put(KeyBean.KEY_UID, intent.getStringExtra(KeyBean.KEY_UID));
		NetworkConnectionResult wsResult = NetworkConnection
				.retrieveResponseFromService(UrlConfig.BJ_MYORDER_URL,
						NetworkConnection.METHOD_GET, params);
		return MyOrderFactory.parseResult(wsResult.wsResponse);
	}

	//
	public static Bundle startOrderDesc(Intent intent)
			throws IllegalStateException, IOException, URISyntaxException,
			RestClientException, ParserConfigurationException, JSONException {
		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		params.put(KeyBean.KEY_UID, intent.getStringExtra(KeyBean.KEY_UID));
		params.put(KeyBean.KEY_ORDER_ID,
				intent.getStringExtra(KeyBean.KEY_ORDER_ID));
		NetworkConnectionResult wsResult = NetworkConnection
				.retrieveResponseFromService(UrlConfig.BJ_ORDERDESC_URL,
						NetworkConnection.METHOD_GET, params);
		return OrderDescFactory.parseResult(wsResult.wsResponse);
	}

	//
	public static Bundle startBrowseList(Intent intent)
			throws IllegalStateException, IOException, URISyntaxException,
			RestClientException, ParserConfigurationException, JSONException {
		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		params.put(KeyBean.KEY_GOODS_IDS,
				intent.getStringExtra(KeyBean.KEY_GOODS_IDS));

		NetworkConnectionResult wsResult = NetworkConnection
				.retrieveResponseFromService(UrlConfig.BJ_BROWSELIST_URL,
						NetworkConnection.METHOD_GET, params);
		return BrowseListFactory.parseResult(wsResult.wsResponse);
	}

	//
	public static Bundle startHelpList(Intent intent)
			throws IllegalStateException, IOException, URISyntaxException,
			RestClientException, ParserConfigurationException, JSONException {
		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		NetworkConnectionResult wsResult = NetworkConnection
				.retrieveResponseFromService(UrlConfig.BJ_HELPLIST_URL,
						NetworkConnection.METHOD_GET, params);
		return HelpListFactory.parseResult(wsResult.wsResponse);
	}

	//
	public static Bundle startVersion(Intent intent)
			throws IllegalStateException, IOException, URISyntaxException,
			RestClientException, ParserConfigurationException, JSONException {
		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		params.put(KeyBean.KEY_VERSIONS,
				intent.getStringExtra(KeyBean.KEY_VERSIONS));

		NetworkConnectionResult wsResult = NetworkConnection
				.retrieveResponseFromService(UrlConfig.BJ_VERSION_URL,
						NetworkConnection.METHOD_GET, params);
		return VersionFactory.parseResult(wsResult.wsResponse);
	}

	// startCancelOrder
	public static Bundle startCancelOrder(Intent intent)
			throws IllegalStateException, IOException, URISyntaxException,
			RestClientException, ParserConfigurationException, JSONException {
		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		params.put(KeyBean.KEY_UID, intent.getStringExtra(KeyBean.KEY_UID));
		params.put(KeyBean.KEY_ORDER_SN,
				intent.getStringExtra(KeyBean.KEY_ORDER_SN));
		NetworkConnectionResult wsResult = NetworkConnection
				.retrieveResponseFromService(UrlConfig.BJ_CANCELORDER_URL,
						NetworkConnection.METHOD_GET, params);
		return CancelOrderFactory.parseResult(wsResult.wsResponse);
	}

	//
	public static Bundle startGetGroupPrice(Intent intent)
			throws IllegalStateException, IOException, URISyntaxException,
			RestClientException, ParserConfigurationException, JSONException {
		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		params.put(KeyBean.KEY_UID, intent.getStringExtra(KeyBean.KEY_UID));
		params.put(KeyBean.KEY_GOODS_ID,
				intent.getStringExtra(KeyBean.KEY_GOODS_ID));
		params.put(KeyBean.KEY_GROUP_BUY_ID,
				intent.getStringExtra(KeyBean.KEY_GROUP_BUY_ID));
		params.put(KeyBean.KEY_GOODS_NUMBER,
				intent.getStringExtra(KeyBean.KEY_GOODS_NUMBER));
		params.put(KeyBean.KEY_CONSIGNEE,
				intent.getStringExtra(KeyBean.KEY_CONSIGNEE));
		params.put(KeyBean.KEY_ZITI_NAME,
				intent.getStringExtra(KeyBean.KEY_ZITI_NAME));
		params.put(KeyBean.KEY_ZITI_ID,
				intent.getStringExtra(KeyBean.KEY_ZITI_ID));
		params.put(KeyBean.KEY_PHONE, intent.getStringExtra(KeyBean.KEY_PHONE));
		params.put(KeyBean.KEY_SEND_TIME,
				intent.getStringExtra(KeyBean.KEY_SEND_TIME));
		NetworkConnectionResult wsResult = NetworkConnection
				.retrieveResponseFromService(UrlConfig.BJ_GETGROUPPRICE_URL,
						NetworkConnection.METHOD_POST, params);
		return GetGroupPriceFactory.parseResult(wsResult.wsResponse);
	}

	// startAppPersonCityList
	public static Bundle startAppPersonCommunityList(Intent intent)
			throws IllegalStateException, IOException, URISyntaxException,
			RestClientException, ParserConfigurationException, JSONException {
		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		params.put(KeyBean.KEY_UID, intent.getStringExtra(KeyBean.KEY_UID));

		NetworkConnectionResult wsResult = NetworkConnection
				.retrieveResponseFromService(
						UrlConfig.BJ_APPPERSON_COMMUNITYLIST_URL,
						NetworkConnection.METHOD_GET, params);
		return AppPersonCommunityListFactory.parseResult(wsResult.wsResponse);
	}

	public static Bundle startAppPersonCityList(Intent intent)
			throws IllegalStateException, IOException, URISyntaxException,
			RestClientException, ParserConfigurationException, JSONException {
		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		params.put(KeyBean.KEY_UID, intent.getStringExtra(KeyBean.KEY_UID));
		NetworkConnectionResult wsResult = NetworkConnection
				.retrieveResponseFromService(
						UrlConfig.BJ_APPPERSON_CITYLIST_URL,
						NetworkConnection.METHOD_GET, params);
		return AppPersonCityListFactory.parseResult(wsResult.wsResponse);
	}

	public static Bundle startDeleteCircle(Intent intent)
			throws IllegalStateException, IOException, URISyntaxException,
			RestClientException, ParserConfigurationException, JSONException {
		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		params.put(KeyBean.KEY_UID, intent.getStringExtra(KeyBean.KEY_UID));
		params.put(KeyBean.KEY_ID, intent.getStringExtra(KeyBean.KEY_ID));
		NetworkConnectionResult wsResult = NetworkConnection
				.retrieveResponseFromService(UrlConfig.BJ_DELCIRCLE_URL,
						NetworkConnection.METHOD_GET, params);
		return DeleteCircleFactory.parseResult(wsResult.wsResponse);
	}

	public static Bundle startFriendDel(Intent intent)
			throws IllegalStateException, IOException, URISyntaxException,
			RestClientException, ParserConfigurationException, JSONException {
		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		params.put(KeyBean.KEY_UID, intent.getStringExtra(KeyBean.KEY_UID));
		params.put(KeyBean.KEY_FID, intent.getStringExtra(KeyBean.KEY_FID));
		NetworkConnectionResult wsResult = NetworkConnection
				.retrieveResponseFromService(UrlConfig.BJ_FRIENDDEL_URL,
						NetworkConnection.METHOD_GET, params);
		return DeleteCircleFactory.parseResult(wsResult.wsResponse);
	}

}
