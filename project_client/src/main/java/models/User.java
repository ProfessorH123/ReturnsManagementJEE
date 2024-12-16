package models;

import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String email;
    private String password;
    private double solde;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }
    
    public User(String email, String password,double solde) {
        this.email = email;
        this.password = password;
        this.solde = solde;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

	public double getSolde() {
		return solde;
	}

	public void setSolde(double solde) {
		this.solde = solde;
	}
}

