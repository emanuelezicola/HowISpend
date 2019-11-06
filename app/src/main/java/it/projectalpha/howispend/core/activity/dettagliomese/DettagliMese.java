package it.projectalpha.howispend.core.activity.dettagliomese;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import it.projectalpha.howispend.R;
import it.projectalpha.howispend.core.activity.home.HomeActivity;

public class DettagliMese extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dettagli_mese_activity);
    }



    @Override
    public void onBackPressed() {
        Intent intent = new Intent(DettagliMese.this, HomeActivity.class);
        startActivity(intent);
    }
}
