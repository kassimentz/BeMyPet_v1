package bemypet.com.br.bemypet_v1.pojo;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by kassianesmentz on 30/08/17.
 */

public class Denuncias {

    public String id;
    public String motivo;
    public String tipo;
    public Usuario denunciante;
    public Usuario denunciado;

    public Denuncias() {
        id = UUID.randomUUID().toString();
    }

    public Denuncias(String id, String motivo, String tipo, Usuario denunciante, Usuario denunciado, String dataHora) {
        this.id = UUID.randomUUID().toString();
        this.motivo = motivo;
        this.tipo = tipo;
        this.denunciante = denunciante;
        this.denunciado = denunciado;
    }

    @Override
    public String toString() {
        return "Denuncias{" +
                "id='" + id + '\'' +
                ", motivo='" + motivo + '\'' +
                ", tipo='" + tipo + '\'' +
                ", denunciante=" + denunciante +
                ", denunciado=" + denunciado +
                '}';
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("motivo", motivo);
        result.put("tipo", tipo);
        result.put("denunciante", denunciante);
        result.put("denunciado", denunciado);
        return result;
    }
}
