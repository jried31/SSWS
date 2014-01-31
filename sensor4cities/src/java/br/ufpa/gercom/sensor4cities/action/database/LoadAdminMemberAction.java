/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpa.gercom.sensor4cities.action.database;

import br.ufpa.gercom.sensor4cities.action.IAction;
import br.ufpa.gercom.sensor4cities.model.AdminMember;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author leokassio
 */
public class LoadAdminMemberAction implements IAction {

    private AdminMember adminMember;
    private Session session;
    
    public String execute() {
        Criteria c = session.createCriteria(AdminMember.class);
        c = c.add(Restrictions.and(Restrictions.eq("login", adminMember.getLogin()), Restrictions.eq("password", adminMember.getPassword())));
        c = c.setMaxResults(1);
        Transaction t = session.beginTransaction();
        List<AdminMember> l = c.list();
        t.commit();
        if(!l.isEmpty()) {
            adminMember = l.get(0);
            return "ok";
        } else {
            adminMember = null;
            return "error";
        }
    }

    public AdminMember getAdminMember() {
        return adminMember;
    }

    public void setAdminMember(AdminMember adminMember) {
        this.adminMember = adminMember;
    }

    public void setSession(Session session) {
        this.session = session;
    }
    
}
