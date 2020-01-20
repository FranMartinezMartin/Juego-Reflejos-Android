package com.example.juego_reflejos_13_01;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class JuegoActivity extends BaseActivity implements View.OnClickListener {
    private int velocidad, progreso, fase, contadorBotones;
    private String nombre;
    private ArrayList<Button> botones;
    private Button boton11, boton12, boton13, boton21, boton22, boton23;
    private TextView tvNombre, tvProgreso, tvFase;
    private ProgressBar barra;
    private ProgresoBarra miProgreso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego);
        setModoInmersivo();
        cargarView();
        ponerListeners();
        cargarOpciones();
    }

    private void ponerListeners() {
        for(int i=0; i<botones.size();i++) {
            botones.get(i).setOnClickListener(this);
        }
    }

    public void cargarOpciones(){
        velocidad = getIntent().getIntExtra(MainActivity.VELOCIDAD, 40);
        nombre = getIntent().getStringExtra(MainActivity.NOMBRE);
        tvNombre.setText(nombre);
        fase=1;
        contadorBotones=1;
        progreso=0;
        tvFase.setText(String.format(getResources().getString(R.string.fase), fase));
        tvProgreso.setText(String.format(getResources().getString(R.string.progreso), progreso));

    }

    public ArrayList<Integer> desordenarBotones() {
        ArrayList<Integer> al = new ArrayList<>();
        for(int i=1; i<=6;i++){
            al.add(i);
        }
        Collections.shuffle(al);
        return al;
    }

    public void numerarBotones (ArrayList<Integer> al){
        for(int i=0; i<botones.size();i++){
            botones.get(i).setEnabled(true);
            botones.get(i).setText(""+al.get(i));
        }
    }

    public void jugar(View v){
        numerarBotones(desordenarBotones());
        miProgreso=new ProgresoBarra();
        miProgreso.execute(velocidad);
        ((Button)v).setEnabled(false);
    }

    public void volver(View v){
        finish();
    }


    private void cargarView(){
        tvNombre = findViewById(R.id.textView);
        tvFase = findViewById(R.id.textView2);
        tvProgreso = findViewById(R.id.bProgreso);

        barra=findViewById(R.id.progresBar);
        barra.setScaleY(3f);

        botones = new ArrayList<>();
        boton11 = findViewById(R.id.btn1_1);
        botones.add(boton11);
        boton12 = findViewById(R.id.btn1_2);
        botones.add(boton12);
        boton13 = findViewById(R.id.btn1_3);
        botones.add(boton13);
        boton21 = findViewById(R.id.btn2_1);
        botones.add(boton21);
        boton22 = findViewById(R.id.btn2_2);
        botones.add(boton22);
        boton23 = findViewById(R.id.btn2_3);
        botones.add(boton23);
    }

    @Override
    public void onClick(View v) {
        Button botonPulsado=(Button)v;
        if(Integer.valueOf(botonPulsado.getText().toString())==contadorBotones){
            contadorBotones++;
            botonPulsado.setEnabled(false);
            if(contadorBotones>6){
                miProgreso.cancel(true);
            }
        }
    }
    public void salir(){
        Intent i =new Intent();
        i.putExtra(MainActivity.FASE,fase);
        i.putExtra(MainActivity.NOMBRE,nombre);
        setResult(RESULT_OK,i);
        finish();
    }
    //---------------------------------------------
    //implementaremos nuestra clase con asyntask
    private class ProgresoBarra extends AsyncTask<Integer,Integer,Integer> {

        @Override
        protected Integer doInBackground(Integer... integers) {
            while(progreso<100){
                SystemClock.sleep(integers[0]);
                miProgreso.publishProgress(progreso);
                progreso++;
                if(isCancelled())break;
            }
            return progreso;
        }
        @Override
        protected void onProgressUpdate(Integer...valores){
            super.onProgressUpdate(valores);
            barra.setProgress(valores[0]);
            tvProgreso.setText(String.format(getResources().getString(R.string.progreso),valores[0]));
        }
        //Si pierdo se ejecuta el postExecute es decir la barra llego al 100 o el hilo termina y no ha sido cancelado
        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            salir();
        }
        //si he cancelado es porque antes de llegar a 100 he logrado dar a todos los botones
        //en ese caso reseteo las variable incremento la fase y vueelvo a empezar
        @Override
        protected void onCancelled(Integer integer){
            super.onCancelled(integer);
            if(contadorBotones==7){
                miProgreso=new ProgresoBarra();
                numerarBotones(desordenarBotones());
                tvFase.setText(String.format(getResources().getString(R.string.fase),++fase));
                progreso=0;
                tvProgreso.setText(String.format(getResources().getString(R.string.progreso),progreso));
                contadorBotones=1;
                barra.setProgress(progreso);
                velocidad=(velocidad-5)<0?1:velocidad-5;
                miProgreso.execute(velocidad);
            }
        }
    }

}