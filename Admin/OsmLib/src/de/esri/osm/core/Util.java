package de.esri.osm.core;


/**
 * Utility class.
 * 
 * @author Eva Peters
 *
 */
public class Util {
	
	/**
	 * Checks if a string is null or an empty string.
	 * @param text The string.
	 * @return A boolean value indicating whether a string is null or an empty string.
	 */
	public static boolean isEmpty(String text){
		boolean isEmpty = false;
		if(text == null || text.equals("")){
			isEmpty = true;
		}		
		return isEmpty;
	}
}
