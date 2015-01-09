package de.esri.geotrigger.core;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

public class HttpUtil {
	private static Logger log = LogManager.getLogger(HttpUtil.class.getName());
	
	/**
	 * Send a request by HTTP POST method.
	 * @param url The URL of the request.
	 * @param headers The headers.
	 * @param json The JSON content.
	 * @param listener The listener for the response.
	 */
	public static void postJsonRequest(String url, Header[] headers, JSONObject json, JsonRequestListener listener){
        StringEntity entity;
        if(json == null){
            entity = new StringEntity("{}", "UTF-8");            	
        }else{
            entity = new StringEntity(json.toString(), "UTF-8");            	
        }
        postRequest(url, headers, "application/json", entity,  listener);
	}
	
	/**
	 * Send a request by HTTP POST method.
	 * @param url The URL of the request.
	 * @param contentType The content type.
	 * @param entity The HTTP entity.
	 * @param listener The listener for the response.
	 */
	public static void postRequest(String url, String contentType, HttpEntity entity, JsonRequestListener listener){
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		post.setHeader("Content-Type", contentType);
		post.setEntity(entity);
		try {
			HttpResponse response = client.execute(post);
			StatusLine status = response.getStatusLine();			
			String content = EntityUtils.toString(response.getEntity());
			JSONObject json = new JSONObject(content);
			
			if(status.getStatusCode() == 200){
				listener.onSuccess(json);
			}else{
				listener.onError(json, status);
			}			
		} catch (Exception e) {
			listener.onFailure(new Exception(e));
		}
	}
	
	/**
	 * Send a request by HTTP POST method.
	 * @param url The URL of the request.
	 * @param headers The headers.
	 * @param contentType The content type.
	 * @param entity The HTTP entity.
	 * @param listener The listener for the response.
	 */
	public static void postRequest(String url, Header[] headers, String contentType, HttpEntity entity, JsonRequestListener listener){
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		post.setHeaders(headers);
		post.setHeader("Content-Type", contentType);
		post.setEntity(entity);
		try {
			HttpResponse response = client.execute(post);
			StatusLine status = response.getStatusLine();			
			String content = EntityUtils.toString(response.getEntity());
			JSONObject json = new JSONObject(content);
			if(status.getStatusCode() == 200){
				listener.onSuccess(json);
			}else{
				listener.onError(json, status);
			}			
		} catch (Exception e) {
			listener.onFailure(new Exception(e));
		}
	}
	
	/**
	 * Send a request by HTTP GET method.
	 * @param url The URL of the request.
	 * @return The response string.
	 */
	public static String getRequest(String url){
		String response = "";
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);
		try{
			HttpResponse httpResponse = client.execute(get);
			response = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
		}catch(Exception e){
			log.error("Error on GET request: "+e.getMessage());
		}
		return response;
	}
}
