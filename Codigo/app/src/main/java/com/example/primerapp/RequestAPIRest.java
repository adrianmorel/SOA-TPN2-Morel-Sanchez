package com.example.primerapp;
import android.content.Context;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.widget.Toast;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class RequestAPIRest extends AsyncTask<String, Void, String> {

    private Context context;

    public RequestAPIRest(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {
        URL url = null;
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
            JSONObject json = new JSONObject();
            json.put("env", "TEST");
            json.put("name", "FERNANDO");
            json.put("lastname", "SANCHEZ");
            json.put("dni", new Integer(36822171));
            json.put("email", "SANCHEZ@GMAIL.COM");
            json.put("password", "ABCDFGHIJK");
            json.put("commission", new Integer(2900));
            json.put("group", new Integer(11));
            System.out.println(json);
            byte[] postDataBytes = json.toString().getBytes("UTF-8");
            conn.getOutputStream().write(postDataBytes);
            System.out.println(conn.getResponseCode() + " " + conn.getResponseMessage());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        Toast.makeText(context, result,
                Toast.LENGTH_LONG).show();
    }
}
