package it.projectalpha.howispend.core.adapter;

import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
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

    public ListaMesiAdapter(List<Mese> listaMesi, ItemClickListener clickListener) {
        this.listaMesi = listaMesi;
        this.clickListener = clickListener;
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
        meseViewHolder.headerMese.setText(mese.getMese());
        String entrateContent = mese.getIntroiti() + " €";
        meseViewHolder.entrate.setText(entrateContent);
        String usciteContent = (mese.getAperto() ? mese.getSpesaParziale() : mese.getSpesaFinale()) + " €";
        meseViewHolder.uscite.setText(usciteContent);
        meseViewHolder.icMeseLocked.setVisibility(mese.getAperto() ? View.INVISIBLE : View.VISIBLE);
        disegnaGrafico(mese.getIntroiti(), mese.getAperto() ? mese.getSpesaParziale() : mese.getSpesaFinale(), meseViewHolder);
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

        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(Float.parseFloat(""+spesa), "", 0));
        entries.add(new PieEntry(Float.parseFloat(""+saldoResiduo), "", 1));

        PieDataSet dataSet = new PieDataSet(entries, "Introiti");
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.RED);
        colors.add(Color.GREEN);
        dataSet.setColors(colors);


        PieData data = new PieData(dataSet);
        meseViewHolder.graficoAnteprima.setData(data);
        data.setValueFormatter(new DefaultValueFormatter(2));

        Description description = new Description();
        description.setText("Legenda");
        meseViewHolder.graficoAnteprima.setDescription(description);


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
