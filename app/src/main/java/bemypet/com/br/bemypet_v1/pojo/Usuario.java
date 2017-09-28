package bemypet.com.br.bemypet_v1.pojo;

import java.util.ArrayList;
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
    public String senha;
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
    public String token;


    public Usuario() {
        this.id = UUID.randomUUID().toString();
        this.senha = "";
        this.nome = "";
        this.imagens = new ArrayList<>();
        this.dataNascimento = "";
        this.cpf = "";
        this.localizacao = new PontoGeo();
        this.cep = 0;
        this.endereco = "";
        this.numero = 0;
        this.complemento = "";
        this.bairro = "";
        this.cidade = "";
        this.estado = "";
        this.telefone = "";
        this.email = "";
        this.meusPets = new ArrayList<>();
        this.petsFavoritos = new ArrayList<>();
        this.denuncias = new ArrayList<>();
        this.notificacoes = new ArrayList<>();
        this.token = "";
    }


    public Usuario(String nome, String senha, List<String> imagens, String dataNascimento, String cpf,
                      PontoGeo localizacao, Integer cep, String endereco, Integer numero,
                      String complemento, String bairro, String cidade, String estado,
                      String telefone, String email, List<Pet> meusPets, List<Pet> petsFavoritos,
                      List<Denuncias> denuncias, List<Notificacoes> notificacoes, String token) {
        this.id = UUID.randomUUID().toString();
        this.nome = nome;
        this.senha = senha;
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
        this.token = token;
    }

    public String getLogradouro() {
        StringBuilder logradouro = new StringBuilder();

        if(this.endereco != null
                && this.endereco.length() >1
                && this.numero != null
                && this.numero > 0
                && this.cidade != null
                && this.cidade.length() > 1) {
            logradouro.append(this.endereco+", ");
            logradouro.append(this.numero+", ");
            logradouro.append(this.cidade);
        }
        return logradouro.toString();
    }

    public void addMeuPet(Pet pet) {
        if(pet != null) {
            this.meusPets.add(pet);
        }
    }

    public void addFavorito(Pet pet) {
        if(pet != null) {
            this.petsFavoritos.add(pet);
        }
    }

    public void removerFavorito (Pet pet) {
        if(pet != null) {

            for(int i = 0; i < this.petsFavoritos.size(); i++) {
                Pet p = this.petsFavoritos.get(i);
                if(p.id.equals(pet.id)) {
                    this.petsFavoritos.remove(i);
                    break;
                }
            }
        }
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("nome", nome);
        result.put("senha", senha);
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
        result.put("token", token);
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
                ", token=" + token +
                '}';
    }

    public void addImagem(String url) {
        if(url != null && !url.isEmpty()) {
            imagens.add(url);
        }
    }
}
