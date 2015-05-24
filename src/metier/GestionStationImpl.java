package metier;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

import metier.CarteAcces.Role;
import metier.Velo.Etat;


public class GestionStationImpl extends UnicastRemoteObject implements GestionStation {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2573185254495289156L;
	
	public GestionStationNotif notification;

	
	public GestionStationImpl() throws RemoteException {
		super();
	}
	
	@Override
	public GestionStationNotif getNotification() {
		return this.notification;
	}
	
	@Override
	public void setNotification(GestionStationNotif notification) {
		this.notification = notification;
	}

	@Override
	public int creerStation(String nomStation, double longitude, double latitude, int capacite, boolean estMaitre) throws java.rmi.RemoteException
	{
		Position position = new Position(longitude, latitude);
		Station nouvelleStation = new Station(nomStation, position, capacite);
		int idStation = nouvelleStation.getIdStation();
		return idStation;
	}
	
	@Override
	public synchronized void supprimerStation(int idStation) throws RemoteException
	{
		if(Station.supprimerStation(idStation))
			Station.getLesStations().remove(idStation);
	}
	
	@Override
	public synchronized int creerUtilisateur(String pnom, String pprenom, String pmotdepasse, String ptelephone, String padressemail, String padressepostale, String objectClass) throws RemoteException 
	{
		int idUtilisateur;
		switch(objectClass)
		{
			case "Client" :
				Client client = new Client( pnom, pprenom, pmotdepasse, ptelephone, padressemail, padressepostale);
				idUtilisateur = client.getIdUtilisateur();
				break;
			case "Operateur" :
				Operateur operateur = new Operateur( pnom,  pprenom, pmotdepasse, ptelephone,  padressemail, padressepostale);
				idUtilisateur = operateur.getIdUtilisateur();
				break;
			case "Administrateur" :
				Administrateur administrateur = new Administrateur( pnom,  pprenom, pmotdepasse, ptelephone,  padressemail, padressepostale);
				idUtilisateur = administrateur.getIdUtilisateur();
				break;
			default:
				idUtilisateur = -1; // idUtilisateur non ok 
				break;
		}
		return idUtilisateur;
	    
	}
	
	@Override
	public synchronized void ajouterVeloStation(Velo velo, int idStation) throws RemoteException
	{
		Station station = Station.getLesStations().get(idStation);
		station.ajouterVelo(velo);
	}
	
	@Override
	public synchronized void supprimerVeloStation(Velo velo, int idStation) throws RemoteException
	{
		Station station = Station.getLesStations().get(idStation);
		station.supprimerVelo(velo);
	}
	
	// à modifier
	@Override
	public int[] emprunterVelos(int idUtilisateur, int idStation, int nbVelos, Role roleEmprunt) throws RemoteException
	{
		Utilisateur utilisateur;
		Station station;
		int[] lesIdVelos = new int[nbVelos + 1]; //On rajoute un emplacement qui contiendra l'idStation des vélos
		if(Utilisateur.getLesUtilisateurs().containsKey(idUtilisateur) && Station.getLesStations().containsKey(idStation))
		{
			utilisateur = Utilisateur.getLesUtilisateurs().get(idUtilisateur);
			//On vérifie les droits de l'utilisateur
			if(utilisateur.hasRole(roleEmprunt))
			{
				station = Station.getLesStations().get(idStation);
				//Méthode qui retourne les vélos de la station (ou de la plus proche) avec idStation
				lesIdVelos = station.getVelosLibresStation(nbVelos);
				//On teste que la méthode précédente ne retourne pas un idStation différent
				if(lesIdVelos[nbVelos] == idStation)
				{
					//On assigne les vélos à l'utilisateur, en spécifiant son rôle pour traitements spécifiques
					utilisateur.emprunterVelos(lesIdVelos, roleEmprunt);
					//Suppression des vélos de la station
					station.supprimerVelos(lesIdVelos);
				}
			}
			else
			{
				lesIdVelos[nbVelos] = -2; //Erreur : Utilisateur non Admin
			}
			
		}
		else
		{
			lesIdVelos[nbVelos] = -1; //Erreur : Utilisateur ou Station inexistant
		}
		return lesIdVelos;
	}
	
	@Override
	public int[] emprunterVelos(int idUtilisateur, int idStation, int idVelo) throws RemoteException
	{
		Utilisateur utilisateur;
		Station station;
		Velo velo;
		int[] lesIdVelos = new int[2]; 
		lesIdVelos[0] = idVelo;
		if(Utilisateur.getLesUtilisateurs().containsKey(idUtilisateur) && Station.getLesStations().containsKey(idStation))
		{
			utilisateur = Utilisateur.getLesUtilisateurs().get(idUtilisateur);
			//On vérifie les droits de l'utilisateur
			if(utilisateur.hasRole(Role.Administrateur))
			{
				station = Station.getLesStations().get(idStation);
				velo = station.getVeloStation(idVelo);
				//On vérifie que le vélo choisi soit libre
				if(velo != null && velo.getEtat().equals(Etat.Libre))
				{
					//On assigne les vélos à l'utilisateur, en spécifiant son rôle pour traitements spécifiques
					utilisateur.emprunterVelos(lesIdVelos, Role.Administrateur);
					//Suppression du vélo de la station
					station.supprimerVelo(velo);
					lesIdVelos[1] = 0;//Tout s'est bien passé : code 0
				}
				else
				{
					lesIdVelos[1] = -3; //Erreur : Vélo inexistant ou non libre
				}
			}
			else
			{
				lesIdVelos[1] = -2; //Erreur : Utilisateur non Admin
			}
			
		}
		else
		{
			lesIdVelos[1] = -1; //Erreur : Utilisateur ou Station inexistant
		}
		return lesIdVelos;
	}
	
	@Override
	public int deposerVelos(int idUtilisateur, int idStation) throws RemoteException
	{
		Utilisateur utilisateur;
		Station station;
		int[] lesIdVelos;
		int idStationDepot = idStation;
		int nbVelos;
		if(Utilisateur.getLesUtilisateurs().containsKey(idUtilisateur) && Station.getLesStations().containsKey(idStation))
		{
			utilisateur = Utilisateur.getLesUtilisateurs().get(idUtilisateur);
			station = Station.getLesStations().get(idStation);
			lesIdVelos = utilisateur.getIdVelos();
			nbVelos = lesIdVelos.length;
			if(station.getNombrePlacesDispos() >= nbVelos)
			{
				station.ajouterVelos(lesIdVelos);
				utilisateur.deposerVelos();
			}
			else
				idStationDepot = station.getStationLaPlusProche().getIdStation();
		}
		return idStationDepot;
	}
	
	@Override
	public synchronized void transfererVelo(Velo velo, int idStationOrigine, int idStationDestination) throws RemoteException
	{
		Station stationDestination = Station.getStation(idStationDestination);
		if(stationDestination.getNombrePlacesDispos()==0)
		{
			System.out.println("La station de destination " + stationDestination.getIdStation() + "n'a plus de places disponibles");
		}
		else
		{
			supprimerVeloStation(velo, idStationOrigine);
			ajouterVeloStation(velo, idStationDestination);
		}		
	}
	
	@Override
	public synchronized void supprimerUtilisateur (Utilisateur utilisateur) throws RemoteException
	{
		Utilisateur.supprimerUtilisateur(utilisateur);
	}	
	
	@Override
	public synchronized boolean estUtilisateurIdentifie(int identifiant, String motDePasse) throws RemoteException
	{
		return Utilisateur.estUtilisateurIdentifie(identifiant, motDePasse);
	}
	
	public synchronized String[] getRoleUtilisateur(int identifiant) throws java.rmi.RemoteException
	{
		return Utilisateur.getRoles(identifiant);
	}
	
	public synchronized static void main(String[] args) throws Exception {
		LocateRegistry.createRegistry(1099);
		Naming.rebind("MaGestionStation",new GestionStationImpl());
		System.out.println("MaGestionStation est enregistrée");
	}

	@Override
	public int[] dureePrixEmpruntVeloClient(int idUtilisateur, int idStation) throws RemoteException {
		// TODO code dureePrixEmpruntVeloClient method
		return null;
	}

	@Override
	public boolean gestionStationHasMaitre() throws RemoteException {
		return Station.hasMaitre();
	}
	
	public synchronized void ajouterRoleUtilisateur(int idUtilisateur, Role role) throws RemoteException
	{
		Utilisateur user = Utilisateur.getLesUtilisateurs().get(idUtilisateur);
		user.ajouterRoleUtilisateur(role);
	}

}
