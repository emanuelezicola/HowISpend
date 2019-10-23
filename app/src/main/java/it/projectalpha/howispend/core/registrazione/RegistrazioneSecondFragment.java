package it.projectalpha.howispend.core.registrazione;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;

import java.util.Objects;

import it.projectalpha.howispend.R;
import it.projectalpha.howispend.model.Utente;

public class RegistrazioneSecondFragment extends Fragment {

    private View view;

    private MaterialButton indietro, creaUtente;

    private Utente utente;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.registrazione_second_fragment, container, false);

        utente = NuovoUtenteActivity.getUtente();

        indietro = view.findViewById(R.id.backToFirst);
        creaUtente = view.findViewById(R.id.creaUtente);

        indietro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO INDIETRO
            }
        });

        creaUtente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO CREA UTENTE
            }
        });

        return view;
    }

}
