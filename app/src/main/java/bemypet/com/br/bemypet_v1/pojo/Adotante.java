package bemypet.com.br.bemypet_v1.pojo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kassianesmentz on 30/08/17.
 */

public class Adotante extends Usuario {

    public Boolean jaTeveOutrosPets;
    public Integer quantosPetsTeve;
    public List<String> tiposPetsTeve;
    public String oQueAconteceuComEles;
    public Boolean temPetAtualmente;
    public Integer quantosPetsTem;
    public List<String> tiposPetsTem;
    public String tipoMoradia;
    public Boolean possuiPatio;
    public Boolean temCuidadoContraPestes;
    public Boolean possuiTelasProtecao;
    public String informacoesAdicionais;


    public Adotante() {

    }

    public Adotante(String nome, List<String> imagens, String dataNascimento, String cpf, PontoGeo localizacao, Integer cep, String endereco, Integer numero,
                    String complemento, String bairro, String cidade, String estado, String telefone, String email, List<Pet> meusPets, List<Pet> petsFavoritos,
                    List<Denuncias> denuncias, List<Notificacoes> notificacoes, String tipoUsuario, Boolean jaTeveOutrosPets, Integer quantosPetsTeve, List<String>
                            tiposPetsTeve, String oQueAconteceuComEles, Boolean temPetAtualmente, Integer quantosPetsTem, List<String> tiposPetsTem, String tipoMoradia,
                    Boolean possuiPatio, Boolean temCuidadoContraPestes, Boolean possuiTelasProtecao, String informacoesAdicionais) {
        super(nome, imagens, dataNascimento, cpf, localizacao, cep, endereco, numero, complemento, bairro, cidade, estado, telefone, email, meusPets, petsFavoritos,
                denuncias, notificacoes, tipoUsuario);
        this.jaTeveOutrosPets = jaTeveOutrosPets;
        this.quantosPetsTeve = quantosPetsTeve;
        this.tiposPetsTeve = tiposPetsTeve;
        this.oQueAconteceuComEles = oQueAconteceuComEles;
        this.temPetAtualmente = temPetAtualmente;
        this.quantosPetsTem = quantosPetsTem;
        this.tiposPetsTem = tiposPetsTem;
        this.tipoMoradia = tipoMoradia;
        this.possuiPatio = possuiPatio;
        this.temCuidadoContraPestes = temCuidadoContraPestes;
        this.possuiTelasProtecao = possuiTelasProtecao;
        this.informacoesAdicionais = informacoesAdicionais;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("jaTeveOutrosPets", jaTeveOutrosPets);
        result.put("quantosPetsTeve", quantosPetsTeve);
        result.put("tiposPetsTeve", tiposPetsTeve);
        result.put("oQueAconteceuComEles", oQueAconteceuComEles);
        result.put("temPetAtualmente", temPetAtualmente);
        result.put("quantosPetsTem", quantosPetsTem);
        result.put("tiposPetsTem", tiposPetsTem);
        result.put("tipoMoradia", tipoMoradia);
        result.put("possuiPatio", possuiPatio);
        result.put("temCuidadoContraPestes", temCuidadoContraPestes);
        result.put("possuiTelasProtecao", possuiTelasProtecao);
        result.put("informacoesAdicionais", informacoesAdicionais);

        return result;
    }

    @Override
    public String toString() {
        return "Adotante{" +
                "jaTeveOutrosPets=" + jaTeveOutrosPets +
                ", quantosPetsTeve=" + quantosPetsTeve +
                ", tiposPetsTeve=" + tiposPetsTeve +
                ", oQueAconteceuComEles=" + oQueAconteceuComEles +
                ", temPetAtualmente=" + temPetAtualmente +
                ", quantosPetsTem=" + quantosPetsTem +
                ", tiposPetsTem=" + tiposPetsTem +
                ", tipoMoradia='" + tipoMoradia + '\'' +
                ", possuiPatio=" + possuiPatio +
                ", temCuidadoContraPestes=" + temCuidadoContraPestes +
                ", possuiTelasProtecao=" + possuiTelasProtecao +
                ", informacoesAdicionais='" + informacoesAdicionais + '\'' +
                '}';
    }
}
