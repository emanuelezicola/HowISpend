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
import it.projectalpha.howispend.utilities.SnackbarUtils;

public class DialogCambiaNomeCognome extends AppCompatDialogFragment {

    private TextInputEditText nomeInput;


    private CambiaNomeCognomeDialogListener listener;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder  = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
        @SuppressLint("InflateParams") final View view = inflater.inflate(R.layout.dialog_cambio_nome, null);

        nomeInput = view.findViewById(R.id.nuovoNomeInput);

        Bundle bundle = Objects.requireNonNull(getArguments());
        String title = bundle.getString("Title");
        String message = bundle.getString("Message");
        final String nomeOCognome = bundle.getString("Valore");

        builder.setView(view)
                .setTitle(title)
                .setMessage(message)
                .setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Salva modifiche", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String nome = InputTextUtils.getTextFromTextInput(nomeInput);

                        if(StringUtils.isBlank(nome)) {
                            SnackbarUtils.showShortSnackBar(view, "Il " + nomeOCognome + " inserito non è valido");
                            return;
                        }
                        if("nome".equals(nomeOCognome)) {
                            listener.cambiaNome(nome);
                        } else {
                            listener.cambiaCognome(nome);
                        }
                    }
                }
        );

        return builder.create();
    }



    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (DialogCambiaNomeCognome.CambiaNomeCognomeDialogListener) context;
        }catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "Class must be implement CambiaNomeCognomeDialogListener");
        }
    }


    public interface CambiaNomeCognomeDialogListener
    {
        void cambiaNome(String nuovoNome);
        void cambiaCognome(String nuovoCognome);
    }




}
