package bemypet.com.br.bemypet_v1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class EscolhaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_escolha);
    }

    public void introAdotante(View v) {
        Intent intent = new Intent(this, IntroAdotanteActivity.class);
        startActivity(intent);
        this.finish();
    }

    public void introAdocao(View v) {
        Intent intent = new Intent(this, IntroAdocaoActivity.class);
        startActivity(intent);
        this.finish();
    }
}
