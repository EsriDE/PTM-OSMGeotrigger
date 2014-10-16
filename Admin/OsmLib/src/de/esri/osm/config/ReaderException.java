package de.esri.osm.config;

/**
 * This exception is thrown if the reading process failed.
 *
 * @author Eva Peters
 *
 */
public class ReaderException extends Exception
{
	/**
     * Unique serial version id.
     */
	private static final long serialVersionUID = -3081918239407257048L;

	/**
     * Creates a new ReaderException.
     *
     * @param errorMessage The description of the new exception.
     */
    public ReaderException(String errorMessage)
    {
        super(errorMessage);
    }

    /**
     * Creates a new ReaderException.
     *
     * @param e The cause of the new exception.
     */
    public ReaderException(Throwable e)
    {
        super(e);
    }

    /**
     * Creates a new ReaderException.
     *
     * @param errorMessage The description of the new exception.
     * @param e The cause of the new exception.
     */
    public ReaderException(String errorMessage, Throwable e)
    {
        super(errorMessage, e);
    }
}