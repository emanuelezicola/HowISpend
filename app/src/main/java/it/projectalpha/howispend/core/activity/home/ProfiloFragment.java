package it.projectalpha.howispend.core.activity.home;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import it.projectalpha.howispend.R;
import it.projectalpha.howispend.core.dialog.DialogCambiaNomeCognome;
import it.projectalpha.howispend.model.Utente;
import it.projectalpha.howispend.utilities.Session;

public class ProfiloFragment extends Fragment {

    private TextView nomeText, cognomeText, nomeCognomeText;
    private ConstraintLayout wrapperNome, wrapperCognome;

    private View view;

    private Session session;
    private Utente utente;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_profilo_fragment, null);

        nomeText = view.findViewById(R.id.nomeProfilo);
        cognomeText = view.findViewById(R.id.cognomeProfilo);
        nomeCognomeText = view.findViewById(R.id.nomeCognomeProfiloFrag);
        wrapperNome = view.findViewById(R.id.constraintNomeProfiloFrag);
        wrapperCognome = view.findViewById(R.id.constraintCognomeProfiloFrag);


        session = Session.getInstance();
        utente = session.getLoggedUser();

        setRes();

        
        wrapperNome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialog("Sei sicuro di voler cambiare nome?", "Inserisci il nuovo nome", "nome");
            }
        });
        
        wrapperCognome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialog("Sei sicuro di voler cambiare cognome?","Inserisci il nuovo cognome", "cognome");
            }
        });





        return view;
    }

    public void setRes() {
        String nomeCognomeRes = utente.getNome() + " " + utente.getCognome();
        nomeCognomeText.setText(nomeCognomeRes);
        nomeText.setText(utente.getNome());
        cognomeText.setText(utente.getCognome());
    }



    private void createDialog(String title, String message, String valore) {
        DialogCambiaNomeCognome dialog = new DialogCambiaNomeCognome();

        Bundle info = new Bundle();
        info.putString("Title", title);
        info.putString("Message", message);
        info.putString("Valore", valore);
        dialog.setArguments(info);
        dialog.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "cambiaNome dialog");



    }

}
