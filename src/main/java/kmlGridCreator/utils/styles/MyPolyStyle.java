package main.java.kmlGridCreator.utils.styles;

import java.awt.Color;

import de.micromata.opengis.kml.v_2_2_0.PolyStyle;
import main.java.kmlGridCreator.utils.StringUtils;

public final class MyPolyStyle extends PolyStyle {

	private int minPointCount, maxPointCount;
	private String colorForKml;
	private Color colorForTxt;

	public MyPolyStyle(int minPointCount, int maxPointCount, Color c) {
		super();
		this.minPointCount = minPointCount;
		this.maxPointCount = maxPointCount;
		this.colorForTxt = c;
		this.colorForKml = ColorUtils.convertColorToKMLColorString(c);
	}

	public boolean rangeCoveredByThisPolyStyle(int minCount, int maxCount) {
		return (minCount >= minPointCount && minCount <= maxPointCount) || (maxCount >= minPointCount && maxCount <= maxPointCount);
	}

	public boolean countCoveredByThisPolyStyle(int pointCount) {
		return (pointCount >= minPointCount && pointCount <= maxPointCount);
	}

	// ------------------------------------------------------------------/
	public String getColor() {
		return this.colorForKml;
	}

//	public void setColor(String color) {
//		this.color = color;
//	}

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
	
	public String getPolyStyleStringForTxt(){
		String s = "";
		s+=StringUtils.toFixedLength3(""+minPointCount)+" ";
		s+=StringUtils.toFixedLength3(""+maxPointCount)+" ";
		s+=StringUtils.toFixedLength3(""+colorForTxt.getRed())+",";
		s+=StringUtils.toFixedLength3(""+colorForTxt.getGreen())+",";
		s+=StringUtils.toFixedLength3(""+colorForTxt.getBlue())+",";
		s+=StringUtils.toFixedLength3(""+colorForTxt.getAlpha());
		return s;
	}

	public void setColorForTxt(Color c) {
		this.colorForTxt = c;
		colorForKml = ColorUtils.convertColorToKMLColorString(c);
		
	}
}
