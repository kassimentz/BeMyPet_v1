package bemypet.com.br.bemypet_v1;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;

import bemypet.com.br.bemypet_v1.pojo.Pet;
import bemypet.com.br.bemypet_v1.pojo.Usuario;

public class VisualizarSolicitacaoAdocaoActivity extends AppCompatActivity {

    private Pet pet;
    private Usuario adotante;
    private TextView txtNomePet, txtIdadePet, txtNomeAdotante, txtCidadeAdotante;
    private ImageView imgPet, imgAdotante;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_solicitacao_adocao);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.visualizarSolicitacaoAdocaoToolbar);
        setSupportActionBar(myToolbar);

        ActionBar ab = getSupportActionBar();
        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        ab.setTitle(R.string.activity_title_confirmar_solicitacao_adocao);

        initializeVariables();

        getBundle();

        if(getPet() != null && getAdotante() != null) {
            preencherDados();
        }

    }

    private void preencherDados() {

        if(getPet().imagens.size() > 0) {
            // Loading profile image
            Glide.with(this).load(getPet().imagens.get(0)).apply(RequestOptions.circleCropTransform()).into(imgPet);
        }

        if(getPet().nome != null) {
            txtNomePet.setText(getPet().nome);
        }

        if(getPet().idadeAproximada != null) {
            txtIdadePet.setText(getPet().nome);
        }

        if(getAdotante().imagens.size() > 0) {
            // Loading profile image
            Glide.with(this).load(getAdotante().imagens.get(0)).apply(RequestOptions.circleCropTransform()).into(imgAdotante);
        }

        if(getAdotante().nome != null) {
            txtNomeAdotante.setText(getAdotante().nome);
        }

        if(getAdotante().cidade != null) {
            txtCidadeAdotante.setText(getAdotante().cidade);
        }

    }

    public void visualizarUsuarioPerfil(View v){

    }

    public void visualizaPerfilPet(View v) {

    }

    public void reprovarAdocao(View v) {

    }

    public void aprovarAdocao(View v) {

    }


    private void initializeVariables() {

        txtNomePet = (TextView) findViewById(R.id.txtNomePet);
        txtIdadePet = (TextView) findViewById(R.id.txtIdadePet);
        txtNomeAdotante = (TextView) findViewById(R.id.txtNomeAdotante);
        txtCidadeAdotante = (TextView) findViewById(R.id.txtCidadeAdotante);
        imgAdotante = (ImageView) findViewById(R.id.imgAdotante);
        imgPet = (ImageView) findViewById(R.id.imgPet);
    }

    private void getBundle() {

        String jsonPet = null, jsonAdotante = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            jsonPet = extras.getString("pet");
            jsonAdotante = extras.getString("adotante");
        }
        Pet pet = new Gson().fromJson(jsonPet, Pet.class);

        if(pet != null) {
            setPet(pet);
        }

        Usuario adotante = new Gson().fromJson(jsonAdotante, Usuario.class);
        if(adotante != null) {
            setAdotante(adotante);
        }
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public Usuario getAdotante() {
        return adotante;
    }

    public void setAdotante(Usuario adotante) {
        this.adotante = adotante;
    }
}
