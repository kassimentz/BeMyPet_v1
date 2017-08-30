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
    public String imagens;
    public String dataNascimento;
    public String cpf;
    public PontoGeo localizacao;
    public Integer cep;
    public String endereco;
    public Integer numero;
    public String complemento;
    public String cidade;
    public String estado;
    public String telefone;
    public String email;

    public Usuario() {
        id = UUID.randomUUID().toString();
    }

    public Usuario(String nome,
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
                   String email) {
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
        this.cidade = cidade;
        this.estado = estado;
        this.telefone = telefone;
        this.email = email;

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
        result.put("cidade", cidade);
        result.put("estado", estado);
        result.put("telefone", telefone);
        result.put("email", email);

        return result;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "nome='" + nome + '\'' +
                ", imagens='" + imagens + '\'' +
                ", dataNascimento='" + dataNascimento + '\'' +
                ", cpf='" + cpf + '\'' +
                ", localizacao=" + localizacao +
                ", cep=" + cep +
                ", endereco='" + endereco + '\'' +
                ", numero=" + numero +
                ", complemento='" + complemento + '\'' +
                ", cidade='" + cidade + '\'' +
                ", estado='" + estado + '\'' +
                ", telefone='" + telefone + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
