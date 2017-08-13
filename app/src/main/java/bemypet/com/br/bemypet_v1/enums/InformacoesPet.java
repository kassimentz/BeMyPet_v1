package bemypet.com.br.bemypet_v1.enums;

/**
 * Created by kassianesmentz on 12/08/17.
 */

public enum InformacoesPet {

    NAO_CASTRADO("NÃO"),
    CASTRADO("SIM"),
    NAO_VERMIFUGADO("NÃO"),
    VERMIFUGADO("SIM"),
    SOCIAVEL_PESSOAS("PESSOAS"),
    SOCIAVEL_CAES("CÃES"),
    SOCIAVEL_GATOS("GATOS"),
    SOCIAVEL_OUTROS("OUTROS"),
    TEMPERAMENTO_BRAVO("Bravo"),
    TEMPERAMENTO_COM_CUIDADO("Com cuidado"),
    TEMPERAMENTO_CONVIVE_BEM("Convive bem"),
    TEMPERAMENTO_MUITO_DOCIL("Muito Dócil");

    private String informacoes;

    private InformacoesPet(String friendlyName) {
        this.informacoes = friendlyName;
    }

    @Override
    public String toString() {
        return informacoes;
    }
}
