package metier;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GestionStationNotif extends Remote {
	
	public void notification() throws RemoteException;

}
