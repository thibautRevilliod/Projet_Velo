package metier;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
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
	
	public GestionStationNotif notification;

	
	public GestionStationImpl() throws RemoteException {
		super();
	}
	
	
	public HashMap<Integer, Station> getLesStations() {return lesStations;}

	public HashMap<Integer, Utilisateur> getLesUtilisateurs() {return lesUtilisateurs;}



	@Override
	public GestionStationNotif getNotification() {
		return this.notification;
	}
	
	@Override
	public void setNotification(GestionStationNotif notification) {
		this.notification = notification;
	}

	@Override
	public int creerStation(String nomStation, double longitude, double latitude, int capacite) throws java.rmi.RemoteException
	{
		Position position = new Position(longitude, latitude);
		Station nouvelleStation = new Station(nomStation, position, capacite);
		int idStation = nouvelleStation.getIdStation();
		lesStations.put(Integer.valueOf(idStation), nouvelleStation);
		return idStation;
	}
	
	@Override
	public synchronized void supprimerStation(int idStation) throws RemoteException
	{
		if(Station.supprimerStation(idStation))
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
				lesUtilisateurs.put(Integer.valueOf(idUtilisateur), client);
				break;
			case "Operateur" :
				Operateur operateur = new Operateur( pnom,  pprenom, pmotdepasse, ptelephone,  padressemail, padressepostale);
				idUtilisateur = operateur.getIdUtilisateur();
				lesUtilisateurs.put(Integer.valueOf(idUtilisateur), operateur);
				break;
			case "Administrateur" :
				Administrateur administrateur = new Administrateur( pnom,  pprenom, pmotdepasse, ptelephone,  padressemail, padressepostale);
				idUtilisateur = administrateur.getIdUtilisateur();
				lesUtilisateurs.put(Integer.valueOf(idUtilisateur), administrateur);
				break;
			default:
				idUtilisateur = -1; // idUtilisateur non ok 
				break;
		}
		return idUtilisateur;
	    
	}
	
	@Override
	public synchronized boolean ajouterVeloStation(Velo velo, int idStation) throws RemoteException
	{
		boolean result;
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
	public int isEmprunterVelosPossible(int idUtilisateur, int idStation,
			int nbVelos, Role roleEmprunt) throws RemoteException {
		int resultat = 0;
		Utilisateur utilisateur;
		Station station;
		int[] lesIdVelos = new int[nbVelos + 1]; //On rajoute un emplacement qui contiendra l'idStation des v�los
		if(Utilisateur.getLesUtilisateurs().containsKey(idUtilisateur))
		{
			if(Station.getLesStations().containsKey(idStation))
			{
				station = Station.getLesStations().get(idStation);
				if(station.getCapacite()>=nbVelos)
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
	
	// � modifier
	@Override
	public int[] emprunterVelos(int idUtilisateur, int idStation, int nbVelos, Role roleEmprunt) throws RemoteException
	{
		Utilisateur utilisateur;
		Station station;
		int[] lesIdVelos = new int[nbVelos + 1]; //On rajoute un emplacement qui contiendra l'idStation des v�los
		
		utilisateur = Utilisateur.getLesUtilisateurs().get(idUtilisateur);
		
		station = Station.getLesStations().get(idStation);
		//M�thode qui retourne les v�los de la station (ou de la plus proche) avec idStation
		lesIdVelos = station.getVelosLibresStation(nbVelos);
		//On teste que la m�thode pr�c�dente ne retourne pas un idStation diff�rent
		
		//On assigne les v�los � l'utilisateur, en sp�cifiant son r�le pour traitements sp�cifiques
		utilisateur.emprunterVelos(lesIdVelos, roleEmprunt);
		//Suppression des v�los de la station
		station.supprimerVelos(lesIdVelos);
				
			

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
			//On v�rifie les droits de l'utilisateur
			if(utilisateur.hasRole(Role.Administrateur))
			{
				station = Station.getLesStations().get(idStation);
				velo = station.getVeloStation(idVelo);
				//On v�rifie que le v�lo choisi soit libre
				if(velo != null && velo.getEtat().equals(Etat.Libre))
				{
					//On assigne les v�los � l'utilisateur, en sp�cifiant son r�le pour traitements sp�cifiques
					utilisateur.emprunterVelos(lesIdVelos, Role.Administrateur);
					//Suppression du v�lo de la station
					station.supprimerVelo(velo);
					lesIdVelos[1] = 0;//Tout s'est bien pass� : code 0
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
		System.out.println("MaGestionStation est enregistr�e");
	}

	@Override
	public int[] dureePrixEmpruntVeloClient(int idUtilisateur, int idStation) throws RemoteException {
		// TODO code dureePrixEmpruntVeloClient method
		return null;
	}

	@Override
	public boolean gestionStationHasMaitre(int idStation) throws RemoteException {
		return Station.getStation(idStation).isEstMaitre();
	}
	
	public synchronized void ajouterRoleUtilisateur(int idUtilisateur, Role role) throws RemoteException
	{
		Utilisateur user = Utilisateur.getLesUtilisateurs().get(idUtilisateur);
		user.ajouterRoleUtilisateur(role);
	}

	@Override
	public int[] getVelosLibresStation(int idStation) throws RemoteException 
	{
		Station station = Station.getStation(idStation);
		return station.getVelosLibresStation(station.getNombreVelosLibres());
	}

	public HashMap<Integer, Station> getLesStationsGS() throws RemoteException
	{
		return Station.getLesStations();
	}
	
	public HashMap<Integer, Utilisateur> getLesUtilisateursGS() throws RemoteException
	{
		return Utilisateur.getLesUtilisateurs();
	}
	

}
