package metier;

import java.util.ArrayList;
import java.util.HashMap;

public class CarteAcces {
	
	public enum Role{Administrateur, Operateur, Client, Expire};
	private Role role;	
	
	public CarteAcces(Role role) {
		this.role = role;
	}


	public Role getRole() {
		return role;
	}


	public void setRole(Role role) {
		this.role = role;
	}
	
	

}
