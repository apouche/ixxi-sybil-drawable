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

import com.example.sybildrawable.model.BusLine;
import com.example.sybildrawable.model.BusStop;

public class BusMapDrawable extends Drawable {
    private static int DEFAULT_BOX_LENGTH = 10;
    private static int DEFAULT_CIRCLE_RADIUS = 30;
    private Paint backgroundPaint = new Paint();
    private Paint boxPaint = new Paint();
    private Paint textPaint = new Paint();
    private Paint fillCirclePaint = new Paint();
    private Paint strokeCirclePaint = new Paint();

    public float getScale() {
        return scale;
    }

    private float scale = 8f;
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
        boxPaint.setColor(context.getColor(R.color.lightBlue));
        textPaint.setColor(context.getColor(R.color.darkBlue));
        textPaint.setTextSize(40);
        strokeCirclePaint.setColor(context.getColor(R.color.lightBlue));
        strokeCirclePaint.setStyle(Paint.Style.STROKE);
        strokeCirclePaint.setStrokeWidth(dp2px(2));
        fillCirclePaint.setColor(context.getColor(R.color.darkBlue));
        fillCirclePaint.setStyle(Paint.Style.FILL);
    }

    private float dp2px(int dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    @Override
    public void draw( @NonNull Canvas canvas) {
        drawableMap = new HashMap<>();

        canvas.drawRect(new Rect(0, 0, getBounds().width(), getBounds().height()), backgroundPaint);
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

    private void drawText(@NonNull Canvas canvas, BusStop stop)  {
        float x = stop.mnemoPosition.y*scale;
        float y = stop.mnemoPosition.x*scale;

        x = (int)x - (int)(textPaint.measureText(stop.mnemo)/2);
        y = (int) (y - ((textPaint.descent() + textPaint.ascent()) / 2)) ;
        //((textPaint.descent() + textPaint.ascent()) / 2) is the distance from the baseline to the center.

//        canvas.drawText("Hello", xPos, yPos, textPaint);
        canvas.drawText(stop.mnemo, x, y, textPaint);
    }
    private void drawBox(@NonNull Canvas canvas, BusStop stop)  {
        float x = stop.position.y*scale;
        float y = stop.position.x*scale;
        RectF rect = new RectF(x,y, x + dp2px(DEFAULT_BOX_LENGTH)*scale,y + dp2px(DEFAULT_BOX_LENGTH)*scale);
        ShapeDrawable square = new ShapeDrawable(new RectShape());
        square.getPaint().setColor(context.getColor(R.color.lightBlue));
        square.getPaint().setStyle(Paint.Style.FILL);
        square.setBounds((int)rect.left, (int)rect.top, (int)rect.right, (int)rect.bottom);
        square.draw(canvas);
        drawableMap.put(square, stop);
//        canvas.drawCircle(stop.position.y*scale, stop.position.x*scale, dp2px(10), strokeCirclePaint);
//        canvas.drawRect(rect, boxPaint);
        drawDot(canvas, stop);
    }

    private void drawDot(@NonNull Canvas canvas, BusStop stop)  {
        ShapeDrawable circle = new ShapeDrawable(new OvalShape());
        float x = stop.position.y*scale - dp2px(DEFAULT_CIRCLE_RADIUS)/2;
        float y = stop.position.x*scale - dp2px(DEFAULT_CIRCLE_RADIUS)/2;
        RectF rect = new RectF(x,y, x + dp2px(DEFAULT_CIRCLE_RADIUS),y + dp2px(DEFAULT_CIRCLE_RADIUS));
        circle.getPaint().setColor(context.getColor(R.color.lightBlue));
        circle.getPaint().setStyle(Paint.Style.FILL);
        circle.setBounds((int)rect.left, (int)rect.top, (int)rect.right, (int)rect.bottom);
        canvas.drawCircle(stop.position.y*scale, stop.position.x*scale, dp2px(DEFAULT_CIRCLE_RADIUS), strokeCirclePaint);
        canvas.drawCircle(stop.position.y*scale, stop.position.x*scale, dp2px(DEFAULT_CIRCLE_RADIUS), fillCirclePaint);
        drawableMap.put(circle, stop);
        circle.draw(canvas);
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

        // find the stop with "lowest" position
        // (remember x and y will be inverted during drawing)
        for (BusStop stop: line.boxes) {
            maxY = Math.max(stop.position.x, maxY);
            maxX = Math.max(stop.position.y, maxX);
            minX = Math.min(stop.position.y, minX);
            minY = Math.min(stop.position.x, minY);
        }

        // forces drawing pass
        invalidateSelf();
    }

    public void setScale(float scale) {
        this.scale = scale;

        // forces drawing pass
        invalidateSelf();
    }
}
