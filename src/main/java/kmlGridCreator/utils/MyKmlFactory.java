package main.java.kmlGridCreator.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import com.peertopark.java.geocalc.Point;

import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.LineStyle;
import de.micromata.opengis.kml.v_2_2_0.PolyStyle;
import main.java.kmlGridCreator.exceptions.NoPolyStyleCoveringThisPointCountException;
import main.java.kmlGridCreator.exceptions.OverlappingPolyStylesException;
import main.java.kmlGridCreator.model.MyBoundingArea;
import main.java.kmlGridCreator.model.MyPoint;
import main.java.kmlGridCreator.model.Unused;
import main.java.kmlGridCreator.utils.styles.MyPolyStyle;
import main.java.kmlGridCreator.utils.styles.PolyStyleHandler;

public class MyKmlFactory {

	private Kml kml;
	private Document document;
	private PolyStyleHandler polyStyleHandler;

	public MyKmlFactory(String documentName) throws OverlappingPolyStylesException {
		this.kml = new Kml();
		this.document = this.kml.createAndSetDocument().withName(documentName);

		this.polyStyleHandler = new PolyStyleHandler();
		createPolyStyles();
	}

	private void createPolyStyles() throws OverlappingPolyStylesException {
		try {
			List<String> lines = Files.readAllLines(Paths.get("colors.txt"), Charset.forName("UTF-8"));

			lines.forEach(x -> {
				if (x.length() > 0) {

					int min = Integer.parseInt(x.substring(0, 3));
					int max = Integer.parseInt(x.substring(4, 7));
					String color = x.substring(8, 23);
					//System.out.println(color);

					int redInDec = Integer.parseInt(color.substring(0, 3));
					int blueInDec = Integer.parseInt(color.substring(4, 7));
					int greenInDec = Integer.parseInt(color.substring(8, 11));
					int alphaInDec = Integer.parseInt(color.substring(12, 15));

					String colorInHex = toFixedLength2(Integer.toHexString(alphaInDec)) + ""
							+ toFixedLength2(Integer.toHexString(blueInDec)) + ""
							+ toFixedLength2(Integer.toHexString(greenInDec)) + ""
							+ toFixedLength2(Integer.toHexString(redInDec));
					//System.out.println(colorInHex);

					try {
						polyStyleHandler.add(new MyPolyStyle(min, max, colorInHex));
					} catch (OverlappingPolyStylesException e) {
						e.printStackTrace();
					}
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
			polyStyleHandler.add(new MyPolyStyle(0, 10, "B2FFFFFF"));
			polyStyleHandler.add(new MyPolyStyle(11, 20, "B28C8CFF"));
			polyStyleHandler.add(new MyPolyStyle(21, 30, "B2000058"));
			polyStyleHandler.add(new MyPolyStyle(31, 100, "B20000B7"));
		}

		createPolyStylesInDocument();
	}

	private String toFixedLength2(String s) {
		while (s.length() < 2) {
			s = "0" + s;
		}
		return s;
	}

	private void createPolyStylesInDocument() {
		final LineStyle linestyle = document.createAndAddStyle().withId("linestyle").createAndSetLineStyle()
				.withColor("FFC0C0C0").withWidth(1.0);
		polyStyleHandler.getPolyStyles().forEach(x -> {
			PolyStyle poly = document.createAndAddStyle().withId("polystyle" + x.getId()).createAndSetPolyStyle()
					.withFill(true).withColor(x.getColor());
			document.createAndAddStyle().withId(x.getId()).withPolyStyle(poly).withLineStyle(linestyle);
		});
	}

	@Unused
	public void addPointsToKml(List<MyPoint> points) {
		if (points == null) {
			return;
		}

		for (int i = 0; i < points.size(); i++) {
			Point kmlPoint = points.get(i);
			document.createAndAddPlacemark().withOpen(Boolean.FALSE).createAndSetPoint()
					.addToCoordinates(kmlPoint.getLongitude(), kmlPoint.getLatitude());
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

	public void addBoundingArea(MyBoundingArea ba)
			throws OverlappingPolyStylesException, NoPolyStyleCoveringThisPointCountException {
		Point nw = ba.getNorthWest();
		Point ne = ba.getNorthEast();
		Point se = ba.getSouthEast();
		Point sw = ba.getSouthWest();

		String description = "<table border=\"1\" cellspacing= \"0\" cellpadding=\"3\" width=\"300\"><tr><td width=\"90\"><b>Feldname</b></td><td><b>Feldwert</b></td></tr><tr> <td>Count</td><td>"
				+ ba.getPointCount() + "</td></tr><tr><td>Id</td><td>" + ba.getId() + "</td></tr></table>";

		document.createAndAddPlacemark().withName("" + ba.getPointCount()).withDescription(description)
				.withStyleUrl(polyStyleHandler.getPolyStyleForPointCount(ba.getPointCount()).getStyleURL())
				.createAndSetPolygon().createAndSetOuterBoundaryIs().createAndSetLinearRing()
				.addToCoordinates(nw.getLongitude() + "," + nw.getLatitude())
				.addToCoordinates(ne.getLongitude() + "," + ne.getLatitude())
				.addToCoordinates(se.getLongitude() + "," + se.getLatitude())
				.addToCoordinates(sw.getLongitude() + "," + sw.getLatitude())
				.addToCoordinates(nw.getLongitude() + "," + nw.getLatitude());
	}

	public Kml getKml() {
		return kml;
	}

	public Document getDocument() {
		return document;
	}

}
