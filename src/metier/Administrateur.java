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
	
	public void emprunterVelos(int[] idVelos, Role roleEmprunt, int idStation) throws java.rmi.RemoteException
	{
		Velo veloTemp; 
		//int nbVelos = idVelos.length - 1; //La dernière occurence est l'idStation où se trouve le/les vélos
		if(roleEmprunt.equals(Role.Client))
		{
			Station stationTemp = Station.getLesStations().get(idStation);
			 veloTemp = stationTemp.getLesVelos().get(idVelos[0]);
//			veloTemp = Velo.getVelo(idVelos[0]);
			if(veloTemp != null && lesVelos.size() < 1)
			{
				veloTemp.setEtat(Etat.Emprunte);
				this.ajouterVelo(veloTemp);
			}
		}
		else if (roleEmprunt.equals(Role.Administrateur))
		{
			Station stationTemp = Station.getLesStations().get(idStation);
			 veloTemp = stationTemp.getLesVelos().get(idVelos[0]);
//			veloTemp = Velo.getVelo(idVelos[0]);
			if(veloTemp != null)
			{
				veloTemp.setEtat(Etat.EnReparation);
				System.out.println("test");
//				this.setVelo(veloTemp);
				lesVelos.put(new Integer(veloTemp.getIdVelo()),veloTemp);
			}	
			
		}
	}
	
	@Override
	public boolean ajouterCarteAcces(CarteAcces carteAcces, Role role)
	{
		if(role.equals(Role.Client) || role.equals(Role.Administrateur))
		{
			this.lesCartesAccesUtilisateur.put(role, carteAcces);
			return true;
		}
		else
			return false;
	}
}
