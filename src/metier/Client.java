package metier;

import metier.CarteAcces.Role;

public class Client extends Utilisateur {
	
	private Velo velo;

	public Client(String pnom, String pprenom, String pmotdepasse,
			String ptelephone, String padressemail, String padressepostale) {
		super(pnom, pprenom, pmotdepasse, ptelephone, padressemail, padressepostale);
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
