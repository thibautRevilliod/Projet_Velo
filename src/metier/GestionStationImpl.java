package metier;

import java.util.HashMap;

public class GestionStationImpl implements GestionStation {
	
	@Override
	public void ajouterStation(Station station)
	{
		Station.lesStations.put(station.getIdStation(), station);
	}
	
	@Override
	public void supprimerStation(Station station)
	{
		Station.lesStations.remove(station.getIdStation());
	}
	
	@Override
	public int creerOperateur(String pnom, String pprenom, String pmotdepasse,
			String ptelephone, String padressemail, String padressepostale) {
		Operateur operateur = new Operateur( pnom,  pprenom, pmotdepasse, ptelephone,  padressemail, 
				 padressepostale);
		return operateur.getIdUtilisateur();
		
	}
	
	@Override
	public int creerAdministrateur(String pnom, String pprenom, String pmotdepasse,
			String ptelephone, String padressemail, String padressepostale) {
		Administrateur adminstrateur = new Administrateur( pnom,  pprenom, pmotdepasse, ptelephone,  padressemail, 
				 padressepostale);
		return adminstrateur.getIdUtilisateur();
		
	}
	
	@Override
	public int creerClient(String pnom, String pprenom, String pmotdepasse,
			String ptelephone, String padressemail, String padressepostale) {
		Client client = new Client( pnom, pprenom, pmotdepasse, ptelephone, padressemail, 
				 padressepostale);
		return client.getIdUtilisateur();
		
	}
	
	@Override
	public void ajouterVeloStation(Velo velo, Station station)
	{
		velo.setStation(station);
		station.ajouterVelo(velo);
	}
	
	@Override
	public void supprimerVeloStation(Velo velo, Station station)
	{
		velo.setStation(null);
		station.supprimerVelo(velo);
	}
	
	@Override
	public void transfererVelo(Velo velo, Station stationOrigine, Station stationDestination)
	{
		supprimerVeloStation(velo, stationOrigine);
		ajouterVeloStation(velo, stationDestination);
	}
	
	@Override
	public void ajouterUtilisateur (Utilisateur utilisateur)
	{
		utilisateur.lesUtilisateurs.put(utilisateur.getIdUtilisateur(), utilisateur);
	}
	
	@Override
	public void supprimerUtilisateur (Utilisateur utilisateur)
	{
		utilisateur.lesUtilisateurs.remove(utilisateur.getIdUtilisateur());
	}	
	
	@Override
	public void emprunterVeloClient(Client client, Velo velo, Station station)
	{
		client.setVelo(velo);
		station.lesVelos.remove(velo);
	}
	
	@Override
	public void ramenerVeloClient(Client client, Velo velo, Station station)
	{
		client.setVelo(null);
		station.lesVelos.add(velo);
	}

	

}
