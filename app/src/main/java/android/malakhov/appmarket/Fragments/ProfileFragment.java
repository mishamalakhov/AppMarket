package android.malakhov.appmarket.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.malakhov.appmarket.Activities.AddCard;
import android.malakhov.appmarket.Activities.AdminsListActivity;
import android.malakhov.appmarket.Activities.MainActivity;
import android.malakhov.appmarket.Activities.MyCard;
import android.malakhov.appmarket.Activities.MyComments;
import android.malakhov.appmarket.Activities.OrdersActivity;
import android.malakhov.appmarket.Activities.PokupkiActivity;
import android.malakhov.appmarket.Data.Comments.CommentsRepository;
import android.malakhov.appmarket.Data.Orders.Order;
import android.malakhov.appmarket.Data.Orders.OrdersRepository;
import android.malakhov.appmarket.Data.Users.UserRepository;
import android.malakhov.appmarket.ICallback;
import android.malakhov.appmarket.R;
import android.malakhov.appmarket.Data.Users.User;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;


public class ProfileFragment extends Fragment {

    private TextView name, mail, admin, idOfUser;
    private CardView  pokupki, creditCard, comments, giveAdmin, adminList, orders;
    private Button exit;
    private User user;
    private String whoIsUser;
    private Toast pokupkiToast, commToast, ordersToast, admToast;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        init(v);

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

        UserRepository.get().getCurrentUser(new ICallback() {
            @Override
            public void callingBack(Object obj) {
                user = (User)obj;
                name.setText(user.getSecondName() + " " + user.getName());
                mail.setText(user.getEmail());
                idOfUser.setText(user.getId());
            }
        });

        orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OrdersRepository.get().getOrders(new ICallback() {
                    @Override
                    public void callingBack(Object obj) {
                        List<Order> list = new ArrayList<>();
                        list.addAll((List<Order>)obj);
                        if (list.size() == 0){
                            if (ordersToast != null)
                                ordersToast.cancel();
                            ordersToast = Toast.makeText(getActivity(), "Нет заказов", Toast.LENGTH_SHORT);
                            ordersToast.show();
                        }else{
                            Intent intent = new Intent(getActivity(), OrdersActivity.class);
                            OrdersActivity.list = list;
                            startActivity(intent);
                        }
                    }
                });
            }
        });

        adminList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserRepository.get().getAdminsList(new ICallback() {
                    @Override
                    public void callingBack(Object obj) {
                        List<User> list = new ArrayList<>();
                        list.addAll((List<User>)obj);
                        if (list.size() == 0){
                            if (admToast != null) admToast.cancel();
                            admToast = Toast.makeText(getActivity(), "Нет админов", Toast.LENGTH_SHORT);
                            admToast.show();
                        }else{
                            Intent intent = new Intent(getActivity(), AdminsListActivity.class);
                            AdminsListActivity.list = list;
                            startActivity(intent);
                        }
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
                                UserRepository.get().giveAdmin(input.getText().toString());
                            }
                        }).show();
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserRepository.get().exit();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });


        comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentsRepository.get().getUserComments(new ICallback() {
                    @Override
                    public void callingBack(Object obj) {
                        if (obj == null){
                            if (commToast != null)
                                commToast.cancel();
                            commToast = Toast.makeText(getActivity(), "Нет комментариев", Toast.LENGTH_SHORT);
                            commToast.show();
                        }else{
                            Intent intent = new Intent(getActivity(), MyComments.class);
                            startActivity(intent);
                        }

                    }
                });
            }
        });

        creditCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UserRepository.get().getCardNumber(new ICallback() {
                    @Override
                    public void callingBack(Object obj) {
                        if (obj == null){
                            Intent intent = new Intent(getActivity(), AddCard.class);
                            startActivity(intent);
                        }else{
                            Intent intent = new Intent(getActivity(), MyCard.class);
                            startActivity(intent);
                        }
                    }
                });
            }
        });



        pokupki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UserRepository.get().getUserPokupki(new ICallback() {
                    @Override
                    public void callingBack(Object obj) {
                        if (obj == null){
                            if (pokupkiToast != null)
                                pokupkiToast.cancel();
                            pokupkiToast = Toast.makeText(getActivity(), "Нет покупок", Toast.LENGTH_SHORT);
                            pokupkiToast.show();
                        }else{
                            Intent intent = new Intent(getActivity(), PokupkiActivity.class);
                            startActivity(intent);
                        }
                    }
                });
            }
        });
        return v;
    }

    private void init(View v){
        admin = v.findViewById(R.id.admin);
        name = v.findViewById(R.id.name);
        mail = v.findViewById(R.id.mail);
        pokupki = v.findViewById(R.id.pokupki);
        creditCard = v.findViewById(R.id.credit_card);
        comments = v.findViewById(R.id.comments);
        exit = v.findViewById(R.id.exit);
        giveAdmin = v.findViewById(R.id.giveAdmin);
        adminList = v.findViewById(R.id.adminList);
        orders = v.findViewById(R.id.orders);
        whoIsUser = getArguments().getString("whoIsUser");
        idOfUser = v.findViewById(R.id.idOfUser);
    }

    public static ProfileFragment newInstance(String str){
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString("whoIsUser", str);
        fragment.setArguments(args);
        return fragment;
    }
}