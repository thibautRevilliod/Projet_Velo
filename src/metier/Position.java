package metier;
import java.io.Serializable;
import java.util.Date;

public class Position implements Serializable {
	private double longitude;
	private double latitude;

	public Position(double longitude, double latitude) {
		this.longitude = longitude;
		this.latitude = latitude;
	}
}