package metier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import metier.Velo.Etat;

public class Station {
	
	private int idStation;
	private String nomStation;
	private Position position;
	private int capacite;
	public static HashMap<Integer, Station> lesStations = new HashMap<Integer, Station>();
	public ArrayList<Velo> lesVelos = new ArrayList<Velo>();
	public static int idsStation = 0; 
	

	public Station(String nomStation, double longitude, double latitude, int capacite) {		
		this.nomStation = nomStation;
		this.position = new Position(longitude, latitude);
		this.capacite = capacite;
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
		if(this.lesVelos.contains(velo))
		{
			this.lesVelos.remove(velo);
		}
	}
	
	public int getNombrePlacesDispos()
	{
		return this.capacite - this.lesVelos.size();
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
	
	public int[] getVelosLibresStation()
	{
		int[] listeIdsVelosLibres = new int[lesVelos.size()];
		int j = 0;
		
		for(int i = 0; i < lesVelos.size(); i++){
		    Velo velo = lesVelos.get(i);
		    
		    if(velo.getEtat() == Etat.Libre)
		    {
		    	listeIdsVelosLibres[j] = velo.getIdVelo();
		    	j++;
		    }
		}
		
		return listeIdsVelosLibres;
		
	}
	
	public int chercherVeloLibreStation()
	{
		for(int i = 0; i < lesVelos.size(); i++){
		    Velo velo = lesVelos.get(i);
		    
		    if(velo.getEtat() == Etat.Libre)
		    {
		    	return velo.getIdVelo();
		    }
		}
		
		return -1; // si aucun velo libre trouvé
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
	
	public static Station getStationSaturee()
	{
		Station stationSatureee = null;
		Iterator it = lesStations.entrySet().iterator();
		
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        Integer idStation = (Integer) pair.getKey();
	        Station stationListe = (Station) pair.getValue();
	        stationSatureee = stationListe;
	        
	        if(stationListe.capacite > stationSatureee.capacite)
	        {
	        	stationSatureee = stationListe;
	        }

	        
	        it.remove(); // avoids a ConcurrentModificationException
	    }
	    
	    return stationSatureee;
	}
	
	public static Station getStationEnPenurie()
	{
		Station stationEnPenurie = null;
		Iterator it = lesStations.entrySet().iterator();
		
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        Integer idStation = (Integer) pair.getKey();
	        Station stationListe = (Station) pair.getValue();
	        stationEnPenurie = stationListe;
	        
	        if(stationListe.capacite == 0)
	        {
	        	stationEnPenurie = stationListe;
	        }

	        
	        it.remove(); // avoids a ConcurrentModificationException
	    }
	    
	    return stationEnPenurie;
	}
	



}
