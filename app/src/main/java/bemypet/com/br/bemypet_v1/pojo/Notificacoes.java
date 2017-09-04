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
    public String remetente;
    public String destinatario;
    public String dataHora;
    public Usuario adotante;
    public Usuario doador;
    public String statusNotificacao;
    public Boolean lida;
    public Pet pet;
    public String tipoNotificacao;

    public Notificacoes() {
        id = UUID.randomUUID().toString();
    }

    public Notificacoes(String mensagem, String remetente, String destinatario, String dataHora,
                        Usuario adotante, Usuario doador, String statusNotificacao, Boolean lida,
                        Pet pet, String tipoNotificacao) {
        this.id = UUID.randomUUID().toString();
        this.mensagem = mensagem;
        this.remetente = remetente;
        this.destinatario = destinatario;
        this.dataHora = dataHora;
        this.adotante = adotante;
        this.doador = doador;
        this.statusNotificacao = statusNotificacao;
        this.lida = lida;
        this.pet = pet;
        this.tipoNotificacao = tipoNotificacao;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("mensagem", mensagem);
        result.put("remetente", remetente);
        result.put("destinatario", destinatario);
        result.put("dataHora", dataHora);
        result.put("adotante", adotante);
        result.put("doador", doador);
        result.put("lida", lida);
        result.put("statusNotificacao", statusNotificacao);
        result.put("pet", pet);
        result.put("tipoNotificacao", tipoNotificacao);
        return result;
    }

    @Override
    public String toString() {
        return "Notificacoes{" +
                "id='" + id + '\'' +
                ", mensagem='" + mensagem + '\'' +
                ", remetente='" + remetente + '\'' +
                ", destinatario='" + destinatario + '\'' +
                ", dataHora='" + dataHora + '\'' +
                ", adotante=" + adotante +
                ", doador=" + doador +
                ", statusNotificacao='" + statusNotificacao + '\'' +
                ", lida=" + lida +
                ", pet=" + pet +
                ", tipoNotificacao=" + tipoNotificacao +
                '}';
    }
}
