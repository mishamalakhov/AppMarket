package android.malakhov.appmarket;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.List;


public class ProfileFragment extends Fragment {

    private TextView name, mail, admin, idOfUser;
    private CardView  pokupki, creditCard, comments, giveAdmin, adminList, orders;
    private Button exit;
    private FirebaseAuth mAuth;
    private FirebaseDatabase dataBase;
    private User user, user1;
    private String whoIsUser;
    private Toast pokupkiToast, commToast, ordersToast, admToast;


    public static ProfileFragment newInstance(String str){
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString("whoIsUser", str);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        admin = v.findViewById(R.id.admin);
        name = v.findViewById(R.id.name);
        mail = v.findViewById(R.id.mail);
        pokupki = v.findViewById(R.id.pokupki);
        creditCard = v.findViewById(R.id.credit_card);
        comments = v.findViewById(R.id.comments);
        exit = v.findViewById(R.id.exit);
        mAuth = FirebaseAuth.getInstance();
        dataBase = FirebaseDatabase.getInstance();
        giveAdmin = v.findViewById(R.id.giveAdmin);
        adminList = v.findViewById(R.id.adminList);
        orders = v.findViewById(R.id.orders);
        whoIsUser = getArguments().getString("whoIsUser");
        idOfUser = v.findViewById(R.id.idOfUser);

        if (whoIsUser.equals("SuperAdmin")){
            admin.setVisibility(View.VISIBLE);
            admin.setText("SuperAdmin");
            giveAdmin.setVisibility(View.VISIBLE);
            adminList.setVisibility(View.VISIBLE);
            orders.setVisibility(View.VISIBLE);
        }
        else
            if (whoIsUser.equals("Admin")){
                admin.setVisibility(View.VISIBLE);
                admin.setText("Admin");
                orders.setVisibility(View.VISIBLE);
            }


        dataBase.getReference("Users").child(mAuth.getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        user = snapshot.getValue(User.class);
                        name.setText(user.getSecondName() + " " + user.getName());
                        mail.setText(user.getEmail());
                        idOfUser.setText(user.getId());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
        //dgggggggggddddddddddddddddddddddddddddd
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductLab.get().getUserById(new SomeInt() {
                                    @Override
                                    public Object callingBack(Object obj) {
                                        List list = (List<User>)obj;
                                        for (Object users:list){
                                            Log.d("dkgd", users.toString());
                                        }
                                        return null;
                                    }
                                });
            }
        });

        orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dataBase.getReference("Orders").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!snapshot.exists()){
                            if (ordersToast != null)
                                ordersToast.cancel();
                            ordersToast = Toast.makeText(getActivity(), "Нет заказов", Toast.LENGTH_SHORT);
                            ordersToast.show();
                        }else{
                            Intent intent = new Intent(getActivity(), OrdersActivity.class);
                            startActivity(intent);
                        }

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        adminList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dataBase.getReference("Admins").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!snapshot.exists()){
                            if (admToast != null)
                                admToast.cancel();
                            admToast = Toast.makeText(getActivity(), "Нет админов", Toast.LENGTH_SHORT);
                            admToast.show();
                        }else{
                            Intent intent = new Intent(getActivity(), AdminsListActivity.class);
                            startActivity(intent);
                        }

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        giveAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText input = new EditText(getActivity());
                AlertDialog alert = new AlertDialog.Builder(getActivity())
                        .setTitle("Введите id пользователя")
                        .setView(input) // Use our EditText
                        .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseDatabase.getInstance().getReference("Admins")
                                        .child(input.getText().toString())
                                        .setValue(input.getText().toString());
                            }
                        }).show();
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });


        comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dataBase.getReference("Users").child(mAuth.getCurrentUser().getUid())
                        .child("Comments").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!snapshot.exists()){
                            if (commToast != null)
                                commToast.cancel();
                            commToast = Toast.makeText(getActivity(), "Нет комментариев", Toast.LENGTH_SHORT);
                            commToast.show();
                        }else{
                            Intent intent = new Intent(getActivity(), MyCommentsActivity.class);
                            startActivity(intent);
                        }

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        creditCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser()
                        .getUid()).child("cardNumber");

                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {


                        if (!snapshot.exists()){
                            Intent intent = new Intent(getActivity(), AddCard.class);
                            startActivity(intent);
                        }else{

                            Intent intent = new Intent(getActivity(), MyCard.class);
                            startActivity(intent);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });




            }
        });



        pokupki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dataBase.getReference("Users").child(mAuth.getCurrentUser().getUid())
                        .child("Pokupki").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!snapshot.exists()){
                            if (pokupkiToast != null)
                                pokupkiToast.cancel();
                            pokupkiToast = Toast.makeText(getActivity(), "Нет покупок", Toast.LENGTH_SHORT);
                            pokupkiToast.show();
                        }else{
                            Intent intent = new Intent(getActivity(), PokupkiActivity.class);
                            startActivity(intent);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        return v;
    }
}