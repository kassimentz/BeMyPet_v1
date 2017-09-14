package bemypet.com.br.bemypet_v1;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import bemypet.com.br.bemypet_v1.pojo.Pet;
import ernestoyaquello.com.verticalstepperform.VerticalStepperFormLayout;
import ernestoyaquello.com.verticalstepperform.interfaces.VerticalStepperForm;

public class CadastroPetActivity extends AppCompatActivity implements VerticalStepperForm {

    private VerticalStepperFormLayout verticalStepperForm;
    private Pet pet;

    private static final int DADOS_PET = 0;
    private static final int OUTRAS_INFORMACOES = 1;

    private LinearLayout dadosPetStep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_pet);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.cadastroPetToolbar);
        setSupportActionBar(myToolbar);

        ActionBar ab = getSupportActionBar();
        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        ab.setTitle(R.string.activity_title_cadastro_usuario);

        initializeActivity();
        initializeVariables();
    }
    private void initializeVariables() {}

    private void initializeActivity() {

        // Time step vars

        // Vertical Stepper form vars
        int colorPrimary = ContextCompat.getColor(getApplicationContext(), R.color.step_colorPrimary);
        int colorPrimaryDark = ContextCompat.getColor(getApplicationContext(), R.color.step_colorPrimaryDark);
        String[] stepsTitles = getResources().getStringArray(R.array.steps_cadastro_usuario_titles);
        //String[] stepsSubtitles = getResources().getStringArray(R.array.steps_subtitles);

        // Here we find and initialize the form
        verticalStepperForm = (VerticalStepperFormLayout) findViewById(R.id.vertical_stepper_form_pet);
        VerticalStepperFormLayout.Builder.newInstance(verticalStepperForm, stepsTitles, this, this)
                .stepTitleTextColor(Color.rgb(241,89,34))
                //.stepsSubtitles(stepsSubtitles)
//                .materialDesignInDisabledSteps(true) // false by default
                .showVerticalLineWhenStepsAreCollapsed(true) // false by default
                .primaryColor(colorPrimary)
                .primaryDarkColor(colorPrimaryDark)
                .displayBottomNavigation(true)
                .init();

    }

    @Override
    public View createStepContentView(int stepNumber) {
        View view = null;
        switch (stepNumber) {
            case DADOS_PET:
                view = criaStepDadosPets();
                break;
            case OUTRAS_INFORMACOES:
                view = criaStepOutrasInformacoes();
                break;
        }
        return view;
    }

    @Override
    public void onStepOpening(int stepNumber) {

        switch (stepNumber) {
            case DADOS_PET:
                verticalStepperForm.setStepAsCompleted(DADOS_PET);
                break;
            case OUTRAS_INFORMACOES:
                verticalStepperForm.setStepAsCompleted(OUTRAS_INFORMACOES);
                break;
        }

    }

    @Override
    public void sendData() {

    }

    //cria steps
    private View criaStepDadosPets(){
        LayoutInflater inflater = LayoutInflater.from(getBaseContext());
        dadosPetStep = (LinearLayout) inflater.inflate(R.layout.step_cadastro_pets_dados, null, false);

        //edtNomeUsuario = (EditText) dadosPessoaisStep.findViewById(R.id.edtNomeUsuario);

        //valida os dados do formulário se passar vai para proximo
//        verticalStepperForm.goToNextStep();

        return dadosPetStep;
    }

    private View criaStepOutrasInformacoes(){
        LayoutInflater inflater = LayoutInflater.from(getBaseContext());
        dadosPetStep = (LinearLayout) inflater.inflate(R.layout.step_cadastro_pets_outras_informacoes, null, false);


        //valida os dados do formulário se passar vai para proximo
        verticalStepperForm.goToNextStep();

        return dadosPetStep;

    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }
}
