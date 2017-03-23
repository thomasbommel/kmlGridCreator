package kml;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import com.peertopark.java.geocalc.EarthCalc;
import com.peertopark.java.geocalc.Point;

import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import map.GridField;

public class MyKmlFactory {

	private Kml kml;
	private Document document;

	public MyKmlFactory() {
		this.kml = new Kml();
		this.document = this.kml.createAndSetDocument().withName("kmlDocument");
	}

	public void addPointsToKml(List<Point> points) {
		for (int i = 0; i < points.size(); i++) {
			Point kmlPoint = points.get(i);
			document.createAndAddPlacemark().withName("point " + i).withOpen(Boolean.TRUE).createAndSetPoint().addToCoordinates(kmlPoint.getLongitude(),
					kmlPoint.getLatitude());
		}
	}

	@Deprecated
	public void createAndAddGroundOverlayToKml(List<Point> points, int gridSizeInM) {
		for (Point pp : points) {
			document.createAndAddGroundOverlay()
					.withName("test")
					.withDescription("testdescription")
					.withColor("88aaffff")
					.createAndSetLatLonBox()
					.withNorth(pp.getLatitude())
					.withSouth(EarthCalc.pointRadialDistance(pp, 0, gridSizeInM * 0.96).getLatitude())
					.withWest(pp.getLongitude())
					.withEast(EarthCalc.pointRadialDistance(pp, 90, gridSizeInM * 0.96).getLongitude());
		}
	}

	public void createAndAddColoredGroundOverlayToKml(GridField gridField, int gridSizeInM) {
		document.createAndAddGroundOverlay()
				.withName("test")
				.withDescription("testdescription")
				.withColor(ColorScheme.getColor(gridField.getCount()))
				.createAndSetLatLonBox()
				.withNorth(gridField.getNwPoint().getLatitude())
				.withSouth(EarthCalc.pointRadialDistance(gridField.getNwPoint(), 0, gridSizeInM * 0.96).getLatitude())
				.withWest(gridField.getNwPoint().getLongitude())
				.withEast(EarthCalc.pointRadialDistance(gridField.getNwPoint(), 90, gridSizeInM * 0.96).getLongitude());
	}

	public void saveKmlFile(String name) {
		try {
			kml.marshal(new File(name + ".kml"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}
