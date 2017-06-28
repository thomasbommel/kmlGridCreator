package main.java.kmlGridCreator.utils.styles;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import main.java.kmlGridCreator.exceptions.NoPolyStyleCoveringThisPointCountException;
import main.java.kmlGridCreator.exceptions.OverlappingPolyStylesException;

public final class PolyStyleHandler {
	private List<MyPolyStyle> polyStyles;

	public PolyStyleHandler() {
		this.polyStyles = new ArrayList<>();
	}

	public List<MyPolyStyle> getPolyStyles() {
		return polyStyles;
	}

	public void add(MyPolyStyle polyStyle) throws OverlappingPolyStylesException {
		if (polyStyles.stream().anyMatch(
				x -> x.rangeCoveredByThisPolyStyle(polyStyle.getMinPointCount(), polyStyle.getMaxPointCount()))) {
			throw new OverlappingPolyStylesException("the range [" + polyStyle.getMinPointCount() + ","
					+ polyStyle.getMaxPointCount() + "] is already used by an other Polystyle");
		} else {
			this.polyStyles.add(polyStyle);
		}
	}

	public void addDefaultPolyStyles() throws OverlappingPolyStylesException {
		add(new MyPolyStyle(0, 10, new Color(255, 255, 255, 200)));
		add(new MyPolyStyle(11, 20, new Color(255, 255, 0, 200)));
		add(new MyPolyStyle(21, 30, new Color(255, 200, 20, 200)));
		add(new MyPolyStyle(31, 100, new Color(255, 0, 0, 200)));
	}

	public MyPolyStyle getPolyStyleForPointCount(int pointCount)
			throws OverlappingPolyStylesException, NoPolyStyleCoveringThisPointCountException {
		List<MyPolyStyle> polyStylesCoveringThisPointCount = polyStyles.stream()
				.filter(x -> x.countCoveredByThisPolyStyle(pointCount)).collect(Collectors.toList());

		if (polyStylesCoveringThisPointCount.size() == 0) {
			throw new NoPolyStyleCoveringThisPointCountException(
					"there is no PolyStyle covering a pointcount of " + pointCount);
		} else if (polyStylesCoveringThisPointCount.size() > 1) {
			throw new OverlappingPolyStylesException(
					"there are more than one PolyStyle which cover a pointcount of " + pointCount);
		} else {
			return polyStylesCoveringThisPointCount.get(0);
		}
	}

}
