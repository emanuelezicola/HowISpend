package it.projectalpha.howispend.core.activity.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.textfield.TextInputEditText;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;

import it.projectalpha.howispend.R;
import it.projectalpha.howispend.core.activity.dettagliomese.DettagliMese;
import it.projectalpha.howispend.core.adapter.ListaMesiAdapter;
import it.projectalpha.howispend.model.Anno;
import it.projectalpha.howispend.model.Mese;
import it.projectalpha.howispend.model.Utente;
import it.projectalpha.howispend.utilities.Constants;
import it.projectalpha.howispend.utilities.InputTextUtils;
import it.projectalpha.howispend.utilities.Session;
import it.projectalpha.howispend.utilities.SnackbarUtils;

public class CercaFragment extends Fragment implements ListaMesiAdapter.ItemClickListener{

    private View view;

    private Chip chipGuadagnoSpesa;
    private Spinner spinnerAnni;
    private TextInputEditText min, max;
    private MaterialButton btnCerca;
    private RecyclerView listaMesiRecycler;


    private Constants constants;
    private Utente loggedUser;
    private List<Anno> listaAnni = new ArrayList<>();
    private List<Mese> listaMesi = new ArrayList<>();
    private Anno currentAnno;


    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.home_cerca_fragment, null);

        constants = Constants.getInstance();
        Session session = Session.getInstance();
        loggedUser = session.getLoggedUser();



        chipGuadagnoSpesa = view.findViewById(R.id.chipGuadagnoSpesa);
        chipGuadagnoSpesa.setChecked(true);
        setChipValues(chipGuadagnoSpesa, getResources().getString(R.string.guadagno),
                getResources().getColorStateList(R.color.colorAccentDarkTrasparent, null),
                ContextCompat.getDrawable(view.getContext(), R.drawable.ic_guadagno));


        spinnerAnni = view.findViewById(R.id.spinnerAnnoCerca);
        min = view.findViewById(R.id.importoMinimo);
        max = view.findViewById(R.id.importoMassimo);
        btnCerca = view.findViewById(R.id.cercaHomeBtn);
        btnCerca.setClickable(false);

        listaMesiRecycler = view.findViewById(R.id.listaRisultati);
        listaMesiRecycler.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        listaMesiRecycler.setLayoutManager(llm);


        chipGuadagnoSpesa.setOnCheckedChangeListener((compoundButton, b) -> {
            if(chipGuadagnoSpesa.isChecked()) {
                setChipValues(chipGuadagnoSpesa, getResources().getString(R.string.guadagno),
                        getResources().getColorStateList(R.color.colorAccentDarkTrasparent, null),
                        ContextCompat.getDrawable(view.getContext(), R.drawable.ic_guadagno));
            } else {
                setChipValues(chipGuadagnoSpesa, getResources().getString(R.string.spesa),
                        getResources().getColorStateList(R.color.redTransparent, null),
                        ContextCompat.getDrawable(view.getContext(), R.drawable.ic_spesa));
            }
        });




        fetchListaAnni();


        btnCerca.setOnClickListener(view -> {
            String importoMinimoString = InputTextUtils.getTextFromTextInput(min);
            String importoMassimoString = InputTextUtils.getTextFromTextInput(max);

            Double importoMinimo = null;
            Double importoMassimo = null;

            if(!StringUtils.isBlank(importoMinimoString)) {
                importoMinimo = Double.parseDouble(importoMinimoString);
            }

            if(!StringUtils.isBlank(importoMassimoString)) {
                importoMassimo = Double.parseDouble(importoMassimoString);
            }

            if(importoMinimo != null && importoMassimo != null) {
                if(importoMassimo < importoMinimo) {
                    SnackbarUtils.showShortSnackBar(view, "L'importo massimo non può essere più piccolo dell'importo minimo!");
                    InputTextUtils.setError(max, "Importo massimo minore di importo minimo");
                    return;
                }
            }

            boolean guadagno = chipGuadagnoSpesa.isChecked();

            String anno = String.valueOf(currentAnno.getId());

            fetchMesiByFilters(importoMinimo, importoMassimo, guadagno, anno);
        });





        return view;
    }



    private void setSpinnerList() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(view.getContext(), R.layout.spinner_anno_row);
        for(Anno anno : listaAnni) {
            adapter.add(anno.getAnno());
        }
        spinnerAnni.setAdapter(adapter);
        btnCerca.setClickable(true);
    }

    private void popolaListaMesi() {
        SortedSet<Mese> monthSet = new TreeSet<>((o1, o2) -> {
            try {
                SimpleDateFormat fmt = new SimpleDateFormat("MMM", Locale.ITALY);
                return currentAnno.getAperto() ?
                        Objects.requireNonNull(fmt.parse(o2.getMese())).compareTo(fmt.parse(o1.getMese())) :
                        Objects.requireNonNull(fmt.parse(o1.getMese())).compareTo(fmt.parse(o2.getMese()));
            } catch (ParseException ex) {
                return o1.compareTo(o2);
            }
        });

        monthSet.addAll(listaMesi);

        ListaMesiAdapter adapter = new ListaMesiAdapter(new ArrayList<>(monthSet), this, view.getContext());
        listaMesiRecycler.setAdapter(adapter);
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        listaMesiRecycler.setClickable(false);
        Mese mese = listaMesi.get(clickedItemIndex);
        Bundle bundle = new Bundle();
        bundle.putInt("meseId", mese.getId());

        Intent intent = new Intent(view.getContext(), DettagliMese.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }


    private void setChipValues(Chip chip, String title, ColorStateList backgroundColor, Drawable icon) {
        chip.setText(title);
        chip.setChipBackgroundColor(backgroundColor);
        chip.setChipIcon(icon);
    }



    private void fetchListaAnni() {

        RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
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



    private void fetchMesiByFilters(Double importoMinimo, Double importoMassimo, boolean guadagno, String annoId) {

        RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, constants.getURL_RICERCA(),

                serverResponse -> {
                    try {
                        JSONArray jsonArray = new JSONArray(serverResponse);

                        if(jsonArray.length() == 0) {
                            SnackbarUtils.showShortSnackBar(view, "La ricerca non ha prodotto alcun risultato");
                            listaMesi.clear();
                            popolaListaMesi();
                            return;
                        }

                        for(int i=0; i<jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Mese mese = constants.createMeseFromJson(jsonObject);
                            listaMesi.add(mese);
                        }

                        popolaListaMesi();

                    } catch (JSONException | ParseException e) {
                        e.printStackTrace();
                    }
                },
                volleyError -> SnackbarUtils.showShortSnackBar(view, "Si è verificato un errore, prova di nuovo"))

        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("utenteId", String.valueOf(loggedUser.getId()));
                params.put("importoMinimo", String.valueOf(importoMinimo == null ? "" : importoMinimo));
                params.put("importoMassimo", String.valueOf(importoMassimo == null ? "" : importoMassimo));
                params.put("annoId", annoId);
                params.put("guadagno", guadagno ? "1" : "0");

                return params;
            }
        };

        requestQueue.add(stringRequest);


    }


}
