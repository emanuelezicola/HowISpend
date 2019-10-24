package it.projectalpha.howispend.core.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

import it.projectalpha.howispend.R;
import it.projectalpha.howispend.core.LoginActivity;
import it.projectalpha.howispend.filters.CheckAuthFilter;
import it.projectalpha.howispend.utilities.IOHandler;
import it.projectalpha.howispend.utilities.Session;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener {

    private Session session;
    private CheckAuthFilter checkAuthFilter;


    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;

    public HomeActivity() {
    }

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
        navigationView = findViewById(R.id.menulaterale);
        drawerLayout = findViewById(R.id.home_drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.apri, R.string.chiudi);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
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

        if(toggle.onOptionsItemSelected(item))
            return true;

        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        //TODO dialog sicuro di voler uscire dall'app
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
}
