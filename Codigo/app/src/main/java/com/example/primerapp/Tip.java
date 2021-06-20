package com.example.primerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
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

    private static final int cantTips = 10;
    private static final float brilloBajo = (float) 100.0;
    private static final float brilloAlto = (float) 1000.0;
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
        sensor.registerListener(this, sensor.getDefaultSensor(Sensor.TYPE_LIGHT), SensorManager.SENSOR_DELAY_NORMAL);
        SharedPreferences medicionesSensores = getSharedPreferences("sensores", Context.MODE_PRIVATE);
        medicionesSensores.getString("Lectura sensor distancia", "desconocido");
        medicionesSensores.getString("Lectura sensor luz", "desconocido");
        medicionesSensores.getString("Listener sensor distancia", "desconocido");
        medicionesSensores.getString("Listener sensor luz", "desconocido");
        lblIndicacion = (TextView) findViewById(R.id.lblIndicacion);
        lblTip = (TextView) findViewById(R.id.lblTip);

        listaDeTips.add("Lavate las manos con frecuencia."+"\n"+ "Usa agua y jabón o un desinfectante de manos a base de alcohol.");
        listaDeTips.add("Cuando tosas o estornudes, cubrite la nariz " + " \n" + "y la boca con el codo flexionado o con un pañuelo.");
        listaDeTips.add("Si no te sentís bien, quedate en casa.");
        listaDeTips.add("En caso de que tengas fiebre, tos"+"\n"+" o dificultad para respirar, buscá atención médica.");
        listaDeTips.add("Mantené una distancia de seguridad de" + "\n" + "al menos 2 metros con otras personas.");
        listaDeTips.add("Utilizá mascarilla cuando no sea posible"+"\n"+" mantener el distanciamiento físico.");
        listaDeTips.add("No te toques los ojos, la nariz"+ "\n"+" ni la boca sin higienizarte previamente.");
        listaDeTips.add("Llama por teléfono antes de acudir a" +"\n"+"cualquier proveedor de servicios sanitarios para que te dirijan al centro médico adecuado.");
        listaDeTips.add("Evitá las multitudes y los espacios" + "\n" + "interiores con mala ventilación.");
        listaDeTips.add("Limpiá las superficies de alto contacto a diario."+ "\n"+ "Esto incluye las mesas, las manijas de las puertas, los interruptores de luz, etc.");

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        synchronized (this) {
            SharedPreferences medicionesSensores = getSharedPreferences("sensores", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = medicionesSensores.edit();
            switch (event.sensor.getType()) {
                case Sensor.TYPE_PROXIMITY:

                    editor.putString("Lectura sensor distancia", String.valueOf(event.values[0]));
                    editor.commit();
                    if(event.values[0] == 0){
                        int rnd = (int) (Math.random()*cantTips);
                        tip = listaDeTips.get(rnd);
                        lblTip.setText(tip);
                    }
                    break;

                case Sensor.TYPE_LIGHT:
                    editor.putString("Lectura sensor luz", String.valueOf(event.values[0]));
                    editor.commit();

                    if(event.values[0] < brilloBajo){
                        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
                        layoutParams.screenBrightness = 1.0f;
                        getWindow().setAttributes(layoutParams);
                        Toast.makeText(getApplicationContext(), "Ajustando brillo", Toast.LENGTH_LONG).show();
                    }
                    if(event.values[0] > brilloAlto){
                        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
                        layoutParams.screenBrightness = 0.1f;
                        getWindow().setAttributes(layoutParams);
                        Toast.makeText(getApplicationContext(), "Ajustando brillo", Toast.LENGTH_LONG).show();
                    }
                    break;
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onResume() {
        super.onResume();
        sensor.registerListener(this, sensor.getDefaultSensor(Sensor.TYPE_PROXIMITY), SensorManager.SENSOR_DELAY_NORMAL);
        sensor.registerListener(this, sensor.getDefaultSensor(Sensor.TYPE_LIGHT), SensorManager.SENSOR_DELAY_NORMAL);
        SharedPreferences medicionesSensores = getSharedPreferences("sensores", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = medicionesSensores.edit();
        editor.putString("Listener sensor luz", "Suscripto");
        editor.putString("Listener sensor distancia", "Suscripto");
        editor.commit();

    }

    @Override
    public void onPause() {
        super.onPause();
        sensor.unregisterListener(this, sensor.getDefaultSensor(Sensor.TYPE_PROXIMITY));
        sensor.unregisterListener(this, sensor.getDefaultSensor(Sensor.TYPE_LIGHT));
        SharedPreferences medicionesSensores = getSharedPreferences("sensores", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = medicionesSensores.edit();
        editor.putString("Listener sensor luz", "No Suscripto");
        editor.putString("Listener sensor distancia", "No Suscripto");
        editor.commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sensor.unregisterListener(this, sensor.getDefaultSensor(Sensor.TYPE_PROXIMITY));
        sensor.unregisterListener(this, sensor.getDefaultSensor(Sensor.TYPE_LIGHT));
        SharedPreferences medicionesSensores = getSharedPreferences("sensores", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = medicionesSensores.edit();
        editor.putString("Listener sensor luz", "No Suscripto");
        editor.putString("Listener sensor distancia", "No Suscripto");
        editor.commit();
    }
}
