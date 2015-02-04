package de.esri.osm.core;

import org.apache.http.StatusLine;
import org.json.JSONObject;

/**
 * Listener for JSON requests.
 * 
 * @author Eva Peters
 *
 */
public interface JsonRequestListener {
    public abstract void onSuccess(JSONObject jsonObject);
    public abstract void onError(JSONObject jsonObject, StatusLine statusLine);
    public abstract void onFailure(Throwable throwable);
}
