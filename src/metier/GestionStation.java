package metier;

import java.rmi.RemoteException;

public interface GestionStation extends java.rmi.Remote {
	
	public GestionStationNotif getNotification() throws java.rmi.RemoteException;
	
	public void setNotification(GestionStationNotif notification) throws java.rmi.RemoteException;
	
	public void ajouterStation (Station station) throws java.rmi.RemoteException;
	
	public void supprimerStation(Station station) throws java.rmi.RemoteException;
	
	public int creerUtilisateur(String pnom, String pprenom, String pmotdepasse, String ptelephone, String padressemail, 
			String padressepostale, String objectClass) throws java.rmi.RemoteException;
	
	public int creerOperateur(String pnom, String pprenom, String pmotdepasse, String ptelephone, String padressemail, 
			String padressepostale) throws java.rmi.RemoteException;

	public int creerAdministrateur(String pnom, String pprenom, String pmotdepasse, String ptelephone, String padressemail, 
			String padressepostale) throws java.rmi.RemoteException;
	
	public int creerClient(String pnom, String pprenom, String pmotdepasse, String ptelephone, String padressemail, 
			String padressepostale) throws java.rmi.RemoteException;
	
	public void ajouterVeloStation(Velo velo, Station station) throws java.rmi.RemoteException;
	
	public void supprimerVeloStation(Velo velo, Station station) throws java.rmi.RemoteException;
	
	public void transfererVelo(Velo velo, Station stationOrigine, Station stationDestination) throws java.rmi.RemoteException;
	
	public void transfererVelo(int nombreVelos, Station stationOrigine, Station stationDestination) throws RemoteException;
	
	public void ajouterUtilisateur (Utilisateur utilisateur) throws java.rmi.RemoteException;
	
	public void supprimerUtilisateur (Utilisateur utilisateur) throws java.rmi.RemoteException;
	
	public int emprunterVeloClient(int idClient, int idStation) throws java.rmi.RemoteException;
	
	public int ramenerVeloClient(int idClient, int idStation) throws java.rmi.RemoteException;
	
	public boolean estUtilisateurIdentifie(int identifiant, String motDePasse) throws java.rmi.RemoteException;
	
	public Station chercherStationLaPlusProche(Station stationActuelle) throws java.rmi.RemoteException;
	
	public String[] getRoleUtilisateur(int identifiant) throws java.rmi.RemoteException;
	
	public int emprunterVeloAdministrateur(int identifiant, int idVelo, int idStation) throws java.rmi.RemoteException;
	
	public int deposerVeloAdministrateur(int identifiant, int idVelo, int idStation) throws java.rmi.RemoteException;
	
	public int[] emprunterVeloOperateur(int identifiant, int idStation) throws java.rmi.RemoteException;
	
	public int[] deposerVeloOperateur(int identifiant, int idStation) throws java.rmi.RemoteException;
	
	public int creerStation(String nomStation, double longitude, double latitude, int capacite) throws java.rmi.RemoteException;
}
