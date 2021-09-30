package android.malakhov.appmarket.Data.Products;

import android.malakhov.appmarket.ICallback;
import android.widget.ImageView;

import java.util.List;

public interface IProductRepository {

    void getProductsSortedByDate(ICallback callback);
    void getProductsSortedByExpensive(ICallback callback);
    void getProductsSortedByCheap(ICallback callback);
    void getProductsSortedByRating(ICallback callback);
    void deleteProduct(Product product);
    void addProduct(ImageView[]images, Product product);
    void addToCorzina(Product product);
    void getCorzinaList(ICallback callback);
    void deleteFromCorzina(Product product);
    void minusAmount(Product product, int a);
    void setRating(Product product, int rating);
}
