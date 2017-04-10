package kmlGridCreator.model;

import static kmlGridCreator.view.MainApplication.formatForConsole;

import java.util.ArrayList;
import java.util.List;

import com.peertopark.java.geocalc.EarthCalc;
import com.peertopark.java.geocalc.Point;

import kmlGridCreator.utils.GeoUtil;
import kmlGridCreator.view.View;

public class MyMap {

	private Point nwPoint, nePoint, swPoint, sePoint;
	private Point[] corners;
	private List<MyBoundingArea> boundingAreas;
	private final int GRID_SIZE_IN_M;
	private View view;
	private List<Point> points;

	public MyMap(List<Point> points, int gridSize, View view) {
		super();
		this.points = points;
		this.GRID_SIZE_IN_M = gridSize;
		this.view = view;

		this.corners = GeoUtil.getCornersNwNeSeSw(points);
		this.nwPoint = corners[0];
		this.nePoint = corners[1];
		this.sePoint = corners[2];
		this.swPoint = corners[3];
		this.boundingAreas = generateBoundingAreas(points);
	}

	private List<MyBoundingArea> generateBoundingAreas(List<Point> points) {
		List<MyBoundingArea> areas = new ArrayList<>();
		Point startPointNW = nwPoint;

		startPointNW = EarthCalc.pointRadialDistance(EarthCalc.pointRadialDistance(startPointNW, 0, GRID_SIZE_IN_M * 2), 270, GRID_SIZE_IN_M * 2);

		Point nwPointOfField = startPointNW;

		Point sw = EarthCalc.pointRadialDistance(EarthCalc.pointRadialDistance(startPointNW, 90, GRID_SIZE_IN_M), 180, GRID_SIZE_IN_M);

		int xSize = Math.max((int) Math.ceil(EarthCalc.getHarvesineDistance(corners[0], corners[1]) / GRID_SIZE_IN_M),
				(int) Math.ceil(EarthCalc.getHarvesineDistance(corners[2], corners[3]) / GRID_SIZE_IN_M)) + 2;
		int ySize = Math.max((int) Math.ceil(EarthCalc.getHarvesineDistance(corners[0], corners[3]) / GRID_SIZE_IN_M),
				(int) Math.ceil(EarthCalc.getHarvesineDistance(corners[1], corners[2]) / GRID_SIZE_IN_M)) + 4;
		view.printToViewConsole("xSize: " + xSize);
		view.printToViewConsole("ySize: " + ySize);

		for (int yIndex = 0; yIndex < ySize; yIndex++) {// height
			for (int xIndex = 0; xIndex < xSize; xIndex++) {// width
				nwPointOfField = (EarthCalc.pointRadialDistance(nwPointOfField, 90, GRID_SIZE_IN_M));
				sw = EarthCalc.pointRadialDistance(EarthCalc.pointRadialDistance(nwPointOfField, 270, GRID_SIZE_IN_M), 180, GRID_SIZE_IN_M);

				MyBoundingArea ba = new MyBoundingArea(nwPointOfField, sw, yIndex + " | " + xIndex);
				areas.add(ba);
			}
			nwPointOfField = (EarthCalc.pointRadialDistance(startPointNW, 180, GRID_SIZE_IN_M * (yIndex + 1)));
		}
		return areas;
	}

	public void addBoundingArea(MyBoundingArea area) {
		this.boundingAreas.add(area);
	}

	public void addPointsToTheAreas() {

		view.printToViewConsole("--- " + formatForConsole(points.size()) + " Punkte werden hinzugefügt. ---");
		final int pointsSize = points.size();
		for (int i = 0; i < pointsSize; i++) {
			if (i % 10000 == 0) {
				view.printToViewConsole("bisher " + formatForConsole(i + 10000) + " von " + formatForConsole(pointsSize) + " Punkten hinzugefügt.");
			}

			for (final MyBoundingArea ba : boundingAreas) {
				if (ba.addPoint(points.get(i))) {
					break;
				}
			}
		}

		final int addedPointsCount = boundingAreas.stream().mapToInt(x -> x.getPointCount()).sum();
		view.printToViewConsole(
				formatForConsole(addedPointsCount) + " Punkte wurden " + formatForConsole(boundingAreas.size()) + " Feldern hinzugefügt, "
						+ formatForConsole(points.size() - addedPointsCount)
						+ " Punkte konnten keinem Feld zugeordnet werden.");
	}

	// ==================================================
	public Point getNwPoint() {
		return nwPoint;
	}

	public Point getNePoint() {
		return nePoint;
	}

	public Point getSwPoint() {
		return swPoint;
	}

	public Point getSePoint() {
		return sePoint;
	}

	public Point[] getCorners() {
		return corners;
	}

	public List<MyBoundingArea> getBoundingAreas() {
		return boundingAreas;
	}

	public int getGRID_SIZE_IN_M() {
		return GRID_SIZE_IN_M;
	}

}
