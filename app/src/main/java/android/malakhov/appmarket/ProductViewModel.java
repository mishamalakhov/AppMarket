package android.malakhov.appmarket;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import java.util.List;

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
