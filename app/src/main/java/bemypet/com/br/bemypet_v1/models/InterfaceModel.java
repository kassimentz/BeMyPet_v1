package bemypet.com.br.bemypet_v1.models;

import java.util.List;

/**
 * Created by kassianesmentz on 15/08/17.
 */

public interface InterfaceModel<T> {
    public void salvar(T entidade);
    public void listar(OnGetDataListener listener);
    public T procurarPorId(Integer id);
}
