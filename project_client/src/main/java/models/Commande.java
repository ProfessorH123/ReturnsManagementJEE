package models;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Commande implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String emailClient;
    private Date dateCommande;
    private List<Article> articles;
    private String status;

    public Commande(int id, String emailClient, Date dateCommande, List<Article> articles, String status) {
        this.id = id;
        this.emailClient = emailClient;
        this.dateCommande = dateCommande;
        this.articles = articles;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmailClient() {
        return emailClient;
    }

    public void setEmailClient(String emailClient) {
        this.emailClient = emailClient;
    }

    public Date getDateCommande() {
        return dateCommande;
    }

    public void setDateCommande(Date dateCommande) {
        this.dateCommande = dateCommande;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Commande{" +
                "id=" + id +
                ", emailClient='" + emailClient + '\'' +
                ", dateCommande=" + dateCommande +
                ", articles=" + articles +
                ", status='" + status + '\'' +
                '}';
    }
}
