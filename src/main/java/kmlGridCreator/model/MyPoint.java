package main.java.kmlGridCreator.model;

import com.peertopark.java.geocalc.Coordinate;
import com.peertopark.java.geocalc.Point;

import main.java.kmlGridCreator.utils.MathUtils;

public class MyPoint extends Point {

	public MyPoint(Coordinate latitude, Coordinate longitude) {
		super(latitude, longitude);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}

		MyPoint other = (MyPoint) obj;
		return MathUtils.equals(getLatitude(), other.getLatitude()) &&
				MathUtils.equals(getLongitude(), other.getLongitude());
	}

}
