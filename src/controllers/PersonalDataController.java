package controllers;

import models.BeanUser;
import models.UserPersonalData;
import org.apache.commons.beanutils.BeanUtils;
import utils.DAO;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

@WebServlet("/PersonalData")
public class PersonalDataController extends HttpServlet {

    public PersonalDataController() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BeanUser user = new BeanUser();
        try {
            request.getParameter("action").equals("edit");
            user = (BeanUser) request.getSession(false).getAttribute("user");
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("user", user.getUser());
            map.put("mail", user.getMail());
            map.put("userId", user.getUserId());
            if(request.getParameter("age") != null)
                map.put("age", Integer.parseInt(request.getParameter("age")));
            if(request.getParameter("gender") != null && !request.getParameter("gender").equals("null"))
                map.put("gender", (String) request.getParameter("gender"));
            if(request.getParameter("language") != null && !request.getParameter("language").equals("null"))
                map.put("language", (String) request.getParameter("language"));
            if(request.getParameter("job") != null && !request.getParameter("job").equals("null"))
                map.put("job", (String) request.getParameter("job"));
            if(request.getParameter("company") != null && !request.getParameter("company").equals("null"))
                map.put("company", (String) request.getParameter("company"));
            if(request.getParameter("description") != null && !request.getParameter("description").equals("null"))
                map.put("description", (String) request.getParameter("description"));
            if(request.getParameter("image") != null && !request.getParameter("image").equals("null"))
                map.put("image", (String) request.getParameter("image"));

            UserPersonalData personalData = new UserPersonalData();
            BeanUtils.populate(personalData, map);

            if(request.getSession(false).getAttribute("personalData") != null) {
                DAO.getDAO().updatePersonalData(personalData);
            } else {
                DAO.getDAO().insertPersonalData(user.getUserId(), personalData);
            }

            request.getSession().setAttribute("personalData", DAO.getDAO().getPersonalData(user.getUserId()));

            ArrayList<UserPersonalData> follower = DAO.getDAO().getUserFollower(user.getUserId());
            request.setAttribute("follower",follower);
            ArrayList<UserPersonalData> following = DAO.getDAO().getUserFollowing(user.getUserId());
            request.setAttribute("following", following);

            /*This doesn't work with AJAX POST*/
            RequestDispatcher dispatcher = request.getRequestDispatcher("ViewProfile.jsp");
            dispatcher.forward(request, response);

            /*So*/
            /*response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"redirect\": \"ViewProfile.jsp\"}");*/

        } catch (Exception e) {
            e.printStackTrace();
            RequestDispatcher dispatcher = request.getRequestDispatcher("ViewPersonalData.jsp");
            dispatcher.forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
