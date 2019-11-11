package it.projectalpha.howispend.core.activity.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.DefaultAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;

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
import java.util.stream.Collectors;

import it.projectalpha.howispend.R;
import it.projectalpha.howispend.model.Anno;
import it.projectalpha.howispend.model.Mese;
import it.projectalpha.howispend.model.Utente;
import it.projectalpha.howispend.utilities.Constants;
import it.projectalpha.howispend.utilities.InputTextUtils;
import it.projectalpha.howispend.utilities.Session;
import it.projectalpha.howispend.utilities.SnackbarUtils;

public class InsightFragment extends Fragment {

    private View view;



    private Spinner spinnerAnni;
    private HorizontalBarChart horizontalBarChart;



    private Anno currentAnno;
    private Constants constants;
    private Utente loggedUser;

    private List<Anno> listaAnni = new ArrayList<>();
    private List<Mese> listaMesi = new ArrayList<>();

    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.home_insight_fragment, null);

        spinnerAnni = view.findViewById(R.id.spinnerAnniInsight);
        horizontalBarChart = view.findViewById(R.id.mesiInsight);

        horizontalBarChart.setVisibility(View.INVISIBLE);



        constants = Constants.getInstance();
        Session session = Session.getInstance();
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












        return view;
    }



    private void setSpinnerList() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(view.getContext(), R.layout.spinner_anno_row);
        for(Anno anno : listaAnni) {
            adapter.add(anno.getAnno());
        }
        spinnerAnni.setAdapter(adapter);
    }




    private void disegnaGrafico() {

        SortedSet<Mese> monthSet = new TreeSet<>((o1, o2) -> {
            try {
                SimpleDateFormat fmt = new SimpleDateFormat("MMM", Locale.ITALY);
                return Objects.requireNonNull(fmt.parse(o2.getMese())).compareTo(fmt.parse(o1.getMese()));
            } catch (ParseException ex) {
                return o1.compareTo(o2);
            }
        });

        monthSet.addAll(listaMesi);

        List<String> months = monthSet.stream().map(x -> x.getMese().substring(0, 3)).collect(Collectors.toList());


        horizontalBarChart.setPinchZoom(false);
        horizontalBarChart.setScaleEnabled(false);
        horizontalBarChart.setDrawGridBackground(false);
        horizontalBarChart.setScaleXEnabled(false);
        horizontalBarChart.setScaleYEnabled(false);
        horizontalBarChart.setDrawBarShadow(false);

        float barWidth = 0.3f;
        float barSpace = 0f;
        float groupSpace = 0.4f;


        List<BarEntry> yVals1 = getData(monthSet, true);
        List<BarEntry> yVals2 = getData(monthSet, false);


        BarDataSet set1, set2;
        set1 = new BarDataSet(yVals1, "Entrate");
        set1.setColor(ContextCompat.getColor(view.getContext(), R.color.colorAccent));
        set1.setValueTextSize(10);
        set1.setValueTextColor(ContextCompat.getColor(view.getContext(), R.color.colorAccent));

        set2 = new BarDataSet(yVals2, "Uscite");
        set2.setColor(ContextCompat.getColor(view.getContext(), R.color.red));
        set2.setValueTextSize(10);
        set2.setValueTextColor(ContextCompat.getColor(view.getContext(), R.color.red));


        BarData data = new BarData(set2, set1);
        data.setValueFormatter(new LargeValueFormatter());
        horizontalBarChart.setData(data);
        horizontalBarChart.getBarData().setBarWidth(barWidth);

        horizontalBarChart.getDescription().setText("Andamento dei mesi");
        horizontalBarChart.getDescription().setTextSize(10);


        horizontalBarChart.getXAxis().setAxisMinimum(0);
        horizontalBarChart.getXAxis().setAxisMaximum(0 + horizontalBarChart.getBarData().getGroupWidth(groupSpace, barSpace) * 2);
        horizontalBarChart.groupBars(0, groupSpace, barSpace);
        horizontalBarChart.getData().setHighlightEnabled(false);
        horizontalBarChart.invalidate();
        horizontalBarChart.animateY(1000);


        Legend l = horizontalBarChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setYOffset(20f);
        l.setXOffset(0f);
        l.setYEntrySpace(0f);
        l.setTextSize(10f);


        //X-axis
        XAxis xAxis = horizontalBarChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setCenterAxisLabels(true);
        xAxis.setDrawGridLines(true);
        xAxis.setAxisMaximum(yVals2.size());
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(months));
        xAxis.setLabelCount(months.size());

        //Y-axis
        horizontalBarChart.getAxisRight().setEnabled(false);
        YAxis leftAxis = horizontalBarChart.getAxisLeft();
        leftAxis.setValueFormatter(new DefaultAxisValueFormatter(2));
        leftAxis.setDrawGridLines(true);
        leftAxis.setSpaceTop(35f);
        leftAxis.setAxisMinimum(0f);

        horizontalBarChart.setVisibility(View.VISIBLE);

    }


    private List<BarEntry> getData(SortedSet<Mese> mesi, boolean isEntrate) {
        List<BarEntry> result = new ArrayList<>();


        List<Integer> entrate;

        if(isEntrate) {
          entrate = mesi.stream().map(x -> x.getIntroiti().intValue()).collect(Collectors.toList());
        }
        else {
            entrate = mesi.stream()
                    .map(x -> x.getAperto() ? Math.abs(x.getSpesaParziale().intValue()) : Math.abs(x.getSpesaFinale().intValue()))
                    .collect(Collectors.toList());
        }

        int counter = 0;
        for(double entrata : entrate) {
            result.add(new BarEntry(counter, (float)entrata));
            counter++;
        }
        return result;
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

                        disegnaGrafico();

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
