package bemypet.com.br.bemypet_v1;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;

import bemypet.com.br.bemypet_v1.adapters.CustomGridMesmaNinhadaBaseAdapter;
import bemypet.com.br.bemypet_v1.pojo.Pet;


public class PerfilPetActivity extends AppCompatActivity {

    private Pet pet;

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

    private TextView user_profile_name, especiePerfilPet, sexoPerfilPet, racaPerfilPet, idadePerfilPet,
            pesoPerfilPet, castradoPerfilPet, vermifugadoPerfilPet, sociavelPerfilPet, temperamentoPerfilPet;
    private ImageView header_cover_image, user_profile_photo;


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

        CustomGridMesmaNinhadaBaseAdapter adapter = new CustomGridMesmaNinhadaBaseAdapter (PerfilPetActivity.this, nomes, imageId);
        grid=(GridView)findViewById(R.id.grid);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(PerfilPetActivity.this, "You Clicked at " +nomes[+ position], Toast.LENGTH_SHORT).show();

            }
        });
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
    }

    private void getBundle() {

        String jsonObj = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            jsonObj = extras.getString("pet");
        }
        Pet pet = new Gson().fromJson(jsonObj, Pet.class);

        if(pet != null) {
            setPet(pet);
        }
    }

    private void preencherDados() {

        if(getPet().imagens.size() > 0) {
            // Loading profile image

            Glide.with(this).load(getPet().imagens.get(0)).apply(RequestOptions.circleCropTransform()).into(user_profile_photo);
            Glide.with(this).load(getPet().imagens.get(getPet().imagens.size()-1)).into(header_cover_image);
        }


        if(!getPet().nome.isEmpty()) {
            user_profile_name.setText(getPet().nome);
        }
        if(!getPet().especie.isEmpty()) {
            especiePerfilPet.setText(getPet().especie);
        }
        if(!getPet().sexo.isEmpty()) {
            sexoPerfilPet.setText(getPet().sexo);
        }
        if(!getPet().raca.isEmpty()) {
            racaPerfilPet.setText(getPet().raca);
        }
        if(!getPet().idadeAproximada.isEmpty()) {
            idadePerfilPet.setText(getPet().idadeAproximada);
        }
        if(!String.valueOf(getPet().pesoAproximado).isEmpty()) {
            pesoPerfilPet.setText(String.valueOf(getPet().pesoAproximado));
        }
        if(!getPet().castrado.isEmpty()) {
            castradoPerfilPet.setText(getPet().castrado);
        }
        if(!getPet().vermifugado.isEmpty()) {
            vermifugadoPerfilPet.setText(getPet().vermifugado);
        }
        if(!getPet().sociavel.isEmpty()) {
            StringBuilder stringSociavel = new StringBuilder();
            for (String sociavel : getPet().sociavel) {
                stringSociavel.append(sociavel);
                stringSociavel.append(", ");
            }

            stringSociavel.deleteCharAt(stringSociavel.length()-2);
            sociavelPerfilPet.setText(stringSociavel.toString());

        }
        if(!getPet().temperamento.isEmpty()) {
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
}
