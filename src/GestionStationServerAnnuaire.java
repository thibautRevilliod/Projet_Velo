

import java.rmi.registry.LocateRegistry;

import metier.GestionStationImpl;

public class GestionStationServerAnnuaire {

	public static void main(String[] args) throws Exception {
		LocateRegistry.createRegistry(1099); // d�marre service de nom RMI qui sert � localiser les objets serveur RMI depuis une autre machine -> lien entre partie client et partie serveur
		
		GestionStationImpl gestionStationImpl = new GestionStationImpl();
	}

}
