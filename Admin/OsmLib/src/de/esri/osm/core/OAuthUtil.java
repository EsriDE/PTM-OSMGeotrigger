package de.esri.osm.core;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

/**
 * Handles authorization requests.
 * 
 * @author Eva Peters
 *
 */
public class OAuthUtil {
	private static Logger log = LogManager.getLogger(OAuthUtil.class.getName());
	private static final String USER_TOKEN_URL = "https://www.arcgis.com/sharing/rest/generateToken";
	private static final String CONTENTTYPE_FORM = "application/x-www-form-urlencoded";
	
	/**
	 * Request a user token for secured services that need authentication with user/password.
	 * 
	 * @param user The user name.
	 * @param password The password.
	 * @param listener The listener with the token in onSuccess()
	 */
	public void requestUserToken(String user, String password, final JsonRequestListener listener) {		
		try {
			log.debug("Requesting user token...");
			// add POST parameters
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			nameValuePairs.add(new BasicNameValuePair("username", user));	
			nameValuePairs.add(new BasicNameValuePair("password", password));
			nameValuePairs.add(new BasicNameValuePair("f", "json"));
			nameValuePairs.add(new BasicNameValuePair("referer", "http://www.arcgis.com"));
			HttpEntity entity = new UrlEncodedFormEntity(nameValuePairs);
			// post request
			HttpUtil.postRequest(USER_TOKEN_URL, null, CONTENTTYPE_FORM, entity, new JsonRequestListener() {				
				@Override
				public void onSuccess(JSONObject jsonObject) {
					log.debug("Response user token: " + jsonObject.toString());
					JSONObject errorObject = jsonObject.optJSONObject("error");
					if(errorObject == null){
						try {
							listener.onSuccess(jsonObject);
						} catch (Exception e) {
							listener.onFailure(new Exception(e));
						}
					}else{
						int errorCode = errorObject.optInt("code", 400);
						listener.onFailure(new Exception("Error requesting user token. Code: " + errorCode));
					}
				}
				
				@Override
				public void onError(JSONObject jsonObject, StatusLine statusLine) {
					listener.onError(jsonObject, statusLine);
				}
				
				@Override
				public void onFailure(Throwable error) {
					listener.onFailure(new Exception(error));
				}
			});
			
		}catch (UnsupportedEncodingException e) {
			listener.onFailure(new Exception(e));
		}
	}
}
