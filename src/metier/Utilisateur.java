package metier;

import java.util.ArrayList;
import java.util.HashMap;

import com.sun.javafx.scene.accessibility.Role;

public abstract class Utilisateur {

	private int idUtilisateur;
	private String nom;
	private String prenom;
	private int telephone;
	//un utilisateur peut avoir plusieurs cartes d'acces (ex : le gestionnaire peut être aussi client)
	public ArrayList<CarteAcces> lesCartesAccesUtilisateur = new ArrayList<CarteAcces>(); 
	public static HashMap<Integer, Utilisateur> lesUtilisateurs = new HashMap<Integer, Utilisateur>();
	public static int idsUtilisateur = 0; 
	
	public Utilisateur(String pnom, String pprenom, int ptelephone){
		nom = pnom;
		prenom = pprenom;
		telephone = ptelephone;
		idsUtilisateur++;
		idUtilisateur=idsUtilisateur;
		Utilisateur.lesUtilisateurs.put(idsUtilisateur,this);
	}
	
	
	
	public int getIdUtilisateur() {
		return idUtilisateur;
	}



	public void setIdUtilisateur(int idUtilisateur) {
		this.idUtilisateur = idUtilisateur;
	}



	public String getNom() {
		return nom;
	}



	public void setNom(String nom) {
		this.nom = nom;
	}



	public String getPrenom() {
		return prenom;
	}



	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}



	public int getTelephone() {
		return telephone;
	}



	public void setTelephone(int telephone) {
		this.telephone = telephone;
	}



	public void ajouterCarteAcces(CarteAcces carteAcces)
	{
		this.lesCartesAccesUtilisateur.add(carteAcces);
	}
	
	public void desactiverCarteAcces(CarteAcces carteAcces)
	{
		this.lesCartesAccesUtilisateur.get(carteAcces.getIdCarteAcces()).setRole(metier.CarteAcces.Role.Expire);
	}

}
