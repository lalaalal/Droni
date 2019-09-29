package com.lalaalal.droni.client;

import com.lalaalal.droni.DroniException;

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

    public boolean requestLogin() throws DroniException {
        try {
            DroniRequest request = new DroniRequest("TEXT", "LOGIN", userId + ":" + userPw);
            DroniClient droniClient = new DroniClient(request);
            Thread thread = new Thread(droniClient);
            thread.start();
            thread.join();

            DroniResponse response = droniClient.getResponse();
            if (response == null)
                throw new DroniException(DroniException.NO_RESPONSE);
            String responseString = response.stringData.get(0);
            return responseString.equals("SUCCEED!");
        } catch (InterruptedException e) {
            return false;
        }
    }
}
