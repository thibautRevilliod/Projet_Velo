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
	private static HashMap<Integer, Station> lesStations;
	private HashMap<Integer, Velo> lesVelos ;
	private static int idsStation = 0; 
	private boolean estMaitre = false;
	
	public boolean isEstMaitre() {
		return estMaitre;
	}


	public Station(String nomStation, Position position, int capacite) 
	{						
		this.nomStation = nomStation;
		this.position = position;
		this.capacite = capacite;
		idsStation++;
		this.idStation = idsStation;
		if(lesStations == null)
		{
			lesStations = new HashMap<Integer, Station>();	
			this.estMaitre = !hasMaitre();
		}
		Station.lesStations.put(new Integer(idsStation),this);
		this.lesVelos = new HashMap<Integer, Velo>();
		System.out.println("Test : "+lesStations.toString());

	}
	
	
	public int getIdStation() {return idStation;}
	public void setIdStation(int idStation) {this.idStation = idStation;}
	public String getNomStation() {return nomStation;}
	public void setNomStation(String nomStation) {this.nomStation = nomStation;}
	public Position getPosition() {return position;}
	public void setPosition(Position position) {this.position = position;}
	public int getCapacite() {return capacite;}
	public void setCapacite(int capacite) {this.capacite = capacite;}
	public HashMap<Integer, Velo> getLesVelos() {return lesVelos;}
	public void setLesVelos(HashMap<Integer, Velo> lesVelos) {this.lesVelos = lesVelos;}
	public boolean EstMaitre() {return estMaitre;}
	public void setEstMaitre(boolean estMaitre) {this.estMaitre = estMaitre;}
	public static HashMap<Integer, Station> getLesStations() {return lesStations;}


	public int getNombrePlacesDispos()
	{
		return this.capacite - this.lesVelos.size();
	}
	
	public int getNombreVelosLibres()
	{
		int nombreVelosLibres = 0;
	
		Iterator it = lesVelos.entrySet().iterator();
		
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        Velo veloListe = (Velo) pair.getValue();
	        if(veloListe.getEtat() == Etat.Libre)
		    {
	        	nombreVelosLibres++;
		    }
		}
		
		return nombreVelosLibres;
	}
	
	public boolean ajouterVelo(Velo velo)
	{
		if(getNombrePlacesDispos() == 0 )
			return false;
		else
			lesVelos.put(new Integer(velo.getIdVelo()),velo);
			return true;
	}
	
	public void ajouterVelos(int[] idsVelos)
	{
		Velo veloTemp;
		int i = 0, nbVelos = idsVelos.length;
		while(i < nbVelos)
		{
			veloTemp = lesVelos.get(idsVelos[i]); //TODO pourquoi récupère-t-on les Velo dans cette HashMap ?
			ajouterVelo(veloTemp);
			i++;
		}
	}
	
	public boolean supprimerVelo(Velo velo)
	{
		if(lesVelos.containsKey(velo.getIdVelo()))
		{
			lesVelos.remove(velo.getIdVelo());
			return true;
		}
		else
			return false;
	}
	
	public boolean supprimerVelos(int[] idsVelos)
	{
		Velo veloTemp;
		boolean suppOk = true;
		int i = 0, nbVelos = idsVelos.length -1;//La dernière occurence est l'idStation où se trouve le/les vélos
		while(i < nbVelos && suppOk)
		{
			veloTemp = lesVelos.get(idsVelos[i]);
			if(!supprimerVelo(veloTemp))
				suppOk = false;
			i++;
		}
		return suppOk;
	}
	
//	public static Station getStation(int idStation)
//	{
//		if(lesStations.containsKey(idStation))
//			return lesStations.get(idStation);
//		else
//			return null;
//	}
	
	public Velo getVeloStation(int idVelo)
	{
		return lesVelos.get(idVelo);	
	}
	
	public int[] getVelosLibresStation(int nbVelos)
	{
		int nbVelosNecessaires = nbVelos;
		int[] listeIdsVelosLibres = new int[nbVelos + 1];
		int j = 0;
		
		Iterator it = lesVelos.entrySet().iterator();
		
	    while (it.hasNext() && j < nbVelosNecessaires) {
	        Map.Entry pair = (Map.Entry)it.next();
	        Integer idVelo = (Integer) pair.getKey();
	        Velo veloListe = (Velo) pair.getValue();
	        if(veloListe.getEtat() == Etat.Libre)
		    {
		    	listeIdsVelosLibres[j] = veloListe.getIdVelo();
		    	j++;
		    }
		}
		//On vérifie que le nombre voulu de vélos est libre, sinon -> getStationLaPlusProche
		if(j < nbVelosNecessaires)
			listeIdsVelosLibres[nbVelos] = getStationLaPlusProche().getIdStation();
		else
			listeIdsVelosLibres[nbVelos] = this.idStation;
		return listeIdsVelosLibres;
		
	}
	
	public int chercherVeloLibreStation()
	{
		
		Iterator it = lesVelos.entrySet().iterator();
		
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        Integer idVelo = (Integer) pair.getKey();
	        Velo veloListe = (Velo) pair.getValue();
	        if(veloListe.getEtat() == Etat.Libre)
		    {
		    	return veloListe.getIdVelo();
		    }
		}
		
		return -1; // si aucun velo libre trouvé
	}
	
	
	public Station getStationLaPlusProche()
	{
		Station stationPlusProche = null;
		Station stationTemp;
		double distanceMinimale = 10000; 
		double distanceCalculee = 0; 
		
		Iterator<Station> it = lesStations.values().iterator();
	    while (it.hasNext()) 
	    {
	        stationTemp = it.next();
	        distanceCalculee = Distance.distanceInKilometers(this.getPosition().getLatitude(), this.getPosition().getLongitude(), stationTemp.getPosition().getLatitude(), stationTemp.getPosition().getLongitude());
	        if(distanceCalculee < distanceMinimale)
	        {
	        	distanceMinimale=distanceCalculee;
	        	stationPlusProche=stationTemp;
	        }	        
	        it.remove(); // avoids a ConcurrentModificationException
	    }
		return stationPlusProche;
	}
	
	public boolean supprimerStation()
	{
		if(lesStations.containsKey(idStation))
		{
			Station stationTemp = lesStations.get(idStation);
			int idVeloTemp;
			//Suppression de tous les vélos de cette station
			for(int i = 0; i < stationTemp.lesVelos.size(); i++)
			{
				idVeloTemp = stationTemp.lesVelos.get(i).getIdVelo();
				if(supprimerVelo(idVeloTemp))
					stationTemp.lesVelos.remove(i);
			}
			lesStations.remove(stationTemp);
			return true;
		}
		else
			return false;
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
	
	public static boolean hasMaitre()
	{
		Iterator it = lesStations.entrySet().iterator();
		
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        Integer idStation = (Integer) pair.getKey();
	        Station stationListe = (Station) pair.getValue();
	        
	        if(stationListe.EstMaitre())
	        {
	        	return true;
	        }

	        
	        it.remove(); // avoids a ConcurrentModificationException
	    }
	    
	    return false;
	}
	
	public boolean supprimerVelo(int idVelo)
	{
		//Hypothèse : un vélo ne peut être supprimé que s'il est libre
		if(lesVelos.containsKey(idVelo) && lesVelos.get(idVelo).getEtat() == Etat.Libre)
		{
			lesVelos.remove(idVelo);
			return true;
		}
		else
			return false;
	}
	
}
