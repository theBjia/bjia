package com.llkj.db;



import java.io.Serializable;

public class NoticeContacts implements Serializable {
	private String Gid;// 群id 目标id
	private String N_newMsgRemind;// 声音提示
	private String showNikeName;// 显示昵称
	private String userId;// 本地Uid
	private String isMember;// 是否是群成员（用于是否显示群设置）
	private String backgroundPic;// 是否是群成员（用于是否显示群设置）

	public NoticeContacts() {
		super();
	}

	public String getGid() {
		return Gid;
	}

	public String getBackgroundPic() {
		return backgroundPic;
	}

	public void setBackgroundPic(String backgroundPic) {
		this.backgroundPic = backgroundPic;
	}

	public void setGid(String gid) {
		Gid = gid;
	}

	public String getN_newMsgRemind() {
		return N_newMsgRemind;
	}

	public void setN_newMsgRemind(String n_newMsgRemind) {
		N_newMsgRemind = n_newMsgRemind;
	}

	public String getShowNikeName() {
		return showNikeName;
	}

	public void setShowNikeName(String showNikeName) {
		this.showNikeName = showNikeName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getIsMember() {
		return isMember;
	}

	public void setIsMember(String isMember) {
		this.isMember = isMember;
	}

	public NoticeContacts(String gid, String n_newMsgRemind,
			String showNikeName, String userId, String isMember) {
		super();
		Gid = gid;
		N_newMsgRemind = n_newMsgRemind;
		this.showNikeName = showNikeName;
		this.userId = userId;
		this.isMember = isMember;
	}

	@Override
	public String toString() {
		return "NoticeContacts [Gid=" + Gid + ", N_newMsgRemind="
				+ N_newMsgRemind + ", showNikeName=" + showNikeName
				+ ", userId=" + userId + ", isMember=" + isMember + "]";
	}

}