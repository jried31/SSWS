/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpa.gercom.sensor4cities.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 *
 * @author leokassio
 */
@Entity
public class AdminMember implements Serializable {
    
    @Id
    @GeneratedValue
    private Long idMember;
    @Column(nullable=false)
    private String login;
    @Column(nullable=false)
    private String password;

    public AdminMember() {
    }

    public AdminMember(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public Long getIdMember() {
        return idMember;
    }

    public void setIdMember(Long idMember) {
        this.idMember = idMember;
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
