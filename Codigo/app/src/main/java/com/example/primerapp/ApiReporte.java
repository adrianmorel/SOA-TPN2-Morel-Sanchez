package com.example.primerapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.View;
import android.widget.TextView;

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
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ApiReporte extends AppCompatActivity implements SensorEventListener{

    private final static float ACC = 30;
    SensorManager sensor;
    private TextView lblAgitar;
    private  TextView lblReporte;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api_reporte);

        sensor = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor.registerListener(this, sensor.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_UI);
        lblAgitar = (TextView) findViewById(R.id.lblAgitar);
        lblReporte = (TextView) findViewById(R.id.lblReporte);

        //ReporteAPITask reporte = new ReporteAPITask();
        //reporte.execute();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        String txt = "\n\nSensor: ";

        if ((Math.abs(event.values[0]) > ACC || Math.abs(event.values[1]) > ACC || Math.abs(event.values[2]) > ACC))
        {
            txt += "\n" + event.values[0];
            System.out.println(txt);
            txt += "\n" + event.values[1];
            System.out.println(txt);
            txt += "\n" + event.values[2];
            System.out.println(txt);
            lblAgitar.setVisibility(View.INVISIBLE);
            lblReporte.setVisibility(View.VISIBLE);
            lblReporte.setText("¡Bien chamaco!");

        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        sensor.registerListener(this, sensor.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensor.unregisterListener(this, sensor.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sensor.unregisterListener(this, sensor.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
    }

    // Asynctask ---------------------------------------------------------------------------------
    public class ReporteAPITask extends android.os.AsyncTask<String, Void, Integer> {

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected Integer doInBackground(String... params) {
            URL url = null;
            try {
                url = new URL("https://covid-193.p.rapidapi.com/history?country=Argentina&day=2021-06-17");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            HttpURLConnection conn;
            try {
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("x-rapidapi-key", "f962429069msh4509b9c676b5f63p10a40bjsn73daa23ab641");
                conn.setRequestProperty("x-rapidapi-host", "covid-193.p.rapidapi.com");

                int respCode = conn.getResponseCode();
                String respMessage = conn.getResponseMessage();
                System.out.println(respCode + " " + respMessage);

                InputStream response = new BufferedInputStream(conn.getInputStream());
                InputStreamReader inputStreamReader = new InputStreamReader(response);
                Stream<String> streamOfString= new BufferedReader(inputStreamReader).lines();
                String streamToString = streamOfString.collect(Collectors.joining());
                System.out.println(streamToString);
                String cadenaCortada = streamToString.substring(streamToString.indexOf("\"cases"), streamToString.indexOf("\"deaths"));
                System.out.println(cadenaCortada);
                String nuevosCasos = cadenaCortada.substring(cadenaCortada.indexOf("new")+6,cadenaCortada.indexOf("active")-3);
                System.out.println("Nuevos Casos: "+ nuevosCasos);
                String activos = streamToString.substring(streamToString.indexOf("\"active") + 9, streamToString.indexOf(",\"critical"));
                System.out.println("Activos: "+ activos);
                String criticos = streamToString.substring(streamToString.indexOf("\"critical") + 11, streamToString.indexOf(",\"recovered"));
                System.out.println("Criticos: "+ criticos);
                String recuperados = cadenaCortada.substring(cadenaCortada.indexOf("recovered")+ 11,cadenaCortada.indexOf("1M_pop")-2);
                System.out.println("Recuperados: "+ recuperados);
                String total = cadenaCortada.substring(cadenaCortada.indexOf("total")+ 6,cadenaCortada.lastIndexOf("}"));
                System.out.println("Total: "+ total);
                String cadenaCortada2 = streamToString.substring(streamToString.indexOf("\"deaths"), streamToString.indexOf(",\"tests"));
                String muertes = cadenaCortada2.substring(cadenaCortada2.indexOf("new")+ 6,cadenaCortada2.indexOf("1M_pop")-3);
                System.out.println("Muertes: "+ muertes);
                String totalMuertes = cadenaCortada2.substring(cadenaCortada2.indexOf("total")+ 6,cadenaCortada2.lastIndexOf("}"));
                System.out.println("Total Muertes: "+ totalMuertes);
                return respCode;

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

    }

}
