package de.esri.geotrigger.core;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class HttpUtil {
	
	public static void postJsonRequest(String url, Header[] headers, JSONObject json, JsonRequestListener listener){
        StringEntity entity;
        if(json == null){
            entity = new StringEntity("{}", "UTF-8");            	
        }else{
            entity = new StringEntity(json.toString(), "UTF-8");            	
        }
        postRequest(url, headers, "application/json", entity,  listener);
	}
	
	public static void postRequest(String url, Header[] headers, String contentType, HttpEntity entity, JsonRequestListener listener){
		//HttpClient client = HttpClientBuilder.create().build();
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
}
