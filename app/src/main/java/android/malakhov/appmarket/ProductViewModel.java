package android.malakhov.appmarket;

import android.malakhov.appmarket.Data.Products.Product;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class ProductViewModel extends BaseObservable {


    private Product product;

    public ProductViewModel(Product product) {
        this.product = product;
    }

    @Bindable
    public String getName() {
        return product.getName();
    }

    public int getPrice(){
        return product.getPrice();
    }

    public boolean isInCorzina(){
        return product.isInCorzina();
    }

    public int getFleshMemmory(){
        return product.getFleshMemory();
    }


}
