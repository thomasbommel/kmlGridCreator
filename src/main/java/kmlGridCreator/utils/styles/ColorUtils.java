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
	

	
	
	
}
