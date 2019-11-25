package com.example.sybildrawable.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class SynopticScrollView extends ScrollView {
    private boolean isScrollingEnabled = true;

    public SynopticScrollView(Context context) {
        super(context);
    }

    public SynopticScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SynopticScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SynopticScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public boolean isScrollingEnabled() {
        return isScrollingEnabled;
    }

    public void setScrollingEnabled(boolean scrollingEnabled) {
        isScrollingEnabled = scrollingEnabled;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!isScrollingEnabled)
            return false;

        return super.onTouchEvent(ev);
    }
}
