package main.java.kmlGridCreator.utils.styles;

import de.micromata.opengis.kml.v_2_2_0.PolyStyle;

public final class MyPolyStyle extends PolyStyle {

	private int minPointCount, maxPointCount;
	private String color;

	public MyPolyStyle(int minPointCount, int maxPointCount, String color) {
		super();
		this.minPointCount = minPointCount;
		this.maxPointCount = maxPointCount;
		this.color = color;
	}

	public boolean rangeCoveredByThisPolyStyle(int minCount, int maxCount) {
		return (minCount >= minPointCount && minCount <= maxPointCount) || (maxCount >= minPointCount && maxCount <= maxPointCount);
	}

	public boolean countCoveredByThisPolyStyle(int pointCount) {
		return (pointCount >= minPointCount && pointCount <= maxPointCount);
	}

	// ------------------------------------------------------------------/
	public String getColor() {
		return this.color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public int getMinPointCount() {
		return this.minPointCount;
	}

	public int getMaxPointCount() {
		return this.maxPointCount;
	}
	
	public String getId(){
		return this.minPointCount+"_"+this.maxPointCount;
	}
	
	public String getStyleURL(){
		return "#"+this.getId();
	}
}
