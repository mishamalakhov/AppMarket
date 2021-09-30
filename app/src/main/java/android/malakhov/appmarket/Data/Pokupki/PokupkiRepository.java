package android.malakhov.appmarket.Data.Pokupki;

import android.malakhov.appmarket.Data.Users.UserRepository;
import android.malakhov.appmarket.ICallback;
import androidx.annotation.NonNull;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PokupkiRepository implements IPokupkiRepository {

    private static PokupkiRepository obj;
    private DatabaseReference userRef = UserRepository.get().getCurrUserRef();

    //Singletone
    public static PokupkiRepository get(){
        if (obj == null)
            return new PokupkiRepository();
        else
            return obj;
    }

    @Override
    public void addPokupka(Pokupka pokupka) {
        String id = userRef.child("Pokupki").push().getKey();
        pokupka.setId(id);
        userRef.child("Pokupki").child(id).setValue(pokupka);
    }

    @Override
    public void getPokupki(ICallback callback) {
        List<Pokupka> list = new ArrayList<>();

        userRef.child("Pokupki").orderByChild("date").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    list.add(ds.getValue(Pokupka.class));
                }
                Collections.reverse(list);
                callback.callingBack(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void deletePokupka(Pokupka pokupka) {
        userRef.child("Pokupki").child(pokupka.getId()).removeValue();
    }
}
