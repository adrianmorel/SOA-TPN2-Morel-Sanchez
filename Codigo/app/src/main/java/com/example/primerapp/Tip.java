package com.example.primerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Tip extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensor;
    private TextView lblIndicacion;
    private TextView lblTip;
    private ArrayList<String> listaDeTips = new ArrayList();
    private String tip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips);

        sensor = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor.registerListener(this, sensor.getDefaultSensor(Sensor.TYPE_PROXIMITY), SensorManager.SENSOR_DELAY_NORMAL);

        lblIndicacion = (TextView) findViewById(R.id.lblIndicacion);
        lblTip = (TextView) findViewById(R.id.lblTip);

        listaDeTips.add("Lavate las manos con frecuencia. Usa agua y jabón o un desinfectante de manos a base de alcohol.");
        listaDeTips.add("Cuando tosas o estornudes, cubrite la nariz y la boca con el codo flexionado o con un pañuelo.");
        listaDeTips.add("Si no te sentís bien, quedate en casa.");
        listaDeTips.add("En caso de que tengas fiebre, tos o dificultad para respirar, buscá atención médica.");
        listaDeTips.add("Mantené una distancia de seguridad de al menos 2 metros con otras personas.");
        listaDeTips.add("Utilizá mascarilla cuando no sea posible mantener el distanciamiento físico.");
        listaDeTips.add("No te toques los ojos, la nariz ni la boca sin higienizarte previamente.");
        listaDeTips.add("Llama por teléfono antes de acudir a cualquier proveedor de servicios sanitarios para que te dirijan al centro médico adecuado.");
        listaDeTips.add("Evitá las multitudes y los espacios interiores con mala ventilación.");
        listaDeTips.add("Limpiá las superficies de alto contacto a diario. Esto incluye las mesas, las manijas de las puertas, los interruptores de luz, etc.");

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if(event.values[0] == 0){
            int rnd = (int) (Math.random()*9+1);
            System.out.println("El nro elegido es:" + rnd);
            tip = listaDeTips.get(rnd);
            lblTip.setText(tip);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onResume() {
        super.onResume();
        sensor.registerListener(this, sensor.getDefaultSensor(Sensor.TYPE_PROXIMITY), SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        super.onPause();
        sensor.unregisterListener(this, sensor.getDefaultSensor(Sensor.TYPE_PROXIMITY));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sensor.unregisterListener(this, sensor.getDefaultSensor(Sensor.TYPE_PROXIMITY));
    }
}
