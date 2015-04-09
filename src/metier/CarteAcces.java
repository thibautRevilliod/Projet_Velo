package metier;

import java.util.ArrayList;
import java.util.HashMap;

public class CarteAcces {
	
	public enum Role{Administrateur, Operateur, Client};
	public enum Statut{Active, Inactive};
	
	private Role role;	
	private Statut statut;
	
	public CarteAcces(Role prole) {
		this.role = prole;
		this.statut = Statut.Active;
	}

	
	public Statut getStatut() {
		return statut;
	}


	public void setStatut(Statut statut) {
		this.statut = statut;
	}

	public Role getRole() {
		return role;
	}


	public void setRole(Role role) {
		this.role = role;
	}
	
	

}
