package metier;

import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.HashMap;

public class GestionStationImpl implements GestionStation {
	
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
		client.setVelo(velo);
		station.lesVelos.remove(velo);
	}
	
	@Override
	public synchronized void ramenerVeloClient(Client client, Velo velo, Station station) throws RemoteException
	{
		client.setVelo(null);
		station.lesVelos.add(velo);
	}

	public synchronized static void main(String[] args) throws Exception {
		LocateRegistry.createRegistry(1099);
		Naming.rebind("MaGestionStation",new GestionStationImpl());
		System. out.println("MaGestionStation est enregistrée");
	}

}
