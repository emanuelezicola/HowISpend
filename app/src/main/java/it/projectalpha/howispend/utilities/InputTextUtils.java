package it.projectalpha.howispend.utilities;

import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class InputTextUtils {

    public static void setError(TextInputEditText inputField, String message) {
        inputField.setError(message);
    }


    public static String getTextFromTextInput(TextInputEditText editText) {
        return Objects.requireNonNull(editText.getText()).toString().trim();
    }


    public static String getTextFromTextView(TextView textView) {
        return textView.getText().toString().trim();
    }

}
