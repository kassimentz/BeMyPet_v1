package bemypet.com.br.bemypet_v1;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class PoliticaDePrivacidadeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_politica_de_privacidade);

        Toolbar myChildToolbar = (Toolbar) findViewById(R.id.politicaDePrivacidadeToolbar);
        setSupportActionBar(myChildToolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(R.string.privacy_policy);
    }
}
