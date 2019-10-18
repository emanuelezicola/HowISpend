package it.projectalpha.howispend.core;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import it.projectalpha.howispend.R;
import it.projectalpha.howispend.filters.CheckAuthFilter;
import it.projectalpha.howispend.utilities.Session;

public class HomeActivity extends AppCompatActivity {

    private Session session;
    private CheckAuthFilter checkAuthFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        session = Session.getInstance();
        checkAuthFilter = CheckAuthFilter.getInstance();

        if(checkAuthFilter.filter()) {
            Intent logOut = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(logOut);
        }


        TextView textView = findViewById(R.id.homeText);
        String text = session.getLoggedUser().getNome() + " " +  session.getLoggedUser().getCognome();
        textView.setText(text);
    }

}
