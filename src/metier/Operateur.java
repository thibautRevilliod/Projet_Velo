package metier;

import metier.CarteAcces.Role;

public class Operateur extends Utilisateur {

	public Operateur(int pidUtilisateur, String pnom, String pprenom,
			int ptelephone) {
		super(pnom, pprenom, ptelephone);
		CarteAcces carteAcces = new CarteAcces(Role.Operateur);
		lesCartesAccesUtilisateur.put(carteAcces.getRole(),carteAcces);
		// TODO Auto-generated constructor stub
	}

}
