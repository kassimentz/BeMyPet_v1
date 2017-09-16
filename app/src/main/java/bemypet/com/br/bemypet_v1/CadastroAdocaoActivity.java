package bemypet.com.br.bemypet_v1;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.List;

import bemypet.com.br.bemypet_v1.models.FirebaseConnection;
import bemypet.com.br.bemypet_v1.pojo.Adocao;
import bemypet.com.br.bemypet_v1.pojo.Pet;
import ernestoyaquello.com.verticalstepperform.VerticalStepperFormLayout;
import ernestoyaquello.com.verticalstepperform.interfaces.VerticalStepperForm;


public class CadastroAdocaoActivity extends AppCompatActivity implements VerticalStepperForm{


    private VerticalStepperFormLayout verticalStepperForm;

    // Information about the steps/fields of the form
    private static final int OUTROS_PET_STEP_NUM = 0;
    private static final int LOCALIZACAO_STEP_NUM = 1;

    //step DADOS PESSOAIS
    private LinearLayout outrosPestsStep, localizacaoStep;

    List<String> ufListagem;

    private Adocao adocao;
    private Pet pet;

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


    public void onRBTevePetsClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.rbTevePetsSIM:
                if (checked)
                    // Pirates are the best
                    break;
            case R.id.rbTevePetsNAO:
                if (checked)
                    // Ninjas rule
                    break;
        }
    }

    private void salvarAdocao(Adocao data) {
        final Adocao adocao = data;
        FirebaseConnection.getConnection();
        DatabaseReference connectedReference = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                    FirebaseConnection.getDatabase().child("adocoes").child(String.valueOf(adocao.id)).setValue(adocao);

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

        Toast toast = Toast.makeText(CadastroAdocaoActivity.this, "soma", Toast.LENGTH_LONG);
        toast.show();
    }


    public void diminueQuantidaTevePet(View v){

        Toast toast = Toast.makeText(CadastroAdocaoActivity.this, "diminue", Toast.LENGTH_LONG);
        toast.show();
    }
    public void somaQuantidadeTemPet(View v){

        Toast toast = Toast.makeText(CadastroAdocaoActivity.this, "soma", Toast.LENGTH_LONG);
        toast.show();
    }


    public void diminueQuantidaTemPet(View v){

        Toast toast = Toast.makeText(CadastroAdocaoActivity.this, "diminue", Toast.LENGTH_LONG);
        toast.show();
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
