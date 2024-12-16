package services;

import java.util.*;
import models.Article;
import models.Commande;
import models.User;

public class DonneesInitiales {

    public static List<User> initialiserUtilisateurs() {
        List<User> utilisateurs = new ArrayList<>();
        utilisateurs.add(new User("admin@admin.com", "0000",1000));
        utilisateurs.add(new User("client1@gmail.com", "0000",2000));
        utilisateurs.add(new User("client2@gmail.com", "0000",500));
        return utilisateurs;
    }
    
    public static List<Article> initializeArticles() {
        List<Article> articles = new ArrayList<>();
        articles.add(new Article(1, "Laptop", 1000.0, 10,"https://res.cloudinary.com/dhzlfojtv/image/upload/v1734190521/pexels-morningtrain-18105_ggd5vq.jpg"));
        articles.add(new Article(2, "Smartphone", 700.0, 20,"https://res.cloudinary.com/dhzlfojtv/image/upload/v1734190493/pexels-lastly-699122_zh5bop.jpg"));
        articles.add(new Article(3, "Headphones", 50.0, 100,"https://res.cloudinary.com/dhzlfojtv/image/upload/v1734190450/pexels-moose-photos-170195-1037992_l4bqqw.jpg"));
        articles.add(new Article(4, "Keyboard", 30.0, 50,"https://res.cloudinary.com/dhzlfojtv/image/upload/v1734190625/pexels-pixabay-532173_dugolg.jpg"));
        articles.add(new Article(5, "Mouse", 20.0, 50,"https://res.cloudinary.com/dhzlfojtv/image/upload/v1734190420/pexels-vojtech-okenka-127162-392018_wtzdez.jpg"));
        articles.add(new Article(6, "Monitor", 150.0, 25,"https://res.cloudinary.com/dhzlfojtv/image/upload/v1734190366/pexels-karoldach-400678_blmm0j.jpg"));
        articles.add(new Article(7, "External Hard Drive", 80.0, 30,"https://res.cloudinary.com/dhzlfojtv/image/upload/v1734190668/pexels-thepaintedsquare-3361486_evkup5.jpg"));
        articles.add(new Article(8, "USB Hub", 15.0, 60,"https://res.cloudinary.com/dhzlfojtv/image/upload/v1734190314/pexels-rann-vijay-677553-7742583_chgg0d.jpg"));
        return articles;
    }


    
    public static List<Commande> initialiserCommandes(List<Article> articles) {
        List<Commande> commandes = new ArrayList<>();

        Map<Integer, Article> articleMap = new HashMap<>();
        for (Article article : articles) {
            articleMap.put(article.getId(), article);
        }

        List<Article> articlesCommande1 = new ArrayList<>();
        articlesCommande1.add(new Article(articleMap.get(1).getId(), articleMap.get(1).getNom(), articleMap.get(1).getPrix(), 5,articleMap.get(1).getImageUrl()));
        articlesCommande1.add(new Article(articleMap.get(2).getId(), articleMap.get(2).getNom(), articleMap.get(2).getPrix(), 10,articleMap.get(2).getImageUrl()));
        commandes.add(new Commande(1, "client2@gmail.com", new Date(), articlesCommande1, "Livré"));

        List<Article> articlesCommande2 = new ArrayList<>();
        articlesCommande2.add(new Article(articleMap.get(6).getId(), articleMap.get(6).getNom(), articleMap.get(6).getPrix(), 3,articleMap.get(6).getImageUrl()));
        commandes.add(new Commande(2, "client2@gmail.com", new Date(), articlesCommande2, "En cours"));

        List<Article> articlesCommande3 = new ArrayList<>();
        articlesCommande3.add(new Article(articleMap.get(4).getId(), articleMap.get(4).getNom(), articleMap.get(4).getPrix(), 5,articleMap.get(4).getImageUrl()));
        articlesCommande3.add(new Article(articleMap.get(7).getId(), articleMap.get(7).getNom(), articleMap.get(7).getPrix(), 7,articleMap.get(7).getImageUrl()));
        commandes.add(new Commande(3, "client1@gmail.com", new Date(), articlesCommande3, "Annulé"));

        List<Article> articlesCommande4 = new ArrayList<>();
        articlesCommande4.add(new Article(articleMap.get(2).getId(), articleMap.get(2).getNom(), articleMap.get(2).getPrix(), 2,articleMap.get(2).getImageUrl()));
        articlesCommande4.add(new Article(articleMap.get(8).getId(), articleMap.get(8).getNom(), articleMap.get(8).getPrix(), 1,articleMap.get(8).getImageUrl()));
        commandes.add(new Commande(4, "client1@gmail.com", new Date(), articlesCommande4, "Livré"));

        List<Article> articlesCommande5 = new ArrayList<>();
        articlesCommande5.add(new Article(articleMap.get(5).getId(), articleMap.get(5).getNom(), articleMap.get(5).getPrix(), 3,articleMap.get(5).getImageUrl()));
        articlesCommande5.add(new Article(articleMap.get(3).getId(), articleMap.get(3).getNom(), articleMap.get(3).getPrix(), 10,articleMap.get(3).getImageUrl()));
        commandes.add(new Commande(5, "client1@gmail.com", new Date(), articlesCommande5, "En cours"));

        return commandes;
    }

}
