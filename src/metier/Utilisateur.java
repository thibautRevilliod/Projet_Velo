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
	@SuppressWarnings("unused")
	private String nom;
	@SuppressWarnings("unused")
	private String prenom;
	private String motDePasse;
	@SuppressWarnings("unused")
	private String telephone;
	@SuppressWarnings("unused")
	private String adresseMail;
	@SuppressWarnings("unused")
	private String adressePostale;
	//un utilisateur peut avoir plusieurs cartes d'acces (ex : le gestionnaire peut �tre aussi client)
	protected HashMap<Role,CarteAcces> lesCartesAccesUtilisateur ;
	//Chaque utilisateur a une liste de v�lo, dont le plafond est d�fini pour un Client
	protected HashMap<Integer, Velo> lesVelos ;
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
		Utilisateur.lesUtilisateurs.put(new Integer(idsUtilisateur),this);
		lesCartesAccesUtilisateur = new HashMap<Role, CarteAcces>();
		lesVelos = new HashMap<Integer, Velo>();
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
	//public void setVelo(Velo velo) {this.lesVelos.put(new Integer(velo.getIdVelo()),velo);}
	public static HashMap<Integer, Utilisateur> getLesUtilisateurs() {return lesUtilisateurs;}

	public int[] getIdVelos()
	{
		int nbVelos = lesVelos.size();
		int[] listeIdsVelos = new int[nbVelos];
		Velo veloTemp;
		int i = 0;
		
		Iterator it = lesVelos.entrySet().iterator();
		
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        Integer idVelo = (Integer) pair.getKey();
	        Velo veloListe = (Velo) pair.getValue();
	        if(veloListe.getEtat() == Etat.Libre)
		    {
	        	veloTemp = veloListe;
				listeIdsVelos[i] = veloTemp.getIdVelo();
				i++;
		    }
		}
		
		return listeIdsVelos;
	}
	
	public void ajouterVelo(Velo velo)
	{
		this.lesVelos.put(new Integer(velo.getIdVelo()),velo);
	}
	
	public void supprimerVelo(Velo velo)
	{
		this.lesVelos.remove(velo);
	}
	
	public boolean hasUtilisateurEmprunteVelos()
	{
		return lesVelos.size()>0;
	}
	
	public static Utilisateur supprimerUtilisateur(Utilisateur utilisateur)
	{
		CarteAcces carteAccesTemp;
		//Hypoth�se : on ne peut supprimer un utilisateur que s'il n'a pas d'emprunt en cours
		if(utilisateur.lesVelos.size() == 0)
		{
			//Suppression de toutes les cartes d'acc�s de l'utilisateur
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
			int numberOfRoles = utilisateur.lesCartesAccesUtilisateur.size();
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
	//La m�thode emprunter va d�pendre du type d'utilisateur et de son r�le
	public abstract void emprunterVelos(int[] idVelos, Role roleEmprunt, int idStation) throws java.rmi.RemoteException;
	
	//La m�thode d�poser ne d�pend pas des droits de l'utilisateur (pas d'authentification)
	public void deposerVelos() throws java.rmi.RemoteException
	{	
		Velo veloTemp;
		
		Iterator it = lesVelos.entrySet().iterator();
		
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        Integer idVelo = (Integer) pair.getKey();
	        Velo veloListe = (Velo) pair.getValue();

        	veloTemp = veloListe;
        	veloTemp.setEtat(Etat.Libre);
			supprimerVelo(veloTemp);
		}
	}
	
	public void ajouterRoleUtilisateur(Role r) {
		CarteAcces carteAcces = new CarteAcces(r);
		if(!this.lesCartesAccesUtilisateur.containsKey(r))
		{
			lesCartesAccesUtilisateur.put(carteAcces.getRole(), carteAcces);
		}
	}
}
