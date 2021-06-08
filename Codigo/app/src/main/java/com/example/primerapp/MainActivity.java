package com.example.primerapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
    private int codigoGenerado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnEnviarSMS = (Button)findViewById(R.id.btnEnviarSMS);
        lblIngresar = (TextView) findViewById(R.id.lblIngresar);
        btnValidarSMS = (Button) findViewById(R.id.btnValidarSMS);
        inCodigo = (TextView) findViewById(R.id.inCodigo);
        inCelular = (TextView) findViewById(R.id.inCelular);
        if(ActivityCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED&& ActivityCompat.checkSelfPermission(
                MainActivity.this, Manifest
                        .permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]
                    { Manifest.permission.SEND_SMS,},1000);
        }
    }

    public void enviarSMS(View view){
        codigoGenerado = (int)(Math.random()*1000+1);
        enviarMensaje(inCelular.getText().toString(),"Ingresa el número" +" "+ codigoGenerado +" " + "en tu pantalla");
        lblIngresar.setText("Ingrese el código");
        btnEnviarSMS.setVisibility(View.INVISIBLE);
        inCelular.setVisibility(View.INVISIBLE);
        btnValidarSMS.setVisibility(View.VISIBLE);
        inCodigo.setVisibility(View.VISIBLE);

    }

    public void verificarSMS(View view){
        if(codigoGenerado == Integer.parseInt(inCodigo.getText().toString())){
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

