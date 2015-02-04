package de.esri.osm.config;

/**
 * This exception is thrown if the reading process of the XML file failed.
 *
 * @author Eva Peters
 *
 */
public class XMLReaderException extends Exception
{
	/**
     * Unique serial version id.
     */
	private static final long serialVersionUID = -3081918239407257048L;

	/**
     * Creates a new XMLReaderException.
     *
     * @param errorMessage The description of the new exception.
     */
    public XMLReaderException(String errorMessage)
    {
        super(errorMessage);
    }

    /**
     * Creates a new XMLReaderException.
     *
     * @param e The cause of the new exception.
     */
    public XMLReaderException(Throwable e)
    {
        super(e);
    }

    /**
     * Creates a new XMLReaderException.
     *
     * @param errorMessage The description of the new exception.
     * @param e The cause of the new exception.
     */
    public XMLReaderException(String errorMessage, Throwable e)
    {
        super(errorMessage, e);
    }
}