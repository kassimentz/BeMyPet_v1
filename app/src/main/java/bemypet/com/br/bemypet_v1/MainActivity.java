package bemypet.com.br.bemypet_v1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void iniciar(View v) {
        //Intent intent = new Intent(this, EscolhaActivity.class);
        //Intent intent = new Intent(this, InicialActivity.class);
        Intent intent = new Intent(this, FiltrosActivity.class);
        startActivity(intent);
    }
}
