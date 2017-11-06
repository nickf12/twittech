package models;

import utils.DAO;

public class BeanLogin {

    private String user = "";
    private String password = "";
    private int[] error = {0, 0};
    private int[] confirm = {0, 0};

    public String getUser() {
        return user;
    }

    public synchronized void setUser(String user) {
        try {
            if (DAO.getDAO().checkUser(user)) {
                this.user = user;
                this.confirm[0] = 1;
            } else {
                this.error[0] = 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public int[] getError() {
        return error;
    }

    public void setError(int[] error) {
        this.error = error;
    }

    public int[] getConfirm() { return confirm; }

    public void setConfirm(int[] confirm) {
        this.confirm = confirm;
    }

    public String getPassword() {
        return password;
    }

    public synchronized void setPassword(String password) {
        try {
            if (DAO.getDAO().checkPassword(this.getUser(), password)) {
                this.password = password;
                this.confirm[1] = 1;
            } else {
                this.error[1] = 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean checkError() {
        if (this.error[0] == 1 || this.error[1] == 1 || this.user.equals("") || this.password.equals("")) {
            return false;
        }
        return true;
    }

    public boolean isComplete() {
        return (hasValue(getUser()) && hasValue(getPassword()));
    }

    private boolean hasValue(String val) {
        return ((val != null) && (!val.equals("")));
    }


}