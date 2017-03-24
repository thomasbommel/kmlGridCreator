package map;

import java.util.ArrayList;
import java.util.List;

import com.peertopark.java.geocalc.DegreeCoordinate;
import com.peertopark.java.geocalc.EarthCalc;
import com.peertopark.java.geocalc.Point;

import application.MainApplication;
import utils.Logger;

public class Grid {
	private static final Logger log = MainApplication.log;

	private final Point nwPoint, sePoint;
	private final List<GridField> gridFields;
	private final int gridSizeInM;

	public final List<Point> unusedPoints;

	public Grid(Point nwPoint, Point sePoint, int gridSizeInM) {
		this.nwPoint = nwPoint;
		this.sePoint = sePoint;
		this.gridSizeInM = gridSizeInM;
		this.gridFields = new ArrayList<>();
		this.unusedPoints = new ArrayList<>();
		generateGridFields();
	}

	/**
	 * generates the Grid fields which can be filled with all the points
	 */
	private void generateGridFields() {
		final int unnecessaryBound = 5;
		// to make the grid a bit bigger than the absolute minimum
		Point p = EarthCalc.pointRadialDistance(EarthCalc.pointRadialDistance(nwPoint, -90, gridSizeInM * unnecessaryBound), 0, gridSizeInM * unnecessaryBound);

		// go east
		for (int a = 0; a < Integer.MAX_VALUE; a++) {
			DegreeCoordinate lng = new DegreeCoordinate(EarthCalc.pointRadialDistance(p, 90, gridSizeInM).getLongitude());
			p = new Point(new DegreeCoordinate(p.getLatitude()), lng);
			gridFields.add(new GridField(p));
			if (p.getLongitude() > EarthCalc.pointRadialDistance(sePoint, 90, gridSizeInM * unnecessaryBound).getLongitude()) {
				break;
			}
		}

		final ArrayList<GridField> newg = new ArrayList<>();

		// go south
		for (int k = 0; k < gridFields.size(); k++) {
			GridField b = gridFields.get(k);
			for (int c = 0; c < Integer.MAX_VALUE; c++) {
				Point n = new Point(new DegreeCoordinate(EarthCalc.pointRadialDistance(b.getNwPoint(), 180, gridSizeInM * c).getLatitude()),
						new DegreeCoordinate(b.getNwPoint().getLongitude()));
				newg.add(new GridField(n));
				if (n.getLatitude() < EarthCalc.pointRadialDistance(sePoint, 180, gridSizeInM * unnecessaryBound).getLatitude()) {
					break;
				}
			}
		}
		gridFields.addAll(newg);

		gridFields.forEach(bb -> {
			for (int k = 0; k < gridFields.size(); k++) {
				if (gridFields.get(k).getNwPoint().getLatitude() == bb.getNwPoint().getLatitude()) {
					// System.out.println(EarthCalc.getBearing(gridFields.get(k).getNwPoint(),
					// bb.getNwPoint()));
				}
			}

		});

		// gridFields.forEach(gf -> System.out.println(gf.getNwPoint()));

		log.debug(gridFields.size() + " GridFields created");
	}

	private static int count = 0;

	public void addPoint(Point p) {
		Point gNW, gNE, gSW, gSE;

		for (GridField gf : gridFields) {
			gNW = gf.getNwPoint();
			DegreeCoordinate lng = new DegreeCoordinate(EarthCalc.pointRadialDistance(gNW, 90, gridSizeInM).getLongitude());
			gNE = new Point(new DegreeCoordinate(p.getLatitude()), lng);

			gSW = new Point(new DegreeCoordinate(EarthCalc.pointRadialDistance(gNW, 180, gridSizeInM).getLatitude()),
					new DegreeCoordinate(gNW.getLongitude()));

			gSE = new Point(new DegreeCoordinate(gSW.getLatitude()), new DegreeCoordinate(gNE.getLongitude()));

			if (p.getLatitude() <= gNW.getLatitude() && p.getLatitude() >= gSW.getLatitude() && p.getLongitude() >= gNW.getLongitude()
					&& p.getLongitude() <= gSE.getLongitude()) {
				gf.addPoint(p);
				return;
			}

		}

		// Point cNW = null;
		// Point cNE = null;
		// Point cSW = null;
		// Point cSE = null;
		//
		// for (GridField gf : gridFields) {
		// cNW = gf.getNwPoint();
		// cNE = EarthCalc.pointRadialDistance(cNW, 90, gridSizeInM + 1);
		// cSW = EarthCalc.pointRadialDistance(cNW, 180, gridSizeInM + 1);
		// cSE = EarthCalc.pointRadialDistance(cSW, 90, gridSizeInM + 1);
		//
		// double lat = p.getLatitude();
		// double lng = p.getLongitude();
		//
		// if (((lat <= cNW.getLatitude() && lat >= cSW.getLatitude()) || (lat
		// <= cNE.getLatitude() && lat >= cSE.getLatitude())) &&
		// ((lng >= cNW.getLongitude() && lng <= cNE.getLongitude()) || (lng >=
		// cSW.getLongitude() && lng <= cSE.getLongitude()))) {
		// gf.addPoint(p);
		// return;
		// }
		//
		// }
		unusedPoints.add(p);
	}

	public List<GridField> getGridFields() {
		return this.gridFields;
	}

	public Point getNwPoint() {
		return nwPoint;
	}

	public Point getSePoint() {
		return sePoint;
	}

	public int getGridSizeInM() {
		return gridSizeInM;
	}

}
