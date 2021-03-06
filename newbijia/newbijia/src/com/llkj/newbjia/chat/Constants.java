package com.llkj.newbjia.chat;

/** 常量类，聊天用到的常量都放在这里 */
public class Constants {
	//pinpin 115.159.6.46:8881
	//bijia 115.159.29.218 8777
	public static final String SERVER_IP = "219.148.38.135";// 服务器ip
	public static final int SERVER_PORT = 8777;// 服务器端口
	public static final int REGISTER_FAIL = 0;// 注册失败
	public static final String ACTION = "com.im.message";// 消息广播action
	// 已改
	public static final String MSGKEY = "chat";// 消息的key
	public static final String IP_PORT = "ipPort";// 保存ip、port的xml文件名
	public static final String SAVE_USER = "saveUser";// 保存用户信息的xml文件名
	public static final String BACKKEY_ACTION = "com.im.backKey";// 返回键发送广播的action
	public static final int NOTIFY_ID = 0x911;// 通知ID
	public static final String DBNAME = "dd.db";// 数据库名称
	public static final int REQUEST_SUCCESS = 1;
	// =========
	public static final String VOICE = "Voice";
	public static final String PICTURE = "Picture";
	public static final String TEXT = "Text";
	public static final String LOCATIONPOSITION = "LocalPosition";
												   

	// 私聊
	public static final String OTHERCHAT = "transmitChat";
	// 群聊
	public static final String OTHERGROUP = "transmitGroup";

	// 1、获取离线数据
	public static final String OFFLINEMESSAGE = "offlineMessage";

	// 、获取事件的离线数据
	public static final String EVENTOFFLINEMESSAGE = "eventOfflineMessage";
	// 接收私聊数据

	public static final String CHECKUSER = "checkUser";
	// 5、获取创建群/添加群用户的推送事件通知
	public static final String TRANSMITONE = "transmitOne";
	// 群中推送某人退群或修改群名事件
	public static final String TRANSMITALL = "transmitAll";
	// 删除离线数据事件
	public static final String REMOVEOFFLINE = "removeOffline";
	//
	public static final String ADDME = "addme";
	// 发送私聊信息
	public static final String SENDCHAT = "onChat";
	//
	public static final String SENDGROUP = "sendGroup";
	//
	public static final String PUSHONE = "pushOne";
	//
	public static final String PUSHALL = "pushAll";
	//
	public static final String GETOFFLINE = "getOffline";

	public static final String GETEVENTOFFLINE = "getEventOffline";

	public static final String FROMIDTARGETID = "fromidtargetid";
	public static final String FROMID = "fromId";
	public static final String FROMNAME = "fromName";
	public static final String FROMPHOTO = "fromPhoto";
	public static final String TARGETID = "targetId";
	public static final String TAGETNAME = "tagetName";
	public static final String TAGETPHOTO = "tagetPhoto";
	public static final String DATE = "date";
	public static final String CONTENT = "content";
	public static final String CONTENTTYPE = "contentType";
	public static final String ISCOMING = "isComing";
	public static final String VOICELENGTH = "voiceLength";
	public static final String MESSAGETYPE = "messageType";
	public static final String MESSAGEID = "messageId";
	public static final String MESSAGENUM = "messageNum";
	public static final String OTHERID = "otherId";
	public static final String CREATORID = "creatorId";
	public static final String CREATORNAME = "creatorName";

	public static final String UPDATECHATLIST = "update_chat_list";

	public static final String CHAT_TYPE_CREATE = "create";
	public static final String CHAT_TYPE_ADD = "add";
	public static final String CHAT_TYPE_EXIT = "exit";
	public static final String CHAT_TYPE_DELETE = "delete";
	public static final String CHAT_TYPE_EDIT = "edit";

	public static final String SWITCHCART = "switch_cart";
	public static final String REFRESH_FRIEND = "refresh_friend";
	public static final String REFRESH_SHOPCART = "refresh_shopcart";
	public static final String REFRESH_TOUXIANG = "refresh_touxiang";
	public static final String REFRESH = "refresh";
	public static final String REFRESH_ORDER = "refresh_order";
	public static final String REFRESH_COLLECTION = "refresh_collection";
	//onChatRemove
	public static final String ONCHATREMOVE = "onChatRemove";
	//
	public static final String ONESQREMOVE = "onEsqRemove";
	//offlineMessage
	public static final String ONTRANSMIT = "onTransmit";
	//onReply 
	public static final String ONREPLY = "onReply";
	//
	public static final String NOTIFYSTYLE = "notifystyle";
}
