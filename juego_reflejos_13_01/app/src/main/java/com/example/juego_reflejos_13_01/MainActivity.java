package com.example.juego_reflejos_13_01;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import static java.lang.Character.toUpperCase;

public class MainActivity extends BaseActivity implements DialogoOpciones.DialogoOcionesListener {
    public static final String VELOCIDAD = "v", NOMBRE = "n", FASE="d";
    public static final int REQ_PLAY = 100;
    private String nombre;
    private int fase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void opciones(View v){
        mostrarDialogo();
    }

    public void mostrarDialogo(){
        DialogFragment dialogo = new DialogoOpciones();
        dialogo.setCancelable(false);
        dialogo.show(getSupportFragmentManager(), "DialogoOpciones");
    }

    @Override
    public void onDialogOpcionesListener(String nombre, int velocidad) {
        Intent i = new Intent(this, JuegoActivity.class);
        nombre = toUpperCase(nombre.charAt(0))+nombre.substring(1);
        i.putExtra(NOMBRE, nombre);
        i.putExtra(VELOCIDAD, velocidad);
        Log.d("DATOS-----0000-----> ", "nombre="+nombre+", velocidad="+velocidad);
        startActivityForResult(i, REQ_PLAY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQ_PLAY && resultCode==RESULT_OK){
            AlertDialog.Builder alerta=new AlertDialog.Builder(this);
            nombre=data.getStringExtra(NOMBRE);
            int fase=data.getIntExtra(FASE,0);
            alerta.setMessage(String.format(getResources().getString(R.string.finJuego),nombre,fase));
            alerta.setPositiveButton("Ok",null);
            alerta.show();
        }
        else {
            Toast.makeText(this,"El usuario cancelo",Toast.LENGTH_LONG).show();
        }
    }
}
