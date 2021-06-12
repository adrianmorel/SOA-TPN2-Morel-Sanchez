package com.example.primerapp;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class RequestAPIRest extends android.os.AsyncTask<String, Void, Integer> {


    @Override
    protected Integer doInBackground(String... params) {
        URL url = null;
        System.out.println(params.toString());
        try {
            url = new URL("http://so-unlam.net.ar/api/api/register");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection conn;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setConnectTimeout(5000);
            System.out.println(params.length);
            JSONObject json = new JSONObject();
            json.put("env", "TEST");
            json.put("name", params[0]);
            json.put("lastname", params[1]);
            json.put("dni", params[2]);
            json.put("email", params[3]);
            json.put("password", params[4]);
            json.put("commission", new Integer(2900));
            json.put("group", new Integer(11));
            System.out.println(json);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(json.toString());
            wr.flush();
            conn.connect();
            System.out.println(conn.getResponseCode() + " " + conn.getResponseMessage());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    protected int onPostExecute(int result){
        return result;
    }


}
