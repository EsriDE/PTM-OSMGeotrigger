/***************************************************************************************
 	Copyright (C) 2011-2012 by 52°North Initiative for Geospatial Open Source Software GmbH

       	Contact: Andreas Wytzisk
       	52°North Initiative for Geospatial Open Source Software GmbH
       	Martin-Luther-King-Weg 24
       	48155 Muenster, Germany
       	info@52north.org

       	Licensed to the Apache Software Foundation (ASF) under one
       	or more contributor license agreements.  See the NOTICE file
      	distributed with this work for additional information
       	regarding copyright ownership.  The ASF licenses this file
       	to you under the Apache License, Version 2.0 (the
       	"License"); you may not use this file except in compliance
       	with the License.  You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

       	Unless required by applicable law or agreed to in writing,
       	software distributed under the License is distributed on an
       	"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
       	KIND, either express or implied.  See the License for the
       	specific language governing permissions and limitations
       	under the License. 
       	
 ****************************************************************************************/

package org.n52.osm2nds.reader;

import java.io.File;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.XMLConstants;

import org.n52.osm2nds.core.general.ParameterException;
import org.n52.osm2nds.logging.LogMessageInformer;
import org.n52.osm2nds.parameters.schema.Parameters;
import org.n52.osm2nds.parameters.schema.ParametersValidation;
import org.xml.sax.SAXException;


/**
 * Parameters Reader.
 * 
 * @author Eva Peters
 *
 */
public class ParametersReader 
{	
	/**
	 * The logger.
	 */
	private final LogMessageInformer LOG;
	
	/**
	 * The path to the package.
	 */
	private final String packagePath = "org.n52.osm2nds.parameters.schema";
	
	/**
	 * The relative path to the schema file.
	 */
	private String schemaPath = "/resources/Parameters.xsd";
	
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
    public ParametersReader(final LogMessageInformer LOG, File file)
    {
    	this.LOG = LOG;
        this.file = file;
    }

    /**
     * Reads a Parameters file which is based on the schema: org.n52.osm2nds.parameters.schema.
     * 
     * <p>
     * While unmarshalling the file is validated against the schema Parameters.xsd.
     * Then it is validated by using the method {@link ParametersValidation#isValid()}.
     * </p>
     *
     * @return The parameters.
     * @throws ReaderException If the unmarshalling of the given file failed. Maybe the file isn't valid.
     */
    public Parameters read() throws ReaderException
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
    		throw new ReaderException(failedMessage + "The unmarshaller could not be initialized.\n\t" + e);
    	}

    	SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
    	Class<? extends ParametersReader> clazz = getClass();

    	URL schemaURL = clazz.getResource(schemaPath);
    	
    	if(schemaURL == null)
    	{
    		throw new ReaderException(failedMessage + "The XML schema could not be read.");
    	}

    	Schema schema = null; //Initialize
    	try
    	{
        	schema = schemaFactory.newSchema(schemaURL);
        	
    	}
    	catch(SAXException e)
    	{
    		throw new ReaderException(failedMessage + "The schema '" + schemaURL.getPath() + "' could not be generated.\n\t" + e);
    	}
    	catch(Exception e)
    	{
    		throw new ReaderException(failedMessage + "The schema '" + schemaURL.getPath() + "' could not be generated.\n\t" + e);
    	}

    	unmarshaller.setSchema(schema);
    	
    	Parameters parameters = null; //Initialize
    	try
    	{
        	parameters = (Parameters)unmarshaller.unmarshal(file);
    	}
    	catch(JAXBException e)
    	{
    		throw new ReaderException(failedMessage + "The file could not be unmarshalled. Maybe the file isn't valid.\n\t" + e);
    	}

    	ParametersValidation parametersValidation = new ParametersValidation(LOG, parameters);
    	
		try
		{
			parametersValidation.isValid();
		}
		catch(ParameterException e)
		{
			throw new ReaderException(failedMessage + "\n\t" + e.getMessage());
		}
		
    	return parameters;
    }
}
