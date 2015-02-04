package de.esri.osm.data;

/**
 * This exception is thrown if the geometry types of OSM and ArcGIS are not supported.
 *
 * @author Eva Peters
 *
 */
public class GeometryTypeSupportException extends Exception
{
	/**
     * Unique serial version id.
     */
	private static final long serialVersionUID = -3168890817003764727L;

	/**
     * Creates a new ReaderException.
     *
     * @param errorMessage The description of the new exception.
     */
    public GeometryTypeSupportException(String errorMessage)
    {
        super(errorMessage);
    }

    /**
     * Creates a new ReaderException.
     *
     * @param e The cause of the new exception.
     */
    public GeometryTypeSupportException(Throwable e)
    {
        super(e);
    }

    /**
     * Creates a new ReaderException.
     *
     * @param errorMessage The description of the new exception.
     * @param e The cause of the new exception.
     */
    public GeometryTypeSupportException(String errorMessage, Throwable e)
    {
        super(errorMessage, e);
    }
}