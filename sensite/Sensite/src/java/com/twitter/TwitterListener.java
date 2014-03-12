/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Victor
 */
package com.twitter;

import com.queries.SensiteTwitterPost;
import javax.servlet.*;

public class TwitterListener implements javax.servlet.ServletContextListener{

    public void contextDestroyed(ServletContextEvent sce) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void contextInitialized(ServletContextEvent sce) {
        //SensiteTwitterPost.startBot();
        System.out.println("hello");
    }

}
