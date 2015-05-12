package metier;

import java.rmi.Remote;
import java.rmi.RemoteException;
import metier.CarteAcces.Role;

public interface GestionStation extends Remote {
	
	public GestionStationNotif getNotification() throws RemoteException;
	
	public void setNotification(GestionStationNotif notification) throws RemoteException;
	
	public int creerStation(String nomStation, double longitude, double latitude, int capacite, boolean estMaitre) throws RemoteException;
	
	public void supprimerStation(int idStation) throws RemoteException;
	
	public int creerUtilisateur(String pnom, String pprenom, String pmotdepasse, String ptelephone, String padressemail, 
			String padressepostale, String objectClass) throws RemoteException;
	
	public void supprimerUtilisateur (Utilisateur utilisateur) throws RemoteException;
	
	public void ajouterVeloStation(Velo velo, int idStation) throws RemoteException;
	
	public void supprimerVeloStation(Velo velo, int idStation) throws RemoteException;
	
	public void transfererVelo(Velo velo, int idStationOrigine, int idStationDestination) throws RemoteException;
	
	public int[] emprunterVelos(int idUtilisateur, int idStation, int nbVelos, Role roleEmprunt) throws RemoteException;
	
	public int[] emprunterVelos(int idUtilisateur, int idStation, int idVelo) throws RemoteException;
	
	public int deposerVelos(int idUtilisateur, int idStation) throws RemoteException;
	
	public boolean estUtilisateurIdentifie(int identifiant, String motDePasse) throws RemoteException;
	
	public String[] getRoleUtilisateur(int identifiant) throws RemoteException;

	public int[] dureePrixEmpruntVeloClient(int idUtilisateur, int idStation) throws RemoteException;
	
}
