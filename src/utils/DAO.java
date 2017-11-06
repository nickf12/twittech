package utils;

import models.*;

import java.sql.*;
import java.util.ArrayList;

public class DAO {

    private Connection connection;

    private static utils.DAO dao = null;

    public static utils.DAO getDAO() {
        if (dao == null) {
            try {
                dao = new utils.DAO();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return dao;
    }

    private DAO() throws Exception {
        String user = "mysql";
        String password = "prac";
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        connection = DriverManager.getConnection("jdbc:mysql://localhost/lab2?user=" + user + "&password=" + password);
    }

    //Manage database
    public ResultSet executeSQL(String query) throws SQLException {
        return connection.createStatement().executeQuery(query);
    }

    public int update(String query) throws SQLException {
        return connection.createStatement().executeUpdate(query);
    }

    public int insert(String table, String... args) {
        String query = "INSERT INTO " + table + "(";
        String values = " VALUES(";
        try {
            ResultSet rc = executeSQL("SELECT * FROM " + table);
            ResultSetMetaData mrc = rc.getMetaData();
            for (int i = 1; i <= mrc.getColumnCount(); i++) {
                query += mrc.getColumnName(i);
                if (mrc.getColumnType(i) == Types.NVARCHAR ||
                        mrc.getColumnType(i) == Types.LONGNVARCHAR) {
                    values += "\'" + args[i - 1] + "\',";
                } else if (mrc.getColumnType(i) == Types.INTEGER) {
                    values += args[i - 1] + ",";
                }
            }
            values = values.substring(0, values.length() - 1);
            query += values + ");";
            System.out.println(query);
            return update(query);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    public void disconnectBD() throws SQLException {
        connection.close();
    }

    //User functions
    public void insertUser(BeanUser user, String password) {
        try {
            update("INSERT INTO APP_USER(USERNAME,EMAIL,PASSWORD) VALUES(" +
                    "\'" + user.getUser() + "\'," +
                    "\'" + user.getMail() + "\'," +
                    "\'" + password + "\')");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public BeanUser getUser(String username) {
        try {
            ResultSet res = this.executeSQL("SELECT * FROM APP_USER WHERE USERNAME=\'" + username + "\'");
            if (res.next()) {
                BeanUser user = new BeanUser();
                user.setUserId(res.getInt("id"));
                user.setUser(res.getString("username"));
                user.setAdmin(res.getBoolean("is_admin"));
                return user;
            }
            res.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<UserPersonalData> getPopularUsers() {
        ArrayList<UserPersonalData> result = new ArrayList<UserPersonalData>();
        try {
            ResultSet res = executeSQL("SELECT count(f.user_id_dest) as followers, u.id, u.username, pd.image " +
                    "FROM app_user u " +
                    "LEFT JOIN follower f ON u.id=f.user_id_dest " +
                    "LEFT JOIN personal_data pd ON pd.user_id=u.id " +
                    "GROUP BY u.id ORDER BY followers DESC LIMIT 5");
            while (res.next()) {
                result.add(new UserPersonalData(res.getInt(2), res.getString(3), 0, null, null, null, null, null, res.getString(4)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    public ArrayList<UserPersonalData> getPopularUsers(int last) {
        ArrayList<UserPersonalData> result = new ArrayList<UserPersonalData>();
        try {
            ResultSet res = executeSQL("SELECT count(f.user_id_dest) as followers, u.id, u.username, pd.image " +
                    "FROM app_user u " +
                    "LEFT JOIN follower f ON u.id=f.user_id_dest " +
                    "LEFT JOIN personal_data pd ON pd.user_id=u.id " +
                    "GROUP BY u.id ORDER BY followers DESC LIMIT " + (last+5));
            while (res.next()) {
                result.add(new UserPersonalData(res.getInt(2), res.getString(3), 0, null, null, null, null, null, res.getString(4)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    //Personal data functions
    public void insertPersonalData(int userId, UserPersonalData pd) {
        try {
            update("INSERT INTO PERSONAL_DATA(USER_ID,AGE,GENDER,LANGUAGE,JOB,COMPANY,DESCRIPTION,IMAGE) VALUES (" +
                    +userId + "," +
                    +pd.getAge() + "," +
                    "\'" + pd.getGender() + "\'," +
                    "\'" + pd.getLanguage() + "\'," +
                    "\'" + pd.getJob() + "\'," +
                    "\'" + pd.getCompany() + "\'," +
                    "\'" + pd.getDescription() + "\'," +
                    "\'" + pd.getImage() + "\'" +
                    ")");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void updatePersonalData(UserPersonalData pd) {
        try {
            String query = "UPDATE personal_data SET ";
            if (pd.getAge() != 0) {
                query += "age=" + pd.getAge() + ",";
            }
            if (pd.getGender() != null) {
                query += " gender=\'" + pd.getGender() + "\',";
            }
            if (pd.getLanguage() != null) {
                query += " language=\'" + pd.getLanguage() + "\',";
            }
            if (pd.getJob() != null) {
                query += " job=\'" + pd.getJob() + "\',";
            }
            if (pd.getCompany() != null) {
                query += " company=\'" + pd.getCompany() + "\',";
            }
            if (pd.getImage() != null) {
                query += " image=\'" + pd.getImage() + "\',";
            }
            if (pd.getDescription() != null) {
                query += " description=\'" + pd.getDescription() + "\',";
            }
            query = query.substring(0,query.length()-1);
            query += " WHERE user_id=" + pd.getUserId();

            update(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public UserPersonalData getPersonalData(int userId) {
        try {
            ResultSet res = executeSQL("SELECT * FROM personal_data pd RIGHT JOIN app_user u ON u.id=pd.user_id WHERE USER_ID=" + userId);
            if (res.next()) {
                UserPersonalData pd = new UserPersonalData();
                pd.setUserId(res.getInt("user_id"));
                pd.setUser(res.getString("username"));
                pd.setAge(res.getInt("age"));
                pd.setGender(res.getString("gender"));
                pd.setLanguage(res.getString("language"));
                pd.setJob(res.getString("job"));
                pd.setCompany(res.getString("company"));
                pd.setDescription(res.getString("description"));
                pd.setImage(res.getString("image"));
                return pd;
            }
            res.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //Author functions
    public BeanAuthor getAuthorById(int authorId) {
        BeanAuthor author = null;
        try {
            ResultSet res = this.executeSQL("SELECT user.id, username, image, age, gender, language, job, company, description FROM app_user user LEFT JOIN personal_data pd on user.id=pd.user_id where user.id=" + authorId);
            if (res.next()) {
                author = new BeanAuthor(res.getInt(1), res.getString(2), res.getString(3), res.getInt(4), res.getString(5), res.getString(6), res.getString(7), res.getString(8), res.getString(9));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return author;
    }

    //Register and login checks
    public boolean checkUser(String username) {
        try {
            ResultSet res = executeSQL("SELECT username FROM app_user WHERE username=\'" + username + "\'");
            if (res.next())
                return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean checkPassword(String username, String password) {
        try {
            ResultSet res = executeSQL("SELECT password FROM app_user WHERE username=\'" + username + "\'");
            if (res.next()) {
                if (password.equals(res.getString("password"))) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean checkMail(String mail) {
        try {
            ResultSet res = executeSQL("SELECT email FROM app_user WHERE EMAIL=\'" + mail + "\'");
            if (res.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //Tweet functions
    public void insertTweet(int userId, String message) {
        try {
            update("INSERT INTO tweet(user_id, message, date) VALUES(" + userId + ",\'" + message + "\',NOW())");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<BeanTweet> getTweets() {
        ArrayList<BeanTweet> tweets = null;
        try {
            ResultSet res = executeSQL("SELECT TWEET.id as ID, USER.id, TWEET.message, USER.username, PD.image " +
                    "FROM tweet TWEET " +
                    "JOIN app_user USER ON TWEET.user_id=USER.id " +
                    "JOIN personal_data PD ON PD.user_id=USER.id " +
                    "ORDER BY TWEET.date desc");
            while (res.next()) {
                tweets.add(new BeanTweet(res.getInt(1), res.getInt(2), res.getString(3), res.getString(4), res.getString(5)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tweets;
    }

    public ArrayList<BeanTweet> paginateTweets(int next, int length) {
        ArrayList<BeanTweet> tweets = new ArrayList<>();
        try {
            ResultSet res = executeSQL("SELECT DISTINCT TWEET.id,USER.id,USER.username,TWEET.message,PD.image, TWEET.date " +
                    "FROM tweet TWEET " +
                    "JOIN app_user USER ON TWEET.user_id=USER.id " +
                    "JOIN personal_data PD ON PD.user_id=USER.id " +
                    "ORDER BY TWEET.date desc " +
                    "LIMIT " + length + " OFFSET " + next);
            while (res.next()) {
                tweets.add(new BeanTweet(res.getInt(1), res.getInt(2), res.getString(3), res.getString(4), res.getString(5), res.getTimestamp(6)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tweets;
    }

    public ArrayList<BeanTweet> getTweetsByAuthor(BeanAuthor author, int next, int length) {
        ArrayList<BeanTweet> result = new ArrayList<>();
        try {
            ResultSet res =
                    this.executeSQL("SELECT tweet.id, user_id, message, date " +
                            "FROM tweet " +
                            "WHERE tweet.user_id=\'" + author.getId() + "\' " +
                            "ORDER BY tweet.date DESC " +
                            "LIMIT " + length + " OFFSET " + next);
            while (res.next()) {
                result.add(new BeanTweet(res.getInt(1), res.getInt(2), author.getName(), res.getString(3), author.getImage(), res.getTimestamp(4)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<BeanTweet> getTweetsForUserId(int userId, int next, int length) {
        ArrayList<BeanTweet> result = new ArrayList<>();
        try {
            String query = "SELECT DISTINCT T.id, T.user_id, USER.username, T.message, PD.image, T.date " +
                    "FROM tweet T " +
                    "JOIN app_user USER ON T.user_id=USER.id " +
                    "LEFT JOIN follower F ON F.user_id_dest=T.user_id " +
                    "LEFT JOIN retweet R ON R.tweet_id=T.id " +
                    "LEFT JOIN personal_data PD ON USER.id=PD.user_id " +
                    "WHERE F.user_id_orig=" + userId + " OR T.user_id=" + userId + " " +
                    "ORDER BY T.date desc " +
                    "LIMIT " + length;
            if (next != 0) {
                query += " OFFSET " + next;
            }
            ResultSet res = executeSQL(query);
            while (res.next()) {
                result.add(new BeanTweet(res.getInt(1), res.getInt(2), res.getString(3), res.getString(4), res.getString(5), res.getTimestamp(6)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    //Follower functions
    public void insertUserFollower(int userId_orig, int userId_dest) {
        try {
            update("INSERT INTO follower(user_id_orig, user_id_dest) VALUES (" +
                    +userId_orig + "," +
                    +userId_dest + ")");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void unfollow(int userIdOrig, int userIdDest) {
        try {
            update("DELETE FROM FOLLOWER WHERE user_id_orig=" + userIdOrig + " AND user_id_dest=" + userIdDest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<UserPersonalData> getUserFollowing(int userId) {
        ArrayList<UserPersonalData> result = new ArrayList<>();
        try {
            ResultSet res = executeSQL("SELECT USER.id, USER.username, PD.image FROM FOLLOWER F " +
                    "JOIN APP_USER USER ON USER.id=F.user_id_dest " +
                    "JOIN PERSONAL_DATA PD ON PD.user_id=F.user_id_dest " +
                    "WHERE F.user_id_orig=" + userId);
            while (res.next()) {
                result.add(new UserPersonalData(res.getInt(1), res.getString(2), -1, null, null, null, null, null, res.getString(3)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<UserPersonalData> getUserFollower(int userId) {
        ArrayList<UserPersonalData> result = new ArrayList<>();
        try {
            ResultSet res = executeSQL("SELECT USER.id, USER.username, PD.image FROM FOLLOWER F " +
                    "JOIN APP_USER USER ON USER.id=F.user_id_orig " +
                    "JOIN PERSONAL_DATA PD ON PD.user_id=F.user_id_orig " +
                    "WHERE F.user_id_dest=" + userId);
            while (res.next()) {
                result.add(new UserPersonalData(res.getInt(1), res.getString(2), -1, null, null, null, null, null, res.getString(3)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    //Comment functions
    public void addComment(int tweetId, String comment, int userId) {
        try {
            this.update("INSERT INTO comment(tweet_id, comment, user_id, time) " +
                    "VALUES(" + tweetId + ",\'" + comment + "\',"+userId+", now())");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public ArrayList<BeanComment> getCommentsByTweet(int tweetId) {
        ArrayList<BeanComment> result = new ArrayList<BeanComment>();
        try {
            ResultSet res = this.executeSQL("SELECT tweet_id, comment, user_id, time FROM comment WHERE tweet_id=" + tweetId + " ORDER BY time DESC");
            while (res.next()) {
                BeanComment userComment = new BeanComment(res.getInt(1), res.getString(2), res.getInt(3), res.getTimestamp(4));
                userComment.setUser(getPersonalData(res.getInt(3)));
                result.add(userComment);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    //Favorite functions
    public void addFavorite(int tweetId, int userId) {
        try {
            this.update("INSERT INTO favorite VALUES(" + tweetId + "," + userId + ")");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getFavoritesByTweet(int tweetId) {
        int count = 0;
        try {
            ResultSet res = this.executeSQL("SELECT COUNT(*) FROM favorite WHERE tweet_id=" + tweetId);
            if (res.next()) {
                count = res.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public ArrayList<BeanTweet> getFavoritesByUser(int userId) {
        ArrayList<BeanTweet> result = new ArrayList<BeanTweet>();
        try {
            ResultSet res = this.executeSQL("SELECT tweet.id, tweet.user_id, username, message, image " +
                    "FROM favorite f JOIN tweet ON f.tweet_id=tweet.id JOIN app_user user on tweet.user_id=user.id JOIN personal_data pd on pd.user_id=tweet.user_id " +
                    "WHERE f.user_id=" + userId);
            while (res.next()) {
                result.add(new BeanTweet(res.getInt(1), res.getInt(2), res.getString(3), res.getString(4), res.getString(5)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    //Retweet functions
    public void addRetweet(int tweetId, int userId) {
        try {
            this.update("INSERT INTO retweet VALUES(" + tweetId + "," + userId + ")");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getRetweetsByTweet(int tweetId) {
        int count = 0;
        try {
            ResultSet res = this.executeSQL("SELECT COUNT(*) FROM retweet WHERE tweet_id=" + tweetId);
            if (res.next()) {
                count = res.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public ArrayList<BeanTweet> getRetweetsByUser(int userId) {
        ArrayList<BeanTweet> result = new ArrayList<BeanTweet>();
        try {
            ResultSet res = this.executeSQL(("SELECT tweet.id, tweet.user_id, username, message, image " +
                    "FROM retweet r JOIN tweet ON r.tweet_id=tweet.id JOIN app_user user on tweet.user_id=user.id JOIN personal_data pd on pd.user_id=tweet.user_id " +
                    "WHERE r.user_id=" + userId));
            while (res.next()) {
                result.add(new BeanTweet(res.getInt(1), res.getInt(2), res.getString(3), res.getString(4), res.getString(5)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    //Experimental video-calling
    public void insertWsId(int userId, String wsId) {
        try {
            this.update("INSERT INTO USER_WS_ID VALUES(" + userId + ",\'" + wsId + "\')");
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                update("UPDATE user_ws_id SET ws_id=\'"+wsId+"\' WHERE user_id=" + userId);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }

    //Admin functions
    public void removeTweet(int tweetId) {
        try {
            update("DELETE FROM retweet WHERE tweet_id="+tweetId);
            update("DELETE FROM favorite WHERE tweet_id="+tweetId);
            update("DELETE FROM comment WHERE tweet_id="+tweetId);
            update("DELETE FROM tweet WHERE id=" + tweetId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeUser(int userId, boolean removeTweets) {
        try {
            //instead of removing the user account, update the password
            update("UPDATE app_user SET password=MD5(NOW()) WHERE id=" + userId);
            if(removeTweets) {
                update("DELETE FROM retweet WHERE tweet_id IN (SELECT id FROM tweet WHERE user_id="+userId+")");
                update("DELETE FROM favorite WHERE tweet_id IN (SELECT id FROM tweet WHERE user_id="+userId+")");
                update("DELETE FROM comment WHERE tweet_id IN (SELECT id FROM tweet WHERE user_id="+userId+")");
                update("DELETE FROM tweet WHERE user_id="+userId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //Websocket
    public String getSessionByJSPSession(int userId) {
        String result = "";
        try {
            ResultSet res = executeSQL("SELECT ws_id FROM user_ws_id WHERE user_id=" + userId);
            if(res.next()) {
                return res.getString(1);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public void removeWsSession(String wsId) {
        try {
            update("DELETE FROM user_ws_id WHERE ws_id=\'"+ wsId+ "\'");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}
