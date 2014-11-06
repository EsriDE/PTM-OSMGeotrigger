package de.esri.osm.data;

/**
 * This exception is thrown if the given XML node is not an OSM node.
 *
 * @author Eva Peters
 *
 */
public class NoOSMNodeException extends Exception
{
	/**
     * Unique serial version id.
     */
	private static final long serialVersionUID = 7212747963244645385L;

	/**
     * Creates a new ReaderException.
     *
     * @param errorMessage The description of the new exception.
     */
    public NoOSMNodeException(String errorMessage)
    {
        super(errorMessage);
    }

    /**
     * Creates a new ReaderException.
     *
     * @param e The cause of the new exception.
     */
    public NoOSMNodeException(Throwable e)
    {
        super(e);
    }

    /**
     * Creates a new ReaderException.
     *
     * @param errorMessage The description of the new exception.
     * @param e The cause of the new exception.
     */
    public NoOSMNodeException(String errorMessage, Throwable e)
    {
        super(errorMessage, e);
    }
}