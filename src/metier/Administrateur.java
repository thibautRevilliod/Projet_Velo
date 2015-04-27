package metier;

import metier.CarteAcces.Role;
import metier.Velo.Etat;

public class Administrateur extends Utilisateur {

	public Administrateur(String pnom, String pprenom, String pmotdepasse,
			String ptelephone, String padressemail, String padressepostale) 
	{
		super(pnom, pprenom, pmotdepasse, ptelephone, padressemail, padressepostale);
		CarteAcces carteAcces = new CarteAcces(Role.Administrateur);
		lesCartesAccesUtilisateur.put(carteAcces.getRole(), carteAcces);
	}
	
	
	public int emprunterVelo(int identifiant, int idVelo, int idStation) throws java.rmi.RemoteException
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
}
