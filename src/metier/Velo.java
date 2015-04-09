package metier;

import java.util.HashMap;

public class Velo {
	
	private int idVelo;
	public static HashMap<Integer, Velo> lesVelos = new HashMap<Integer, Velo>();
	public enum Etat {Emprunte, Libre};
	private Etat etat;
	public static int idsVelo = 0; 
	
	

	public Velo(int idVelo) {
		this.etat = Etat.Libre;
		this.idVelo = idVelo;
		idsVelo++;
		lesVelos.put(idsVelo,this);
	}



	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
