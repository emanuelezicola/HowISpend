package it.projectalpha.howispend.core.activity.dettagliomese;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

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
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import it.projectalpha.howispend.R;
import it.projectalpha.howispend.core.activity.home.CercaFragment;
import it.projectalpha.howispend.core.activity.home.HomeActivity;
import it.projectalpha.howispend.core.activity.home.HomeFragment;
import it.projectalpha.howispend.core.activity.home.InsightFragment;
import it.projectalpha.howispend.core.activity.home.ProfiloFragment;
import it.projectalpha.howispend.core.activity.login.LoginActivity;
import it.projectalpha.howispend.core.filters.CheckAuthFilter;
import it.projectalpha.howispend.model.Anno;
import it.projectalpha.howispend.model.Mese;
import it.projectalpha.howispend.model.Utente;
import it.projectalpha.howispend.utilities.Constants;
import it.projectalpha.howispend.utilities.IOHandler;
import it.projectalpha.howispend.utilities.Session;
import it.projectalpha.howispend.utilities.SnackbarUtils;

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

    private Mese currentMese;
    private Anno annoDelMese;



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


        //getting bottom navigation view and attaching the listener
        navigation = findViewById(R.id.navigation_dettagli_mese);
        navigation.setOnNavigationItemSelectedListener(this);

        initMenuRes();

        Bundle bundle = Objects.requireNonNull(getIntent().getExtras());
        int idMese = bundle.getInt("meseId");

        fetchMeseById(idMese);


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
                fragment = new DettagliMeseFragment(currentMese, annoDelMese);
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


    private void fetchMeseById(Integer idMese) {

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, constants.getURL_MESE_BY_ID(),

                serverResponse -> {

                    try {
                        JSONArray jsonArray = new JSONArray(serverResponse);

                        currentMese = constants.createMeseFromJson(jsonArray.getJSONObject(0));

                        fetchAnnoByIdMese(currentMese.getIdAnno());

                    } catch (JSONException | ParseException e) {
                        e.printStackTrace();
                    }

                },
                volleyError -> SnackbarUtils.showShortSnackBar(drawerLayout, "Si è verificato un errore, prova di nuovo"))

        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("meseId", String.valueOf(idMese));
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    private void fetchAnnoByIdMese(Integer idAnno) {

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, constants.getURL_ANNO_BY_ID(),

                serverResponse -> {

                    try {
                        JSONArray jsonArray = new JSONArray(serverResponse);

                        annoDelMese = constants.createAnnoFromJson(jsonArray.getJSONObject(0));

                        loadFragment(new DettagliMeseFragment(currentMese, annoDelMese));

                    } catch (JSONException | ParseException e) {
                        e.printStackTrace();
                    }

                },
                volleyError -> SnackbarUtils.showShortSnackBar(navigation, "Si è verificato un errore, prova di nuovo"))

        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("idAnno", String.valueOf(idAnno));
                return params;
            }
        };

        requestQueue.add(stringRequest);


    }

}
