package com.example.primerapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ApiReporte extends AppCompatActivity implements SensorEventListener{

    private final static float ACC = 30;
    private SensorManager sensor;
    private TextView lblAgitar;
    private  TextView lblReporte;
    private  TextView lblInfoAPI;
    private  TextView lblHead;
    private ImageView imagenCov;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api_reporte);
        SharedPreferences medicionesSensores = getSharedPreferences("sensores", Context.MODE_PRIVATE);
        medicionesSensores.getString("Lectura acelerometro eje X", "desconocido");
        medicionesSensores.getString("Lectura acelerometro eje Y", "desconocido");
        medicionesSensores.getString("Lectura acelerometro eje Z", "desconocido");
        medicionesSensores.getString("Listener acelerometro", "desconocido");
        sensor = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor.registerListener(this, sensor.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);


        lblAgitar = (TextView) findViewById(R.id.lblAgitar);
        lblReporte = (TextView) findViewById(R.id.lblReporte);
        lblInfoAPI = (TextView) findViewById(R.id.textoInfo);
        lblHead = (TextView) findViewById(R.id.textHead);
        imagenCov = (ImageView) findViewById(R.id.imagenCovid19);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        SharedPreferences medicionesSensores = getSharedPreferences("sensores", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = medicionesSensores.edit();
        editor.putString("Lectura acelerometro eje X", String.valueOf(event.values[0]));
        editor.putString("Lectura acelerometro eje Y", String.valueOf(event.values[1]));
        editor.putString("Lectura acelerometro eje Z", String.valueOf(event.values[2]));
        editor.commit();

        if ((Math.abs(event.values[0]) > ACC || Math.abs(event.values[1]) > ACC || Math.abs(event.values[2]) > ACC)) {
            lblAgitar.setVisibility(View.INVISIBLE);
            lblReporte.setVisibility(View.VISIBLE);
            lblInfoAPI.setVisibility(View.VISIBLE);
            lblHead.setVisibility(View.VISIBLE);
            imagenCov.setVisibility(View.VISIBLE);
            ReporteAPITask reporte = new ReporteAPITask();
            reporte.execute();

        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        sensor.registerListener(this, sensor.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        SharedPreferences medicionesSensores = getSharedPreferences("sensores", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = medicionesSensores.edit();
        editor.putString("Listener acelerometro", "Suscripto");
        editor.commit();
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensor.unregisterListener(this, sensor.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
        sensor.registerListener(this, sensor.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        SharedPreferences medicionesSensores = getSharedPreferences("sensores", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = medicionesSensores.edit();
        editor.putString("Listener acelerometro", "No suscripto");
        editor.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sensor.unregisterListener(this, sensor.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
        sensor.registerListener(this, sensor.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        SharedPreferences medicionesSensores = getSharedPreferences("sensores", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = medicionesSensores.edit();
        editor.putString("Listener acelerometro", "No suscripto");
        editor.commit();
    }


    // Asynctasks ---------------------------------------------------------------------------------
    public class ReporteAPITask extends android.os.AsyncTask<String, Void, String> {

        private final static int relleno = 0;
        private final static int lecturaJsonRec = 2;
        private final static int lecturaJsonBack = 3;
        private final static int lecturaJsonNew = 6;
        private final static int lecturaJsonTot = 7;
        private final static int lecturaJsonAct = 9;
        private final static int lecturaJsonCrtRe = 11;
        private final static int difMes = 1;
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected String doInBackground(String... params) {
            Date date = new Date();
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("America/Argentina"));
            cal.setTime(date);
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            String link = "https://covid-193.p.rapidapi.com/history?country=Argentina&day="+year+"-"+relleno+(month+difMes)+"-"+day;
            URL url = null;
            try {
                url = new URL(link);
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
                String nuevosCasos = cadenaCortada.substring(cadenaCortada.indexOf("new")+lecturaJsonNew,cadenaCortada.indexOf("active")-lecturaJsonBack);
                String activos = streamToString.substring(streamToString.indexOf("\"active") + lecturaJsonAct, streamToString.indexOf(",\"critical"));
                String criticos = streamToString.substring(streamToString.indexOf("\"critical") + lecturaJsonCrtRe, streamToString.indexOf(",\"recovered"));
                String recuperados = cadenaCortada.substring(cadenaCortada.indexOf("recovered")+ lecturaJsonCrtRe,cadenaCortada.indexOf("1M_pop")-lecturaJsonRec);
                String total = cadenaCortada.substring(cadenaCortada.indexOf("total")+ lecturaJsonTot,cadenaCortada.lastIndexOf("}"));
                String cadenaCortada2 = streamToString.substring(streamToString.indexOf("\"deaths"), streamToString.indexOf(",\"tests"));
                String muertes = cadenaCortada2.substring(cadenaCortada2.indexOf("new")+ lecturaJsonNew,cadenaCortada2.indexOf("1M_pop")-lecturaJsonBack);
                String totalMuertes = cadenaCortada2.substring(cadenaCortada2.indexOf("total")+ lecturaJsonTot,cadenaCortada2.lastIndexOf("}"));
                String reporte = "Nuevos Casos: "+ nuevosCasos + "\n\n"
                        + "Activos: "+"\t"+ activos + "\n\n"
                        + "Criticos: "+"\t"+ criticos + "\n\n"
                        + "Recuperados: "+"\t"+ recuperados + "\n\n"
                        + "Muertes: "+"\t"+ muertes + "\n\n"
                        + "Total Muertes: "+"\t"+ totalMuertes + "\n\n"
                        + "Total: "+"\t"+ total;
                return reporte;

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            lblReporte.setText(s);
        }
    }

}
