package metier;

public interface GestionStation {
	public void ajouterStation(Station station);
	
	public void supprimerStation(Station station);
	
	public void ajouterVeloStation(Velo velo, Station station);
	
	public void supprimerVeloStation(Velo velo, Station station);
	
	public void transfererVelo(Velo velo, Station stationOrigine, Station stationDestination);
	
	public void ajouterUtilisateur (Utilisateur utilisateur);
	
	public void supprimerUtilisateur (Utilisateur utilisateur);
	
	public void emprunterVeloClient(Client client, Velo velo, Station station);
	
	public void ramenerVeloClient(Client client, Velo velo, Station station);
}
