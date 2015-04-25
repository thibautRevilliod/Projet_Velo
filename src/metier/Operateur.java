package metier;

import java.util.ArrayList;

import metier.CarteAcces.Role;
import metier.Velo.Etat;

public class Operateur extends Utilisateur {
	
	public ArrayList<Velo> lesVelos = new ArrayList<Velo>();

	public Operateur(String pnom, String pprenom, String pmotdepasse,
			String ptelephone, String padressemail, String padressepostale) {
		super(pnom, pprenom, pmotdepasse, ptelephone, padressemail, padressepostale);
		CarteAcces carteAcces = new CarteAcces(Role.Operateur);
		lesCartesAccesUtilisateur.put(carteAcces.getRole(),carteAcces);
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
	
	
	public int[] emprunterVelos(int idStation) throws java.rmi.RemoteException
	{
		Station station = Station.getStation(idStation);
		int[] listeIdsVelosEmpruntes = new int[station.lesVelos.size()];
				
		if(station != null)
		{
			listeIdsVelosEmpruntes = station.getVelosLibresStation();
			int veloId;
			Velo velo;

			for(int i=0; i<listeIdsVelosEmpruntes.length;i++)
			{
				veloId = listeIdsVelosEmpruntes[i];
				velo = station.getVeloStation(veloId);
				velo.setEtat(Etat.Emprunte);
				this.ajouterVelo(velo);
				station.supprimerVelo(velo);
			}
		}

		return listeIdsVelosEmpruntes;
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
