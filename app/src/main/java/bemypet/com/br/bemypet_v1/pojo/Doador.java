package bemypet.com.br.bemypet_v1.pojo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kassianesmentz on 30/08/17.
 */

public class Doador extends Usuario {

    public Doador() {}

    public Doador(String nome, String imagens, String dataNascimento, String cpf,
                  PontoGeo localizacao, Integer cep, String endereco, Integer numero,
                  String complemento, String cidade, String estado, String telefone,
                  String email, List<Pet> meusPets, List<Pet> petsFavoritos,
                  List<Denuncias> denuncias, List<Notificacoes> notificacoes) {
        super(nome, imagens, dataNascimento, cpf, localizacao, cep, endereco,
                numero, complemento, cidade, estado, telefone, email,
                meusPets, petsFavoritos, denuncias, notificacoes);
    }

}
