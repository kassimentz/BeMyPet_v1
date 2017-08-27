package bemypet.com.br.bemypet_v1.pojo;

import com.google.gson.Gson;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by kassianesmentz on 12/08/17.
 */

public class Pet {

    public String id;
    public String nome;
    public String especie;
    public String sexo;
    public String raca;
    public String dataNascimento;
    public String idadeAproximada;
    public Integer pesoAproximado;
    public String parteDeNinhada;
    public String nomeNinhada;
    public String castrado;
    public String vermifugado;
    public String dose;
    public List<String> sociavel;
    public List<String> temperamento;
    public List<String> imagens;
    public String informacoesAdicionais;
    public String status;
    public Boolean cadastroAtivo;
    public PontoGeo localizacao;

    public Pet() {
        id = UUID.randomUUID().toString();
    }

    public Pet(String nome, String especie, String sexo, String raca,
               String dataNascimento, String idadeAproximada, Integer pesoAproximado,
               String parteDeNinhada, String nomeNinhada, String castrado, String vermifugado,
               String dose, List<String> sociavel, List<String> temperamento, List<String> imagens,
               String informacoesAdicionais, String status, Boolean cadastroAtivo,
               PontoGeo localizacao) {

        this.nome = nome;
        this.especie = especie;
        this.sexo = sexo;
        this.raca = raca;
        this.dataNascimento = dataNascimento;
        this.idadeAproximada = idadeAproximada;
        this.pesoAproximado = pesoAproximado;
        this.parteDeNinhada = parteDeNinhada;
        this.nomeNinhada = nomeNinhada;
        this.castrado = castrado;
        this.vermifugado = vermifugado;
        this.dose = dose;
        this.sociavel = sociavel;
        this.temperamento = temperamento;
        this.imagens = imagens;
        this.informacoesAdicionais = informacoesAdicionais;
        this.status = status;
        this.cadastroAtivo = cadastroAtivo;
        this.localizacao = localizacao;
    }

    public Map <String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("nome", nome);
        result.put("especie", especie);
        result.put("sexo", sexo);
        result.put("raca", raca);
        result.put("dataNascimento", dataNascimento);
        result.put("idadeAproximada", idadeAproximada);
        result.put("pesoAproximado", pesoAproximado);
        result.put("parteDeNinhada", parteDeNinhada);
        result.put("nomeNinhada", nomeNinhada);
        result.put("castrado", castrado);
        result.put("vermifugado", vermifugado);
        result.put("dose", dose);
        result.put("sociavel", sociavel);
        result.put("temperamento", temperamento);
        result.put("imagens", imagens);
        result.put("informacoesAdicionais", informacoesAdicionais);
        result.put("status", status);
        result.put("cadastroAtivo", cadastroAtivo);
        result.put("localizacao", localizacao);

        return result;
    }

    @Override
    public String toString() {
        return "Pet{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", especie='" + especie + '\'' +
                ", sexo='" + sexo + '\'' +
                ", raca='" + raca + '\'' +
                ", dataNascimento='" + dataNascimento + '\'' +
                ", idadeAproximada='" + idadeAproximada + '\'' +
                ", pesoAproximado=" + pesoAproximado +
                ", parteDeNinhada='" + parteDeNinhada + '\'' +
                ", nomeNinhada='" + nomeNinhada + '\'' +
                ", castrado='" + castrado + '\'' +
                ", vermifugado='" + vermifugado + '\'' +
                ", dose='" + dose + '\'' +
                ", sociavel=" + sociavel +
                ", temperamento='" + temperamento + '\'' +
                ", imagens=" + imagens +
                ", informacoesAdicionais='" + informacoesAdicionais + '\'' +
                ", status='" + status + '\'' +
                ", cadastroAtivo=" + cadastroAtivo +
                ", localizacao=" + localizacao +
                '}';
    }
}
