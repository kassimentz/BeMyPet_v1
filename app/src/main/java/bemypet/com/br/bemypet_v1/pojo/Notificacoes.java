package bemypet.com.br.bemypet_v1.pojo;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by kassianesmentz on 30/08/17.
 */

public class Notificacoes {

    public String id;
    public String mensagem;
    public String dataHora;
    public String statusNotificacao;
    public Boolean lida;
    public String tipoNotificacao;
    public Adocao adocao;

    public Notificacoes() {
        id = UUID.randomUUID().toString();
    }

    public Notificacoes(String id, String mensagem, String dataHora, String statusNotificacao, Boolean lida, String tipoNotificacao, Adocao adocao) {
        this.id = UUID.randomUUID().toString();
        this.mensagem = mensagem;
        this.dataHora = dataHora;
        this.statusNotificacao = statusNotificacao;
        this.lida = lida;
        this.tipoNotificacao = tipoNotificacao;
        this.adocao = adocao;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("mensagem", mensagem);
        result.put("dataHora", dataHora);
        result.put("statusNotificacao", statusNotificacao);
        result.put("lida", lida);
        result.put("tipoNotificacao", tipoNotificacao);
        result.put("adocao", adocao);
        return result;
    }

    @Override
    public String toString() {
        return "Notificacoes{" +
                "id='" + id + '\'' +
                ", mensagem='" + mensagem + '\'' +
                ", dataHora='" + dataHora + '\'' +
                ", statusNotificacao='" + statusNotificacao + '\'' +
                ", lida=" + lida +
                ", tipoNotificacao='" + tipoNotificacao + '\'' +
                ", adocao=" + adocao +
                '}';
    }
}
