package de.esri.osm.core;

/**
 * Listener for token requests.
 * 
 * @author Eva Peters
 *
 */
public interface TokenListener {
	public abstract void onSuccess(String message);
	public abstract void onError(String message);
}
