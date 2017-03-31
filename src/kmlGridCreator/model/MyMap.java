package kmlGridCreator.model;

import java.util.ArrayList;
import java.util.List;

import com.peertopark.java.geocalc.Point;

public class MyMap {

	private Point nwPoint, nePoint, swPoint, sePoint;
	private List<MyBoundingArea> boundingAreas;

	public MyMap(Point nwPoint, Point nePoint, Point swPoint, Point sePoint) {
		super();
		this.nwPoint = nwPoint;
		this.nePoint = nePoint;
		this.swPoint = swPoint;
		this.sePoint = sePoint;
		this.boundingAreas = new ArrayList<>();
	}

	public void generateBoundingAreas(List<Point> points) {
		// TODO
	}

	public void addBoundingArea() {

	}

	// ==================================================
	public Point getNwPoint() {
		return nwPoint;
	}

	public Point getNePoint() {
		return nePoint;
	}

	public Point getSwPoint() {
		return swPoint;
	}

	public Point getSePoint() {
		return sePoint;
	}

}
