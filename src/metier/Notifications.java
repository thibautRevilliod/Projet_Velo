package metier;

import java.io.Serializable;
import metier.GestionStationNotifImpl.TypeNotification;

public class Notifications implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2573185254499289151L;
	private TypeNotification typeNotification;
	private int idStation;
	
	public Notifications(TypeNotification typeNotification, int idStation) {
		this.typeNotification = typeNotification;
		this.idStation = idStation;
	}
	
	public TypeNotification getTypeNotification() {
		return typeNotification;
	}
	public void setTypeNotification(TypeNotification typeNotification) {
		this.typeNotification = typeNotification;
	}
	public int getStation() {
		return idStation;
	}
	public void setIdStation(int idStation) {
		this.idStation = idStation;
	}
	
	
}
