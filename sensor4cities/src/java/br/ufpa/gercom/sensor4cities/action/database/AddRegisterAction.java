/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpa.gercom.sensor4cities.action.database;

import br.ufpa.gercom.sensor4cities.action.IAction;
import br.ufpa.gercom.sensor4cities.dao.Dao;
import br.ufpa.gercom.sensor4cities.model.Register;
import org.hibernate.Session;

/**
 * Action desenvolvida para adição de um novo registro de colaborador do sistema,
 * esta action realiza persistência dos dados do usuário junto ao banco de dados.
 * @author leokassio
 */
public class AddRegisterAction implements IAction {

    private Register register;
    private Session session;

    public AddRegisterAction() {
    }

    public AddRegisterAction(Register register) {
        this.register = register;
    }
    
    public String execute() {
        Dao<Register> daoRegister = new Dao<Register>(Register.class, session);
        daoRegister.save(register);
        return "ok";
    }

    public void setRegister(Register register) {
        this.register = register;
    }

    public void setSession(Session session) {
        this.session = session;
    }
        
}
