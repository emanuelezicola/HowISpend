package it.projectalpha.howispend.core.registrazione;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.button.MaterialButton;

import java.util.Objects;

import it.projectalpha.howispend.R;

public class RegistrazioneFirstFragment extends Fragment {

    private View view;

    private MaterialButton btnAvanti;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.registrazione_first_fragment, container, false);


        btnAvanti = view.findViewById(R.id.nuovoUtenteStep2);

        btnAvanti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RegistrazioneSecondFragment fragment2 = new RegistrazioneSecondFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = Objects.requireNonNull(fragmentManager).beginTransaction();
                fragmentTransaction.replace(R.id.nuovoUtenteFrame, fragment2);
                fragmentTransaction.commit();
            }
        });



        return view;
    }
}
