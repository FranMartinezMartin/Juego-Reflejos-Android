package com.example.juego_reflejos_13_01;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.fragment.app.DialogFragment;

public class DialogoOpciones extends DialogFragment implements DialogInterface.OnClickListener {
    EditText etNombre;
    RadioButton rbFacil, rbMedio, rbDificil;
    DialogoOcionesListener listener;

    @Override
    public Dialog onCreateDialog(Bundle saveInstance){
        AlertDialog.Builder dialogo = new AlertDialog.Builder(getActivity());
        //Atamos el cuadro de dialogo al layout "layout_opciones"
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        dialogo.setView(inflater.inflate(R.layout.opciones, null));
        //Ponemos titulo
        dialogo.setMessage(getResources().getString(R.string.titOpciones));
        dialogo.setPositiveButton(getResources().getString(R.string.Aceptar), this);
        dialogo.setNegativeButton(getResources().getString(R.string.Cancelar), this);
        return dialogo.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        //cogemos los elementos
        etNombre = ((Dialog) dialog).findViewById(R.id.txtJugador);
        rbFacil = ((Dialog) dialog).findViewById(R.id.rbFacil);
        rbMedio = ((Dialog) dialog).findViewById(R.id.rbMedio);
        rbDificil = ((Dialog) dialog).findViewById(R.id.rbDificil);
        int velocidad = 0;
        String nombre = etNombre.getText().toString().trim();
        if(nombre.length() == 0){
            nombre = "Guest";
        }
        //Retardo en funcion de la dificultad
        if(rbFacil.isChecked()) velocidad = 60;
        if(rbMedio.isChecked()) velocidad = 40;
        if(rbDificil.isChecked()) velocidad = 20;
        switch (which){
            case DialogInterface.BUTTON_POSITIVE:
                listener.onDialogOpcionesListener(nombre, velocidad);
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                getActivity().finish();
        }
    }
    //----------------------------------------------------------------------------------------------
    //Necesito contruir la interfaz
    public interface DialogoOcionesListener{
        public void onDialogOpcionesListener(String nombre, int velocidad);

    }
    //Metodo obligatorio para atarlo a la clase que lo llama
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        try{
            listener = (DialogoOcionesListener) context;
        }catch (Exception e){

        }

    }

}
