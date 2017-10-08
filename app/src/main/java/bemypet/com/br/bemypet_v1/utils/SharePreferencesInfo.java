package bemypet.com.br.bemypet_v1.utils;

import android.content.Context;

/**
 * Created by kassianesmentz on 08/10/17.
 */

public class SharePreferencesInfo {

    public static void updateIntroStatus(Context context, Boolean status) {
        context.getSharedPreferences("PREF", Context.MODE_PRIVATE)
                .edit()
                .putBoolean("status", status)
                .apply();
    }

    public static Boolean isIntroActivityShown(Context context) {
        return context.getSharedPreferences("PREF", Context.MODE_PRIVATE).getBoolean("status", false);
    }
}
