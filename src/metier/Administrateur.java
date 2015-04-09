package metier;

import metier.CarteAcces.Role;

public class Administrateur extends Utilisateur {

	public Administrateur(String pnom, String pprenom, String pmotdepasse,
			String ptelephone, String padressemail, String padressepostale) {
		super(pnom, pprenom, pmotdepasse, ptelephone, padressemail, padressepostale);
		CarteAcces carteAcces = new CarteAcces(Role.Administrateur);
		lesCartesAccesUtilisateur.put(carteAcces.getRole(), carteAcces);
		// TODO Auto-generated constructor stub
	}
}
