package it.projectalpha.howispend;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import it.projectalpha.howispend.utilities.Session;

public class HomeActivity extends AppCompatActivity {

    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        session = Session.getInstance();

        //TODO account loggato

        TextView textView = findViewById(R.id.homeText);
        String text = session.getLoggedUser().getNome() + " " +  session.getLoggedUser().getCognome();
        textView.setText(text);
    }

}
