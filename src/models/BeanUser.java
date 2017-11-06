package models;

import utils.DAO;

public class BeanUser implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	protected int userId;
	protected String user = null;
	protected String mail = null;
	protected Boolean isAdmin;
	protected int[] error = {0,0};
	
	/* Getters */
	public String getUser(){
		return user;
	}
	
	public String getMail() {
		return mail;
	}
	
	public int[] getError() {
		return error;
	}
	
	/*Setters*/
	public void setUser(String user){
		if(user == null) {
			error[0] = 1;
			return;
		}
		this.user = user;
		try {
			if(DAO.getDAO().checkUser(user)) {
				error[0] = 1;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setMail(String mail){
		if(mail == null) {
			error[1] = 1;
			return;
		}
		this.mail = mail;
		try {
			if(DAO.getDAO().checkMail(mail)) {
				error[1] = 1;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/* Logic Functions */
	public boolean isComplete() {
	    return(hasValue(getUser()) &&
	           hasValue(getMail()) );
	}
	
	private boolean hasValue(String val) {
		return((val != null) && (!val.equals("")));
	}

	public boolean checkError() {
		if (this.error[0] == 1 || this.error[1] == 1) {
			return false;
		}
		return true;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public Boolean getAdmin() {
		return isAdmin;
	}

	public void setAdmin(Boolean admin) {
		isAdmin = admin;
	}
}
