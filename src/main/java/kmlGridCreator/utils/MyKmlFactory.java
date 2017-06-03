package main.java.kmlGridCreator.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import com.peertopark.java.geocalc.Point;

import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.LineStyle;
import de.micromata.opengis.kml.v_2_2_0.PolyStyle;
import main.java.kmlGridCreator.model.MyBoundingArea;
import main.java.kmlGridCreator.model.Unused;

public class MyKmlFactory {

	private Kml kml;
	private Document document;

	public MyKmlFactory(String documentName) {
		this.kml = new Kml();
		this.document = this.kml.createAndSetDocument().withName(documentName);

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

	@Unused
	public void addPointsToKml(List<Point> points) {
		if (points == null) {
			return;
		}

		for (int i = 0; i < points.size(); i++) {
			Point kmlPoint = points.get(i);
			document.createAndAddPlacemark().withOpen(Boolean.FALSE).createAndSetPoint().addToCoordinates(kmlPoint.getLongitude(),
					kmlPoint.getLatitude());
		}
	}

	public void saveKmlFile(File outputFile) {
		try {
			kml.marshal(outputFile);
			// System.out.println("kml " + outputFile.getAbsolutePath() + "
			// saved");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void addBoundingArea(MyBoundingArea ba) {

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

	public Kml getKml() {
		return kml;
	}

	public Document getDocument() {
		return document;
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
