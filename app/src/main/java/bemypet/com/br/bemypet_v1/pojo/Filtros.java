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

    public String especie;
    public String sexo;
    public String raca;
    public String idade;
    public String peso;
    public Boolean castrado;
    public Boolean vermifugado;
    public List<String> sociavel = new ArrayList<>();
    public List<String> temperamento = new ArrayList<>();
    public Integer raioDeBusca;

    public Filtros() {
    }


    @Override
    public String toString() {
        return "Filtros{" +
                "especie='" + especie + '\'' +
                ", sexo='" + sexo + '\'' +
                ", raca='" + raca + '\'' +
                ", idade='" + idade + '\'' +
                ", peso='" + peso + '\'' +
                ", castrado=" + castrado +
                ", vermifugado=" + vermifugado +
                ", sociavel=" + sociavel +
                ", temperamento=" + temperamento +
                ", raioDeBusca=" + raioDeBusca +
                '}';
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("especie", especie);
        result.put("sexo", sexo);
        result.put("raca", raca);
        result.put("idade", idade);
        result.put("peso", peso);
        result.put("castrado", castrado);
        result.put("vermifugado", vermifugado);
        result.put("sociavel", sociavel);
        result.put("temperamento", temperamento);
        result.put("raioDeBusca", raioDeBusca);

        return result;
    }
}
