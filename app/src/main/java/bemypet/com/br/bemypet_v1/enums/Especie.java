package bemypet.com.br.bemypet_v1.enums;

/**
 * Created by kassianesmentz on 12/08/17.
 */

public enum Especie {
    GATO("Gato"),
    CACHORRO("Cachorro"),
    OUTROS("OUTROS");

    private String nome;

    private Especie(String friendlyName) {
        this.nome = friendlyName;
    }

    @Override
    public String toString() {
        return nome;
    }
}
