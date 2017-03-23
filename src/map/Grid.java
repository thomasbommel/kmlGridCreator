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

	public Grid(Point nwPoint, Point sePoint, int gridSizeInM) {
		this.nwPoint = nwPoint;
		this.sePoint = sePoint;
		this.gridSizeInM = gridSizeInM;
		this.gridFields = new ArrayList<>();
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

		// gridFields.forEach(gf -> System.out.println(gf.getNwPoint()));

		log.debug(gridFields.size() + " GridFields created");
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
