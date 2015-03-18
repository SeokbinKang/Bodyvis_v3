package com.redbear.bodyvis.Bioharness;

public abstract class StringUtils {

	// "static" class
	private StringUtils() {} 
	
	public static boolean isNullOrEmpty(String str) {
	    return (str == null || "".equals(str.trim()));
	}
}
