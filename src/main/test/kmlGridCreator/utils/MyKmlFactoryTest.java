package main.test.kmlGridCreator.utils;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.testng.Assert;

import com.peertopark.java.geocalc.DegreeCoordinate;
import com.peertopark.java.geocalc.Point;

import main.java.kmlGridCreator.utils.MyKmlFactory;
import main.test.kmlGridCreator.TestUtil;

public class MyKmlFactoryTest {

	@Test
	public void testAddPointsToKmlNulLValue() {
		MyKmlFactory kml = new MyKmlFactory("testKmlDocument");
		kml.addPointsToKml(null);
	}

	@Test
	public void testAddPointsToKmlNoPoints() throws FileNotFoundException {
		MyKmlFactory kml = new MyKmlFactory("testKmlDocument");
		kml.addPointsToKml(new ArrayList<>());
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		kml.getKml().marshal(out);
		List<String> generatedPoints = TestUtil.extractPointFromKml(out.toString());
		Assert.assertEquals(generatedPoints.size(), 0);
	}

	@Test
	public void testAddPointsToKmlManyPoints() throws FileNotFoundException {
		MyKmlFactory kml = new MyKmlFactory("testKmlDocument");
		List<Point> points = new ArrayList<>();

		for (int i = 0; i < 50000; i++) {
			points.add(new Point(new DegreeCoordinate(Math.random() * 10000), new DegreeCoordinate(Math.random() * 10000)));
		}
		kml.addPointsToKml(points);
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		kml.getKml().marshal(out);
		List<String> generatedPoints = TestUtil.extractPointFromKml(out.toString());
		Assert.assertEquals(generatedPoints.size(), points.size());

		for (int i = 0; i < points.size(); i++) {
			String generatedLat = generatedPoints.get(i).split(",")[1];
			String generatedLng = generatedPoints.get(i).split(",")[0];

			Assert.assertTrue(generatedLat.contains("" + points.get(i).getLatitude()));
			Assert.assertTrue(generatedLng.contains("" + points.get(i).getLongitude()));
		}
	}

}
