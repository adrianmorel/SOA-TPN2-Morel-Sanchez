package com.example.primerapp;

import androidx.appcompat.app.AppCompatActivity;


import android.content.res.AssetManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import static android.widget.ArrayAdapter.*;

public class Parametros extends AppCompatActivity {

    private Spinner cantPersonas;
    private Spinner mascarilla;
    private Spinner ventana;
    private Spinner tiempoHablando;
    private Spinner volumenVoz;
    private String cantPersonasSelec;
    private String mascarillaSelec;
    private String ventanaSelec;
    private String tiempoHablandoSelec;
    private String volumenVozSelec;
    private TextView lblProba;
    private TextView lblPersonasInfectadas;
    private String proba;
    private String cantPersonasInfectadas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parametros);

        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = registerReceiver(null, ifilter);
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        float battery = (level / (float)scale)*100;
        new SimpleDialog().show(getSupportFragmentManager(), String.valueOf(battery));

        cantPersonas = findViewById(R.id.comboPersonas);
        ArrayAdapter<CharSequence> adapterPersonas = ArrayAdapter.createFromResource(this, R.array.personas, android.R.layout.simple_spinner_dropdown_item);
        cantPersonas.setAdapter(adapterPersonas);

        mascarilla = findViewById(R.id.comboMascarillas);
        ArrayAdapter<CharSequence> adapterMascarilla = ArrayAdapter.createFromResource(this,R.array.mascarillas, android.R.layout.simple_spinner_dropdown_item);
        mascarilla.setAdapter(adapterMascarilla);

        ventana = findViewById(R.id.comboVentanas);
        ArrayAdapter<CharSequence> adapterVentana = ArrayAdapter.createFromResource(this, R.array.ventanas, android.R.layout.simple_spinner_dropdown_item);
        ventana.setAdapter(adapterVentana);

        tiempoHablando = findViewById(R.id.comboTiempoHablando);
        ArrayAdapter<CharSequence> adapterTiempo = ArrayAdapter.createFromResource(this,R.array.tiempoHablando, android.R.layout.simple_spinner_dropdown_item);
        tiempoHablando.setAdapter(adapterTiempo);

        volumenVoz = findViewById(R.id.comboVolumenDeVoz);
        ArrayAdapter<CharSequence> adapterVolumen = ArrayAdapter.createFromResource(this,R.array.volumenDeVoz, android.R.layout.simple_spinner_dropdown_item);
        volumenVoz.setAdapter(adapterVolumen);

        lblProba = findViewById(R.id.lblProba);
        lblPersonasInfectadas = findViewById(R.id.lblPersonasInfectadas);

        }

        public void calcularProbabilidad(View view) throws IOException {

            cantPersonasSelec = cantPersonas.getSelectedItem().toString();
            mascarillaSelec = mascarilla.getSelectedItem().toString();
            ventanaSelec = ventana.getSelectedItem().toString();
            tiempoHablandoSelec = tiempoHablando.getSelectedItem().toString();
            volumenVozSelec = volumenVoz.getSelectedItem().toString();


            List<String> listado = new ArrayList<String>();
            String linea;

            InputStream is = this.getResources().openRawResource(R.raw.datos);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            if(is != null){
                while ((linea = br.readLine()) != null) {

                    if  ( ! (linea.split(";")[0].equals(cantPersonasSelec)))
                        continue;

                    if ( ! (linea.split(";")[1].equals(mascarillaSelec)))
                        continue;

                    if( ! (linea.split(";")[2].equals(ventanaSelec)))
                        continue;

                    if ( ! (linea.split(";")[3].equals(tiempoHablandoSelec)))
                        continue;

                    if ( ! (linea.split(";")[4].equals(volumenVozSelec)))
                        continue;

                    System.out.println(linea.split(";")[0]);
                    System.out.println(linea.split(";")[1]);
                    System.out.println(linea.split(";")[2]);
                    System.out.println(linea.split(";")[3]);
                    System.out.println(linea.split(";")[4]);

                    proba = linea.split(";")[5];
                    cantPersonasInfectadas = linea.split(";")[6];

                    lblProba.setText(proba + "% de probabilidad de ser infectado");
                    lblPersonasInfectadas.setText(cantPersonasInfectadas + " personas infectadas");

                    break;
                }
            }
    }
}


