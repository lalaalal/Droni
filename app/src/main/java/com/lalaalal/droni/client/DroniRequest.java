package com.lalaalal.droni.client;

import java.util.ArrayList;

public class DroniRequest {
    public static final int TYPE_TEXT = 1;
    public static final int TYPE_FILE = 2;

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
