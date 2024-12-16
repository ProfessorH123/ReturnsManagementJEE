package controller;


import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import services.IRetourService;
import services.IUserService;
import models.*;
import java.io.*;
import java.rmi.Naming;
import java.util.ArrayList;
import java.util.List;

@WebServlet({"/RetoursServlet", "/acceptRetour","/processRetour","/processExchange","/exchange"})
public class RetoursServlet extends HttpServlet {
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
            case "/addRetour":
            	int commandeId = Integer.parseInt(request.getParameter("idCommande"));
                Commande commande = retourService.getCommandeById(commandeId);
                request.setAttribute("commande", commande);
                destination = "/ajouterRetour.jsp";
                break;
            case "/retour":
                HttpSession session2 = request.getSession();
                String sessionEmail = (String) session2.getAttribute("userMail");

                if (sessionEmail != null) {
                    List<Retour> retours = retourService.getRetoursbyUserMail(sessionEmail);
                    request.setAttribute("retours", retours);
                    destination = "/retours.jsp";
                } else {
                    request.setAttribute("message", "Session expirée. Veuillez vous reconnecter.");
                    destination = "/login.jsp";
                }
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
            case "/addRetour":
                int commandeId = Integer.parseInt(request.getParameter("idCommande"));
                
                String raison = request.getParameter("raison");

                Commande commande = retourService.getCommandeById(commandeId);
                
                if (commande != null) {
                    List<Article> retourArticles=new ArrayList<Article>();
                    for (Article article : commande.getArticles()) {
                        String returnQuantityParam = request.getParameter("returnQuantity_" + article.getId());
                        int returnQuantity = Integer.parseInt(returnQuantityParam);

                        if (returnQuantity > 0) {
                        	Article retourArticle=new Article(article.getId(),article.getNom(),article.getPrix(),returnQuantity);
                			retourArticles.add(retourArticle);
                            
                        }
                    }
                    
                    retourService.addRetour(commandeId, retourArticles, raison, commande.getEmailClient());
                    message = "Retour(s) traité(s) avec succès.";
                    request.setAttribute("message", message);
                    List<Commande> commandes = retourService.getHistoriqueCommandes(commande.getEmailClient());
                    request.setAttribute("commandes", commandes);
    	            User user = userService.getUserByEmail(commande.getEmailClient());
    	            request.setAttribute("solde", user.getSolde());
                                        
                    destination = "/historique.jsp";
                } else {
                    message = "Commande non trouvée.";
                    request.setAttribute("message", message);
                    destination = "/historique.jsp";
                }
                break;
            case "/acceptRetour":
                try {
                    int retourId = Integer.parseInt(request.getParameter("retourId"));
                    Retour retourToUpdate = retourService.getRetourById(retourId);
                    
                    if (retourToUpdate != null) {
                        retourToUpdate.setStatus("Accepted");
                        retourService.updateRetourStatus(retourToUpdate);
                        message = "Retour ID " + retourId + " est accepter avec success.";
                    } else {
                        message = "Retour with ID " + retourId + " not found.";
                    }
                } catch (Exception e) {
                    message = "An error occurred while accepting the retour.";
                    e.printStackTrace();
                }
                List<Retour> retours = retourService.getAllRetours();
                request.setAttribute("retours", retours);
                request.setAttribute("message", message);
                destination = "/listOfAllRetours.jsp";
                break;
                
                
            case "/processRetour":
                int retourId = Integer.parseInt(request.getParameter("retourId"));
                String action = request.getParameter("action");
                double totalPrice = Double.parseDouble(request.getParameter("totalPrice"));

                if ("remboursement".equals(action)) {
                    try {
                        Retour retour = retourService.getRetourById(retourId);
                        Commande command = retourService.getCommandeById(retour.getIdCommande());

                        for (Article retourArticle : retour.getArticles()) {
                            for (Article commandeArticle : command.getArticles()) {
                                if (commandeArticle.getId() == retourArticle.getId()) {
                                    int updatedQuantity = commandeArticle.getQuantite() - retourArticle.getQuantite();
                                    commandeArticle.setQuantite(updatedQuantity);
                                }
                            }
                        }
                        
                        command.getArticles().removeIf(article -> article.getQuantite() <= 0);
                        retourService.updateCommande(command);
                        retour.setReturned("yes");
                        retourService.modifierRetour(retour);
                        userService.setSoldUser(retourId);

                        request.setAttribute("message", "Remboursement de " + totalPrice + " € effectué avec succès !");
                    } catch (Exception e) {
                        e.printStackTrace();
                        request.setAttribute("message", "Erreur lors du traitement du remboursement.");
                    }
                }
                
                HttpSession session2 = request.getSession();
                String sessionEmail = (String) session2.getAttribute("userMail");
	            User user = userService.getUserByEmail(sessionEmail);
	            request.setAttribute("solde", user.getSolde());
                List<Commande> commandes = retourService.getHistoriqueCommandes(sessionEmail);
                request.setAttribute("email", sessionEmail);
                request.setAttribute("commandes", commandes);

                destination = "/historique.jsp";
                break;

            case "/exchange":
                try {
                    int rtId = Integer.parseInt(request.getParameter("retourId"));
                    Retour retour = retourService.getRetourById(rtId);

                    if (retour != null) {
                        List<Article> availableArticles = retourService.getAvailableArticlesForExchange();
                        double totalReturnedValue = retour.getArticles().stream()
                            .mapToDouble(article -> article.getPrix() * article.getQuantite())
                            .sum();

                        request.setAttribute("availableArticles", availableArticles);
                        request.setAttribute("totalReturnedValue", totalReturnedValue);
                        request.setAttribute("retourId", rtId);
        	            User user1 = userService.getUserByEmail(retour.getUserMail());
        	            double solde = user1.getSolde();
        	            request.setAttribute("solde", solde);
                        destination = "/exchange.jsp";
                    } else {
                        request.setAttribute("message", "Retour not found.");
                        destination = "/historique.jsp";
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    request.setAttribute("message", "Error loading exchange page.");
                    destination = "/historique.jsp";
                }
                break;

            case "/processExchange":
                try {
                    int rtId = Integer.parseInt(request.getParameter("retourId"));
                    Retour retour = retourService.getRetourById(rtId);
                    String[] articleIds = request.getParameterValues("articleIds");
                    String[] articleQuantities = request.getParameterValues("articleQuantities");

                    List<Article> newArticles = new ArrayList<>();
                    double totalNewCost = 0;
                    for (int i = 0; i < articleIds.length; i++) {
                        int id = Integer.parseInt(articleIds[i]);
                        int quantity = Integer.parseInt(articleQuantities[i]);

                        if (quantity > 0) {
                            Article article = retourService.getArticleById(id);
                            if (article != null) {
                                article.setQuantite(quantity);
                                totalNewCost += article.getPrix() * quantity;
                                newArticles.add(article);
                            }
                        }
                    }
                    
                    double totalReturnedValue = retour.getArticles().stream()
                            .mapToDouble(article -> article.getPrix() * article.getQuantite())
                            .sum();
                    double difference = totalReturnedValue - totalNewCost;
                    
                    HttpSession session3 = request.getSession();
                    String sessionEmail1 = (String) session3.getAttribute("userMail");
    	            User user1 = userService.getUserByEmail(sessionEmail1);
    	            double solde = user1.getSolde();
    	           
    	            if (-difference > 0 && -difference > solde) {
    	                request.setAttribute("message", "Échange impossible : le coût supplémentaire de " + String.format("%.2f", -difference) + " € dépasse votre solde disponible de " + String.format("%.2f", solde) + " €.");
    	                List<Commande> commandes1 = retourService.getHistoriqueCommandes(sessionEmail1);
    	                request.setAttribute("email", sessionEmail1);
    	                request.setAttribute("commandes", commandes1);
    		            request.setAttribute("solde", solde);
    	                destination = "/historique.jsp";
    	                break;
    	            }
    	            
                    if (difference == 0) {
                        request.setAttribute("message", "Échange traité avec succès !");
                    } else if (difference > 0) {
                        request.setAttribute("message", "Échange réussi ! La valeur restante de " + difference + " € sera remboursée.");
                    } else {
                        request.setAttribute("message", "L'échange nécessite un paiement supplémentaire de " + (-difference) + " €.");
                    }

                    retourService.processExchange(rtId, newArticles);
                    retour.setReturned("yes");
                    retourService.modifierRetour(retour);
                } catch (Exception e) {
                    e.printStackTrace();
                    request.setAttribute("message", "Error processing exchange.");
                }
                destination = "/confirmation.jsp";
                break;

        }

        request.getRequestDispatcher(destination).forward(request, response);
    }

}