package bemypet.com.br.bemypet_v1.enums;

/**
 * Created by kassianesmentz on 12/08/17.
 */

public enum StatusAdocao {

    ADOTADO("Adotado"),
    DISPONIVEL("Disponivel"),
    EM_ANDAMENTO("Em andamento");

    private String status;

    private StatusAdocao(String friendlyName) {
        this.status = friendlyName;
    }

    @Override
    public String toString() {
        return status;
    }
}
