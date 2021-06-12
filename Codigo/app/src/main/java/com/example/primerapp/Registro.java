package com.example.primerapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.primerapp.SimpleDialog;
import com.google.android.material.snackbar.Snackbar;

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
    public void registrar(View view) throws IOException, JSONException, InterruptedException {

        RequestAPIRest hilo = new RequestAPIRest();

        name = (TextView) findViewById(R.id.inNombre);
        surname = (TextView) findViewById(R.id.inApellido);
        dni = (TextView) findViewById(R.id.inDNI);
        email = (TextView) findViewById(R.id.inCorreo);
        pw = (TextView) findViewById(R.id.inPass);
        repass = (TextView) findViewById(R.id.inRepPass);

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
        hilo.execute(name.getText().toString(), surname.getText().toString(), dni.getText().toString(), email.getText().toString(), pw.getText().toString());
        Toast.makeText(getApplicationContext(), "Registrado Exitosamente!", 10).show();
        Intent i = new Intent(this, Login.class);
        startActivity(i);
    }
}