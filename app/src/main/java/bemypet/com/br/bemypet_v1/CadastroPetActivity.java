package bemypet.com.br.bemypet_v1;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.pm.PackageManager;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.vicmikhailau.maskededittext.MaskedFormatter;
import com.vicmikhailau.maskededittext.MaskedWatcher;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import bemypet.com.br.bemypet_v1.models.FirebaseConnection;
import bemypet.com.br.bemypet_v1.pojo.Pet;
import bemypet.com.br.bemypet_v1.pojo.PontoGeo;
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

    private static int RESULT_LOAD_IMAGE = 1;
    MaskedFormatter formatdata;
    Integer pesoValue = 1;

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
        } else {
            pet = new Pet();
            setPet(pet);
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
        formatdata = new MaskedFormatter("##/##/####");
        edtDataNascimento.addTextChangedListener(new MaskedWatcher(formatdata, edtDataNascimento));

        edtDataNascimento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog.OnDateSetListener dpd = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        int s = monthOfYear+1;
                        String month = "";
                        if(s < 10) {
                            month = "0"+s;
                        } else {
                            month = String.valueOf(s);
                        }
                        String a = dayOfMonth+"/"+month+"/"+year;
                        System.out.println(a);
                        edtDataNascimento.setText(""+a);
                    }
                };

                Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog d = new DatePickerDialog(CadastroPetActivity.this, dpd, mYear ,mMonth, mDay);
                d.show();

            }
        });
        edtPesoPet = (EditText) findViewById(R.id.edtPesoPet);
        edtPesoPet.setEnabled(Boolean.FALSE);
        edtPesoPet.setText(String.valueOf(pesoValue));
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

        if(getPet().raca != null){
            int selectionPosition = adapter.getPosition(getPet().raca);
            spinnerRacas.setSelection(selectionPosition);
        }

        if(getPet().castrado != null) {
            for (int i = 0; i < radioGroupCastrado.getChildCount(); i++) {
                RadioButton child = (RadioButton) radioGroupCastrado.getChildAt(i);
                if(child.getText().toString().equalsIgnoreCase(getPet().castrado)) {
                    child.setChecked(true);
                }
            }
        }

        if(getPet().vermifugado != null) {
            for (int i = 0; i < radioGroupVermifugado.getChildCount(); i++) {
                RadioButton child = (RadioButton) radioGroupVermifugado.getChildAt(i);
                if(child.getText().toString().equalsIgnoreCase(getPet().vermifugado)) {
                    child.setChecked(true);
                }
            }
        }

        if(getPet().dose != null) {
            if(getPet().dose.equalsIgnoreCase("1")) {
                chk_primeira_dose.setChecked(Boolean.TRUE);
            }

            if(getPet().dose.equalsIgnoreCase("2")) {
                chk_segunda_dose.setChecked(Boolean.TRUE);
            } else {
                chk_segunda_dose.setChecked(Boolean.FALSE);
            }
        }

        if(getPet().sociavel != null) {
            for (String sociavel : getPet().sociavel) {
                if (sociavel.equalsIgnoreCase(chk_sociavel_caes.getText().toString()))
                    chk_sociavel_caes.setChecked(Boolean.TRUE);

                if (sociavel.equalsIgnoreCase(chk_sociavel_gatos.getText().toString()))
                    chk_sociavel_gatos.setChecked(Boolean.TRUE);

                if (sociavel.equalsIgnoreCase(chk_sociavel_outros.getText().toString()))
                    chk_sociavel_outros.setChecked(Boolean.TRUE);

                if (sociavel.equalsIgnoreCase(chk_sociavel_pessoas.getText().toString()))
                    chk_sociavel_pessoas.setChecked(Boolean.TRUE);
            }
        }

        if(getPet().temperamento != null) {
            for (String temperamento : getPet().temperamento) {
                if (temperamento.equalsIgnoreCase(radioTemperamentoBravo.getText().toString()))
                    radioTemperamentoBravo.setChecked(Boolean.TRUE);

                if (temperamento.equalsIgnoreCase(radioTemperamentoComCuidado.getText().toString()))
                    radioTemperamentoComCuidado.setChecked(Boolean.TRUE);

                if (temperamento.equalsIgnoreCase(radioTemperamentoConviveBem.getText().toString()))
                    radioTemperamentoConviveBem.setChecked(Boolean.TRUE);

                if (temperamento.equalsIgnoreCase(radioTemperamentoMuitoDocil.getText().toString()))
                    radioTemperamentoMuitoDocil.setChecked(Boolean.TRUE);
            }
        }

        //percorre as imagens do pet e adicona dinamicamente as imagens no layout
        if(getPet().imagens != null && getPet().imagens.size() > 0) {
            LayoutInflater inflater = LayoutInflater.from(getBaseContext());
            dadosPetStep = (LinearLayout) inflater.inflate(R.layout.step_cadastro_pets_dados, null, false);
            for (String img : getPet().imagens) {
                View childLayout = inflater.inflate(R.layout.pet_image, (ViewGroup) findViewById(R.id.img_pet_remove));
                ImageView petImage = (ImageView) childLayout.findViewById(R.id.pet_photo);
                Glide.with(this).load(img).apply(RequestOptions.circleCropTransform()).into(petImage);
                dadosPetStep.addView(childLayout);
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

    public void buscaFotoPet(View v) {
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(i, RESULT_LOAD_IMAGE);

        if (ContextCompat.checkSelfPermission(CadastroPetActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(CadastroPetActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions(CadastroPetActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 50);
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        final LinearLayout rl = (LinearLayout) findViewById(R.id.petImgLayout);

        super.onActivityResult(requestCode, resultCode, data);
        try{
            if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
                Uri selectedImage = data.getData();
                System.out.println("URI: "+ selectedImage);
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();
                System.out.println(picturePath);
                storeImageToFirebase(picturePath);

                View imagLayout = getLayoutInflater().inflate(R.layout.pet_image, null);
                ImageView petImage = (ImageView) imagLayout.findViewById(R.id.pet_photo);
                petImage.setMaxWidth(45);
                petImage.setMaxHeight(45);
                Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
                petImage.setImageBitmap(Utils.getRoundedCroppedBitmap(bitmap, 90));

                //Glide.with(CadastroPetActivity.this).load(url).apply(RequestOptions.circleCropTransform()).into(petImage);

                rl.addView(imagLayout);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void storeImageToFirebase(final String imgPath) {

        Uri file = Uri.fromFile(new File(imgPath));
        StorageReference imgRef = FirebaseConnection.getStorage().child("images/"+String.valueOf(System.currentTimeMillis()+file.getLastPathSegment()));
        UploadTask uploadTask = imgRef.putFile(file);

        final LinearLayout rl = (LinearLayout) findViewById(R.id.petImgLayout);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                if(downloadUrl != null) {
                    String url = downloadUrl.toString();
                    getPet().addImagem(url);
                    System.out.println("firebase image: "+url);


                } else {
                    System.out.println("nulo");
                }
            }
        });

    }

    @Override
    public void sendData() {

        if(Utils.validaEditText(edNomePet)){
            getPet().nome = edNomePet.getText().toString();
        }

        if(Utils.validaEditText(edtDataNascimento)){
            getPet().dataNascimento = edtDataNascimento.getText().toString();
        }

        if(Utils.validaEditText(edtPesoPet)){
            getPet().pesoAproximado = Integer.valueOf(edtPesoPet.getText().toString());
        }

        int especieSelecionada = radioGroupEspecie.getCheckedRadioButtonId();
        RadioButton especie = (RadioButton) findViewById(especieSelecionada);
        getPet().especie = especie.getText().toString();

        int sexoSelecionado = radioGroupSexo.getCheckedRadioButtonId();
        RadioButton sexo = (RadioButton) findViewById(sexoSelecionado);
        getPet().sexo = sexo.getText().toString();

        getPet().raca = spinnerRacas.getSelectedItem().toString();


        int castradoSelecionado = radioGroupCastrado.getCheckedRadioButtonId();
        RadioButton castrado = (RadioButton) findViewById(castradoSelecionado);
        getPet().castrado = castrado.getText().toString();


        int vermifugadoSelecionado = radioGroupVermifugado.getCheckedRadioButtonId();
        RadioButton vermifugado = (RadioButton) findViewById(vermifugadoSelecionado);
        getPet().vermifugado = vermifugado.getText().toString();

        if(chk_segunda_dose.isChecked()) {
            getPet().vermifugado = "2";
        } else {
            getPet().vermifugado = "1";
        }

        //pegar os valores de sociavel selecionados
        List<String> sociavel = new ArrayList<>();
        if(chk_sociavel_gatos.isChecked()){
            sociavel.add(chk_sociavel_gatos.getText().toString());
        }

        if(chk_sociavel_pessoas.isChecked()){
            sociavel.add(chk_sociavel_pessoas.getText().toString());
        }

        if(chk_sociavel_outros.isChecked()){
            sociavel.add(chk_sociavel_outros.getText().toString());
        }

        if(chk_sociavel_caes.isChecked()){
            sociavel.add(chk_sociavel_caes.getText().toString());
        }

        pet.sociavel = sociavel;


        int temperamentoSelecionado = radioGroupTemperamento.getCheckedRadioButtonId();
        RadioButton temperamento = (RadioButton) findViewById(temperamentoSelecionado);
        List<String> temperamentoList = new ArrayList<>();
        temperamentoList.add(temperamento.getText().toString());
        getPet().temperamento = temperamentoList;

        PontoGeo pontoGeo = new PontoGeo(getDoador().localizacao.lat, getDoador().localizacao.lon);

        salvarPet(getPet());


    }

    //cria steps
    private View criaStepDadosPets(){
        LayoutInflater inflater = LayoutInflater.from(getBaseContext());
        dadosPetStep = (LinearLayout) inflater.inflate(R.layout.step_cadastro_pets_dados, null, false);
        return dadosPetStep;
    }

    private View criaStepOutrasInformacoes(){
        LayoutInflater inflater = LayoutInflater.from(getBaseContext());
        dadosPetStep = (LinearLayout) inflater.inflate(R.layout.step_cadastro_pets_outras_informacoes, null, false);
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


    public void diminuirPeso(View v) {
        if(pesoValue > 1) {
            pesoValue = pesoValue - 1;
            edtPesoPet.setText(String.valueOf(pesoValue));
        } else {
            Utils.showToastMessage(getApplicationContext(), "Peso deve ser maior que 1!");
        }
    }

    public void aumentarPeso(View v) {
        pesoValue = pesoValue + 1;
        edtPesoPet.setText(String.valueOf(pesoValue));
    }


    /**
     * Método para salva pets
     * @param entidade
     */
    private void salvarPet(Pet entidade) {
        final Pet pet = entidade;

        FirebaseConnection.getConnection();
        DatabaseReference connectedReference = FirebaseDatabase.getInstance().getReference(".info/connected");


        connectedReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                    FirebaseConnection.getDatabase().child("pets").child(String.valueOf(pet.id)).setValue(pet);

                    GeoFire geoFire = new GeoFire(FirebaseConnection.getDatabase().child("geofire"));
                    geoFire.setLocation(pet.id, new GeoLocation(pet.localizacao.lat, pet.localizacao.lon), new GeoFire.CompletionListener() {
                        @Override
                        public void onComplete(String key, DatabaseError error) {
                            if (error != null) {
                                System.err.println("There was an error saving the location to GeoFire: " + error);
                            } else {
                                System.out.println("Location saved on server successfully!");
                            }
                        }
                    });
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
}
