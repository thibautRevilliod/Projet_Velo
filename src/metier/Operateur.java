package metier;

import java.util.HashMap;

import metier.CarteAcces.Role;
import metier.Velo.Etat;

public class Operateur extends Utilisateur {

	public Operateur(String pnom, String pprenom, String pmotdepasse,
			String ptelephone, String padressemail, String padressepostale) 
	{
		super(pnom, pprenom, pmotdepasse, ptelephone, padressemail, padressepostale);
		CarteAcces carteAcces = new CarteAcces(Role.Operateur);
		lesCartesAccesUtilisateur.put(carteAcces.getRole(),carteAcces);
	}
	
	public void emprunterVelos(int[] idVelos, Role roleEmprunt, int idStation, HashMap<Integer,Station> lesStationsGS) throws java.rmi.RemoteException
	{
		Station stationTemp = lesStationsGS.get(idStation);
		Velo veloTemp = null;
		int nbVelos = idVelos.length - 1; //La dernière occurence est l'idStation où se trouve le/les vélos
		if(roleEmprunt.equals(Role.Client))
		{
			if(stationTemp.hasVelosStation())
			{
				veloTemp = stationTemp.getLesVelos().get(idVelos[0]);
			}
//			veloTemp = Velo.getVelo(idVelos[0]);
			if(veloTemp != null && lesVelos.size() < 1)
			{
				veloTemp.setEtat(Etat.Emprunte);
				this.ajouterVelo(veloTemp);
			}
		}
		else if (roleEmprunt.equals(Role.Operateur))
		{
			for(int i = 0; i < nbVelos; i++)
			{
				veloTemp = stationTemp.getLesVelos().get(idVelos[i]);
//				veloTemp = Velo.getVelo(idVelos[i]);
				if(veloTemp != null)
				{
					veloTemp.setEtat(Etat.Emprunte);
					this.ajouterVelo(veloTemp);
				}	
			}
		}
	}
	
	@Override
	public boolean ajouterCarteAcces(CarteAcces carteAcces, Role role)
	{
		if(role.equals(Role.Client) || role.equals(Role.Operateur))
		{
			this.lesCartesAccesUtilisateur.put(role, carteAcces);
			return true;
		}
		else
			return false;
	}

}
