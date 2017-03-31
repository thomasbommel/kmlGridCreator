package old.event;

import com.peertopark.java.geocalc.Point;

public class PointCreatedEvent {

	private Point point;

	public PointCreatedEvent(Point point) {
		this.point = point;
	}

	public Point getPoint() {
		return point;
	}

}
