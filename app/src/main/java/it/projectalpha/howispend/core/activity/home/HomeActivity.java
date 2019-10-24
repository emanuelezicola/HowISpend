package it.projectalpha.howispend.core.activity.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

import it.projectalpha.howispend.R;
import it.projectalpha.howispend.core.activity.LoginActivity;
import it.projectalpha.howispend.core.dialog.DialogCambiaNomeCognome;
import it.projectalpha.howispend.core.filters.CheckAuthFilter;
import it.projectalpha.howispend.model.Utente;
import it.projectalpha.howispend.utilities.IOHandler;
import it.projectalpha.howispend.utilities.Session;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        BottomNavigationView.OnNavigationItemSelectedListener, DialogCambiaNomeCognome.CambiaNomeCognomeDialogListener {

    private Session session;
    private CheckAuthFilter checkAuthFilter;
    private Utente utente;


    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private View header;
    private TextView nomeCognome;
    private TextView emailHeader;

    private GestureDetectorCompat gestureDetectorCompat = null;


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

        utente = session.getLoggedUser();


        navigationView = findViewById(R.id.menu_laterale);
        header = navigationView.inflateHeaderView(R.layout.drawer_header);
        nomeCognome = header.findViewById(R.id.nomeCognomeDrawerTop);
        emailHeader = header.findViewById(R.id.emailDrawerTop);
        drawerLayout = findViewById(R.id.home_drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.apri, R.string.chiudi);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        initMenuRes();


        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
        }

        loadFragment(new HomeFragment());

        //getting bottom navigation view and attaching the listener
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);


    }


    private void initMenuRes() {
        String nomeCognomeRes = utente.getNome() + " " + utente.getCognome();
        nomeCognome.setText(nomeCognomeRes);
        emailHeader.setText(utente.getEmail());
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Fragment fragment = null;
        int id = item.getItemId();

        switch(id) {

            case R.id.profilo_drawer :
                drawerLayout.closeDrawers();
                Toast.makeText(getApplicationContext(), "Hai cliccato su profilo", Toast.LENGTH_SHORT).show();
                break;

            case R.id.esci_drawer :
                IOHandler ioHandler = new IOHandler();
                ioHandler.deleteFile(getApplicationContext());
                Intent goToLogin = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(goToLogin);
                break;

            case R.id.home_navigation:
                fragment = new HomeFragment();
                break;

            case R.id.insight_navigation:
                fragment = new InsightFragment();
                break;

            case R.id.cerca_navigation:
                fragment = new CercaFragment();
                break;

            case R.id.profilo_navigation:
                fragment = new ProfiloFragment();
                break;

            default:
                break;
        }

        return loadFragment(fragment);
    }


    @SuppressWarnings("all")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(toggle.onOptionsItemSelected(item)) {
            return true;
        }

        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Sei sicuro di voler uscire da How I Spend?");

        alertDialogBuilder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                finishAffinity();
                System.exit(0);
            }
        });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }



    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public void cambiaNome(String nuovoNome) {
        this.utente.setNome(nuovoNome);
        initMenuRes();
        loadFragment(new ProfiloFragment());
    }

    @Override
    public void cambiaCognome(String nuovoCognome) {
        this.utente.setCognome(nuovoCognome);
        initMenuRes();
        loadFragment(new ProfiloFragment());
    }
}
