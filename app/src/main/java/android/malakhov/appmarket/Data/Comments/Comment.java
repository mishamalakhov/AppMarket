package android.malakhov.appmarket.Data.Comments;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Comment implements Serializable {

    private String userName;
    private int rating;
    private long date;
    private String commentText;
    private String advantageText;
    private String disadvantageText;
    private List<String> photoURI = new ArrayList<>();
    private String id;
    private String userID;
    private String title;
    private String productName;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Comment() {
        id = UUID.randomUUID().toString();
        date = new Date().getTime();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public String getAdvantageText() {
        return advantageText;
    }

    public void setAdvantageText(String advantageText) {
        this.advantageText = advantageText;
    }

    public String getDisadvantageText() {
        return disadvantageText;
    }

    public void setDisadvantageText(String disadvantageText) {
        this.disadvantageText = disadvantageText;
    }

    public List<String> getPhotoURI() {
        return photoURI;
    }

    public void setPhotoURI(List<String> photoURI) {
        this.photoURI = photoURI;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
