package metier;

import java.util.HashMap;

public class GestionStationImpl {
	
	public void ajouterStation(Station station)
	{
		Station.lesStations.put(station.getIdStation(), station);
	}
	
	public void supprimerStation(Station station)
	{
		Station.lesStations.remove(station.getIdStation());
	}
	
	public void ajouterVeloStation(Velo velo, Station station)
	{
		station.ajouterVelo(velo);
	}
	
	public void supprimerVeloStation(Velo velo, Station station)
	{
		station.supprimerVelo(velo);
	}
	
	public void transfererVelo(Velo velo, Station stationOrigine, Station stationDestination)
	{
		supprimerVeloStation(velo, stationOrigine);
		ajouterVeloStation(velo, stationDestination);
	}
	
	public void ajouterUtilisateur (Utilisateur utilisateur)
	{
		utilisateur.lesUtilisateurs.put(utilisateur.getIdUtilisateur(), utilisateur);
	}
	
	public void supprimerUtilisateur (Utilisateur utilisateur)
	{
		utilisateur.lesUtilisateurs.remove(utilisateur.getIdUtilisateur());
	}	
	
	public void emprunterVeloClient(Client client, Velo velo, Station station)
	{
		client.setVelo(velo);
		station.lesVelos.remove(velo);
	}
	
	public void ramenerVeloClient(Client client, Velo velo, Station station)
	{
		client.setVelo(null);
		station.lesVelos.add(velo);
	}

}
