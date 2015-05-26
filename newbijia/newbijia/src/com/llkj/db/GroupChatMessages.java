package com.llkj.db;



/**
 * 该类为消息实体类
 * 属性已经很全了
 * */
import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class GroupChatMessages implements Serializable {
	private String userId;// 用户uid本地
	private String groupId;// 群id
	private String groupName;// 群名字
	private String groupLogo;// 群头像（base64
	private String sendedId;// 发送者id
	private String sendedName;// 发送者名字
	private String sendedLogo;// 发送者头像（base64）
	private String messageContent;// 消息内容（文本、图片、声音、通知）
	private String messageDate;// 消息时间
	private String messageType;// 消息类型（文本、图片、声音、通知）
	private String bubbleType;// 气泡类型（发送、接收）
	private String voicelength;// 声音长度
	private String isRead;// 消息是否已读（用于声音未读提示）

	private String actionType;// 事件类型
	private String cmid;// 消息id 自增长
	private String otherId;
	// 如果是群
	private String creatorId;
	private String creatorName;
	private String otherType;
	private String path;

	public GroupChatMessages() {
		super();
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getGroupLogo() {
		return groupLogo;
	}

	public void setGroupLogo(String groupLogo) {
		this.groupLogo = groupLogo;
	}

	public String getSendedId() {
		return sendedId;
	}

	public void setSendedId(String sendedId) {
		this.sendedId = sendedId;
	}

	public String getSendedName() {
		return sendedName;
	}

	public void setSendedName(String sendedName) {
		this.sendedName = sendedName;
	}

	public String getSendedLogo() {
		return sendedLogo;
	}

	public void setSendedLogo(String sendedLogo) {
		this.sendedLogo = sendedLogo;
	}

	public String getMessageContent() {
		return messageContent;
	}

	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}

	public String getMessageDate() {
		return messageDate;
	}

	public void setMessageDate(String messageDate) {
		this.messageDate = messageDate;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public String getBubbleType() {
		return bubbleType;
	}

	public void setBubbleType(String bubbleType) {
		this.bubbleType = bubbleType;
	}

	public String getVoicelength() {
		return voicelength;
	}

	public void setVoicelength(String voicelength) {
		this.voicelength = voicelength;
	}

	public String getIsRead() {
		return isRead;
	}

	public void setIsRead(String isRead) {
		this.isRead = isRead;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public String getCmid() {
		return cmid;
	}

	public void setCmid(String cmid) {
		this.cmid = cmid;
	}

	public String getOtherId() {
		return otherId;
	}

	public void setOtherId(String otherId) {
		this.otherId = otherId;
	}

	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public String getOtherType() {
		return otherType;
	}

	public void setOtherType(String otherType) {
		this.otherType = otherType;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public GroupChatMessages(String userId, String groupId, String groupName,
			String groupLogo, String sendedId, String sendedName,
			String sendedLogo, String messageContent, String messageDate,
			String messageType, String bubbleType, String voicelength,
			String isRead, String actionType, String cmid, String otherId,
			String creatorId, String creatorName, String otherType, String path) {
		super();
		this.userId = userId;
		this.groupId = groupId;
		this.groupName = groupName;
		this.groupLogo = groupLogo;
		this.sendedId = sendedId;
		this.sendedName = sendedName;
		this.sendedLogo = sendedLogo;
		this.messageContent = messageContent;
		this.messageDate = messageDate;
		this.messageType = messageType;
		this.bubbleType = bubbleType;
		this.voicelength = voicelength;
		this.isRead = isRead;
		this.actionType = actionType;
		this.cmid = cmid;
		this.otherId = otherId;
		this.creatorId = creatorId;
		this.creatorName = creatorName;
		this.otherType = otherType;
		this.path = path;
	}

	@Override
	public String toString() {
		return "GroupChatMessages [userId=" + userId + ", groupId=" + groupId
				+ ", groupName=" + groupName + ", groupLogo=" + groupLogo
				+ ", sendedId=" + sendedId + ", sendedName=" + sendedName
				+ ", sendedLogo=" + sendedLogo + ", messageContent="
				+ messageContent + ", messageDate=" + messageDate
				+ ", messageType=" + messageType + ", bubbleType=" + bubbleType
				+ ", voicelength=" + voicelength + ", isRead=" + isRead
				+ ", actionType=" + actionType + ", cmid=" + cmid
				+ ", otherId=" + otherId + ", creatorId=" + creatorId
				+ ", creatorName=" + creatorName + ", otherType=" + otherType
				+ ", path=" + path + "]";
	}

	public JSONObject toJSONObject() throws JSONException {

		JSONObject jo = new JSONObject();
		jo.put("user_id", this.getSendedId());
		jo.put("content", this.getMessageContent());
		jo.put("target_id", this.getGroupId());
		jo.put("user_name", this.getSendedName());
		jo.put("user_logo", this.getSendedLogo());
		jo.put("message_type", this.getMessageType());
		jo.put("voice_length", this.getVoicelength());
		// 根据消息类型的不同，发送的参数不一样
		return jo;
	}

}
