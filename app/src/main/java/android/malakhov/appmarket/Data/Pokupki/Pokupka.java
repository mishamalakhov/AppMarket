package android.malakhov.appmarket.Data.Pokupki;

import android.malakhov.appmarket.Data.Products.Product;

import java.util.Date;

public class Pokupka {

    private long date;
    private String id = "fdfd";
    private int amount;
    private Product product;
    private int price;


    public Pokupka() {

        date = new Date().getTime();
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
