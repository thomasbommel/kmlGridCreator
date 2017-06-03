package main.test.kmlGridCreator.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.peertopark.java.geocalc.DegreeCoordinate;

import main.java.kmlGridCreator.model.MyPoint;
import main.java.kmlGridCreator.utils.TxtUtil;
import main.test.kmlGridCreator.TestUtil;

public class TxtUtilTest {

	@Test
	public void testgetPointsFromValidFile() throws IOException {
		List<MyPoint> testPoints = TxtUtil.getPointsFromTxt(new File(TestUtil.TEST_FILE));
		Assert.assertEquals(testPoints.size(), 803);

		MyPoint p0 = new MyPoint(new DegreeCoordinate(55.59493333333333), new DegreeCoordinate(21.37836666666667));
		MyPoint p100 = new MyPoint(new DegreeCoordinate(55.51808333333334), new DegreeCoordinate(21.383));
		MyPoint p200 = new MyPoint(new DegreeCoordinate(55.4669), new DegreeCoordinate(21.424966666666666));
		MyPoint p300 = new MyPoint(new DegreeCoordinate(55.4668), new DegreeCoordinate(21.576383333333332));
		MyPoint p802 = new MyPoint(new DegreeCoordinate(55.280883333333335), new DegreeCoordinate(21.872966666666667));

		Assert.assertEquals(testPoints.get(0), p0);
		Assert.assertEquals(testPoints.get(100), p100);
		Assert.assertEquals(testPoints.get(200), p200);
		Assert.assertEquals(testPoints.get(300), p300);
		Assert.assertEquals(testPoints.get(802), p802);
	}

	@Test
	public void testgetPointsFromTxtNullValue() throws IOException {
		Assert.assertEquals(TxtUtil.getPointsFromTxt(null).size(), 0);
	}

	@Test(expectedExceptions = NoSuchFileException.class)
	public void testgetPointsFromInvalidFile() throws IOException {
		TxtUtil.getPointsFromTxt(new File("iAmInvalid")).size();
	}

}
