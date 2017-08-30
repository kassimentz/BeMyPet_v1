package bemypet.com.br.bemypet_v1.pojo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kassianesmentz on 30/08/17.
 */

public class Doador extends Usuario {

    public List<Pet> meusPets;
    public List<Pet> petsFavoritos;
    public List<Denuncias> denuncias;
    public List<Notificacoes> notificacoes;

    public Doador() {}

    public Doador(String nome,
                  String imagens,
                  String dataNascimento,
                  String cpf,
                  PontoGeo localizacao,
                  Integer cep,
                  String endereco,
                  Integer numero,
                  String complemento,
                  String cidade,
                  String estado,
                  String telefone,
                  String email,
                  List<Pet> meusPets,
                  List<Pet> petsFavoritos,
                  List<Denuncias> denuncias,
                  List<Notificacoes> notificacoes) {
        super(nome, imagens, dataNascimento, cpf, localizacao, cep, endereco, numero, complemento, cidade, estado, telefone, email);
        this.meusPets = meusPets;
        this.petsFavoritos = petsFavoritos;
        this.denuncias = denuncias;
        this.notificacoes = notificacoes;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("meusPets", meusPets);
        result.put("petsFavoritos", petsFavoritos);
        result.put("denuncias", denuncias);
        result.put("notificacoes", notificacoes);

        return result;
    }

    @Override
    public String toString() {
        return "Doador{" +
                "meusPets=" + meusPets +
                ", petsFavoritos=" + petsFavoritos +
                ", denuncias=" + denuncias +
                ", notificacoes=" + notificacoes +
                '}';
    }
}
