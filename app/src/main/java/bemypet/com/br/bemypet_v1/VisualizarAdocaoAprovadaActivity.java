package bemypet.com.br.bemypet_v1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class VisualizarAdocaoAprovadaActivity extends AppCompatActivity {

    /**
     * Quando a notificação (mensagem) recebida for avisando da aprovacao da adocao, mostrar uma tela com a rota para buscar o pet, nome, telefone e email do dono para contato.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_adocao_aprovada);
    }
}
