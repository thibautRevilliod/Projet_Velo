package metier;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GestionStationNotif extends Remote {
	
	public void notification() throws RemoteException;

	//retourne -1 s'il y pas de notification et l'id de la notification s'il y en a.
	// modifie le statut de la notification en "EnCours" !
	public int estnotificationStation();
	
	// return [0] = nbreVelos; [1] = stationSaturée; [2] = stationPénurie
	public String[] detailNotificationStation(int notification);
	
	public void notificationOK(int idNotification);
}
