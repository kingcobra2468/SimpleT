package com.github.simplet;

import android.content.Context;
import android.content.SharedPreferences;

public class LocalStorage {

    private static Context context;
    private static SharedPreferences sharedPref;
    private static SharedPreferences.Editor editor;

    public static void setContext(Context in_context) {

        context = in_context;
        sharedPref = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        editor = sharedPref.edit();
    }


    public static void setInt(int resource_id, int input) {

        editor.putInt(context.getResources().getResourceEntryName(resource_id), input);
        editor.apply();
    }

    public static void setString(int resource_id, String input) {

        editor.putString(context.getResources().getResourceEntryName(resource_id), input);
        editor.apply();
    }

    public static int getInt(int resource_id, int default_value) {


        int value = sharedPref.getInt(context.getResources().getResourceEntryName(resource_id), default_value);
        return value;
    }

    public static String getString(int resource_id, String default_value) {

        String value = sharedPref.getString(context.getResources().getResourceEntryName(resource_id), default_value);
        return value;
    }
}
