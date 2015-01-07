package de.esri.geotrigger.core;

import org.apache.http.Header;
import org.apache.http.StatusLine;
import org.apache.http.message.BasicHeader;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.json.JSONObject;

public class GeotriggerApiClient {
	private static Logger log = LogManager.getLogger(GeotriggerApiClient.class.getName());
	private static final String GEOTRIGGER_BASE_URL = "https://geotrigger.arcgis.com";
	
    private GeotriggerApiClient()
    {
    }
    
    /**
     * Run a request to the geotrigger service.
     * @param route The route of the geotrigger endpoint (like "trigger/create").
     * @param listener The GeotriggerApiListener.
     */
    public static void runRequest(String route, GeotriggerApiListener listener){
        runRequest(route, null, listener);
    }
    
    /**
     * Run a request to the geotrigger service.
     * @param route The route of the geotrigger endpoint (like "trigger/create").
     * @param params The parameter JSON object.
     * @param listener The GeotriggerApiListener.
     */
    public static void runRequest(String route, JSONObject params, final GeotriggerApiListener listener){
    	String url = new StringBuilder().append(GEOTRIGGER_BASE_URL).append("/").append(route).toString();
    	Header headers[] = {
    			new BasicHeader("Authorization", String.format("Bearer %s", Params.get().getAccessToken()))
    	};
    	log.debug("Sending request to geotrigger service: " + url);
    	for (Header header : headers) {
    		log.debug("Header: " + header.getName() + " " + header.getValue());
		}
    	log.debug("Request: " + params.toString());
    	HttpUtil.postJsonRequest(url, headers, params, new JsonRequestListener() {
			@Override
			public void onSuccess(JSONObject jsonObject) {
				JSONObject errorObject = jsonObject.optJSONObject("error");
				if(errorObject == null){
					log.debug("Response: " + jsonObject.toString());
					listener.onSuccess(jsonObject);
				}else{
					int errorCode = errorObject.optInt("code", 400);
					log.error("Error executing geotrigger request. Code: " + errorCode);
					if(errorCode == 498){
						// handle refresh token
					}else{
						String response = jsonObject.toString();
						log.error("Error response: " + response);
                        String message = "";
                        for(int i = response.indexOf("\"message\":"); i > 0; i = response.indexOf("\"message\":", i + 11)){
                            message = (new StringBuilder()).append(message).append(response.substring(i + 10, response.indexOf("\"", i + 11)).replaceAll("\"", "")).append("; ").toString();                        	
                        }
                        listener.onFailure(new Exception(message));
					}
				}
			}
			
			@Override
			public void onError(JSONObject jsonObject, StatusLine statusLine) {
				listener.onFailure(new Exception(statusLine.getReasonPhrase()));
			}
			
			@Override
			public void onFailure(Throwable error) {
				listener.onFailure(new Exception(error));
			}
		});
    }
}
