package android.malakhov.appmarket.Data.Orders;


import android.malakhov.appmarket.Data.FireBaseDB;
import android.malakhov.appmarket.ICallback;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class OrdersRepository implements IOrdersRepository{

    private static OrdersRepository obj;


    //Singletone
    public static OrdersRepository get(){
        if (obj == null)
            return new OrdersRepository();
        else
            return obj;
    }

    @Override
    public void getOrders(ICallback callback) {
        FireBaseDB.get().getDataBase().getReference("Orders").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Order> list = new ArrayList<>();
                for (DataSnapshot ds:snapshot.getChildren()){
                    Order order = ds.getValue(Order.class);
                    list.add(order);
                }
                callback.callingBack(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void deleteOrder(String id) {
        FireBaseDB.get().getDataBase().getReference("Orders").child(id).
                removeValue();
    }
}
