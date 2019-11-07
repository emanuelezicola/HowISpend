package it.projectalpha.howispend.core.dialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.material.textfield.TextInputEditText;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

import it.projectalpha.howispend.R;
import it.projectalpha.howispend.model.Anno;
import it.projectalpha.howispend.utilities.InputTextUtils;
import it.projectalpha.howispend.utilities.SpinnerUtils;

public class DialogNuovoMese extends AppCompatDialogFragment {

    private Spinner spinnerMese;
    private TextInputEditText introitiFacoltativi;

    private NuovoMeseListener nuovoMeseListener;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder  = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
        @SuppressLint("InflateParams") final View view = inflater.inflate(R.layout.dialog_aggiungi_mese, null);

        Bundle bundle = Objects.requireNonNull(getArguments());
        final Anno anno = (Anno) bundle.getSerializable("anno");

        spinnerMese = view.findViewById(R.id.selezionaMese);
        introitiFacoltativi = view.findViewById(R.id.inputIntroitiFac);

        builder.setView(view)
                .setTitle("Inserimento nuovo mese")
                .setMessage("Riempi i valori sottostanti")
                .setNegativeButton("Annulla", (dialogInterface, i) -> {

                })
                .setPositiveButton("Inserisci mese", (dialogInterface, i) -> {
                    String mese = SpinnerUtils.getTextFromSpinner(spinnerMese);

                    if(StringUtils.isBlank(mese)) {
                        nuovoMeseListener.error("Il mese inserito non è valido");
                        return;
                    }
                    String introitiString = InputTextUtils.getTextFromTextInput(introitiFacoltativi);
                    double introiti = 0;
                    if(!StringUtils.isBlank(introitiString)) {
                        introiti = Double.parseDouble(introitiString);
                    }

                    nuovoMeseListener.aggiungiMese(mese, introiti, anno);
                }
                );

        return builder.create();
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            nuovoMeseListener = (DialogNuovoMese.NuovoMeseListener) context;
        }catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "Class must be implement NuovoMeseDialogListener");
        }
    }


    public interface NuovoMeseListener
    {
        void aggiungiMese(String mese, double introiti, Anno anno);
        void error(String error);
    }

}
