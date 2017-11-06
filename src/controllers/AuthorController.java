package controllers;


import models.BeanAuthor;
import models.BeanTweet;
import models.BeanUser;
import models.UserPersonalData;
import utils.DAO;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

@WebServlet("/Author")
public class AuthorController extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {

            if(req.getParameter("userId") != null && ((BeanUser)req.getSession(false).getAttribute("user")).getAdmin()) {
                DAO.getDAO().removeUser(Integer.parseInt(req.getParameter("userId")),
                        Boolean.parseBoolean(req.getParameter("removeTweets")));
                return;
            }
            int authorId = Integer.parseInt(req.getParameter("authorId"));
            ArrayList<UserPersonalData> following = DAO.getDAO().getUserFollowing(authorId);
            req.setAttribute("following", following);

            ArrayList<UserPersonalData> follower = DAO.getDAO().getUserFollower(authorId);
            req.setAttribute("follower", follower);

            if(req.getSession(false).getAttribute("user") != null) {
                BeanUser user = (BeanUser)req.getSession(false).getAttribute("user");
                req.setAttribute("isFollow", isFollow(user.getUserId(), follower));
            }

            BeanAuthor author = DAO.getDAO().getAuthorById(authorId);
            req.setAttribute("author", author);
            if(req.getParameter("next") == null) {
                RequestDispatcher dispatcher = req.getRequestDispatcher("author.jsp");
                dispatcher.forward(req, resp);
            } else {
                int next = Integer.parseInt(req.getParameter("next"));
                ArrayList<BeanTweet> tweets = author.getTweets(next, 5);
                if (tweets.size() > 0) {
                    String json = "[";
                    for(BeanTweet tweet : tweets) {
                        json += tweet.toJSON() + ",";
                    }
                    json = json.substring(0, json.length()-1) + "]";
                    PrintWriter out = resp.getWriter();
                    out.print(json);
                }

            }

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    private boolean isFollow(int userId, ArrayList<UserPersonalData> followers) {
        for(UserPersonalData follower : followers) {
            if(follower.getUserId() == userId) {
                return true;
            }
        }
        return false;
    }
}
