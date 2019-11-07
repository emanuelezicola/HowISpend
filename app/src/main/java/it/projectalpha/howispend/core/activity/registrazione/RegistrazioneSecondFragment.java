package it.projectalpha.howispend.core.activity.registrazione;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.apache.commons.lang3.StringUtils;
import java.time.LocalDate;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import it.projectalpha.howispend.R;
import it.projectalpha.howispend.core.activity.LoginActivity;
import it.projectalpha.howispend.model.Utente;
import it.projectalpha.howispend.utilities.ButtonUtils;
import it.projectalpha.howispend.utilities.Constants;
import it.projectalpha.howispend.utilities.InputTextUtils;
import it.projectalpha.howispend.utilities.SnackbarUtils;

public class RegistrazioneSecondFragment extends Fragment {

    private View view;

    private MaterialButton creaUtente;
    private TextInputEditText emailInput, passwordInput, confermaPasswordInput;

    private Utente utente;
    private Constants constants;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.registrazione_second_fragment, container, false);

        utente = NuovoUtenteActivity.getUtente();
        constants = Constants.getInstance();

        MaterialButton indietro = view.findViewById(R.id.backToFirst);
        creaUtente = view.findViewById(R.id.creaUtente);
        emailInput = view.findViewById(R.id.nuovoUtenteEmail);
        passwordInput = view.findViewById(R.id.nuovoUtentePassword);
        confermaPasswordInput = view.findViewById(R.id.nuovoUtentePasswordRepeat);


        NuovoUtenteActivity.setFragment("step2");


        //Gestione tasto indietro
        indietro.setOnClickListener(view -> {


            RegistrazioneFirstFragment fragment1 = new RegistrazioneFirstFragment();
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = Objects.requireNonNull(fragmentManager).beginTransaction();
            fragmentTransaction.replace(R.id.nuovoUtenteFrame, fragment1);
            fragmentTransaction.commit();
        });

        //Gestione crea nuovo utente
        creaUtente.setOnClickListener(view -> {
            String email = InputTextUtils.getTextFromTextInput(emailInput);
            String password = InputTextUtils.getTextFromTextInput(passwordInput);
            String passwordConferma = InputTextUtils.getTextFromTextInput(confermaPasswordInput);

            if(!invalidEmail(email) && !invalidPassword(password, passwordConferma)) {
                utente.setEmail(email);

                String passwordEncoded = Base64.getEncoder().encodeToString(password.getBytes());
                utente.setPassword(passwordEncoded);
                utente.setDataCreazione(LocalDate.now());

                doRegistrazione();

            }

        });

        return view;
    }


    /**
     * Metodo che controlla se la mail soddisfa i requisiti del sistema
     * @param email il parametro da controllare
     * @return true se la mail non soddisfa i requisiti delle mail, false altrimenti
     */
    private boolean invalidEmail(String email) {

        if(StringUtils.isBlank(email)) {
            InputTextUtils.setError(emailInput, "Inserire una mail valida");
            return true;
        }

        if(!constants.matchMailRegex(email)) {
            InputTextUtils.setError(emailInput, "La mail inserita non è valida");
        }

        return false;
    }

    /**
     * Controlla se le password soddisfano i requisiti delle password
     * @param password parametro che identifica la password dell'utente
     * @param confermaPassword parametro che identifica la password di conferma
     * @return true se la password o la conferma non sono valide, true altrimenti
     */
    private boolean invalidPassword(String password, String confermaPassword) {

        if(StringUtils.isBlank(password)) {
            InputTextUtils.setError(passwordInput, "Inserire una password valida");
            return true;
        }

        if(StringUtils.isBlank(confermaPassword)) {
            InputTextUtils.setError(confermaPasswordInput, "Inserire una password di conferma valida");
            return true;
        }

        if(!password.equals(confermaPassword)) {
            InputTextUtils.setError(passwordInput, "Le password non coincidono");
            InputTextUtils.setError(confermaPasswordInput, "Le password non coincidono");
            return true;
        }

        if(password.length() < 6) {
            InputTextUtils.setError(passwordInput, "La password deve essere di almeno sei caratteri");
            return true;
        }


        return false;
    }



    private void doRegistrazione() {

        ButtonUtils.enableButton(creaUtente, false);

        RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, constants.getURL_NUOVO_UTENTE(),
                serverResponse -> {

                    if(!"[]".equals(serverResponse)) {
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        ButtonUtils.enableButton(creaUtente, true);
                        NuovoUtenteActivity.setUtente(new Utente());
                        NuovoUtenteActivity.setFragment(null);
                    }

                    else {
                        try {
                            throw new Exception();
                        } catch (Exception e) {
                            e.printStackTrace();
                            SnackbarUtils.showShortSnackBar(creaUtente, "Ops, qualcosa è andato storto");
                        }
                    }

                },
                volleyError -> {
                    SnackbarUtils.showShortSnackBar(creaUtente, "Si è verificato un errore, riprova più tardi");
                    ButtonUtils.enableButton(creaUtente, true);
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("email", utente.getEmail());
                params.put("password", utente.getPassword());
                params.put("nome", utente.getNome());
                params.put("cognome", utente.getCognome());
                params.put("dataRegistrazione", utente.getDataCreazione().toString());

                return params;
            }
        };

        requestQueue.add(stringRequest);
    }





}
