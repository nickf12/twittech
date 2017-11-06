package controllers;

import models.BeanUser;
import utils.DAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet implementation class UserFollowerController
 */
@WebServlet("/UserFollowerController")
public class UserFollowerController extends HttpServlet {

    public UserFollowerController() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("UserFollowerController.");
        try {

            BeanUser bUser = (BeanUser) request.getSession(false).getAttribute("user");
            int userSorgId = bUser.getUserId();
            int userDestId = Integer.parseInt(request.getParameter("user_id"));
            if(request.getParameter("action") != null && request.getParameter("action").equals("unfollow")) {
                DAO.getDAO().unfollow(userSorgId, userDestId);
            } else {
                DAO.getDAO().insertUserFollower(userSorgId, userDestId);
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
