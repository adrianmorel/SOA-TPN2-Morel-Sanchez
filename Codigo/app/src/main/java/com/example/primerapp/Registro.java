package com.example.primerapp;


import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Registro extends AppCompatActivity {

     TextView name;
     TextView surname;
     TextView email;
     TextView pw;
     TextView repass;
     TextView dni;
     Button botonConf;
     ProgressBar pb;
     Integer internet;
     private static final int ok = 200;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
    }

    @SuppressLint("ResourceType")
    public void registrar(View view) throws IOException, JSONException, InterruptedException {

        RequestAPIRest hilo = new RequestAPIRest();

        name = (TextView) findViewById(R.id.inNombre);
        surname = (TextView) findViewById(R.id.inApellido);
        dni = (TextView) findViewById(R.id.inDNI);
        email = (TextView) findViewById(R.id.inCorreo);
        pw = (TextView) findViewById(R.id.inPass);
        repass = (TextView) findViewById(R.id.inRepPass);
        botonConf = (Button) findViewById(R.id.btnConfirmarRegistro);
        pb = (ProgressBar) findViewById(R.id.progressBar);

        if(name.length() == 0){
            Toast.makeText(getApplicationContext(), "El Nombre no puede estar vacio", Toast.LENGTH_LONG).show();
            return;
        }

        if(surname.length() == 0){
            Toast.makeText(getApplicationContext(), "El Apellido no puede estar vacio", Toast.LENGTH_LONG).show();
            return;
        }

        if(dni.length() == 0){
            Toast.makeText(getApplicationContext(), "El DNI no puede estar vacio", Toast.LENGTH_LONG).show();
            return;
        }

        if(email.length() == 0){
            Toast.makeText(getApplicationContext(), "El email no puede estar vacio", Toast.LENGTH_LONG).show();
            return;
        }

        if(pw.length() < 8){
            Toast.makeText(getApplicationContext(), "La contraseña debe tener 8 caracteres como minimo", Toast.LENGTH_LONG).show();
            return;
        }
        System.out.println(pw.getText().toString() + "VS " + repass.getText().toString());

        if(!pw.getText().toString().equals(repass.getText().toString())){
            Toast.makeText(getApplicationContext(), "Las contraseñas no coinciden", Toast.LENGTH_LONG).show();
            return;
        }

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            internet = ok;
        } else {
            Toast.makeText(getApplicationContext(), "No hay conexion a internet, revise su estado de red e intentelo nuevamente", Toast.LENGTH_LONG).show();
            return;
        }

        Toast.makeText(getApplicationContext(), "Verificando datos del servidor..", Toast.LENGTH_LONG).show();
        pb.setVisibility(View.VISIBLE);
        botonConf.setVisibility(View.INVISIBLE);
        hilo.execute(name.getText().toString(), surname.getText().toString(), dni.getText().toString(), email.getText().toString(), pw.getText().toString());

    }


    public class RequestAPIRest extends android.os.AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
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
                conn.setDoInput(true);
                conn.setConnectTimeout(5000);
                JSONObject json = new JSONObject();
                json.put("env", "PROD");
                json.put("name", params[0]);
                json.put("lastname", params[1]);
                json.put("dni", params[2]);
                json.put("email", params[3]);
                json.put("password", params[4]);
                json.put("commission", new Integer(2900));
                json.put("group", new Integer(11));
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write(json.toString());
                wr.flush();
                conn.connect();
                int resp = conn.getResponseCode();
                return resp;

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Integer result) {
            if(result == ok){
                Toast.makeText(getApplicationContext(), "¡Registrado exitosamente!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Registro.this, Login.class);
                startActivity(intent);
                pb.setVisibility(View.INVISIBLE);
            }
            else {
                Toast.makeText(getApplicationContext(), "¡UPS! algo salió mal :(", Toast.LENGTH_LONG).show();
            }
        }
    }

}