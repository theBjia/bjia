package com.llkj.newbjia.chat;


/**
 * 该类为最大传递的对象，可以添加属性和方法。*/

import java.io.Serializable;

public class TranObject implements Serializable{
	//
	private Object object;
	private String actionType;
	public Object getObject() {
		return object;
	}
	public void setObject(Object object) {
		this.object = object;
	}
	public String getActionType() {
		return actionType;
	}
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}
	
	
}
