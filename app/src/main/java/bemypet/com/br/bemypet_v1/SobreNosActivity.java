package bemypet.com.br.bemypet_v1;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.google.gson.Gson;

import bemypet.com.br.bemypet_v1.pojo.Pet;
import bemypet.com.br.bemypet_v1.utils.Utils;

public class SobreNosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sobre_nos);

        Toolbar myChildToolbar = (Toolbar) findViewById(R.id.sobreToolbar);
        setSupportActionBar(myChildToolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(R.string.activity_title_sobre);
    }


}
