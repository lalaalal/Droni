package com.lalaalal.droni.ui.user;

import com.lalaalal.droni.DroniException;

import java.util.ArrayList;

public class UserData {
    public String name;
    public String career;
    public String phone;
    public String drone;

    public UserData(ArrayList<String> userData) throws DroniException {
        if (userData.size() != 4)
            throw new DroniException("Wrong Param");
        name = userData.get(0);
        career = userData.get(1);
        phone = userData.get(2);
        drone = userData.get(3);
    }

    @Override
    public String toString() {
        return "이름 : " + name + "\n자격증 : " + career + "\n전화번호 : " + phone + "\n드론 : " + drone;
    }
}
