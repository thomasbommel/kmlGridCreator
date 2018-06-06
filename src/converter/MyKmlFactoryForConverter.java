package converter;

import java.awt.Color;
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
import main.java.kmlGridCreator.model.Unused;
import main.java.kmlGridCreator.utils.styles.MyPolyStyle;
import main.java.kmlGridCreator.utils.styles.PolyStyleHandler;

public class MyKmlFactoryForConverter {

	private Kml kml;
	private Document document;
	private PolyStyleHandler polyStyleHandler;

	public MyKmlFactoryForConverter(String documentName) throws OverlappingPolyStylesException {
		System.out.println("MyKmlFactory");
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
					int greenInDec = Integer.parseInt(color.substring(4, 7));
					int blueInDec = Integer.parseInt(color.substring(8, 11));
					int alphaInDec = Integer.parseInt(color.substring(12, 15));

					Color colo = new Color(redInDec, greenInDec, blueInDec, alphaInDec);
					
					try {
						polyStyleHandler.add(new MyPolyStyle(min, max,colo));
					} catch (OverlappingPolyStylesException e) {
						e.printStackTrace();
					}
				}
			});
		} catch (IOException e) {
			System.out.println("default points used because colors.txt was missing in kmlGridCreator folder");
			polyStyleHandler.addDefaultPolyStyles();
		}

		
	}

	public  void createPolyStylesInDocument() {
		final LineStyle linestyle = document.createAndAddStyle().withId("linestyle").createAndSetLineStyle()
				.withColor("FFC0C0C0").withWidth(1.0);
		polyStyleHandler.getPolyStyles().forEach(x -> {
			PolyStyle poly = document.createAndAddStyle().withId("polystyle" + x.getId()).createAndSetPolyStyle()
					.withFill(true).withColor(x.getColor());
			document.createAndAddStyle().withId(x.getId()).withPolyStyle(poly).withLineStyle(linestyle);
		});
	}

	@Unused
	public void addPointsToKml(List<MyPointForConverter> points) {
		if (points == null) {
			return;
		}

		for (int i = 0; i < points.size(); i++) {
			MyPointForConverter kmlPoint = points.get(i);
			document.createAndAddPlacemark().withName("#"+i+", "+kmlPoint.toString()).withOpen(Boolean.TRUE).createAndSetPoint()
					.addToCoordinates(kmlPoint.getLongitude(), kmlPoint.getLatitude());
//			document.createAndAddPlacemark().withName(kmlPoint.toString()).withOpen(Boolean.TRUE).createAndSetPoint()
//			.addToCoordinates(kmlPoint.getLongitude(), kmlPoint.getLatitude());
		}
	}

	public void saveKmlFile(File outputFile) {
		try {
			kml.marshal(outputFile);
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

	public PolyStyleHandler getPolyStyleHandler() {
		return polyStyleHandler;
	}

}
