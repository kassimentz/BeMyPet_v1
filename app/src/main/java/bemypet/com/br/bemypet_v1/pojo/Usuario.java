package bemypet.com.br.bemypet_v1.pojo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by kassianesmentz on 30/08/17.
 *
 */

public class Usuario {

    public String id;
    public String nome;
    public List<String> imagens;
    public String dataNascimento;
    public String cpf;
    public PontoGeo localizacao;
    public Integer cep;
    public String endereco;
    public Integer numero;
    public String complemento;
    public String bairro;
    public String cidade;
    public String estado;
    public String telefone;
    public String email;
    public List<Pet> meusPets;
    public List<Pet> petsFavoritos;
    public List<Denuncias> denuncias;
    public List<Notificacoes> notificacoes;

    public Usuario() {
        id = UUID.randomUUID().toString();
    }

    public Usuario(String nome, List<String> imagens, String dataNascimento, String cpf,
                      PontoGeo localizacao, Integer cep, String endereco, Integer numero,
                      String complemento, String bairro, String cidade, String estado,
                      String telefone, String email, List<Pet> meusPets, List<Pet> petsFavoritos,
                      List<Denuncias> denuncias, List<Notificacoes> notificacoes) {
        this.id = UUID.randomUUID().toString();
        this.nome = nome;
        this.imagens = imagens;
        this.dataNascimento = dataNascimento;
        this.cpf = cpf;
        this.localizacao = localizacao;
        this.cep = cep;
        this.endereco = endereco;
        this.numero = numero;
        this.complemento = complemento;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
        this.telefone = telefone;
        this.email = email;
        this.meusPets = meusPets;
        this.petsFavoritos = petsFavoritos;
        this.denuncias = denuncias;
        this.notificacoes = notificacoes;

    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("imagens", imagens);
        result.put("dataNascimento", dataNascimento);
        result.put("cpf", cpf);
        result.put("localizacao", localizacao);
        result.put("cep", cep);
        result.put("endereco", endereco);
        result.put("numero", numero);
        result.put("complemento", complemento);
        result.put("bairro", bairro);
        result.put("cidade", cidade);
        result.put("estado", estado);
        result.put("telefone", telefone);
        result.put("email", email);
        result.put("meusPets", meusPets);
        result.put("petsFavoritos", petsFavoritos);
        result.put("denuncias", denuncias);
        result.put("notificacoes", notificacoes);
        return result;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id='" + id + '\'' +
                ", nome='" + nome + '\'' +
                ", imagens='" + imagens + '\'' +
                ", dataNascimento='" + dataNascimento + '\'' +
                ", cpf='" + cpf + '\'' +
                ", localizacao=" + localizacao +
                ", cep=" + cep +
                ", endereco='" + endereco + '\'' +
                ", numero=" + numero +
                ", complemento='" + complemento + '\'' +
                ", bairro='" + bairro + '\'' +
                ", cidade='" + cidade + '\'' +
                ", estado='" + estado + '\'' +
                ", telefone='" + telefone + '\'' +
                ", email='" + email + '\'' +
                ", meusPets=" + meusPets +
                ", petsFavoritos=" + petsFavoritos +
                ", denuncias=" + denuncias +
                ", notificacoes=" + notificacoes +
                '}';
    }
}
