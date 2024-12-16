package controller;

import java.io.IOException;
import java.rmi.Naming;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import models.Commande;
import models.Retour;
import models.User;
import services.IRetourService;
import services.IUserService;

@WebServlet("/UserServlet")
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private IRetourService retourService;
	private IUserService userService;

    @Override
    public void init() throws ServletException {
        try {
            retourService = (IRetourService) Naming.lookup("rmi://localhost:1099/RetourService");
        	userService = (IUserService) Naming.lookup("rmi://localhost:1099/UserService");
        } catch (Exception e) {
            throw new ServletException("Erreur lors de la connexion au serveur RMI", e);
        }
    }
    
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getContextPath();
        String uri = request.getRequestURI();
        String mapping = uri.substring(path.length());
        String destination = null;

        switch (mapping) {
	        case "/seConnecter":
	            destination = "/login.jsp"; 
	            break;
	        case "/historique":
	            String email = request.getParameter("email");
	            
	            List<Commande> commandes = retourService.getHistoriqueCommandes(email);

	            User user = userService.getUserByEmail(email);

	            request.setAttribute("email", email);
	            request.setAttribute("commandes", commandes);
	            request.setAttribute("solde", user.getSolde());
	            HttpSession session = request.getSession();
	            session.setAttribute("userMail", email);

	            destination = "/historique.jsp";
	            break;
        }
        request.getRequestDispatcher(destination).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getContextPath();
        String uri = request.getRequestURI();
        String mapping = uri.substring(path.length());
        String destination = null;
        String message = null;

        switch (mapping) {
	        case "/seConnecter":
	            String loginEmail = request.getParameter("email");
	            String password = request.getParameter("password");
	            if(loginEmail.equals("admin@admin.com")) {
		            boolean result = userService.seConnecter(loginEmail, password);
		
		            if (result) {
		                List<Retour> rt = retourService.getAllRetours();
		                request.setAttribute("retours", rt);
		
		                destination = "/listOfAllRetours.jsp";
		            } else {
		                message = "Connexion échouée. Vérifiez vos informations.";
		                request.setAttribute("message", message);
		                destination = "/login.jsp";
		            }
	            }else {
		            boolean result = userService.seConnecter(loginEmail, password);
		
		            if (result) {
		                List<Commande> commandesList = retourService.getHistoriqueCommandes(loginEmail);
		                request.setAttribute("commandes", commandesList);
			            User user = userService.getUserByEmail(loginEmail);
			            request.setAttribute("solde", user.getSolde());
		                HttpSession session = request.getSession();
		                session.setAttribute("userMail", loginEmail);
		
		                destination = "/historique.jsp";
		            } else {
		                message = "Connexion échouée. Vérifiez vos informations.";
		                request.setAttribute("message", message);
		                destination = "/login.jsp";
		            }
	            }
	            break;
	        case "/deconnecter":
	        	HttpSession session = request.getSession(false);
	            if (session != null) {
	                session.invalidate();
	            }
	            destination = "/login.jsp";
	            break;
	        case "/creerCompte":
	            String newEmail = request.getParameter("email");
	            String newPassword = request.getParameter("password");
	            String confirmPassword = request.getParameter("confirmPassword");
	            String mess = null;

	            if (newPassword != null && confirmPassword != null && newPassword.equals(confirmPassword)) {
	                boolean created = userService.creerCompte(newEmail, newPassword);
	                mess = created ? "Compte créé avec succès." : "Échec de la création du compte.";
	                request.setAttribute("message", mess);
	                destination = "/login.jsp";
	            } else {
	                mess = "Les mots de passe ne correspondent pas.";
	                request.setAttribute("error", mess);
	                destination = "/registre.jsp";
	            }
	            break;
        }
        request.getRequestDispatcher(destination).forward(request, response);
    }
}
