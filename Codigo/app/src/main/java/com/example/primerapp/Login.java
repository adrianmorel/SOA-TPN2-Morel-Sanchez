package com.example.primerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Login extends AppCompatActivity {

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
        Intent i = new Intent(this, Parametros.class);
        startActivity(i);
    }
}
