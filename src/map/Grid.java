package map;

import com.peertopark.java.geocalc.DegreeCoordinate;
import com.peertopark.java.geocalc.Point;

import utils.GeoUtil;

public class Grid {
	private double sizeX;

	public Grid(double gridFieldSize, Point northWest, Point southEast) {
		// FIXME not yet working

		sizeX = Math
				.ceil(GeoUtil.calculateDistanceInKM(new Point(new DegreeCoordinate(northWest.getLatitude()), new DegreeCoordinate(northWest.getLongitude())),
						new Point(new DegreeCoordinate(northWest.getLatitude()), new DegreeCoordinate(southEast.getLongitude()))));
		System.out.println(">>>" + sizeX);

		sizeX = Math
				.ceil(GeoUtil.calculateDistanceInKM(new Point(new DegreeCoordinate(southEast.getLatitude()), new DegreeCoordinate(northWest.getLongitude())),
						new Point(new DegreeCoordinate(southEast.getLatitude()), new DegreeCoordinate(southEast.getLongitude()))));
		System.out.println("---" + sizeX);
	}

}
