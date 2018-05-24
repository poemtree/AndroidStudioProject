package com.example.student.tcsphone.service;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LoginTask extends AsyncTask<String, Void, Boolean> {

    @Override
    protected Boolean doInBackground(String... strings) {
        boolean result;
        String id = strings[0];
        String pwd = strings[1];
        URL url = null;
        HttpURLConnection con = null;
        BufferedReader br = null;

        try {
            url = new URL(" 70.12.114.140/car/itsMe.do?id="+id+"&pwd="+pwd);
            con = (HttpURLConnection) url.openConnection();

            if(con != null) {
                con.setConnectTimeout(5000);
                con.setRequestMethod("GET");
                con.setRequestProperty("Accept","*/*");
                if(con.getResponseCode() != HttpURLConnection.HTTP_OK){
                    return false;
                }
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                if(br.readLine().toString().equals("1")) {
                    result = true;
                } else {
                    result = false;
                }
            }
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
        super.onPostExecute(aBoolean);
    }
}
