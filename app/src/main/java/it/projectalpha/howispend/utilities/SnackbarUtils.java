package it.projectalpha.howispend.utilities;

import android.view.View;

import com.google.android.material.snackbar.Snackbar;

public class SnackbarUtils {


    public static void showShortSnackBar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }

    public static void showLongSnackBar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
    }



}
