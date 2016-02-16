package com.mathilde.appmessage.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author mathilde on 16/02/16.
 */
public class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {

    GestureDetector mGestureDetector;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    public RecyclerItemClickListener(Context c, OnItemClickListener listener) {
        mListener = listener;
        mGestureDetector = new GestureDetector(c, new GestureDetector.SimpleOnGestureListener() {
            @Override public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View child = rv.findChildViewUnder(e.getX(), e.getY());
        if(child != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
            mListener.onItemClick(child, rv.getChildAdapterPosition(child));
            return true;
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
