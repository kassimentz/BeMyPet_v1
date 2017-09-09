package bemypet.com.br.bemypet_v1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class VisualizarDenunciaActivity extends AppCompatActivity {

    /**
     * Quando a notificação (mensagem) recebida for de denuncia, mostrar uma tela com os motivos da denuncia mas sem o nome de quem denunciou e pedindo para que a pessoa verifique estes pontos em seu cadastro
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_denuncia);
    }
}
