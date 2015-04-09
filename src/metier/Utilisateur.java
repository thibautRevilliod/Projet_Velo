package metier;

import java.util.ArrayList;
import java.util.HashMap;

import metier.CarteAcces.Statut;
import metier.CarteAcces.Role;

public abstract class Utilisateur {

	private int idUtilisateur;
	private String nom;
	private String prenom;
	private int telephone;
	//un utilisateur peut avoir plusieurs cartes d'acces (ex : le gestionnaire peut être aussi client)
	public HashMap<Role,CarteAcces> lesCartesAccesUtilisateur = new HashMap<Role, CarteAcces>(); 
	public static HashMap<Integer, Utilisateur> lesUtilisateurs = new HashMap<Integer, Utilisateur>();
	public static int idsUtilisateur = 0; 
	
	public Utilisateur(String pnom, String pprenom, int ptelephone){
		nom = pnom;
		prenom = pprenom;
		telephone = ptelephone;
		idsUtilisateur++;
		idUtilisateur = idsUtilisateur;
		Utilisateur.lesUtilisateurs.put(idsUtilisateur,this);
	}
	
	public int getIdUtilisateur() {
		return idUtilisateur;
	}

	public void setIdUtilisateur(int idUtilisateur) {
		this.idUtilisateur = idUtilisateur;
	}
	
	public void ajouterCarteAcces(CarteAcces carteAcces, Role role)
	{
		this.lesCartesAccesUtilisateur.put(role, carteAcces);
	}
	
	public void desactiverCarteAcces(Role role)
	{
		lesCartesAccesUtilisateur.get(role).setStatut(Statut.Inactive);
	}

}
