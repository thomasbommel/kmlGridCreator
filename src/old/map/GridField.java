package old.map;

import java.util.ArrayList;
import java.util.List;

import com.peertopark.java.geocalc.Point;

@Deprecated
public class GridField {
	private Point nwPoint;
	private List<Point> pointsInThisGridField;

	public GridField(Point nwPoint) {
		this.nwPoint = nwPoint;
		this.pointsInThisGridField = new ArrayList<>();
	}

	public Point getNwPoint() {
		return this.nwPoint;
	}

	public void addPoint(Point p) {
		this.pointsInThisGridField.add(p);
	}

	public List<Point> getPointsInThisGridField() {
		return pointsInThisGridField;
	}

	public int getCount() {
		return this.pointsInThisGridField.size();
	}

}
