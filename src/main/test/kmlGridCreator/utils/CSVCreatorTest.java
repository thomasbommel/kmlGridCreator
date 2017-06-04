package main.test.kmlGridCreator.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import javax.swing.filechooser.FileNameExtensionFilter;

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

public class CSVCreatorTest {
	
	@Test
	public void testGetPointCountToBoundingAreaCountMap() throws IOException, OverlappingPolyStylesException {
		List<MyPoint> testPoints = TestUtil.getTestPoints();
		MyMap map = new MyMap(testPoints, 1000, new TestView(new MapDataModel(new MyKmlFactory("test"))));
		map.addPointsToTheAreas();

		FileNameExtensionFilter filter = new FileNameExtensionFilter("dsad", "kml");
		System.out.println(filter.getExtensions()[0]);
		
		File file = new File(TestUtil.TEST_FOLDER_DIRECTORY + "testGetPointCountToBoundingAreaCountMap_"
				+ System.currentTimeMillis() + ".csv");
		CSVCreatorUtils.savePointCountToBoundingAreaCountMapToCSV(map.getPointCountToBoundingAreaCountMap(), file);

		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			Map<Integer, Integer> lines = new HashMap<>();
			String line;
			while ((line = br.readLine()) != null) {
				String key = line.split(";")[0];
				String value = line.split(";")[1];
				try {
					lines.put(Integer.parseInt(key), Integer.parseInt(value));
				} catch (NumberFormatException e) {
					continue;
				}
			}

			Map<Integer, Integer> pcToBaCount = map.getPointCountToBoundingAreaCountMap();
			pcToBaCount.entrySet().stream().forEach(e -> {
				Assert.assertEquals(e.getValue(), lines.get(e.getKey()));
			});

			lines.entrySet().stream().forEach(e -> {
				Assert.assertEquals(e.getValue(), pcToBaCount.get(e.getKey()));
			});
		}
	}

}
