package bemypet.com.br.bemypet_v1.pojo;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by kassianesmentz on 30/08/17.
 */

class Denuncias {

    public String id;
    public String motivo;
    public Usuario denunciante;
    public Usuario denunciado;
    public String dataHora;

    public Denuncias() {
        id = UUID.randomUUID().toString();
    }

    public Denuncias(String id, String motivo, Usuario denunciante, Usuario denunciado, String dataHora) {
        this.id = UUID.randomUUID().toString();
        this.motivo = motivo;
        this.denunciante = denunciante;
        this.denunciado = denunciado;
        this.dataHora = dataHora;
    }

    @Override
    public String toString() {
        return "Denuncias{" +
                "id='" + id + '\'' +
                ", motivo='" + motivo + '\'' +
                ", denunciante=" + denunciante +
                ", denunciado=" + denunciado +
                ", dataHora='" + dataHora + '\'' +
                '}';
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("motivo", motivo);
        result.put("denunciante", denunciante);
        result.put("denunciado", denunciado);
        result.put("dataHora", dataHora);
        return result;
    }
}
