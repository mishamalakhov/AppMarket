package android.malakhov.appmarket.Data.Users;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.malakhov.appmarket.Activities.MarketActivity;
import android.malakhov.appmarket.Data.FireBaseDB;
import android.malakhov.appmarket.ICallback;
import android.malakhov.appmarket.R;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserRepository implements IUserRepository {

    private static UserRepository obj;
    private FirebaseDatabase db = FireBaseDB.get().getDataBase();
    private FirebaseAuth auth = FireBaseDB.get().getAuth();


    //Singletone
    public static UserRepository get() {
        if (obj == null)
            return new UserRepository();
        else
            return obj;
    }


    @Override
    public DatabaseReference getCurrUserRef() {
        return db.getReference("Users").
                child(FireBaseDB.get().getAuth().getCurrentUser().getUid());
    }

    @Override
    public void getCurrentUser(ICallback callback) {
        getCurrUserRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                callback.callingBack(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void getAdminsList(ICallback callback) {
        db.getReference("Admins").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<User> list = new ArrayList<>();
                if (!snapshot.exists())
                    callback.callingBack(list);
                else
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String id = ds.getValue(String.class);
                        db.getReference("Users").child(id).
                                addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        User user = snapshot.getValue(User.class);
                                        list.add(user);
                                        callback.callingBack(list);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                    }
                                });
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void deleteAdmin(String id) {
        db.getReference("Admins").child(id).
                removeValue();
    }

    @Override
    public void giveAdmin(String id) {
        db.getReference("Admins")
                .child(id).setValue(id);
    }

    @Override
    public void exit() {
        auth.signOut();
    }

    @Override
    public void addCardToDB(String number, String date, String CVC) {
        getCurrUserRef().child("cardNumber").setValue(number);
        getCurrUserRef().child("cardDate").setValue(date);
        getCurrUserRef().child("CVC").setValue(CVC);
    }

    @Override
    public void getUserRole(Resources resources, ICallback callback) {
        db.getReference("Admins").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String role = resources.getString(R.string.User);
                if (auth.getCurrentUser().getUid().equals("kPuPGcafHfYhoyrMBibwiH3DL822"))
                    role = resources.getString(R.string.SuperAdmin);
                else {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        if (ds.getValue(String.class).equals(FireBaseDB.get().getAuth().getCurrentUser().getUid())) {
                            role = resources.getString(R.string.Admin);
                            break;
                        }
                    }
                }
                callback.callingBack(role);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void getUserPokupki(ICallback callback) {
        db.getReference("Users").child(auth.getCurrentUser().getUid()).child("Pokupki").
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!snapshot.exists()) callback.callingBack(null);
                        else callback.callingBack("yes");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    public void getCardNumber(ICallback callback) {
        getCurrUserRef().child("cardNumber").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists())
                    callback.callingBack(null);
                String number = snapshot.getValue().toString();
                callback.callingBack(number);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void getCardCVC(ICallback callback) {
        getCurrUserRef().child("CVC").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String cvc = snapshot.getValue().toString();
                callback.callingBack(cvc);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void getCardDate(ICallback callback) {
        getCurrUserRef().child("cardDate").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String date = snapshot.getValue().toString();
                callback.callingBack(date);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void deleteCard() {
        getCurrUserRef().child("cardNumber").removeValue();
        getCurrUserRef().child("cardDate").removeValue();
        getCurrUserRef().child("CVC").removeValue();
    }

    @Override
    public void registerUser(String password, User user, ICallback callback) {
        auth.createUserWithEmailAndPassword(user.getEmail(), password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            user.setId(auth.getCurrentUser().getUid());
                            addUserToDB(user);
                        }
                        callback.callingBack(task);
                    }
                });
    }

    @Override
    public void addUserToDB(User user) {
        db.getReference("Users").child(user.getId()).setValue(user);
    }

    @Override
    public void signIn(String email, String password, ICallback callback) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                callback.callingBack(task);
            }
        });
    }
}
