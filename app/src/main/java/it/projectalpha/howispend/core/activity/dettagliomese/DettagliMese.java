package it.projectalpha.howispend.core.activity.dettagliomese;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

import it.projectalpha.howispend.R;
import it.projectalpha.howispend.core.activity.home.CercaFragment;
import it.projectalpha.howispend.core.activity.home.HomeActivity;
import it.projectalpha.howispend.core.activity.home.HomeFragment;
import it.projectalpha.howispend.core.activity.home.InsightFragment;
import it.projectalpha.howispend.core.activity.home.ProfiloFragment;
import it.projectalpha.howispend.core.activity.login.LoginActivity;
import it.projectalpha.howispend.core.filters.CheckAuthFilter;
import it.projectalpha.howispend.model.Utente;
import it.projectalpha.howispend.utilities.Constants;
import it.projectalpha.howispend.utilities.IOHandler;
import it.projectalpha.howispend.utilities.Session;

public class DettagliMese extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        BottomNavigationView.OnNavigationItemSelectedListener {


    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private TextView nomeCognome;
    private TextView emailHeader;
    private BottomNavigationView navigation;


    private Session session;
    private Utente loggedUser;
    private Constants constants;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dettagli_mese_activity);


        session = Session.getInstance();
        constants = Constants.getInstance();

        CheckAuthFilter checkAuthFilter = CheckAuthFilter.getInstance();
        if(checkAuthFilter.filter()) {
            Intent logOut = new Intent(DettagliMese.this, LoginActivity.class);
            startActivity(logOut);
        }

        loggedUser = session.getLoggedUser();


        NavigationView navigationView = findViewById(R.id.menu_laterale);
        View header = navigationView.inflateHeaderView(R.layout.drawer_header);
        nomeCognome = header.findViewById(R.id.nomeCognomeDrawerTop);
        emailHeader = header.findViewById(R.id.emailDrawerTop);
        drawerLayout = findViewById(R.id.home_drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.apri, R.string.chiudi);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        initMenuRes();


        navigationView.setNavigationItemSelectedListener(this);

        loadFragment(new DettagliMeseFragment());

        //getting bottom navigation view and attaching the listener
        navigation = findViewById(R.id.navigation_dettagli_mese);
        navigation.setOnNavigationItemSelectedListener(this);

        initMenuRes();


    }


    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container_dettagli_mese, fragment)
                    .commit();
            return true;
        }
        return false;
    }



    @Override
    public void onBackPressed() {
        Intent intent = new Intent(DettagliMese.this, HomeActivity.class);
        startActivity(intent);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        int id = item.getItemId();

        switch(id) {

            case R.id.esci_drawer :
                IOHandler ioHandler = new IOHandler();
                ioHandler.deleteFile(getApplicationContext());
                Intent goToLogin = new Intent(DettagliMese.this, LoginActivity.class);
                startActivity(goToLogin);
                break;

            case R.id.dettagli_mese_navigation:
                fragment = new DettagliMeseFragment();
                break;

            case R.id.operazioni_mese_navigation:
                fragment = new ListaOperazioniMeseFragment();
                break;

            case R.id.insight_mese_navigation:
                fragment = new InsightMeseFragment();
                break;

            case R.id.cerca_mese_navigation:
                fragment = new CercaMeseFragment();
                break;

            default:
                break;
        }

        return loadFragment(fragment);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initMenuRes() {
        String nomeCognomeRes = loggedUser.getNome() + " " + loggedUser.getCognome();
        nomeCognome.setText(nomeCognomeRes);
        emailHeader.setText(loggedUser.getEmail());
    }
}
