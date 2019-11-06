package it.projectalpha.howispend.core.adapter;

import android.content.Context;
import android.graphics.Color;
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
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;

import java.util.ArrayList;
import java.util.List;

import it.projectalpha.howispend.R;
import it.projectalpha.howispend.model.Mese;

public class ListaMesiAdapter extends RecyclerView.Adapter<ListaMesiAdapter.MeseViewHolder> {


    List<Mese> listaMesi;
    ItemClickListener clickListener;

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

        meseViewHolder.labelEntrate.setText(isAperto ? "Saldo" : "Guadagno");

        String entrateContent = (isAperto? mese.getIntroiti() : mese.getGuadagno()) + " €";
        meseViewHolder.entrate.setText(entrateContent);

        String usciteContent = (isAperto ? mese.getSpesaParziale() : mese.getSpesaFinale()) + " €";
        meseViewHolder.uscite.setText(usciteContent);

        meseViewHolder.icMeseLocked.setVisibility(isAperto ? View.INVISIBLE : View.VISIBLE);
        disegnaGrafico(mese.getIntroiti(), isAperto ? mese.getSpesaParziale() : mese.getSpesaFinale(), meseViewHolder);
    }

    private void disegnaGrafico(Double introiti, Double spesa, MeseViewHolder meseViewHolder) {
        spesa = Math.abs(spesa);
        double saldoResiduo = introiti - spesa < 0 ? 0 : introiti - spesa;


        /*if(saldoResiduo == 0.0d && spesa == 0.0d) {
            saldoResiduo = 1;
            Toast.makeText(this, "Il grafico disegnato non è preciso. " +
                    "Devi inserire delle operazioni per visualizzare il grafico correttamente", Toast.LENGTH_LONG).show();
        }*/

        meseViewHolder.graficoAnteprima.setUsePercentValues(true);
        meseViewHolder.graficoAnteprima.setTouchEnabled(false);

        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(Float.parseFloat(""+spesa), "", 0));
        entries.add(new PieEntry(Float.parseFloat(""+saldoResiduo), "", 1));

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setDrawValues(false);
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(ContextCompat.getColor(context, R.color.red));
        colors.add(ContextCompat.getColor(context, R.color.colorAccent));
        dataSet.setColors(colors);
        meseViewHolder.graficoAnteprima.setDrawEntryLabels(false);




        PieData data = new PieData(dataSet);
        meseViewHolder.graficoAnteprima.setData(data);
        data.setValueFormatter(new DefaultValueFormatter(2));


        Description description = new Description();
        description.setText("");
        meseViewHolder.graficoAnteprima.setDescription(description);

        meseViewHolder.graficoAnteprima.getLegend().setEnabled(false);

        meseViewHolder.graficoAnteprima.setDrawHoleEnabled(false);
        meseViewHolder.graficoAnteprima.setTransparentCircleRadius(58f);
        meseViewHolder.graficoAnteprima.setHoleRadius(58f);
        meseViewHolder.graficoAnteprima.setRotationEnabled(false);
        meseViewHolder.graficoAnteprima.setClickable(false);
        //meseViewHolder.graficoAnteprima.animateY(1000, Easing.EasingOption.EaseInOutCubic);
        meseViewHolder.graficoAnteprima.setUsePercentValues(false);
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

        TextView labelEntrate;

        MeseViewHolder(@NonNull View itemView) {
            super(itemView);

            headerMese = itemView.findViewById(R.id.headerMese);
            entrate = itemView.findViewById(R.id.textEntrate);
            uscite = itemView.findViewById(R.id.textUscite);
            icMeseLocked = itemView.findViewById(R.id.icMeseLocked);
            graficoAnteprima = itemView.findViewById(R.id.graficoAnteprima);
            itemView.setOnClickListener(this);

            labelEntrate = itemView.findViewById(R.id.headerSaldoResiiduoMeseItem);
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
