package metier;

import java.util.HashMap;

public class Velo {
	
	private int idVelo;
	public static HashMap<Integer, Velo> lesVelos = new HashMap<Integer, Velo>();
	public enum Etat {Emprunte, Libre, EnReparation};
	private Etat etat;
	private Station station;
	public static int idsVelo = 0; 
	
	

	public Velo(int idVelo) {
		this.etat = Etat.Libre;
		idsVelo++;
		this.idVelo = idsVelo;
		lesVelos.put(idsVelo,this);
		Station.lesVelos.add(this);
	}


	public Station getStation() {
		return station;
	}



	public void setStation(Station station) {
		this.station = station;
	}

}