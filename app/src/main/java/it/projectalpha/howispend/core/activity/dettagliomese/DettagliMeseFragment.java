package it.projectalpha.howispend.core.activity.dettagliomese;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;

import it.projectalpha.howispend.R;
import it.projectalpha.howispend.model.Anno;
import it.projectalpha.howispend.model.Mese;

public class DettagliMeseFragment extends Fragment {

    private View view;
    private TextView nomeMeseDettagli, annoMeseDettagli, introitiValueDettagliMese, speseValueDettagliMese, saldoValueDettagliMese,
            valueNEntrateDettagliMese, valueNUsciteDettagliMese;
    private ImageView meseAperto;
    private PieChart graficoMese;


    private Mese currentMese;
    private Anno annoDelMese;



    DettagliMeseFragment(Mese currentMese, Anno annoDelMese) {
        this.currentMese = currentMese;
        this.annoDelMese = annoDelMese;
    }

    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.dettagli_mese_fragment, null);


        nomeMeseDettagli = view.findViewById(R.id.nomeMeseDettagli);
        annoMeseDettagli = view.findViewById(R.id.annoMeseDettagli);
        introitiValueDettagliMese = view.findViewById(R.id.introitiValueDettagliMese);
        speseValueDettagliMese = view.findViewById(R.id.speseValueDettagliMese);
        saldoValueDettagliMese = view.findViewById(R.id.saldoValueDettagliMese);
        valueNEntrateDettagliMese = view.findViewById(R.id.valueNEntrateDettagliMese);
        valueNUsciteDettagliMese = view.findViewById(R.id.valueNUsciteDettagliMese);

        meseAperto = view.findViewById(R.id.meseApertoDettagliMese);
        graficoMese = view.findViewById(R.id.graficoDettagliMese);

        meseAperto.setVisibility(View.INVISIBLE);
        graficoMese.setVisibility(View.INVISIBLE);

        setEntries();





        return view;
    }


    private void setEntries() {
        nomeMeseDettagli.setText(currentMese.getMese());
        annoMeseDettagli.setText(annoDelMese.getAnno());
        meseAperto.setVisibility(currentMese.getAperto() ? View.INVISIBLE : View.VISIBLE);
        introitiValueDettagliMese.setText(String.valueOf(currentMese.getIntroiti()));
        speseValueDettagliMese.setText(String.valueOf(currentMese.getAperto() ? currentMese.getSpesaParziale() : currentMese.getSpesaFinale()));
        saldoValueDettagliMese.setText(String.valueOf(currentMese.getIntroiti() -
                Math.abs( (currentMese.getAperto() ? currentMese.getSpesaParziale() : currentMese.getSpesaFinale()) ) ));
        valueNEntrateDettagliMese.setText("2");
        valueNUsciteDettagliMese.setText("9");

        disegnaGrafico();

    }


    private void disegnaGrafico() {

        float spesa = Math.abs( (currentMese.getAperto() ? currentMese.getSpesaParziale().floatValue() : currentMese.getSpesaFinale().floatValue()) );
        float rimanenza = currentMese.getIntroiti().floatValue() - spesa;

        if(spesa == 0 && rimanenza == 0) {
            rimanenza = 1;
        }


        Description d = new Description();
        d.setText("Andamento del mese");
        graficoMese.setDescription(d);

        graficoMese.setTouchEnabled(false);

        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(spesa, "Spese", 0));
        entries.add(new PieEntry(rimanenza, "Rimanenze", 1));

        PieDataSet dataSet = new PieDataSet(entries, "");
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(ContextCompat.getColor(view.getContext(), R.color.red));
        colors.add(ContextCompat.getColor(view.getContext(), R.color.colorAccent));
        dataSet.setColors(colors);


        PieData data = new PieData(dataSet);
        graficoMese.setData(data);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        graficoMese.setUsePercentValues(true);
        graficoMese.setDrawEntryLabels(false);

        Description description = new Description();
        description.setText("Grafico del Mese");
        graficoMese.setDescription(description);

        Legend legend = graficoMese.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setXEntrySpace(5f);
        legend.setDrawInside(false);

        //meseViewHolder.graficoAnteprima.setMinOffset(2);
        graficoMese.setDrawHoleEnabled(false);
        graficoMese.setTransparentCircleRadius(58f);
        graficoMese.setHoleRadius(58f);
        graficoMese.setRotationEnabled(false);
        graficoMese.setClickable(false);

        graficoMese.setVisibility(View.VISIBLE);





    }

}
