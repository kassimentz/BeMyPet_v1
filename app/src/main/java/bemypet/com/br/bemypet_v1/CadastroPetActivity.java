package bemypet.com.br.bemypet_v1;

import android.graphics.Color;
import android.support.annotation.IdRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import bemypet.com.br.bemypet_v1.pojo.Pet;
import bemypet.com.br.bemypet_v1.pojo.Usuario;
import bemypet.com.br.bemypet_v1.utils.Utils;
import ernestoyaquello.com.verticalstepperform.VerticalStepperFormLayout;
import ernestoyaquello.com.verticalstepperform.interfaces.VerticalStepperForm;

public class CadastroPetActivity extends AppCompatActivity implements VerticalStepperForm {

    private VerticalStepperFormLayout verticalStepperForm;
    private Pet pet;
    private Usuario doador;

    private static final int DADOS_PET = 0;
    private static final int OUTRAS_INFORMACOES = 1;

    private LinearLayout dadosPetStep;
    private EditText edNomePet, edtDataNascimento, edtPesoPet;
    private RadioGroup radioGroupEspecie, radioGroupSexo, radioGroupCastrado, radioGroupVermifugado,
            radioGroupTemperamento;
    private RadioButton radioCao, radioGato, radioOutros, radioMacho, radioFemea, radioNaoSei,
            radioCastradoSim, radioCastradoNao, radioVermifugadoSim, radioVermifugadoNao,
            radioTemperamentoBravo, radioTemperamentoComCuidado, radioTemperamentoConviveBem,
            radioTemperamentoMuitoDocil;
    private Spinner spinnerRacas;
    private ImageView imgReduzirPeso, imgAumentarPeso;
    private CheckBox chk_primeira_dose, chk_segunda_dose, chk_sociavel_pessoas, chk_sociavel_caes,
            chk_sociavel_gatos, chk_sociavel_outros;
    private TextView txtNaoSei;

    List<String> racasCao = new ArrayList<>();
    List<String> racasGato = new ArrayList<>();
    List<String> racasOutros = new ArrayList<>();
    List<String> racas = new ArrayList<>();

    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_pet);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.cadastroPetToolbar);
        setSupportActionBar(myToolbar);

        ActionBar ab = getSupportActionBar();
        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        ab.setTitle(R.string.activity_title_cadastro_pet);

        initializeActivity();
        initializeVariables();
        getBundle();

        //se tem o pet preenchido, entao é edicao do pet e deve preencher os dados
        if(getPet() != null) {
            preencherDados();
        }

    }

    private void getBundle() {

        if (Utils.getUsuarioSharedPreferences(getApplicationContext()) != null) {
            setDoador(Utils.getUsuarioSharedPreferences(getApplicationContext()));
        }
        String jsonObj = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            jsonObj = extras.getString("pet");
        }
        Pet pet = new Gson().fromJson(jsonObj, Pet.class);
        //se tem pet no bundle é porque trata-se de edição
        if (pet != null) {
            setPet(pet);
        }


    }

    private void initializeVariables() {

        racas.add("Escolha");

        racasCao.add("Shih Tzu");
        racasCao.add("Yorkshire Terrier");
        racasCao.add("Poodle");
        racasCao.add("Lhasa Apso");
        racasCao.add("Buldogue Francês");
        racasCao.add("Maltês");
        racasCao.add("Golden Retriever");
        racasCao.add("Labrador");
        racasCao.add("Pug");
        racasCao.add("Dachshund");
        racasCao.add("Spitz Alemão");
        racasCao.add("Pinscher");
        racasCao.add("Schnauzer");
        racasCao.add("Beagle");
        racasCao.add("Cocker Spaniel");
        racasCao.add("Border Collie");
        racasCao.add("Buldogue Inglês");
        racasCao.add("American Pitbull");
        racasCao.add("Chow Chow");

        racasGato.add("Persa");
        racasGato.add("Himalaia");
        racasGato.add("Siamês");
        racasGato.add("Maine Coon");
        racasGato.add("Angorá");
        racasGato.add("Sphynx");
        racasGato.add("Ragdoll");
        racasGato.add("Ashera");
        racasGato.add("American Shorthair");
        racasGato.add("Siberiano");
        racasGato.add("Egípcio");

        racasOutros.add("Outros");

        racas.addAll(racasCao);
        spinnerRacas = (Spinner) findViewById(R.id.spinnerRacas);
        adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_style, racas);
        spinnerRacas.setAdapter(adapter);


        txtNaoSei = (TextView) findViewById(R.id.txtNaoSei);
        edNomePet = (EditText) findViewById(R.id.edNomePet);
        edtDataNascimento = (EditText) findViewById(R.id.edtDataNascimento);
        edtPesoPet = (EditText) findViewById(R.id.edtPesoPet);
        radioGroupEspecie = (RadioGroup) findViewById(R.id.radioGroupEspecie);
        radioGroupSexo = (RadioGroup) findViewById(R.id.radioGroupSexo);
        radioGroupCastrado = (RadioGroup) findViewById(R.id.radioGroupCastrado);
        radioGroupVermifugado = (RadioGroup) findViewById(R.id.radioGroupVermifugado);
        radioGroupTemperamento = (RadioGroup) findViewById(R.id.radioGroupTemperamento);
        radioCao = (RadioButton) findViewById(R.id.radioCao);
        radioGato = (RadioButton) findViewById(R.id.radioGato);
        radioOutros = (RadioButton) findViewById(R.id.radioOutros);

        radioGroupEspecie.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                int selectedId = radioGroupEspecie.getCheckedRadioButtonId();
                RadioButton rb = (RadioButton) findViewById(selectedId);
                if(rb.getText().toString().equals(radioCao.getText().toString())) {
                    racas.addAll(racasCao);
                } else {
                    racas.removeAll(racasCao);
                }

                if(rb.getText().toString().equals(radioGato.getText().toString())) {
                    racas.addAll(racasGato);
                } else {
                    racas.removeAll(racasGato);
                }

                if(rb.getText().toString().equals(radioOutros.getText().toString())) {
                    racas.addAll(racasOutros);
                } else {
                    racas.removeAll(racasOutros);
                }
            }
        });

        radioMacho = (RadioButton) findViewById(R.id.radioMacho);
        radioFemea = (RadioButton) findViewById(R.id.radioFemea);
        radioNaoSei = (RadioButton) findViewById(R.id.radioNaoSei);
        radioCastradoSim = (RadioButton) findViewById(R.id.radioCastradoSim);
        radioCastradoNao = (RadioButton) findViewById(R.id.radioCastradoNao);
        radioVermifugadoSim = (RadioButton) findViewById(R.id.radioVermifugadoSim);
        radioVermifugadoNao = (RadioButton) findViewById(R.id.radioVermifugadoNao);
        radioTemperamentoBravo = (RadioButton) findViewById(R.id.radioTemperamentoBravo);
        radioTemperamentoComCuidado = (RadioButton) findViewById(R.id.radioTemperamentoComCuidado);
        radioTemperamentoConviveBem = (RadioButton) findViewById(R.id.radioTemperamentoConviveBem);
        radioTemperamentoMuitoDocil = (RadioButton) findViewById(R.id.radioTemperamentoMuitoDocil);
        imgReduzirPeso = (ImageView) findViewById(R.id.imgReduzirPeso);
        imgAumentarPeso = (ImageView) findViewById(R.id.imgAumentarPeso);
        chk_primeira_dose = (CheckBox) findViewById(R.id.chk_primeira_dose);
        chk_segunda_dose = (CheckBox) findViewById(R.id.chk_segunda_dose);
        chk_sociavel_pessoas = (CheckBox) findViewById(R.id.chk_sociavel_pessoas);
        chk_sociavel_caes = (CheckBox) findViewById(R.id.chk_sociavel_caes);
        chk_sociavel_gatos = (CheckBox) findViewById(R.id.chk_sociavel_gatos);
        chk_sociavel_outros = (CheckBox) findViewById(R.id.chk_sociavel_outros);

        txtNaoSei.setText(Html.fromHtml(getString(R.string.txtNaoSei)));
    }

    private void preencherDados() {
        if(getPet().nome != null) {
            edNomePet.setText(getPet().nome);
        }

        if(getPet().dataNascimento != null) {
            edtDataNascimento.setText(getPet().dataNascimento);
        }

        if(getPet().pesoAproximado != null) {
            edtPesoPet.setText(String.valueOf(getPet().pesoAproximado));
        }

        if(getPet().especie != null) {
            for (int i = 0; i < radioGroupEspecie.getChildCount(); i++) {
                RadioButton child = (RadioButton) radioGroupEspecie.getChildAt(i);
                if(child.getText().toString().equalsIgnoreCase(getPet().especie)) {
                    child.setChecked(true);
                }
            }
        }

        if(getPet().sexo != null) {
            for (int i = 0; i < radioGroupSexo.getChildCount(); i++) {
                RadioButton child = (RadioButton) radioGroupSexo.getChildAt(i);
                if(child.getText().toString().equalsIgnoreCase(getPet().sexo)) {
                    child.setChecked(true);
                }
            }
        }




    }

    private void initializeActivity() {

        // Time step vars

        // Vertical Stepper form vars
        int colorPrimary = ContextCompat.getColor(getApplicationContext(), R.color.step_colorPrimary);
        int colorPrimaryDark = ContextCompat.getColor(getApplicationContext(), R.color.step_colorPrimaryDark);
        String[] stepsTitles = getResources().getStringArray(R.array.steps_cadastro_pet_titles);

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

    public Usuario getDoador() {
        return doador;
    }

    public void setDoador(Usuario doador) {
        this.doador = doador;
    }
}
