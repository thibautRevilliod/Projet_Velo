package metier;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GestionStationNotif extends Remote {

	//retourne -1 s'il y pas de notification et l'id de la notification s'il y en a.
	// modifie le statut de la notification en "EnCours" !
	public int estnotificationStation() throws RemoteException;
	
	// return [0] = nbreVelos; [1] = stationSaturée; [2] = stationPénurie
	public String[] detailNotificationStation(int idNotification) throws RemoteException;
	
	public void notificationOK(int idNotification) throws RemoteException;
}
