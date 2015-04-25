package metier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import metier.CarteAcces.Statut;
import metier.CarteAcces.Role;

public abstract class Utilisateur {

	private int idUtilisateur;
	private String nom;
	private String prenom;
	private String motDePasse;
	private String telephone;
	private String adresseMail;
	private String adressePostale;
	//un utilisateur peut avoir plusieurs cartes d'acces (ex : le gestionnaire peut être aussi client)
	public HashMap<Role,CarteAcces> lesCartesAccesUtilisateur = new HashMap<Role, CarteAcces>(); 
	public static HashMap<Integer, Utilisateur> lesUtilisateurs = new HashMap<Integer, Utilisateur>();
	public static int idsUtilisateur = 0; 
	
	public Utilisateur(String pnom, String pprenom, String pmotdepasse, String ptelephone, String padressemail, String padressepostale){
		nom = pnom;
		prenom = pprenom;
		motDePasse = pmotdepasse;
		telephone = ptelephone;
		adresseMail = padressemail;
		adressePostale = padressepostale;
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
	
	public static Utilisateur getUtilisateur(int idUtilisateur)
	{
		Iterator it = lesUtilisateurs.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        Utilisateur utilisateurListe = (Utilisateur) pair.getValue();
	        
	        if(utilisateurListe.getIdUtilisateur() == idUtilisateur)
	        {
	        	return utilisateurListe;
	        }
	    }
	    
	    return null;
	}
	
	public String getMotDePasse() {
		return motDePasse;
	}

	public void setMotDePasse(String motDePasse) {
		this.motDePasse = motDePasse;
	}
	
	

	public static HashMap<Integer, Utilisateur> getLesUtilisateurs() {
		return lesUtilisateurs;
	}

	public static void setLesUtilisateurs(
			HashMap<Integer, Utilisateur> lesUtilisateurs) {
		Utilisateur.lesUtilisateurs = lesUtilisateurs;
	}

	public void ajouterCarteAcces(CarteAcces carteAcces, Role role)
	{
		this.lesCartesAccesUtilisateur.put(role, carteAcces);
	}
	
	public void desactiverCarteAcces(Role role)
	{
		lesCartesAccesUtilisateur.get(role).setStatut(Statut.Inactive);
	}
	
	public String[] getRoles()
	{
		int numberOfRoles = CarteAcces.Role.values().length;
		String[] lesRoles = new String[numberOfRoles];
		
		int i =0;
		for(Map.Entry<Role, CarteAcces> lesCartes : lesCartesAccesUtilisateur.entrySet())
		{
			lesRoles[i] = lesCartes.getKey().toString();
			i++;
		}
		
		return lesRoles;
	}

}
