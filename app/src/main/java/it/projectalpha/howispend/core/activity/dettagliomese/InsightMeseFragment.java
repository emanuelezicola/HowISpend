package it.projectalpha.howispend.core.activity.dettagliomese;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import it.projectalpha.howispend.R;

public class InsightMeseFragment extends Fragment {

    private View view;

    @SuppressLint("InflateParams")
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.dettagli_mese_insight_fragment, null);
        return view;
    }

}
