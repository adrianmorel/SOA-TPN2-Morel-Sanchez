package com.example.primerapp;


import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.primerapp.SimpleDialog;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Registro extends AppCompatActivity {

     TextView name;
     TextView surname;
     TextView email;
     TextView pw;
     TextView repass;
     TextView dni;
     Button botonConf;
     ProgressBar pb;

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
            Toast.makeText(getApplicationContext(), "El Nombre no puede estar vacio", 10).show();
            return;
        }

        if(surname.length() == 0){
            Toast.makeText(getApplicationContext(), "El Apellido no puede estar vacio", 10).show();
            return;
        }

        if(dni.length() == 0){
            Toast.makeText(getApplicationContext(), "El DNI no puede estar vacio", 10).show();
            return;
        }

        if(email.length() == 0){
            Toast.makeText(getApplicationContext(), "El email no puede estar vacio", 10).show();
            return;
        }

        if(pw.length() < 8){
            Toast.makeText(getApplicationContext(), "La contraseña debe tener 8 caracteres como minimo", 10).show();
            return;
        }
        System.out.println(pw.getText().toString() + "VS " + repass.getText().toString());

        if(!pw.getText().toString().equals(repass.getText().toString())){
            Toast.makeText(getApplicationContext(), "Las contraseñas no coinciden", 10).show();
            return;
        }

        Toast.makeText(getApplicationContext(), "Verificando datos del servidor..", 10).show();
        pb.setVisibility(View.VISIBLE);
        botonConf.setVisibility(View.INVISIBLE);
        hilo.execute(name.getText().toString(), surname.getText().toString(), dni.getText().toString(), email.getText().toString(), pw.getText().toString());

    }

    ///------------------------------------AsyncTask-----------------------------------------------------------------------------
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
            if(result == 200){
                Toast.makeText(getApplicationContext(), "¡Registrado exitosamente!", 10).show();
                Intent intent = new Intent(Registro.this, Login.class);
                startActivity(intent);
                pb.setVisibility(View.INVISIBLE);
            }
            else {
                Toast.makeText(getApplicationContext(), "¡UPS! algo salió mal :(", 10).show();
            }
        }
    }

}