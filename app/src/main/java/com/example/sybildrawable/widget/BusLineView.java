package com.example.sybildrawable.widget;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.sybildrawable.R;
import com.example.sybildrawable.drawable.BusMapDrawable;
import com.example.sybildrawable.model.BusLine;
import com.example.sybildrawable.model.BusStop;

import java.util.Map;

public class BusLineView extends FrameLayout {
    private BusMapDrawable busMapDrawable;
    private ImageView imageView;
    private ScrollView scrollView;

    public BusLineView(@NonNull Context context) {
        super(context);
        init();
    }

    public BusLineView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BusLineView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public BusLineView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.bus_line_view, this, true);
        busMapDrawable = new BusMapDrawable(getContext());
        imageView = findViewById(R.id.image_view);
        scrollView = findViewById(R.id.scroll_view);
        scrollView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    for(Map.Entry<Drawable, BusStop> entry : busMapDrawable.drawableMap.entrySet()) {
                        Rect drawableRect = entry.getKey().getBounds();
                        if (drawableRect.contains((int)event.getX(), (int)event.getY() + scrollView.getScrollY())) {
                            Toast.makeText(getContext(), entry.getValue().mnemo, Toast.LENGTH_LONG).show();
                            return false;
                        }
                    }
                }
                return false;
            }
        });
    }

    public void updateWith(BusLine connections) {
        busMapDrawable.updateWith(connections);
        imageView.setImageDrawable(busMapDrawable);
    }

    public void increaseScale() {
        busMapDrawable.setScale(busMapDrawable.getScale() + 0.5f);
    }

    public void decreaseScale() {
        busMapDrawable.setScale(busMapDrawable.getScale() - 0.5f);
    }
}
