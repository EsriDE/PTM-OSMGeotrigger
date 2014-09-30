package de.esri.geotrigger.core;

public class Params {
	private static Params instance = null;
	private String clientId;
	private String clientSecret;
	private String accessToken;
	private long expireTime;
	
	private Params(){
	}
	
	public static Params get(){
		if(instance == null){
			instance = new Params();
		}
		return instance;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public String getAccessToken() {
		if(accessToken == null){
			OAuthUtil.getAccessToken();
		}
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	
	public long getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(long expireTime) {
		this.expireTime = expireTime;
	}
}
