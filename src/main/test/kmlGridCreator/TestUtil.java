package main.test.kmlGridCreator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import converter.MyPointForConverter;
import main.java.kmlGridCreator.model.MyPoint;
import main.java.kmlGridCreator.utils.TxtUtil;

public class TestUtil {

	public static final String TEST_FOLDER_DIRECTORY = Paths.get(".").toAbsolutePath().normalize().toString() + "/testdata/";
	public static final String TEST_FILE = TEST_FOLDER_DIRECTORY + "TestFile.txt";

//	public static List<MyPointForConverter> getTestPoints() throws IOException {
//		return TxtUtil.getPointsFromTxt(new File(TEST_FILE));
//	}

	public static List<String> extractPointFromKml(String kmlText) {
		final Pattern pattern = Pattern.compile("<Point>((.|\n)*?)<\\/Point>");
		final Matcher matcher = pattern.matcher(kmlText);

		List<String> results = new ArrayList<>();
		while (matcher.find()) {
			results.add(matcher.group(1).trim());
		}
		return results;
	}

}
