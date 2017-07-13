package bemypet.com.br.bemypet_v1.configs;

/**
 * Created by kassi on 12/07/17.
 */

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {

    SharedPreferences prefAdocao;
    SharedPreferences prefAdotante;
    SharedPreferences.Editor editor;
    Context _context;

    int PRIVATE_MODE = 0;

    private static final String PREF_ADOCAO_NAME = "bemypet-adocao";
    private static final String PREF_ADOTANTE_NAME = "bemypet-adotante";

    private static final String IS_FIRST_TIME_ADOCAO = "IsFirstTimeAdocao";
    private static final String IS_FIRST_TIME_ADOTANTE = "IsFirstTimeAdotante";

    public PrefManager(Context context) {
        this._context = context;
        prefAdocao = _context.getSharedPreferences(PREF_ADOCAO_NAME, PRIVATE_MODE);
        editor = prefAdocao.edit();

        prefAdotante = _context.getSharedPreferences(PREF_ADOTANTE_NAME, PRIVATE_MODE);
        editor = prefAdotante.edit();
    }

    public void setFirstTimeAdocao(boolean firstTimeAdocao) {
        editor.putBoolean(IS_FIRST_TIME_ADOCAO, firstTimeAdocao);
        editor.commit();
    }

    public boolean isFirstTimeAdocao() {
        return prefAdocao.getBoolean(IS_FIRST_TIME_ADOCAO, true);
    }

    public void setFirstTimeAdotante(boolean firstTimeAdotante) {
        editor.putBoolean(IS_FIRST_TIME_ADOTANTE, firstTimeAdotante);
        editor.commit();
    }

    public boolean isFirstTimeAdotante() {
        return prefAdotante.getBoolean(IS_FIRST_TIME_ADOTANTE, true);
    }
}
