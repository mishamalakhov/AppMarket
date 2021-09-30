package android.malakhov.appmarket;

import android.content.SharedPreferences;
import android.malakhov.appmarket.Data.Products.Product;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class CompareWithFilters {

    public static List<Product> compare(List<Product> productList, SharedPreferences settings) {

        if (settings == null)
            return productList;

        boolean a = true;
        List<Product> list = new ArrayList<>();
        HashMap<String, Object> map = (HashMap) settings.getAll();

        for (Product product: productList) {
            for (Map.Entry<String, Object> filter : map.entrySet()) {
                a = true;
                try {
                    int value = Integer.parseInt(filter.getValue().toString());
                    if (filter.getKey().contains("From"))
                        if (value > (int) Product.class.getMethod("get"+filter.getKey().replace("From","")).invoke(product)) {
                            a=false;break;}
                    if (filter.getKey().contains("To"))
                        if (value < (int) Product.class.getMethod("get" + filter.getKey().replace("To", "")).invoke(product)){
                            a=false;break;}

                } catch (Exception e) {
                    String value = filter.getKey();
                    try {
                        if (value.equals("Color")) {
                            for (String color: (HashSet<String>)filter.getValue()){
                                if (color.equals(product.getColor())) {a= true; break;} else a = false;
                            }
                        }else {
                            if (!filter.getValue().equals(product.getType())) {
                                a = false;
                                break;
                            }
                        }
                    } catch (Exception exception) {
                        System.out.println(exception.toString());
                    }
                }
            }
            if (a) list.add(product);
        }
        return list;
    }
}
