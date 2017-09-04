package bemypet.com.br.bemypet_v1.pojo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import bemypet.com.br.bemypet_v1.utils.Constants;

/**
 * Created by kassianesmentz on 30/08/17.
 */

public class Adocao {

    public String id;
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
    public String statusAdocao;
    public Pet pet;
    public Usuario adotante;
    public Usuario doador;

    public Adocao() {
        id = UUID.randomUUID().toString();
    }

    public Adocao(Boolean jaTeveOutrosPets,
                  Integer quantosPetsTeve, List<String> tiposPetsTeve,
                  String oQueAconteceuComEles, Boolean temPetAtualmente,
                  Integer quantosPetsTem, List<String> tiposPetsTem,
                  String tipoMoradia, Boolean possuiPatio,
                  Boolean temCuidadoContraPestes, Boolean possuiTelasProtecao,
                  String informacoesAdicionais, String statusAdocao,
                  Pet pet, Usuario adotante, Usuario doador) {

        this.id = UUID.randomUUID().toString();
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
        this.statusAdocao = statusAdocao;
        this.pet = pet;
        this.adotante = adotante;
        this.doador = doador;
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
        result.put("statusAdocao", statusAdocao);
        result.put("pet", pet);
        result.put("adotante", adotante);
        result.put("doador", doador);
        return result;
    }

    @Override
    public String toString() {
        return "Adocao{" +
                "id=" + id +
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
                ", statusAdocao='" + statusAdocao + '\'' +
                ", pet=" + pet +
                ", adotante='" + adotante + '\'' +
                ", doador='" + doador + '\'' +
                '}';
    }
}
