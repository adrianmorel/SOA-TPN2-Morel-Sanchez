package com.example.primerapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

public class Login extends AppCompatActivity {
    TextView email;
    TextView pass;
    TextView lbl;
    Button botonCancel;
    Button botonConf;
    Button botonQr;
    ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void registrarse(View view) {
        Intent i = new Intent(this, Registro.class);
        startActivity(i);
    }

    public void confirmarInicio(View view){

        LoginTask lg = new LoginTask();
        email = (TextView) findViewById(R.id.inUsuario);
        pass = (TextView) findViewById(R.id.InContraseña);
        botonConf = (Button) findViewById(R.id.btnConfirmarLogin);
        botonCancel = (Button) findViewById(R.id.btnCancelarLogin);
        pb = (ProgressBar) findViewById(R.id.progressBarLogin);
        botonQr = (Button) findViewById(R.id.btnRegistro);
        lbl = (TextView) findViewById(R.id.lblNoUser);

        if(email.length() == 0){
            Toast.makeText(getApplicationContext(), "El campo email no puede estar vacio", Toast.LENGTH_LONG).show();
            return;
        }
        if(pass.length() == 0){
            Toast.makeText(getApplicationContext(), "El campo password no puede estar vacio", Toast.LENGTH_LONG).show();
            return;
        }

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            System.out.println("Conexion a internet ok");
        } else {
            Toast.makeText(getApplicationContext(), "No hay conexion a internet, revise su estado de red e intentelo nuevamente", Toast.LENGTH_LONG).show();
            return;
        }
        pb.setVisibility(View.VISIBLE);
        lg.execute(email.getText().toString(), pass.getText().toString());
        botonConf.setVisibility(View.INVISIBLE);
        botonCancel.setVisibility(View.INVISIBLE);
        botonQr.setVisibility(View.INVISIBLE);
        lbl.setText("Iniciando Sesión...");
    }

    // Asynctask ---------------------------------------------------------------------------------
    public class LoginTask extends android.os.AsyncTask<String, Void, Integer> {

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected Integer doInBackground(String... params) {
            URL url = null;
            try {
                url = new URL("http://so-unlam.net.ar/api/api/login");
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
                json.put("email", params[0]);
                json.put("password", params[1]);
                System.out.println(json);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write(json.toString());
                wr.flush();
                conn.connect();
                int respCode = conn.getResponseCode();
                String respMessage = conn.getResponseMessage();
                System.out.println(respCode + " " + respMessage);

                InputStream token = new BufferedInputStream(conn.getInputStream());
                InputStreamReader inputStreamReader = new InputStreamReader(token);
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
        protected void onPostExecute(Integer code) {
            if(code == 200){
                Intent i = new Intent(Login.this, Parametros.class);
                startActivity(i);
                Toast.makeText(getApplicationContext(), "Inicio de sesión correcto", Toast.LENGTH_LONG).show();
                pb.setVisibility(View.INVISIBLE);
                botonCancel.setVisibility(View.VISIBLE);
                botonQr.setVisibility(View.VISIBLE);
                botonConf.setVisibility(View.VISIBLE);
                lbl.setText("¿No tienes usuario?");
            }
            else {
                Toast.makeText(getApplicationContext(), "Error Bad Request - " + code + ", revise sus datos e ingreselos nuevamente", Toast.LENGTH_LONG).show();
                pb.setVisibility(View.INVISIBLE);
                botonCancel.setVisibility(View.VISIBLE);
                botonQr.setVisibility(View.VISIBLE);
                botonConf.setVisibility(View.VISIBLE);
                lbl.setText("¿No tienes usuario?");
            }
        }
    }
}
