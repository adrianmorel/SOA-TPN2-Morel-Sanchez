package com.example.primerapp;

import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.content.res.AssetManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Calculadora extends AppCompatActivity {

    private Spinner cantPersonas;
    private Spinner mascarilla;
    private Spinner ventana;
    private Spinner tiempoHablando;
    private Spinner volumenVoz;
    private String cantPersonasSelec;
    private String mascarillaSelec;
    private String ventanaSelec;
    private String tiempoHablandoSelec;
    private String volumenVozSelec;
    private TextView lblProba;
    private TextView lblPersonasInfectadas;
    private String proba;
    private String cantPersonasInfectadas;
    String token;
    String token_refresh;
    int year, month, day;
    Date objDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculadora);
        token = getIntent().getStringExtra("token");
        token_refresh = getIntent().getStringExtra("token_refresh");

        cantPersonas = findViewById(R.id.comboPersonas);
        ArrayAdapter<CharSequence> adapterPersonas = ArrayAdapter.createFromResource(this, R.array.personas, android.R.layout.simple_spinner_dropdown_item);
        cantPersonas.setAdapter(adapterPersonas);

        mascarilla = findViewById(R.id.comboMascarillas);
        ArrayAdapter<CharSequence> adapterMascarilla = ArrayAdapter.createFromResource(this,R.array.mascarillas, android.R.layout.simple_spinner_dropdown_item);
        mascarilla.setAdapter(adapterMascarilla);

        ventana = findViewById(R.id.comboVentanas);
        ArrayAdapter<CharSequence> adapterVentana = ArrayAdapter.createFromResource(this, R.array.ventanas, android.R.layout.simple_spinner_dropdown_item);
        ventana.setAdapter(adapterVentana);

        tiempoHablando = findViewById(R.id.comboTiempoHablando);
        ArrayAdapter<CharSequence> adapterTiempo = ArrayAdapter.createFromResource(this,R.array.tiempoHablando, android.R.layout.simple_spinner_dropdown_item);
        tiempoHablando.setAdapter(adapterTiempo);

        volumenVoz = findViewById(R.id.comboVolumenDeVoz);
        ArrayAdapter<CharSequence> adapterVolumen = ArrayAdapter.createFromResource(this,R.array.volumenDeVoz, android.R.layout.simple_spinner_dropdown_item);
        volumenVoz.setAdapter(adapterVolumen);

        lblProba = findViewById(R.id.lblProba);
        lblPersonasInfectadas = findViewById(R.id.lblPersonasInfectadas);

        }

        public void calcularProbabilidad(View view) throws IOException {

            objDate = new Date();
            Date date = new Date(); // your date
            // Choose time zone in which you want to interpret your Date
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));
            cal.setTime(date);
            year = cal.get(Calendar.YEAR);
            month = cal.get(Calendar.MONTH);
            day = cal.get(Calendar.DAY_OF_MONTH);
            RegistrarEvento registro = new RegistrarEvento();
            registro.execute("Consulta de calculadora de contagio", "Fecha y Hora: "+ day + "-" + month + "-" + year +
                    " " + objDate.getHours() + ":"+ objDate.getMinutes(), token, token_refresh);

            cantPersonasSelec = cantPersonas.getSelectedItem().toString();
            mascarillaSelec = mascarilla.getSelectedItem().toString();
            ventanaSelec = ventana.getSelectedItem().toString();
            tiempoHablandoSelec = tiempoHablando.getSelectedItem().toString();
            volumenVozSelec = volumenVoz.getSelectedItem().toString();


            List<String> listado = new ArrayList<String>();
            String linea;

            InputStream is = this.getResources().openRawResource(R.raw.datos);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            if(is != null){
                while ((linea = br.readLine()) != null) {

                    if  ( ! (linea.split(";")[0].equals(cantPersonasSelec)))
                        continue;

                    if ( ! (linea.split(";")[1].equals(mascarillaSelec)))
                        continue;

                    if( ! (linea.split(";")[2].equals(ventanaSelec)))
                        continue;

                    if ( ! (linea.split(";")[3].equals(tiempoHablandoSelec)))
                        continue;

                    if ( ! (linea.split(";")[4].equals(volumenVozSelec)))
                        continue;

                    System.out.println(linea.split(";")[0]);
                    System.out.println(linea.split(";")[1]);
                    System.out.println(linea.split(";")[2]);
                    System.out.println(linea.split(";")[3]);
                    System.out.println(linea.split(";")[4]);

                    proba = linea.split(";")[5];
                    cantPersonasInfectadas = linea.split(";")[6];

                    lblProba.setText(proba + "% de probabilidad de ser infectado");
                    lblPersonasInfectadas.setText(cantPersonasInfectadas + " personas infectadas");

                    break;
                }
            }
    }


    /////////////////////////////////////////////////////////////////////////////////////////
    public class RegistrarEvento extends android.os.AsyncTask<String, Void, Integer> {

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
                System.out.println(json);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write(json.toString());
                wr.flush();
                conn.connect();
                int respCode = conn.getResponseCode();
                String respMessage = conn.getResponseMessage();
                System.out.println(respCode + " " + respMessage);

                InputStream response = new BufferedInputStream(conn.getInputStream());
                InputStreamReader inputStreamReader = new InputStreamReader(response);
                Stream<String> streamOfString= new BufferedReader(inputStreamReader).lines();
                String streamToString = streamOfString.collect(Collectors.joining());
                System.out.println(streamToString);

                return respCode;

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Integer respuesta) {
            if(respuesta == 401){
                RefrescarToken actualizarToken = new RefrescarToken();
                actualizarToken.execute();
            }
        }
    }

    /////////////////////////////////////////////////////////////////////////////////
    public class RefrescarToken extends android.os.AsyncTask<String, Void, Integer> {

        @SuppressLint("WrongThread")
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected Integer doInBackground(String... strings) {
            URL url = null;
            try {
                url = new URL("http://so-unlam.net.ar/api/api/refresh");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            HttpURLConnection conn;
            try {
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("PUT");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Authorization", "Bearer " + token_refresh);
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setConnectTimeout(5000);
                conn.connect();
                int respCode = conn.getResponseCode();
                String respMessage = conn.getResponseMessage();
                System.out.println(respCode + " " + respMessage);
                if(respCode == 200){
                    //Obteniendo el token
                    JsonReader reader = new JsonReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                    reader.beginObject();
                    reader.nextName();
                    Boolean success = reader.nextBoolean();
                    reader.nextName();
                    token = reader.nextString();
                    reader.nextName();
                    token_refresh = reader.nextString();
                    System.out.println("success: "+ success);
                    System.out.println("token: "+ token);
                    System.out.println("token_refresh: "+ token_refresh);
                    RegistrarEvento registro = new RegistrarEvento();
                    registro.execute("Consulta de reporte diario", "Fecha y Hora: "+ day + "-" + month + "-" + year +
                            " " + objDate.getHours() + ":"+ objDate.getMinutes(), token, token_refresh);
                }

                InputStream response = new BufferedInputStream(conn.getInputStream());
                InputStreamReader inputStreamReader = new InputStreamReader(response);
                Stream<String> streamOfString= new BufferedReader(inputStreamReader).lines();
                String streamToString = streamOfString.collect(Collectors.joining());
                System.out.println(streamToString);

                return respCode;

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

}


