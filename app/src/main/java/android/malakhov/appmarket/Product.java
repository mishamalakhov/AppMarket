package android.malakhov.appmarket;


import android.content.SharedPreferences;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Product implements Serializable {

    private String name;
    private int price;
    private String id;
    private boolean isInCorzina;
    private String type;
    private String CPU;
    private int OZU;
    private int FleshMemory;
    private int Capacity;
    private double diagonal;
    private int mainCamera;
    private int frontCamera;
    private int dateOfRelease;
    private List<String> photoURI = new ArrayList<>();
    private String color;
    private int amount;
    private long dateTime;
    private int allRating = 0;
    private int commentsCount = 0;
    private float averageRating = 0;

    public float getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(float averageRating) {
        this.averageRating = averageRating;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }

    public int getAllRating() {
        return allRating;
    }

    public void setAllRating(int allRating) {
        this.allRating = allRating;
    }

    public boolean compareWithFilter(SharedPreferences settings) {


        boolean a = true;

        if (!settings.getString("Type", "").equals(""))
            if (!settings.getString("Type", "").equals(this.type))
                return false;
        if (!settings.getString("FromOZU", "").equals(""))
            if (Integer.parseInt(settings.getString("FromOZU", "")) > this.OZU)
                return false;
        if (!settings.getString("ToOZU", "").equals(""))
            if (Integer.parseInt(settings.getString("ToOZU", "")) < this.OZU)
                return false;
        if (!settings.getString("FromFleash", "").equals(""))
            if (Integer.parseInt(settings.getString("FromFleash", "")) > this.FleshMemory)
                return false;
        if (!settings.getString("ToFlesh", "").equals(""))
            if (Integer.parseInt(settings.getString("ToFlesh", "")) < this.FleshMemory)
                return false;
        if (!settings.getString("FromCapacity", "").equals(""))
            if (Integer.parseInt(settings.getString("FromCapacity", "")) > this.Capacity)
                return false;
        if (!settings.getString("ToCapacity", "").equals(""))
            if (Integer.parseInt(settings.getString("ToCapacity", "")) < this.Capacity)
                return false;
        if (!settings.getString("FromDiagonal", "").equals(""))
            if (Integer.parseInt(settings.getString("FromDiagonal", "")) > this.diagonal)
                return false;
        if (!settings.getString("ToDiagonal", "").equals(""))
            if (Integer.parseInt(settings.getString("ToDiagonal", "")) < this.diagonal)
                return false;
        if (!settings.getString("FromMain", "").equals(""))
            if (Integer.parseInt(settings.getString("FromMain", "")) > this.mainCamera)
                return false;
        if (!settings.getString("ToMain", "").equals(""))
            if (Integer.parseInt(settings.getString("ToMain", "")) < this.mainCamera)
                return false;
        if (!settings.getString("FromFront", "").equals(""))
            if (Integer.parseInt(settings.getString("FromFront", "")) > this.frontCamera)
                return false;
        if (!settings.getString("ToFront", "").equals(""))
            if (Integer.parseInt(settings.getString("ToFront", "")) < this.frontCamera)
                return false;
        if (!settings.getString("FromYear", "").equals(""))
            if (Integer.parseInt(settings.getString("FromYear", "")) > this.dateOfRelease)
                return false;
        if (!settings.getString("ToYear", "").equals(""))
            if (Integer.parseInt(settings.getString("ToYear", "")) < this.dateOfRelease)
                return false;
        if (!settings.getString("FromPrice", "").equals(""))
            if (Integer.parseInt(settings.getString("FromPrice", "")) > this.price)
                return false;
        if (!settings.getString("ToPrice", "").equals(""))
            if (Integer.parseInt(settings.getString("ToPrice", "")) < this.price)
                return false;


        if (settings.getBoolean("Black", false)) {
            if (color.equals("Черный"))
                return true;
            else
                a = false;
        }  if (settings.getBoolean("white", false)) {
            if (color.equals("Белый"))
                return true;
            else
                a = false;
        }  if (settings.getBoolean("silver", false)) {
            if (color.equals("Серебристый"))
                return true;
            else
                a = false;
        }  if (settings.getBoolean("red", false)) {
            if (color.equals("Красный"))
                return true;
            else
                a = false;
        }  if (settings.getBoolean("purple", false)) {
            if (color.equals("Фиолетовый"))
                return true;
            else
                a = false;
        }  if (settings.getBoolean("pink", false)) {
            if (color.equals("Розовый"))
                return true;
            else
                a = false;
        }  if (settings.getBoolean("gold", false)) {
            if (color.equals("Золотой"))
                return true;
            else
                a = false;
        } if (settings.getBoolean("biruz", false)) {
                if (color.equals("Бирюзовый"))
                    return true;
                else
                    a = false;
        }  if (settings.getBoolean("yellow", false)) {
                if (color.equals("Желтый"))
                    return true;
                else
                    a = false;
        }  if (settings.getBoolean("green", false)) {
                if (color.equals("Зеленый"))
                    return true;
                else
                    a = false;
        }  if (settings.getBoolean("blue", false)) {
                if (color.equals("Синий"))
                    return true;
                else
                    a = false;
        }  if (settings.getBoolean("korall", false)) {
                if (color.equals("Коралловый"))
                    return true;
                else
                    a = false;
        }

            return a;
    }


    public Product() {
        id = UUID.randomUUID().toString();
        dateTime = new Date().getTime();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isInCorzina() {
        return isInCorzina;
    }

    public void setInCorzina(boolean inCorzina) {
        isInCorzina = inCorzina;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCPU() {
        return CPU;
    }

    public void setCPU(String CPU) {
        this.CPU = CPU;
    }

    public int getOZU() {
        return OZU;
    }

    public void setOZU(int OZU) {
        this.OZU = OZU;
    }

    public int getFleshMemory() {
        return FleshMemory;
    }

    public void setFleshMemory(int fleshMemory) {
        FleshMemory = fleshMemory;
    }

    public int getCapacity() {
        return Capacity;
    }

    public void setCapacity(int capacity) {
        Capacity = capacity;
    }

    public double getDiagonal() {
        return diagonal;
    }

    public void setDiagonal(double diagonal) {
        this.diagonal = diagonal;
    }

    public int getMainCamera() {
        return mainCamera;
    }

    public void setMainCamera(int mainCamera) {
        this.mainCamera = mainCamera;
    }

    public int getFrontCamera() {
        return frontCamera;
    }

    public void setFrontCamera(int frontCamera) {
        this.frontCamera = frontCamera;
    }

    public int getDateOfRelease() {
        return dateOfRelease;
    }

    public void setDateOfRelease(int dateOfRelease) {
        this.dateOfRelease = dateOfRelease;
    }

    public List<String> getPhotoURI() {
        return photoURI;
    }

    public void setPhotoURI(List<String> photoURI) {
        this.photoURI = photoURI;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }
}
