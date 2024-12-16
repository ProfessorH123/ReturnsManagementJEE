package services;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import models.Article;
import models.Commande;
import models.Retour;
import models.User;

public interface IRetourService extends Remote {
    // COMMANDES
    public void updateCommande(Commande commande) throws RemoteException;
    List<Commande> getHistoriqueCommandes(String email) throws RemoteException;
    Commande getCommandeById(int idCommande)throws RemoteException;

    // RETOURS
    List<Retour> getAllRetours() throws RemoteException;
    void updateRetourStatus(Retour retour) throws RemoteException;
    boolean verifierRetoursCommandeExiste(int idCommande) throws RemoteException;
    void modifierRetour(Retour retour) throws RemoteException;
    List<Retour> getRetoursbyUserMail(String userMail) throws RemoteException;
    Retour addRetour(int idCommande, List<Article> articlesToReturn, String raison, String userMail)throws RemoteException;
    public Retour getRetourById(int id) throws RemoteException;
    
    // ARTICLES
    public Article getArticleById(int idArticle)throws RemoteException;
    List<Article> getAvailableArticlesForExchange() throws RemoteException;
    void processExchange(int retourId, List<Article> newArticles) throws RemoteException;
}

