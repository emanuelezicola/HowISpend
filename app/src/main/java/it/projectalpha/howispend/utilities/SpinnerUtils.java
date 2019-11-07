package it.projectalpha.howispend.utilities;

import android.widget.Spinner;

public class SpinnerUtils {

    public static String getTextFromSpinner(Spinner spinner) {
        return spinner.getSelectedItem().toString().trim();
    }
}
