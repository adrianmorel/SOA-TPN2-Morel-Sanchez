package com.example.primerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
        pb.setVisibility(View.VISIBLE);
        lg.execute(email.getText().toString(), pass.getText().toString());
        botonConf.setVisibility(View.INVISIBLE);
        botonCancel.setVisibility(View.INVISIBLE);
        botonQr.setVisibility(View.INVISIBLE);
        lbl.setText("Iniciando Sesión...");
    }

    // Asynctask ---------------------------------------------------------------------------------
    public class LoginTask extends android.os.AsyncTask<String, Void, Integer> {

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
                Toast.makeText(getApplicationContext(), "Inicio de sesión Correcto", Toast.LENGTH_LONG).show();
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
