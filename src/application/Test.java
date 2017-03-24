package application;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.peertopark.java.geocalc.Coordinate;
import com.peertopark.java.geocalc.DegreeCoordinate;
import com.peertopark.java.geocalc.EarthCalc;
import com.peertopark.java.geocalc.Point;

import kml.MyKmlFactory;
import utils.Logger;
import utils.Logger.LogLevel;
import utils.TxtUtil;

public class Test {

	public static final int GRID_SIZE_IN_M = 1000;

	private static final Logger log = new Logger(LogLevel.DEBUG);

	public static void main(String[] args) {
		log.debug(new Date().toString());

		List<Point> points = TxtUtil.getTestPoints();

		List<MyBoundingArea> boundingAreas = new ArrayList<>();

		// Point m1 = new Point(new DegreeCoordinate(41.842898), new
		// DegreeCoordinate(18.394896));
		// Point m2 = new Point(new DegreeCoordinate(41.833901), new
		// DegreeCoordinate(18.395261));
		// Point m3 = new Point(new DegreeCoordinate(41.834173), new
		// DegreeCoordinate(18.4072921));
		// Point m4 = new Point(new DegreeCoordinate(41.843171), new
		// DegreeCoordinate(18.406928));

		// points = Arrays.asList(m1, m2, m3, m4);

		Coordinate lat = new DegreeCoordinate(43.554);
		Coordinate lng = new DegreeCoordinate(18.4346);
		Point startPoint = new Point(lat, lng);

		MyKmlFactory kml = new MyKmlFactory();

		// kml.addPointsToKml(points);
		startPoint = EarthCalc.pointRadialDistance(EarthCalc.pointRadialDistance(startPoint, 0, GRID_SIZE_IN_M), 270, GRID_SIZE_IN_M);

		Point ne = startPoint;

		Point sw = EarthCalc.pointRadialDistance(EarthCalc.pointRadialDistance(startPoint, 90, GRID_SIZE_IN_M), 180, GRID_SIZE_IN_M);

		for (int a = 0; a < 200; a++) {// height
			for (int b = 0; b < 180; b++) {// width
				ne = (EarthCalc.pointRadialDistance(ne, 90, GRID_SIZE_IN_M));
				sw = EarthCalc.pointRadialDistance(EarthCalc.pointRadialDistance(ne, 270, GRID_SIZE_IN_M), 180, GRID_SIZE_IN_M);

				MyBoundingArea ba = new MyBoundingArea(ne, sw, a + "/" + b);

				boundingAreas.add(ba);
				// log.debug(ba.getNorthEast() + "" + ba.getSouthWest() + " " +
				// ba.getPointCount());

			}
			ne = (EarthCalc.pointRadialDistance(startPoint, 180, GRID_SIZE_IN_M * (a + 1)));
		}

		List<Point> notfound = new ArrayList<>();

		for (Point p : points) {
			boolean found = false;
			for (MyBoundingArea ba : boundingAreas) {
				if (ba.addPoint(p)) {
					found = true;
					break;
				}
			}
			if (!found) {
				notfound.add(p);
			}
		}

		for (MyBoundingArea ba : boundingAreas) {
			kml.addBoundingArea(ba);
			if (ba.getPointCount() > 0) {
				// log.debug("" + ba.getPointCount());
			}
		}

		kml.addPointsToKml(notfound);
		// kml.addPointsToKml(points);

		log.debug(points.size() + " points added");
		log.debug(notfound.size() + " points couldn't be added");
		kml.saveKmlFile("test");
		log.debug(new Date().toString());
	}
}
