/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpa.gercom.sensor4cities.exceptions;

/**
 *
 * @author leokassio
 */
public class LoginDuplicatedException extends RuntimeException {

    public LoginDuplicatedException() {
        super("The login used is duplicated, please contact the administrator NOW!");
    }
    
}
