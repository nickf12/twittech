package models;

public class UserPersonalData extends BeanUser {

    private int age;
    private String gender;
    private String language;
    private String job;
    private String company;
    private String Description;
    private String image;

    public UserPersonalData() {super();}

    public UserPersonalData(int userId, String username, int age, String gender, String language, String job, String company, String description, String image) {
        this.userId = userId;
        this.user = username;
        this.age = age;
        this.gender = gender;
        this.language = language;
        this.job = job;
        this.company = company;
        Description = description;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
