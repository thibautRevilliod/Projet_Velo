package metier;

import metier.CarteAcces.Role;
import metier.Velo.Etat;

public class Client extends Utilisateur {
	
	public Client(String pnom, String pprenom, String pmotdepasse,
			String ptelephone, String padressemail, String padressepostale) 
	{
		super(pnom, pprenom, pmotdepasse, ptelephone, padressemail, padressepostale);
		CarteAcces carteAcces = new CarteAcces(Role.Client);
		lesCartesAccesUtilisateur.put(carteAcces.getRole(),carteAcces);
	}
	
	public void emprunterVelos(int[] idVelos, Role roleEmprunt, int idStation) throws java.rmi.RemoteException
	{
		Station stationTemp = Station.getLesStations().get(idStation);
		Velo veloTemp = stationTemp.getLesVelos().get(idVelos[0]);
//		Velo veloTemp = Velo.getVelo(idVelos[0]);
		if(veloTemp != null && lesVelos.size() < 1)
		{
			veloTemp.setEtat(Etat.Emprunte);
//			this.setVelo(veloTemp);
			lesVelos.put(new Integer(veloTemp.getIdVelo()),veloTemp);
		}	
	}
	
	@Override
	public boolean ajouterCarteAcces(CarteAcces carteAcces, Role role)
	{
		if(role.equals(Role.Client))
		{
			this.lesCartesAccesUtilisateur.put(role, carteAcces);
			return true;
		}
		else
			return false;
	}

}
