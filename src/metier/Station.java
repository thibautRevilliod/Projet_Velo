package metier;

import java.util.ArrayList;
import java.util.HashMap;

public class Station {
	
	private int idStation;
	private String nomStation;
	private Position position;
	public static HashMap<Integer, Station> lesStations = new HashMap<Integer, Station>();
	public static ArrayList<Velo> lesVelos = new ArrayList<Velo>();
	public static int idsStation = 0; 
	

	public Station(int idStation, String nomStation, Position position) {		
		this.nomStation = nomStation;
		this.position = position;
		idsStation++;
		this.idStation = idsStation;
		lesStations.put(idsStation,this);
	}
	
	
	
	public int getIdStation() {
		return idStation;
	}



	public void setIdStation(int idStation) {
		this.idStation = idStation;
	}



	public String getNomStation() {
		return nomStation;
	}



	public void setNomStation(String nomStation) {
		this.nomStation = nomStation;
	}



	public Position getPosition() {
		return position;
	}



	public void setPosition(Position position) {
		this.position = position;
	}



	public void ajouterVelo(Velo velo)
	{
		this.lesVelos.add(velo);
	}
	
	public void supprimerVelo(Velo velo)
	{
		this.lesVelos.remove(velo);
	}
	



}
