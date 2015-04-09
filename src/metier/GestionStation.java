package metier;

public interface GestionStation {
	public void ajouterStation(Station station);
	
	public void supprimerStation(Station station);
	
	public int creerOperateur(String pnom, String pprenom, String pmotdepasse, String ptelephone, String padressemail, 
			String padressepostale);

	public int creerAdministrateur(String pnom, String pprenom, String pmotdepasse, String ptelephone, String padressemail, 
			String padressepostale);
	
	public int creerClient(String pnom, String pprenom, String pmotdepasse, String ptelephone, String padressemail, 
			String padressepostale);
	
	public void ajouterVeloStation(Velo velo, Station station);
	
	public void supprimerVeloStation(Velo velo, Station station);
	
	public void transfererVelo(Velo velo, Station stationOrigine, Station stationDestination);
	
	public void ajouterUtilisateur (Utilisateur utilisateur);
	
	public void supprimerUtilisateur (Utilisateur utilisateur);
	
	public void emprunterVeloClient(Client client, Velo velo, Station station);
	
	public void ramenerVeloClient(Client client, Velo velo, Station station);
}
