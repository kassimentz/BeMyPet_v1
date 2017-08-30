package bemypet.com.br.bemypet_v1.pojo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kassianesmentz on 30/08/17.
 *
 * Ja teve outros pet? Boolean
 Quantos pets teve? Integer
 Tipo de pets teve? List<String>
 O que aconteceu com eles? List<String>
 Tem pet atualmente? Boolean
 Quantos pets tem? Integer
 Tipos de pet tem? List<String>
 Moradia String
 Patio (casa) Boolean
 Cuidado Contra Pestes Boolean
 Telas Boolean
 Informações adicionais String
 */

public class Adotante extends Usuario {

    public Boolean jaTeveOutrosPets;
    public Integer quantosPetsTeve;
    public List<String> tiposPetsTeve;
    public List<String> oQueAconteceuComEles;
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

    public Adotante(String nome,
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
                    Boolean jaTeveOutrosPets,
                    Integer quantosPetsTeve,
                    List<String> tiposPetsTeve,
                    List<String> oQueAconteceuComEles,
                    Boolean temPetAtualmente,
                    Integer quantosPetsTem,
                    List<String> tiposPetsTem,
                    String tipoMoradia,
                    Boolean possuiPatio,
                    Boolean temCuidadoContraPestes,
                    Boolean possuiTelasProtecao,
                    String informacoesAdicionais) {
        super(nome, imagens, dataNascimento, cpf, localizacao, cep, endereco, numero, complemento, cidade, estado, telefone, email);
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
