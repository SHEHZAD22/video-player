package com.shehzad.gifsvideo.preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class ManagePreferences {

    public static final String LOGIN_STATE = "login_state";

    private SharedPreferences preferences;

    public ManagePreferences(Context context) {
        preferences = context.getSharedPreferences("gifs-video", Context.MODE_PRIVATE);
    }

    public boolean isLoggedIn() {
        return preferences.getBoolean(LOGIN_STATE, false);
    }

    public void setLoggedIn(boolean loggedIn) {
        preferences.edit().putBoolean(LOGIN_STATE, loggedIn).apply();
    }
}
