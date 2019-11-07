package it.projectalpha.howispend.core.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import it.projectalpha.howispend.R;
import it.projectalpha.howispend.core.activity.LoginActivity;
import it.projectalpha.howispend.core.dialog.DialogCambiaNomeCognome;
import it.projectalpha.howispend.core.dialog.DialogCambioPassword;
import it.projectalpha.howispend.core.dialog.DialogNuovoMese;
import it.projectalpha.howispend.core.filters.CheckAuthFilter;
import it.projectalpha.howispend.model.Anno;
import it.projectalpha.howispend.model.Utente;
import it.projectalpha.howispend.utilities.Constants;
import it.projectalpha.howispend.utilities.IOHandler;
import it.projectalpha.howispend.utilities.Session;
import it.projectalpha.howispend.utilities.SnackbarUtils;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        BottomNavigationView.OnNavigationItemSelectedListener, DialogCambiaNomeCognome.CambiaNomeCognomeDialogListener,
        DialogCambioPassword.CambioPasswordDialogListener, DialogNuovoMese.NuovoMeseListener {

    private Session session;
    private Utente utente;
    private Constants constants;


    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private TextView nomeCognome;
    private TextView emailHeader;

    private ConstraintLayout constraintLayoutActivity;
    private BottomNavigationView navigation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        session = Session.getInstance();
        constants = Constants.getInstance();
        CheckAuthFilter checkAuthFilter = CheckAuthFilter.getInstance();

        if(checkAuthFilter.filter()) {
            Intent logOut = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(logOut);
        }

        utente = session.getLoggedUser();

        constraintLayoutActivity = findViewById(R.id.content_full_activity);

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

        loadFragment(new HomeFragment());

        //getting bottom navigation view and attaching the listener
        navigation = findViewById(R.id.navigation);
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

        alertDialogBuilder.setPositiveButton("Si", (arg0, arg1) -> {
            finishAffinity();
            System.exit(0);
        });

        alertDialogBuilder.setNegativeButton("No", (dialog, which) -> {});

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
        modificaUtente(nuovoNome, utente.getCognome(), utente.getPassword(), ""+utente.getId());
    }

    @Override
    public void cambiaCognome(String nuovoCognome) {
        modificaUtente(utente.getNome(), nuovoCognome, utente.getPassword(), ""+utente.getId());
    }

    @Override
    public void cambioPassword(String nuovaPassword) {
        modificaUtente(utente.getNome(), utente.getCognome(), nuovaPassword, ""+utente.getId());
    }

    @Override
    public void aggiungiMese(String mese, double introiti, Anno anno) {
        aggiungiMeseOnDb(mese, introiti, anno);
    }

    @Override
    public void error(String error) {
        SnackbarUtils.showLongSnackBar(constraintLayoutActivity, error);
    }


    private void modificaUtente(final String nome, final String cognome, final String password, final String id) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, constants.getURL_MODIFICA_UTENTE(),
                serverResponse -> {

                    try {
                        JSONArray jsonArray = new JSONArray(serverResponse);

                        JSONObject jsonObject = jsonArray.getJSONObject(0);

                        Utente nuovoUtente = constants.createUserFromJson(jsonObject);

                        session.setLoggedUser(nuovoUtente);
                        utente = nuovoUtente;

                        initMenuRes();
                        loadFragment(new ProfiloFragment());
                        SnackbarUtils.showLongSnackBar(constraintLayoutActivity, "Le informazioni sono state aggiornate correttamente");

                    } catch (JSONException | ParseException e) {
                        e.printStackTrace();
                    }
                },
                volleyError -> {
                    SnackbarUtils.showShortSnackBar(constraintLayoutActivity, "Si è verificato un errore.");
                    initMenuRes();
                    loadFragment(new ProfiloFragment());
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("id", id);
                params.put("nome", nome);
                params.put("cognome", cognome);
                String passwordEncoded = Base64.getEncoder().encodeToString(password.getBytes());
                params.put("password", passwordEncoded);

                return params;
            }
        };

        requestQueue.add(stringRequest);
    }


    private void aggiungiMeseOnDb(String mese, double introiti, Anno anno) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, constants.getURL_NUOVO_MESE(),

                serverResponse -> {
                    if("1".equals(serverResponse)) {
                        loadFragment(new HomeFragment());
                    } else {
                        SnackbarUtils.showShortSnackBar(constraintLayoutActivity, "Si è verificato un errore");
                    }
                },

                volleyError -> SnackbarUtils.showShortSnackBar(constraintLayoutActivity, "Si è verificato un errore."))
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("nomeMese", mese);
                params.put("utenteId", String.valueOf(session.getLoggedUser().getId()));
                params.put("dataCreazione", LocalDate.now().toString());
                params.put("annoId", String.valueOf(anno.getId()));
                params.put("introiti", String.valueOf(introiti));

                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

}
