package com.llkj.newbjia;
/**
 * This is mainly used for LeftDrawerFragment.When the new message is coming,then
 * change the state of some views.
 * @author Wang
 *
 */
public interface MyOnNewMessageListener {
	//maybe i can use the enum.
	public static final int NEW_CHAT_MESSAGE = 1;
	public static final int NEW_QUANZI_MESSAGE = 2;
	public void onNewMessegeComing(int type);

}
