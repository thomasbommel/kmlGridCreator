package main.test.kmlGridCreator.utils;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Test;
import org.testng.Assert;

import com.peertopark.java.geocalc.DegreeCoordinate;

import main.java.kmlGridCreator.model.MyPoint;
import main.java.kmlGridCreator.utils.GeoUtil;
import main.test.kmlGridCreator.TestUtil;

public class GeoUtilTest {

	@Test
	public void getCornersNwNeSeSwWithTestFile() throws Exception {
		List<MyPoint> testpoints = TestUtil.getTestPoints();
		MyPoint[] cornersNwNeSeSw = GeoUtil.getCornersNwNeSeSw(testpoints);

		MyPoint nw = new MyPoint(new DegreeCoordinate(55.5949333333333), new DegreeCoordinate(21.3783));
		MyPoint ne = new MyPoint(new DegreeCoordinate(55.5949333333333), new DegreeCoordinate(21.883133333333333));
		MyPoint se = new MyPoint(new DegreeCoordinate(55.280883333333335), new DegreeCoordinate(21.883133333333333));
		MyPoint sw = new MyPoint(new DegreeCoordinate(55.280883333333335), new DegreeCoordinate(21.3783));

		Assert.assertEquals(nw, cornersNwNeSeSw[0], "NW corner point not correct");
		Assert.assertEquals(ne, cornersNwNeSeSw[1], "NE corner point not correct");
		Assert.assertEquals(se, cornersNwNeSeSw[2], "SE corner point not correct");
		Assert.assertEquals(sw, cornersNwNeSeSw[3], "SW corner point not correct");
	}

	@Test(expected = NullPointerException.class)
	public void getCornersNwNeSeSwWithNullValue() throws Exception {
		GeoUtil.getCornersNwNeSeSw(null);
	}

	@Test(expected = NoSuchElementException.class)
	public void getCornersNwNeSeSwWithEmptyList() throws Exception {
		GeoUtil.getCornersNwNeSeSw(Collections.emptyList());
	}

}
