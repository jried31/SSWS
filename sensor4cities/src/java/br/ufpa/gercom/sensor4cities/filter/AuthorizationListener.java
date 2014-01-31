/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpa.gercom.sensor4cities.filter;

import br.ufpa.gercom.sensor4cities.model.AdminMember;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpSession;

/**
 *
 * @author leokassio
 */
public class AuthorizationListener implements PhaseListener {

    public void afterPhase(PhaseEvent pe) {
        FacesContext facesContext = pe.getFacesContext();
        String currentPage = facesContext.getViewRoot().getViewId();
        HttpSession httpSession = (HttpSession) facesContext.getExternalContext().getSession(true);
        AdminMember adminMember = (AdminMember) httpSession.getAttribute("currentUser");
        if(adminMember == null) {
            NavigationHandler nh = facesContext.getApplication().getNavigationHandler();
            nh.handleNavigation(facesContext, null, "loginPage");
        }
    }

    public void beforePhase(PhaseEvent pe) {    }

    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }
    
}
