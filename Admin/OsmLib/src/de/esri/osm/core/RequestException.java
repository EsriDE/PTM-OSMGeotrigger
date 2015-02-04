package de.esri.osm.core;

/**
 * This exception is thrown if the request failed.
 *
 * @author Eva Peters
 *
 */
public class RequestException extends Exception
{
	/**
     * Unique serial version id.
     */
	private static final long serialVersionUID = 4068871480057178175L;

	/**
     * Creates a new RequestException.
     *
     * @param errorMessage The description of the new exception.
     */
    public RequestException(String errorMessage)
    {
        super(errorMessage);
    }

    /**
     * Creates a new RequestException.
     *
     * @param e The cause of the new exception.
     */
    public RequestException(Throwable e)
    {
        super(e);
    }

    /**
     * Creates a new RequestException.
     *
     * @param errorMessage The description of the new exception.
     * @param e The cause of the new exception.
     */
    public RequestException(String errorMessage, Throwable e)
    {
        super(errorMessage, e);
    }
}