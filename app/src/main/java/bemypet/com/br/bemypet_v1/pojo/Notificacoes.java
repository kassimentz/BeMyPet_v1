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
    public Usuario remetente;
    public Usuario destinatario;
    public String dataHora;

    public Notificacoes() {
        id = UUID.randomUUID().toString();
    }

    public Notificacoes(String mensagem, Usuario remetente, Usuario destinatario, String dataHora) {
        this.id = UUID.randomUUID().toString();
        this.mensagem = mensagem;
        this.remetente = remetente;
        this.destinatario = destinatario;
        this.dataHora = dataHora;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("mensagem", mensagem);
        result.put("remetente", remetente);
        result.put("destinatario", destinatario);
        result.put("dataHora", dataHora);
        return result;
    }
}
