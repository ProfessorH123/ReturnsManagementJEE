package services;

import java.rmi.Remote;
import java.rmi.RemoteException;

import models.User;

public interface IUserService extends Remote{
	// UTILISATEURS
    boolean creerCompte(String email, String motDePasse) throws RemoteException;
    boolean seConnecter(String email, String motDePasse) throws RemoteException;
    User getUserByEmail(String email) throws RemoteException;
    void setSoldUser(int retourId) throws RemoteException;
}
