package de.esri.osm.config;

import java.io.File;
import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

public class ConfigurationReader {
	
	/**
	 * The path to the package.
	 */
	private final String packagePath = "de.esri.osm.config";
	
	/**
	 * The relative path to the schema file.
	 */
	private String schemaPath = "/configuration.xsd";
	
	/**
     * File.
     */
    private final File file;

    /**
     * Constructor generates the file object.
     *
     * @param LOG The logger.
     * @param file The input file.
     */
    public ConfigurationReader(File file)
    {
        this.file = file;
    }

    /**
     * Reads a Parameters file which is based on the schema
     * 
     * <p>
     * While unmarshalling the file is validated against the schema Parameters.xsd.
     * Then it is validated by using the method {@link ParametersValidation#isValid()}.
     * </p>
     *
     * @return The parameters.
     * @throws XMLReaderException If the unmarshalling of the given file failed. Maybe the file isn't valid.
     */
    public Configuration read() throws XMLReaderException
    {
    	String failedMessage = "The reading process of the file '" + file.getAbsolutePath() + "' failed:\n\t";

    	Unmarshaller unmarshaller = null; //Initialize
    	try
    	{
        	JAXBContext jc = JAXBContext.newInstance(packagePath);
        	unmarshaller = jc.createUnmarshaller();
    	}
    	catch(JAXBException e)
    	{
    		throw new XMLReaderException(failedMessage + "The unmarshaller could not be initialized.\n\t" + e);
    	}

    	SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

    	URL schemaURL = getClass().getResource(schemaPath);
    	
    	if(schemaURL == null)
    	{
    		throw new XMLReaderException(failedMessage + "The XML schema could not be read.");
    	}

    	Schema schema = null; //Initialize
    	try
    	{
        	schema = schemaFactory.newSchema(schemaURL);
        	
    	}
    	catch(SAXException e)
    	{
    		throw new XMLReaderException(failedMessage + "The schema '" + schemaURL.getPath() + "' could not be generated.\n\t" + e);
    	}
    	catch(Exception e)
    	{
    		throw new XMLReaderException(failedMessage + "The schema '" + schemaURL.getPath() + "' could not be generated.\n\t" + e);
    	}

    	unmarshaller.setSchema(schema);
    	
    	Configuration configuration = null; //Initialize
    	try
    	{
    		configuration = (Configuration)unmarshaller.unmarshal(file);
    	}
    	catch(JAXBException e)
    	{
    		throw new XMLReaderException(failedMessage + "The file could not be unmarshalled. Maybe the file isn't valid.\n\t" + e);
    	}
		
    	return configuration;
    }
}
