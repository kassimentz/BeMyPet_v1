package bemypet.com.br.bemypet_v1;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.google.gson.Gson;

import bemypet.com.br.bemypet_v1.pojo.Pet;
import bemypet.com.br.bemypet_v1.utils.Utils;

public class PerfilPetActivity extends AppCompatActivity {

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

        getBundle();

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
