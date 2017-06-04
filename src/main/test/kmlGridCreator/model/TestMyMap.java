package main.test.kmlGridCreator.model;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import main.java.kmlGridCreator.exceptions.OverlappingPolyStylesException;
import main.java.kmlGridCreator.model.MapDataModel;
import main.java.kmlGridCreator.model.MyMap;
import main.java.kmlGridCreator.model.MyPoint;
import main.java.kmlGridCreator.utils.CSVCreatorUtils;
import main.java.kmlGridCreator.utils.MyKmlFactory;
import main.java.kmlGridCreator.view.TestView;
import main.test.kmlGridCreator.TestUtil;

public class TestMyMap {

	@Test
	public void testGetPointCountToBoundingAreaCountMap() throws IOException, OverlappingPolyStylesException {
		List<MyPoint> testPoints = TestUtil.getTestPoints();
		MyMap map = new MyMap(testPoints, 1000,new TestView(new MapDataModel(new MyKmlFactory("test"))));
		map.addPointsToTheAreas();

		Map<Integer,Integer> pcToBaCount = map.getPointCountToBoundingAreaCountMap();
		Assert.assertEquals(pcToBaCount.get(0),new Integer(1259));
		Assert.assertEquals(pcToBaCount.get(1),new Integer(1));
		Assert.assertEquals(pcToBaCount.get(2),new Integer(1));
		Assert.assertEquals(pcToBaCount.get(11),new Integer(3));
		Assert.assertEquals(pcToBaCount.get(17),new Integer(1));
		Assert.assertEquals(pcToBaCount.get(20),new Integer(1));
	}

}
