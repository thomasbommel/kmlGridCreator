package main.java.kmlGridCreator.utils.styles;

import java.awt.Color;

import main.java.kmlGridCreator.utils.StringUtils;

public class ColorUtils {

	public static String convertColorToKMLColorString(Color color){
		
		String colorInHex = StringUtils.toFixedLength2(Integer.toHexString(color.getAlpha())) + ""
				+  StringUtils.toFixedLength2(Integer.toHexString(color.getBlue())) + ""
				+  StringUtils.toFixedLength2(Integer.toHexString(color.getGreen())) + ""
				+  StringUtils.toFixedLength2(Integer.toHexString(color.getRed()));
	
		return colorInHex;
	}
	
// TODO Remove unused code found by UCDetector
// 	@Unused
// 	public static List<MyPolyStyle> getPolyStylesFromTxt(){
// 		List<String> linesOfColorTxt;
// 		try {
// 			linesOfColorTxt = TxtUtil.getLinesFromTextFile("colors.txt");
// 		
// 		 return linesOfColorTxt.stream().map(l-> {
// 			int minCount = Integer.parseInt(l.substring(0,3));
// 			int maxCount = Integer.parseInt(l.substring(4,7));
// 			int red = Integer.parseInt(l.substring(8,11));
// 			int green = Integer.parseInt(l.substring(12,15));
// 			int blue = Integer.parseInt(l.substring(16,18));
// 			int alpha = Integer.parseInt(l.substring(19,22));
// 			return  new MyPolyStyle(minCount,maxCount,new Color(red,green,blue,alpha));
// 		}).collect(Collectors.toList());
// 		} catch (IOException e) {
// 			e.printStackTrace();
// 			return null;
// 		}
// 		
// 	}
	
}
