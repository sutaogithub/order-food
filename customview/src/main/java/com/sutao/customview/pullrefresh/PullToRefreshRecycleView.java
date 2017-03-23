package com.sutao.customview.pullrefresh;


import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2017/3/12.
 */
public class PullToRefreshRecycleView extends PullToRefreshBase<RecyclerView> {


    private RecyclerView mRecyclerView;
    private RecyclerView.OnScrollListener mScrollListener;

    public PullToRefreshRecycleView(Context context) {
        super(context);
    }

    public PullToRefreshRecycleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public PullToRefreshRecycleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setmScrollListener(RecyclerView.OnScrollListener scrollListener) {
        this.mScrollListener = scrollListener;
    }



    @Override
    protected RecyclerView createRefreshableView(Context context, AttributeSet attrs) {
        mRecyclerView = new RecyclerView(context);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (null != mScrollListener) {
                    mScrollListener.onScrolled(recyclerView, dx, dy);
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
//                if (isScrollLoadEnabled() && hasMoreData()) {
//                    if (newState == RecyclerView.SCROLL_STATE_IDLE
//                            || newState == RecyclerView.SCROLL_STATE_SETTLING) {
//                        if (isReadyForPullUp()) {
//                            startLoading();
//                        }
//                    }
//                }

                if (null != mScrollListener) {
                    mScrollListener.onScrollStateChanged(recyclerView, newState);
                }
            }
        });
        return mRecyclerView;
    }



    @Override
    protected boolean isReadyForPullDown() {
        return isFirstItemVisible();
    }

    @Override
    protected boolean isReadyForPullUp() {
        return isLastItemVisible();
    }


    /**
     * 判断第一个child是否完全显示出来
     *
     * @return true完全显示出来，否则false
     */
    private boolean isFirstItemVisible() {
        RecyclerView.Adapter adapter = mRecyclerView.getAdapter();

        if (null == adapter || adapter.getItemCount() == 0) {
            return true;
        }

        int mostTop = (mRecyclerView.getChildCount() > 0) ? mRecyclerView.getChildAt(0).getTop() : 0;
        if (mostTop >= 0) {
            return true;
        }

        return false;
    }

    /**
     * 判断最后一个child是否完全显示出来
     *
     * @return true完全显示出来，否则false
     */
    private boolean isLastItemVisible() {
        final RecyclerView.Adapter adapter = mRecyclerView.getAdapter();

        if (null == adapter || adapter.getItemCount() == 0) {
            return true;
        }

        final int lastItemPosition = adapter.getItemCount() - 1;
        final int lastVisiblePosition = getLastVisiblePosition();

        /**
         * This check should really just be: lastVisiblePosition == lastItemPosition, but ListView
         * internally uses a FooterView which messes the positions up. For me we'll just subtract
         * one to account for it and rely on the inner condition which checks getBottom().
         */
        if (lastVisiblePosition >= lastItemPosition - 1) {
            final int childIndex = lastVisiblePosition - getFirstVisiblePosition();
            final int childCount = mRecyclerView.getChildCount();
            final int index = Math.min(childIndex, childCount - 1);
            final View lastVisibleChild = mRecyclerView.getChildAt(index);
            if (lastVisibleChild != null) {
                return lastVisibleChild.getBottom() <= mRecyclerView.getBottom();
            }
        }

        return false;
    }

    private int getLastVisiblePosition() {
        RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
        return ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
    }

    private int getFirstVisiblePosition() {
        RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
        return ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
    }
}
