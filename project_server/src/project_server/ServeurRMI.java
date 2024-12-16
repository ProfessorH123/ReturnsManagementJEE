package project_server;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.util.List;

import models.User;
import services.DonneesInitiales;
import services.IRetourService;
import services.IUserService;
import services.RetourServiceImpl;
import services.UserServiceImpl;

public class ServeurRMI {
	public static void main(String[] args) {
        try {
           
            LocateRegistry.createRegistry(1099);

           
            List<User> utilisateurs = DonneesInitiales.initialiserUtilisateurs();
            
            IRetourService service = new RetourServiceImpl(utilisateurs);
            
            Naming.rebind("rmi://localhost:1099/RetourService", service);
            
            IUserService userService = new UserServiceImpl(service,utilisateurs);
            Naming.rebind("rmi://localhost:1099/UserService", userService);

            System.out.println("Serveur RMI prêt et en attente de connexions.");
        } catch (Exception e) {
            System.out.println("Erreur du serveur RMI : " + e.getMessage());
            e.printStackTrace();
        }
    }
}

