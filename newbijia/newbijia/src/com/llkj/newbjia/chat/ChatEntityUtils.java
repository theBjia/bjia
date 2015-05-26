package com.llkj.newbjia.chat;

import java.io.File;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import com.llkj.db.GroupChatMessages;
import com.llkj.db.PrivateChatMessagesEntity;
import com.llkj.db.RecentlyContacts;
import com.llkj.newbjia.bean.UserInfoBean;
import com.llkj.newbjia.utils.File2Code;

public class ChatEntityUtils {
	
	// 获取私聊消息工具类
	public static PrivateChatMessagesEntity getPrivateChatEntity(
			JSONObject jsonObject, Context context) {
		
		PrivateChatMessagesEntity entity = new PrivateChatMessagesEntity();
		
		entity.setUserId(UserInfoBean.getUserInfo(context).getUid());

		try {
			if (jsonObject.has("user_id")) {
				
				String user_id = jsonObject.getString("user_id");
				
				entity.setSendedId(user_id);
			}
			if (jsonObject.has("esq_id")) {
				String esq_id = jsonObject.getString("esq_id");
				entity.setSendedId(esq_id);
			}

			if (jsonObject.has("message_type")) {
				String message_type = jsonObject.getString("message_type");
				entity.setMessageType(message_type);
			}
			if (jsonObject.has("content")) {
				String content = jsonObject.getString("content");
				String path = null;
				if (entity.getMessageType().equals(Constants.PICTURE)) {
					try {
						// Bitmap bt = ComplexOperation
						// .decodeBase64Img(content);
						// BitmapUtil.saveMyBitmap(bt, getPicPath());
						path = getPicPath();

						File2Code.decoderBase64File(content, path);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Log.e("异常", e.toString());
					}
					entity.setMessageContent(path);
				} else {
					entity.setMessageContent(content);
				}
			}

			if (jsonObject.has("target_name")) {
				String target_name = jsonObject.getString("target_name");
				entity.setReceiveName(target_name);
			}
			if (jsonObject.has("target_id")) {
				String target_id = jsonObject.getString("target_id");
				entity.setReceiveId(target_id);
			}
			if (jsonObject.has("user_name")) {
				String user_name = jsonObject.getString("user_name");
				entity.setSendedName(user_name);
			}
			if (jsonObject.has("esq_name")) {
				String esq_name = jsonObject.getString("esq_name");
				entity.setSendedName(esq_name);
			}
			if (jsonObject.has("user_logo")) {
				String user_logo = jsonObject.getString("user_logo");
				entity.setSendedLogo(user_logo);
			}

			if (jsonObject.has("voice_length")) {
				String voice_length = jsonObject.getString("voice_length");
				entity.setVoicelength(voice_length);
			}
			if (jsonObject.has("message_date")) {
				String message_date = jsonObject.getString("message_date");
				entity.setMessageDate(message_date);
			}
			if (jsonObject.has("owner_id")) {
				String owner_id = jsonObject.getString("owner_id");

			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return entity;

	}

	public static GroupChatMessages getGroupChatMessagesEntity(
			JSONObject jsonObject, Context context) {
		GroupChatMessages entity = new GroupChatMessages();
		entity.setUserId(UserInfoBean.getUserInfo(context).getUid());

		try {
			if (jsonObject.has("user_id")) {
				String user_id = jsonObject.getString("user_id");
				entity.setSendedId(user_id);
			}
			if (jsonObject.has("message_type")) {
				String message_type = jsonObject.getString("message_type");
				entity.setMessageType(message_type);
			}
			if (jsonObject.has("content")) {
				String content = jsonObject.getString("content");
				String path = null;
				if (entity.getMessageType().equals(Constants.PICTURE)) {
					try {
						// Bitmap bt = ComplexOperation
						// .decodeBase64Img(content);
						// BitmapUtil.saveMyBitmap(bt, getPicPath());
						path = getPicPath();

						File2Code.decoderBase64File(content, path);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Log.e("异常", e.toString());
					}
					entity.setMessageContent(path);
				} else {
					entity.setMessageContent(content);
				}
			}

			if (jsonObject.has("target_name")) {
				String target_name = jsonObject.getString("target_name");
				entity.setGroupName(target_name);
			}
			if (jsonObject.has("target_id")) {
				String target_id = jsonObject.getString("target_id");
				entity.setGroupId(target_id);
			}
			if (jsonObject.has("user_name")) {
				String user_name = jsonObject.getString("user_name");
				entity.setSendedName(user_name);
			}
			if (jsonObject.has("user_logo")) {
				String user_logo = jsonObject.getString("user_logo");
				entity.setSendedLogo(user_logo);
			}

			if (jsonObject.has("voice_length")) {
				String voice_length = jsonObject.getString("voice_length");
				entity.setVoicelength(voice_length);
			}
			if (jsonObject.has("message_date")) {
				String message_date = jsonObject.getString("message_date");
				entity.setMessageDate(message_date);
			}
			if (jsonObject.has("owner_id")) {
				String owner_id = jsonObject.getString("owner_id");

			}
			if (jsonObject.has("dataId")) {
				String dataId = jsonObject.getString("dataId");
				entity.setCmid(dataId);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return entity;

	}

	private static String getPicPath() {
		File file = new File(Environment.getExternalStorageDirectory(),
				System.currentTimeMillis() + ".jpg");

		return file.getAbsolutePath();
	}

	public static RecentlyContacts c2r(PrivateChatMessagesEntity pEntity,
			Context context) {
		RecentlyContacts rc = new RecentlyContacts();
		rc.setMessageChatType(pEntity.getActionType());
		rc.setMessageContent(pEntity.getMessageContent());
		rc.setMessageDate(pEntity.getMessageDate());
		rc.setMessageType(pEntity.getMessageType());
		if (Constants.ONREPLY.equals(pEntity.getActionType())) {
			if ("1".equals(pEntity.getBubbleType())) {
				rc.setReceiveId(pEntity.getReceiveId());
				rc.setReceiveLogo(pEntity.getSendedLogo());
				rc.setReceiveName(pEntity.getSendedName());
			} else {
				rc.setReceiveId(pEntity.getSendedId());
				rc.setReceiveLogo(pEntity.getReceiveLogo());
				rc.setReceiveName(pEntity.getReceiveName());
			}
		} else {
			if ("1".equals(pEntity.getBubbleType())) {
				rc.setReceiveId(pEntity.getSendedId());
				rc.setReceiveLogo(pEntity.getSendedLogo());
				rc.setReceiveName(pEntity.getSendedName());
			} else {
				rc.setReceiveId(pEntity.getReceiveId());
				rc.setReceiveLogo(pEntity.getReceiveLogo());
				rc.setReceiveName(pEntity.getReceiveName());
			}
		}

		rc.setUnReadNumber(0 + "");
		rc.setUserId(UserInfoBean.getUserInfo(context).getUid());
		return rc;

	}

	public static RecentlyContacts c2r(GroupChatMessages pEntity,
			Context context) {
		RecentlyContacts rc = new RecentlyContacts();
		rc.setMessageChatType(pEntity.getActionType());
		rc.setMessageContent(pEntity.getMessageContent());
		rc.setMessageDate(pEntity.getMessageDate());
		rc.setMessageType(pEntity.getMessageType());
		rc.setReceiveId(pEntity.getGroupId());
		rc.setReceiveLogo(pEntity.getGroupLogo());
		rc.setReceiveName(pEntity.getGroupName());
		rc.setUnReadNumber(0 + "");
		rc.setUserId(UserInfoBean.getUserInfo(context).getUid());
		return rc;

	}

	public static GroupChatMessages getTransmitOne(JSONObject jsonObject,
			Context context) {
		GroupChatMessages entity = new GroupChatMessages();
		entity.setUserId(UserInfoBean.getUserInfo(context).getUid());
		entity.setMessageType(Constants.TEXT);
		entity.setBubbleType("2");

		try {
			if (jsonObject.has("user_id")) {
				String user_id = jsonObject.getString("user_id");
				entity.setSendedId(user_id);
			}

			if (jsonObject.has("group_id")) {
				String group_id = jsonObject.getString("group_id");
				entity.setGroupId(group_id);
			}

			if (jsonObject.has("group_name")) {
				String group_name = jsonObject.getString("group_name");
				entity.setGroupName(group_name);
			}
			if (jsonObject.has("message_date")) {
				String message_date = jsonObject.getString("message_date");
				entity.setMessageDate(message_date);
			}
			if (jsonObject.has("creator_name")) {
				String creator_name = jsonObject.getString("creator_name");
				entity.setCreatorName(creator_name);
			}
			if (jsonObject.has("creator")) {
				String creator = jsonObject.getString("creator");
				entity.setCreatorId(creator);
			}

			if (jsonObject.has("target_id")) {
				String target_id = jsonObject.getString("target_id");
				entity.setOtherId(target_id);
			}

			if (jsonObject.has("user_name")) {
				String user_name = jsonObject.getString("user_name");
				entity.setSendedName(user_name);
			}
			if (jsonObject.has("group_users")) {
				String group_users = jsonObject.getString("group_users");

			}

			if (jsonObject.has("type")) {
				String type = jsonObject.getString("type");
				String content = "";
				if (jsonObject.has("to_users")) {
					String to_users = jsonObject.getString("to_users");
					JSONObject json = new JSONObject(to_users);
					JSONArray jarray = json.getJSONArray("user");
					StringBuffer sbb = new StringBuffer();
					for (int i = 0; i < jarray.length(); i++) {
						sbb.append(jarray.getString(i) + ",");
					}
					sbb.substring(0, sbb.length() - 1);
					if (Constants.CHAT_TYPE_CREATE.equals(type)) {
						content = entity.getCreatorName() + "创建了群";
						entity.setOtherType(Constants.CHAT_TYPE_CREATE);
					} else if (Constants.CHAT_TYPE_ADD.equals(type)) {
						content = entity.getSendedName() + "邀请了"
								+ sbb.toString() + "加入群聊";
						entity.setOtherType(Constants.CHAT_TYPE_ADD);
					}

					entity.setMessageContent(content);

				}

			}
			if (jsonObject.has("dataId")) {
				String dataId = jsonObject.getString("dataId");
				entity.setCmid(dataId);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return entity;

	}

	public static GroupChatMessages gettransmitAll(JSONObject jsonObject,
			Context context) {
		GroupChatMessages entity = new GroupChatMessages();
		entity.setUserId(UserInfoBean.getUserInfo(context).getUid());
		entity.setMessageType(Constants.TEXT);
		entity.setBubbleType("2");

		try {
			if (jsonObject.has("user_id")) {
				String user_id = jsonObject.getString("user_id");
				entity.setSendedId(user_id);
			}

			if (jsonObject.has("group_id")) {
				String group_id = jsonObject.getString("group_id");
				entity.setGroupId(group_id);
			}

			if (jsonObject.has("group_name")) {
				String group_name = jsonObject.getString("group_name");
				entity.setGroupName(group_name);
			}
			if (jsonObject.has("message_date")) {
				String message_date = jsonObject.getString("message_date");
				entity.setMessageDate(message_date);
			}
			if (jsonObject.has("creator_name")) {
				String creator_name = jsonObject.getString("creator_name");
				entity.setCreatorName(creator_name);
			}
			if (jsonObject.has("creator")) {
				String creator = jsonObject.getString("creator");
				entity.setCreatorId(creator);
			}

			if (jsonObject.has("target_id")) {
				String target_id = jsonObject.getString("target_id");
				entity.setOtherId(target_id);
			}

			if (jsonObject.has("user_name")) {
				String user_name = jsonObject.getString("user_name");
				entity.setSendedName(user_name);
			}
			if (jsonObject.has("group_users")) {
				String group_users = jsonObject.getString("group_users");

			}

			if (jsonObject.has("type")) {
				String type = jsonObject.getString("type");
				String content = "";
				if (jsonObject.has("to_users")) {
					String to_users = jsonObject.getString("to_users");
					JSONObject json = new JSONObject(to_users);
					JSONArray jarray = json.getJSONArray("user");
					StringBuffer sbb = new StringBuffer();
					for (int i = 0; i < jarray.length(); i++) {
						sbb.append(jarray.getString(i) + ",");
					}
					sbb.substring(0, sbb.length() - 1);
					if ("exit".equals(type)) {
						content = entity.getSendedName() + "退出了群";
						entity.setOtherType(Constants.CHAT_TYPE_EXIT);
					} else if ("delete".equals(type)) {
						content = entity.getCreatorName() + "删除了群";
						entity.setOtherType(Constants.CHAT_TYPE_DELETE);
					} else if ("edit".equals(type)) {
						content = entity.getSendedName() + "编辑了群名";
						entity.setOtherType(Constants.CHAT_TYPE_EDIT);
					}

					entity.setMessageContent(content);

				}

			}
			if (jsonObject.has("dataId")) {
				String dataId = jsonObject.getString("dataId");
				entity.setCmid(dataId);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return entity;

	}

}
