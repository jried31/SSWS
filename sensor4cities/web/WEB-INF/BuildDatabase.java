/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpa.gercom.sensorslab.dao;

import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;

/**
 *
 * @author leokassio
 */
public class BuildDatabase {

    public static void main(String[] args) {
        Configuration configuration = new Configuration();
        configuration = configuration.configure();
        SchemaExport export = new SchemaExport(configuration);
        export.create(true, true);
    }
}
