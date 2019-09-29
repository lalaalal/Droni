package com.lalaalal.droni;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesHandler {
    public static final String SHARED_PREF = "shared_preferences";
    public static final String LOGIN_SESSION = "login_session";
    public static final String FPV_USING = "fpv_using";

    public static final String SESSION_ID = "session_id";
    public static final String FPV_BAND = "fpv_band";
    public static final String FPV_CHANNEL = "fpv_channel";

    private Context context;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public static SharedPreferencesHandler getSharedPreferences(Context context) {
        return new SharedPreferencesHandler(context);
    }

    private SharedPreferencesHandler(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);;
        editor = sharedPreferences.edit();
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(LOGIN_SESSION, false);
    }

    public void logIn(String SessionId) {
        if (!isLoggedIn()) {
            editor.putBoolean(LOGIN_SESSION, true);
            editor.putString(SESSION_ID, SessionId);
            editor.commit();
        }
    }

    public void logOut() {
        editor.putBoolean(LOGIN_SESSION, false);
        editor.putString(SESSION_ID, "");
        editor.commit();
    }

    public void useFpv(int band, int channel) {
        if (!isFpvUsing()) {
            editor.putBoolean(FPV_USING, true);
            editor.putInt(FPV_BAND, band);
            editor.putInt(FPV_CHANNEL, channel);
            editor.commit();
        }
    }

    public void cancelFpv() {
        if (isFpvUsing()) {
            editor.putBoolean(FPV_USING, false);
            editor.putInt(FPV_BAND, -1);
            editor.putInt(FPV_CHANNEL, -1);
            editor.commit();
        }
    }

    public int getFpvBand() {
        return sharedPreferences.getInt(FPV_BAND, -1);
    }

    public int getFpvChannel() {
        return sharedPreferences.getInt(FPV_CHANNEL, -1);
    }

    public boolean isFpvUsing() {
        return sharedPreferences.getBoolean(FPV_USING, false);
    }

    public String getSessionId() throws LoginException {
        if (!isLoggedIn())
            throw new LoginException(LoginException.NOT_LOGGED_IN);
        return sharedPreferences.getString(SESSION_ID, "");
    }
}
