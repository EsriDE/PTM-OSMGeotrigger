package de.esri.geotrigger.core;

import org.json.JSONObject;

public interface GeotriggerApiListener {
    public abstract void onSuccess(JSONObject jsonObject);
    public abstract void onFailure(Throwable throwable);
}
