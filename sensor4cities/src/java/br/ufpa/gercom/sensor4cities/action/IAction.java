/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpa.gercom.sensor4cities.action;

/**
 * Interface de definição do formato das Actions do sistema.
 * Baseada conforme o padrão utilizado no framework Apache Struts2.
 * A estrutura baseia-se no princípio da parametrização do construtor,
 * fornçando que o programador entregue os parâmetros necessários a 
 * execução da action desde a criação do objeto.
 * @author leokassio
 */
public interface IAction {
    
    /**
     * Método base da action, retorna uma String contendo o valor ok para execução normal.
     * @return 
     */
    public String execute();
    
}
