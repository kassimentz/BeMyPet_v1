package bemypet.com.br.bemypet_v1;

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

            if(pet.imagens.size() > 0) {
                // Loading profile image

                Glide.with(this).load(pet.imagens.get(0)).apply(RequestOptions.circleCropTransform()).into(user_profile_photo);
                Glide.with(this).load(pet.imagens.get(pet.imagens.size()-1)).into(header_cover_image);
            }


            if(!pet.nome.isEmpty()) {
                user_profile_name.setText(pet.nome);
            }
            if(!pet.especie.isEmpty()) {
                especiePerfilPet.setText(pet.especie);
            }
            if(!pet.sexo.isEmpty()) {
                sexoPerfilPet.setText(pet.sexo);
            }
            if(!pet.raca.isEmpty()) {
                racaPerfilPet.setText(pet.raca);
            }
            if(!pet.idadeAproximada.isEmpty()) {
                idadePerfilPet.setText(pet.idadeAproximada);
            }
            if(!String.valueOf(pet.pesoAproximado).isEmpty()) {
                pesoPerfilPet.setText(String.valueOf(pet.pesoAproximado));
            }
            if(!pet.castrado.isEmpty()) {
                castradoPerfilPet.setText(pet.castrado);
            }
            if(!pet.vermifugado.isEmpty()) {
                vermifugadoPerfilPet.setText(pet.vermifugado);
            }
            if(!pet.sociavel.isEmpty()) {
                StringBuilder stringSociavel = new StringBuilder();
                for (String sociavel : pet.sociavel) {
                    stringSociavel.append(sociavel);
                    stringSociavel.append(", ");
                }

                stringSociavel.deleteCharAt(stringSociavel.length()-2);
                sociavelPerfilPet.setText(stringSociavel.toString());

            }
            if(!pet.temperamento.isEmpty()) {
                StringBuilder stringTemperamento = new StringBuilder();
                for (String temperamento : pet.temperamento) {
                    stringTemperamento.append(temperamento);
                    stringTemperamento.append(", ");
                }

                stringTemperamento.deleteCharAt(stringTemperamento.length()-2);
                temperamentoPerfilPet.setText(stringTemperamento.toString());

            }

        }
    }
}