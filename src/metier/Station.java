package metier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Station {
	
	private int idStation;
	private String nomStation;
	private Position position;
	private static HashMap<Integer, Station> lesStations = new HashMap<Integer, Station>();
	private ArrayList<Velo> lesVelos ;
	private static int idsStation = 0; 
	

	public Station(String nomStation, Position position) {		
		this.nomStation = nomStation;
		this.position = position;
		idsStation++;
		this.idStation = idsStation;
		lesStations.put(idsStation,this);
		this.lesVelos = new ArrayList<Velo>();
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
	
	public boolean supprimerVelo(Velo velo)
	{
		return this.lesVelos.remove(velo);
	}
	
	public static Station getStation(int idStation)
	{
		Iterator it = lesStations.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        Station stationListe = (Station) pair.getValue();
	        
	        if(stationListe.getIdStation() == idStation)
	        {
	        	return stationListe;
	        }
	    }
	    
	    return null;
	}
	
	public Velo getVeloStation(int idVelo)
	{
		for(int i = 0; i < lesVelos.size(); i++){
		    Velo velo = lesVelos.get(i);
		    
		    if(velo.getIdVelo() == idVelo)
		    {
		    	return velo;
		    }
		}
		
		return null;
	}
	
	
	public Station getStationLaPlusProche()
	{
		Station stationPlusProche = null;
		double distanceMinimale = 10000; 
		double distanceCalculee = 0; 
		
		Iterator it = lesStations.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        Integer idStation = (Integer) pair.getKey();
	        Station station = (Station) pair.getValue();
	        
	        distanceCalculee = Distance.distanceInKilometers(this.getPosition().getLatitude(), this.getPosition().getLongitude(), station.getPosition().getLatitude(), station.getPosition().getLongitude());
	        if(distanceCalculee<distanceMinimale)
	        {
	        	distanceMinimale=distanceCalculee;
	        	stationPlusProche=station;
	        }	        
	        
	        it.remove(); // avoids a ConcurrentModificationException
	    }
		
		return stationPlusProche;
	}
	
	public static boolean supprimerStation(int idStation)
	{
		Station stationTemp = lesStations.get(idStation);
		int idVeloTemp;
		//Suppression de tous les vélos de cette station
		for(int i = 0; i < stationTemp.lesVelos.size(); i++)
		{
			idVeloTemp = stationTemp.lesVelos.get(i).getIdVelo();
			Velo.supprimerVelo(idVeloTemp);
			stationTemp.lesVelos.remove(i);
		}
		lesStations.remove(stationTemp);
		return true;
	}
	



}
