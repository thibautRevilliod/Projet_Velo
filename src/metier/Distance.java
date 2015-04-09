/*
 * Université Toulouse III - Paul Sabatier
 * Author: E. Lavinal
 */

package metier;

public class Distance {

	/**
	 * central angle: arccos( sin(lat1)*sin(lat2) + cos(lat1)*cos(lat2)*cos(abs(lon1-lon2)) )
	 *   lat1, lat2, lon1, lon2 are in radians
	 * 
	 * distance (arc length) for a sphere of radius r
	 *    d = r*(central angle)
	 * 
	 * @param lat1
	 *            latitude point 1
	 * @param lon1
	 *            longitude point 1
	 * @param lat2
	 *            latitude point 2
	 * @param lon2
	 *            longitude point 2
	 * @return distance in kilometers between the 2 points
	 */
	public static double distanceInKilometers(double lat1, double lon1, double lat2, double lon2) {

		double lat1r = Math.toRadians(lat1);
		double lon1r = Math.toRadians(lon1);
		double lat2r = Math.toRadians(lat2);
		double lon2r = Math.toRadians(lon2);

		double sum1 = Math.sin(lat1r) * Math.sin(lat2r);

		double deltaLon = Math.abs(lon1r - lon2r);
		double sum2 = Math.cos(lat1r) * Math.cos(lat2r) * Math.cos(deltaLon);

		double centralAngle = Math.acos(sum1 + sum2);

		// Radius of Earth: 6378.1 kilometers
		double distance = centralAngle * 6378.1;

		return distance;
	}
	

	// Test
	public static void main(String args[]) {

		// Place du capitole, Toulouse: 43.604437, 1.443482
		// Hôtel de ville, Paris: 48.856487, 2.352660
		System.out.println("Distance entre Toulouse et Paris : "
				+ distanceInKilometers(43.604437, 1.443482, 48.856487, 2.352660)
				+ " kilometres");
		
		System.out.println("Distance entre métro UPS et métro Ramonville : "
				+ distanceInKilometers(43.560865, 1.463392, 43.555850, 1.476438)
				+ " kilometres");
	}

}
