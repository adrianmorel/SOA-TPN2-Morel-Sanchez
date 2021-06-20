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
    TextView fila6Col2;
    TextView fila7Col2;
    TextView fila8Col2;
    TextView fila9Col2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventos_sensor);
        fila2Col2 = findViewById(R.id.fila2Col2);
        fila3Col2 = findViewById(R.id.fila3Col2);
        fila4Col2 = findViewById(R.id.fila4Col2);
        fila5Col2 = findViewById(R.id.fila5Col2);
        fila6Col2 = findViewById(R.id.fila6Col2);
        fila7Col2 = findViewById(R.id.fila7Col2);
        fila8Col2 = findViewById(R.id.fila8Col2);
        fila9Col2 = findViewById(R.id.fila9Col2);
        SharedPreferences valoresSensores = getSharedPreferences("sensores", Context.MODE_PRIVATE);
        Map<String, ?> mapaSensores = null;
        mapaSensores = valoresSensores.getAll();
        String valorSensadoDist = (String) mapaSensores.get("Lectura sensor distancia");
        String valorSensadoLuz = (String) mapaSensores.get("Lectura sensor luz");
        String valorSuscDist = (String) mapaSensores.get("Listener sensor distancia");
        String valorSuscLuz = (String) mapaSensores.get("Listener sensor luz");
        String valorCoordX = (String) mapaSensores.get("Lectura acelerometro eje X");
        String valorCoordY = (String) mapaSensores.get("Lectura acelerometro eje Y");
        String valorCoordZ = (String) mapaSensores.get("Lectura acelerometro eje Z");
        String valorSuscAcel = (String) mapaSensores.get("Listener acelerometro");
        fila2Col2.setText(valorSensadoDist + " cm");
        fila3Col2.setText(valorSensadoLuz + " lum");
        fila4Col2.setText(valorSuscDist);
        fila5Col2.setText(valorSuscLuz);
        fila6Col2.setText(valorCoordX);
        fila7Col2.setText(valorCoordY);
        fila8Col2.setText(valorCoordZ);
        fila9Col2.setText(valorSuscAcel);
    }
}
