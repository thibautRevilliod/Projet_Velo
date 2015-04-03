package metier;

import java.util.HashMap;

public class Utilisateur {

	private int idUtilisateur;
	private String nom;
	private String prenom;
	private int telephone;
	
	private static HashMap<Integer, Utilisateur> lesUtilisateurs = new HashMap<Integer, Utilisateur>();
	
	public Utilisateur(int pidUtilisateur, String pnom, String pprenom, int ptelephone){
		idUtilisateur = pidUtilisateur;
		nom = pnom;
		prenom = pprenom;
		telephone = ptelephone;
		Utilisateur.lesUtilisateurs.put(new Integer(idUtilisateur),this);
	}

}
