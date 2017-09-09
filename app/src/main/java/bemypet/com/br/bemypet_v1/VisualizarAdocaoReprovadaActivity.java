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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import bemypet.com.br.bemypet_v1.pojo.Adocao;
import bemypet.com.br.bemypet_v1.pojo.Notificacoes;
import bemypet.com.br.bemypet_v1.pojo.Pet;
import bemypet.com.br.bemypet_v1.pojo.Usuario;

public class VisualizarAdocaoReprovadaActivity extends AppCompatActivity {

    /**
     * Quando a notificação (mensagem) recebida for avisando da reprovacao da adocao, mostrar uma tela com uma mensagem.
     */

    private Adocao adocao;
    private Notificacoes notificacao;
    private Pet pet;
    private TextView txtNomePet, txtIdadePet;
    private ImageView imgPet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_adocao_reprovada);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.visualizarAdocaoReprovadaToolbar);
        setSupportActionBar(myToolbar);

        ActionBar ab = getSupportActionBar();
        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        ab.setTitle(R.string.activity_title_adocao_reprovada);

        initializeVariables();

        getBundle();

        if(getPet() != null) {
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
            txtIdadePet.setText(getPet().idadeAproximada + " anos.");
        }
    }

    private void initializeVariables() {

        txtNomePet = (TextView) findViewById(R.id.txtNomePet);
        txtIdadePet = (TextView) findViewById(R.id.txtIdadePet);
        imgPet = (ImageView) findViewById(R.id.imgPet);
    }

    private void getBundle() {

        String jsonNotificacao = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            jsonNotificacao = extras.getString("notificacao");
        }
        Notificacoes notificacao = new Gson().fromJson(jsonNotificacao, Notificacoes.class);
        if(notificacao != null) {
            setNotificacao(notificacao);
            setAdocao(notificacao.adocao);
            setPet(notificacao.adocao.pet);
        }
    }

    private void updateNotificacao() {
        final DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("notificacoes");
        myRef.child(getNotificacao().id).child("lida").setValue(getNotificacao().lida);
    }

    public void fecharVisualizacaoReprovacao(View v){
        getNotificacao().lida = Boolean.TRUE;
        updateNotificacao();
        this.finish();
    }

    public Adocao getAdocao() {
        return adocao;
    }

    public void setAdocao(Adocao adocao) {
        this.adocao = adocao;
    }

    public Notificacoes getNotificacao() {
        return notificacao;
    }

    public void setNotificacao(Notificacoes notificacao) {
        this.notificacao = notificacao;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

}
