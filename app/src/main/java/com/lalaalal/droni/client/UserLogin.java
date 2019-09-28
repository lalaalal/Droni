package com.lalaalal.droni.client;

import java.math.BigInteger;
import java.security.MessageDigest;

public class UserLogin {
    private String userId;
    private String userPw;

    private String MD5Hash(String s) {
        MessageDigest m;
        try {
            m = MessageDigest.getInstance("MD5");
            m.update(s.getBytes(),0,s.length());

            StringBuilder result = new StringBuilder(new BigInteger(1, m.digest()).toString(16));
            while (result.length() < 32) {
                result.insert(0, "0");
            }

            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public UserLogin(String id, String pw) {
        userId = id;
        userPw = MD5Hash(pw);
    }

    public boolean requestLogin() {
        DroniRequest request = new DroniRequest("TEXT", "LOGIN", userId + ":" + userPw);
        DroniResponse response = new DroniResponse();
        try {
            Thread thread = new Thread(new DroniClient(request, response));
            thread.start();
            thread.join();
            return response.stringData.equals("SUCCEED!");

        } catch (InterruptedException e) {
            return false;
        }
    }
}
