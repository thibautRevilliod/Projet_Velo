package metier;

import metier.CarteAcces.Role;

public class Client extends Utilisateur {
	
	public Velo velo;

	public Client(int pidUtilisateur, String pnom, String pprenom,
			int ptelephone) {
		super(pnom, pprenom, ptelephone);
		CarteAcces carteAcces = new CarteAcces(Role.Client);
		lesCartesAccesUtilisateur.put(carteAcces.getRole(),carteAcces);
		// TODO Auto-generated constructor stub
	}

	public Velo getVelo() {
		return velo;
	}

	public void setVelo(Velo velo) {
		this.velo = velo;
	}
	
	

}
