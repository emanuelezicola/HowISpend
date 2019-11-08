package it.projectalpha.howispend.core.activity.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import it.projectalpha.howispend.R;
import it.projectalpha.howispend.core.activity.dettagliomese.DettagliMese;
import it.projectalpha.howispend.core.adapter.ListaMesiAdapter;
import it.projectalpha.howispend.core.dialog.DialogNuovoMese;
import it.projectalpha.howispend.model.Anno;
import it.projectalpha.howispend.model.Mese;
import it.projectalpha.howispend.model.Utente;
import it.projectalpha.howispend.utilities.Constants;
import it.projectalpha.howispend.utilities.InputTextUtils;
import it.projectalpha.howispend.utilities.Session;
import it.projectalpha.howispend.utilities.SnackbarUtils;

public class HomeFragment extends Fragment implements ListaMesiAdapter.ItemClickListener {

    private View view;

    private Spinner spinnerAnni;
    private RecyclerView listaMesiRecycler;
    private FloatingActionButton btnAddMese;


    private Constants constants;
    private Session session;
    private Utente loggedUser;


    private List<Anno> listaAnni = new ArrayList<>();
    private List<Mese> listaMesi = new ArrayList<>();
    private Anno currentAnno;

    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.home_fragment, null);



        spinnerAnni = view.findViewById(R.id.spinnerAnniInsight);
        btnAddMese = view.findViewById(R.id.btnAddMese);


        listaMesiRecycler = view.findViewById(R.id.listaMesi);
        listaMesiRecycler.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        listaMesiRecycler.setLayoutManager(llm);



        constants = Constants.getInstance();
        session = Session.getInstance();
        loggedUser = session.getLoggedUser();


        fetchListaAnni();




        spinnerAnni.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView anno = adapterView.findViewById(R.id.entriesSpinnerAnno);
                final String annoString = InputTextUtils.getTextFromTextView(anno);

                if(!annoString.equals(currentAnno.getAnno())) {
                    for(Anno annoObj : listaAnni) {
                        if(annoObj.getAnno().equals(annoString)) {
                            currentAnno = annoObj;
                        }
                    }
                    fetchListaMesiByAnnoId(String.valueOf(currentAnno.getId()));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnAddMese.setOnClickListener(view -> createDialogNuovoMese());


        return view;
    }


    private void setSpinnerList() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(view.getContext(), R.layout.spinner_anno_row);
        for(Anno anno : listaAnni) {
            adapter.add(anno.getAnno());
        }
        spinnerAnni.setAdapter(adapter);
    }


    private void populateListaMesi() {
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


    private void createDialogNuovoMese() {
        DialogNuovoMese dialogNuovoMese = new DialogNuovoMese();

        Bundle bundle = new Bundle();
        bundle.putSerializable("anno", currentAnno);

        dialogNuovoMese.setArguments(bundle);
        dialogNuovoMese.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "nuovoMeseDialog");
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
                        fetchListaMesiByAnnoId(String.valueOf(currentAnno.getId()));

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


    private void fetchListaMesiByAnnoId(final String annoId) {

        RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, constants.getURL_MESI_BY_ANNO(),

                serverResponse -> {

                    try {
                        JSONArray jsonArray = new JSONArray(serverResponse);
                        listaMesi.clear();

                        for(int i=0; i<jsonArray.length(); i++) {
                            Mese mese = constants.createMeseFromJson(jsonArray.getJSONObject(i));
                            listaMesi.add(mese);
                        }

                        populateListaMesi();

                    } catch (JSONException | ParseException e) {
                        e.printStackTrace();
                        SnackbarUtils.showLongSnackBar(view, "Qualcosa è andato storto");
                    }
                },
                volleyError -> fetchListaMesiByAnnoId(annoId))

        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("idAnno", annoId);

                return params;
            }
        };

        requestQueue.add(stringRequest);
    }


}
