package com.llkj.newbjia.adpater;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.llkj.db.RecentlyContacts;
import com.llkj.newbjia.MyApplication;
import com.llkj.newbjia.MyClicker;
import com.llkj.newbjia.R;
import com.llkj.newbjia.chat.Constants;
import com.llkj.newbjia.chat.ExpressionUtil;
import com.llkj.newbjia.utils.FinalBitmapUtil;
import com.llkj.newbjia.utils.TimeUtils;

public class RecentlyChatAdpter extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater;
	private List<RecentlyContacts> list;
	private FinalBitmapUtil fb;
	private MyClicker myClicker;

	public RecentlyChatAdpter(Context context, List<RecentlyContacts> list,
			MyClicker myClicker) {
		super();
		this.context = context;
		setData(list);
		inflater = LayoutInflater.from(context);
		fb = FinalBitmapUtil.create(context);
		this.myClicker = myClicker;
	}

	public void setData(List<RecentlyContacts> list) {
		if (list == null) {
			this.list = new ArrayList<RecentlyContacts>();
		} else {
			this.list = list;
		}
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);

	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View view, ViewGroup arg2) {
		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = inflater.inflate(R.layout.item_recentchat, null);
			holder.picIv = (ImageView) view.findViewById(R.id.picIv);
			holder.nameTv = (TextView) view.findViewById(R.id.nameTv);
			holder.messageNumberTv = (TextView) view
					.findViewById(R.id.messageNumberTv);
			holder.tv_delete = (TextView) view.findViewById(R.id.tv_delete);
			holder.textTv = (TextView) view.findViewById(R.id.textTv);
			holder.timeTv = (TextView) view.findViewById(R.id.timeTv);
			holder.rl_background = (RelativeLayout) view
					.findViewById(R.id.rl_background);
			holder.rl_background.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					myClicker.myClick(v, 1);
					holder.tv_delete.setVisibility(View.INVISIBLE);
				}
			});
			holder.rl_background
					.setOnLongClickListener(new OnLongClickListener() {

						@Override
						public boolean onLongClick(View v) {
							holder.tv_delete.setVisibility(View.VISIBLE);
							return true;
						}
					});
			holder.tv_delete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					myClicker.myClick(v, 0);
				}
			});
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		RecentlyContacts rentity = list.get(position);
		holder.rl_background.setTag(rentity);
		holder.tv_delete.setTag(rentity);
		String time = rentity.getMessageDate();
		String content = rentity.getMessageContent();
		String chatType = rentity.getMessageChatType();
		String type = rentity.getMessageType();
		String messagenum = rentity.getUnReadNumber();
		String name = rentity.getReceiveName();
		String logo = rentity.getReceiveLogo();
		if(rentity.getMessageChatType().equals(Constants.ONREPLY)){
			holder.nameTv.setText(MyApplication.esq_name);
			holder.picIv.setImageResource(R.drawable.chat_xitongxiaoxi);
		}else{
			holder.nameTv.setText(name);
			fb.displayForHeader(holder.picIv, logo);
		}
		
		try {
			long times = Long.parseLong(time);
			holder.timeTv.setText(TimeUtils.showTime(times));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		if (Integer.parseInt(messagenum) > 0) {
			holder.messageNumberTv.setText(messagenum);
			holder.messageNumberTv.setVisibility(View.VISIBLE);
		} else {
			holder.messageNumberTv.setVisibility(View.INVISIBLE);
		}
		if (Constants.PICTURE.equals(type)) {
			content = "[图片]";
			holder.textTv.setText(content);
		} else if (Constants.VOICE.equals(type)) {
			content = "[语音]";
			holder.textTv.setText(content);
		} else if (Constants.TEXT.equals(type)) {
			try {
				String zhengze = "\\[([\u4e00-\u9fa5]+)\\]"; // 正则表达式，用来判断消息内是否有表情
				SpannableString spannableString = ExpressionUtil
						.getExpressionString(context, content, zhengze);
				holder.textTv.setText(spannableString);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (Constants.LOCATIONPOSITION.equals(type)) {
			try {
				holder.textTv.setText(content);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return view;
	}

	class ViewHolder {
		ImageView picIv;
		TextView nameTv, messageNumberTv, tv_delete, textTv, timeTv;
		RelativeLayout rl_background;
	}

}
