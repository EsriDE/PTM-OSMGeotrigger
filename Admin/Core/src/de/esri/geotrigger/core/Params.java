package de.esri.geotrigger.core;

/**
 * The Params class is used to store necessary information like client id, client secret and token.
 * So an application can access these values over its life time.
 * The class is implemented as a singleton.
 */
public class Params {
	private static Params instance = null;
	private String clientId;
	private String clientSecret;
	private String accessToken;
	private long expireTime;
	
	private Params(){
	}
	
	/**
	 * Use this method to get the (only) instance of this class.
	 * @return The instance of this class.
	 */
	public static Params get(){
		if(instance == null){
			instance = new Params();
		}
		return instance;
	}

	/**
	 * Get the client id.
	 * @return The client id.
	 */
	public String getClientId() {
		return clientId;
	}

	/**
	 * Set the client id.
	 * @param clientId The client id.
	 */
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	/**
	 * Get the client secret.
	 * @return The client secret.
	 */
	public String getClientSecret() {
		return clientSecret;
	}

	/**
	 * Set the client secret.
	 * @param clientSecret The client secret.
	 */
	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	/**
	 * Get the access token.
	 * @return The access token.
	 */
	public String getAccessToken() {
		if(accessToken == null || tokenExpired()){
			OAuthUtil.getAccessToken();
		}
		return accessToken;
	}

	/**
	 * Set the access token.
	 * @param accessToken The access token.
	 */
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	
	/**
	 * Get the expire time of the token.
	 * @return The expire time of the token.
	 */
	public long getExpireTime() {
		return expireTime;
	}

	/**
	 *  Set the expire time of the token.
	 * @param expireTime  The expire time of the token.
	 */
	public void setExpireTime(long expireTime) {
		this.expireTime = expireTime;
	}
	
	private boolean tokenExpired(){
		boolean expired = false;
		long now = System.currentTimeMillis();
		if(now > (expireTime - 1000)){
			expired = true;
		}
		return expired;
	}
}
