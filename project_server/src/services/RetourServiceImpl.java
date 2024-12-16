package services;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Article;
import models.Commande;
import models.Retour;
import models.User;

public class RetourServiceImpl extends UnicastRemoteObject implements IRetourService {
    private static final long serialVersionUID = 1L;

    private List<User> utilisateurs;
    private List<Article> articles;
    private List<Commande> commandes;
    private static List<Retour> retours = new ArrayList<>();

    public RetourServiceImpl(List<User> utilisateurs) throws RemoteException {
        super();
        this.utilisateurs = utilisateurs;
        this.articles = DonneesInitiales.initializeArticles();
        this.commandes = DonneesInitiales.initialiserCommandes(articles);
    }
    
    // COMMANDES
    @Override
    public List<Commande> getHistoriqueCommandes(String email) throws RemoteException {
        List<Commande> historique = new ArrayList<>();
        for (Commande commande : commandes) {
            if (commande.getEmailClient().equals(email)) {
                historique.add(commande);
            }
        }
        return historique;
    }
    
    @Override
    public void updateCommande(Commande updatedCommande) throws RemoteException {
        for (int i = 0; i < commandes.size(); i++) {
            if (commandes.get(i).getId() == updatedCommande.getId()) {
                commandes.set(i, updatedCommande);
                return;
            }
        }
    }
    
    @Override
    public Commande getCommandeById(int idCommande)throws RemoteException {
        for (Commande commande : commandes) {
            if (commande.getId() == idCommande) {
                return commande;
            }
        }
        return null;
    }
    
    // RETOURS
    @Override
    public List<Retour> getAllRetours() throws RemoteException {
        List<Retour> retoursByUser = new ArrayList<>();
        for (Retour retour : retours) {
                retoursByUser.add(retour);
        }
        return retoursByUser;
    }
    
    @Override
    public void updateRetourStatus(Retour retour) throws RemoteException {
        for (int i = 0; i < retours.size(); i++) {
            Retour existingRetour = retours.get(i);
            if (existingRetour.getId() == retour.getId()) {
                existingRetour.setStatus(retour.getStatus());
                return;
            }
        }
    }
    @Override
    public Retour getRetourById(int id) throws RemoteException {
        for (Retour retour : retours) {
            if (retour.getId() == id) {
                return retour;
            }
        }
        return null;
    }

    @Override
    public boolean verifierRetoursCommandeExiste(int idCommande) throws RemoteException{
        for (Retour retour : retours) {
            if (retour.getIdCommande() == idCommande) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void modifierRetour(Retour retour) throws RemoteException {
        for (int i = 0; i < retours.size(); i++) {
            if (retours.get(i).getId() == retour.getId()) {
                retours.set(i, retour);
                return;
            }
        }
        retours.add(retour);
    }

    @Override
    public List<Retour> getRetoursbyUserMail(String userMail) throws RemoteException {
        List<Retour> retoursByUser = new ArrayList<>();
        for (Retour retour : retours) {
            if (retour.getUserMail().equals(userMail)) {
                retoursByUser.add(retour);
            }
        }
        return retoursByUser;
    }
    
    @Override
    public Retour addRetour(int idCommande, List<Article> articlesToReturn, String raison, String userMail)throws RemoteException {
        Commande commande = getCommandeById(idCommande);

        if (commande == null) {
            return null;
        }

        for (Article articleToReturn : articlesToReturn) {
            boolean validReturn = false;
            for (Article commandeArticle : commande.getArticles()) {
                if (commandeArticle.getId() == articleToReturn.getId()) {
                    if (articleToReturn.getQuantite() <= commandeArticle.getQuantite()) {
                        validReturn = true;
                    }
                    break;
                }
            }
            if (!validReturn) {
                return null;
            }
        }

        Retour retour = new Retour(
            retours.size() + 1,
            idCommande,
            articlesToReturn,
            raison,
            "En cours",
            userMail,
            new Date(),
            "no"
        );
        retours.add(retour);

        return retour;
    }
    
    // ARTICLES
    @Override
    public Article getArticleById(int idArticle)throws RemoteException {
        for (Article article : articles) {
            if (article.getId() == idArticle) {
                return article;
            }
        }
        return null;
    }
    
    @Override
    public List<Article> getAvailableArticlesForExchange() throws RemoteException {
        return articles;
    }

    @Override
    public void processExchange(int retourId, List<Article> newArticles) throws RemoteException {
        Retour retour = getRetourById(retourId);
        if (retour == null) {
            return;
        }

        Commande originalCommande = getCommandeById(retour.getIdCommande());
        if (originalCommande == null) {
            return;
        }

        for (Article retourArticle : retour.getArticles()) {
            for (Article commandeArticle : originalCommande.getArticles()) {
                if (commandeArticle.getId() == retourArticle.getId()) {
                    int updatedQuantity = commandeArticle.getQuantite() - retourArticle.getQuantite();
                    commandeArticle.setQuantite(updatedQuantity);
                }
            }
        }

        for (Article newArticle : newArticles) {
            boolean found = false;
            for (Article commandeArticle : originalCommande.getArticles()) {
                if (commandeArticle.getId() == newArticle.getId()) {
                    commandeArticle.setQuantite(commandeArticle.getQuantite() + newArticle.getQuantite());
                    found = true;
                    break;
                }
            }
            if (!found) {
                originalCommande.getArticles().add(newArticle);
            }
        }
        originalCommande.getArticles().removeIf(article -> article.getQuantite() <= 0);

        updateCommande(originalCommande);
        
        double totalReturnedPrice = retour.getArticles().stream()
            .mapToDouble(article -> article.getPrix() * article.getQuantite())
            .sum();

        double totalNewPrice = newArticles.stream()
            .mapToDouble(article -> article.getPrix() * article.getQuantite())
            .sum();
        
        double priceDifference = totalNewPrice - totalReturnedPrice;

        User user = utilisateurs.stream()
            .filter(u -> u.getEmail().equals(originalCommande.getEmailClient()))
            .findFirst()
            .orElse(null);

        if (user != null) {
            user.setSolde(user.getSolde() - (float) priceDifference);
        }
    }
}