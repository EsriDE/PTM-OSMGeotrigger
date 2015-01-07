package de.esri.geotrigger.core;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.message.BasicStatusLine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

public class OAuthUtil {
	private static Logger log = LogManager.getLogger(OAuthUtil.class.getName());
	private static final String OAUTH_TOKEN_URL = "https://www.arcgis.com/sharing/oauth2/token";
	private static final String USER_TOKEN_URL = "https://www.arcgis.com/sharing/rest/generateToken";
	private static final String CONTENTTYPE_FORM = "application/x-www-form-urlencoded";
	private static String accessToken;
	private static String userToken;
			
	/**
	 * Get an access token for secured requests.
	 */
	public static void getAccessToken(){	
		String clientId = Params.get().getClientId();
		String clientSecret = Params.get().getClientSecret();
		if(clientId != null && clientSecret != null){
			requestAccessToken(clientId, clientSecret, new JsonRequestListener() {
				@Override
				public void onSuccess(JSONObject json) {
					try {
						accessToken = json.getString("access_token");
						Params.get().setAccessToken(accessToken);
						int expireInterval = json.getInt("expires_in");
						long now = System.currentTimeMillis();
						long expireTime = now + expireInterval * 1000;
						Params.get().setExpireTime(expireTime);
					} catch (Exception e) {
						log.error("Error: " + e.getMessage());
					}
				}
				
				@Override
				public void onFailure(Throwable error) {
					log.error("Error requesting access token: " + error.getMessage());
				}
				
				@Override
				public void onError(JSONObject jsonObject, StatusLine statusLine) {
					log.error("Error requesting access token - code: " + statusLine.getStatusCode() + ", error: " + jsonObject.toString());
				}
			});				
		}
	}

	/**
	 * Request an OAuth access token.
	 * @param clientId The client ID.
	 * @param clientSecret The client secret.
	 * @param listener The listener for the response.
	 */
	public static void requestAccessToken(String clientId, String clientSecret, final JsonRequestListener listener){
		try {
			log.debug("Requesting access token...");
			// add POST parameters
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			nameValuePairs.add(new BasicNameValuePair("client_id", clientId));	
			nameValuePairs.add(new BasicNameValuePair("client_secret", clientSecret));
			nameValuePairs.add(new BasicNameValuePair("grant_type", "client_credentials"));
			nameValuePairs.add(new BasicNameValuePair("f", "json"));
			HttpEntity entity = new UrlEncodedFormEntity(nameValuePairs);
			// post request
			HttpUtil.postRequest(OAUTH_TOKEN_URL, null, CONTENTTYPE_FORM, entity, new JsonRequestListener() {				
				@Override
				public void onSuccess(JSONObject jsonObject) {
					log.debug("Response access token: " + jsonObject.toString());
					JSONObject errorObject = jsonObject.optJSONObject("error");
					if(errorObject == null){
						listener.onSuccess(jsonObject);
					}else{
	                    int errorCode = errorObject.optInt("code", 400);
	                    String reason = errorObject.optString("message", "Application level error received.");
	                    StatusLine status = new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), errorCode, reason);
	                    listener.onError(jsonObject, status);
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
		} catch (UnsupportedEncodingException e) {
			listener.onFailure(new Exception(e));
		}		
	}
	
	/**
	 * Request a user token for secured services that need authentication with user/password.
	 * @param user The user name.
	 * @param password The password.
	 * @return The user token.
	 */
	public static String requestUserToken(String user, String password){
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
							userToken = jsonObject.getString("token");
						} catch (Exception e) {
							log.error("Error: "+e.getMessage());
						}
					}else{
						int errorCode = errorObject.optInt("code", 400);
						log.error("Error requesting user token. Code: " + errorCode);
					}
				}
				
				@Override
				public void onError(JSONObject jsonObject, StatusLine statusLine) {
					log.error("Error requesting user token: " + statusLine);
				}
				
				@Override
				public void onFailure(Throwable error) {
					log.error("Error requesting user token: " + error);
				}
			});
			
		}catch (UnsupportedEncodingException e) {
			log.error("Error: "+e.getMessage());
		}
		
		return userToken;
	}
}
