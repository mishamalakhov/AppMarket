package android.malakhov.appmarket.Data.Orders;

import android.malakhov.appmarket.ICallback;

public interface IOrdersRepository {

    void getOrders(ICallback callback);
    void deleteOrder(String id);
}
