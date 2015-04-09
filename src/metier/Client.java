package metier;

import metier.CarteAcces.Role;

public class Client extends Utilisateur {

	public Client(int pidUtilisateur, String pnom, String pprenom,
			int ptelephone) {
		super(pidUtilisateur, pnom, pprenom, ptelephone);
		CarteAcces carteAcces = new CarteAcces(Role.Client);
		lesCartesAccesUtilisateur.add(carteAcces);
		// TODO Auto-generated constructor stub
	}

}
