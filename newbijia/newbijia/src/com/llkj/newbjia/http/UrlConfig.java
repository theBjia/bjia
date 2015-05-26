package com.llkj.newbjia.http;

/**
 * Url 配置类,包含所有的接口访问地址
 * 
 * @author zhang.zk
 * 
 */
public class UrlConfig {
	/************************************************************************************
	 * =====================ROOT URL====================
	 * 
	 *************************************************************************************/
	// www.bjia.co baifahui.bloveambition.com
	
	//public static final String ROOT_URL = "http://192.168.1.136:80/api/";
	public static final String ROOT_URL = "http://www.bjia.co/api/";
	
	// public static final String ROOT_URL =
	// "http://baifahui.bloveambition.com/api/";
	public static final String ROOT_URL_TWO = "http://www.bjia.co/";
	/**
	 * 据说是版本号,但是后台的人说没有版本号,总之,就这样吧.
	 */

	public static final String BJ_UPLOAD_URL = ROOT_URL
			+ "?r=default/publish/setUpload";
	// 获取验证码
	public static final String BJ_GETCODE_URL = ROOT_URL
			+ "appLogin.php?action=getCode";
	// 注册
	public static final String BJ_REGISTER_URL = ROOT_URL
			+ "appLogin.php?action=register";
	// 登录
	public static final String BJ_LOGIN_URL = ROOT_URL
			+ "appLogin.php?action=login";
	// 找回密码
	public static final String BJ_FORGETPASSWORD_URL = ROOT_URL
			+ "appLogin.php?action=forgetPassword";
	// 个人资料
	public static final String BJ_PERSONDESC_URL = ROOT_URL
			+ "appPerson.php?action=personDesc";
	// 我的彼佳币
	public static final String BJ_MYCOIN_URL = ROOT_URL
			+ "appPerson.php?action=myCoin";
	// 修改个性签名
	public static final String BJ_SIGNEDIT_URL = ROOT_URL
			+ "appPerson.php?action=signEdit";
	// 修改用户名
	public static final String BJ_NAMEEDIT_URL = ROOT_URL
			+ "appPerson.php?action=nameEdit";
	// 我的积分
	public static final String BJ_MYINTEGRAL_URL = ROOT_URL
			+ "appPerson.php?action=myIntegral";
	// 优惠券
	public static final String BJ_MYPRIVILEGE_URL = ROOT_URL
			+ "appPerson.php?action=myPrivilege";
	// 收藏夹
	public static final String BJ_COLLECTLIST_URL = ROOT_URL
			+ "appPerson.php?action=collectList";
	// 意见反馈
	public static final String BJ_ADVISE_URL = ROOT_URL
			+ "appPerson.php?action=advise";
	// 修改密码
	public static final String BJ_PASSWORDEDIT_URL = ROOT_URL
			+ "appPerson.php?action=passwordEdit";

	// 购物车
	public static final String BJ_SHOPCARTLIST_URL = ROOT_URL
			+ "appShop.php?action=shopCartList";
	// 收货地址
	public static final String BJ_SHOPADDRESSLIST_URL = ROOT_URL
			+ "appShop.php?action=addressList";
	// 编辑收货地址
	public static final String BJ_ADDRESSEDIT_URL = ROOT_URL
			+ "appShop.php?action=addressEdit";
	// 添加收货地址
	public static final String BJ_ADDRESSADD_URL = ROOT_URL
			+ "appShop.php?action=addressAdd";
	// 判断用户是否注册
	public static final String BJ_ISUSERURL_URL = ROOT_URL
			+ "appLogin.php?action=isUser";
	// 1.7根据用户手机号获取用户头像
	public static final String BJ_GETLOGO_URL = ROOT_URL
			+ "appIndex.php?action=getLogo";
	// 2.1首页
	public static final String BJ_INDEX_URL = ROOT_URL
			+ "appIndex.php?action=index";
	// 2.3商品详情
	public static final String BJ_GOODDESC_URL = ROOT_URL
			+ "appIndex.php?action=goodDesc";
	// 2.8附近自提点
	public static final String BJ_ZITIDISTRIBUTION_URL = ROOT_URL
			+ "appIndex.php?action=zitiDistribution";
	// 2.9团购详情
	public static final String BJ_PURCHASEDESC_URL = ROOT_URL
			+ "appIndex.php?action=purchaseDesc";
	// 2.11关注商品goodAttention
	public static final String BJ_GOODATTENTION_URL = ROOT_URL
			+ "appIndex.php?action=goodAttention";
	// 2.12加入购物车接口
	public static final String BJ_SHOPCARTADD_URL = ROOT_URL
			+ "appShop.php?action=shopCartAdd";
	// 2.7分类列表
	public static final String BJ_CLASSLIST_URL = ROOT_URL
			+ "appIndex.php?action=classList";
	// 2.2商品列表
	public static final String BJ_GOODLIST_URL = ROOT_URL
			+ "appIndex.php?action=goodList";
	// 全部自提点与自提点分布
	public static final String BJ_ZITILIST_URL = ROOT_URL
			+ "appIndex.php?action=zitiList";
	// 2.15搜索商品 searchGoods
	public static final String BJ_SEARCHGOODS_URL = ROOT_URL
			+ "appIndex.php?action=searchGoods";
	// judgeAvailable配送自提点是否有货
	public static final String BJ_JUDGEAVAILABLE_URL = ROOT_URL
			+ "appIndex.php?action=judgeAvailable";
	// 5.3 获取默认收货地址
	public static final String BJ_DEFAULTADDRESS_URL = ROOT_URL
			+ "appShop.php?action=defaultAddress";
	// friendsList 3.1好友列表
	public static final String BJ_FRIENDSLIST_URL = ROOT_URL
			+ "appFriend.php?action=friendsList";
	// 3.2新的好友
	public static final String BJ_NEWFRIEND_URL = ROOT_URL
			+ "appFriend.php?action=newFriend";
	// 3.3搜索好友
	public static final String BJ_FRIENDSEARCH_URL = ROOT_URL
			+ "appFriend.php?action=friendSearch";
	// 3.4通讯录好友
	public static final String BJ_ADDRESSLIST_URL = ROOT_URL
			+ "appFriend.php?action=addressList";
	// 3.5社区圈好友
	public static final String BJ_COMMUNITYFRIEND_URL = ROOT_URL
			+ "appFriend.php?action=communityFriend";
	// 3.6好友资料
	public static final String BJ_FRIENDDESC_URL = ROOT_URL
			+ "appFriend.php?action=friendDesc";
	// 3.7加为好友
	public static final String BJ_FRIENDADD_URL = ROOT_URL
			+ "appFriend.php?action=friendAdd";
	// 3.9通过验证
	public static final String BJ_PASSVERIFICATION_URL = ROOT_URL
			+ "appFriend.php?action=passVerification";
	// 4.2加入的圈子列表
	public static final String BJ_COMMUNITYLIST_URL = ROOT_URL
			+ "appPen.php?action=communityList";
	// 4.1好友圈
	public static final String BJ_FRIENDPEN_URL = ROOT_URL
			+ "appPen.php?action=friendPen";
	// 4.3上传图片
	public static final String BJ_UPLOADPIC_URL = "http://www.bjia.co/api/appPen.php?action=uploadPic";
	// 4.4发布圈子
	public static final String BJ_PENADD_URL = ROOT_URL
			+ "appPen.php?action=penAdd";
	// 4.5评论
	public static final String BJ_PENCOMMENT_URL = ROOT_URL
			+ "appPen.php?action=penComment";
	// 4.6点赞或取消赞
	public static final String BJ_PENPRAISE_URL = ROOT_URL
			+ "appPen.php?action=penPraise";
	// 6.21 上传头像接口
	public static final String BJ_UPLOADLOGO_URL = "http://www.bjia.co/api/appPerson.php?action=uploadLogo";
	// 3.8注册成功后调用
	public static final String BJ_REGISTERSUCCEED_URL = ROOT_URL
			+ "appFriend.php?action=registerSucceed";
	// 6.20 编辑用户头像接口
	public static final String BJ_EDITLOGO_URL = ROOT_URL
			+ "appPerson.php?action=editLogo";
	// 5.10 修改购物车商品数量接口
	public static final String BJ_EDITNUMBER_URL = ROOT_URL
			+ "appShop.php?action=editNumber";
	// 2.13 删除购物车
	public static final String BJ_SHOPCARTDEL_URL = ROOT_URL
			+ "appShop.php?action=shopCartDel";
	// purchaseBuy 2.14团购购买
	public static final String BJ_PURCHASEBUY_URL = ROOT_URL
			+ "appShop.php?action=purchaseBuy";
	// 5.11 银联支付获取Tn接口
	public static final String BJ_GETTN_URL = ROOT_URL
			+ "appShop.php?action=getTn";
	// 支付成功后调用
	public static final String BJ_ALIPAYAFFIRM_URL = ROOT_URL
			+ "appShop.php?action=alipayAffirm";
	// submitOrder5.8提交订单
	public static final String BJ_SUBMITORDER_URL = ROOT_URL
			+ "appIndex.php?action=submitOrder";
	// 6.16 确认收货
	public static final String BJ_VERIFYGOOD_URL = ROOT_URL
			+ "appPerson.php?action=verifyGood";
	// zitiSearch 2.10搜索自提点
	public static final String BJ_ZITISEARCH_URL = ROOT_URL
			+ "appIndex.php?action=zitiSearch";
	// myOrder6.4 我的订单列表
	public static final String BJ_MYORDER_URL = ROOT_URL
			+ "appPerson.php?action=myOrder";
	// 6.5 我的订单详情
	public static final String BJ_ORDERDESC_URL = ROOT_URL
			+ "appPerson.php?action=orderDesc";
	// browseList6.10 浏览记录列表
	public static final String BJ_BROWSELIST_URL = ROOT_URL
			+ "appPerson.php?action=browseList";
	// 6.12帮助列表
	public static final String BJ_HELPLIST_URL = ROOT_URL
			+ "appPerson.php?action=helpList";
	// 6.20版本更新
	public static final String BJ_VERSION_URL = ROOT_URL
			+ "appPerson.php?action=version";
	// 5.13 取消订单接口
	public static final String BJ_CANCELORDER_URL = ROOT_URL
			+ "appShop.php?action=cancelOrder";
	// purchaseBuy 2.16 获取团购商品价格
	public static final String BJ_GETGROUPPRICE_URL = ROOT_URL
			+ "appShop.php?action=getGroupPrice";
	// communityList6.2社区圈
	public static final String BJ_APPPERSON_COMMUNITYLIST_URL = ROOT_URL
			+ "appPerson.php?action=communityList";
	// 6.3城市圈
	public static final String BJ_APPPERSON_CITYLIST_URL = ROOT_URL
			+ "appPerson.php?action=cityList";
	// 4.7 删除圈子内发布的内容接口
	public static final String BJ_DELCIRCLE_URL = ROOT_URL
			+ "appPen.php?action=delCircle";
	// 3.10 删除好友
	public static final String BJ_FRIENDDEL_URL = ROOT_URL
			+ "appFriend.php?action=friendDel";
}
