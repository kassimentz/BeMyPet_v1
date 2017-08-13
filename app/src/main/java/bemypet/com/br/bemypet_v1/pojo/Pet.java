package bemypet.com.br.bemypet_v1.pojo;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kassianesmentz on 12/08/17.
 */

public class Pet {
    private Integer id;
    private String nome;
    private String especie;
    private String sexo;
    private String raca;
    private String dataNascimento;
    private String idadeAproximada;
    private Double pesoAproximado;
    private String parteDeNinhada;
    private String nomeNinhada;
    private String castrado;
    private String vermifugado;
    private String dose;
    private List<String> sociavel;
    private String temperamento;
    private List<String> imagens;
    private String informacoesAdicionais;
    private String status;
    private Boolean cadastroAtivo;
    private PontoGeo localizacao;

    public Pet() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getRaca() {
        return raca;
    }

    public void setRaca(String raca) {
        this.raca = raca;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getIdadeAproximada() {
        return idadeAproximada;
    }

    public void setIdadeAproximada(String idadeAproximada) {
        this.idadeAproximada = idadeAproximada;
    }

    public Double getPesoAproximado() {
        return pesoAproximado;
    }

    public void setPesoAproximado(Double pesoAproximado) {
        this.pesoAproximado = pesoAproximado;
    }

    public String getParteDeNinhada() {
        return parteDeNinhada;
    }

    public void setParteDeNinhada(String parteDeNinhada) {
        this.parteDeNinhada = parteDeNinhada;
    }

    public String getNomeNinhada() {
        return nomeNinhada;
    }

    public void setNomeNinhada(String nomeNinhada) {
        this.nomeNinhada = nomeNinhada;
    }

    public String getCastrado() {
        return castrado;
    }

    public void setCastrado(String castrado) {
        this.castrado = castrado;
    }

    public String getVermifugado() {
        return vermifugado;
    }

    public void setVermifugado(String vermifugado) {
        this.vermifugado = vermifugado;
    }

    public String getDose() {
        return dose;
    }

    public void setDose(String dose) {
        this.dose = dose;
    }

    public List<String> getSociavel() {
        return sociavel;
    }

    public void setSociavel(List<String> sociavel) {
        this.sociavel = sociavel;
    }

    public String getTemperamento() {
        return temperamento;
    }

    public void setTemperamento(String temperamento) {
        this.temperamento = temperamento;
    }

    public List<String> getImagens() {
        return imagens;
    }

    public void setImagens(List<String> imagens) {
        this.imagens = imagens;
    }

    public String getInformacoesAdicionais() {
        return informacoesAdicionais;
    }

    public void setInformacoesAdicionais(String informacoesAdicionais) {
        this.informacoesAdicionais = informacoesAdicionais;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getCadastroAtivo() {
        return cadastroAtivo;
    }

    public void setCadastroAtivo(Boolean cadastroAtivo) {
        this.cadastroAtivo = cadastroAtivo;
    }

    public PontoGeo getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(PontoGeo localizacao) {
        this.localizacao = localizacao;
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
