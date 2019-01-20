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
public class MyPointForConverterWithDateAndDistance extends MyPoint {
	private Double distance;
	private String date, hourAndMinute;

	public MyPointForConverterWithDateAndDistance(Coordinate latitude, Coordinate longitude, Double distance, String date, String hourAndMinute) {
		super(latitude, longitude);
		this.distance = distance;
		this.date = date;
		this.hourAndMinute = hourAndMinute;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}

		MyPointForConverterWithDateAndDistance other = (MyPointForConverterWithDateAndDistance) obj;
		return MathUtils.equals(getLatitude(), other.getLatitude())
				&& MathUtils.equals(getLongitude(), other.getLongitude());
	}

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getHourAndMinute() {
		return hourAndMinute;
	}

	public void setHourAndMinute(String hourAndMinute) {
		this.hourAndMinute = hourAndMinute;
	}

	

}
