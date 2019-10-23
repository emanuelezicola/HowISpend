package it.projectalpha.howispend.core.registrazione;

import android.os.Bundle;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

import it.projectalpha.howispend.R;
import it.projectalpha.howispend.core.LoginActivity;
import it.projectalpha.howispend.utilities.InputTextUtils;

import it.projectalpha.howispend.model.Utente;


public class RegistrazioneFirstFragment extends Fragment {

    private View view;

    private MaterialButton btnAvanti, btnAnnulla;
    private TextInputEditText nomeInput, cognomeInput;

    private Utente utente;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.registrazione_first_fragment, container, false);


        btnAvanti = view.findViewById(R.id.nuovoUtenteStep2);
        btnAnnulla = view.findViewById(R.id.backToLogin);
        nomeInput = view.findViewById(R.id.nuovoUtenteNome);
        cognomeInput = view.findViewById(R.id.nuovoUtenteCognome);

        btnAvanti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nome = InputTextUtils.getTextFromTextInput(nomeInput);
                String cognome = InputTextUtils.getTextFromTextInput(cognomeInput);

                if(!invalidInput(nome, cognome)) {

                    utente = new Utente();
                    utente.setNome(nome);
                    utente.setCognome(cognome);

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("nuovoUtente", utente);


                    RegistrazioneSecondFragment fragment2 = new RegistrazioneSecondFragment();
                    fragment2.setArguments(bundle);


                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = Objects.requireNonNull(fragmentManager).beginTransaction();
                    fragmentTransaction.replace(R.id.nuovoUtenteFrame, fragment2);
                    fragmentTransaction.commit();
                }
            }
        });

        btnAnnulla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                utente = null;
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });



        return view;
    }


    private boolean invalidInput(String nome, String cognome) {
        boolean result = false;

        if(StringUtils.isBlank(nome)) {
            InputTextUtils.setError(nomeInput, "Inserire un nome valido");
            result = true;
        }
        if (StringUtils.isBlank(cognome)) {
            InputTextUtils.setError(cognomeInput, "Inserire un cognome valido");
            result = true;
        }

        return result;
    }




}
