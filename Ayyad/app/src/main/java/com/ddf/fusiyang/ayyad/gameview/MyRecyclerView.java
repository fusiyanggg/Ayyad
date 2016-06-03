package com.ddf.fusiyang.ayyad.gameview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by fusiyang on 2016/6/2.
 */
public class MyRecyclerView extends RecyclerView {

    private View mCurrentView;

    private OnItemScrollChangeListener mItemScrollChangeListener;

    public void setOnItemScrollChangeListener(
            OnItemScrollChangeListener mItemScrollChangeListener) {

        this.mItemScrollChangeListener = mItemScrollChangeListener;

    }

    public interface OnItemScrollChangeListener {

        void onChange(View view, int position);

    }

    public MyRecyclerView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.setOnScrollListener(new mOnScrollListener());
    }


    //实时滚动的回掉接口


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        mCurrentView = getChildAt(0);

        if (mItemScrollChangeListener != null) {
            mItemScrollChangeListener.onChange(mCurrentView,
                    getChildLayoutPosition(mCurrentView));
        }
    }


    class mOnScrollListener extends OnScrollListener {

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            View newView = getChildAt(0);
            if (mItemScrollChangeListener != null) {
                if (newView != null && newView != mCurrentView) {

                    mCurrentView = newView;
                    mItemScrollChangeListener.onChange(mCurrentView,
                            getChildPosition(mCurrentView));

                }

            }
        }



        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }
    }

}