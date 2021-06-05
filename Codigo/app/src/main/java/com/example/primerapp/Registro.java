package com.example.primerapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Registro extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
    }

    Thread hilo = new Thread(new Runnable()
    {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void run()
        {
            URL url = null;
            try {
                url = new URL("http://so-unlam.net.ar/api/api/register");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            HttpURLConnection http = null;
            try {
                http = (HttpURLConnection)url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                http.setRequestMethod("POST");
            } catch (ProtocolException e) {
                e.printStackTrace();
            }
            http.setDoOutput(true);
            http.setRequestProperty("Content-Type", "application/json");

            String data = "{\n\"env\": \"TEST\",\n\"name\": \"FERNANDO\",\n\"lastname\": \"PRUEBASOA\",\n\"dni\": \"12345678\",\n\"email\": \"EMAIL@GMAIL.COM\",\n \"password\": \"ABCDEFGHI\",\n\"commission\": 2900,\n \"group\": 11\n}\n";

            byte[] out = data.getBytes(StandardCharsets.UTF_8);

            /*OutputStream stream = null;
            try {
                stream = http.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                stream.write(out);
            } catch (IOException e) {
                e.printStackTrace();
            }*/
            try {
                http.connect();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                System.out.println(http.getResponseCode() + " " + http.getResponseMessage());
            } catch (IOException e) {
                e.printStackTrace();
            }
            http.disconnect();

        }
    });

    public void registrar (View view){
        hilo.start();
    }

}
