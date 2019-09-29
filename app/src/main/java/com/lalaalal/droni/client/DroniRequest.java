package com.lalaalal.droni.client;

public class DroniRequest {
    public static final String TYPE_TEXT = "TEXT";
    public static final String TYPE_FILE = "FILE";

    public String type = null;
    public String command = null;
    public String stringData = null;
    public byte[] data = null;

    public DroniRequest(String type, String command, String stringData) {
        this.command = command;
        this.stringData = stringData;
    }

    public DroniRequest(String type, String command, byte[] date) {

    }
}
