package main.java.kmlGridCreator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.peertopark.java.geocalc.BoundingArea;
import com.peertopark.java.geocalc.DegreeCoordinate;
import com.peertopark.java.geocalc.Point;

public class MyBoundingArea extends BoundingArea {

	private final List<Point> points;
	private final String id;

	MyBoundingArea(Point northEast, Point southWest, String id) {
		super(northEast, southWest);
		this.points = Collections.synchronizedList(new ArrayList<>());
		this.id = id;
	}

	public String getId() {
		return this.id;
	}

	boolean tryToAddPoint(Point p) {
		if (this.isContainedWithin(p)) {
			points.add(p);
			return true;
		}
		return false;
	}

	public int getPointCount() {
		return this.points.size();
	}

	public List<MyPoint> getPoints() {
		return this.points.stream().map(x->new MyPoint(new DegreeCoordinate(x.getLatitude()), new DegreeCoordinate(x.getLongitude()))).collect(Collectors.toList());
	}

}
