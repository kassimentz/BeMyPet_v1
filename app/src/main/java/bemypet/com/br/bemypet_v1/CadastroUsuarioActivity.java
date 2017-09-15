package bemypet.com.br.bemypet_v1;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import bemypet.com.br.bemypet_v1.pojo.Pet;
import bemypet.com.br.bemypet_v1.pojo.Usuario;
import bemypet.com.br.bemypet_v1.utils.Utils;
import ernestoyaquello.com.verticalstepperform.VerticalStepperFormLayout;
import ernestoyaquello.com.verticalstepperform.interfaces.VerticalStepperForm;


public class CadastroUsuarioActivity extends AppCompatActivity implements VerticalStepperForm {


    private Usuario usuario;
    private Pet pet;

    private VerticalStepperFormLayout verticalStepperForm;

    // Information about the steps/fields of the form
    private static final int DADOS_PESSOAIS_STEP_NUM = 0;
    private static final int LOCALIZACAO_STEP_NUM = 1;
    private static final int CONTATO_STEP_NUM = 2;

    //step DADOS PESSOAIS
    ArrayAdapter<String> adapter;
    private Spinner spinnerUf;
    private LinearLayout dadosPessoaisStep, localizacaoStep, contatoStep;
    private EditText edtNomeUsuario, edtDataNascimento, edtCpf, edtCep, edtEndereco, edtNumero, edtComplemento, edtCidade, edtTelefone, edtEmail;
    private ImageView user_profile_photo;
    List<String> ufListagem;

    String origem = null;

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
        initializeVariables();

        getBundle();
        if(getUsuario() != null) {
            preencherDados();
        }


        //usar getbudle pegar exemplo no doancao
        //adicionar a funcao getBundle para receber os dados do usuario.
        // testar dentro da getBundle se existe um usuario (se veio algo ou nao)
        // se o usuario estiver vazio, significa que é novo, nao é edicao.
        // se o usuario nao estiver vazio, criar um metodo que preenche os dados
        // do forumlario com as informacoes contidas no objeto usuario
        // exemplo em PerfilPetActivity linha 76 ou em
        // PerfilUsuarioActivity linha 71

    }

    //inicializando os elementos do layout
    private void initializeVariables() {

        //UF
        spinnerUf = (Spinner) findViewById(R.id.spinnerUf);


        String[] ufString = new String[]{"AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO"};
        ;
        ufListagem = new ArrayList<String>(Arrays.asList(ufString));

        adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_item, ufListagem);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUf.setAdapter(adapter);

        edtNomeUsuario = (EditText) findViewById(R.id.edtNomeUsuario);
        user_profile_photo = (ImageView) findViewById(R.id.user_profile_photo);
        edtDataNascimento = (EditText) findViewById(R.id.edtDataNascimento);
        edtCpf = (EditText) findViewById(R.id.edtCpf);
        edtCep = (EditText) findViewById(R.id.edtCep);
        edtEndereco = (EditText) findViewById(R.id.edtEndereco);
        edtNumero = (EditText) findViewById(R.id.edtNumero);
        edtComplemento = (EditText) findViewById(R.id.edtComplemento);
        edtCidade = (EditText) findViewById(R.id.edtCidade);
        edtTelefone = (EditText) findViewById(R.id.edtTelefone);
        edtEmail = (EditText) findViewById(R.id.edtEmail);

    }

    private void getBundle() {

        if (Utils.getUsuarioSharedPreferences(getApplicationContext()) != null) {
            setUsuario(Utils.getUsuarioSharedPreferences(getApplicationContext()));
        }
        String jsonObj = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            jsonObj = extras.getString("pet");
            if (extras != null) {
                if (extras.containsKey("origem")) {
                    origem = extras.getString("origem");
                }
            }
        }
        Pet pet = new Gson().fromJson(jsonObj, Pet.class);

        if (pet != null) {
            setPet(pet);
        }

    }

    private void preencherDados() {

        if (getUsuario().imagens.size() > 0) {
            // Loading profile image
            Glide.with(this).load(getUsuario().imagens.get(0)).apply(RequestOptions.circleCropTransform()).into(user_profile_photo);
        }

        if (getUsuario().nome != null) {
            edtNomeUsuario.setText(getUsuario().nome);
        }
        if (getUsuario().dataNascimento != null) {
            edtDataNascimento.setText(getUsuario().dataNascimento);
        }
        if (getUsuario().cpf != null) {
            edtCpf.setText(getUsuario().cpf);
        }
        if (String.valueOf(getUsuario().cep) != null) {
            edtCep.setText(String.valueOf(getUsuario().cep));
        }
        if (getUsuario().endereco != null) {
            edtEndereco.setText(getUsuario().endereco);
        }
        if (String.valueOf(getUsuario().numero) != null) {
            edtNumero.setText(String.valueOf(getUsuario().numero));
        }
        if (getUsuario().complemento != null) {
            edtComplemento.setText(getUsuario().complemento);
        }
        if (getUsuario().cidade != null) {
            edtCidade.setText(getUsuario().cidade);
        }
        if (getUsuario().estado != null) {
            spinnerUf.setSelection(adapter.getPosition(getUsuario().estado));
        }
        if (getUsuario().telefone != null) {
            edtTelefone.setText(getUsuario().telefone);
        }
        if (getUsuario().email != null) {
            edtEmail.setText(getUsuario().email);
        }
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
                .stepTitleTextColor(Color.rgb(241, 89, 34))
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

                //TODO fazer validacao dos campos antes de passar para o passo seguinte
                verticalStepperForm.setStepAsCompleted(DADOS_PESSOAIS_STEP_NUM);
                break;
            case LOCALIZACAO_STEP_NUM:
                verticalStepperForm.setStepAsCompleted(LOCALIZACAO_STEP_NUM);
                break;
            case CONTATO_STEP_NUM:
                verticalStepperForm.setStepAsCompleted(CONTATO_STEP_NUM);
                break;
        }
    }


    //cria steps
    private View criaStepDadosPessoais() {
        LayoutInflater inflater = LayoutInflater.from(getBaseContext());
        dadosPessoaisStep = (LinearLayout) inflater.inflate(
                R.layout.step_cadastro_usuario_dados_pessoais, null, false);

        edtNomeUsuario = (EditText) dadosPessoaisStep.findViewById(R.id.edtNomeUsuario);

        //valida os dados do formulário se passar vai para proximo
//        verticalStepperForm.goToNextStep();

        return dadosPessoaisStep;
    }

    private View criaStepLocalizacao() {
        LayoutInflater inflater = LayoutInflater.from(getBaseContext());
        localizacaoStep = (LinearLayout) inflater.inflate(
                R.layout.step_cadastro_usuario_localizacao, null, false);

        //valida os dados do formulário se passar vai para proximo
        verticalStepperForm.goToNextStep();

        return localizacaoStep;
    }

    private View criaStepContato() {
        LayoutInflater inflater = LayoutInflater.from(getBaseContext());
        contatoStep = (LinearLayout) inflater.inflate(
                R.layout.step_cadastro_usuario_contato, null, false);


        //valida os dados do formulário se passar vai para proximo
        verticalStepperForm.goToNextStep();

        return contatoStep;

    }


    @Override
    public void sendData() {

//        if (user_profile_photo.) {
//            // Loading profile image
//            Glide.with(this).load(getUsuario().imagens.get(0)).apply(RequestOptions.circleCropTransform()).into(user_profile_photo);
//        }

        if(Utils.validaEditText(edtNomeUsuario)){
            getUsuario().nome = edtNomeUsuario.getText().toString();
        }
        if(Utils.validaEditText(edtDataNascimento)){
            getUsuario().dataNascimento = edtDataNascimento.getText().toString();
        }
        if(Utils.validaEditText(edtCpf)){
            getUsuario().cpf = edtCpf.getText().toString();
        }
        if(Utils.validaEditText(edtCep)){
            getUsuario().cep = Integer.parseInt(edtCep.getText().toString());
        }
        if(Utils.validaEditText(edtEndereco)){
            getUsuario().endereco = edtEndereco.getText().toString();
        }
        if(Utils.validaEditText(edtNumero)){
            getUsuario().numero = Integer.parseInt(edtNumero.getText().toString());
        }
        if(Utils.validaEditText(edtComplemento)){
            getUsuario().complemento = edtComplemento.getText().toString();
        }
        if(Utils.validaEditText(edtCidade)){
            getUsuario().cidade = edtCidade.getText().toString();
        }
        if(!spinnerUf.getAdapter().isEmpty()){
            getUsuario().estado = spinnerUf.getSelectedItem().toString();
        }
        if(Utils.validaEditText(edtTelefone)){
            getUsuario().telefone = edtTelefone.getText().toString();
        }
        if(Utils.validaEditText(edtEmail)){
            getUsuario().email = edtEmail.getText().toString();
        }

        salvarUsuario();

    }


    private void salvarUsuario() {
        /**
         *   this.nome = nome;
         this.imagens = imagens;
         this.dataNascimento = dataNascimento;
         this.cpf = cpf;
         this.localizacao = localizacao;
         this.cep = cep;
         this.endereco = endereco;
         this.numero = numero;
         this.complemento = complemento;
         this.bairro = bairro;
         this.cidade = cidade;
         this.estado = estado;
         this.telefone = telefone;
         this.email = email;
         this.meusPets = meusPets;
         this.petsFavoritos = petsFavoritos;
         this.denuncias = denuncias;
         this.notificacoes = notificacoes;
         this.token = token;
         */
        final DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("usuarios");
        myRef.child(getUsuario().id).child("nome").setValue(getUsuario().nome);
        myRef.child(getUsuario().id).child("imagens").setValue(getUsuario().imagens);
        myRef.child(getUsuario().id).child("dataNascimento").setValue(getUsuario().dataNascimento);
        myRef.child(getUsuario().id).child("cpf").setValue(getUsuario().cpf);
        myRef.child(getUsuario().id).child("cep").setValue(getUsuario().cep);
        myRef.child(getUsuario().id).child("endereco").setValue(getUsuario().endereco);
        myRef.child(getUsuario().id).child("numero").setValue(getUsuario().numero);
        myRef.child(getUsuario().id).child("complento").setValue(getUsuario().complemento);
        myRef.child(getUsuario().id).child("cidade").setValue(getUsuario().cidade);
        myRef.child(getUsuario().id).child("estado").setValue(getUsuario().estado);
        myRef.child(getUsuario().id).child("telefone").setValue(getUsuario().telefone);
        myRef.child(getUsuario().id).child("email").setValue(getUsuario().email);


        if (origem == null) {

            Intent intent = new Intent(CadastroUsuarioActivity.this, CadastroAdocaoActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("pet", new Gson().toJson(getPet()));
            intent.putExtras(bundle);
            startActivity(intent);
            finish();

        } else {

            Intent intent = new Intent(CadastroUsuarioActivity.this, PerfilUsuarioActivity.class);
            startActivity(intent);
            finish();

        }
    }

    //TODO ESTA FUNCAO DEVE SER USADA NO LOGIN, PARA SALVAR O USUARIO PELA PRIMEIRA VEZ, COM O TOKKEN E COM O LAT LONG NO GEOFIRE
//    private void salvarUsuario(Usuario entidade) {
//        final Usuario usuario = entidade;
//
//        //LINHAS ADICIONADAS PARA SALVAR O TOKEN QUE SERA UTILIZADO PARA O PUSH NOTIFICATION
//        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
//        Log.d("FirebaseInstanceId", "Refreshed token: " + refreshedToken);
//
//        usuario.token = refreshedToken;
//
//        FirebaseConnection.getConnection();
//        DatabaseReference connectedReference = FirebaseDatabase.getInstance().getReference(".info/connected");
//
//        connectedReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot snapshot) {
//                boolean connected = snapshot.getValue(Boolean.class);
//                if (connected) {
//                    FirebaseConnection.getDatabase().child("usuarios").child(String.valueOf(usuario.id)).setValue(usuario);
//
//                    GeoFire geoFire = new GeoFire(FirebaseConnection.getDatabase().child("geofire"));
//                    geoFire.setLocation(usuario.id, new GeoLocation(usuario.localizacao.lat, usuario.localizacao.lon), new GeoFire.CompletionListener() {
//                        @Override
//                        public void onComplete(String key, DatabaseError error) {
//                            if (error != null) {
//                                System.err.println("There was an error saving the location to GeoFire: " + error);
//                            } else {
//                                System.out.println("Location saved on server successfully!");
//
//                                if(origem == null){
//
//                                    Intent intent = new Intent(CadastroUsuarioActivity.this, CadastroAdocaoActivity.class);
//                                    Bundle bundle = new Bundle();
//                                    bundle.putSerializable("pet", new Gson().toJson(getPet()));
//                                    intent.putExtras(bundle);
//                                    startActivity(intent);
//                                    finish();
//
//                                }else{
//
//                                    Intent intent = new Intent(CadastroUsuarioActivity.this, PerfilUsuarioActivity.class);
//                                    startActivity(intent);
//                                    finish();
//
//                                }
//
//                            }
//                        }
//                    });
//
//
//                } else {
//                    //logar erro
//                }
//            }
//            @Override
//            public void onCancelled(DatabaseError error) {
//                //Log.i("Cancel", "Listener was cancelled");
//            }
//        });
//
//    }

    public View buscaFotoPerfil(View v) {

        Toast toast = Toast.makeText(this, "Add foto", Toast.LENGTH_LONG);
        toast.show();

        return v;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

}
