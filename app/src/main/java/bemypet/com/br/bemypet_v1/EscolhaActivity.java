package bemypet.com.br.bemypet_v1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class EscolhaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escolha);
    }

    public void introAdotante(View v) {
        Intent intent = new Intent(this, IntroAdotanteActivity.class);
        startActivity(intent);
    }

    public void introAdocao(View v) {
        Intent intent = new Intent(this, IntroAdocaoActivity.class);
        startActivity(intent);
    }
}
