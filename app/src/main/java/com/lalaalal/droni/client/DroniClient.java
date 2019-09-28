package com.lalaalal.droni.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class DroniClient implements Runnable {
    private static final String SERVER_ADDRESS = "192.168.232.1";
    private static final int SERVER_PORT = 51101;

    private Socket server;
    private Scanner in;
    private PrintWriter out;

    private final DroniRequest request;
    private DroniResponse response;

    public DroniClient(DroniRequest request) {
        this.request = request;
    }

    @Override
    public void run() {
        try {
            server = new Socket("192.168.55.2", SERVER_PORT);
            in = new Scanner(server.getInputStream());
            out = new PrintWriter(server.getOutputStream(), true);

            sendText();
            receiveText();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendText() {
        out.println(request.type);
        out.println(request.command);
        out.println(request.stringData);
    }

    private void receiveText() {
        String type = in.nextLine();
        String responseString = in.nextLine();

        response = new DroniResponse(type, responseString);
    }

    public DroniResponse getResponse() {
        return response;
    }
}
