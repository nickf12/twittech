package controllers;


import models.BeanComment;
import models.BeanUser;
import utils.DAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

@WebServlet("/tweet")
public class TweetController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            BeanUser user = (BeanUser) req.getSession(false).getAttribute("user");
            String action = req.getParameter("action");
            switch (action) {
                case "remove":
                    if (user.getAdmin() == false && req.getParameter("action").equals("remove")) {
                        resp.sendError(405, "You are not admin user");
                        return;
                    } else {
                        DAO.getDAO().removeTweet(Integer.parseInt(req.getParameter("tweetId")));
                    }
                    break;
                case "add":
                    DAO.getDAO().insertTweet(user.getUserId(), (String) req.getParameter("message"));
                    break;
                case "comment":
                    DAO.getDAO().addComment(Integer.parseInt(req.getParameter("tweetId")),
                            (String) req.getParameter("comment"),
                            user.getUserId());
                    break;
                case "favorite":
                    DAO.getDAO().addFavorite(Integer.parseInt(req.getParameter("tweetId")), user.getUserId());
                    break;
                case "retweet":
                    DAO.getDAO().addRetweet(Integer.parseInt(req.getParameter("tweetId")), user.getUserId());
                    break;
                case "getComment":
                    ArrayList<BeanComment> beanComment = DAO.getDAO().getCommentsByTweet(Integer.parseInt(req.getParameter("tweetId")));
                    PrintWriter out = resp.getWriter();
                    if(beanComment.size()>0)
                    {
                        String json = "[";
                        for (BeanComment t : beanComment) {
                            json += t.toJSON().toJSONString() + ",";
                        }

                        json = json.substring(0, json.length() - 1) + "]";
                        out.print(json);
                    } else {
                        out.print("[]");
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
