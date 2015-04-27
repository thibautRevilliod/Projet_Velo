package metier;

import java.util.HashMap;
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
	protected HashMap<Role,CarteAcces> lesCartesAccesUtilisateur ; 
	private static HashMap<Integer, Utilisateur> lesUtilisateurs = new HashMap<Integer, Utilisateur>();
	private static int idsUtilisateur = 0; 
	
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
		lesCartesAccesUtilisateur = new HashMap<Role, CarteAcces>() ;
	}
	
	private static Utilisateur getUtilisateur(int identifiant)
	{
		if(lesUtilisateurs.containsKey(identifiant))
			return lesUtilisateurs.get(identifiant);
		else
			return null;
	}
	
	public int getIdUtilisateur() {
		return idUtilisateur;
	}

	public void setIdUtilisateur(int idUtilisateur) {
		this.idUtilisateur = idUtilisateur;
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
	
	public static void ajouterUtilisateur(Utilisateur utilisateur)
	{
		lesUtilisateurs.put(utilisateur.getIdUtilisateur(), utilisateur);
	}
	
	public static Utilisateur supprimerUtilisateur(Utilisateur utilisateur)
	{
		return lesUtilisateurs.remove(utilisateur.getIdUtilisateur());
	}
	
	public static boolean estUtilisateurIdentifie(int identifiant, String motDePasse)
	{
		Utilisateur utilisateur = getUtilisateur(identifiant);
		if (utilisateur != null)
			return utilisateur.getMotDePasse().equals(motDePasse);
		else
			return false;
	}

	public void ajouterCarteAcces(CarteAcces carteAcces, Role role)
	{
		this.lesCartesAccesUtilisateur.put(role, carteAcces);
	}
	
	public void desactiverCarteAcces(Role role)
	{
		lesCartesAccesUtilisateur.get(role).setStatut(Statut.Inactive);
	}
	
	public static String[] getRoles(int identifiant)
	{
		Utilisateur utilisateur = getUtilisateur(identifiant);
		if (utilisateur != null)
		{
			int numberOfRoles = CarteAcces.Role.values().length;
			String[] lesRoles = new String[numberOfRoles];
			
			int i =0;
			for(Map.Entry<Role, CarteAcces> lesCartes : utilisateur.lesCartesAccesUtilisateur.entrySet())
			{
				lesRoles[i] = lesCartes.getKey().toString();
				i++;
			}
			return lesRoles;
		}
		else
			return null;
	}

}
