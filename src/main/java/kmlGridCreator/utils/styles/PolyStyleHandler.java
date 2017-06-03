package main.java.kmlGridCreator.utils.styles;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class PolyStyleHandler {
	private List<PolyStyle> polyStyles;

	public PolyStyleHandler() {
		this.polyStyles = new ArrayList<>();
	}

	public List<PolyStyle> getPolyStyles() {
		return polyStyles;
	}

	public void add(PolyStyle polyStyle) {
		if (polyStyles.stream().anyMatch(x -> x.rangeCoveredByThisPolyStyle(polyStyle.getMinPointCount(), polyStyle.getMaxPointCount()))) {
			throw new IllegalArgumentException(
					"the range [" + polyStyle.getMinPointCount() + "," + polyStyle.getMaxPointCount() + "] is already used by an other Polystyle");
		} else {
			this.polyStyles.add(polyStyle);
		}
	}

	public PolyStyle getPolyStyleForPointCount(int pointCount) throws OverlappingPolyStylesException {
		List<PolyStyle> polyStylesCoveringThisPointCount = polyStyles.stream().filter(x -> x.countCoveredByThisPolyStyle(pointCount))
				.collect(Collectors.toList());

		if (polyStylesCoveringThisPointCount.size() > 1) {
			throw new OverlappingPolyStylesException("there are more than one PolyStyle which cover a pointcount of " + pointCount);
		} else {
			return polyStylesCoveringThisPointCount.get(0);
		}
	}

}
