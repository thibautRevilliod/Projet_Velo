package metier;

import metier.CarteAcces.Role;
import metier.Velo.Etat;

public class Client extends Utilisateur {
	
	public Velo velo;

	public Client(String pnom, String pprenom, String pmotdepasse,
			String ptelephone, String padressemail, String padressepostale) {
		super(pnom, pprenom, pmotdepasse, ptelephone, padressemail, padressepostale);
		CarteAcces carteAcces = new CarteAcces(Role.Client);
		lesCartesAccesUtilisateur.put(carteAcces.getRole(),carteAcces);
		// TODO Auto-generated constructor stub
	}

	public Velo getVelo() {
		return velo;
	}

	public void setVelo(Velo velo) {
		this.velo = velo;
	}
	
	public int emprunterVelo(int idVelo, int idStation) throws java.rmi.RemoteException
	{	
		Station station = Station.getStation(idStation);
		Velo velo = station.getVeloStation(idVelo);
		if(velo != null)
		{
			velo.setEtat(Etat.EnReparation);
			this.setVelo(velo);
			station.supprimerVelo(velo);
			return velo.getIdVelo();
		}
		else
		{
			return -1;
		}		
	}
	
	public int deposerVelo(int idVelo, int idStation) throws java.rmi.RemoteException
	{	
		Station station = Station.getStation(idStation);
		Velo velo = station.getVeloStation(idVelo);
		if(velo != null)
		{
			if(station.getNombrePlacesDispos()>0)
			{
				velo.setEtat(Etat.Libre);
				this.setVelo(null);
				station.ajouterVelo(velo);
				return velo.getIdVelo();
			}
		}
		
		return -1;
				
	}

}
