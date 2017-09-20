package bemypet.com.br.bemypet_v1;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import bemypet.com.br.bemypet_v1.models.FirebaseConnection;
import bemypet.com.br.bemypet_v1.pojo.Adocao;
import bemypet.com.br.bemypet_v1.pojo.Pet;
import bemypet.com.br.bemypet_v1.utils.Constants;
import bemypet.com.br.bemypet_v1.utils.Utils;
import ernestoyaquello.com.verticalstepperform.VerticalStepperFormLayout;
import ernestoyaquello.com.verticalstepperform.interfaces.VerticalStepperForm;


public class CadastroAdocaoActivity extends AppCompatActivity implements VerticalStepperForm{


    private VerticalStepperFormLayout verticalStepperForm;

    // Information about the steps/fields of the form
    private static final int OUTROS_PET_STEP_NUM = 0;
    private static final int LOCALIZACAO_STEP_NUM = 1;

    //step DADOS PESSOAIS
    private LinearLayout outrosPestsStep, localizacaoStep;

    private RadioGroup radioGroupTevePet, radioGroupTemPet, radioGroupTipoMoradia;
    private RadioButton rbTemPetsSIM, rbTemPetsNAO, rbTevePetsSIM, rbTevePetsNAO, rbPatioNao, rbPatioSim, rbCasa, rbApartamento, rbCuidadoPesteNao, rbCuidadoPesteSim, rbGradeNao, rbGradeSim;
    private CheckBox ckdEspeceCaoTeve, chkEspecieGatoTeve, chkEspecieOutrosTeve, ckdMorteIdadeAvancada, chkMorteAcidente, chkRoubo, chkFuga, chkAdocao, ckdEspeceCaoTem, chkEspecieGatoTem, chkEspecieOutrosTem;
    private TextView txtTevePets, txtTemPets;
    private EditText edtInfoAdionais;

    List<String> ufListagem;

    private Adocao adocao;
    private Pet pet;

    private int qtdTeve = 0;
    private int qtdTem = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_adocao);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.cadastroAdocaoToolbar);
        setSupportActionBar(myToolbar);

        ActionBar ab = getSupportActionBar();
        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(R.string.activity_title_cadastro_adocao);

        initializeActivity();
        initializeVariables();

        getBundle();

        //se tem o pet preenchido, entao é edicao do pet e deve preencher os dados
        if(getAdocao() != null) {
            preencherDados();
        } else {
            adocao = new Adocao();
            setAdocao(adocao);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getBundle() {

        String jsonObj = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            jsonObj = extras.getString("pet");
        }
        Pet pet = new Gson().fromJson(jsonObj, Pet.class);

        if(pet != null) {
            setPet(pet);
        }


    }

    //inicializando os elementos do layout
    private void initializeVariables() {


        txtTevePets = (TextView) findViewById(R.id.txtTevePets);
        txtTemPets = (TextView) findViewById(R.id.txtTemPets);
        radioGroupTevePet = (RadioGroup) findViewById(R.id.radioGroupTevePet);
        radioGroupTemPet = (RadioGroup) findViewById(R.id.radioGroupTemPet);
        radioGroupTipoMoradia = (RadioGroup) findViewById(R.id.radioGroupTipoMoradia);
        rbTevePetsNAO = (RadioButton)  findViewById(R.id.rbTevePetsNAO);
        rbTevePetsSIM = (RadioButton)  findViewById(R.id.rbTevePetsSIM);
        rbTemPetsNAO = (RadioButton)  findViewById(R.id.rbTemPetsNAO);
        rbTemPetsSIM = (RadioButton)  findViewById(R.id.rbTemPetsSIM);
        txtTevePets.setText("0");
        txtTemPets.setText("0");

        radioGroupTevePet.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                int selectedId = radioGroupTevePet.getCheckedRadioButtonId();
                RadioButton rb = (RadioButton) findViewById(selectedId);
                if(rb.getText().toString().equals(rbTevePetsSIM.getText().toString())) {
                    txtTevePets.setText("1");
                } else {
                    txtTevePets.setText("0");
                }

                if(rb.getText().toString().equals(rbTevePetsNAO.getText().toString())) {
                    txtTevePets.setText("0");
                } else {
                    txtTevePets.setText("1");
                }

            }
        });
        
        radioGroupTemPet.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                int selectedId = radioGroupTemPet.getCheckedRadioButtonId();
                RadioButton rb = (RadioButton) findViewById(selectedId);
                if(rb.getText().toString().equals(rbTemPetsSIM.getText().toString())) {
                    txtTemPets.setText("1");
                } else {
                    txtTemPets.setText("0");
                }

                if(rb.getText().toString().equals(rbTemPetsNAO.getText().toString())) {
                    txtTemPets.setText("0");
                } else {
                    txtTemPets.setText("1");
                }

            }
        });


        ckdEspeceCaoTeve = (CheckBox) findViewById(R.id.ckdEspeceCaoTeve);
        chkEspecieGatoTeve = (CheckBox) findViewById(R.id.chkEspecieGatoTeve);
        chkEspecieOutrosTeve = (CheckBox) findViewById(R.id.chkEspecieOutrosTeve);
        ckdMorteIdadeAvancada = (CheckBox) findViewById(R.id.ckdMorteIdadeAvancada);
        chkMorteAcidente = (CheckBox) findViewById(R.id.chkMorteAcidente);
        chkRoubo = (CheckBox) findViewById(R.id.chkRoubo);
        chkFuga = (CheckBox) findViewById(R.id.chkFuga);
        chkAdocao = (CheckBox) findViewById(R.id.chkAdocao);
        ckdEspeceCaoTem = (CheckBox) findViewById(R.id.ckdEspeceCaoTem);
        chkEspecieGatoTem = (CheckBox) findViewById(R.id.chkEspecieGatoTem);
        chkEspecieOutrosTem = (CheckBox) findViewById(R.id.chkEspecieOutrosTem);
        rbPatioNao = (RadioButton)  findViewById(R.id.rbPatioNao);
        rbPatioSim = (RadioButton)  findViewById(R.id.rbPatioSim);
        rbCasa = (RadioButton)  findViewById(R.id.rbCasa);
        rbApartamento = (RadioButton)  findViewById(R.id.rbApartamento);
        rbCuidadoPesteNao = (RadioButton) findViewById(R.id.rbCuidadoPesteNao);
        rbCuidadoPesteSim = (RadioButton) findViewById(R.id.rbCuidadoPesteSim);
        rbGradeNao = (RadioButton) findViewById(R.id.rbGradeNao);
        rbGradeSim = (RadioButton) findViewById(R.id.rbGradeSim);
        edtInfoAdionais = (EditText) findViewById(R.id.edtInfoAdionais);

    }

    private void preencherDados() {

        if(getAdocao().jaTeveOutrosPets != null) {
            if (getAdocao().jaTeveOutrosPets){
                rbTevePetsSIM.setChecked(true);

            }else{
                rbTevePetsNAO.setChecked(true);
            }
        }

        if(getAdocao().quantosPetsTeve != null){
            txtTevePets.setText(getAdocao().quantosPetsTeve);
        }
        

        if(getAdocao().tiposPetsTeve != null) {
            for (String tevePet : getAdocao().tiposPetsTeve) {
                if (tevePet.equalsIgnoreCase(ckdEspeceCaoTeve.getText().toString()))
                    ckdEspeceCaoTeve.setChecked(Boolean.TRUE);

                if (tevePet.equalsIgnoreCase(chkEspecieGatoTeve.getText().toString()))
                    chkEspecieGatoTeve.setChecked(Boolean.TRUE);

                if (tevePet.equalsIgnoreCase(chkEspecieOutrosTeve.getText().toString()))
                    chkEspecieOutrosTeve.setChecked(Boolean.TRUE);
            }
        }

        if(getAdocao().oQueAconteceuComEles != null) {
            for (String oQueAconteceu : getAdocao().oQueAconteceuComEles) {
                if (oQueAconteceu.equalsIgnoreCase(ckdMorteIdadeAvancada.getText().toString()))
                    ckdMorteIdadeAvancada.setChecked(Boolean.TRUE);

                if (oQueAconteceu.equalsIgnoreCase(chkMorteAcidente.getText().toString()))
                    chkMorteAcidente.setChecked(Boolean.TRUE);

                if (oQueAconteceu.equalsIgnoreCase(chkRoubo.getText().toString()))
                    chkRoubo.setChecked(Boolean.TRUE);

                if (oQueAconteceu.equalsIgnoreCase(chkFuga.getText().toString()))
                    chkFuga.setChecked(Boolean.TRUE);

                if (oQueAconteceu.equalsIgnoreCase(chkAdocao.getText().toString()))
                    chkAdocao.setChecked(Boolean.TRUE);
            }
        }


        if(getAdocao().temPetAtualmente != null) {
            if (getAdocao().temPetAtualmente){
                rbTemPetsSIM.setChecked(true);

            }else{
                rbTemPetsNAO.setChecked(true);
            }
        }

        if(getAdocao().quantosPetsTem != null){
            txtTemPets.setText(getAdocao().quantosPetsTem);
        }


        if(getAdocao().tiposPetsTem != null) {
            for (String temPet : getAdocao().tiposPetsTem) {
                if (temPet.equalsIgnoreCase(ckdEspeceCaoTem.getText().toString()))
                    ckdEspeceCaoTem.setChecked(Boolean.TRUE);

                if (temPet.equalsIgnoreCase(chkEspecieGatoTem.getText().toString()))
                    chkEspecieGatoTem.setChecked(Boolean.TRUE);

                if (temPet.equalsIgnoreCase(chkEspecieOutrosTem.getText().toString()))
                    chkEspecieOutrosTem.setChecked(Boolean.TRUE);
            }
        }

        if(getAdocao().tipoMoradia != null) {
            for (int i = 0; i < radioGroupTipoMoradia.getChildCount(); i++) {
                RadioButton child = (RadioButton) radioGroupTipoMoradia.getChildAt(i);
                if(child.getText().toString().equalsIgnoreCase(getAdocao().tipoMoradia)) {
                    child.setChecked(true);
                }
            }
        }

        if(getAdocao().possuiPatio != null) {
            if (getAdocao().possuiPatio){
                rbPatioSim.setChecked(true);

            }else{
                rbPatioNao.setChecked(true);
            }
        }

        if(getAdocao().temCuidadoContraPestes != null) {
            if (getAdocao().temCuidadoContraPestes){
                rbCuidadoPesteSim.setChecked(true);

            }else{
                rbCuidadoPesteNao.setChecked(true);
            }
        }

        if(getAdocao().possuiTelasProtecao != null) {
            if (getAdocao().possuiTelasProtecao){
                rbGradeSim.setChecked(true);

            }else{
                rbGradeNao.setChecked(true);
            }
        }

        if(getAdocao().informacoesAdicionais != null){
            edtInfoAdionais.setText(getAdocao().informacoesAdicionais);
        }

    }

    private void initializeActivity() {

        // Time step vars

        // Vertical Stepper form vars
        int colorPrimary = ContextCompat.getColor(getApplicationContext(), R.color.step_colorPrimary);
        int colorPrimaryDark = ContextCompat.getColor(getApplicationContext(), R.color.step_colorPrimaryDark);
        String[] stepsTitles = getResources().getStringArray(R.array.steps_cadastro_adocao_titles);
        //String[] stepsSubtitles = getResources().getStringArray(R.array.steps_subtitles);

        // Here we find and initialize the form
        verticalStepperForm = (VerticalStepperFormLayout) findViewById(R.id.vertical_stepper_form);
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
            case OUTROS_PET_STEP_NUM:
                view = criaStepOutrosPets();
                break;
            case LOCALIZACAO_STEP_NUM:
                view = criaStepLocalizacao();
                break;
        }
        return view;
    }


    @Override
    public void onStepOpening(int stepNumber) {
        switch (stepNumber) {
            case OUTROS_PET_STEP_NUM:
                verticalStepperForm.setStepAsCompleted(OUTROS_PET_STEP_NUM);

                break;
            case LOCALIZACAO_STEP_NUM:
                verticalStepperForm.setStepAsCompleted(LOCALIZACAO_STEP_NUM);
                break;

        }
    }

    @Override
    public void sendData() {

         Boolean erro = Boolean.FALSE;

        if(rbTevePetsSIM.isChecked()) {
            getAdocao().jaTeveOutrosPets = true;
        } else {
            getAdocao().jaTeveOutrosPets = false;
        }

        if(rbTemPetsSIM.isChecked()) {
            getAdocao().temPetAtualmente = true;
        } else {
            getAdocao().temPetAtualmente = false;
        }

        if(!txtTevePets.getText().toString().equalsIgnoreCase("0") && rbTevePetsSIM.isChecked()){
            getAdocao().quantosPetsTeve = Integer.parseInt(txtTevePets.getText().toString());
        } else if(txtTevePets.getText().toString().equalsIgnoreCase("0") && rbTevePetsNAO.isChecked()) {
            getAdocao().quantosPetsTeve = Integer.parseInt(txtTevePets.getText().toString());
        }
        
        if(!txtTemPets.getText().toString().equalsIgnoreCase("0") && rbTemPetsSIM.isChecked()){
            getAdocao().quantosPetsTem = Integer.parseInt(txtTemPets.getText().toString());
        } else if(txtTemPets.getText().toString().equalsIgnoreCase("0") && rbTemPetsNAO.isChecked()) {
            getAdocao().quantosPetsTem = Integer.parseInt(txtTemPets.getText().toString());
        }


        //pegar os valores de tiposPetsTem selecionados
        List<String> tiposPetsTeve = new ArrayList<>();
        if(ckdEspeceCaoTeve.isChecked()){
            tiposPetsTeve.add(ckdEspeceCaoTeve.getText().toString());
        }
        if(chkEspecieGatoTeve.isChecked()){
            tiposPetsTeve.add(chkEspecieGatoTeve.getText().toString());
        }
        if(chkEspecieOutrosTeve.isChecked()){
            tiposPetsTeve.add(chkEspecieOutrosTeve.getText().toString());
        }
        getAdocao().tiposPetsTeve = tiposPetsTeve;


        //pegar os valores de tiposPetsTem selecionados
        List<String> oQueAconteceuComEles = new ArrayList<>();
        if(ckdMorteIdadeAvancada.isChecked()){
            oQueAconteceuComEles.add(ckdMorteIdadeAvancada.getText().toString());
        }
        if(chkMorteAcidente.isChecked()){
            oQueAconteceuComEles.add(chkMorteAcidente.getText().toString());
        }
        if(chkRoubo.isChecked()){
            oQueAconteceuComEles.add(chkRoubo.getText().toString());
        }
        if(chkFuga.isChecked()){
            oQueAconteceuComEles.add(chkFuga.getText().toString());
        }
        if(chkAdocao.isChecked()){
            oQueAconteceuComEles.add(chkAdocao.getText().toString());
        }
        getAdocao().oQueAconteceuComEles = oQueAconteceuComEles;


        //pegar os valores de tiposPetsTem selecionados
        List<String> tiposPetsTem = new ArrayList<>();
        if(ckdMorteIdadeAvancada.isChecked()){
            tiposPetsTem.add(ckdEspeceCaoTem.getText().toString());
        }
        if(chkEspecieGatoTem.isChecked()){
            tiposPetsTem.add(chkEspecieGatoTem.getText().toString());
        }
        if(chkEspecieOutrosTem.isChecked()){
            tiposPetsTem.add(chkEspecieOutrosTem.getText().toString());
        }
        getAdocao().tiposPetsTem = tiposPetsTem;


        int tipoMoradiaSelecionado = radioGroupTipoMoradia.getCheckedRadioButtonId();
        RadioButton tipoMoradia = (RadioButton) findViewById(tipoMoradiaSelecionado);
        getAdocao().tipoMoradia = tipoMoradia.getText().toString();

        if(rbPatioSim.isChecked()) {
            getAdocao().possuiPatio = true;
        } else {
            getAdocao().possuiPatio = false;
        }

        if(rbCuidadoPesteSim.isChecked()) {
            getAdocao().temCuidadoContraPestes = true;
        } else {
            getAdocao().temCuidadoContraPestes = false;
        }

        if(rbGradeSim.isChecked()) {
            getAdocao().possuiTelasProtecao = true;
        } else {
            getAdocao().possuiTelasProtecao = false;
        }


        if(Utils.validaEditText(edtInfoAdionais)){
            getAdocao().informacoesAdicionais = edtInfoAdionais.getText().toString();
        } else {
            getAdocao().informacoesAdicionais = "";
        }

        getAdocao().adotante = Utils.getUsuarioSharedPreferences(getApplicationContext());
        getAdocao().doador = getPet().doador;
        getAdocao().pet = getPet();
        if(!erro) {
            salvarAdocao(getAdocao());
            
        }
    }

    //cria steps
    private View criaStepOutrosPets(){
        LayoutInflater inflater = LayoutInflater.from(getBaseContext());
        outrosPestsStep = (LinearLayout) inflater.inflate(
                R.layout.step_cadastro_adocao_outros_pets, null, false);


        //valida os dados do formulário se passar vai para proximo
//        verticalStepperForm.goToNextStep();

        return outrosPestsStep;
    }

    private View criaStepLocalizacao(){
        LayoutInflater inflater = LayoutInflater.from(getBaseContext());
        localizacaoStep = (LinearLayout) inflater.inflate(
                R.layout.step_cadastro_adocao_localizacao, null, false);

        //valida os dados do formulário se passar vai para proximo
        verticalStepperForm.goToNextStep();

        return localizacaoStep;
    }



    private void salvarAdocao(Adocao entidade) {
        final Adocao saveA = entidade;

        FirebaseConnection.getConnection();
        DatabaseReference connectedReference = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                    FirebaseConnection.getDatabase().child("adocoes").child(String.valueOf(saveA.id)).setValue(saveA);

                    Intent intent = new Intent(CadastroAdocaoActivity.this, ConfirmarSolicitacaoAdocao.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("pet", new Gson().toJson(getPet()));
                    bundle.putSerializable("adocao", new Gson().toJson(getAdocao()));
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();

                } else {
                    //logar erro
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                //Log.i("Cancel", "Listener was cancelled");
            }
        });
    }


    public void somaQuantidadeTevePet(View v){
        qtdTeve = qtdTeve + 1;
        txtTevePets.setText(String.valueOf(qtdTeve));

    }
    public void diminueQuantidaTevePet(View v){

        if(qtdTeve > 0) {
            qtdTeve = qtdTeve - 1;
            txtTevePets.setText(String.valueOf(qtdTeve));
        }
    }


    public void somaQuantidadeTemPet(View v){
        qtdTem = qtdTem + 1;
        txtTemPets.setText(String.valueOf(qtdTem));

    }
    public void diminueQuantidaTemPet(View v){

        if(qtdTem > 0) {
            qtdTem = qtdTem - 1;
            txtTemPets.setText(String.valueOf(qtdTem));
        }
    }


    public Adocao getAdocao() {
        return adocao;
    }

    public void setAdocao(Adocao adocao) {
        this.adocao = adocao;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }
}
