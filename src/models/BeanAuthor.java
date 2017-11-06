package models;


import utils.DAO;

import java.util.ArrayList;

public class BeanAuthor {

    int id;
    String name;
    String image;
    int age;
    String gender;
    String language;
    String job;
    String company;
    String Description;

    public BeanAuthor(int id, String name, String image, int age, String gender, String language, String job, String company, String description) {
        Description = description;
        this.id = id;
        this.name = name;
        this.image = image;
        this.age = age;
        this.gender = gender;
        this.language = language;
        this.job = job;
        this.company = company;
    }

    public BeanAuthor(int authorId) {
        this.id = authorId;
        BeanAuthor author = DAO.getDAO().getAuthorById(authorId);
        this.name = author.getName();
        this.image = author.getImage();

    }

    public BeanAuthor(int authorId, String authorName, String authorImage) {
        this.id = authorId;
        this.name = authorName;
        this.image = authorImage;
    }

    public ArrayList<BeanTweet> getTweets(int next, int length) {
        ArrayList<BeanTweet> tweets = null;
        try {
            tweets = DAO.getDAO().getTweetsByAuthor(this, next, length);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tweets;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public String getLanguage() {
        return language;
    }
    public void setLanguage(String language) {
        this.language = language;
    }
    public String getJob() {
        return job;
    }
    public void setJob(String job) {
        this.job = job;
    }
    public String getCompany() {
        return company;
    }
    public void setCompany(String company) {
        this.company = company;
    }
    public String getDescription() {
        return Description;
    }
    public void setDescription(String description) {
        Description = description;
    }

}
