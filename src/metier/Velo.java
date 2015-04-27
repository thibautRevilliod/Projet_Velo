package metier;

import java.util.HashMap;

public class Velo {
	
	public enum Etat {Emprunte, Libre, EnReparation};
	
	private int idVelo;
	private static HashMap<Integer, Velo> lesVelos = new HashMap<Integer, Velo>();
	private Etat etat;
	private Station station;
	private static int idsVelo = 0; 
	
	

	public Velo() {
		this.etat = Etat.Libre;
		idsVelo++;
		this.idVelo = idsVelo;
		lesVelos.put(idsVelo,this);
	}
	
	


	public int getIdVelo() {
		return idVelo;
	}




	public Station getStation() {
		return station;
	}



	public void setStation(Station station) {
		this.station = station;
	}


	public Etat getEtat() {
		return etat;
	}


	public void setEtat(Etat etat) {
		this.etat = etat;
	}
	
	public static boolean supprimerVelo(int idVelo)
	{
		if(lesVelos.containsKey(idVelo))
		{
			lesVelos.remove(idVelo);
			return true;
		}
		else
			return false;
	}

}
