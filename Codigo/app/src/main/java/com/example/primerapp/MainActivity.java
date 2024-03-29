package com.example.primerapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.telephony.SmsManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class MainActivity extends AppCompatActivity {

    private TextView lblIngresar;
    private Button btnEnviarSMS;
    private TextView inCelular;
    private Button btnValidarSMS;
    private TextView inCodigo;
    private TextView lblInicio;
    private TextView tituloProy;
    private ImageView imgCov;
    private ImageView imgCalc;
    private int codigoGenerado;
    private int msjok = 1;
    private int msjerr = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnEnviarSMS = (Button)findViewById(R.id.btnEnviarSMS);
        lblIngresar = (TextView) findViewById(R.id.lblIngresar);
        btnValidarSMS = (Button) findViewById(R.id.btnValidarSMS);
        inCodigo = (TextView) findViewById(R.id.inCodigo);
        inCelular = (TextView) findViewById(R.id.inCelular);
        lblInicio = (TextView) findViewById(R.id.lblInicio2);
        tituloProy = (TextView) findViewById(R.id.titleProy);
        imgCalc = (ImageView) findViewById(R.id.imagecalc);
        imgCov = (ImageView) findViewById(R.id.imgCovid);
        if(ActivityCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED&& ActivityCompat.checkSelfPermission(
                MainActivity.this, Manifest
                        .permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]
                    { Manifest.permission.SEND_SMS,},1000);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void enviarSMS(View view){
        codigoGenerado = (int)(Math.random()*10000+1);
        int msj = enviarMensaje(inCelular.getText().toString(),"Ingresa el número" +" "+ codigoGenerado +" " + "en tu pantalla");
        if(msj == msjok){
        lblIngresar.setText("Ingrese el código");
        btnEnviarSMS.setVisibility(View.INVISIBLE);
        inCelular.setVisibility(View.INVISIBLE);
        btnValidarSMS.setVisibility(View.VISIBLE);
        inCodigo.setVisibility(View.VISIBLE);}

    }

    public void verificarSMS(View view){
        if(inCodigo.length() == 0){
            Toast.makeText(getApplicationContext(), "El campo código no puede estar vacío.", Toast.LENGTH_LONG).show();
            return;
        }

        if(codigoGenerado == Integer.parseInt(inCodigo.getText().toString())){
            Intent i = new Intent(this, Login.class);
            startActivity(i);
        }
        else{
            Toast.makeText(getApplicationContext(), "El código es incorrecto", Toast.LENGTH_LONG).show();}

    }
    
    private int enviarMensaje(String numero, String mensaje) {
        try {
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(numero,null,mensaje,null,null);
            Toast.makeText(getApplicationContext(), "Mensaje Enviado.", Toast.LENGTH_LONG).show();
            return msjok;
        }
        catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Mensaje no enviado, datos incorrectos.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
            return msjerr;
        }
    }
}

