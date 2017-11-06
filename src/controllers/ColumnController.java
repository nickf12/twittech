package controllers;

import models.UserPersonalData;
import utils.DAO;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet("/column")
public class ColumnController extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            if(req.getParameter("content") != null) {
                String content = req.getParameter("content");
                switch(content) {
                    case "popularUsers":
                        ArrayList<UserPersonalData> users = new ArrayList<>();
                        if(req.getParameter("last") != null) {
                            int last = Integer.parseInt(req.getParameter("last"));
                            users.addAll(DAO.getDAO().getPopularUsers(last));
                        } else {
                            users = DAO.getDAO().getPopularUsers();
                        }

                        req.setAttribute("users", users);
                        RequestDispatcher dispatcher = req.getRequestDispatcher("popularUsers.jsp");
                        dispatcher.forward(req,resp);
                        break;
                }
            } else {

            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
