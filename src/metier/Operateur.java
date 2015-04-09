package metier;

import metier.CarteAcces.Role;

public class Operateur extends Utilisateur {

	public Operateur(String pnom, String pprenom, String pmotdepasse,
			String ptelephone, String padressemail, String padressepostale) {
		super(pnom, pprenom, pmotdepasse, ptelephone, padressemail, padressepostale);
		CarteAcces carteAcces = new CarteAcces(Role.Operateur);
		lesCartesAccesUtilisateur.put(carteAcces.getRole(),carteAcces);
		// TODO Auto-generated constructor stub
	}

}
