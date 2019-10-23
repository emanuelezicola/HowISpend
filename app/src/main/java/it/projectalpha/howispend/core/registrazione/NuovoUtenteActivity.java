package it.projectalpha.howispend.core.registrazione;

import androidx.appcompat.app.AppCompatActivity;
import it.projectalpha.howispend.R;
import it.projectalpha.howispend.core.LoginActivity;

import android.content.Intent;
import android.os.Bundle;

public class NuovoUtenteActivity extends AppCompatActivity {

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
        Intent intent = new Intent(NuovoUtenteActivity.this, LoginActivity.class);
        startActivity(intent);
    }

}
