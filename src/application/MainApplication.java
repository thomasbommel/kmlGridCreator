package application;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.peertopark.java.geocalc.Point;

import kml.MyKmlFactory;
import utils.GeoUtil;
import utils.TxtUtil;

public class MainApplication {

	public static void main(String[] args) throws IOException {

		List<Point> allPoints = TxtUtil.getTestPoints();

		Point[] tests = GeoUtil.getCornersNwNeSeSw(allPoints);
		System.out.println(Arrays.asList(tests));

		MyKmlFactory kml = new MyKmlFactory();
		kml.addPointsToKml(Arrays.asList(tests));
		kml.saveKmlFile("testkml");
	}

}
