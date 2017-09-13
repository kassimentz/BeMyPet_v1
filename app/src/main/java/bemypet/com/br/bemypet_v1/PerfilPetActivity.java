package bemypet.com.br.bemypet_v1;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import bemypet.com.br.bemypet_v1.adapters.CustomGridMesmaNinhadaBaseAdapter;
import bemypet.com.br.bemypet_v1.pojo.Pet;


public class PerfilPetActivity extends AppCompatActivity {

    private Pet pet;

    //temporário até puxar do banco
    GridView grid;
    List<String> nomes = new ArrayList<>();
    List<String> images = new ArrayList<>();

    private TextView user_profile_name, especiePerfilPet, sexoPerfilPet, racaPerfilPet, idadePerfilPet,
            pesoPerfilPet, castradoPerfilPet, vermifugadoPerfilPet, sociavelPerfilPet, temperamentoPerfilPet;
    private ImageView header_cover_image, user_profile_photo;
    private Button buttonPerfil;


    Boolean esconderBotaoAdotar = Boolean.FALSE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_pet);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.visualizarPetToolbar);
        setSupportActionBar(myToolbar);

        ActionBar ab = getSupportActionBar();
        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        ab.setTitle(R.string.activity_title_peril_pet);

        initializeVariables();
        getBundle();
        if(getPet() != null) {
            preencherDados();
        }

        CustomGridMesmaNinhadaBaseAdapter adapter = new CustomGridMesmaNinhadaBaseAdapter (PerfilPetActivity.this, nomes, images);
        grid=(GridView)findViewById(R.id.grid);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(PerfilPetActivity.this, "You Clicked at " +nomes.get(position), Toast.LENGTH_SHORT).show();

            }
        });
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == 2404) {
            if(data != null) {
                String jsonObj = data.getStringExtra("dataPet");
                Pet pet = new Gson().fromJson(jsonObj, Pet.class);
                setPet(pet);
                System.out.println(pet.nome);
                preencherDados();
            }
        }
    }

    private void initializeVariables() {
        header_cover_image = (ImageView) findViewById(R.id.header_cover_image);
        user_profile_photo = (ImageView) findViewById(R.id.user_profile_photo);
        user_profile_name = (TextView) findViewById(R.id.user_profile_name);
        especiePerfilPet = (TextView) findViewById(R.id.especiePerfilPet);
        sexoPerfilPet = (TextView) findViewById(R.id.sexoPerfilPet);
        racaPerfilPet = (TextView) findViewById(R.id.racaPerfilPet);
        idadePerfilPet = (TextView) findViewById(R.id.idadePerfilPet);
        pesoPerfilPet = (TextView) findViewById(R.id.pesoPerfilPet);
        castradoPerfilPet = (TextView) findViewById(R.id.castradoPerfilPet);
        vermifugadoPerfilPet = (TextView) findViewById(R.id.vermifugadoPerfilPet);
        sociavelPerfilPet = (TextView) findViewById(R.id.sociavelPerfilPet);
        temperamentoPerfilPet = (TextView) findViewById(R.id.temperamentoPerfilPet);
        buttonPerfil = (Button) findViewById(R.id.ButtonPerfil);
    }

    private void getBundle() {

        String jsonObj = null;
        String key = "";
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            jsonObj = extras.getString("pet");
            if(extras.containsKey("key")) {
                key = extras.getString("key");
                if(!key.isEmpty()) {
                    setEsconderBotaoAdotar(Boolean.TRUE);
                }
            }
        }
        Pet pet = new Gson().fromJson(jsonObj, Pet.class);

        if(pet != null) {
            setPet(pet);
        }


    }

    private void preencherDados() {

        if(getEsconderBotaoAdotar()) {
            buttonPerfil.setVisibility(View.INVISIBLE);
        } else {
            buttonPerfil.setVisibility(View.VISIBLE);
        }

        if(getPet().imagens.size() > 0) {
            // Loading profile image

            Glide.with(this).load(getPet().imagens.get(0)).apply(RequestOptions.circleCropTransform()).into(user_profile_photo);
            Glide.with(this).load(getPet().imagens.get(getPet().imagens.size()-1)).into(header_cover_image);
        }


        if(getPet().nome != null) {
            user_profile_name.setText(getPet().nome);
        }
        if(getPet().especie != null) {
            especiePerfilPet.setText(getPet().especie);
        }
        if(getPet().sexo != null) {
            sexoPerfilPet.setText(getPet().sexo);
        }
        if(getPet().raca != null) {
            racaPerfilPet.setText(getPet().raca);
        }
        if(getPet().idadeAproximada != null) {
            idadePerfilPet.setText(getPet().idadeAproximada);
        }
        if(getPet().pesoAproximado != null) {
            pesoPerfilPet.setText(String.valueOf(getPet().pesoAproximado));
        }
        if(getPet().castrado != null) {
            castradoPerfilPet.setText(getPet().castrado);
        }
        if(getPet().vermifugado != null) {
            vermifugadoPerfilPet.setText(getPet().vermifugado);
        }
        if(getPet().sociavel != null) {
            StringBuilder stringSociavel = new StringBuilder();
            for (String sociavel : getPet().sociavel) {
                stringSociavel.append(sociavel);
                stringSociavel.append(", ");
            }

            stringSociavel.deleteCharAt(stringSociavel.length()-2);
            sociavelPerfilPet.setText(stringSociavel.toString());

        }
        if(getPet().temperamento != null) {
            StringBuilder stringTemperamento = new StringBuilder();
            for (String temperamento : getPet().temperamento) {
                stringTemperamento.append(temperamento);
                stringTemperamento.append(", ");
            }

            stringTemperamento.deleteCharAt(stringTemperamento.length()-2);
            temperamentoPerfilPet.setText(stringTemperamento.toString());

        }
    }

    public void confirmarSolicitacaoAdocao(View v) {

        Intent intent = new Intent(this, ConfirmarSolicitacaoAdocao.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("pet", new Gson().toJson(getPet()));
        intent.putExtras(bundle);
        startActivityForResult(intent, 1);
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public Boolean getEsconderBotaoAdotar() {
        return esconderBotaoAdotar;
    }

    public void setEsconderBotaoAdotar(Boolean esconderBotaoAdotar) {
        this.esconderBotaoAdotar = esconderBotaoAdotar;
    }
}
