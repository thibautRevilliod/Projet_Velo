package metier;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class GestionStationNotifImpl extends UnicastRemoteObject
implements GestionStationNotif {
	
	private String id;
	
	public GestionStationNotifImpl(String id) throws RemoteException {
		 	 super() ;
		 	 this. id = id;
	}
	
	public void notification()
			 throws RemoteException {
				 	 System. out.println("");
			}

}
