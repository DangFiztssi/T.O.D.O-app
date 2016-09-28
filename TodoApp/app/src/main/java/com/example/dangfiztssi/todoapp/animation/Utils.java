package com.example.dangfiztssi.todoapp.animation;

/**
 * Created by DangF on 09/26/16.
 */

public class Utils {
    public static double clamp(double value, double low, double high){
        return Math.min(Math.max(value, low), high);
    }

    public static double mapValueFromRangeToRange(double value, double fromLow, double fromHigh, double toLow, double toHigh){
        return toLow + ((value - fromLow)/ (fromHigh - fromLow) * (toHigh - toLow));
    }
}
