package bemypet.com.br.bemypet_v1;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import bemypet.com.br.bemypet_v1.adapters.CustomGridMesmaNinhadaBaseAdapter;
import bemypet.com.br.bemypet_v1.pojo.Pet;
import bemypet.com.br.bemypet_v1.pojo.Usuario;
import bemypet.com.br.bemypet_v1.utils.Utils;

public class PerfilUsuarioActivity extends AppCompatActivity {

    GridView grid;
    List<String> nomes = new ArrayList<>();
    List<String> images = new ArrayList<>();

    private TextView nomeUsuario, dataNascPerfilUse, cpfPerfilUse, cepPerfilUse, enderecoPerfilUse,
            numeroPerfilUse, complementoPerfilUse, cidadePessoalPerfilUse, estadoPerfilUse,
            telefonePerfilUse, emailPerfilUse;
    private ImageView header_cover_image, user_profile_photo, btnEditarUsuario;

    private Usuario usuario;
    private Usuario usuarioLogado;

    Boolean esconderBotaoEditar = Boolean.FALSE;

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

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_usuario, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
            case R.id.denunciar_usuario:
                denunciarUsuario();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void denunciarUsuario() {

        AlertDialog.Builder builder = new AlertDialog.Builder(PerfilUsuarioActivity.this);
        builder.setTitle("Denunciar Usuário");
        builder.setMessage("Você tem certeza que deseja denunciar o usuario "+getUsuario().nome+" ? Todas as informações fornecidas serão de sua responsabilidade.");
        builder.setPositiveButton(R.string.denunciar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                dialog.dismiss();
                Intent intent = new Intent(PerfilUsuarioActivity.this, DenunciarUsuarioActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("usuarioDenunciado", new Gson().toJson(getUsuario()));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                dialog.cancel();
            }
        });

        // Create the AlertDialog
        AlertDialog dialog = builder.create();
        if(!dialog.isShowing()) {
            dialog.show();
        }
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
        } else {
            if(Utils.getUsuarioSharedPreferences(getApplicationContext()) != null) {
                setUsuario(Utils.getUsuarioSharedPreferences(getApplicationContext()));
            }
        }

        if(Utils.getUsuarioSharedPreferences(getApplicationContext()) != null) {
            setUsuarioLogado(Utils.getUsuarioSharedPreferences(getApplicationContext()));
        }

        //trata-se do proprio usuario acessando seu perfil, entao ele pode editar
        //se for outro usuario visualizando o perfil, nao pode editar
        if(getUsuario().id.equalsIgnoreCase(getUsuarioLogado().id)) {
            setEsconderBotaoEditar(Boolean.FALSE);
        } else {
            setEsconderBotaoEditar(Boolean.TRUE);
        }
    }

    private void preencherDados() {

        if(getEsconderBotaoEditar()) {
            btnEditarUsuario.setVisibility(View.INVISIBLE);
        } else {
            btnEditarUsuario.setVisibility(View.VISIBLE);
        }

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

        buscarPetsUsuario();
    }

    private void buscarPetsUsuario() {
        if(getUsuario() != null) {
            FirebaseDatabase.getInstance().getReference().child("pets").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Pet pet = null;
                    List<Pet> petsUsuario = new ArrayList<Pet>();

                    for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                        pet = postSnapshot.getValue(Pet.class);
                        if(pet != null) {
                            if (pet.atualDonoID.equalsIgnoreCase(getUsuario().id)) {
                                petsUsuario.add(pet);
                                nomes.add(pet.nome);
                                if (pet.imagens.size() > 0) {
                                    images.add(pet.imagens.get(0));
                                }
                            }
                        }
                    }

                    CustomGridMesmaNinhadaBaseAdapter adapter = new CustomGridMesmaNinhadaBaseAdapter (PerfilUsuarioActivity.this, nomes, images);
                    grid=(GridView)findViewById(R.id.grid);
                    grid.setAdapter(adapter);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) { }
            });
        }
    }

    private void initializeVariables() {
        header_cover_image = (ImageView) findViewById(R.id.header_cover_image);
        btnEditarUsuario = (ImageView) findViewById(R.id.btnEditarUsuario);
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

    public void editaPerilUsuario(View v){
        Intent intent = new Intent(PerfilUsuarioActivity.this, CadastroUsuarioActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("origem", "perfil");
        intent.putExtras(bundle);
        startActivity(intent);
        this.finish();
    }

    public Boolean getEsconderBotaoEditar() {
        return esconderBotaoEditar;
    }

    public void setEsconderBotaoEditar(Boolean esconderBotaoEditar) {
        this.esconderBotaoEditar = esconderBotaoEditar;
    }

    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public void setUsuarioLogado(Usuario usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
    }
}
