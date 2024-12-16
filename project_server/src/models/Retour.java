package models;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Retour implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private int idCommande;
    private List<Article> articles;
    private String raison;
    private String status;
    private String userMail;
    private Date retourDate;
    private String returned;
    public Retour(int id, int idCommande, List<Article> articles, String raison, String status, String userMail,Date retourDate, String returned) {
        this.id = id;
        this.idCommande = idCommande;
        this.articles = articles;
        this.raison = raison;
        this.status = status;
        this.userMail = userMail;
        this.retourDate = retourDate;
        this.returned=returned;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public Date getRetourDate() {
        return retourDate;
    }

    public void setRetourDate(Date retourDate) {
        this.retourDate = retourDate;
    }

    public int getIdCommande() {
        return idCommande;
    }

    public void setIdCommande(int idCommande) {
        this.idCommande = idCommande;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    public String getRaison() {
        return raison;
    }

    public void setRaison(String raison) {
        this.raison = raison;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserMail() {
        return userMail;
    }

    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }

    @Override
    public String toString() {
        return "Retour{" + "id=" + id + ", idCommande=" + idCommande + ", articles=" + articles +
                ", raison='" + raison + '\'' + ", status='" + status + '\'' + ", userMail=" + userMail + '}';
    }

	public String getReturned() {
		return returned;
	}

	public void setReturned(String returned) {
		this.returned = returned;
	}
}

