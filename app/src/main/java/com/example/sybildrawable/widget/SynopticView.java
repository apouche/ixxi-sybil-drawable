package com.example.sybildrawable.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.sybildrawable.R;
import com.example.sybildrawable.drawable.SynopticDrawable;
import com.example.sybildrawable.model.BusLine;
import com.example.sybildrawable.model.BusStop;
import com.example.sybildrawable.model.Vector2f;
import com.example.sybildrawable.utils.Utils;

import java.util.Map;

public class SynopticView extends FrameLayout {
    private SynopticDrawable synopticDrawable;
    private ImageView imageView;
    private ScrollView scrollView;
    private static int HITBOX_OFFSET_DP = 20; // in dp
    private SelectionListener onSelectStopListener;

    public interface SelectionListener {

        void onClick(BusStop busStop, Vector2f coordinates);
    }
    public SynopticView(@NonNull Context context) {
        super(context);
        init();
    }

    public SynopticView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SynopticView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public SynopticView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.bus_line_view, this, true);
        synopticDrawable = new SynopticDrawable(getContext());
        imageView = findViewById(R.id.image_view);
        scrollView = findViewById(R.id.scroll_view);
        scrollView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    for(Map.Entry<Drawable, BusStop> entry : synopticDrawable.drawableMap.entrySet()) {
                        Rect drawableRect = new Rect();

                        // copy rect so that bounds do not get modified
                        drawableRect.set(entry.getKey().getBounds());

                        // for bus stops, increase the hit zone of the are defined by the drawable
                        if (entry.getValue().isStop()) {
                            drawableRect.left -= Utils.dp2px(getContext(), HITBOX_OFFSET_DP);
                            drawableRect.top -= Utils.dp2px(getContext(), HITBOX_OFFSET_DP);
                            drawableRect.right += Utils.dp2px(getContext(), HITBOX_OFFSET_DP);
                            drawableRect.bottom += Utils.dp2px(getContext(), HITBOX_OFFSET_DP);
                        }

                        float absoluteX = event.getX();
                        float absoluteY = event.getY() + scrollView.getScrollY();

                        if (drawableRect.contains((int)absoluteX, (int)absoluteY)) {
                            if (onSelectStopListener != null) {
                                onSelectStopListener.onClick(entry.getValue(), new Vector2f(absoluteX, absoluteY));
                            }
                            return false;
                        }
                    }
                }
                return false;
            }
        });
    }

    public void updateWith(BusLine connections) {
        synopticDrawable.updateWith(connections);
        imageView.setImageDrawable(synopticDrawable);
    }

    public void increaseScale() {
        synopticDrawable.setScale(synopticDrawable.getScale() + 0.5f);
        imageView.requestLayout();
        Log.d("Sybil", "" + scrollView.getChildAt(0).getHeight());
    }

    public void decreaseScale() {
        synopticDrawable.setScale(synopticDrawable.getScale() - 0.5f);
        imageView.requestLayout();
        Log.d("Sybil", "" + scrollView.getChildAt(0).getHeight());
    }

    public void setOnSelectStopListener(SelectionListener onSelectStopListener) {
        this.onSelectStopListener = onSelectStopListener;
    }
}
