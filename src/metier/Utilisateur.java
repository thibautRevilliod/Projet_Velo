package metier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import metier.CarteAcces.Statut;
import metier.CarteAcces.Role;
import metier.Velo.Etat;

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
	//Chaque utilisateur a une liste de vélo, dont le plafond est défini pour un Client
	protected ArrayList<Velo> lesVelos ;
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
		lesCartesAccesUtilisateur = new HashMap<Role, CarteAcces>();
		lesVelos = new ArrayList<Velo>();
	}
	
	private static Utilisateur getUtilisateur(int identifiant)
	{
		if(lesUtilisateurs.containsKey(identifiant))
			return lesUtilisateurs.get(identifiant);
		else
			return null;
	}
	
	public int getIdUtilisateur() {return idUtilisateur;}
	public String getMotDePasse() {return motDePasse;}
	public void setMotDePasse(String motDePasse) {this.motDePasse = motDePasse;}
	public Velo getVelo() {return this.lesVelos.get(0);}
	public void setVelo(Velo velo) {this.lesVelos.set(0, velo);}
	
	public int[] getIdVelos()
	{
		int nbVelos = lesVelos.size();
		int[] listeIdsVelos = new int[nbVelos];
		Velo veloTemp;
		for(int i = 0; i < nbVelos; i++)
		{
			veloTemp = lesVelos.get(i);
			listeIdsVelos[i] = veloTemp.getIdVelo();
		}
		return listeIdsVelos;
	}
	
	public void ajouterVelo(Velo velo)
	{
		this.lesVelos.add(velo);
	}
	
	public void supprimerVelo(Velo velo)
	{
		this.lesVelos.remove(velo);
	}
	
	public static Utilisateur supprimerUtilisateur(Utilisateur utilisateur)
	{
		CarteAcces carteAccesTemp;
		//Hypothèse : on ne peut supprimer un utilisateur que s'il n'a pas d'emprunt en cours
		if(utilisateur.lesVelos.size() == 0)
		{
			//Suppression de toutes les cartes d'accès de l'utilisateur
			Iterator<CarteAcces> it = utilisateur.lesCartesAccesUtilisateur.values().iterator();
		    while (it.hasNext()) 
		    {
		    	carteAccesTemp = it.next();
		    	utilisateur.lesCartesAccesUtilisateur.remove(carteAccesTemp);
		    }
		    it.remove();
			return lesUtilisateurs.remove(utilisateur);
		}
		else
			return null;
	}
	
	public static boolean estUtilisateurIdentifie(int identifiant, String motDePasse)
	{
		Utilisateur utilisateur = getUtilisateur(identifiant);
		if (utilisateur != null)
			return utilisateur.getMotDePasse().equals(motDePasse);
		else
			return false;
	}

	public abstract boolean ajouterCarteAcces(CarteAcces carteAcces, Role role);
	
	
	public void desactiverCarteAcces(Role role)
	{
		if(lesCartesAccesUtilisateur.containsKey(role))
			lesCartesAccesUtilisateur.get(role).setStatut(Statut.Inactive);
	}
	
	public boolean hasRole(Role role)
	{
		if(lesCartesAccesUtilisateur.containsKey(role))
			return true;
		else
			return false;
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
	//La méthode emprunter va dépendre du type d'utilisateur et de son rôle
	public abstract void emprunterVelos(int[] idVelos, Role roleEmprunt) throws java.rmi.RemoteException;
	
	//La méthode déposer ne dépend pas des droits de l'utilisateur (pas d'authentification)
	public void deposerVelos() throws java.rmi.RemoteException
	{	
		Velo veloTemp;
		for(int i = 0; i < lesVelos.size(); i++)
		{
			veloTemp = lesVelos.get(i);
			veloTemp.setEtat(Etat.Libre);
			supprimerVelo(veloTemp);
		}	
	}
}
