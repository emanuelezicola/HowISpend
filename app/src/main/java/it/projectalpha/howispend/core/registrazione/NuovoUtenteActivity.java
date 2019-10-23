package it.projectalpha.howispend.core.registrazione;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import it.projectalpha.howispend.R;
import it.projectalpha.howispend.core.LoginActivity;
import it.projectalpha.howispend.model.Utente;

import android.content.Intent;
import android.os.Bundle;

public class NuovoUtenteActivity extends AppCompatActivity {

    private static Utente utente = new Utente();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nuovo_utente_activity);

        if (savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.nuovoUtenteFrame, new RegistrazioneFirstFragment()).commit();
        }


    }


    @Override
    public void onBackPressed() {
        //TODO navigazione tra fragment con tasto indietro
        utente = new Utente();
        Intent intent = new Intent(NuovoUtenteActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    public static Utente getUtente() {
        return utente;
    }

    public static void setUtente(Utente nuovoUtente) {
        utente = nuovoUtente;
    }
}
