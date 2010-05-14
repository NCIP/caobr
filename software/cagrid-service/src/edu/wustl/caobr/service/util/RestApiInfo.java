package edu.wustl.caobr.service.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * @author chandrakant_talele
 */
public class RestApiInfo {
    private static final Logger logger = Logger.getLogger(RestApiInfo.class);

    private static final Properties properties;

    //private static String resourceXmlPath="";

    static {
        properties = getPropertiesFromFile("caobr.properties");
    }

    //========================================================================
    public static String getResourceURL() {
        return properties.getProperty("resources.url");
    }

    public static String getConceptIdURL() {
        return properties.getProperty("conceptId.url");
    }

    public static String getOntologyDetailsURL() {
        return properties.getProperty("ontology.details.url");
    }

    public static String getOntologyURL() {
        return properties.getProperty("ontology.url");
    }

    public static String getObrResultURL() {
        return properties.getProperty("obr.result.url");
    }

    //========================================================================
    public static String getTagNameResourceRoot() {
        return properties.getProperty("tagname.resource.root");
    }

    public static String getTagNameResourceName() {
        return properties.getProperty("tagname.resource.name");
    }

    public static String getTagNameResourceDescription() {
        return properties.getProperty("tagname.resource.description");
    }

    public static String getTagNameResourceElementURL() {
        return properties.getProperty("tagname.resource.elementURL");
    }

    public static String getTagNameResourceLogo() {
        return properties.getProperty("tagname.resource.logo");
    }

    public static String getTagNameResourceId() {
        return properties.getProperty("tagname.resource.id");
    }

    public static String getTagNameResourceMainContext() {
        return properties.getProperty("tagname.resource.mainContext");
    }

    public static String getTagNameResourceContexts() {
        return properties.getProperty("tagname.resource.contexts");
    }

    public static String getTagNameResourceURL() {
        return properties.getProperty("tagname.resource.resourceURL");
    }
    
    
    //========================================================================
    public static String getTagNameOntologyRoot() {
        return properties.getProperty("tagname.ontology.root");
    }

    public static String getTagNameOntologyId() {
        return properties.getProperty("tagname.ontology.id");
    }

    public static String getTagNameOntologyLocalId() {
        return properties.getProperty("tagname.ontology.local.id");
    }

    public static String getTagNameOntologyAbbreviation() {
        return properties.getProperty("tagname.ontology.abbreviation");
    }

    public static String getTagNameOntologyDescription() {
        return properties.getProperty("tagname.ontology.abbreviation");
    }

    public static String getTagNameOntologyDisplayLabel() {
        return properties.getProperty("tagname.ontology.displayLabel");
    }

    public static String getTagNameOntologyFormat() {
        return properties.getProperty("tagname.ontology.format");
    }

    public static String getTagNameOntologyVersion() {
        return properties.getProperty("tagname.ontology.version");
    }

    
    
    /**
     * Loads properties from a property file present in classpath to java objects.
     * 
     * @param propertyfile Name of property file. 
     * @return Properties loaded from given file.
     */
    private static Properties getPropertiesFromFile(String propertyfile) {
        Properties properties = null;
        try {
            ClassLoader loader = RestApiInfo.class.getClassLoader();
            URL url = loader.getResource(propertyfile);

            InputStream is = url.openStream();
            if (is == null) {
                logger.info("Unable fo find property file in class path : " + propertyfile);
            }
            properties = new Properties();
            properties.load(is);
        } catch (IOException e) {
            logger.error("Unable to load properties from : " + propertyfile);
        }
        return properties;
    }
}