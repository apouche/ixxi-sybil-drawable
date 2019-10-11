package com.example.sybildrawable.utils;

import android.content.Context;
import android.util.TypedValue;

public class Utils {
    public static float dp2px(Context context, int dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }
}
