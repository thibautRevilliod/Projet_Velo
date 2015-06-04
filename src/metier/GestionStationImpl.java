package metier;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;

import com.sun.org.apache.bcel.internal.generic.GETSTATIC;

import metier.CarteAcces.Role;
import metier.Velo.Etat;


public class GestionStationImpl extends UnicastRemoteObject implements GestionStation {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2573185254495289156L;
	private HashMap<Integer, Station> lesStations = new HashMap<Integer, Station>(); //On gère ici la liste de toutes les stations, qui elles-même gèrent les vélos
	private HashMap<Integer, Utilisateur> lesUtilisateurs= new HashMap<Integer, Utilisateur>(); //On gère ici la liste de tous les utilisateurs
	
	public GestionStationNotif gestionStationNotif = new GestionStationNotifImpl();
	protected static final int SEUIL_ALERT_PENURIE = 5;
	protected static final int SEUIL_ALERT_SATUREE = 26;

	
	public Notifications getNotification() throws RemoteException {
		return gestionStationNotif.getNotification();
	}


	public GestionStationImpl() throws RemoteException {
		super();
	}
	
	
	public HashMap<Integer, Station> getLesStations() {return lesStations;}

	public HashMap<Integer, Utilisateur> getLesUtilisateurs() {return lesUtilisateurs;}
	
	@Override
	public synchronized int creerStation(String nomStation, double longitude, double latitude, int capacite) throws java.rmi.RemoteException
	{
		Position position = new Position(longitude, latitude);
		Station nouvelleStation = new Station(nomStation, position, capacite);
		int idStation = nouvelleStation.getIdStation();
		lesStations.put(new Integer(idStation), nouvelleStation);
		return idStation;
	}
	
	@Override
	public synchronized void supprimerStation(int idStation) throws RemoteException
	{
		Station station = lesStations.get(idStation);
		if(station.supprimerStation())
		{
			Station.getLesStations().remove(idStation);
			lesStations.remove(idStation);
		}
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
				lesUtilisateurs.put(new Integer(idUtilisateur), client);
				break;
			case "Operateur" :
				Operateur operateur = new Operateur( pnom,  pprenom, pmotdepasse, ptelephone,  padressemail, padressepostale);
				idUtilisateur = operateur.getIdUtilisateur();
				lesUtilisateurs.put(new Integer(idUtilisateur), operateur);
				break;
			case "Administrateur" :
				Administrateur administrateur = new Administrateur( pnom,  pprenom, pmotdepasse, ptelephone,  padressemail, padressepostale);
				idUtilisateur = administrateur.getIdUtilisateur();
				lesUtilisateurs.put(new Integer(idUtilisateur), administrateur);
				break;
			default:
				idUtilisateur = -1; // idUtilisateur non ok 
				break;
		}
		return idUtilisateur;
	    
	}
	
//	@Override
//	public synchronized boolean ajouterVeloStation(Velo velo, int idStation) throws RemoteException
//	{
//		boolean result;
//
//		Station station = Station.getLesStations().get(idStation);
//		result = station.ajouterVelo(velo);
//		return result;
//	}
//	
	@Override
	public synchronized boolean ajouterVeloStation(int idStation) throws RemoteException
	{
		boolean result;
		Velo velo = new Velo();
		Station station = Station.getLesStations().get(idStation);
		//TODO ? Station stationGS = lesStations.get(idStation); -->
		//TODO ? stationGS.ajouterVelo(velo) --> OK
		result = station.ajouterVelo(velo);
		gestionNotification(station);
		
		return result;
	}
	
	@Override
	public synchronized boolean ajouterVeloStationInitialisation(int idStation) throws RemoteException
	{
		boolean result;
		Velo velo = new Velo();
		Station station = Station.getLesStations().get(idStation);
		//TODO ? Station stationGS = lesStations.get(idStation); --> OK
		//TODO ? stationGS.ajouterVelo(velo) --> OK
		result = station.ajouterVelo(velo);
		
		return result;
	}
	
	@Override
	public synchronized void supprimerVeloStation(Velo velo, int idStation) throws RemoteException
	{
		Station station = Station.getLesStations().get(idStation);
		station.supprimerVelo(velo);
		//TODO ? Station stationGS = lesStations.get(idStation);
		//TODO ? stationGS.supprimerVelo(velo); --> OK
	}
	
	@Override
	public synchronized int isEmprunterVelosPossible(int idUtilisateur, int idStation,
			int nbVelos, Role roleEmprunt) throws Exception, RemoteException {
		int resultat = 0;
		Utilisateur utilisateur;
		Station station;
		int[] lesIdVelos = new int[nbVelos + 1]; //On rajoute un emplacement qui contiendra l'idStation des vélos
		if(Utilisateur.getLesUtilisateurs().containsKey(idUtilisateur)) // l'utilisateur existe
		{
			if(Station.getLesStations().containsKey(idStation)) // la station existe
			{
				station = Station.getLesStations().get(idStation);
				if(station.getCapacite() >= nbVelos)
				{
					//TODO ? if(station.getNombreVelosLibres() > 0)
					//{
						utilisateur = Utilisateur.getLesUtilisateurs().get(idUtilisateur);
						//On vérifie les droits de l'utilisateur
						
						if(utilisateur.hasRole(roleEmprunt))
						{
							if((nbVelos > 1 && roleEmprunt == Role.Operateur) || (nbVelos == 1))
							{
								//Méthode qui retourne les vélos de la station (ou de la plus proche) avec idStation
								lesIdVelos = station.getVelosLibresStation(nbVelos);
								//On teste que la méthode précédente ne retourne pas un idStation différent
								if(lesIdVelos[nbVelos] == idStation)
								{
									resultat = 0; // pas d'erreur : l'emprunt est possible
								}
								else
								{
									resultat = lesIdVelos[nbVelos]; // idSTation de la station la plus proche
								}
							}
							else
							{
								resultat = -2; //Erreur : l'utilisateur n'a pas le bon rôle
							}
						}
					//}
					//else
					//{
						//resultat = -5; // Erreur : plus de vélos libres
					//}
				}
				else
				{
					resultat = -4; //Erreur : la capacité de la station est inférieur au nombre de vélos souhaités
				}
			}
			else
			{
				resultat = -3; //Erreur : Station inexistante
			}
			
		}
		else
		{
			resultat = -1; //Erreur : Utilisateur inexistant
		}
		return resultat;
	}
	
	// à modifier
	@Override
	public synchronized int[] emprunterVelos(int idUtilisateur, int idStation, int nbVelos, Role roleEmprunt) throws Exception, RemoteException
	{
		Utilisateur utilisateur;
		Station station;
		int[] lesIdVelos = new int[nbVelos + 1]; //On rajoute un emplacement qui contiendra l'idStation des vélos
		
		utilisateur = Utilisateur.getLesUtilisateurs().get(idUtilisateur);
		
		station = Station.getLesStations().get(idStation);
		//Méthode qui retourne les vélos de la station (ou de la plus proche) avec idStation
		lesIdVelos = station.getVelosLibresStation(nbVelos);
		//On teste que la méthode précédente ne retourne pas un idStation différent
		//On assigne les vélos à l'utilisateur, en spécifiant son rôle pour traitements spécifiques
		utilisateur.emprunterVelos(lesIdVelos, roleEmprunt, idStation);
		//Suppression des vélos de la station
		station.supprimerVelos(lesIdVelos);
		//TODO ? supprimerVeloStation de GestionStation --> OK
			
		//notification
		gestionNotification(station);

		return lesIdVelos;
	}
	
	@Override
	public synchronized int[] emprunterVelos(int idUtilisateur, int idStation, int idVelo) throws RemoteException
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
					utilisateur.emprunterVelos(lesIdVelos, Role.Administrateur, idStation);
					//Suppression du vélo de la station
					station.supprimerVelo(velo);
					//TODO ? supprimerVeloStation de GestionStation --> OK
					lesIdVelos[1] = 0;//Tout s'est bien passé : code 0
					
					//notification
					gestionNotification(station);
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
	
	// utilisé par un client ou un opérateur
	@Override
	public synchronized int deposerVelos(int idUtilisateur, int idStation) throws Exception, RemoteException
	{
		Utilisateur utilisateur;
		Station station;
		int[] lesIdVelos;
		int idStationDepot = idStation;
		int nbVelos;
		if(Utilisateur.getLesUtilisateurs().containsKey(idUtilisateur) && Station.getLesStations().containsKey(idStation))
		{
			utilisateur = lesUtilisateurs.get(idUtilisateur);
			station = lesStations.get(idStation);
			lesIdVelos = utilisateur.getIdVelos();
			nbVelos = lesIdVelos.length;
			if(station.getNombrePlacesDispos() >= nbVelos)
			{
				station.ajouterVelos(lesIdVelos);
				utilisateur.deposerVelos(idStation);
				
				//notification
				gestionNotification(station);
			}
			else
				idStationDepot = station.getStationLaPlusProche(nbVelos).getIdStation();
		}
		return idStationDepot;
	}
	
	// utilisé par l'administrateur pour déposer un vélo précédemment en maintenance
	@Override
	public synchronized int deposerVelos(int idUtilisateur, int idStation, int idVelo) throws Exception, RemoteException
	{
		Utilisateur utilisateur;
		Station station;
		Velo veloADeposer;
		int idStationDepot = idStation;
		int[] idVeloADeposer = new int[1];
		if(Utilisateur.getLesUtilisateurs().containsKey(idUtilisateur) && Station.getLesStations().containsKey(idStation))
		{
			utilisateur = lesUtilisateurs.get(idUtilisateur);
			station = lesStations.get(idStation);
			veloADeposer = utilisateur.getLesVelos().get(idVelo);
			if(station.getNombrePlacesDispos() >= 1 && veloADeposer.getIdVelo()==idVelo)
			{
				idVeloADeposer[0] = idVelo;
				station.ajouterVelos(idVeloADeposer);
				utilisateur.deposerVelo(idVeloADeposer[0], idStation);
				
				//notification
				gestionNotification(station);
			}
			else
				idStationDepot = station.getStationLaPlusProche(1).getIdStation();
		}
		return idStationDepot;
	}
	
//	@Override
//	public synchronized void transfererVelo(Velo velo, int idStationOrigine, int idStationDestination) throws RemoteException
//	{
//		Station stationDestination = lesStations.get(idStationDestination);
//		if(stationDestination.getNombrePlacesDispos()==0)
//		{
//			System.out.println("La station de destination " + stationDestination.getIdStation() + "n'a plus de places disponibles");
//		}
//		else
//		{
//			supprimerVeloStation(velo, idStationOrigine);
//			ajouterVeloStation(velo, idStationDestination);
//		}		
//	}
	
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
		GestionStationNotifImpl gestionStationNotifImpl = new GestionStationNotifImpl();
		System.out.println("MaGestionStationNotifImpl est enregistrée");
	}

	@Override
	public synchronized int[] dureePrixEmpruntVeloClient(int idUtilisateur, int idStation) throws RemoteException {
		// TODO code dureePrixEmpruntVeloClient method
		return null;
	}

	@Override
	public synchronized boolean gestionStationHasMaitre(int idStation) throws RemoteException {
		return lesStations.get(idStation).isEstMaitre();
	}
	
	public synchronized void ajouterRoleUtilisateur(int idUtilisateur, Role role) throws RemoteException
	{
		Utilisateur user = Utilisateur.getLesUtilisateurs().get(idUtilisateur);
		user.ajouterRoleUtilisateur(role);
	}

	@Override
	public synchronized int[] getVelosLibresStation(int idStation) throws Exception, RemoteException 
	{
		Station station = lesStations.get(idStation);
		station.getNombreVelosLibres();
		return station.getVelosLibresStation(station.getNombreVelosLibres());
	}

	public synchronized HashMap<Integer, Station> getLesStationsGS() throws RemoteException
	{
		return Station.getLesStations();
	}
	
	public synchronized HashMap<Integer, Utilisateur> getLesUtilisateursGS() throws RemoteException
	{
		return Utilisateur.getLesUtilisateurs();
	}
	
	public synchronized Station getStation(int idStation) throws RemoteException
	{
		return lesStations.get(idStation);
	}
	
	public synchronized Velo getVelo(int idVelo) throws RemoteException
	{
		return Velo.getVelo(idVelo);
	}
	
	@Override
	public boolean hasCompteUtilisateur(int idUtilisateur) throws RemoteException
	{
		return lesUtilisateurs.containsKey(idUtilisateur);
	}


	@Override
	public synchronized boolean hasUtilisateurEmprunteVelos(int idUtilisateur)
			throws RemoteException {
		Utilisateur utilisateur = lesUtilisateurs.get(idUtilisateur);
		return utilisateur.hasUtilisateurVelosEtat(metier.Velo.Etat.Emprunte);
	}
	
	@Override
	public synchronized boolean hasUtilisateurVeloEnReparation(int idUtilisateur)
			throws RemoteException {
		Utilisateur utilisateur = lesUtilisateurs.get(idUtilisateur);
		return utilisateur.hasUtilisateurVelosEtat(metier.Velo.Etat.EnReparation);
	}


	@Override
	public ArrayList<Integer> getIdsVelosEtat(int idUtilisateur, Etat etat)
			throws RemoteException {
		Utilisateur utilisateur = lesUtilisateurs.get(idUtilisateur);
		return utilisateur.getIdVelosEtat(etat);
	}


	@Override
	public void supprimerNotification() throws RemoteException {
		gestionStationNotif.supprimerNotification();
	}
	
	//notification
	public void gestionNotification(Station pStation) throws RemoteException {
		if(pStation.getLesVelos().size() <= SEUIL_ALERT_PENURIE)
		{
			gestionStationNotif.notificationPenurie(pStation);
		}
		if(pStation.getLesVelos().size() >= SEUIL_ALERT_SATUREE)
		{
			gestionStationNotif.notificationSaturee(pStation);
		}
	}
	
}
