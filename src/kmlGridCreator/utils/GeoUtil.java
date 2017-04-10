package kmlGridCreator.utils;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import com.peertopark.java.geocalc.DegreeCoordinate;
import com.peertopark.java.geocalc.Point;

public class GeoUtil {

	/**
	 * @param points
	 * @return the array of the NW, NE, SE, SW corner points of the given list
	 */
	public static Point[] getCornersNwNeSeSw(List<Point> points) {
		Optional<Point> west = points.stream().min(new Comparator<Point>() {
			@Override
			public int compare(Point a, Point b) {
				return Double.compare(a.getLongitude(), b.getLongitude());
			}
		});

		Optional<Point> east = points.stream().max(new Comparator<Point>() {
			@Override
			public int compare(Point a, Point b) {
				return Double.compare(a.getLongitude(), b.getLongitude());
			}
		});

		Optional<Point> north = points.stream().max(new Comparator<Point>() {
			@Override
			public int compare(Point a, Point b) {
				return Double.compare(a.getLatitude(), b.getLatitude());
			}
		});

		Optional<Point> south = points.stream().min(new Comparator<Point>() {
			@Override
			public int compare(Point a, Point b) {
				return Double.compare(a.getLatitude(), b.getLatitude());
			}
		});
		/*
		 * log.debug("---------- CORNERS --------"); if (north.isPresent())
		 * log.debug("N:  " + (double) Math.round(north.get().getLatitude() *
		 * 1000) / 1000 + " °N ");
		 * 
		 * if (west.isPresent()) log.debug("E:  " + (double)
		 * Math.round(east.get().getLongitude() * 1000) / 1000 + " °O");
		 * 
		 * if (south.isPresent()) log.debug("S:  " + (double)
		 * Math.round(south.get().getLatitude() * 1000) / 1000 + " °N ");
		 * 
		 * if (west.isPresent()) log.debug("W:  " + (double)
		 * Math.round(west.get().getLongitude() * 1000) / 1000 + " °O");
		 * log.debug("---------------------------");
		 */
		Point nw = new Point(new DegreeCoordinate(north.get().getLatitude()), new DegreeCoordinate(west.get().getLongitude()));
		Point se = new Point(new DegreeCoordinate(south.get().getLatitude()), new DegreeCoordinate(east.get().getLongitude()));
		Point ne = new Point(new DegreeCoordinate(north.get().getLatitude()), new DegreeCoordinate(east.get().getLongitude()));
		Point sw = new Point(new DegreeCoordinate(south.get().getLatitude()), new DegreeCoordinate(west.get().getLongitude()));
		return new Point[] { nw, ne, se, sw };
	}
}
