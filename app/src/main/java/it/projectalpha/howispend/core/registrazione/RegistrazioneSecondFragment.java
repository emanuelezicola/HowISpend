package it.projectalpha.howispend.core.registrazione;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import it.projectalpha.howispend.R;

public class RegistrazioneSecondFragment extends Fragment {

    private View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.registrazione_second_fragment, container, false);

        return view;
    }

}
