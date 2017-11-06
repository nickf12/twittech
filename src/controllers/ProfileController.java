package controllers;

import models.BeanUser;
import models.UserPersonalData;
import utils.DAO;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;


@WebServlet("/Profile")
public class ProfileController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProfileController() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("ProfileController.");

        try {
            HttpSession session = request.getSession(false);
            BeanUser user = (BeanUser) session.getAttribute("user");
            request.setAttribute("user", user);

            UserPersonalData personalData = DAO.getDAO().getPersonalData(user.getUserId());
            request.getSession(false).setAttribute("personalData", personalData);

            ArrayList<UserPersonalData> follower = DAO.getDAO().getUserFollower(user.getUserId());
            request.setAttribute("follower", follower);

            ArrayList<UserPersonalData> following = DAO.getDAO().getUserFollowing(user.getUserId());
            request.setAttribute("following", following);

            RequestDispatcher dispatcher = request.getRequestDispatcher("ViewProfile.jsp");
            dispatcher.forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
// TODO Auto-generated method stub
        doGet(request, response);
    }

}