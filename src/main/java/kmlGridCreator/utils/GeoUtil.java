package main.java.kmlGridCreator.utils;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import com.peertopark.java.geocalc.DegreeCoordinate;

import main.java.kmlGridCreator.model.MyPoint;

public class GeoUtil {

	/**
	 * @param points
	 * @return the array of the NW, NE, SE, SW corner MyPoints of the given list
	 * @throws Exception
	 */
	public static MyPoint[] getCornersNwNeSeSw(List<MyPoint> points) {
		if (points == null) {
			throw new NullPointerException("pointlist has to be != null");
		}

		Optional<MyPoint> west = points.stream().min(new Comparator<MyPoint>() {
			@Override
			public int compare(MyPoint a, MyPoint b) {
				return Double.compare(a.getLongitude(), b.getLongitude());
			}
		});

		Optional<MyPoint> east = points.stream().max(new Comparator<MyPoint>() {
			@Override
			public int compare(MyPoint a, MyPoint b) {
				return Double.compare(a.getLongitude(), b.getLongitude());
			}
		});

		Optional<MyPoint> north = points.stream().max(new Comparator<MyPoint>() {
			@Override
			public int compare(MyPoint a, MyPoint b) {
				return Double.compare(a.getLatitude(), b.getLatitude());
			}
		});

		Optional<MyPoint> south = points.stream().min(new Comparator<MyPoint>() {
			@Override
			public int compare(MyPoint a, MyPoint b) {
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
		MyPoint nw = new MyPoint(new DegreeCoordinate(north.get().getLatitude()), new DegreeCoordinate(west.get().getLongitude()));
		MyPoint se = new MyPoint(new DegreeCoordinate(south.get().getLatitude()), new DegreeCoordinate(east.get().getLongitude()));
		MyPoint ne = new MyPoint(new DegreeCoordinate(north.get().getLatitude()), new DegreeCoordinate(east.get().getLongitude()));
		MyPoint sw = new MyPoint(new DegreeCoordinate(south.get().getLatitude()), new DegreeCoordinate(west.get().getLongitude()));
		return new MyPoint[] { nw, ne, se, sw };
	}
}
