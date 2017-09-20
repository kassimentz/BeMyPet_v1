package bemypet.com.br.bemypet_v1;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import bemypet.com.br.bemypet_v1.models.FirebaseConnection;
import bemypet.com.br.bemypet_v1.pojo.Adocao;
import bemypet.com.br.bemypet_v1.pojo.Denuncias;
import bemypet.com.br.bemypet_v1.pojo.Notificacoes;
import bemypet.com.br.bemypet_v1.pojo.Usuario;
import bemypet.com.br.bemypet_v1.services.NotificacoesService;
import bemypet.com.br.bemypet_v1.utils.Constants;
import bemypet.com.br.bemypet_v1.utils.Utils;

public class DenunciarUsuarioActivity extends AppCompatActivity {

    private Denuncias denuncia;
    private Usuario denunciante;
    private Usuario denunciado;

    private RadioGroup radioGroupDenuncia;
    private RadioButton radioMausTratos, radioImproprio;
    private EditText edMotivoDenuncia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_denunciar_usuario);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.denunciarToolbar);
        setSupportActionBar(myToolbar);

        ActionBar ab = getSupportActionBar();
        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        ab.setTitle(R.string.activity_title_denunciar);

        initializeVariables();
        getBundle();
    }

    private void initializeVariables() {
        denuncia = new Denuncias();
        setDenuncia(denuncia);
        radioGroupDenuncia = (RadioGroup) findViewById(R.id.radioGroupDenuncia);
        radioImproprio = (RadioButton) findViewById(R.id.radioImproprio);
        radioMausTratos = (RadioButton) findViewById(R.id.radioMausTratos);
        edMotivoDenuncia = (EditText) findViewById(R.id.edMotivoDenuncia);
    }

    private void getBundle() {
        Usuario denuncianteTmp = Utils.getUsuarioSharedPreferences(getApplicationContext());
        if(denuncianteTmp != null) {
            setDenunciante(denuncianteTmp);
        }

        String jsonDenunciado = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            jsonDenunciado = extras.getString("usuarioDenunciado");
        }
        Usuario usuarioDenunciadoTmp = new Gson().fromJson(jsonDenunciado, Usuario.class);
        if(usuarioDenunciadoTmp != null) {
            setDenunciado(usuarioDenunciadoTmp);
        }
    }


    public void denunciarUsuario(View v){

        if(Utils.validaEditText(edMotivoDenuncia)) {
            getDenuncia().motivo = edMotivoDenuncia.getText().toString();
        }

        int tipoDenuncia = radioGroupDenuncia.getCheckedRadioButtonId();
        RadioButton tipo = (RadioButton) findViewById(tipoDenuncia);
        getDenuncia().tipo = tipo.getText().toString();

        getDenuncia().denunciado = getDenunciado();
        getDenuncia().denunciante = getDenunciante();

        Notificacoes notificacao = new Notificacoes();
        notificacao.destinatarioId = getDenunciado().id;
        notificacao.mensagem = "Você foi recebeu uma denúncia de "+getDenuncia().tipo ;
        notificacao.data = Utils.getCurrentDate();
        notificacao.hora = Utils.getCurrentTime();
        notificacao.titulo = "Be My Pet - Denúncia Recebida";
        notificacao.statusNotificacao = Constants.STATUS_NOTIFICACAO_ENVIADA;
        notificacao.tipoNotificacao = getDenuncia().tipo;
        notificacao.topico = Constants.TOPICO_DENUNCIA;
        notificacao.lida = Boolean.FALSE;
        notificacao.adocao = null;
        notificacao.denuncia = getDenuncia();

        //salvar notificacao no firebase
        salvarNotificacao(notificacao);

        NotificacoesService.sendNotificationDenuncia(getDenunciado().token, notificacao.titulo, notificacao, this);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle("Denúncia realizada!");

        alertDialogBuilder
                .setMessage("Uma mensagem foi enviada informando a denúncia realizada. Seus dados não serão enviados!")
                .setCancelable(false)
                .setPositiveButton("OK",new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(getApplication(), InicialActivity.class);
                        startActivity(intent);
                        DenunciarUsuarioActivity.this.finish();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();

    }

    private void salvarNotificacao(Notificacoes data) {
        final Notificacoes notificacao = data;
        FirebaseConnection.getConnection();
        DatabaseReference connectedReference = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                    FirebaseConnection.getDatabase().child("notificacoes").child(String.valueOf(notificacao.id)).setValue(notificacao);
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

    public Denuncias getDenuncia() {
        return denuncia;
    }

    public void setDenuncia(Denuncias denuncia) {
        this.denuncia = denuncia;
    }

    public Usuario getDenunciante() {
        return denunciante;
    }

    public void setDenunciante(Usuario denunciante) {
        this.denunciante = denunciante;
    }

    public Usuario getDenunciado() {
        return denunciado;
    }

    public void setDenunciado(Usuario denunciado) {
        this.denunciado = denunciado;
    }
}
