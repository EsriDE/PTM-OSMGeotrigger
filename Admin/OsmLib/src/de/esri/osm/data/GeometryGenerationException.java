package de.esri.osm.data;

/**
 * This exception is thrown if the geometry can not be generated.
 *
 * @author Eva Peters
 *
 */
public class GeometryGenerationException extends Exception
{
	/**
     * Unique serial version id.
     */
	private static final long serialVersionUID = 4830355724323057503L;

	/**
     * Creates a new GeometryException.
     *
     * @param errorMessage The description of the new exception.
     */
    public GeometryGenerationException(String errorMessage)
    {
        super(errorMessage);
    }

    /**
     * Creates a new GeometryException.
     *
     * @param e The cause of the new exception.
     */
    public GeometryGenerationException(Throwable e)
    {
        super(e);
    }

    /**
     * Creates a new GeometryException.
     *
     * @param errorMessage The description of the new exception.
     * @param e The cause of the new exception.
     */
    public GeometryGenerationException(String errorMessage, Throwable e)
    {
        super(errorMessage, e);
    }
}