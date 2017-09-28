package bemypet.com.br.bemypet_v1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import bemypet.com.br.bemypet_v1.utils.Utils;

public class EscolhaActivity extends AppCompatActivity {

    String origem = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_escolha);

        checkNextActivity();

    }

    private void checkNextActivity() {

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey("origem")) {
                origem = extras.getString("origem");
            }
        } else if(Utils.getUsuarioSharedPreferences(getApplicationContext()) != null) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            this.finish();
        }
    }

    public void introAdotante(View v) {
        Intent intent = new Intent(this, IntroAdotanteActivity.class);

        Bundle bundle = new Bundle();
        bundle.putSerializable("origem",origem);
        intent.putExtras(bundle);
        startActivity(intent);
        this.finish();
    }

    public void introAdocao(View v) {
        Intent intent = new Intent(this, IntroAdocaoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("origem",origem);
        intent.putExtras(bundle);
        startActivity(intent);
        startActivity(intent);
        this.finish();
    }
}
