package metier;

import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

import metier.Velo.Etat;

public class GestionStationImpl extends UnicastRemoteObject implements GestionStation {
	public GestionStationImpl() throws RemoteException {
		super();
	}

	@Override
	public synchronized void ajouterStation(Station station) throws RemoteException
	{
		Station.lesStations.put(station.getIdStation(), station);
	}
	
	@Override
	public synchronized void supprimerStation(Station station) throws RemoteException
	{
		Station.lesStations.remove(station.getIdStation());
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
	public synchronized void ajouterVeloStation(Velo velo, Station station) throws RemoteException
	{
		velo.setStation(station);
		station.ajouterVelo(velo);
	}
	
	@Override
	public synchronized void supprimerVeloStation(Velo velo, Station station) throws RemoteException
	{
		velo.setStation(null);
		station.supprimerVelo(velo);
	}
	
	@Override
	public synchronized void transfererVelo(Velo velo, Station stationOrigine, Station stationDestination) throws RemoteException
	{
		supprimerVeloStation(velo, stationOrigine);
		ajouterVeloStation(velo, stationDestination);
	}
	
	@Override
	public synchronized void ajouterUtilisateur (Utilisateur utilisateur) throws RemoteException
	{
		utilisateur.lesUtilisateurs.put(utilisateur.getIdUtilisateur(), utilisateur);
	}
	
	@Override
	public synchronized void supprimerUtilisateur (Utilisateur utilisateur) throws RemoteException
	{
		utilisateur.lesUtilisateurs.remove(utilisateur.getIdUtilisateur());
	}	
	
	@Override
	public synchronized void emprunterVeloClient(Client client, Velo velo, Station station) throws RemoteException
	{
		velo.setEtat(Etat.Emprunte);
		client.setVelo(velo);
		station.lesVelos.remove(velo);
	}
	
	@Override
	public synchronized void ramenerVeloClient(Client client, Velo velo, Station station) throws RemoteException
	{
		client.setVelo(null);
		station.lesVelos.add(velo);
	}
	
	@Override
	public synchronized boolean estUtilisateurIdentifie(int identifiant, String motDePasse) throws RemoteException
	{
		Utilisateur utilisateur;
		
		if (Utilisateur.lesUtilisateurs.containsKey(identifiant))
		{
			utilisateur = Utilisateur.lesUtilisateurs.get(identifiant);
			return utilisateur.getIdUtilisateur()==identifiant && utilisateur.getMotDePasse().equals(motDePasse);
		}
		else
		{
			return false;
		}
		
	}
	
	public synchronized Station chercherStationLaPlusProche(Station stationActuelle) throws java.rmi.RemoteException
	{
		return stationActuelle.getStationLaPlusProche();
	}
	
	public synchronized String[] getRoleUtilisateur(int identifiant) throws java.rmi.RemoteException
	{
		Utilisateur utilisateur;
		
		if (Utilisateur.lesUtilisateurs.containsKey(identifiant))
		{
			utilisateur = Utilisateur.lesUtilisateurs.get(identifiant);
			return utilisateur.getRoles();
		}
		
		return null;
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
