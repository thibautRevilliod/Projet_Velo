package metier;

import java.util.HashMap;

public class GestionStationImpl {
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
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
		utilisateur.lesUtilisateurs.put
	}

}
