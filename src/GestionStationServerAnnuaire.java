

import java.rmi.registry.LocateRegistry;

import metier.GestionStationImpl;

public class GestionStationServerAnnuaire {

	public static void main(String[] args) throws Exception {
		LocateRegistry.createRegistry(1099); // démarre service de nom RMI qui sert à localiser les objets serveur RMI depuis une autre machine -> lien entre partie client et partie serveur
		
		GestionStationImpl gestionStationImpl = new GestionStationImpl();
	}

}
