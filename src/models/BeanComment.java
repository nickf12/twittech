package models;

import org.json.simple.JSONObject;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class BeanComment {
    int tweetId;
    String comment;
    int userId;
    Timestamp time;
    UserPersonalData user;

    public UserPersonalData getUser() {
        return user;
    }

    public void setUser(UserPersonalData user) {
        this.user = user;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public BeanComment(int tweetId, String comment, int userId, Timestamp time) {
        this.tweetId = tweetId;
        this.comment = comment;
        this.userId = userId;
        this.time = time;
    }

    public int getTweetId() {
        return tweetId;
    }

    public void setTweetId(int tweetId) {
        this.tweetId = tweetId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public JSONObject toJSON() {
        JSONObject res = new JSONObject();
        res.put("tweetId", this.getTweetId());
        res.put("comment", this.getComment());
        res.put("userId", this.getUserId());
        DateFormat df = new SimpleDateFormat("dd/MM/YYYY HH:ss");
        res.put("date", df.format(this.getTime()));
        res.put("username", this.getUser().getUser());
        res.put("image", this.getUser().getImage());
        return res;
    }
}