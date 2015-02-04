package de.esri.osm.conversion;

import org.apache.http.StatusLine;
import org.json.JSONObject;

import de.esri.osm.config.Query;
import de.esri.osm.core.ArcGISRequestHandler;
import de.esri.osm.core.JsonRequestListener;
import de.esri.osm.core.TokenListener;
import de.esri.osm.core.Util;

/**
 * Handles the token.
 * 
 * @author Eva Peters
 *
 */
public class TokenHandler {
	
	/**
	 * The user.
	 */
	private String user;
	
	/**
	 * The password.
	 */
	private String password;
	
	/**
	 * The valid token.
	 */
	private String token;
	
	/**
	 * The milliseconds after 1970-01-01 when the token expires.
	 */
	private long tokenExpires;
	
	/**
	 * Constructor.
	 * 
	 * @param query The query.
	 * @param listener The listener.
	 */
	public TokenHandler(Query query)
	{
		this.user = query.getArcgis().getLogin().getUser();
		this.password = query.getArcgis().getLogin().getPassword();
	}
	
	/**
	 * Gets the current token.
	 * 
	 * An empty string is returned 
	 * - if the user or password is empty.
	 * - if an error occurs.
	 * 
	 * @return The token or an empty string.
	 */
	public String getToken()
	{
		return this.token;
	}
	
	/**
	 * Generates a valid user token.
	 * 
	 * An empty string is generated 
	 * - if the user or password is empty.
	 * - if an error occurs.
	 * 
	 * Retrieve the token with {{@link #getToken()}.
	 * 
	 * @param listener The listener.
	 */
	public void generateValidToken(final TokenListener listener)
	{
		if(!Util.isEmpty(user) && !Util.isEmpty(password))
		{
			boolean valid = isTokenValid();
			
			if(! valid)
			{
				updateToken(listener);
			}
			else
			{
				listener.onSuccess("Token still valid.");
			}
		}
		else
		{
			listener.onSuccess("User name or password empty.");
		}
	}
	
	/**
	 * Checks if the current token is still five minutes valid.
	 * 
	 * @return Returns true if the token is still five minutes valid. Otherwise false.
	 */
	private boolean isTokenValid()
	{
		long fiveMinutesThreshold = System.currentTimeMillis() - 1000*60*5;
		
		if(this.tokenExpires >= fiveMinutesThreshold)
		{
			return true;
		}
		return false;
	}
	
	/**
	 * Updates the token.
	 *
	 * @param listener The listener.
	 */
	private void updateToken(final TokenListener listener)
	{	
		ArcGISRequestHandler.requestToken(user, password, new JsonRequestListener() {
			
			@Override
			public void onSuccess(JSONObject jsonObject) {
				token = jsonObject.getString("token");
				tokenExpires = jsonObject.getLong("expires");
				listener.onSuccess("Successfully requested token.");
			}
			
			@Override
			public void onFailure(Throwable throwable) {			
				listener.onError("Error requesting token: " + throwable.getMessage());				
			}
			
			@Override
			public void onError(JSONObject jsonObject, StatusLine statusLine) {			
				listener.onError("Error requesting token - code: " + statusLine.getStatusCode() + ", error: " + jsonObject.toString());				
			}
		});
	}
}
