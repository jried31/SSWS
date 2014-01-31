/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpa.gercom.sensor4cities.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 *
 * @author leokassio
 */
@Entity
public class Register implements Serializable {
    
    @Id
    @GeneratedValue
    private Long idRegister;
    private String name;
    private String email;
    private String fronWhere;
    private String categorie;

    public Register() {
    }

    public Register(Long idRegister, String name, String email, String fronWhere, String categorie) {
        this.idRegister = idRegister;
        this.name = name;
        this.email = email;
        this.fronWhere = fronWhere;
        this.categorie = categorie;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFronWhere() {
        return fronWhere;
    }

    public void setFronWhere(String fronWhere) {
        this.fronWhere = fronWhere;
    }

    public Long getIdRegister() {
        return idRegister;
    }

    public void setIdRegister(Long idRegister) {
        this.idRegister = idRegister;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }    
}
