package com.lalaalal.droni.client;

public class DroniResponse {
    public static final int TYPE_TEXT = 1;
    public static final int TYPE_FILE = 2;

    public String type = null;
    public String stringData = null;
    public byte[] data = null;

    DroniResponse() {

    }

    DroniResponse(String type, String stringData) {
        this.type = type;
        this.stringData = stringData;
    }
}
