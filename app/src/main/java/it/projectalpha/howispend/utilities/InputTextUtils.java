package it.projectalpha.howispend.utilities;

import com.google.android.material.textfield.TextInputEditText;

public class InputTextUtils {

    public static void setError(TextInputEditText inputField, String message) {
        inputField.setError(message);
    }


    public static String getTextFromTextInput(TextInputEditText editText) {
        return editText.getText().toString().trim();
    }

}
