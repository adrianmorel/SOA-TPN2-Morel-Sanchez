package com.example.primerapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.telephony.SmsManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class MainActivity extends AppCompatActivity {

    private TextView lblCelular;
    private EditText et1;
    private Button btnEnviarSMS;
    private TextView inCelular;
    private Button btnValidarSMS;
    private Number inCodigo;
    private int codigoGenerado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnEnviarSMS = (Button)findViewById(R.id.btnEnviarSMS);
        lblCelular = (TextView) findViewById(R.id.labelCel);
        btnValidarSMS = (Button) findViewById(R.id.btnValidarSMS);
        inCodigo = (Number) (R.id.editText3);
        inCelular = (TextView) findViewById(R.id.inCelular);
        if(ActivityCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED&& ActivityCompat.checkSelfPermission(
                MainActivity.this, Manifest
                        .permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]
                    { Manifest.permission.SEND_SMS,},1000);
        }else{
        };
            }

    public void enviarSMS(View view){
        codigoGenerado = (int) Math.random();
        codigoGenerado = 123;
        enviarMensaje(" " ,"Ingresa el número" + codigoGenerado + "en tu pantalla");
        btnEnviarSMS.setVisibility(View.INVISIBLE);
        lblCelular.setVisibility(View.INVISIBLE);
        inCelular.setVisibility(View.INVISIBLE);
        btnValidarSMS.setVisibility(View.VISIBLE);
        //inCodigo.setVisibility(View.VISIBLE);
        }

    public void verificarSMS(View view){
        if((codigoGenerado) != inCodigo.intValue()){
            Intent i = new Intent(this, Login.class);
            startActivity(i);
        }
        else
            btnValidarSMS.setVisibility(View.INVISIBLE);


    }
    
    private void enviarMensaje(String numero, String mensaje) {
        try {
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(numero,null,mensaje,null,null);
            System.out.println("enviado mensj");
            Toast.makeText(getApplicationContext(), "Mensaje Enviado.", Toast.LENGTH_LONG).show();
        }
        catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Mensaje no enviado, datos incorrectos.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}

