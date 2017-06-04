package main.java.kmlGridCreator.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.peertopark.java.geocalc.EarthCalc;
import com.peertopark.java.geocalc.Point;

import main.java.kmlGridCreator.utils.GeoUtil;
import main.java.kmlGridCreator.view.View;

public class MyMap {

	private MyPoint nwPoint, nePoint, swPoint, sePoint;
	private MyPoint[] corners;
	private List<MyBoundingArea> boundingAreas;
	private int GRID_SIZE_IN_M;
	private View view;
	private List<MyPoint> points;

	public MyMap(List<MyPoint> points, int gridSizeInM, View view) {
		super();
		this.points = points;
		this.GRID_SIZE_IN_M = gridSizeInM;
		this.view = view;

		this.corners = GeoUtil.getCornersNwNeSeSw(points);
		this.nwPoint = corners[0];
		this.nePoint = corners[1];
		this.sePoint = corners[2];
		this.swPoint = corners[3];
		this.boundingAreas = generateBoundingAreas(points);
	}
	
	private List<MyBoundingArea> generateBoundingAreas(List<MyPoint> points) {
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
		long starttime = System.currentTimeMillis();

		view.printToViewConsole("--- " + view.formatForConsole(points.size()) + " Punkte werden hinzugefügt. ---");
		final int pointsSize = points.size();

		for (int i = 0; i < pointsSize; i++) {
			if (i % 10000 == 0) {
				view.printToViewConsole("bisher <=" + view.formatForConsole(i + 10000) + " von " + view.formatForConsole(pointsSize) + " Punkten hinzugefügt.");
			}

			final Point p = points.get(i);
			final Runnable r = new Runnable() {
				@Override
				public void run() {
					for (final MyBoundingArea ba : boundingAreas) {
						if (ba.tryToAddPoint(p)) {
							return;
						}
					}
				}
			};
			r.run();
		}

		final int addedPointsCount = boundingAreas.stream().mapToInt(x -> x.getPointCount()).sum();
		view.printToViewConsole(
				view.formatForConsole(addedPointsCount) + " Punkte wurden " +
						view.formatForConsole(boundingAreas.size()) + " Feldern hinzugefügt, "
						+ view.formatForConsole(points.size() - addedPointsCount)
						+ " Punkte konnten keinem Feld zugeordnet werden.");
		//System.out.println(System.currentTimeMillis() - starttime);
	}
	
	public int getAveragePointCountPerBoundingArea(){
		if(points == null || points.size()==0){
			return 0;
		}
		return Math.round(boundingAreas.size()/points.size());
	}
	
	
	public Map<Integer, Integer> getPointCountToBoundingAreaCountMap(){
		
		Map<Integer, List<MyBoundingArea>> map = boundingAreas.stream()
				.collect(Collectors.groupingBy(x->x.getPointCount()));
		Map<Integer,Integer> mapFromPointCountToCorrespondingBAcount= new HashMap<>();
		map.entrySet().forEach(x->mapFromPointCountToCorrespondingBAcount.put(x.getKey(), x.getValue().size()));
		
		// add missing (with no corresponding BoundingArea)
		int max = mapFromPointCountToCorrespondingBAcount.keySet().stream().mapToInt(Integer::intValue).max().orElse(0);
		for(int i=0;i<=max;i++){
			if(!mapFromPointCountToCorrespondingBAcount.containsKey(new Integer(i))){
				mapFromPointCountToCorrespondingBAcount.put(new Integer(i), 0);
			}
		}
		return mapFromPointCountToCorrespondingBAcount;
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
