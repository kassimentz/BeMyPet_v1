package bemypet.com.br.bemypet_v1.pojo;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by kassianesmentz on 30/08/17.
 */

public class Notificacoes {

    public String id;
    public String destinatarioId;
    public String mensagem;
    public String data;
    public String hora;
    public String statusNotificacao;
    public Boolean lida;
    public String tipoNotificacao;
    public Adocao adocao;
    public String topico;
    public String titulo;
    public Denuncias denuncia;

    public Notificacoes() {
        id = UUID.randomUUID().toString();
    }

    public Notificacoes(String id, String destinatarioId, String mensagem, String data, String hora, String statusNotificacao,
                        Boolean lida, String tipoNotificacao, Adocao adocao, String topico, String titulo, Denuncias denuncia) {
        this.id = UUID.randomUUID().toString();
        this.destinatarioId = destinatarioId;
        this.mensagem = mensagem;
        this.data = data;
        this.hora = hora;
        this.statusNotificacao = statusNotificacao;
        this.lida = lida;
        this.tipoNotificacao = tipoNotificacao;
        this.adocao = adocao;
        this.topico = topico;
        this.titulo = titulo;
        this.denuncia = denuncia;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("destinatarioId", destinatarioId);
        result.put("mensagem", mensagem);
        result.put("data", data);
        result.put("hora", hora);
        result.put("statusNotificacao", statusNotificacao);
        result.put("lida", lida);
        result.put("tipoNotificacao", tipoNotificacao);
        result.put("adocao", adocao);
        result.put("topico", topico);
        result.put("titulo", titulo);
        result.put("denuncia", denuncia);
        return result;
    }

    @Override
    public String toString() {
        return "Notificacoes{" +
                "id='" + id + '\'' +
                ", destinatarioId='" + destinatarioId + '\'' +
                ", mensagem='" + mensagem + '\'' +
                ", data='" + data + '\'' +
                ", hora='" + hora + '\'' +
                ", statusNotificacao='" + statusNotificacao + '\'' +
                ", lida=" + lida +
                ", tipoNotificacao='" + tipoNotificacao + '\'' +
                ", adocao=" + adocao +
                ", topico='" + topico + '\'' +
                ", titulo='" + titulo + '\'' +
                ", denuncia='" + denuncia + '\'' +
                '}';
    }
}
