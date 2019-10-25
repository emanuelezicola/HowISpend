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
import it.projectalpha.howispend.core.dialog.DialogCambioPassword;
import it.projectalpha.howispend.model.Utente;
import it.projectalpha.howispend.utilities.Session;
import it.projectalpha.howispend.utilities.SnackbarUtils;

public class ProfiloFragment extends Fragment {

    private TextView nomeText, cognomeText, nomeCognomeText, emailText, passwordText;
    private ConstraintLayout wrapperNome, wrapperCognome, wrapperEmail, wrapperPassword;

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
        emailText = view.findViewById(R.id.emailProfilo);
        passwordText = view.findViewById(R.id.passwordProfilo);

        wrapperNome = view.findViewById(R.id.constraintNomeProfiloFrag);
        wrapperCognome = view.findViewById(R.id.constraintCognomeProfiloFrag);
        wrapperEmail = view.findViewById(R.id.constraintEmailProfiloFrag);
        wrapperPassword = view.findViewById(R.id.constraintPasswordProfiloFrag);


        session = Session.getInstance();
        utente = session.getLoggedUser();

        setRes();


        wrapperNome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialog("Sei sicuro di voler cambiare nome?", "Inserisci il nuovo nome", "Nome");
            }
        });

        wrapperCognome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialog("Sei sicuro di voler cambiare cognome?","Inserisci il nuovo cognome", "Cognome");
            }
        });

        wrapperEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SnackbarUtils.showShortSnackBar(view, "Non è possibile cambiare la mail");
            }
        });

        wrapperPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialogCambioPassword();
            }
        });






        return view;
    }

    private void setRes() {
        String nomeCognomeRes = utente.getNome() + " " + utente.getCognome();
        nomeCognomeText.setText(nomeCognomeRes);
        nomeText.setText(utente.getNome());
        cognomeText.setText(utente.getCognome());
        emailText.setText(utente.getEmail());
        passwordText.setText(utente.getPassword());
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

    private void createDialogCambioPassword() {
        DialogCambioPassword dialog = new DialogCambioPassword();
        Bundle info = new Bundle();
        info.putString("passwordUtente", utente.getPassword());
        dialog.setArguments(info);
        dialog.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "cambiaPassword dialog");
    }

}
