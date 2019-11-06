package it.projectalpha.howispend.core.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;
import java.util.List;

import it.projectalpha.howispend.R;
import it.projectalpha.howispend.model.Mese;

public class ListaMesiAdapter extends RecyclerView.Adapter<ListaMesiAdapter.MeseViewHolder> {


    private List<Mese> listaMesi;
    private ItemClickListener clickListener;

    private Context context;

    public ListaMesiAdapter(List<Mese> listaMesi, ItemClickListener clickListener, Context context) {
        this.listaMesi = listaMesi;
        this.clickListener = clickListener;
        this.context = context;
    }


    @Override
    public int getItemCount() {
        return listaMesi.size();
    }

    @NonNull
    @Override
    public MeseViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_item_mese_lista_mesi, viewGroup, false);
        return new MeseViewHolder(v);
    }


    @Override
    public void onBindViewHolder(MeseViewHolder meseViewHolder, int i) {
        Mese mese = listaMesi.get(i);
        Boolean isAperto = mese.getAperto();

        meseViewHolder.headerMese.setText(mese.getMese());

        String entrateContent = (isAperto? mese.getIntroiti() - Math.abs(mese.getSpesaParziale()) : mese.getGuadagno()) + " €";
        meseViewHolder.entrate.setText(entrateContent);

        String usciteContent = (isAperto ? mese.getSpesaParziale() : mese.getSpesaFinale()) + " €";
        meseViewHolder.uscite.setText(usciteContent);

        meseViewHolder.icMeseLocked.setVisibility(isAperto ? View.INVISIBLE : View.VISIBLE);

        disegnaGrafico(isAperto ? mese.getIntroiti() - Math.abs(mese.getSpesaParziale()) : mese.getGuadagno(),
                       isAperto ? mese.getSpesaParziale() : mese.getSpesaFinale(),
                       meseViewHolder);
    }

    private void disegnaGrafico(Double saldoGuadagno, Double spesa, MeseViewHolder meseViewHolder) {
        spesa = Math.abs(spesa);


        if(saldoGuadagno ==0.0d && spesa == 0.0d) {
            saldoGuadagno = 1d;
        }


        Description d = new Description();
        d.setText("Andamento del mese");
        meseViewHolder.graficoAnteprima.setDescription(d);

        meseViewHolder.graficoAnteprima.setTouchEnabled(false);

        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(Float.parseFloat(""+spesa), "Spese", 0));
        entries.add(new PieEntry(Float.parseFloat(""+saldoGuadagno), "Rimanenze", 1));

        PieDataSet dataSet = new PieDataSet(entries, "");
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(ContextCompat.getColor(context, R.color.red));
        colors.add(ContextCompat.getColor(context, R.color.colorAccent));
        dataSet.setColors(colors);
        dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setValueLinePart1OffsetPercentage(90.f);
        dataSet.setValueLinePart1Length(0.8f);
        dataSet.setValueLinePart2Length(.2f);
        dataSet.setSelectionShift(15);


        PieData data = new PieData(dataSet);
        meseViewHolder.graficoAnteprima.setData(data);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        meseViewHolder.graficoAnteprima.setUsePercentValues(true);
        meseViewHolder.graficoAnteprima.setDrawEntryLabels(false);

        Description description = new Description();
        description.setText("Grafico del Mese");
        meseViewHolder.graficoAnteprima.setDescription(description);

        Legend legend = meseViewHolder.graficoAnteprima.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setXEntrySpace(5f);
        legend.setDrawInside(false);

        //meseViewHolder.graficoAnteprima.setMinOffset(2);
        meseViewHolder.graficoAnteprima.setDrawHoleEnabled(false);
        meseViewHolder.graficoAnteprima.setTransparentCircleRadius(58f);
        meseViewHolder.graficoAnteprima.setHoleRadius(58f);
        meseViewHolder.graficoAnteprima.setRotationEnabled(false);
        meseViewHolder.graficoAnteprima.setClickable(false);

    }


    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    class MeseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView headerMese;
        TextView entrate;
        TextView uscite;
        ImageView icMeseLocked;
        PieChart graficoAnteprima;


        MeseViewHolder(@NonNull View itemView) {
            super(itemView);

            headerMese = itemView.findViewById(R.id.headerMese);
            entrate = itemView.findViewById(R.id.textEntrate);
            uscite = itemView.findViewById(R.id.textUscite);
            icMeseLocked = itemView.findViewById(R.id.icMeseLocked);
            graficoAnteprima = itemView.findViewById(R.id.graficoAnteprima);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            clickListener.onListItemClick(clickedPosition);
        }
    }

    public interface ItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }
}
