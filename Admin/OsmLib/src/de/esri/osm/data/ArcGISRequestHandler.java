package de.esri.osm.data;

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

import de.esri.geotrigger.core.HttpUtil;
import de.esri.geotrigger.core.JsonRequestListener;
import de.esri.geotrigger.core.OAuthUtil;
import de.esri.geotrigger.core.TokenException;

public class ArcGISRequestHandler {
	
	private static Logger log = LogManager.getLogger(ArcGISRequestHandler.class.getName());
	
	private static final String CONTENTTYPE_FORM = "application/x-www-form-urlencoded";

	public static void deleteFeatures(String url, String token, final JsonRequestListener listener)
	{
		url = url + "/deleteFeatures";
		
		try {
			log.debug("Requesting delete features...");
			// add POST parameters
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			nameValuePairs.add(new BasicNameValuePair("f", "json"));
			nameValuePairs.add(new BasicNameValuePair("where", "1=1"));
			nameValuePairs.add(new BasicNameValuePair("token", token));
			nameValuePairs.add(new BasicNameValuePair("rollbackOnFailure", "true"));
			HttpEntity entity = new UrlEncodedFormEntity(nameValuePairs, "UTF-8");
			// post request
			HttpUtil.postRequest(url, CONTENTTYPE_FORM, entity, new JsonRequestListener() {
				@Override
				public void onSuccess(JSONObject jsonObject) {
					log.debug("Response delete features: " + jsonObject.toString());
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
	
	public static void addFeatures(String url, String features, String token, final JsonRequestListener listener)
	{
		url = url + "/addFeatures";
		
		try {
			log.debug("Adding feature: " + features);
			// add POST parameters
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			nameValuePairs.add(new BasicNameValuePair("f", "json"));
			nameValuePairs.add(new BasicNameValuePair("features", features));
			nameValuePairs.add(new BasicNameValuePair("token", token));
			nameValuePairs.add(new BasicNameValuePair("rollbackOnFailure", "true"));
			HttpEntity entity = new UrlEncodedFormEntity(nameValuePairs, "UTF-8");
			// post request
			HttpUtil.postRequest(url, CONTENTTYPE_FORM, entity, new JsonRequestListener() {
				@Override
				public void onSuccess(JSONObject jsonObject) {
					log.debug("Add features response: " + jsonObject.toString());
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
	
	public static String requestToken(String user, String password)
	{
		String token = "";
		try{
			OAuthUtil oauthUtil = new OAuthUtil();		
			token = oauthUtil.requestUserToken(user, password);			
		}catch(TokenException e){
			log.error("Could not get user token: " + e.getMessage());
		}
		
		return token;
	}

}
