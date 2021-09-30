package android.malakhov.appmarket.Data.Orders;

import android.malakhov.appmarket.Data.Pokupki.Pokupka;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class Order implements Serializable {

    private String userNumber;
    private String adress;
    private Pokupka mPokupka;
    private int amount;
    private int itogo;
    private String id;
    private long dateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }

    public Order() {
        id = UUID.randomUUID().toString();
        dateTime = new Date().getTime();
    }

    public int getItogo() {
        return itogo;
    }

    public void setItogo(int itogo) {
        this.itogo = itogo;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public Pokupka getPokupka() {
        return mPokupka;
    }

    public void setPokupka(Pokupka pokupka) {
        mPokupka = pokupka;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
