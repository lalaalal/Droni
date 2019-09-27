package com.lalaalal.droni.client;

public class UserLogin {
    private String userId;
    private String userPw;

    public UserLogin(String id, String pw) {
        userId = id;
        userPw = pw;
    }

    public boolean requestLogin() {
        return true;
    }
}
