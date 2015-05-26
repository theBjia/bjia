package com.llkj.newbjia.gester;

import java.util.ArrayList;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.llkj.newbjia.R;
import com.llkj.newbjia.utils.FinalBitmapUtil;

public class ImagesActivity extends Activity {
	private ViewPager viewPager;
	private ArrayList<View> pageViews;
	private ImageView imageView;
	private ImageView[] imageViews;
	// 包裹滑动图片LinearLayout
	private ViewGroup main;
	// 包裹小圆点的LinearLayout
	private ViewGroup group;
	private FinalBitmapUtil fb;
	private Intent intent;

	private ArrayList<Map<String, String>> url_list;
	private int position;
	private GuidePageAdapter adapter;
	private int i = 0;
	private LinearLayout ly;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.images);
		initView();
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	private void initView() {
		fb = FinalBitmapUtil.create(this);
		LayoutInflater inflater = getLayoutInflater();
		intent = getIntent();
		url_list = (ArrayList) intent.getSerializableExtra("url_list");

		position = intent.getIntExtra("position", 0);
		main = (ViewGroup) inflater.inflate(R.layout.images, null);
		group = (ViewGroup) main.findViewById(R.id.viewGroup);
		viewPager = (ViewPager) main.findViewById(R.id.guidePages);
		setContentView(main);
		pageViews = new ArrayList<View>();
		findViewById(R.id.hehej).setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View arg0) {
				Toast.makeText(getApplicationContext(), "dssa ", 1000).show();
				return false;
			}
		});
		for (int i = 0; i < url_list.size(); i++) {
			LinearLayout layout = new LinearLayout(this);
			LayoutParams ltp = new LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.FILL_PARENT);
			final GestureImageView imageView = new GestureImageView(this);
			imageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					finish();
				}
			});
			imageView.setScaleType(ScaleType.FIT_XY);
			fb.displayForPicture(imageView, url_list.get(i) + "");
			layout.addView(imageView, ltp);
			pageViews.add(layout);
		}
		imageViews = new ImageView[pageViews.size()];

		for (int i = 0; i < pageViews.size(); i++) {
			imageView = new ImageView(ImagesActivity.this);
			imageView.setLayoutParams(new LayoutParams(20, 20));
			imageView.setPadding(20, 0, 20, 0);
			imageViews[i] = imageView;
			if (i == 0) {
				// 默认选中第一张图片
				// imageViews[i].setBackgroundResource(R.drawable.help_select);
			} else {
				// imageViews[i].setBackgroundResource(R.drawable.help_unselect);
			}
			group.addView(imageViews[i]);
		}
		adapter = new GuidePageAdapter();
		viewPager.setAdapter(adapter);
		viewPager.setCurrentItem(position);
		viewPager.setOnPageChangeListener(new GuidePageChangeListener());
	}

	// 指引页面数据适配器
	class GuidePageAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return pageViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public int getItemPosition(Object object) {
			// TODO Auto-generated method stub
			return super.getItemPosition(object);
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			// TODO Auto-generated method stub
			((ViewPager) arg0).removeView(pageViews.get(arg1));
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			// TODO Auto-generated method stub
			((ViewPager) arg0).addView(pageViews.get(arg1));
			return pageViews.get(arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
			// TODO Auto-generated method stub

		}

		@Override
		public Parcelable saveState() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void finishUpdate(View arg0) {
			// TODO Auto-generated method stub

		}
	}

	// 指引页面更改事件监听器
	class GuidePageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageSelected(int arg0) {
			for (int i = 0; i < imageViews.length; i++) {
				// imageViews[arg0].setBackgroundResource(R.drawable.help_select);

				if (arg0 != i) {
					// imageViews[i]
					// .setBackgroundResource(R.drawable.help_unselect);
				}
			}
		}
	}
}
