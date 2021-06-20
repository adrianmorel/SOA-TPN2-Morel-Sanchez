package com.example.primerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Map;

public class EventosSensor extends AppCompatActivity {

    TextView fila2Col2;
    TextView fila3Col2;
    TextView fila4Col2;
    TextView fila5Col2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventos_sensor);
        fila2Col2 = findViewById(R.id.fila2Col2);
        fila3Col2 = findViewById(R.id.fila3Col2);
        fila4Col2 = findViewById(R.id.fila4Col2);
        fila5Col2 = findViewById(R.id.fila5Col2);
        SharedPreferences valoresSensores = getSharedPreferences("sensores", Context.MODE_PRIVATE);
        Map<String, ?> mapaSensores = null;
        mapaSensores = valoresSensores.getAll();
        String valorSensadoDist = (String) mapaSensores.get("Lectura sensor distancia");
        String valorSensadoLuz = (String) mapaSensores.get("Lectura sensor luz");
        String valorSuscDist = (String) mapaSensores.get("Listener sensor distancia");
        String valorSuscLuz = (String) mapaSensores.get("Listener sensor luz");
        fila2Col2.setText(valorSensadoDist + " cm");
        fila3Col2.setText(valorSensadoLuz + " lum");
        fila4Col2.setText(valorSuscDist);
        fila5Col2.setText(valorSuscLuz);
    }
}
