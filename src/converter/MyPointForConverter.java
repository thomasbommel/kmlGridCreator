package converter;

import com.peertopark.java.geocalc.Coordinate;
import com.peertopark.java.geocalc.Point;

import main.java.kmlGridCreator.model.MyPoint;
import main.java.kmlGridCreator.utils.MathUtils;

/**
 * this class does the same as the {@link com.peertopark.java.geocalc.Point}
 * class,<br/>
 * only the <b>equals method is changed</b>
 * 
 * @author Bommel
 *
 */
public class MyPointForConverter extends MyPoint {
	private String name;

	public MyPointForConverter(Coordinate latitude, Coordinate longitude, String name) {
		super(latitude, longitude);
		this.name = name;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}

		MyPointForConverter other = (MyPointForConverter) obj;
		return MathUtils.equals(getLatitude(), other.getLatitude())
				&& MathUtils.equals(getLongitude(), other.getLongitude());
	}

	@Override
	public String toString() {
		return name;
	}

}
