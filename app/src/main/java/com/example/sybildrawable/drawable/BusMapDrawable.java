package com.example.sybildrawable.drawable;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RectShape;
import android.util.TypedValue;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.sybildrawable.R;

import java.util.HashMap;
import java.util.List;

import com.example.sybildrawable.model. BusLine;
import com.example.sybildrawable.model.Vector2f;
import com.example.sybildrawable.model.BusStop;

public class BusMapDrawable extends Drawable {
    private static int DEFAULT_BOX_LENGTH = 10;
    private static int DEFAULT_LINE_WIDTH = 8;
    private static int DEFAULT_CIRCLE_RADIUS = 20;
    private Paint backgroundPaint = new Paint();
    private Paint textPaint = new Paint();
    private Paint linePaint = new Paint();

    public float getScale() {
        return scale;
    }

    private float scale = 6f;
    private float maxY = 0;
    private float maxX = 0;
    private float minX = Float.MAX_VALUE;
    private float minY = Float.MAX_VALUE;
    private Context context;
    private BusLine line;

    public HashMap<Drawable, BusStop> drawableMap;

    public BusMapDrawable(Context context) {
        this.context = context;
        backgroundPaint.setColor(context.getColor(R.color.normalBlue));
        textPaint.setColor(context.getColor(R.color.darkBlue));
        textPaint.setTextSize(40);
        linePaint.setColor(context.getColor(R.color.colorAccent));
        linePaint.setStrokeWidth(dp2px(DEFAULT_LINE_WIDTH));
        linePaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    private float dp2px(int dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    @Override
    public void draw( @NonNull Canvas canvas) {
        drawableMap = new HashMap<>();

        canvas.drawRect(new Rect(0, 0, getBounds().width(), getBounds().height()), backgroundPaint);

        // start by drawing lines so that they appear in the background
        for (List<Vector2f> positions: line.itineraries) {
            drawLine(canvas, positions);
        }

        for (BusStop stop: line.boxes) {
            if (stop.type.equals("ARRET")) {
                drawDot(canvas, stop);
            }
            else {
                drawBox(canvas, stop);
            }
            if (stop.mnemoPosition != null && stop.mnemoPosition.x != 0 && stop.mnemoPosition.y != 0 )
                drawText(canvas, stop);
        }

    }

    private void drawLine(Canvas canvas, List<Vector2f> positions) {
        for (int i = 0; i < positions.size(); ++i) {
            if (i == 0)
                continue;

            Vector2f lastPosition = positions.get(i-1);
            Vector2f currentPosition = positions.get(i);

            float lastX = lastPosition.y*scale + getXCenterOffset(canvas);;
            float lastY = lastPosition.x*scale - minY*scale;
            float currentX = currentPosition.y*scale + getXCenterOffset(canvas);;
            float currentY = currentPosition.x*scale - minY*scale;

            canvas.drawLine(lastX, lastY, currentX, currentY, linePaint);
        }
    }

    private void drawText(@NonNull Canvas canvas, BusStop stop)  {
        float x = stop.mnemoPosition.y*scale + getXCenterOffset(canvas);;
        float y = stop.mnemoPosition.x*scale - minY*scale;

        x = (int)x - (int)(textPaint.measureText(stop.mnemo)/2);
        y = (int) (y - ((textPaint.descent() + textPaint.ascent()) / 2)) ;

        canvas.drawText(stop.mnemo, x, y, textPaint);
    }
    private void drawBox(@NonNull Canvas canvas, BusStop stop)  {
        float x = stop.position.y*scale - dp2px(DEFAULT_BOX_LENGTH)*scale*0.5f + getXCenterOffset(canvas);
        float y = stop.position.x*scale - minY*scale;

        RectF rect = new RectF(x,y, x + dp2px(DEFAULT_BOX_LENGTH)*scale,y + dp2px(DEFAULT_BOX_LENGTH)*scale);
        ShapeDrawable square = new ShapeDrawable(new RectShape());
        square.getPaint().setColor(context.getColor(R.color.lightBlue));
        square.getPaint().setStyle(Paint.Style.FILL);
        square.setBounds((int)rect.left, (int)rect.top, (int)rect.right, (int)rect.bottom);
        square.draw(canvas);
        drawableMap.put(square, stop);
    }

    private void drawDot(@NonNull Canvas canvas, BusStop stop)  {
        ShapeDrawable circle = new ShapeDrawable(new OvalShape());
        float x = stop.position.y*scale - dp2px(DEFAULT_CIRCLE_RADIUS)/2 + getXCenterOffset(canvas);
        float y = stop.position.x*scale - dp2px(DEFAULT_CIRCLE_RADIUS)/2 - minY*scale;
        RectF rect = new RectF(x,y, x + dp2px(DEFAULT_CIRCLE_RADIUS),y + dp2px(DEFAULT_CIRCLE_RADIUS));
        circle.getPaint().setColor(context.getColor(R.color.lightBlue));
        circle.getPaint().setStyle(Paint.Style.FILL);
        circle.setBounds((int)rect.left, (int)rect.top, (int)rect.right, (int)rect.bottom);
        drawableMap.put(circle, stop);
        circle.draw(canvas);
    }

    private float getXCenterOffset(Canvas canvas) {
        return (canvas.getWidth()*0.5f-(maxX-minX)*scale);
    }

    @Override
    public int getIntrinsicHeight() {
        return Math.round(maxY *scale);
    }

    @Override
    public int getIntrinsicWidth() {
        return Math.round(maxX *scale);
    }

    @Override
    public void setAlpha(int i) {

    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }

    public void updateWith(BusLine line) {
        this.line = line;

        // update min/max bounds
        findMinMaxBounds();

        // forces drawing pass
        invalidateSelf();
    }

    public void setScale(float scale) {
        this.scale = scale;

        // forces drawing pass
        invalidateSelf();
    }

    private void findMinMaxBounds() {
        // find the stop with "lowest" and "highest" positions
        // (remember x and y will be inverted during drawing)
        for (BusStop stop: line.boxes) {
            maxY = Math.max(stop.position.x, maxY);
            maxX = Math.max(stop.position.y, maxX);
            minX = Math.min(stop.position.y, minX);
            minY = Math.min(stop.position.x, minY);
        }
    }
}
