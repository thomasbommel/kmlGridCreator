package utils;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.peertopark.java.geocalc.DegreeCoordinate;
import com.peertopark.java.geocalc.EarthCalc;
import com.peertopark.java.geocalc.Point;

import exceptions.ListEmptyException;

public class GeoUtil {

	public static final double calculateDistanceInMeter(Point from, Point to) {
		return EarthCalc.getHarvesineDistance(from, to);
	}

	/**
	 * @param allPoints
	 * @return the array of the NW, NE, SE, SW corner points of the given list
	 */
	public static Point[] getCornersNwNeSeSw(List<Point> allPoints) {
		Optional<Point> west = allPoints.stream().min(new Comparator<Point>() {
			@Override
			public int compare(Point a, Point b) {
				return Double.compare(a.getLongitude(), b.getLongitude());
			}
		});

		Optional<Point> east = allPoints.stream().max(new Comparator<Point>() {
			@Override
			public int compare(Point a, Point b) {
				return Double.compare(a.getLongitude(), b.getLongitude());
			}
		});

		Optional<Point> north = allPoints.stream().max(new Comparator<Point>() {
			@Override
			public int compare(Point a, Point b) {
				return Double.compare(a.getLatitude(), b.getLatitude());
			}
		});

		Optional<Point> south = allPoints.stream().min(new Comparator<Point>() {
			@Override
			public int compare(Point a, Point b) {
				return Double.compare(a.getLatitude(), b.getLatitude());
			}
		});

		if (north.isPresent())
			System.out.println("N:  " + (double) Math.round(north.get().getLatitude() * 1000) / 1000 + " °N ");

		if (west.isPresent())
			System.out.println("E:  " + (double) Math.round(east.get().getLongitude() * 1000) / 1000 + " °O");

		if (south.isPresent())
			System.out.println("S:  " + (double) Math.round(south.get().getLatitude() * 1000) / 1000 + " °N ");

		if (west.isPresent())
			System.out.println("W:  " + (double) Math.round(west.get().getLongitude() * 1000) / 1000 + " °O");

		Point nw = new Point(new DegreeCoordinate(north.get().getLatitude()), new DegreeCoordinate(west.get().getLongitude()));
		Point se = new Point(new DegreeCoordinate(south.get().getLatitude()), new DegreeCoordinate(east.get().getLongitude()));
		Point ne = new Point(new DegreeCoordinate(north.get().getLatitude()), new DegreeCoordinate(east.get().getLongitude()));
		Point sw = new Point(new DegreeCoordinate(south.get().getLatitude()), new DegreeCoordinate(west.get().getLongitude()));
		System.out.println("--- points calculated ---");
		return new Point[] { nw, ne, se, sw };
	}

	/**
	 * calculates the min. value of the bearing between from and the other
	 * points, north=0; clockwise
	 * 
	 * @param from
	 * @param points
	 * @return
	 * @throws ListEmptyException
	 */
	public double getMinBearingOfPointsFromPoint(Point from, List<Point> points) throws ListEmptyException {
		List<Double> minBearPoints = points.stream().sorted(new Comparator<Point>() {
			@Override
			public int compare(Point a, Point b) {
				return Double.compare(EarthCalc.getBearing(from, a), EarthCalc.getBearing(from, b));
			}
		}).skip(3).limit(1).map(p -> EarthCalc.getBearing(from, p)).collect(Collectors.toList());
		if (minBearPoints.size() < 0) {
			throw new ListEmptyException("list empty");
		}
		return minBearPoints.get(0);
	}

	/**
	 * calculates the max. value of the bearing between from and the other
	 * points, north=0; clockwise
	 * 
	 * @param from
	 * @param points
	 * @return
	 * @throws ListEmptyException
	 */
	public double getMaxBearingOfPointsFromPoint(Point from, List<Point> points) throws ListEmptyException {
		List<Double> maxBearPoints = points.stream().sorted(new Comparator<Point>() {
			@Override
			public int compare(Point b, Point a) {
				return Double.compare(EarthCalc.getBearing(from, a), EarthCalc.getBearing(from, b));
			}
		}).skip(3).limit(1).map(p -> EarthCalc.getBearing(from, p)).collect(Collectors.toList());
		if (maxBearPoints.size() < 0) {
			throw new ListEmptyException("list empty");
		}
		return maxBearPoints.get(0);
	}

}
