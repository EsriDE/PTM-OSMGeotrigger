package de.esri.osm.data;

/**
 * This exception is thrown if the geometry can not be read.
 *
 * @author Eva Peters
 *
 */
public class GeometryReaderException extends Exception
{
	/**
     * Unique serial version id.
     */
	private static final long serialVersionUID = 7741895285376819572L;

	/**
     * Creates a new GeometryReaderException.
     *
     * @param errorMessage The description of the new exception.
     */
    public GeometryReaderException(String errorMessage)
    {
        super(errorMessage);
    }

    /**
     * Creates a new GeometryReaderException.
     *
     * @param e The cause of the new exception.
     */
    public GeometryReaderException(Throwable e)
    {
        super(e);
    }

    /**
     * Creates a new GeometryReaderException.
     *
     * @param errorMessage The description of the new exception.
     * @param e The cause of the new exception.
     */
    public GeometryReaderException(String errorMessage, Throwable e)
    {
        super(errorMessage, e);
    }
}