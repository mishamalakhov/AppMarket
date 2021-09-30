package android.malakhov.appmarket.Data.Products;


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
    private int year;
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

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
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
