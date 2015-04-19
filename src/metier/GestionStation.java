package metier;

public interface GestionStation extends java.rmi.Remote {
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
