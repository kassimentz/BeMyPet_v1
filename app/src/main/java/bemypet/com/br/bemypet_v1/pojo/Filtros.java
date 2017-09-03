package bemypet.com.br.bemypet_v1.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kassianesmentz on 16/08/17.
 *
 * Classe que contem os parametros para filtrar o pet.
 * Cada vez que essa classe for alterada no dispositivo, sera salva em um JSON (usar GSON).
 * As configuracoes de filtro de cada usuario estarao salvas nesse JSON em cada dispositivo,
 * Tornando mais facil busca-las, converte-las em classe e realizar a busca conforme os filtros
 */

public class Filtros {

    public List<String> especies;
    public String sexo;
    public List<String> raca;
    public String idadeInicial;
    public String idadeFinal;
    public String pesoInicial;
    public String pesoFinal;
    public String castrado;
    public String vermifugado;
    public List<String> sociavel = new ArrayList<>();
    public List<String> temperamento = new ArrayList<>();
    public Integer raioDeBusca;

    public Filtros() {
    }


    @Override
    public String toString() {
        return "Filtros{" +
                "especies='" + especies + '\'' +
                ", sexo='" + sexo + '\'' +
                ", raca='" + raca + '\'' +
                ", idadeInicial='" + idadeInicial + '\'' +
                ", idadeFinal='" + idadeFinal + '\'' +
                ", pesoInicial='" + pesoInicial + '\'' +
                ", pesoFinal='" + pesoFinal + '\'' +
                ", castrado=" + castrado +
                ", vermifugado=" + vermifugado +
                ", sociavel=" + sociavel +
                ", temperamento=" + temperamento +
                ", raioDeBusca=" + raioDeBusca +
                '}';
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("especies", especies);
        result.put("sexo", sexo);
        result.put("raca", raca);
        result.put("idadeInicial", idadeInicial);
        result.put("idadeFinal", idadeFinal);
        result.put("pesoInicial", pesoInicial);
        result.put("pesoFinal", pesoFinal);
        result.put("castrado", castrado);
        result.put("vermifugado", vermifugado);
        result.put("sociavel", sociavel);
        result.put("temperamento", temperamento);
        result.put("raioDeBusca", raioDeBusca);

        return result;
    }
}