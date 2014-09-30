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
import org.json.JSONObject;

public class OAuthUtil {
	private static Logger log = LogManager.getLogger(OAuthUtil.class.getName());
	private static final String TOKEN_URL = "https://www.arcgis.com/sharing/oauth2/token";
	private static final String CONTENTTYPE_FORM = "application/x-www-form-urlencoded";
	private static String accessToken;
			
	public static void getAccessToken(){	
		String clientId = Params.get().getClientId();
		String clientSecret = Params.get().getClientSecret();
		if(clientId != null && clientSecret != null){
			requestAccessToken(clientId, clientSecret, new JsonRequestListener() {
				@Override
				public void onSuccess(JSONObject json) {
					accessToken = json.getString("access_token");
					Params.get().setAccessToken(accessToken);
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

	public static void requestAccessToken(String clientId, String clientSecret, JsonRequestListener listener){
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
			HttpUtil.postRequest(TOKEN_URL, null, CONTENTTYPE_FORM, entity, new JsonRequestListener() {				
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
}
