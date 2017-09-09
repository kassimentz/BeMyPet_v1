package bemypet.com.br.bemypet_v1;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import bemypet.com.br.bemypet_v1.pojo.Denuncias;
import bemypet.com.br.bemypet_v1.pojo.Notificacoes;

public class VisualizarDenunciaActivity extends AppCompatActivity {

    /**
     * Quando a notificação (mensagem) recebida for de denuncia, mostrar uma tela com os motivos da denuncia mas sem o nome de quem denunciou e pedindo para que a pessoa verifique estes pontos em seu cadastro
     */
    private Notificacoes notificacao;
    private Denuncias denuncia;

    private TextView txtTipoDenuncia, txtMotivoDenuncia;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_denuncia);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.visualizarDenunciaToolbar);
        setSupportActionBar(myToolbar);

        ActionBar ab = getSupportActionBar();
        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        ab.setTitle(R.string.activity_title_denuncia);

        initializeVariables();

        getBundle();

        if(getDenuncia() != null) {
            preencherDados();
        }
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
            setDenuncia(notificacao.denuncia);
        }
    }

    private void preencherDados() {

        if(getDenuncia().motivo != null) {
            txtMotivoDenuncia.setText(getDenuncia().motivo);
        }

        if(getDenuncia().tipo != null) {
            txtTipoDenuncia.setText(getDenuncia().tipo);
        }
    }

    private void initializeVariables() {
        txtTipoDenuncia = (TextView) findViewById(R.id.txtTipoDenuncia);
        txtMotivoDenuncia = (TextView) findViewById(R.id.txtMotivoDenuncia);
    }

    private void updateNotificacao() {
        final DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("notificacoes");
        myRef.child(getNotificacao().id).child("lida").setValue(getNotificacao().lida);
    }

    public void fecharVisualizacaoDenuncia(View v) {
        getNotificacao().lida = Boolean.TRUE;
        updateNotificacao();
        this.finish();
    }

    public Notificacoes getNotificacao() {
        return notificacao;
    }

    public void setNotificacao(Notificacoes notificacao) {
        this.notificacao = notificacao;
    }

    public Denuncias getDenuncia() {
        return denuncia;
    }

    public void setDenuncia(Denuncias denuncia) {
        this.denuncia = denuncia;
    }
}
