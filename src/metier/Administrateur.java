package metier;

import metier.CarteAcces.Role;

public class Administrateur extends Utilisateur {

	public Administrateur(int pidUtilisateur, String pnom, String pprenom,
			int ptelephone) {
		super(pnom, pprenom, ptelephone);
		CarteAcces carteAcces = new CarteAcces(Role.Administrateur);
		lesCartesAccesUtilisateur.add(carteAcces);
		// TODO Auto-generated constructor stub
	}
	
	

}
