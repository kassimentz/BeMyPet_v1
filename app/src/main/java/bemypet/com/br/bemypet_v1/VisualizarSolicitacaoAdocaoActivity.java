package bemypet.com.br.bemypet_v1;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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
import bemypet.com.br.bemypet_v1.pojo.Notificacoes;
import bemypet.com.br.bemypet_v1.pojo.Pet;
import bemypet.com.br.bemypet_v1.pojo.Usuario;
import bemypet.com.br.bemypet_v1.services.NotificacoesService;
import bemypet.com.br.bemypet_v1.utils.Constants;
import bemypet.com.br.bemypet_v1.utils.Utils;

public class VisualizarSolicitacaoAdocaoActivity extends AppCompatActivity {

    private Adocao adocao;
    private Notificacoes notificacao;
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
            txtIdadePet.setText(getPet().idadeAproximada + " anos.");
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

        Intent intent = new Intent(this, PerfilUsuarioActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("usuario", new Gson().toJson(getAdotante()));
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void visualizaPerfilPet(View v) {
        Intent intent = new Intent(this, PerfilPetActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("pet", new Gson().toJson(getPet()));
        bundle.putString("key", "visualizarAdocao");
        intent.putExtras(bundle);
        startActivity(intent);

    }

    public void reprovarAdocao(View v) {

        inserirNotificacaoReprovada();

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Adoção Reprovada!");
        alertDialogBuilder
                .setMessage("Uma mensagem foi enviada informando a reprovação. O processo de adoção de "+getPet().nome+" com este usuário está encerrado!")
                .setCancelable(false)
                .setPositiveButton("OK",new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(getApplication(), InicialActivity.class);
                        startActivity(intent);
                        VisualizarSolicitacaoAdocaoActivity.this.finish();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    private void inserirNotificacaoReprovada() {
        //atualizar o status do pet no banco para "STATUS_PET_DISPONIVEL", para que apareca NOVAMENTE nas buscas
        getPet().status = Constants.STATUS_PET_DISPONIVEL;
        getPet().adotante = null;
        atualizarPet(getPet());

        atualizarAdocao(Constants.TIPO_NOTIFICACAO_ADOCAO_REPROVADA);

        //atualizar a notificacao inicial recebida para respondida
        atualizarNotificacaoInicial();

        Notificacoes notReprovada = new Notificacoes();
        notReprovada.destinatarioId = getAdotante().id;
        notReprovada.mensagem = "Adoção Reprovada. Infelizmente o usuário "+getPet().doador.nome+" reprovou a adoção do pet "+getPet().nome;
        notReprovada.data = Utils.getCurrentDate();
        notReprovada.hora = Utils.getCurrentTime();
        notReprovada.titulo = "Adoção de "+getPet().nome+" Reprovada";
        notReprovada.statusNotificacao = Constants.STATUS_NOTIFICACAO_ENVIADA;
        notReprovada.topico = Constants.TOPICO_ADOÇÃO_REPROVADA;
        notReprovada.lida = Boolean.FALSE;
        notReprovada.adocao = getAdocao();
        notReprovada.denuncia = null;

        //salvar notificacao no firebase
        salvarNotificacao(notReprovada);

    }

    public void aprovarAdocao(View v) {

        inserirNotificacaoAprovada();

        AlertDialog.Builder dialogAprovado = new AlertDialog.Builder(this);
        dialogAprovado.setTitle("Adoção Aprovada!");
        dialogAprovado
                .setMessage("Uma mensagem foi enviada informando a aprovação. O processo de adoção de "+getPet().nome+" está quase no fim! Entre em contato com "+getAdotante().nome+" para combinar a entrega do pet!")
                .setCancelable(false)
                .setPositiveButton("OK",new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(getApplication(), InicialActivity.class);
                        startActivity(intent);
                        VisualizarSolicitacaoAdocaoActivity.this.finish();
                    }
                });

        AlertDialog alert = dialogAprovado.create();
        alert.show();

    }

    private void inserirNotificacaoAprovada() {

        //atualizar status do pet
        getPet().status = Constants.STATUS_PET_ADOTADO;
        getPet().atualDonoID = getAdotante().id;
        getPet().adotante = getAdotante();
        atualizarPet(getPet());

        //atualizar a adocao
        atualizarAdocao(Constants.TIPO_NOTIFICACAO_ADOCAO_APROVADA);
        //adicionar o pet na lista do usuario

        //atualizar a notificacao inicial recebida para respondida
        atualizarNotificacaoInicial();

        //criar nova notificacao de resposta
        Notificacoes notAprovada = new Notificacoes();
        notAprovada.destinatarioId = getAdocao().adotante.id;
        notAprovada.mensagem = "Adoção Aprovada! O usuário "+getAdocao().doador.nome+" aprovou sua solicitação para a adoção do pet "+getPet().nome+ " Entre em contato com ele para combinar tudo direitinho!";
        notAprovada.data = Utils.getCurrentDate();
        notAprovada.hora = Utils.getCurrentTime();
        notAprovada.titulo = "Adoção de "+getPet().nome+" Aprovada!";
        notAprovada.statusNotificacao = Constants.STATUS_NOTIFICACAO_ENVIADA;
        notAprovada.topico = Constants.TOPICO_ADOÇÃO_APROVADA;
        notAprovada.lida = Boolean.FALSE;
        System.out.println(getAdocao());
        notAprovada.adocao = getAdocao();
        notAprovada.denuncia = null;

        //salvar nova notificacao no firebase
        salvarNotificacao(notAprovada);

        //getAdocao().adotante.addMeuPet(getPet());
        atualizarUsuario(getAdocao().adotante);

    }


    private void atualizarUsuario(Usuario data) {
        List<Pet> pets = new ArrayList<>();
        pets.add(getPet());
        //adicionar lista de pets para o usuario com o pet novo
        final DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("usuarios");
        myRef.child(data.id).child("meusPets").setValue(pets);
    }


    private void salvarNotificacao(Notificacoes data) {
        final Notificacoes not = data;
        FirebaseConnection.getConnection();
        DatabaseReference connectedReference = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                    FirebaseConnection.getDatabase().child("notificacoes").child(String.valueOf(not.id)).setValue(not);
                    System.out.println("notiifcacao salva "+ not.id);
                    NotificacoesService.sendNotification(not.adocao.adotante.token, not.titulo, not, getApplicationContext());

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



    private void atualizarNotificacaoInicial() {
        //atualizar notificacao inicial para respondida
        getNotificacao().statusNotificacao = Constants.STATUS_NOTIFICACAO_RESPONDIDA;
        System.out.println("notiifcacao atualizada "+ getNotificacao().id);
        final DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("notificacoes");
        myRef.child(getNotificacao().id).child("statusNotificacao").setValue(getNotificacao().statusNotificacao);
        myRef.child(getNotificacao().id).child("lida").setValue(getNotificacao().lida);
    }

    private void atualizarAdocao(String status) {
        getAdocao().statusAdocao = status;
        final DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("adocoes");
        myRef.child(getAdocao().id).child("statusAdocao").setValue(getAdocao().statusAdocao);
    }

    private void atualizarPet(final Pet updatePet) {
        //atualizar o status do pet no banco para "STATUS_PET_ADOTADO", para que nao apareca nas buscas
        final DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("pets");
        myRef.child(updatePet.id).child("status").setValue(updatePet.status);
        myRef.child(updatePet.id).child("atualDonoID").setValue(updatePet.atualDonoID);
        myRef.child(updatePet.id).child("adotante").setValue(updatePet.adotante);
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

        String jsonNotificacao = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            jsonNotificacao = extras.getString("notificacao");
        }
        Notificacoes notificacao = new Gson().fromJson(jsonNotificacao, Notificacoes.class);
        if(notificacao != null) {
            setNotificacao(notificacao);
            setAdocao(notificacao.adocao);
            setAdotante(notificacao.adocao.adotante);
            setPet(notificacao.adocao.pet);
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

    public Usuario getAdotante() {
        return adotante;
    }

    public void setAdotante(Usuario adotante) {
        this.adotante = adotante;
    }

    public Notificacoes getNotificacao() {
        return notificacao;
    }

    public void setNotificacao(Notificacoes notificacao) {
        this.notificacao = notificacao;
    }
}
