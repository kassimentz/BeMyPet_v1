package bemypet.com.br.bemypet_v1;

import android.app.ProgressDialog;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import bemypet.com.br.bemypet_v1.pojo.Usuario;
import ernestoyaquello.com.verticalstepperform.VerticalStepperFormLayout;
import ernestoyaquello.com.verticalstepperform.interfaces.VerticalStepperForm;


public class CadastroUsuarioActivity extends AppCompatActivity implements VerticalStepperForm{


    private Usuario usuario;
    private VerticalStepperFormLayout verticalStepperForm;

    // Information about the steps/fields of the form
    private static final int DADOS_PESSOAIS_STEP_NUM = 0;
    private static final int LOCALIZACAO_STEP_NUM = 1;
    private static final int CONTATO_STEP_NUM = 2;

    //step DADOS PESSOAIS
    private LinearLayout dadosPessoaisStep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.cadastroUsuarioToolbar);
        setSupportActionBar(myToolbar);

        ActionBar ab = getSupportActionBar();
        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        ab.setTitle(R.string.activity_title_cadastro_usuario);

        initializeActivity();
        //usar getbudle pegar exemplo no doancao


    }

    private void initializeActivity() {

        // Time step vars


        // Vertical Stepper form vars
        int colorPrimary = ContextCompat.getColor(getApplicationContext(), R.color.step_colorPrimary);
        int colorPrimaryDark = ContextCompat.getColor(getApplicationContext(), R.color.step_colorPrimaryDark);
        String[] stepsTitles = getResources().getStringArray(R.array.steps_cadastro_usuario_titles);
        //String[] stepsSubtitles = getResources().getStringArray(R.array.steps_subtitles);

        // Here we find and initialize the form
        verticalStepperForm = (VerticalStepperFormLayout) findViewById(R.id.vertical_stepper_form);
        VerticalStepperFormLayout.Builder.newInstance(verticalStepperForm, stepsTitles, this, this)
                //.stepsSubtitles(stepsSubtitles)
                //.materialDesignInDisabledSteps(true) // false by default
                //.showVerticalLineWhenStepsAreCollapsed(true) // false by default
                .primaryColor(colorPrimary)
                .primaryDarkColor(colorPrimaryDark)
                .displayBottomNavigation(true)
                .init();

    }
    @Override
    public View createStepContentView(int stepNumber) {
        View view = null;
        switch (stepNumber) {
            case DADOS_PESSOAIS_STEP_NUM:
                view = criaStepDadosPessoais();
                break;
            case LOCALIZACAO_STEP_NUM:
                view = criaStepLocalizacao();
                break;
            case CONTATO_STEP_NUM:
                view = criaStepContato();
                break;
        }
        return view;
    }


    @Override
    public void onStepOpening(int stepNumber) {
        switch (stepNumber) {
            case DADOS_PESSOAIS_STEP_NUM:
//                checkName();
                verticalStepperForm.setStepAsCompleted(DADOS_PESSOAIS_STEP_NUM);

                break;
            case LOCALIZACAO_STEP_NUM:
//                checkEmail();
                verticalStepperForm.setStepAsCompleted(LOCALIZACAO_STEP_NUM);
                break;
            case CONTATO_STEP_NUM:
                // As soon as the phone number step is open, we mark it as completed in order to show the "Continue"
                // button (We do it because this field is optional, so the user can skip it without giving any info)
                verticalStepperForm.setStepAsCompleted(CONTATO_STEP_NUM);
                // In this case, the instruction above is equivalent to:
                // verticalStepperForm.setActiveStepAsCompleted();
                break;
        }
    }

    @Override
    public void sendData() {
        //salva os dados usar dialog
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }


    //cria steps
    private View criaStepDadosPessoais(){
        LayoutInflater inflater = LayoutInflater.from(getBaseContext());
        dadosPessoaisStep = (LinearLayout) inflater.inflate(
                R.layout.step_cadastro_usuario_dados_pessoais, null, false);


        //valida os dados do formulário se passar vai para proximo
//        verticalStepperForm.goToNextStep();

        return dadosPessoaisStep;
    }

    private View criaStepLocalizacao(){
        LayoutInflater inflater = LayoutInflater.from(getBaseContext());
        dadosPessoaisStep = (LinearLayout) inflater.inflate(
                R.layout.step_cadastro_usuario_dados_pessoais, null, false);

        //valida os dados do formulário se passar vai para proximo
        verticalStepperForm.goToNextStep();

        return dadosPessoaisStep;
    }

    private View criaStepContato(){
        LayoutInflater inflater = LayoutInflater.from(getBaseContext());
        dadosPessoaisStep = (LinearLayout) inflater.inflate(
                R.layout.step_cadastro_usuario_dados_pessoais, null, false);


        //valida os dados do formulário se passar vai para proximo
        verticalStepperForm.goToNextStep();

        return dadosPessoaisStep;

    }
}
