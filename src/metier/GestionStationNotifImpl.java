package metier;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class GestionStationNotifImpl extends UnicastRemoteObject
implements GestionStationNotif {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4235438163194240184L;
	public static int idsNotification = 0;
	public enum Etat {Ouverte, Cloturee};
	private int idNotification;
	@SuppressWarnings("unused")
	private Date dateNotification;
	private Station stationEnPenurie;
	private Station stationSaturee;
	private Etat etatNotification;
	public static HashMap<Integer, GestionStationNotifImpl> lesNotifications = new HashMap<Integer, GestionStationNotifImpl>();
	
	
	public GestionStationNotifImpl() throws RemoteException {
	 	 super() ;
	 	 this.dateNotification = new Date();
	 	 idsNotification++;
	 	 this.idNotification = idsNotification;
	 	 this.etatNotification = Etat.Ouverte;
}
	
	public GestionStationNotifImpl(Station stationEnPenurie) throws RemoteException {
		 	 super() ;
		 	 this.stationEnPenurie = stationEnPenurie;
		 	 this.dateNotification = new Date();
		 	 idsNotification++;
		 	 this.idNotification = idsNotification;
		 	 this.etatNotification = Etat.Ouverte;
	}
	
	public int getIdNotification()
	{
		return this.idNotification;
	}
	
	public Etat getEtat() {
		return etatNotification;
	}


	public void setEtat(Etat etatNotification) {
		this.etatNotification = etatNotification;
	}
	
	public static GestionStationNotifImpl getNotification(int idNotification)
	{
		Iterator it = lesNotifications.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        GestionStationNotifImpl notificationListe = (GestionStationNotifImpl) pair.getValue();
	        
	        if(notificationListe.getIdNotification() == idNotification)
	        {
	        	return notificationListe;
	        }
	    }
	    
	    return null;
	}
	
	public static GestionStationNotifImpl getFirstOpenNotification()
	{
		Iterator it = lesNotifications.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        GestionStationNotifImpl notificationListe = (GestionStationNotifImpl) pair.getValue();
	        
	        if(notificationListe.getEtat() == Etat.Ouverte)
	        {
	        	return notificationListe;
	        }
	    }
	    
	    return null;
	}

	@Override
	public int estnotificationStation() throws RemoteException {
		GestionStationNotifImpl openNotification = GestionStationNotifImpl.getFirstOpenNotification();
		if(openNotification != null)
		{
			return openNotification.getIdNotification();
		}
		else
		{
			return -1;
		}
	}

	@Override
	public String[] detailNotificationStation(int idNotification) throws RemoteException {
		GestionStationNotifImpl notification = GestionStationNotifImpl.getNotification(idNotification);
		String infosNotification[] = new String[3];
		notification.stationSaturee = Station.getStationSaturee();
		notification.stationEnPenurie = Station.getStationEnPenurie();
		int nombreVeloTransferes = notification.stationSaturee.getLesVelos().size()/2;
		infosNotification[0] = String.valueOf(nombreVeloTransferes);
		infosNotification[1] = String.valueOf(notification.stationSaturee.getIdStation());
		infosNotification[2] = String.valueOf(notification.stationEnPenurie.getIdStation());
		return infosNotification;
	}

	@Override
	public void notificationOK(int idNotification) throws RemoteException {
		GestionStationNotifImpl notification = GestionStationNotifImpl.getNotification(idNotification);
		notification.setEtat(Etat.Cloturee);		
	}

}
