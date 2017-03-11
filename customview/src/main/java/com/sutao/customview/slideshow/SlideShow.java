package com.sutao.customview.slideshow;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.sutao.customview.R;
import com.sutao.customview.util.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * emailTo intern_zhangsutao@cvte.com
 *
 * @author zhangsutao
 * @brief 轮播图控件
 * @date 2017/3/9
 */
public class SlideShow extends FrameLayout implements View.OnClickListener {

    private final int DOT_CONTAINER_HEIGHT = 30;


    private int mDotSize = 10;
    private int mDotMargin = 5;
    private int mSlideInteval = 1500;

    private ViewPager mViewPager;
    private SlidePagerAdapter mAdapter;
    private LinearLayout mDotContainer;
    private Context mContext;
    private Runnable mSlideTask;

    private List<Bitmap> mBitmaps;
    private List<ImageView> mViews;

    private OnItemClickListener mListener;

    public SlideShow(Context context) {
        super(context, null);
    }

    public SlideShow(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mViews = new ArrayList<>();
        mSlideTask = new Runnable() {
            @Override
            public void run() {
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
                postDelayed(this, mSlideInteval);
            }
        };
        initView();
        initEvent();
    }

    private void initEvent() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                updateDot(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewPager.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        startSlide();break;
                    default:stopSlide();break;
                }
                return false;
            }
        });
    }

    private void initView() {
        mViewPager = new ViewPager(mContext);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(mViewPager, params);

        LayoutParams params1 = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mDotContainer = new LinearLayout(mContext);
        mDotContainer.setBackgroundColor(Color.TRANSPARENT);
        mDotContainer.setOrientation(LinearLayout.HORIZONTAL);
        mDotContainer.setGravity(Gravity.CENTER);
        params1.height = (int) UIUtils.getDp(mContext, DOT_CONTAINER_HEIGHT);
        params1.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params1.gravity = Gravity.BOTTOM;
        addView(mDotContainer, params1);
    }


    private void updateDot(int position) {
        for (int i = 0; i < mDotContainer.getChildCount(); i++) {
            mDotContainer.getChildAt(i).setBackgroundResource(R.drawable.dot_unselect);
        }

        mDotContainer.getChildAt(position % mViews.size()).setBackgroundResource(R.drawable.dot_select);
    }


    public List<Bitmap> getBitmaps() {
        return mBitmaps;
    }

    public void setBitmaps(List<Bitmap> bitmaps) {
        this.mBitmaps = bitmaps;

        mViews.clear();
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        for (int i = 0; i < mBitmaps.size(); i++) {
            ImageView img = new ImageView(mContext);
            img.setLayoutParams(params);
            img.setScaleType(ImageView.ScaleType.CENTER_CROP);
            img.setImageBitmap(mBitmaps.get(i));
            img.setTag(i);
            img.setOnClickListener(this);
            mViews.add(img);
        }

        mDotContainer.removeAllViews();
        for (int i = 0; i < bitmaps.size(); i++) {
            View view = new View(mContext);
            view.setBackgroundResource(R.drawable.dot_unselect);
            LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams((int) UIUtils.getDp(mContext, mDotSize), (int) UIUtils.getDp(mContext, mDotSize));
            params1.setMargins((int) UIUtils.getDp(mContext, mDotMargin), 0, (int) UIUtils.getDp(mContext, mDotMargin), 0);
            mDotContainer.addView(view, params1);
        }

        if (mAdapter == null) {
            mAdapter = new SlidePagerAdapter();
        }

        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(mViews.size() * 40000);
        updateDot(mViews.size() * 40000);
    }

    public void startSlide() {
        postDelayed(mSlideTask, mSlideInteval);
    }

    public void stopSlide() {
        getHandler().removeCallbacks(mSlideTask);
    }

    @Override
    public void onClick(View v) {
        if (mListener != null) {
            mListener.onItemClick((Integer) v.getTag());
        }
    }


    private class SlidePagerAdapter extends PagerAdapter {
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView view = mViews.get(position % mViews.size());
            if (view.getParent() != null) {
                container.removeView(view);
            }
            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

        }
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }


    public interface OnItemClickListener {
        void onItemClick(int position);
    }

}
