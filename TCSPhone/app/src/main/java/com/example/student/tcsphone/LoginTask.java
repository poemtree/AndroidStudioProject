package com.example.student.tcsphone;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LoginTask extends AsyncTask<String, Void, Boolean> {

    private Handler handler;
    private final String tag = "------LoginTask------";

    public LoginTask(Handler handler) {
        this.handler = handler;
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        boolean result = false;
        String id = strings[0];
        String pwd = strings[1];
        URL url = null;
        HttpURLConnection con = null;
        BufferedReader br = null;

        try {
            Log.e(tag, "LoginTask run");
            url = new URL("http://70.12.114.140/car/itsMe.do?id="+id+"&pwd="+pwd);
            con = (HttpURLConnection) url.openConnection();
            Log.e(tag, "Ready connection");
            if(con != null) {
                con.setConnectTimeout(5000);
                con.setRequestMethod("GET");
                con.setRequestProperty("Accept","*/*");
                if(con.getResponseCode() != HttpURLConnection.HTTP_OK){
                    return result;
                }
                Log.e(tag, "Succeed connection");
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                if(br.readLine().toString().equals("1")) {
                    result = true;
                }
            }
            Log.e(tag, "LoginTask done");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            con.disconnect();
        }
        return result;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("result", aBoolean);
        Message message = new Message();
        message.setData(bundle);
        handler.sendMessage(message);
    }
}
