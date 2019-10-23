package it.projectalpha.howispend.core.registrazione;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.Objects;

import it.projectalpha.howispend.R;
import it.projectalpha.howispend.model.Utente;

public class RegistrazioneSecondFragment extends Fragment {

    private View view;

    private TextView text3;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.registrazione_second_fragment, container, false);

        Bundle bundle = getArguments();
        Utente obj = (Utente) Objects.requireNonNull(bundle).getSerializable("nuovoUtente");


        text3 = view.findViewById(R.id.textView3);
        text3.setText("Benevenuto " + obj.getNome() + " " + obj.getCognome());

        //TODO EMAIL E PASSWORD


        return view;
    }

}
