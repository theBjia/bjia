package com.llkj.newbjia.bean;



import java.io.Serializable;

/**
 * 画友 实体类
 */
public class FriendBean implements Serializable {
	/**
	 * id
	 */
	private String uid;
	/**
	 * 画号
	 */
	private String name;
	/**
	 * 昵称
	 */
	private String nickname;
	/**
	 * 头像
	 */
	private String pic;
	/**
	 * 首字母,用于排序
	 */
	private String headerChar;
	/**
	 * 省份/直辖市
	 */
	private String province;

	/**
	 * 市/显/区
	 */
	private String city;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getHeaderChar() {
		return headerChar;
	}

	public void setHeaderChar(String headerChar) {
		this.headerChar = headerChar;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

}
