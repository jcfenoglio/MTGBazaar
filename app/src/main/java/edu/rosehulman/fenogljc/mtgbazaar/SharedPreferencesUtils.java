package edu.rosehulman.fenogljc.mtgbazaar;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by fenogljc on 10/2/1240.
 */
public class SharedPreferencesUtils {
    public static String getTradeBinder(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(Constants.PREFS, Activity.MODE_PRIVATE);
//        return prefs.getString(Constants.TRADE_BINDER_STORAGE_ID, "");
        return "-LCVcgBlVNqsAgCg7V52";
    }

    public static void setTradeBinder(Context context, String trade_binder_key) {
        SharedPreferences prefs = context.getSharedPreferences(Constants.PREFS, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Constants.TRADE_BINDER_STORAGE_ID, trade_binder_key);
        editor.commit();
    }

    public static void removeTradeBinder(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(Constants.PREFS, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(Constants.TRADE_BINDER_STORAGE_ID);
        editor.apply();
    }
}
