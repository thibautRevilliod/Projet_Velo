package metier;

import java.util.HashMap;

public class Velo {
	
	private int idVelo;
	public static HashMap<Integer, Velo> lesVelos = new HashMap<Integer, Velo>();
	public enum Etat {Emprunte, Libre, EnReparation};
	private Etat etatVelo;
	private Station station;
	public static int idsVelo = 0; 
	
	

	public Velo() {
		this.etatVelo = Etat.Libre;
		idsVelo++;
		this.idVelo = idsVelo;
		lesVelos.put(idsVelo,this);
		station.lesVelos.add(this);
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
		return etatVelo;
	}


	public void setEtat(Etat etat) {
		this.etatVelo = etat;
	}
	
	

}
