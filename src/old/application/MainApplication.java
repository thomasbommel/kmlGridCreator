package old.application;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JOptionPane;

import com.peertopark.java.geocalc.EarthCalc;
import com.peertopark.java.geocalc.Point;

import old.kml.MyKmlFactory;
import old.map.MyOldBoundingArea;
import old.utils.GeoUtil;
import old.utils.Logger;
import old.utils.Logger.LogLevel;
import old.utils.TxtUtil;

public class MainApplication {

	public static final int GRID_SIZE_IN_M = 5000;

	private static final Logger log = new Logger(LogLevel.DEBUG);

	public static void main(String[] args) {
		final Date start = new Date();
		log.debug(start.toString());

		List<Point> points = TxtUtil.getTestPoints();

		List<MyOldBoundingArea> boundingAreas = new ArrayList<>();

		// just for testing
		// Point m1 = new Point(new DegreeCoordinate(41.842898), new
		// DegreeCoordinate(18.394896));
		// Point m2 = new Point(new DegreeCoordinate(41.833901), new
		// DegreeCoordinate(18.395261));
		// Point m3 = new Point(new DegreeCoordinate(41.834173), new
		// DegreeCoordinate(18.4072921));
		// Point m4 = new Point(new DegreeCoordinate(41.843171), new
		// DegreeCoordinate(18.406928));

		// final Coordinate lat = new DegreeCoordinate(43.554);
		// final Coordinate lng = new DegreeCoordinate(18.4346);

		Point[] corners = GeoUtil.getCornersNwNeSeSw(points);

		Point startPointNW = corners[0];

		final MyKmlFactory kml = new MyKmlFactory();

		startPointNW = EarthCalc.pointRadialDistance(EarthCalc.pointRadialDistance(startPointNW, 0, GRID_SIZE_IN_M * 2), 270, GRID_SIZE_IN_M * 2);

		Point nwPointOfField = startPointNW;

		Point sw = EarthCalc.pointRadialDistance(EarthCalc.pointRadialDistance(startPointNW, 90, GRID_SIZE_IN_M), 180, GRID_SIZE_IN_M);

		int xSize = Math.max((int) Math.ceil(EarthCalc.getHarvesineDistance(corners[0], corners[1]) / GRID_SIZE_IN_M),
				(int) Math.ceil(EarthCalc.getHarvesineDistance(corners[2], corners[3]) / GRID_SIZE_IN_M)) + 2;
		int ySize = Math.max((int) Math.ceil(EarthCalc.getHarvesineDistance(corners[0], corners[3]) / GRID_SIZE_IN_M),
				(int) Math.ceil(EarthCalc.getHarvesineDistance(corners[1], corners[2]) / GRID_SIZE_IN_M)) + 4;
		log.debug("xSize: " + xSize);
		log.debug("ySize: " + ySize);

		for (int yIndex = 0; yIndex < ySize; yIndex++) {// height
			for (int xIndex = 0; xIndex < xSize; xIndex++) {// width
				nwPointOfField = (EarthCalc.pointRadialDistance(nwPointOfField, 90, GRID_SIZE_IN_M));
				sw = EarthCalc.pointRadialDistance(EarthCalc.pointRadialDistance(nwPointOfField, 270, GRID_SIZE_IN_M), 180, GRID_SIZE_IN_M);

				MyOldBoundingArea ba = new MyOldBoundingArea(nwPointOfField, sw, yIndex + " | " + xIndex);
				boundingAreas.add(ba);
			}
			nwPointOfField = (EarthCalc.pointRadialDistance(startPointNW, 180, GRID_SIZE_IN_M * (yIndex + 1)));
		}

		final List<Point> notfound = new ArrayList<>();

		for (Point p : points) {
			boolean found = false;
			for (MyOldBoundingArea ba : boundingAreas) {
				if (ba.addPoint(p)) {
					found = true;
					// System.out.println("point found: " + p);
					break;
				}
			}
			if (!found) {
				notfound.add(p);
			}
		}

		for (MyOldBoundingArea ba : boundingAreas) {
			// kml.addBoundingArea(ba);
			if (ba.getPointCount() > 0) {
				// log.debug("" + ba.getPointCount());
			}
		}

		kml.addPointsToKml(notfound);
		kml.addPointsToKml(points); // if you want to add all points

		log.debug(points.size() + " points added");
		log.debug(notfound.size() + " points couldn't be added");
		kml.saveKmlFile("KmlGrid_" + System.currentTimeMillis());
		log.debug(new Date().toString());
		log.debug("time needed: " + Math.round((new Date().getTime() - start.getTime()) / 1000) + " seconds");
		JOptionPane.showMessageDialog(null, "KML creation successful.");
	}
}
