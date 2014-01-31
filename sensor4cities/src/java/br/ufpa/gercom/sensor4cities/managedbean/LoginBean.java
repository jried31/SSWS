/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpa.gercom.sensor4cities.managedbean;

import br.ufpa.gercom.sensor4cities.action.database.LoadAdminMemberAction;
import br.ufpa.gercom.sensor4cities.dao.HibernateUtil;
import br.ufpa.gercom.sensor4cities.model.AdminMember;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.hibernate.Session;

/**
 *
 * @author leokassio
 */
public class LoginBean {
    
    private AdminMember adminMember;
    private String login;
    private String password;

    public LoginBean() {
        adminMember = new AdminMember();
    }
    
    public String logMeIn() {
        Session session = HibernateUtil.openSession();
        
        LoadAdminMemberAction loadAdminMemberAction = new LoadAdminMemberAction();
        loadAdminMemberAction.setSession(session);
        loadAdminMemberAction.setAdminMember(new AdminMember(login, password));
        loadAdminMemberAction.execute();
        login = "";
        password = "";
        adminMember = loadAdminMemberAction.getAdminMember();
        FacesContext fc = FacesContext.getCurrentInstance();
        HttpSession hs = (HttpSession) fc.getExternalContext().getSession(false);
        if(adminMember != null) {
            hs.setAttribute("currentUser", adminMember);
            return "manager";
        } else {
            hs.setAttribute("currentUser", null);
            adminMember = new AdminMember();
            return "loginPage";
        }
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
