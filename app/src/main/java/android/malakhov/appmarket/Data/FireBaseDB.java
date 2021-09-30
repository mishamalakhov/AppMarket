package android.malakhov.appmarket.Data;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FireBaseDB {

    private FirebaseDatabase dataBase;
    private FirebaseAuth auth;
    private StorageReference storageRef = FirebaseStorage.getInstance().getReference("ProductImages");
    private static FireBaseDB obj = null;

    public FireBaseDB() {
        dataBase = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    //SingleTone
    public static FireBaseDB get(){
        if (obj == null)
            return new FireBaseDB();
        else
            return obj;
    }

    public FirebaseDatabase getDataBase() {
        return dataBase;
    }
    public FirebaseAuth getAuth() {
        return auth;
    }
    public StorageReference getStorageRef(){ return storageRef; }
}
