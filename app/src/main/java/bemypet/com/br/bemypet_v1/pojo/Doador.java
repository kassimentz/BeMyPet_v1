package bemypet.com.br.bemypet_v1.pojo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kassianesmentz on 30/08/17.
 */

public class Doador extends Usuario {

    private static String TIPO_USUARIO = "DOADOR";

    public Doador() {
        this.tipoUsuario = TIPO_USUARIO;
    }

    public Doador(String nome, List<String> imagens, String dataNascimento, String cpf, PontoGeo localizacao, Integer cep,
                  String endereco, Integer numero, String complemento, String bairro, String cidade, String estado,
                  String telefone, String email, List<Pet> meusPets, List<Pet> petsFavoritos, List<Denuncias> denuncias,
                  List<Notificacoes> notificacoes, String tipoUsuario) {
        super(nome, imagens, dataNascimento, cpf, localizacao, cep, endereco, numero, complemento,
                bairro, cidade, estado, telefone, email, meusPets, petsFavoritos, denuncias, notificacoes, tipoUsuario);
        this.tipoUsuario = TIPO_USUARIO;
    }
}
