package metier;

import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;

import metier.Velo.Etat;

public class GestionStationImpl extends UnicastRemoteObject implements GestionStation {
	
	private HashMap<Integer, Station> lesStations; //On gère ici la liste de toutes les stations, qui elles-mêmes gèrent les vélos
	
	public GestionStationImpl() throws RemoteException {
		super();
		lesStations = new HashMap<Integer, Station>();
	}

	@Override
	public synchronized int ajouterStation(String nomStation, double longitude, double latitude) throws RemoteException
	{
		Position position = new Position(longitude, latitude);
		Station nouvelleStation = new Station(nomStation, position);
		int idStation = nouvelleStation.getIdStation();
		lesStations.put(idStation, nouvelleStation);
		return idStation;
	}
	
	@Override
	public synchronized void supprimerStation(int idStation) throws RemoteException
	{
		if(Station.supprimerStation(idStation))
			lesStations.remove(idStation);
	}
	
	@Override
	public synchronized int creerUtilisateur(String pnom, String pprenom, String pmotdepasse, String ptelephone, String padressemail, String padressepostale, String objectClass) throws RemoteException 
	{
		Class myClass;
		int idUtilisateur = -1; // idUtilisateur non ok 
		try {
			myClass = Class.forName(objectClass);
			if (myClass.isInstance(Administrateur.class)) {
				idUtilisateur = creerAdministrateur(pnom, pprenom, pmotdepasse, ptelephone, padressemail, padressepostale);
		    }
			else if(myClass.isInstance(Operateur.class)) {
				idUtilisateur = creerOperateur(pnom, pprenom, pmotdepasse, ptelephone, padressemail, padressepostale);		    
			}
			else if(myClass.isInstance(Client.class)) {
				idUtilisateur = creerClient(pnom, pprenom, pmotdepasse, ptelephone, padressemail, padressepostale);		    
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return idUtilisateur;
	    
	}
	
	@Override
	public synchronized int creerOperateur(String pnom, String pprenom, String pmotdepasse, String ptelephone, String padressemail, String padressepostale) throws RemoteException 
	{
		Operateur operateur = new Operateur( pnom,  pprenom, pmotdepasse, ptelephone,  padressemail, padressepostale);
		return operateur.getIdUtilisateur();
	}
	
	@Override
	public synchronized int creerAdministrateur(String pnom, String pprenom, String pmotdepasse, String ptelephone, String padressemail, String padressepostale) throws RemoteException 
	{
		Administrateur adminstrateur = new Administrateur( pnom,  pprenom, pmotdepasse, ptelephone,  padressemail, padressepostale);
		return adminstrateur.getIdUtilisateur();
	}
	
	@Override
	public synchronized int creerClient(String pnom, String pprenom, String pmotdepasse, String ptelephone, String padressemail, String padressepostale) throws RemoteException 
	{
		Client client = new Client( pnom, pprenom, pmotdepasse, ptelephone, padressemail, padressepostale);
		return client.getIdUtilisateur();
	}
	
	@Override
	public synchronized void ajouterVeloStation(Velo velo, int idStation) throws RemoteException
	{
		Station station = lesStations.get(idStation);
		velo.setStation(station);
		station.ajouterVelo(velo);
	}
	
	@Override
	public synchronized void supprimerVeloStation(Velo velo, int idStation) throws RemoteException
	{
		Station station = lesStations.get(idStation);
		velo.setStation(null);
		station.supprimerVelo(velo);
	}
	
	@Override
	public synchronized void transfererVelo(Velo velo, int idStationOrigine, int idStationDestination) throws RemoteException
	{
		supprimerVeloStation(velo, idStationOrigine);
		ajouterVeloStation(velo, idStationDestination);
	}
	
	@Override
	public synchronized void ajouterUtilisateur (Utilisateur utilisateur) throws RemoteException
	{
		Utilisateur.ajouterUtilisateur(utilisateur);
	}
	
	@Override
	public synchronized void supprimerUtilisateur (Utilisateur utilisateur) throws RemoteException
	{
		Utilisateur.supprimerUtilisateur(utilisateur);
	}	
	
	@Override
	public synchronized void emprunterVeloClient(Client client, Velo velo, Station station) throws RemoteException
	{
		velo.setEtat(Etat.Emprunte);
		client.setVelo(velo);
		station.supprimerVelo(velo);
	}
	
	@Override
	public synchronized void ramenerVeloClient(Client client, Velo velo, Station station) throws RemoteException
	{
		client.setVelo(null);
		station.ajouterVelo(velo);
	}
	
	@Override
	public synchronized boolean estUtilisateurIdentifie(int identifiant, String motDePasse) throws RemoteException
	{
		return Utilisateur.estUtilisateurIdentifie(identifiant, motDePasse);
	}
	
	public synchronized Station chercherStationLaPlusProche(Station stationActuelle) throws java.rmi.RemoteException
	{
		return stationActuelle.getStationLaPlusProche();
	}
	
	public synchronized String[] getRoleUtilisateur(int identifiant) throws java.rmi.RemoteException
	{
		return Utilisateur.getRoles(identifiant);
	}
	
	public int emprunterVeloAdministrateur(int identifiant, int idVelo, int idStation) throws java.rmi.RemoteException
	{	
		Station station = Station.getStation(idStation);
		Velo velo = station.getVeloStation(idVelo);
		if(velo != null)
		{
			velo.setEtat(Etat.EnReparation);
			station.supprimerVelo(velo);
			return velo.getIdVelo();
		}
		else
		{
			return -1;
		}		
	}
	
	public int deposerVeloAdministrateur(int identifiant, int idVelo, int idStation) throws java.rmi.RemoteException
	{
		Station station = Station.getStation(idStation);
		Velo velo = station.getVeloStation(idVelo);
		if(velo != null)
		{
			velo.setEtat(Etat.Libre);
			station.ajouterVelo(velo);
			return velo.getIdVelo();
		}
		else
		{
			return -1;
		}		
	}
	
	
	

	public synchronized static void main(String[] args) throws Exception {
		LocateRegistry.createRegistry(1099);
		Naming.rebind("MaGestionStation",new GestionStationImpl());
		System.out.println("MaGestionStation est enregistrée");
	}

}
