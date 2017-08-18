package bemypet.com.br.bemypet_v1.enums;

/**
 * Created by kassianesmentz on 12/08/17.
 */

public enum Sexo {

    MACHO("MACHO"),
    FEMEA("FÊMEA"),
    NAO_INFORMADO("NÃO SEI");

    private String sexo;

    private Sexo(String friendlyName) {
        this.sexo = friendlyName;
    }

    @Override
    public String toString() {
        return sexo;
    }
}
