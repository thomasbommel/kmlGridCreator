package main.java.kmlGridCreator.utils.styles;

public final class PolyStyle {

	private int minPointCount, maxPointCount;
	private String color;

	public PolyStyle(int minPointCount, int maxPointCount, String color) {
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
}
