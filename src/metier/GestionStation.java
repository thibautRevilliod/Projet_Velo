package metier;

import java.rmi.RemoteException;

public interface GestionStation extends java.rmi.Remote {
	
	public int ajouterStation (String nomStation, double longitude, double latitude) throws java.rmi.RemoteException;
	
	public void supprimerStation(int idStation) throws java.rmi.RemoteException;
	
	public int creerUtilisateur(String pnom, String pprenom, String pmotdepasse, String ptelephone, String padressemail, 
			String padressepostale, String objectClass) throws java.rmi.RemoteException;
	
	public int creerOperateur(String pnom, String pprenom, String pmotdepasse, String ptelephone, String padressemail, 
			String padressepostale) throws java.rmi.RemoteException;

	public int creerAdministrateur(String pnom, String pprenom, String pmotdepasse, String ptelephone, String padressemail, 
			String padressepostale) throws java.rmi.RemoteException;
	
	public int creerClient(String pnom, String pprenom, String pmotdepasse, String ptelephone, String padressemail, 
			String padressepostale) throws java.rmi.RemoteException;
	
	public void ajouterVeloStation(Velo velo, int idStation) throws java.rmi.RemoteException;
	
	public void supprimerVeloStation(Velo velo, int idStation) throws java.rmi.RemoteException;
	
	public void transfererVelo(Velo velo, int idStationOrigine, int idStationDestination) throws java.rmi.RemoteException;
	
	public void ajouterUtilisateur (Utilisateur utilisateur) throws java.rmi.RemoteException;
	
	public void supprimerUtilisateur (Utilisateur utilisateur) throws java.rmi.RemoteException;
	
	public void emprunterVeloClient(Client client, Velo velo, Station station) throws java.rmi.RemoteException;
	
	public void ramenerVeloClient(Client client, Velo velo, Station station) throws java.rmi.RemoteException;
	
	public boolean estUtilisateurIdentifie(int identifiant, String motDePasse) throws java.rmi.RemoteException;
	
	public Station chercherStationLaPlusProche(Station stationActuelle) throws java.rmi.RemoteException;
	
	public String[] getRoleUtilisateur(int identifiant) throws java.rmi.RemoteException;
	
	public int emprunterVeloAdministrateur(int identifiant, int idVelo, int idStation) throws java.rmi.RemoteException;
	
	public int deposerVeloAdministrateur(int identifiant, int idVelo, int idStation) throws java.rmi.RemoteException;
}
