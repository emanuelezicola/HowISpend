package it.projectalpha.howispend.core;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.text.ParseException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import it.projectalpha.howispend.R;
import it.projectalpha.howispend.model.Utente;
import it.projectalpha.howispend.utilities.ButtonUtils;
import it.projectalpha.howispend.utilities.Constants;
import it.projectalpha.howispend.utilities.IOHandler;
import it.projectalpha.howispend.utilities.InputTextUtils;
import it.projectalpha.howispend.utilities.Session;
import it.projectalpha.howispend.utilities.SnackbarUtils;

public class LoginActivity extends AppCompatActivity {

    private MaterialButton buttonLogin, nuovoAccountBtn;
    private TextInputEditText emailInput, passwordInput;
    private CheckBox rimaniCollegato;

    private Constants constants;
    private Session session;
    private IOHandler ioHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        constants = Constants.getInstance();
        session = Session.getInstance();
        ioHandler = new IOHandler();


        buttonLogin = findViewById(R.id.login_btn);
        nuovoAccountBtn = findViewById(R.id.nuovo_account);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        rimaniCollegato = findViewById(R.id.rimaniCollegato);


        //Controllo rimani collegato
        String readed = ioHandler.readFromFile(LoginActivity.this);
        if(!"".equals(readed)) {
            String[] readedSplitted = readed.split("&&&");

            for(String string : readedSplitted) {
                Log.e("CONTENT", string);
            }

            String savedEmail = readedSplitted[0].trim();
            String savedPsw = readedSplitted[1].trim();
            String passwordEncoded = Base64.getEncoder().encodeToString(savedPsw.getBytes());
            emailInput.setText(savedEmail);
            passwordInput.setText(savedPsw);
            rimaniCollegato.setChecked(Boolean.valueOf(readedSplitted[2].trim()));
            doLogin(savedEmail, passwordEncoded);
        }


        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailString = constants.getTextFromTextInput(emailInput);
                String passwordString = constants.getTextFromTextInput(passwordInput);

                if(invalidInput(emailString, passwordString)) {
                    return;
                }

                String passwordEncoded = Base64.getEncoder().encodeToString(passwordString.getBytes());
                doLogin(emailString, passwordEncoded);
            }
        });


        nuovoAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, NuovoUtenteActivity.class);
                startActivity(intent);
            }
        });


    }

    private void doLogin(final String email, final String password) {
        enableButtons(false);

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, constants.getURL_LOGIN(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String serverResponse) {

                        try{
                            JSONArray jsonArray = new JSONArray(serverResponse);

                            Utente utente = constants.createUserFromJson(jsonArray.getJSONObject(0));
                            session.setLoggedUser(utente);

                            if(rimaniCollegato.isChecked()) {
                                String toBeSaved = utente.getEmail() + " &&& " + utente.getPassword() + " &&& true";
                                try {
                                    ioHandler.writeToFile(toBeSaved, LoginActivity.this);
                                    SnackbarUtils.showShortSnackBar(buttonLogin, "Verrai ricordato al prossimo accesso");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    SnackbarUtils.showShortSnackBar(buttonLogin,
                                            "Qualcosa è andato storto nel salvare le credenziali");
                                }
                            }

                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);


                        } catch(JSONException | ParseException e)  {
                            e.printStackTrace();
                            SnackbarUtils.showShortSnackBar(buttonLogin, "Username o password errati");
                            setUsernameAndPasswordInputError();
                        }
                        finally {
                            enableButtons(true);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        SnackbarUtils.showShortSnackBar(buttonLogin, "Si è verificato un errore, riprova più tardi");
                        enableButtons(true);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("email", email);
                params.put("password", password);

                return params;
            }
        };

        requestQueue.add(stringRequest);
    }


    private boolean invalidInput(String email, String password) {
        boolean result = false;

        if(StringUtils.isBlank(email)) {
            InputTextUtils.setError(emailInput, "Inserire l'email");
            result = true;
        }
        if (StringUtils.isBlank(password)) {
            InputTextUtils.setError(passwordInput, "Inserire la password");
            result = true;
        }

        return result;
    }


    private void setUsernameAndPasswordInputError(){
        InputTextUtils.setError(emailInput, "Controlla l'email");
        InputTextUtils.setError(passwordInput, "Controlla la password");
    }

    private void enableButtons(boolean value){
        ButtonUtils.enableButton(buttonLogin, value);
        ButtonUtils.enableButton(nuovoAccountBtn, value);
    }
}
