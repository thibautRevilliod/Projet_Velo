package metier;

import java.util.HashMap;

public class Velo {
	
	public enum Etat {Emprunte, Libre, EnReparation};
	
	private int idVelo;
	private static HashMap<Integer, Velo> lesVelos = new HashMap<Integer, Velo>();
	private Etat etat;
	private static int idsVelo = 0; 
	
	public Velo() {
		this.etat = Etat.Libre;
		idsVelo++;
		this.idVelo = idsVelo;
		lesVelos.put(idsVelo,this);
	}
	
	public int getIdVelo() {return idVelo;}
	public Etat getEtat() {return etat;}
	public void setEtat(Etat etat) {this.etat = etat;}
	
	public static Velo getVelo(int idVelo)
	{
		if(lesVelos.containsKey(idVelo))
				return lesVelos.get(idVelo);
		else
			return null;
	}
	
	public static boolean supprimerVelo(int idVelo)
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
