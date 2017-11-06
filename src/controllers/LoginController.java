package controllers;

import models.BeanLogin;
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
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

/**
 * Servlet implementation class LoginController
 */
@WebServlet("/LoginController")
public class LoginController extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		System.out.println("LoginController.");
		BeanLogin login = null;
		try {
			login = (BeanLogin)request.getSession(false).getAttribute("login");
			login.checkError();
			RequestDispatcher dispatcher = request.getRequestDispatcher("ViewProfile.jsp");
			dispatcher.forward(request, response);
            return;
		} catch(NullPointerException e) {
			//Still not logged
			login = new BeanLogin();
		}

	    try {
	    	BeanUtils.populate(login, request.getParameterMap());

	    	if (login.checkError()) {
		    	HttpSession session = request.getSession();
				BeanUser user = DAO.getDAO().getUser(login.getUser());
		    	session.setAttribute("user", user);
				UserPersonalData pd = DAO.getDAO().getPersonalData(user.getUserId());
				session.setAttribute("personalData", pd);
				response.setHeader("Set-Cookie", session.getId());
				if(pd != null) {
					ArrayList<UserPersonalData> follower = DAO.getDAO().getUserFollower(user.getUserId());
					request.setAttribute("follower",follower);
					ArrayList<UserPersonalData> following = DAO.getDAO().getUserFollowing(user.getUserId());
					request.setAttribute("following", following);
					RequestDispatcher dispatcher = request.getRequestDispatcher("ViewProfile.jsp");
					dispatcher.forward(request, response);
				} else {
					RequestDispatcher dispatcher = request.getRequestDispatcher("ViewPersonalData.jsp");
					dispatcher.forward(request, response);
				}

		    }
			else {
			    request.setAttribute("login",login);
			    RequestDispatcher dispatcher = request.getRequestDispatcher("ViewLoginForm.jsp");
			    dispatcher.forward(request, response);
		    }
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(Exception e) {
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
