package it.projectalpha.howispend.core.registrazione;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import it.projectalpha.howispend.R;
import it.projectalpha.howispend.core.LoginActivity;
import it.projectalpha.howispend.model.Utente;

import android.content.Intent;
import android.os.Bundle;


public class NuovoUtenteActivity extends AppCompatActivity {

    private static Utente utente;

    private static String fragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nuovo_utente_activity);

        utente = new Utente();

        if (savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.nuovoUtenteFrame, new RegistrazioneFirstFragment()).commit();
        }
    }


    @Override
    public void onBackPressed() {
        switch (fragment) {
            case "step1":
                utente = null;
                Intent intent = new Intent(NuovoUtenteActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
            case "step2" :
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nuovoUtenteFrame, new RegistrazioneFirstFragment()).commit();
        }
    }

    public static Utente getUtente() {
        return utente;
    }

    public static void setUtente(Utente nuovoUtente) {
        utente = nuovoUtente;
    }

    public static String getFragment() {
        return fragment;
    }

    public static void setFragment(String fragmentNuovo) {
        fragment = fragmentNuovo;
    }
}
