package com.example.primerapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Registro extends AppCompatActivity {

    private TextView name;
    private TextView surname;
    private TextView email;
    private TextView pw;
    private TextView repass;
    private TextView dni;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
    }

    @SuppressLint("ResourceType")
    public void registrar (View view) throws IOException, JSONException {

        RequestAPIRest hilo = new RequestAPIRest();

        name = (TextView) findViewById(R.id.inNombre);
        surname = (TextView) findViewById(R.id.inApellido);
        dni = (TextView) findViewById(R.id.inDNI);
        email = (TextView) findViewById(R.id.inCorreo);
        pw = (TextView) findViewById(R.id.inPass);
        repass = (TextView) findViewById(R.id.inRepPass);

        hilo.execute(name.getText().toString(), surname.getText().toString(), dni.getText().toString(), email.getText().toString(), pw.getText().toString());

    }

}
