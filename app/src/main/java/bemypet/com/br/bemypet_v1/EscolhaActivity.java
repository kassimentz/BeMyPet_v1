package bemypet.com.br.bemypet_v1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class EscolhaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escolha);
        getSupportActionBar().hide();
    }
}
