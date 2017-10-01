package bemypet.com.br.bemypet_v1.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import ernestoyaquello.com.verticalstepperform.VerticalStepperFormLayout;

/**
 * Created by kassianesmentz on 23/09/17.
 */

public class FormUtils {

    public static void preencherValidarCampos(final VerticalStepperFormLayout verticalStepperForm, EditText edtText, final int length, final String message) {

        edtText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkString(verticalStepperForm, s.toString(), length, message);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
        edtText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(checkString(verticalStepperForm, v.getText().toString(), length, message)) {
                    verticalStepperForm.goToNextStep();
                }
                return false;
            }
        });
    }

    public static void preencherValidarCampos(final VerticalStepperFormLayout verticalStepperForm, TextView edtText, final int length, final String message) {

        edtText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkString(verticalStepperForm, s.toString(), length, message);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
        edtText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(checkString(verticalStepperForm, v.getText().toString(), length, message)) {
                    verticalStepperForm.goToNextStep();
                }
                return false;
            }
        });
    }


    private static boolean checkString(final VerticalStepperFormLayout verticalStepperForm, String valor, int length, String message) {
        boolean checkOk = false;
        String titleErrorString = message;
        String titleError = String.format(titleErrorString, 3);

        if(valor.length() >= length) {
            checkOk = true;
            verticalStepperForm.setActiveStepAsCompleted();
        } else {
            verticalStepperForm.setActiveStepAsUncompleted(titleError);
            checkOk = false;
        }

        return checkOk;
    }

}
