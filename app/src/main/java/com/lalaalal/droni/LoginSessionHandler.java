package com.lalaalal.droni;

import android.content.Context;
import android.content.SharedPreferences;

public class LoginSessionHandler {
    public static final String LOGIN_SESSION = "login_session";
    public static final String SESSION_ID = "session_id";

    private Context context;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public static LoginSessionHandler getLoginSession(Context context) {
        return new LoginSessionHandler(context);
    }

    private LoginSessionHandler(Context context) {
        sharedPreferences = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);;
        editor = sharedPreferences.edit();
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(LOGIN_SESSION, false);
    }

    public void logIn(String SessionId) {
        if (!isLoggedIn()) {
            editor.clear();
            editor.putBoolean(LOGIN_SESSION, true);
            editor.putString(SESSION_ID, SessionId);
            editor.commit();
        }
    }

    public void logOut() {
        editor.clear();
        editor.putBoolean(LOGIN_SESSION, false);
        editor.putString(SESSION_ID, null);
        editor.commit();
    }


}
