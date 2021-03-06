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
	private HashMap<Integer, Station> lesStations = new HashMap<Integer, Station>(); //On g�re ici la liste de toutes les stations, qui elles-m�me g�rent les v�los
	private HashMap<Integer, Utilisateur> lesUtilisateurs= new HashMap<Integer, Utilisateur>(); //On g�re ici la liste de tous les utilisateurs
	
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
		result = station.ajouterVelo(velo);
		
		return result;
	}
	
	@Override
	public synchronized void supprimerVeloStation(Velo velo, int idStation) throws RemoteException
	{
		Station station = Station.getLesStations().get(idStation);
		station.supprimerVelo(velo);
	}
	
	@Override
	public synchronized int isEmprunterVelosPossible(int idUtilisateur, int idStation,
			int nbVelos, Role roleEmprunt) throws RemoteException {
		int resultat = 0;
		Utilisateur utilisateur;
		Station station;
		int[] lesIdVelos = new int[nbVelos + 1]; //On rajoute un emplacement qui contiendra l'idStation des v�los
		if(Utilisateur.getLesUtilisateurs().containsKey(idUtilisateur)) // l'utilisateur existe
		{
			if(Station.getLesStations().containsKey(idStation)) // la station existe
			{
				station = Station.getLesStations().get(idStation);
				if(station.getCapacite() >= nbVelos)
				{
						utilisateur = Utilisateur.getLesUtilisateurs().get(idUtilisateur);
						//On v�rifie les droits de l'utilisateur
						
						if(utilisateur.hasRole(roleEmprunt))
						{
							if((nbVelos > 1 && roleEmprunt == Role.Operateur) || (nbVelos == 1))
							{
								//M�thode qui retourne les v�los de la station (ou de la plus proche) avec idStation
								lesIdVelos = station.getVelosLibresStation(nbVelos);
								//On teste que la m�thode pr�c�dente ne retourne pas un idStation diff�rent
								if(lesIdVelos[nbVelos] == idStation)
								{
									resultat = 0; // pas d'erreur : l'emprunt est possible
								}
								else if(lesIdVelos[nbVelos] == -6)
								{
									resultat = lesIdVelos[nbVelos]; // pas de station plus proche avec assez de v�los
								}
								else
								{
									resultat = lesIdVelos[nbVelos]; // idSTation de la station la plus proche
								}
							}
							else
							{
								resultat = -2; //Erreur : l'utilisateur n'a pas le bon r�le
							}
						}
				}
				else
				{
					resultat = -4; //Erreur : la capacit� de la station est inf�rieur au nombre de v�los souhait�s
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
	
	// 
	@Override
	public synchronized int[] emprunterVelos(int idUtilisateur, int idStation, int nbVelos, Role roleEmprunt) throws RemoteException
	{
		Utilisateur utilisateur;
		Station station;
		int[] lesIdVelos = new int[nbVelos + 1]; //On rajoute un emplacement qui contiendra l'idStation des v�los
		
		utilisateur = Utilisateur.getLesUtilisateurs().get(idUtilisateur);
		
		station = lesStations.get(idStation);
		//M�thode qui retourne les v�los de la station (ou de la plus proche) avec idStation
		lesIdVelos = station.getVelosLibresStation(nbVelos);
		//On teste que la m�thode pr�c�dente ne retourne pas un idStation diff�rent
		//On assigne les v�los � l'utilisateur, en sp�cifiant son r�le pour traitements sp�cifiques
		utilisateur.emprunterVelos(lesIdVelos, roleEmprunt, idStation, lesStations);
		//Suppression des v�los de la station
		station.supprimerVelos(lesIdVelos);
			
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
			//On v�rifie les droits de l'utilisateur
			if(utilisateur.hasRole(Role.Administrateur))
			{
				station = Station.getLesStations().get(idStation);
				velo = station.getVeloStation(idVelo);
				//On v�rifie que le v�lo choisi soit libre
				if(velo != null && velo.getEtat().equals(Etat.Libre))
				{
					//On assigne les v�los � l'utilisateur, en sp�cifiant son r�le pour traitements sp�cifiques
					utilisateur.emprunterVelos(lesIdVelos, Role.Administrateur, idStation, lesStations);
					//Suppression du v�lo de la station
					station.supprimerVelo(velo);
					lesIdVelos[1] = 0;//Tout s'est bien pass� : code 0
					
					//notification
					gestionNotification(station);
				}
				else
				{
					lesIdVelos[1] = -3; //Erreur : V�lo inexistant ou non libre
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
	
	// utilis� par un client ou un op�rateur
	@Override
	public synchronized int deposerVelos(int idUtilisateur, int idStation) throws RemoteException
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
				idStationDepot = station.getStationLaPlusProchePourSaturee(nbVelos, new HashMap<Integer,Station>(lesStations));
		}
		return idStationDepot;
	}
	
	// utilis� par l'administrateur pour d�poser un v�lo pr�c�demment en maintenance
	@Override
	public synchronized int deposerVelos(int idUtilisateur, int idStation, int idVelo) throws RemoteException
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
				idStationDepot = station.getStationLaPlusProchePourSaturee(1, new HashMap<Integer,Station>(lesStations));
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
		System.out.println("MaGestionStation est enregistr�e");
		GestionStationNotifImpl gestionStationNotifImpl = new GestionStationNotifImpl();
		System.out.println("MaGestionStationNotifImpl est enregistr�e");
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
	public synchronized int[] getVelosLibresStation(int idStation) throws RemoteException 
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
