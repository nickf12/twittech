package controllers;

import models.BeanUser;
import org.apache.commons.beanutils.BeanUtils;
import utils.DAO;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * Servlet implementation class FormController
 */
@WebServlet("/RegisterController")
public class RegisterController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegisterController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	   System.out.println("RegisterController.");
		
	   try {
		   BeanUser user = new BeanUser();
		   HashMap<String,String> map = new HashMap<String,String>();
		   map.put("user", request.getParameter("username"));
		   map.put("mail", request.getParameter("email"));
		   BeanUtils.populate(user, map);
		
		   if (user.checkError()) {
			   DAO.getDAO().insertUser(user,
					   /*JWT.getJWT().encode(request.getParameter("password")));*/
					   request.getParameter("password"));
			   ResultSet res = DAO.getDAO().executeSQL("SELECT id FROM APP_USER WHERE USERNAME=\'"+user.getUser()+"\'");
			   res.next();
			   user.setUserId(res.getInt("id"));
			   request.setAttribute("login",user);
			   RequestDispatcher dispatcher = request.getRequestDispatcher("ViewLoginForm.jsp");
			   dispatcher.forward(request, response);
		   } else {
			   request.setAttribute("user",user);
			   RequestDispatcher dispatcher = request.getRequestDispatcher("ViewRegisterForm.jsp");
			   dispatcher.forward(request, response);
		   }
	   
	   } catch (IllegalAccessException | InvocationTargetException | SQLException e) {
			// TODO Auto-generated catch block
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
