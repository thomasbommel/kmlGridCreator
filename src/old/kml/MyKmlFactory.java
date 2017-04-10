package old.kml;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import com.peertopark.java.geocalc.DMSCoordinate;
import com.peertopark.java.geocalc.DegreeCoordinate;
import com.peertopark.java.geocalc.Point;

import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.LineStyle;
import de.micromata.opengis.kml.v_2_2_0.PolyStyle;
import old.map.MyOldBoundingArea;
import old.utils.Logger;
import old.utils.Logger.LogLevel;

public class MyKmlFactory {
	private static final old.utils.Logger log = new Logger(LogLevel.DEBUG);

	private Kml kml;
	private Document document;

	public MyKmlFactory() {
		this.kml = new Kml();
		this.document = this.kml.createAndSetDocument().withName("kmlDocument");

		final LineStyle linestyle = document.createAndAddStyle().withId("linestyle").createAndSetLineStyle().withColor("FFC0C0C0").withWidth(1.0);
		final PolyStyle poly1 = document.createAndAddStyle().withId("polystyle").createAndSetPolyStyle().withFill(true).withColor("B2FFFFFF");
		final PolyStyle poly2 = document.createAndAddStyle().withId("polystyle").createAndSetPolyStyle().withFill(true).withColor("B2E6E6FF");
		final PolyStyle poly3 = document.createAndAddStyle().withId("polystyle").createAndSetPolyStyle().withFill(true).withColor("B28C8CFF");
		final PolyStyle poly4 = document.createAndAddStyle().withId("polystyle").createAndSetPolyStyle().withFill(true).withColor("B20000B7");
		final PolyStyle poly5 = document.createAndAddStyle().withId("polystyle").createAndSetPolyStyle().withFill(true).withColor("B2000058");

		document.createAndAddStyle().withId("poly1").withPolyStyle(poly1).withLineStyle(linestyle);
		document.createAndAddStyle().withId("poly2").withPolyStyle(poly2).withLineStyle(linestyle);
		document.createAndAddStyle().withId("poly3").withPolyStyle(poly3).withLineStyle(linestyle);
		document.createAndAddStyle().withId("poly4").withPolyStyle(poly4).withLineStyle(linestyle);
		document.createAndAddStyle().withId("poly5").withPolyStyle(poly5).withLineStyle(linestyle);
	}

	public void addPointsToKml(List<Point> points) {
		for (int i = 0; i < points.size(); i++) {
			Point kmlPoint = points.get(i);

			DegreeCoordinate lat = new DegreeCoordinate(kmlPoint.getLatitude());
			DegreeCoordinate lng = new DegreeCoordinate(kmlPoint.getLongitude());

			DMSCoordinate latt = lat.getDMSCoordinate();
			DMSCoordinate lngg = lng.getDMSCoordinate();

			document.createAndAddPlacemark().withOpen(Boolean.FALSE)
					.withName(latt.getWholeDegrees() + "," + (latt.getMinutes() + latt.getSeconds() / 60) + "   " + lngg.getWholeDegrees() + ","
							+ (lngg.getMinutes() + lngg.getSeconds() / 60))
					.createAndSetPoint()
					.addToCoordinates(kmlPoint.getLongitude(),
							kmlPoint.getLatitude());
		}
	}

	public void saveKmlFile(String name) {
		try {
			kml.marshal(new File(name + ".kml"));
			log.debug("kml " + name + " saved");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void addBoundingArea(MyOldBoundingArea ba) {

		Point nw = ba.getNorthWest();
		Point ne = ba.getNorthEast();
		Point se = ba.getSouthEast();
		Point sw = ba.getSouthWest();

		// select which style=color to display the field
		String style = "#poly";
		int st = 1;
		int c = ba.getPointCount();
		if (c > 30) {
			st = 5;
		} else if (c >= 10) {
			st = 4;
		} else if (c >= 5) {
			st = 3;
		} else if (c > 0) {
			st = 2;
		}

		style += st;

		String description = "<table border=\"1\" cellspacing= \"0\" cellpadding=\"3\" width=\"300\"><tr><td width=\"90\"><b>Feldname</b></td><td><b>Feldwert</b></td></tr><tr> <td>Count</td><td>"
				+ ba.getPointCount() + "</td></tr><tr><td>Id</td><td>" + ba.getId() + "</td></tr></table>";

		document.createAndAddPlacemark().withName("" + ba.getPointCount()).withDescription(description).withStyleUrl(style)
				.createAndSetPolygon()
				.createAndSetOuterBoundaryIs()
				.createAndSetLinearRing()
				.addToCoordinates(nw.getLongitude() + "," + nw.getLatitude())
				.addToCoordinates(ne.getLongitude() + "," + ne.getLatitude())
				.addToCoordinates(se.getLongitude() + "," + se.getLatitude())
				.addToCoordinates(sw.getLongitude() + "," + sw.getLatitude())
				.addToCoordinates(nw.getLongitude() + "," + nw.getLatitude());

		// document.createAndAddPlacemark().withName("Feld id: " +
		// ba.getId()).withDescription("count: " +
		// ba.getPointCount()).withStyleUrl(style)
		// .createAndSetPolygon()
		// .createAndSetOuterBoundaryIs()
		// .createAndSetLinearRing()
		// .addToCoordinates(nw.getLongitude() + "," + nw.getLatitude())
		// .addToCoordinates(ne.getLongitude() + "," + ne.getLatitude())
		// .addToCoordinates(se.getLongitude() + "," + se.getLatitude())
		// .addToCoordinates(sw.getLongitude() + "," + sw.getLatitude())
		// .addToCoordinates(nw.getLongitude() + "," + nw.getLatitude());

	}

}

// document.createAndAddPlacemark().withName("Folder object 2
// (Polygon)").withStyleUrl("#style")
// .createAndSetPolygon()
// .createAndSetOuterBoundaryIs()
// .createAndSetLinearRing()
// .addToCoordinates(nw.getLongitude() + "," + nw.getLatitude())
// .addToCoordinates(ne.getLongitude() + "," + ne.getLatitude())
// .addToCoordinates(se.getLongitude() + "," + se.getLatitude())
// .addToCoordinates(sw.getLongitude() + "," + sw.getLatitude())
// .addToCoordinates(nw.getLongitude() + "," + nw.getLatitude());
