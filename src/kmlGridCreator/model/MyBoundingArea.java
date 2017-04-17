package kmlGridCreator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.peertopark.java.geocalc.BoundingArea;
import com.peertopark.java.geocalc.Point;

public class MyBoundingArea extends BoundingArea {

	private final List<Point> points;
	private final String id;

	public MyBoundingArea(Point northEast, Point southWest, String id) {
		super(northEast, southWest);
		this.points = Collections.synchronizedList(new ArrayList<>());
		this.id = id;
	}

	public String getId() {
		return this.id;
	}

	public boolean addPoint(Point p) {
		if (this.isContainedWithin(p)) {
			points.add(p);
			return true;
		}
		return false;
	}

	public int getPointCount() {
		return this.points.size();
	}

	public List<Point> getPoints() {
		return this.points;
	}

}
