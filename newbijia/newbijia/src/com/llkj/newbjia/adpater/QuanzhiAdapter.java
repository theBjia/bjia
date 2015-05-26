package com.llkj.newbjia.adpater;

/**
 * 文章详情页图片适配器
 * 
 */

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.Spanned;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.llkj.newbjia.MyApplication;
import com.llkj.newbjia.R;
import com.llkj.newbjia.bean.UserInfoBean;
import com.llkj.newbjia.gester.ImagesActivity;
import com.llkj.newbjia.utils.FinalBitmapUtil;
import com.llkj.newbjia.utils.LogUtil;
import com.llkj.newbjia.utils.StringUtil;

@SuppressLint("UseSparseArrays")
public class QuanzhiAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	protected Map<Integer, View> viewMap = new HashMap<Integer, View>();
	@SuppressWarnings("rawtypes")
	private ArrayList list;
	private Context context;
	private MyClicker myCLicker;
	private FinalBitmapUtil fb;
	private ViewHolder holder = null;

	@SuppressWarnings("rawtypes")
	public QuanzhiAdapter(Context context, ArrayList list, MyClicker myCLicker) {
		this.inflater = LayoutInflater.from(context);
		this.list = list;
		this.context = context;
		this.myCLicker = myCLicker;
		fb = FinalBitmapUtil.create(context);
	}

	@SuppressWarnings("rawtypes")
	public void notifyDataSetChanged(ArrayList arrayList) {
		viewMap.clear();
		this.notifyDataSetChanged();

	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LogUtil.e("position " + position);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_sheququan, null);
			holder = new ViewHolder();
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);

			holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
			holder.tv_zannames = (TextView) convertView
					.findViewById(R.id.tv_zannames);
			holder.tv_delete = (TextView) convertView
					.findViewById(R.id.tv_delete);

			holder.tv_content = (TextView) convertView
					.findViewById(R.id.tv_content);
			holder.iv_pinglun = (ImageView) convertView
					.findViewById(R.id.iv_pinglun);
			holder.iv_praise = (ImageView) convertView
					.findViewById(R.id.iv_praise);
			holder.tv_praise = (TextView) convertView.findViewById(R.id.tv_praise);
			holder.iv_content = (ImageView) convertView
					.findViewById(R.id.iv_content);
			holder.ll_images = (LinearLayout) convertView
					.findViewById(R.id.ll_images);
			holder.ll_comments = (LinearLayout) convertView
					.findViewById(R.id.ll_comments);
			holder.imageView2 = (ImageView) convertView
					.findViewById(R.id.imageView2);
			
			holder.iv_praise.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					myCLicker.myClick(v, 4);
					Map bigMap;
					bigMap = (HashMap) v.getTag();
					String love = bigMap.get("love") + "";
					String iiid = bigMap.get("id") + "";
					if ("0".equals(love)) {
						holder.tv_praise.setText("取消赞");
					} else {
                        holder.tv_praise.setText("赞");
					}
				}
			});

			holder.iv_pinglun.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO change it into diffrent things.(divide)
					myCLicker.myClick(v, 5);
				}
			});
			holder.tv_delete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					myCLicker.myClick(v, 3);
				}
			});
			holder.iv_content.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					myCLicker.myClick(v, 2);
				}
			});
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.ll_images.removeAllViews();
		holder.ll_comments.removeAllViews();

		HashMap map = (HashMap) list.get(position);
		holder.iv_praise.setTag(map);
		holder.iv_pinglun.setTag(map);
		holder.tv_delete.setTag(map);
		// 图片添加
		final ArrayList alsone = (ArrayList) map.get("pictures");
		if (alsone != null && alsone.size() > 0) {
			if (alsone.size() == 1) {
				View view_one = (View) inflater.inflate(
						R.layout.item_quanzi_iv_one, null);
				ImageView iv_one = (ImageView) view_one
						.findViewById(R.id.iv_one);

				String picone = alsone.get(0) + "";

				if (!StringUtil.isEmpty(picone)) {
					fb.displayForPicture(iv_one, picone);
				} else {
					iv_one.setImageResource(R.drawable.ic_launcher);
				}
				holder.ll_images.addView(view_one);
				initClick(iv_one, alsone, 0);

			} else if (alsone.size() == 2) {
				View view_two = (View) inflater.inflate(
						R.layout.item_quanzi_iv_two, null);
				ImageView iv_one = (ImageView) view_two
						.findViewById(R.id.iv_one);
				ImageView iv_two = (ImageView) view_two
						.findViewById(R.id.iv_two);
				String picone = alsone.get(0) + "";
				String pictwo = alsone.get(1) + "";
				if (!StringUtil.isEmpty(picone)) {
					fb.displayForPicture(iv_one, picone);
					initClick(iv_one, alsone, 0);
				} else {
					iv_one.setImageResource(R.drawable.ic_launcher);
				}
				if (!StringUtil.isEmpty(pictwo)) {
					fb.displayForPicture(iv_two, pictwo);
					initClick(iv_two, alsone, 1);
				} else {
					iv_two.setImageResource(R.drawable.ic_launcher);
				}

				holder.ll_images.addView(view_two);
			} else if (alsone.size() > 2) {
				if (alsone.size() == 3) {
					View view = addIVView(alsone.get(0) + "", alsone.get(1)
							+ "", alsone.get(2) + "", 1, alsone);
					holder.ll_images.addView(view);
				} else if (alsone.size() == 4) {
					View view = addIVView(alsone.get(0) + "", alsone.get(1)
							+ "", alsone.get(2) + "", 1, alsone);
					View viewtwo = addIVView(alsone.get(3) + "", "", "", 2,
							alsone);
					holder.ll_images.addView(view);
					holder.ll_images.addView(viewtwo);
				} else if (alsone.size() == 5) {
					View view = addIVView(alsone.get(0) + "", alsone.get(1)
							+ "", alsone.get(2) + "", 1, alsone);
					View viewtwo = addIVView(alsone.get(3) + "", alsone.get(4)
							+ "", "", 2, alsone);
					holder.ll_images.addView(view);
					holder.ll_images.addView(viewtwo);
				} else if (alsone.size() == 6) {
					View view = addIVView(alsone.get(0) + "", alsone.get(1)
							+ "", alsone.get(2) + "", 1, alsone);
					View viewtwo = addIVView(alsone.get(3) + "", alsone.get(4)
							+ "", alsone.get(5) + "", 2, alsone);
					holder.ll_images.addView(view);
					holder.ll_images.addView(viewtwo);
				} else if (alsone.size() == 7) {
					View view = addIVView(alsone.get(0) + "", alsone.get(1)
							+ "", alsone.get(2) + "", 1, alsone);
					View viewtwo = addIVView(alsone.get(3) + "", alsone.get(4)
							+ "", alsone.get(5) + "", 2, alsone);
					View viewthree = addIVView(alsone.get(6) + "", "", "", 3,
							alsone);
					holder.ll_images.addView(view);
					holder.ll_images.addView(viewtwo);
					holder.ll_images.addView(viewthree);
				} else if (alsone.size() == 8) {
					View view = addIVView(alsone.get(0) + "", alsone.get(1)
							+ "", alsone.get(2) + "", 1, alsone);
					View viewtwo = addIVView(alsone.get(3) + "", alsone.get(4)
							+ "", alsone.get(5) + "", 2, alsone);
					View viewthree = addIVView(alsone.get(6) + "",
							alsone.get(7) + "", "", 3, alsone);
					holder.ll_images.addView(view);
					holder.ll_images.addView(viewtwo);
					holder.ll_images.addView(viewthree);
				} else if (alsone.size() == 9) {
					View view = addIVView(alsone.get(0) + "", alsone.get(1)
							+ "", alsone.get(2) + "", 1, alsone);
					View viewtwo = addIVView(alsone.get(3) + "", alsone.get(4)
							+ "", alsone.get(5) + "", 2, alsone);
					View viewthree = addIVView(alsone.get(6) + "",
							alsone.get(7) + "", alsone.get(8) + "", 3, alsone);
					holder.ll_images.addView(view);
					holder.ll_images.addView(viewtwo);
					holder.ll_images.addView(viewthree);
				}
			}
		}

		String name = map.get("name") + "";

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		String add_time = format.format(map.get("add_time"));
		String content = map.get("content") + "";
		String love = map.get("love") + "";
		String number = map.get("number") + "";
		String pic = map.get("pic") + "";
		String id = map.get("id") + "";
		String uid = map.get("user_id") + "";
		holder.tv_name.setText(name);
		holder.tv_time.setText(add_time);
		holder.tv_content.setText(content);
		holder.tv_zannames.setText(number);
		holder.iv_content.setTag(uid);

		if (uid.equals(UserInfoBean.getUserInfo(context).getUid())) {
			holder.tv_delete.setVisibility(View.VISIBLE);
		} else {
			holder.tv_delete.setVisibility(View.INVISIBLE);
		}
		if ("0".equals(love)) {
			holder.imageView2.setImageResource(R.drawable.quanzhi_xin);
			holder.tv_praise.setText("赞");
		} else {
			holder.imageView2.setImageResource(R.drawable.greenyouxin);
			holder.tv_praise.setText("取消赞");
		}

		if (!StringUtil.isEmpty(pic)) {
			fb.displayForPicture(holder.iv_content, pic);
		} else {
			holder.iv_content.setImageResource(R.drawable.ic_launcher);
		}
		// 评论内容添加
		ArrayList comments = (ArrayList) map.get("comments");
		if (comments != null && comments.size() > 0) {
			for (int i = 0; i < comments.size(); i++) {
				HashMap mapone = (HashMap) comments.get(i);
				mapone.put("id", id);
				mapone.put("position", position);
				String strcontent = mapone.get("content") + "";
				String stradd_time = mapone.get("add_time") + "";
				String strfrom_id = mapone.get("from_id") + "";
				String strfrom_name = mapone.get("from_name") + "";
				String strto_id = mapone.get("to_id") + "";
				String strto_name = mapone.get("to_name") + "";

				View view_four = (View) inflater.inflate(
						R.layout.item_quanzi_comment, null);
				TextView tv = (TextView) view_four
						.findViewById(R.id.tv_comment);
				tv.setText(getSpanned(strfrom_id, strto_id, strfrom_name,
						strto_name, strcontent));
				view_four.setTag(mapone);
				view_four.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO learn about it .
						myCLicker.myClick(v, 1);
					}
				});
				holder.ll_comments.addView(view_four);

			}
		}

		return convertView;
	}

	class ViewHolder {
		TextView tv_name, tv_time, tv_delete, tv_content, tv_zannames,
				tv_praise;
		ImageView iv_content, iv_pinglun, iv_praise, imageView2;
		LinearLayout ll_images, ll_comments;
	}

	public Spanned getSpanned(String from_id, String to_id, String user_name,
			String to_name, String content) {
		Spanned htmlstr;
		if (from_id.equals(to_id)) {
			htmlstr = Html.fromHtml("<font color=#28B485>" + user_name + ":"
					+ "</font> " + "<i><font color=#343432>" + content
					+ "</font></i>");
		} else {
			htmlstr = Html.fromHtml("<font color=#28B485>" + user_name
					+ "</font> " + "<i><font color=#343432>回复</font></i>"
					+ "<i><font color=#28B485>" + to_name + ":" + "</font></i>"
					+ "<i><font color=#343432>" + content + "</font></i>");
		}
		return htmlstr;

	}

	public View addIVView(String ivone, String ivtwo, String ivthree,
			int which, ArrayList arrayList) {
		View view_three = (View) inflater.inflate(
				R.layout.item_quanzi_iv_three, null);
		ImageView iv_one = (ImageView) view_three.findViewById(R.id.iv_one);
		ImageView iv_two = (ImageView) view_three.findViewById(R.id.iv_two);
		ImageView iv_three = (ImageView) view_three.findViewById(R.id.iv_three);

		switch (which) {
		case 1:
			if (!StringUtil.isEmpty(ivone)) {
				fb.displayForPicture(iv_one, ivone);
				initClick(iv_one, arrayList, 0);
			}
			if (!StringUtil.isEmpty(ivtwo)) {
				fb.displayForPicture(iv_two, ivtwo);
				initClick(iv_two, arrayList, 1);
			}
			if (!StringUtil.isEmpty(ivthree)) {
				fb.displayForPicture(iv_three, ivthree);
				initClick(iv_three, arrayList, 2);
			}

			break;
		case 2:
			if (!StringUtil.isEmpty(ivone)) {
				fb.displayForPicture(iv_one, ivone);
				initClick(iv_one, arrayList, 3);
			}
			if (!StringUtil.isEmpty(ivtwo)) {
				fb.displayForPicture(iv_two, ivtwo);
				initClick(iv_two, arrayList, 4);
			}
			if (!StringUtil.isEmpty(ivthree)) {
				fb.displayForPicture(iv_three, ivthree);
				initClick(iv_three, arrayList, 5);
			}
			break;
		case 3:
			if (!StringUtil.isEmpty(ivone)) {
				fb.displayForPicture(iv_one, ivone);
				initClick(iv_one, arrayList, 6);
			}
			if (!StringUtil.isEmpty(ivtwo)) {
				fb.displayForPicture(iv_two, ivtwo);
				initClick(iv_two, arrayList, 7);
			}
			if (!StringUtil.isEmpty(ivthree)) {
				fb.displayForPicture(iv_three, ivthree);
				initClick(iv_three, arrayList, 8);
			}
			break;
		default:
			break;
		}
		if (!StringUtil.isEmpty(ivone)) {
			fb.displayForPicture(iv_one, ivone);
		}
		if (!StringUtil.isEmpty(ivtwo)) {
			fb.displayForPicture(iv_two, ivtwo);
		}
		if (!StringUtil.isEmpty(ivthree)) {
			fb.displayForPicture(iv_three, ivthree);
		}
		return view_three;

	}

	public void initClick(ImageView iv, final ArrayList arrayList,
			final int position) {
		iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, ImagesActivity.class);
				intent.putParcelableArrayListExtra("url_list", arrayList);
				intent.putExtra("position", position);
				context.startActivity(intent);
			}
		});
		iv.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {

				MyApplication.showSaveDialog(context, arrayList.get(position)
						+ "");

				return false;
			}
		});
	}

	public interface MyClicker {
		void myClick(View v, int type);// 0点赞和评论，1回复
	}
}
