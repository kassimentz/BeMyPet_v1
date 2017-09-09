package bemypet.com.br.bemypet_v1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class VisualizarAdocaoReprovadaActivity extends AppCompatActivity {

    /**
     * Quando a notificação (mensagem) recebida for avisando da reprovacao da adocao, mostrar uma tela com uma mensagem.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_adocao_reprovada);
    }
}
