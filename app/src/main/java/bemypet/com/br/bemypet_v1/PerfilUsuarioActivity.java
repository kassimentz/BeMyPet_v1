package bemypet.com.br.bemypet_v1;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;

import bemypet.com.br.bemypet_v1.adapters.CustomGridMesmaNinhadaBaseAdapter;
import bemypet.com.br.bemypet_v1.pojo.Pet;
import bemypet.com.br.bemypet_v1.pojo.Usuario;
import bemypet.com.br.bemypet_v1.utils.Utils;

public class PerfilUsuarioActivity extends AppCompatActivity {

    //temporário até puxar do banco
    GridView grid;
    String[] nomes = {
            "GATO1",
            "GATO2",
            "GATO3",
            "GATO4",
            "GATO5",
            "GATO6"

    } ;
    int[] imageId = {
            R.drawable.perfil_ninhada,
            R.drawable.perfil_ninhada,
            R.drawable.perfil_ninhada,
            R.drawable.perfil_ninhada,
            R.drawable.perfil_ninhada,
            R.drawable.perfil_ninhada

    };

    private TextView nomeUsuario, dataNascPerfilUse, cpfPerfilUse, cepPerfilUse, enderecoPerfilUse,
            numeroPerfilUse, complementoPerfilUse, cidadePessoalPerfilUse, estadoPerfilUse,
            telefonePerfilUse, emailPerfilUse;
    private ImageView header_cover_image, user_profile_photo;

    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.visualizarUsuarioToolbar);
        setSupportActionBar(myToolbar);

        ActionBar ab = getSupportActionBar();
        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        ab.setTitle(R.string.activity_title_peril_usuario);

        initializeVariables();
        getBundle();
        if(getUsuario() != null) {
            preencherDados();
        }

        CustomGridMesmaNinhadaBaseAdapter adapter = new CustomGridMesmaNinhadaBaseAdapter (PerfilUsuarioActivity.this, nomes, imageId);
        grid=(GridView)findViewById(R.id.grid);
        grid.setAdapter(adapter);


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
            jsonObj = extras.getString("usuario");
        }
        Usuario usuario = new Gson().fromJson(jsonObj, Usuario.class);
        if(usuario != null) {
            setUsuario(usuario);
        }
    }

    private void preencherDados() {

        if(getUsuario().imagens.size() > 0) {
            // Loading profile image

            Glide.with(this).load(getUsuario().imagens.get(0)).apply(RequestOptions.circleCropTransform()).into(user_profile_photo);
            Glide.with(this).load(getUsuario().imagens.get(getUsuario().imagens.size()-1)).into(header_cover_image);
        }
        
        if(getUsuario().nome != null) {
            nomeUsuario.setText(getUsuario().nome);
        }
        if(getUsuario().dataNascimento != null) {
            dataNascPerfilUse.setText(getUsuario().dataNascimento);
        }
        if(getUsuario().cpf != null) {
            cpfPerfilUse.setText(getUsuario().cpf);
        }
        if(getUsuario().cep != null) {
            cepPerfilUse.setText(String.valueOf(getUsuario().cep));
        }
        if(getUsuario().endereco != null) {
            enderecoPerfilUse.setText(getUsuario().endereco);
        }
        if(getUsuario().numero != null) {
            numeroPerfilUse.setText(String.valueOf(getUsuario().numero));
        }
        if(getUsuario().complemento != null) {
            complementoPerfilUse.setText(getUsuario().complemento);
        }
        if(getUsuario().cidade != null) {
            cidadePessoalPerfilUse.setText(getUsuario().cidade);
        }
        if(getUsuario().estado != null) {
            estadoPerfilUse.setText(getUsuario().estado);
        }
        if(getUsuario().telefone != null) {
            telefonePerfilUse.setText(getUsuario().telefone);
        }
        if(getUsuario().email != null) {
            emailPerfilUse.setText(getUsuario().email);
        }
    }

    private void initializeVariables() {
        header_cover_image = (ImageView) findViewById(R.id.header_cover_image);
        user_profile_photo = (ImageView) findViewById(R.id.user_profile_photo);
        nomeUsuario = (TextView) findViewById(R.id.nomeUsuario);
        dataNascPerfilUse = (TextView) findViewById(R.id.dataNascPerfilUse);
        cpfPerfilUse = (TextView) findViewById(R.id.cpfPerfilUse);
        cepPerfilUse = (TextView) findViewById(R.id.cepPerfilUse);
        enderecoPerfilUse = (TextView) findViewById(R.id.enderecoPerfilUse);
        numeroPerfilUse = (TextView) findViewById(R.id.numeroPerfilUse);
        complementoPerfilUse = (TextView) findViewById(R.id.complementoPerfilUse);
        cidadePessoalPerfilUse = (TextView) findViewById(R.id.cidadePessoalPerfilUse);
        estadoPerfilUse = (TextView) findViewById(R.id.estadoPerfilUse);
        telefonePerfilUse = (TextView) findViewById(R.id.telefonePerfilUse);
        emailPerfilUse = (TextView) findViewById(R.id.emailPerfilUse);
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
