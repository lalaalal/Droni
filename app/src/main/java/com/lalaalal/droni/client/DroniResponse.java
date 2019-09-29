package com.lalaalal.droni.client;

import java.util.ArrayList;
import java.util.Arrays;

public class DroniResponse {
    public static final int TYPE_TEXT = 1;
    public static final int TYPE_FILE = 2;

    public static final String RESPONSE_SUCCEED = "SUCCEED!";
    public static final String RESPONSE_FAILED = "FAILED!";

    public String type;
    public ArrayList<String> stringData;
    public byte[] data;

    public DroniResponse() {
        type = "TEXT";
        stringData = new ArrayList<>();
    }

    public DroniResponse(String type, String request) {
        this.type = type;

        String[] responseStringData = request.split(":");
        stringData = new ArrayList<>(Arrays.asList(responseStringData));
    }

    public DroniResponse(String type, ArrayList<String> stringData) {
        this.type = type;
        this.stringData = stringData;
    }
}
