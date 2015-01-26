package de.esri.geotrigger.config;

public class ReaderException extends Exception{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -171966485318912682L;

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
