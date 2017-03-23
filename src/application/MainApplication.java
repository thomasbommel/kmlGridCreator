package application;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.peertopark.java.geocalc.Point;

import kml.MyKmlFactory;
import map.Grid;
import map.GridField;
import utils.GeoUtil;
import utils.Logger;
import utils.Logger.LogLevel;
import utils.TxtUtil;

public class MainApplication {

	public static final Logger log = new Logger(LogLevel.DEBUG);

	public static void main(String[] args) throws IOException {
		List<Point> allPoints = TxtUtil.getTestPoints();

		Point[] tests = GeoUtil.getCornersNwNeSeSw(allPoints);

		System.out.println(Arrays.asList(tests));

		System.out.println(GeoUtil.calculateDistanceInMeter(tests[0], tests[3]));// nw-sw
		System.out.println(GeoUtil.calculateDistanceInMeter(tests[1], tests[2]));// ne-se

		System.out.println(GeoUtil.calculateDistanceInMeter(tests[0], tests[1]));// nw-ne
		System.out.println(GeoUtil.calculateDistanceInMeter(tests[3], tests[2]));// sw-se

		final int GRIDSIZE = 3000;
		Grid grid = new Grid(tests[0], tests[2], GRIDSIZE);

		MyKmlFactory kml = new MyKmlFactory();
		kml.addPointsToKml(Arrays.asList(tests));

		List<GridField> grids = grid.getGridFields();

		// grids.forEach(gf ->
		// kml.createAndAddGroundOverlayToKml(Arrays.asList(gf.getNwPoint()),
		// 1000));

		grids.forEach(gf -> kml.createAndAddColoredGroundOverlayToKml(gf, GRIDSIZE));

		// Arrays.asList(tests).stream().forEach(System.out::println);

		kml.saveKmlFile("testkml");
	}

}
