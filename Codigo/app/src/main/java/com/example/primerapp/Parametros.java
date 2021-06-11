package com.example.primerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import java.util.List;

public class Parametros extends AppCompatActivity {

    private Spinner opciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parametros);

        opciones = findViewById(R.id.listOpciones);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.personas, android.R.layout.simple_spinner_item);
        opciones.setAdapter(adapter);
    }

    public void confirmarParametros(View view){

    }
}