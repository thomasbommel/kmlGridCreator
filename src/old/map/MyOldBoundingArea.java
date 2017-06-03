package old.map;

import java.util.ArrayList;
import java.util.List;

import com.peertopark.java.geocalc.BoundingArea;
import com.peertopark.java.geocalc.Point;

@Deprecated
public class MyOldBoundingArea extends BoundingArea {

	private List<Point> points;
	private String id;

	public MyOldBoundingArea(Point northEast, Point southWest, String id) {
		super(northEast, southWest);
		this.points = new ArrayList<>();
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

}
