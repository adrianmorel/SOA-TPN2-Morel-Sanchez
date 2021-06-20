package com.example.primerapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MenuApp extends AppCompatActivity {

    String token;
    String token_refresh;
    private static final int difMes = 1;
    private static final int escalaBateria = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_app);
        token = getIntent().getStringExtra("token");
        token_refresh = getIntent().getStringExtra("token_refresh");
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = registerReceiver(null, ifilter);
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        float battery = (level / (float)scale)*escalaBateria;
        new SimpleDialog().show(getSupportFragmentManager(), String.valueOf(battery));
        Date objDate = new Date();
        Date date = new Date(); // your date
        // Choose time zone in which you want to interpret your Date
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("America/Argentina"));
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        EventTask registrarEvento = new EventTask();
        registrarEvento.execute("Login correcto", "Fecha y Hora: "+ day + "-" + (month+difMes) + "-" + year +
                                " " + objDate.getHours() + ":"+ objDate.getMinutes(), token);
    }

    public void irACalculadora(View view) {
        Intent intent = new Intent(this, Calculadora.class);
        intent.putExtra("token", token);
        intent.putExtra("token_refresh", token_refresh);
        startActivity(intent);
    }

    public void irAReporteDiario(View view) {
        Intent intent = new Intent(this, ApiReporte.class);
        startActivity(intent);
    }

    public void irASensores(View view) {
        Intent intent = new Intent(this, EventosSensor.class);
        startActivity(intent);
    }

    public void irATips(View view) {
        Intent intent = new Intent(this, Tip.class);
        startActivity(intent);
    }

    // Asynctask ---------------------------------------------------------------------------------
    public class EventTask extends android.os.AsyncTask<String, Void, Integer> {

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected Integer doInBackground(String... params) {
            URL url = null;
            try {
                url = new URL("http://so-unlam.net.ar/api/api/event");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            HttpURLConnection conn;
            try {
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Authorization", "Bearer " + token);
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setConnectTimeout(5000);
                JSONObject json = new JSONObject();
                json.put("env", "TEST");
                json.put("type_events", params[0]);
                json.put("description", params[1]);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write(json.toString());
                wr.flush();
                conn.connect();
                int respCode = conn.getResponseCode();
                String respMessage = conn.getResponseMessage();

                return respCode;

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

    }
}
