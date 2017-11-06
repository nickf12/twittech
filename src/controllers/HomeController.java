package controllers;

import models.BeanTweet;
import models.BeanUser;
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

@WebServlet("/home")
public class HomeController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            if (request.getParameter("next") == null) {
                request.setAttribute("users",DAO.getDAO().getPopularUsers());
                RequestDispatcher dispatcher = request.getRequestDispatcher("ViewHome.jsp");
                dispatcher.forward(request, response);
            } else {
                request.setAttribute("users",DAO.getDAO().getPopularUsers());

                String next = request.getParameter("next");
                if ((BeanUser) request.getSession(false).getAttribute("user") != null) {
                    BeanUser user = (BeanUser) request.getSession(false).getAttribute("user");
                    ArrayList<BeanTweet> tweets;
                    if (!user.getAdmin()) {
                        tweets = DAO.getDAO().getTweetsForUserId(user.getUserId(), Integer.parseInt(next), 5);
                    } else {
                        tweets = DAO.getDAO().paginateTweets(Integer.parseInt(next), 5);
                    }

                    if (tweets.size() > 0) {
                        String json = "[";
                        for (BeanTweet t : tweets) {
                            json += t.toJSON().toJSONString() + ",";
                        }
                        json = json.substring(0, json.length() - 1) + "]";
                        PrintWriter out = response.getWriter();
                        out.print(json);
                    }

                } else {
                    ArrayList<BeanTweet> tweets = DAO.getDAO().paginateTweets(Integer.parseInt(next), 5);
                    if (tweets.size() > 0) {
                        String json = "[";
                        for (BeanTweet t : tweets) {
                            json += t.toJSON().toJSONString() + ",";
                        }
                        json = json.substring(0, json.length() - 1) + "]";
                        PrintWriter out = response.getWriter();
                        out.print(json);
                    }
                }
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
