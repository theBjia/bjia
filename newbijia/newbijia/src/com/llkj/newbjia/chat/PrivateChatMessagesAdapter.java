package com.llkj.newbjia.chat;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.sax.StartElementListener;
import android.text.SpannableString;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.llkj.db.PrivateChatMessagesEntity;
import com.llkj.newbjia.R;
import com.llkj.newbjia.bean.UserInfoBean;
import com.llkj.newbjia.friend.DetailInfoActivity;
import com.llkj.newbjia.mybijia.MyCenterActivity;
import com.llkj.newbjia.utils.File2Code;
import com.llkj.newbjia.utils.FinalBitmapUtil;
import com.llkj.newbjia.utils.ImageDispose;
import com.llkj.newbjia.utils.ImageOperate;
import com.llkj.newbjia.utils.StringUtil;
import com.llkj.newbjia.utils.TimeUtils;
import com.llkj.newbjia.utils.ToneManager;
import com.llkj.newbjia.utils.Utils;

public class PrivateChatMessagesAdapter extends BaseAdapter {
	protected Map<Integer, View> viewMap = new HashMap<Integer, View>();

	private Context context;

	private static final String TAG = PrivateChatMessagesAdapter.class
			.getSimpleName();

	private List<PrivateChatMessagesEntity> coll;
	private LayoutInflater mInflater;
	private FinalBitmapUtil fb;

	private ImageView imageView1;
	private int width, height;
	private DisplayMetrics displaysMetrics;
	private ToneManager tm;

	public static interface IMsgViewType {
		int IMVT_COM_MSG = 0;
		int IMVT_TO_MSG = 1;
	}

	public PrivateChatMessagesAdapter(Context context,
			List<PrivateChatMessagesEntity> coll) {
		this.coll = coll;
		mInflater = LayoutInflater.from(context);
		this.context = context;
		fb = FinalBitmapUtil.create(context);
		displaysMetrics = context.getResources().getDisplayMetrics();
		width = displaysMetrics.widthPixels;
		height = displaysMetrics.heightPixels;
		tm = new ToneManager(context, null);

	}

	public ToneManager getTM() {
		if (tm == null) {
			tm = new ToneManager(context, null);
		}
		return tm;
	}

	public void setData(List<PrivateChatMessagesEntity> coll) {
		if (coll != null)
			this.coll = coll;
		else
			this.coll = new ArrayList<PrivateChatMessagesEntity>();
	}

	public void notifyDataSetChanged(List<PrivateChatMessagesEntity> coll) {

		setData(coll);
		viewMap.clear();
		this.notifyDataSetChanged();

	}

	public int getCount() {
		return coll.size();
	}

	public Object getItem(int position) {
		return coll.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public int getItemViewType(int position) {

		PrivateChatMessagesEntity entity = coll.get(position);

		if ("1".equals(entity.getBubbleType())) {
			return IMsgViewType.IMVT_COM_MSG;
		} else {
			return IMsgViewType.IMVT_TO_MSG;
		}

	}

	public int getViewTypeCount() {

		return 2;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		final PrivateChatMessagesEntity entity = coll.get(position);

		ViewHolder viewHolder = null;
		View pv = null;
		if (viewMap.get(position) == null) {
			// 图片显示
			if (Constants.PICTURE.equals(entity.getMessageType())) {
				if ("1".equals(entity.getBubbleType())) {
					pv = mInflater.inflate(R.layout.chat_left_from_pic, null);
				} else {
					pv = mInflater.inflate(R.layout.chat_right_to_pic, null);
				}
				viewHolder = new ViewHolder();
				viewHolder.tv_chat_time = (TextView) pv
						.findViewById(R.id.tv_chat_time);
				viewHolder.tv_chat_name = (TextView) pv
						.findViewById(R.id.tv_chat_name);
				viewHolder.iv_chat_photo = (ImageView) pv
						.findViewById(R.id.chat_photo);
				viewHolder.iv_chat_content = (ImageView) pv
						.findViewById(R.id.iv_chat_content);
				viewHolder.iv_chat_content.setTag(entity);
				// 设置几天消息显示一次时间
				viewHolder.tv_chat_name.setVisibility(View.INVISIBLE);
				if (position % 8 == 0) {
					viewHolder.tv_chat_time.setVisibility(View.VISIBLE);
					long time = Long.parseLong(entity.getMessageDate());
					viewHolder.tv_chat_time.setText(TimeUtils.showTime(time));
				} else {
					viewHolder.tv_chat_time.setVisibility(View.INVISIBLE);
				}
				if (entity.getActionType().equals(Constants.ONREPLY)) {

				}
				if (StringUtil.isEmpty(entity.getSendedLogo())) {
					viewHolder.iv_chat_photo
							.setImageResource(R.drawable.chat_xitongxiaoxi);
				} else {
					fb.displayForHeader(viewHolder.iv_chat_photo,
							entity.getSendedLogo());
				}

				viewHolder.iv_chat_photo
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {

								if (!entity.getSendedId().equals(
										UserInfoBean.getUserInfo(context)
												.getUid())) {
									Intent intent = new Intent(context,
											DetailInfoActivity.class);
									intent.putExtra("fid", entity.getSendedId());
									context.startActivity(intent);

								} else {
									Intent intent = new Intent(context,
											MyCenterActivity.class);
									context.startActivity(intent);

								}

							}
						});
				try {
					viewHolder.iv_chat_content.setVisibility(View.VISIBLE);
					// 如果是来的消息就 反编码。否则直接从本地查找

					String path = entity.getMessageContent();
					Log.e("=============", path);
					int degree = ImageDispose.readPictureDegree(path);
					ImageOperate.setImageSrc(viewHolder.iv_chat_content, path,
							degree);

					viewHolder.iv_chat_content
							.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									PrivateChatMessagesEntity entity = (PrivateChatMessagesEntity) v
											.getTag();
									Intent intent = new Intent(context,
											OneImageScanTwoActivity.class);
									try {

										intent.putExtra("path",
												entity.getMessageContent());

										context.startActivity(intent);
									} catch (Exception e1) {
										e1.printStackTrace();
									}
								}
							});

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			// 文本信息
			else if (Constants.TEXT.equals(entity.getMessageType())) {
				if ("1".equals(entity.getBubbleType())) {
					pv = mInflater.inflate(R.layout.chat_left_from_text, null);
				} else {
					pv = mInflater.inflate(R.layout.chat_right_to_text, null);
				}
				viewHolder = new ViewHolder();
				viewHolder.rl_chat = (RelativeLayout) pv
						.findViewById(R.id.rl_chat);
				viewHolder.tv_chat_time = (TextView) pv
						.findViewById(R.id.tv_chat_time);
				viewHolder.tv_chat_name = (TextView) pv
						.findViewById(R.id.tv_chat_name);
				viewHolder.tv_chat_content = (TextView) pv
						.findViewById(R.id.tv_chat_content);
				viewHolder.iv_chat_photo = (ImageView) pv
						.findViewById(R.id.chat_photo);
				viewHolder.tv_chat_content.setTag(entity);

				// 设置几天消息显示一次时间
				viewHolder.tv_chat_name.setVisibility(View.INVISIBLE);
				if (position % 8 == 0) {
					viewHolder.tv_chat_time.setVisibility(View.VISIBLE);
					long time = Long.parseLong(entity.getMessageDate());
					viewHolder.tv_chat_time.setText(TimeUtils.showTime(time));
				} else {
					viewHolder.tv_chat_time.setVisibility(View.INVISIBLE);
				}
				if (StringUtil.isEmpty(entity.getSendedLogo())) {
					viewHolder.iv_chat_photo
							.setImageResource(R.drawable.chat_xitongxiaoxi);
				} else {
					fb.displayForHeader(viewHolder.iv_chat_photo,
							entity.getSendedLogo());
				}
				viewHolder.iv_chat_photo
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {

								Intent intent = new Intent(context,
										DetailInfoActivity.class);
								if (!entity.getSendedId().equals(
										UserInfoBean.getUserInfo(context)
												.getUid())) {

									intent.putExtra("fid", entity.getSendedId());

								} else {

									intent.putExtra("fid", UserInfoBean
											.getUserInfo(context).getUid());
								}
								context.startActivity(intent);
							}
						});
				String str = entity.getMessageContent(); // 消息具体内容
				String zhengze = "\\[([\u4e00-\u9fa5]+)\\]"; // 正则表达式，用来判断消息内是否有表情
				// f0[0-9]{2}|f10[0-7]
				SpannableString spannableString = ExpressionUtil
						.getExpressionString(context, str, zhengze);

				viewHolder.tv_chat_content.setText(spannableString);
				viewHolder.tv_chat_content.setVisibility(View.VISIBLE);

			} else if (Constants.VOICE.equals(entity.getMessageType())) {
				if ("1".equals(entity.getBubbleType())) {
					pv = mInflater.inflate(R.layout.chat_left_from_voice, null);
				} else {
					pv = mInflater.inflate(R.layout.chat_right_to_voice, null);
				}
				viewHolder = new ViewHolder();
				viewHolder.rl_chat = (RelativeLayout) pv
						.findViewById(R.id.rl_chat);
				viewHolder.tv_chat_time = (TextView) pv
						.findViewById(R.id.tv_chat_time);
				viewHolder.tv_chat_name = (TextView) pv
						.findViewById(R.id.tv_chat_name);
				viewHolder.tv_chat_voice_length = (TextView) pv
						.findViewById(R.id.tv_chat_voice_length);
				viewHolder.tv_chat_voice_length.setText("");
				viewHolder.iv_chat_photo = (ImageView) pv
						.findViewById(R.id.chat_photo);

				viewHolder.iv_chat_voice = (ImageView) pv
						.findViewById(R.id.iv_chat_voice);
				viewHolder.rl_chat.setTag(entity);

				// 设置几天消息显示一次时间
				viewHolder.tv_chat_name.setVisibility(View.INVISIBLE);
				if (position % 8 == 0) {
					viewHolder.tv_chat_time.setVisibility(View.VISIBLE);
					long time = Long.parseLong(entity.getMessageDate());
					viewHolder.tv_chat_time.setText(TimeUtils.showTime(time));
				} else {
					viewHolder.tv_chat_time.setVisibility(View.INVISIBLE);
				}

				if (StringUtil.isEmpty(entity.getSendedLogo())) {
					viewHolder.iv_chat_photo
							.setImageResource(R.drawable.chat_xitongxiaoxi);
				} else {
					fb.displayForHeader(viewHolder.iv_chat_photo,
							entity.getSendedLogo());
				}
				viewHolder.iv_chat_photo
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {

								Intent intent = new Intent(context,
										DetailInfoActivity.class);
								if (!entity.getSendedId().equals(
										UserInfoBean.getUserInfo(context)
												.getUid())) {

									intent.putExtra("fid", entity.getSendedId());

								} else {

									intent.putExtra("fid", UserInfoBean
											.getUserInfo(context).getUid());
								}
								context.startActivity(intent);
							}
						});
				// ==============
				// 播放
				viewHolder.rl_chat.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						PrivateChatMessagesEntity centity = (PrivateChatMessagesEntity) v
								.getTag();
						String voiceMessage = centity.getMessageContent();
						try {
							File2Code.decoderBase64File(voiceMessage,
									getAmrPath());
						} catch (Exception e1) {

							e1.printStackTrace();
						}
						tm.play(getAmrPath());
					}
				});
				viewHolder.iv_chat_voice.setVisibility(View.VISIBLE);
				String voiceleng = entity.getVoicelength();
				String[] voicel = voiceleng.split("\\.");
				viewHolder.tv_chat_voice_length.setText(voicel[0] + "''");
				// 宽度，高度

				RelativeLayout.LayoutParams lp = (LayoutParams) viewHolder.rl_chat
						.getLayoutParams();
				// 动态宽度=(屏幕宽度-左边头像50-右边头像50-最小宽度60)*(录音长度/60)+最小宽度60.
				lp.width = (int) ((width - Utils.dip2px(context, 160)) * (Integer
						.parseInt(voicel[0]) / 60.0))
						+ Utils.dip2px(context, 60);
				if ("1".equals(entity.getBubbleType())) {
					lp.addRule(RelativeLayout.RIGHT_OF, R.id.chat_photo);
				} else {
					lp.addRule(RelativeLayout.LEFT_OF, R.id.chat_photo);
				}

				if (viewHolder.tv_chat_name.getVisibility() == View.INVISIBLE) {
					lp.addRule(RelativeLayout.ALIGN_TOP, R.id.chat_photo);
				} else {
					lp.addRule(RelativeLayout.BELOW, R.id.tv_chat_name);
				}
				viewHolder.rl_chat.setLayoutParams(lp);

			} else if (Constants.LOCATIONPOSITION.equals(entity
					.getMessageType())) {
				if ("1".equals(entity.getBubbleType())) {
					pv = mInflater.inflate(
							R.layout.chat_left_from_pic_position, null);
				} else {
					pv = mInflater.inflate(R.layout.chat_right_to_pic_position,
							null);
				}
				viewHolder = new ViewHolder();
				viewHolder.tv_chat_time = (TextView) pv
						.findViewById(R.id.tv_chat_time);
				viewHolder.tv_chat_name = (TextView) pv
						.findViewById(R.id.tv_chat_name);
				viewHolder.tv_position = (TextView) pv
						.findViewById(R.id.tv_position);
				viewHolder.iv_chat_photo = (ImageView) pv
						.findViewById(R.id.chat_photo);
				viewHolder.iv_chat_content = (ImageView) pv
						.findViewById(R.id.iv_chat_content);
				viewHolder.iv_chat_content.setTag(entity);
				// 设置几天消息显示一次时间
				viewHolder.tv_chat_name.setVisibility(View.INVISIBLE);
				if (position % 8 == 0) {
					viewHolder.tv_chat_time.setVisibility(View.VISIBLE);
					long time = Long.parseLong(entity.getMessageDate());
					viewHolder.tv_chat_time.setText(TimeUtils.showTime(time));
				} else {
					viewHolder.tv_chat_time.setVisibility(View.INVISIBLE);
				}

				if (StringUtil.isEmpty(entity.getSendedLogo())) {
					viewHolder.iv_chat_photo
							.setImageResource(R.drawable.chat_xitongxiaoxi);
				} else {
					fb.displayForHeader(viewHolder.iv_chat_photo,
							entity.getSendedLogo());
				}
				viewHolder.iv_chat_photo
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {

								if (!entity.getSendedId().equals(
										UserInfoBean.getUserInfo(context)
												.getUid())) {
									Intent intent = new Intent(context,
											DetailInfoActivity.class);
									intent.putExtra("fid", entity.getSendedId());
									context.startActivity(intent);

								} else {
									Intent intent = new Intent(context,
											MyCenterActivity.class);
									context.startActivity(intent);

								}

							}
						});
				try {
					viewHolder.iv_chat_content.setVisibility(View.VISIBLE);
					// 如果是来的消息就 反编码。否则直接从本地查找

					String content = entity.getMessageContent();
					String[] positions = content.split(":");
					viewHolder.tv_position.setText(positions[0]);

					viewHolder.iv_chat_content
							.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									PrivateChatMessagesEntity entity = (PrivateChatMessagesEntity) v
											.getTag();
									String content = entity.getMessageContent();
									Intent intent = new Intent(context,
											SendPositionActivity.class);
									intent.putExtra("content", content);
									context.startActivity(intent);

								}
							});

				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			pv.setTag(viewHolder);
			viewMap.put(position, pv);
		} else {
			pv = viewMap.get(position);
			viewHolder = (ViewHolder) pv.getTag();
		}

		return pv;
	}

	class ViewHolder {
		TextView tv_chat_time, tv_chat_name, tv_chat_content,
				tv_chat_voice_length, tv_position;
		ImageView iv_chat_photo, iv_chat_content, iv_chat_voice, chat_photo;
		RelativeLayout rl_chat;
	}

	// 获取文件手机路径
	private String getAmrPath() {
		File file = new File(Environment.getExternalStorageDirectory(),
				"my/voice.amr");
		return file.getAbsolutePath();
	}

	private String getPicPath() {
		File file = new File(Environment.getExternalStorageDirectory(),
				"my/pic.png");

		return file.getAbsolutePath();
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public void setClipboard(String text) {
		ClipboardManager clipboard = (ClipboardManager) context
				.getSystemService(Context.CLIPBOARD_SERVICE);
		clipboard.setText(text);
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public String getClipboard() {
		ClipboardManager clipboard = (ClipboardManager) context
				.getSystemService(Context.CLIPBOARD_SERVICE);
		return clipboard.getText().toString();
	}

	public interface ChatMsgClickListener {
		void onImageClick(PrivateChatMessagesEntity info);

		void onItemClick(PrivateChatMessagesEntity info);
	}
}
