package it.projectalpha.howispend.core.activity.home;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.chip.Chip;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import it.projectalpha.howispend.R;
import it.projectalpha.howispend.model.Anno;
import it.projectalpha.howispend.model.Utente;
import it.projectalpha.howispend.utilities.Constants;
import it.projectalpha.howispend.utilities.Session;
import it.projectalpha.howispend.utilities.SnackbarUtils;

public class CercaFragment extends Fragment {

    private View view;

    private Chip chipGuadagnoSpesa;
    private Spinner spinnerAnni;


    private Constants constants;
    private Session session;
    private Utente loggedUser;
    private List<Anno> listaAnni = new ArrayList<>();
    private Anno currentAnno;


    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.home_cerca_fragment, null);

        constants = Constants.getInstance();
        session = Session.getInstance();
        loggedUser = session.getLoggedUser();



        chipGuadagnoSpesa = view.findViewById(R.id.chipGuadagnoSpesa);
        chipGuadagnoSpesa.setChecked(true);
        spinnerAnni = view.findViewById(R.id.spinnerAnnoCerca);


        chipGuadagnoSpesa.setOnCheckedChangeListener((compoundButton, b) -> {
            if(chipGuadagnoSpesa.isChecked()) {
                chipGuadagnoSpesa.setText(getResources().getString(R.string.guadagno));
                chipGuadagnoSpesa.setChipBackgroundColor(getResources().getColorStateList(R.color.colorAccentDarkTrasparent, null));
                chipGuadagnoSpesa.setChipIcon(ContextCompat.getDrawable(view.getContext(), R.drawable.ic_guadagno));
            } else {
                chipGuadagnoSpesa.setText(getResources().getString(R.string.spesa));
                chipGuadagnoSpesa.setChipBackgroundColor(getResources().getColorStateList(R.color.redTransparent, null));
                chipGuadagnoSpesa.setChipIcon(ContextCompat.getDrawable(view.getContext(), R.drawable.ic_spesa));
            }
        });


        fetchListaAnni();









        return view;
    }



    private void setSpinnerList() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(view.getContext(), R.layout.spinner_anno_row);
        for(Anno anno : listaAnni) {
            adapter.add(anno.getAnno());
        }
        spinnerAnni.setAdapter(adapter);
    }



    private void fetchListaAnni() {

        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getContext()));
        StringRequest stringRequest = new StringRequest(Request.Method.POST, constants.getURL_ALL_ANNI(),

                serverResponse -> {

                    try {
                        JSONArray jsonArray = new JSONArray(serverResponse);

                        listaAnni.clear();
                        for(int i=0; i<jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Anno anno = constants.createAnnoFromJson(jsonObject);
                            listaAnni.add(anno);
                            if(i==0) {
                                currentAnno = anno;
                            }
                        }

                        setSpinnerList();
                        //fetchListaMesiByAnnoId(String.valueOf(currentAnno.getId()));

                    } catch (JSONException | ParseException e) {
                        e.printStackTrace();
                        SnackbarUtils.showLongSnackBar(view, "Qualcosa è andato storto");
                    }
                },
                volleyError -> fetchListaAnni())

        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("utenteId", String.valueOf(loggedUser.getId()));

                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

}
