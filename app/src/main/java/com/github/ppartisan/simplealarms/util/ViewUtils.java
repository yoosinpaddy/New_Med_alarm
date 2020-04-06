package com.github.ppartisan.simplealarms.util;

import android.annotation.TargetApi;
import android.content.res.Resources;
import android.os.Build;
import android.util.Log;
import android.widget.TimePicker;

import java.util.Calendar;

public final class ViewUtils {

    private static String TAG="ViewUtils";
    private ViewUtils() { throw new AssertionError(); }
    public static float dpToPx(float dp) {
        return dp*Resources.getSystem().getDisplayMetrics().density;
    }

    public static void setTimePickerTime(TimePicker picker, long time) {

        final Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);

        final int minutes = c.get(Calendar.MINUTE);
        final int hours = c.get(Calendar.HOUR_OF_DAY);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            picker.setMinute(minutes);
            picker.setHour(hours);
        } else {
            picker.setCurrentMinute(minutes);
            picker.setCurrentHour(hours);
        }
    }
    public static void setTimePickerTime2(TimePicker picker, long time) {

        final Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);

        final int minutes = c.get(Calendar.MINUTE);
        final int hours = getNext6Hour(c.get(Calendar.HOUR_OF_DAY));
        Log.e(TAG, "setTimePickerTime2: "+hours );

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            picker.setMinute(minutes);
            picker.setHour(hours);
            Log.e(TAG, "setTimePickerTime2: "+hours );
        } else {
            picker.setCurrentMinute(minutes);
            picker.setCurrentHour(hours);
            Log.e(TAG, "setTimePickerTime2: "+hours );
        }
    }
    public static void setTimePickerTime3(TimePicker picker, long time) {

        final Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);

        final int minutes = c.get(Calendar.MINUTE);
        final int hours = getNext12Hour(c.get(Calendar.HOUR_OF_DAY));
        Log.e(TAG, "setTimePickerTime3: "+hours );

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            picker.setMinute(minutes);
            picker.setHour(hours);
            Log.e(TAG, "setTimePickerTime3: "+hours );
        } else {
            picker.setCurrentMinute(minutes);
            picker.setCurrentHour(hours);
            Log.e(TAG, "setTimePickerTime3: "+hours );
        }
    }

    private static int getNext12Hour(int i) {
        if ((i+12)>24){
            return  24-i+12;
        }else
            return i+12;
    }
    private static int getNext6Hour(int i) {
        if ((i+6)>24){
            return  24-i+6;
        }else
            return i+6;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static int getTimePickerMinute(TimePicker picker) {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                ? picker.getMinute()
                : picker.getCurrentMinute();
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static int getTimePickerHour(TimePicker picker) {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                ? picker.getHour()
                : picker.getCurrentHour();
    }
    @TargetApi(Build.VERSION_CODES.M)
    public static int getTimePickerMinute2(TimePicker picker) {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                ? picker.getMinute()
                : picker.getCurrentMinute();
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static int getTimePickerHour2(TimePicker picker) {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                ? picker.getHour()
                : picker.getCurrentHour();
    }
    @TargetApi(Build.VERSION_CODES.M)
    public static int getTimePickerMinute3(TimePicker picker) {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                ? picker.getMinute()
                : picker.getCurrentMinute();
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static int getTimePickerHour3(TimePicker picker) {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                ? picker.getHour()
                : picker.getCurrentHour();
    }

}
