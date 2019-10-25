package it.projectalpha.howispend.core.dialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.material.textfield.TextInputEditText;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;
import it.projectalpha.howispend.R;
import it.projectalpha.howispend.utilities.InputTextUtils;

public class DialogCambioPassword extends AppCompatDialogFragment {


    private DialogCambioPassword.CambioPasswordDialogListener listener;

    private TextInputEditText vecchiaPasswordInput, nuovaPasswordInput, confermaPasswordInput;

    private String passwordUtente;





    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder  = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
        @SuppressLint("InflateParams")
        final View view = inflater.inflate(R.layout.dialog_cambio_password, null);

        vecchiaPasswordInput = view.findViewById(R.id.vecchiaPassword);
        nuovaPasswordInput = view.findViewById(R.id.nuovaPassword);
        confermaPasswordInput = view.findViewById(R.id.confermaPassword);

        Bundle bundle = Objects.requireNonNull(getArguments());
        passwordUtente = bundle.getString("passwordUtente");



        builder.setView(view)
                .setTitle("Sei sicuro di voler cambiare password?")
                .setMessage("Inserisci la vecchia password, poi la nuova")
                .setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Salva modifiche", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String vecchiaPassword = InputTextUtils.getTextFromTextInput(vecchiaPasswordInput);
                        String nuovaPassword = InputTextUtils.getTextFromTextInput(nuovaPasswordInput);
                        String confermaPassword = InputTextUtils.getTextFromTextInput(confermaPasswordInput);

                        String result = invalidPassword(vecchiaPassword, nuovaPassword, confermaPassword);
                        if(!StringUtils.isBlank(result)) {
                           listener.error(result);
                           return;
                        }
                        listener.cambioPassword(nuovaPassword);
                    }

                });

        return builder.create();
    }




    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (DialogCambioPassword.CambioPasswordDialogListener) context;
        }catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "Class must be implement CambioPasswordDialogListener");
        }
    }


    public interface CambioPasswordDialogListener {
        void cambioPassword(String nuovaPassword);
        void error(String error);
    }





    private String invalidPassword(String vecchiaPassword, String nuovaPassword, String confermaPassword) {

        //CONTROLLO VECCHIA PASSWORD
        if(StringUtils.isBlank(vecchiaPassword) || !vecchiaPassword.equals(passwordUtente) ) {
            return "La vecchia password non è corretta";
        }

        if(StringUtils.isBlank(nuovaPassword)) {
            return "Inserire una nuova password valida";
        }

        if(StringUtils.isBlank(confermaPassword)) {
            return "Inserire una password di conferma valida";
        }

        if(!nuovaPassword.equals(confermaPassword)) {
            return "La nuova password e la conferma non coincidono";
        }

        if(nuovaPassword.length() < 6) {
            return "La password deve essere di almeno sei caratteri";
        }

        if(nuovaPassword.equals(vecchiaPassword)) {
            return "La nuova password non può essere uguale alla vecchia password";
        }


        return null;
    }





}
