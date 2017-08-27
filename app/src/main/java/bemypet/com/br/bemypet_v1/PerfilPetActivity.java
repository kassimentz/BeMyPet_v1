package bemypet.com.br.bemypet_v1;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.google.gson.Gson;

import bemypet.com.br.bemypet_v1.adapters.CustomGridMesmaNinhadaBaseAdapter;
import bemypet.com.br.bemypet_v1.pojo.Pet;
import bemypet.com.br.bemypet_v1.utils.Utils;

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

//        getBundle();

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

    private void getBundle() {

        String jsonObj = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            jsonObj = extras.getString("pet");
        }
        Pet pet = new Gson().fromJson(jsonObj, Pet.class);

        Utils.showToastMessage(this, pet.nome+ " funcionou");
    }
}
