package com.lalaalal.droni.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpsClient implements Runnable {
    private String targetUrl;
    private String result;

    public HttpsClient(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    @Override
    public void run() {
        try {
            URL url = new URL(targetUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            InputStreamReader in = new InputStreamReader(connection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(in);

            StringBuilder input = new StringBuilder();
            String buf;

            while ((buf = bufferedReader.readLine()) != null) {
                input.append(buf);
            }

            bufferedReader.close();
            result = input.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getResult() {
        return result;
    }
}
