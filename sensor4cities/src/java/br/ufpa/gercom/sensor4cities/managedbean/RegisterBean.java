/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpa.gercom.sensor4cities.managedbean;

import br.ufpa.gercom.sensor4cities.action.database.AddRegisterAction;
import br.ufpa.gercom.sensor4cities.dao.HibernateUtil;
import br.ufpa.gercom.sensor4cities.model.Register;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.hibernate.Session;

/**
 *
 * @author leokassio
 */
public class RegisterBean implements Serializable {

    private Register register;

    public RegisterBean() {
        register = new Register();
    }
    
    public Register getRegister() {
        return register;
    }

    public void setRegister(Register register) {
        this.register = register;
    }
    
    public void execute() {
        AddRegisterAction action = new AddRegisterAction(register);
        Session session = HibernateUtil.openSession();
        action.setSession(session);
        action.setRegister(register);
        action.execute();
        register = new Register();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Obrigado por registrar-se :)", "SensorLab"));
    }
}
