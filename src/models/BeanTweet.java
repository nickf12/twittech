package models;

import org.json.simple.JSONObject;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class BeanTweet {

    private int id;
    private int userId;
    private String username;
    private String message;
    private String image;
    private Timestamp date;

    public BeanTweet(int userId, String username, String message, String image) {
        this.userId = userId;
        this.username = username;
        this.message = message;
        this.image = image;
    }

    public BeanTweet(int id, int userId, String username, String message, String image) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.message = message;
        this.image = image;
    }

    public BeanTweet(int id, int userId, String username, String message, String image, Timestamp date) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.message = message;
        this.image = image;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public JSONObject toJSON() {
        JSONObject res = new JSONObject();
        res.put("id", this.getId());
        res.put("userId", this.getUserId());
        res.put("username", this.getUsername());
        res.put("message", this.getMessage());
        res.put("image", this.getImage());
        DateFormat df = new SimpleDateFormat("dd/MM/YYYY HH:ss");
        res.put("date", df.format(this.getDate()));
        return res;
    }
}
