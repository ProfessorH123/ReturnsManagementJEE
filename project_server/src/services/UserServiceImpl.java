package services;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import models.Retour;
import models.User;

public class UserServiceImpl extends UnicastRemoteObject implements IUserService{
    private static final long serialVersionUID = 1L;

	private List<User> utilisateurs;
    private IRetourService rs;
    
    public UserServiceImpl(IRetourService rs,List<User> utilisateurs) throws RemoteException {
        super();
        this.utilisateurs = utilisateurs;
        this.rs = rs;
    }
    
    // UTILISATEURS
    @Override
    public boolean creerCompte(String email, String motDePasse) throws RemoteException {
        for (User user : utilisateurs) {
            if (user.getEmail().equals(email)) {
                return false;
            }
        }
        utilisateurs.add(new User(email, motDePasse));
        return true;
    }

    @Override
    public boolean seConnecter(String email, String motDePasse) throws RemoteException {
        for (User user : utilisateurs) {
            if (user.getEmail().equals(email) && user.getPassword().equals(motDePasse)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public User getUserByEmail(String email)throws RemoteException {
        for (User user : utilisateurs) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }

	@Override
	public void setSoldUser(int retourId) throws RemoteException {
        Retour retour = rs.getRetourById(retourId);
        if (retour == null) {
            return;
        }
        User user = getUserByEmail(retour.getUserMail());
        double totalPrice = retour.getArticles().stream().mapToDouble(article -> article.getPrix() * article.getQuantite()).sum();
        user.setSolde(user.getSolde() + totalPrice);
	}
}
