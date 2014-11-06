package de.esri.osm.data;

/**
 * This exception is thrown if the given XML node is not an OSM way.
 *
 * @author Eva Peters
 *
 */
public class NoOSMWayException extends Exception
{
	/**
     * Unique serial version id.
     */
	private static final long serialVersionUID = 8844037557954872375L;

	/**
     * Creates a new ReaderException.
     *
     * @param errorMessage The description of the new exception.
     */
    public NoOSMWayException(String errorMessage)
    {
        super(errorMessage);
    }

    /**
     * Creates a new ReaderException.
     *
     * @param e The cause of the new exception.
     */
    public NoOSMWayException(Throwable e)
    {
        super(e);
    }

    /**
     * Creates a new ReaderException.
     *
     * @param errorMessage The description of the new exception.
     * @param e The cause of the new exception.
     */
    public NoOSMWayException(String errorMessage, Throwable e)
    {
        super(errorMessage, e);
    }
}