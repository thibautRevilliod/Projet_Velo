package metier;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GestionStationNotif extends Remote {
	
	public void notification() throws RemoteException;

	//retourne false s'il y pas de notification et true s'il y en a.
	public boolean estnotificationStation();
	
	// return [0] = nbreVelos; [1] = stationSaturée; [2] = stationPénurie; [3] idNotification;
	public String[] detailNotificationStation();
	
	public void notificationOK(int idNotification);
}
