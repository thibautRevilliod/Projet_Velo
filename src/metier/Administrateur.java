package metier;

import java.util.ArrayList;

import metier.CarteAcces.Role;
import metier.Velo.Etat;

public class Administrateur extends Utilisateur {
	
	public ArrayList<Velo> lesVelos = new ArrayList<Velo>();

	public Administrateur(String pnom, String pprenom, String pmotdepasse,
			String ptelephone, String padressemail, String padressepostale) {
		super(pnom, pprenom, pmotdepasse, ptelephone, padressemail, padressepostale);
		CarteAcces carteAcces = new CarteAcces(Role.Administrateur);
		lesCartesAccesUtilisateur.put(carteAcces.getRole(), carteAcces);
		// TODO Auto-generated constructor stub
	}
	
	public void ajouterVelo(Velo velo)
	{
		this.lesVelos.add(velo);
	}
	
	public void supprimerVelo(Velo velo)
	{
		this.lesVelos.remove(velo);
	}
	
	public int emprunterVelo(int idVelo, int idStation) throws java.rmi.RemoteException
	{	
		Station station = Station.getStation(idStation);
		Velo velo = station.getVeloStation(idVelo);
		if(velo != null)
		{
			velo.setEtat(Etat.EnReparation);
			this.ajouterVelo(velo);
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
				this.supprimerVelo(velo);
				station.ajouterVelo(velo);
				return velo.getIdVelo();
			}
			
		}

		return -1;
				
	}
}
