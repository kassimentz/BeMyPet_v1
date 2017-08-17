package bemypet.com.br.bemypet_v1.models;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

/**
 * Created by kassianesmentz on 16/08/17.
 */

public interface OnGetDataListener {

    public void onStart();
    public void onSuccess(DataSnapshot data);
    public void onFailed(DatabaseError databaseError);
}
