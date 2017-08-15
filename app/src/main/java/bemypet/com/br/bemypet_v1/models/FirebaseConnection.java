package bemypet.com.br.bemypet_v1.models;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by kassianesmentz on 15/08/17.
 */

public class FirebaseConnection {

    private static FirebaseConnection connection = null;

    protected FirebaseConnection() {

    }

    public static FirebaseConnection getConnection() {
        if(connection == null) {
            connection = new FirebaseConnection();
        }

        return connection;
    }

    public static DatabaseReference getDatabase() {
        FirebaseDatabase database =  FirebaseDatabase.getInstance();
        DatabaseReference mDatabase = database.getReference();
        mDatabase.keepSynced(true);
        return mDatabase;
    }

    public static StorageReference getStorage(){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        return storageReference;
    }


}
